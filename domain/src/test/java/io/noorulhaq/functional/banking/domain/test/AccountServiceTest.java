package io.noorulhaq.functional.banking.domain.test;


import io.noorulhaq.functional.banking.domain.algebra.AccountService;
import io.noorulhaq.functional.banking.domain.interpreter.AccountServiceInterpreter;
import io.noorulhaq.functional.banking.domain.model.test.Account;
import io.noorulhaq.functional.banking.domain.model.test.Amount;
import io.noorulhaq.functional.banking.domain.model.test.Amounts;
import io.noorulhaq.functional.banking.domain.model.test.Balance;
import io.noorulhaq.functional.banking.domain.test.stub.AccountInMemoryRepository;
import javaslang.Tuple2;
import javaslang.control.Try;
import javaslang.test.Arbitrary;
import javaslang.test.Property;
import org.junit.After;
import org.junit.Test;
import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.Patterns.*;
import static io.noorulhaq.functional.banking.domain.test.Generators.*;

/**
 * Created by Noor on 1/18/17.
 */
public class AccountServiceTest {


    private class AccountService<Account,Balance,Amount> implements AccountServiceInterpreter{}
    private AccountService<Account,Balance,Amount> accountService = new AccountService();
    private AccountInMemoryRepository repository = new AccountInMemoryRepository();

    @Test
    public void equalDebitAndCredit() {

        ARBITRARY_ACCOUNTS.andThen((o)->ARBITRARY_ACCOUNTS).apply(1,repository,accountService);

        Property.def("Equal credit & debit in sequence retain the same balance")
                .forAll(ARBITRARY_ACCOUNTS.apply(1000, repository, accountService), ARBITRARY_AMOUNTS)
                .suchThat((account, amount) -> accountService.balance(account.get().no(),repository)
                        .flatMap((initialBalance) -> accountService.credit(account.get().no(), amount,repository)
                                .flatMap((creditAccount) -> accountService.debit(account.get().no(), amount,repository)
                                        .map((debitAccount) ->
                                                debitAccount.balance().amount().equals(initialBalance.amount())))).get())
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void balancedLedgerAfterTransfer() {

        Arbitrary<Try<Account>> arbitraryAcc1 = ARBITRARY_ACCOUNTS.apply(1000, repository, accountService);
        Arbitrary<Try<Account>> arbitraryAcc2 = ARBITRARY_ACCOUNTS.apply(2000, repository, accountService);

        Property.def("Ledger should be balanced after account transfer")
                .forAll(arbitraryAcc1, arbitraryAcc2, ARBITRARY_AMOUNTS)
                .suchThat((debitAccount, creditAccount, amount) ->
                        accountService.credit(debitAccount.get().no(), amount,repository)
                                .flatMap((a) -> accountService.transfer(debitAccount.get().no(), creditAccount.get().no(), amount,repository))
                                .transform((accts)-> Match(accts).of(
                                        Case(Success($()),
                                                (accounts) -> ((Tuple2<Account,Account>)accounts)._1.balance().amount().equals(Amounts.zero())
                                                        && ((Tuple2<Account,Account>)accounts)._2.balance().amount().equals(amount)),
                                        Case($(), () -> false))))
                .check()
                .assertIsSatisfied();
    }

    @After
    public void tearDown(){
        repository.flush();
    }
}
