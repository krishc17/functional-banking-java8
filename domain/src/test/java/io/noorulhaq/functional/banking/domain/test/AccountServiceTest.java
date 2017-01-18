package io.noorulhaq.functional.banking.domain.test;


import io.noorulhaq.functional.banking.domain.interpreter.AccountServiceInterpreter;
import io.noorulhaq.functional.banking.domain.model.test.Account;
import io.noorulhaq.functional.banking.domain.model.test.Amounts;
import io.noorulhaq.functional.banking.domain.test.stub.AccountInMemoryRepository;
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

    private AccountServiceInterpreter accountService = new AccountServiceInterpreter();
    private AccountInMemoryRepository repository = new AccountInMemoryRepository();

    @Test
    public void equalDebitAndCredit() {

        Property.def("Equal credit & debit in sequence retain the same balance")
                .forAll(ARBITRARY_ACCOUNTS.apply(1000, repository, accountService), ARBITRARY_AMOUNTS)
                .suchThat((account, amount) -> accountService.balance(account.get().no()).apply(repository)
                        .flatMap((initialBalance) -> accountService.credit(account.get().no(), amount).apply(repository)
                                .flatMap((creditAccount) -> accountService.debit(account.get().no(), amount).apply(repository)
                                        .map((debitAccount) ->
                                                debitAccount.get().balance().amount().equals(initialBalance.get().amount())))).getOrElse(false))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void balancedLedgerAfterTransfer() {

        Arbitrary<Try<Account>> arbitraryAcc1 = ARBITRARY_ACCOUNTS.apply(1000, repository, accountService);
        Arbitrary<Try<Account>> arbitraryAcc2 = ARBITRARY_ACCOUNTS.apply(2000, repository, accountService);

        Property.def("Ledger should be balanced after amount transfer")
                .forAll(arbitraryAcc1, arbitraryAcc2, ARBITRARY_AMOUNTS)
                .suchThat((debitAccount, creditAccount, amount) ->
                        Match(accountService.credit(debitAccount.get().no(), amount)
                                .flatMap((a) -> accountService.transfer(debitAccount.get().no(), creditAccount.get().no(), amount)).apply(repository)).of(
                                Case(Success(Some($())), (accounts) ->
                                        accounts.get()._1.balance().amount().equals(Amounts.amount())
                                                && accounts.get()._2.balance().amount().equals(amount)),
                                Case($(), () -> false)))
                .check()
                .assertIsSatisfied();
    }

    @After
    public void tearDown(){
        repository.flush();
    }
}
