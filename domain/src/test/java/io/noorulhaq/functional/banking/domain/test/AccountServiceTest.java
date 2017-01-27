package io.noorulhaq.functional.banking.domain.test;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.model.Account;
import io.noorulhaq.functional.banking.domain.model.Amounts;
import io.noorulhaq.functional.banking.domain.test.stub.AccountInMemoryRepository;
import javaslang.control.Option;
import javaslang.control.Try;
import javaslang.test.Arbitrary;
import javaslang.test.Property;
import org.junit.After;
import org.junit.Assert;
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

    private class AccountServiceInterpreter implements io.noorulhaq.functional.banking.domain.interpreter.AccountServiceInterpreter{}

    private AccountServiceInterpreter accountService = new AccountServiceInterpreter();
    //private AccountRepository repository = new AccountJooqRepository("sa",  "", "jdbc:h2:./banking");

    private AccountRepository repository = new AccountInMemoryRepository();

    @Test
    public void equalDebitAndCredit() {

        Property.def("Equal credit & debit in sequence retain the same balance")
                .forAll(ARBITRARY_ACCOUNTS.apply(1000, repository, accountService), ARBITRARY_AMOUNTS)
                .suchThat((account, amount) -> accountService.balance(account.get().no())
                        .flatMap((initialBalance) -> accountService.credit(account.get().no(), amount)
                                .flatMap((creditAccount) -> accountService.debit(account.get().no(), amount)
                                        .map(debitAccount -> initialBalance.get().get().amount().equals(debitAccount.get().get().balanceAmount()))))
                        .apply(repository))
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
                        accountService.credit(debitAccount.get().no(), amount)
                                .flatMap((a) -> accountService.transfer(debitAccount.get().no(), creditAccount.get().no(), amount)).apply(repository)
                                .transform((accts)-> Match(accts).of(
                                        Case(Success(Some($())),
                                                (accounts) -> accounts.get()._1.balance().amount().equals(Amounts.zero())
                                                        && accounts.get()._2.balance().amount().equals(amount)),
                                        Case(Failure($()), (throwable) -> {throwable.printStackTrace(); return false;}))))
                .check()
                .assertIsSatisfied();
    }


    @Test
    public void readerComposition(){

       Assert.assertTrue(accountService.open("Acc1","Acc1", Option.none()).apply(repository)
               .map((acc1) -> accountService.credit("Hox",Amounts.amount(10d))
                       .flatMap(crdAcc -> accountService.debit(acc1.no(),Amounts.amount(10d)))
                       .apply(repository)).isSuccess());
    }



    @After
    public void tearDown(){
        repository.flush();
    }
}