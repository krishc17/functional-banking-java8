package io.noorulhaq.functional.banking.domain.test;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.model.Account;
import io.noorulhaq.functional.banking.domain.model.Amounts;
import io.noorulhaq.functional.banking.domain.test.stub.AccountInMemoryRepository;
import javaslang.concurrent.Future;
import javaslang.control.Option;
import javaslang.test.Arbitrary;
import javaslang.test.Property;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import static io.noorulhaq.functional.banking.domain.test.Generators.*;

/**
 * Created by Noor on 1/18/17.
 */
public class AccountServiceTest {

    private class AccountServiceInterpreter implements io.noorulhaq.functional.banking.domain.interpreter.AccountServiceInterpreter {
    }

    private AccountServiceInterpreter accountService = new AccountServiceInterpreter();
    //private AccountRepository repository = new AccountJooqRepository("sa",  "", "jdbc:h2:./banking");

    private AccountRepository repository = new AccountInMemoryRepository();

    @Test
    public void equalDebitAndCredit() {

        Property.def("Equal credit & debit in sequence retain the same balance")
                .forAll(ARBITRARY_ACCOUNTS.apply(1000, repository, accountService), ARBITRARY_AMOUNTS)
                .suchThat((account, amount) -> accountService.balance(account.no())
                        .flatMap((initialBalance) -> accountService.credit(account.no(), amount)
                                .flatMap((creditAccount) -> accountService.debit(account.no(), amount)
                                        .map(debitAccount -> initialBalance.amount().equals(debitAccount.balanceAmount()))))
                        .apply(repository)
                        .transform(result -> result.getOrElseTry(() -> {
                            if (result.isFailure()) result.failed().get().printStackTrace();
                            return false;
                        })))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void balancedLedgerAfterTransfer() {

        Arbitrary<Account> arbitraryAcc1 = ARBITRARY_ACCOUNTS.apply(1000, repository, accountService);
        Arbitrary<Account> arbitraryAcc2 = ARBITRARY_ACCOUNTS.apply(2000, repository, accountService);

        Property.def("Ledger should be balanced after amount transfer")
                .forAll(arbitraryAcc1, arbitraryAcc2, ARBITRARY_AMOUNTS)
                .suchThat((debitAccount, creditAccount, amount) ->
                        accountService.credit(debitAccount.no(), amount)
                                .flatMap((a) -> accountService.transfer(debitAccount.no(), creditAccount.no(), amount)
                                        .map(accounts -> accounts._1.balance().amount().equals(Amounts.zero()) && accounts._2.balance().amount().equals(amount)))
                                .apply(repository)
                                .transform(result -> result.getOrElseTry(() -> {
                                    if (result.isFailure()) result.failed().get().printStackTrace();
                                    return false;
                                })))
                .check()
                .assertIsSatisfied();
    }


    @Test
    public void readerComposition() {

        Future<Account> account =
                accountService.open("Acc1", "Acc1", Option.none())
                        .flatMap(acc -> accountService.credit("Acc1", Amounts.amount(10d))
                                .flatMap(cAcc -> accountService.debit("Acc1", Amounts.amount(5d))
                                        .flatMap(dAcc -> accountService.debit("Acc1", Amounts.amount(5d)))))
                        .apply(repository);
        Assert.assertTrue(account.get().isZeroBalance());
    }


    @After
    public void tearDown() {
        repository.flush();
    }
}