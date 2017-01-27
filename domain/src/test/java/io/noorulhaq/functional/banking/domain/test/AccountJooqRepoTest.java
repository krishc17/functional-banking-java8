package io.noorulhaq.functional.banking.domain.test;

import static org.assertj.core.api.Assertions.*;
import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.interpreter.AccountJooqRepository;
import io.noorulhaq.functional.banking.domain.model.Account;
import io.noorulhaq.functional.banking.domain.model.Amounts;
import javaslang.control.Option;
import javaslang.control.Try;
import org.junit.After;
import org.junit.Test;


/**
 * Created by Noor on 1/24/17.
 */
public class AccountJooqRepoTest {

    private class AccountServiceInterpreter implements io.noorulhaq.functional.banking.domain.interpreter.AccountServiceInterpreter{}
    private AccountRepository repository = new AccountJooqRepository("sa", "", "jdbc:h2:./banking");
    private AccountServiceInterpreter accountService = new AccountServiceInterpreter();

    @Test
    public void consistency() {

        Try<Account> accountTry = accountService.open("Acc1", "Account Number#1", Option.none()).apply(repository);

        accountTry = accountTry.andThen(account -> {
            assertThat(account.balance().value()).isEqualTo(0d);
            assertThat(account.no()).isEqualTo("Acc1");
        }).andThen(account -> accountService.credit(account.no(), Amounts.amount(10d)).apply(repository)
                .andThen(creditedAccount -> {
                    assertThat(creditedAccount.isDefined()).isTrue();
                    assertThat(creditedAccount.get().balance().value()).isEqualTo(10d);
                })).andThen(account ->
                accountService.debit(account.no(), Amounts.amount(10d)).apply(repository)
                        .andThen(debitedAccount -> {
                            assertThat(debitedAccount.isDefined()).isTrue();
                            assertThat(debitedAccount.get().balance().value()).isEqualTo(0d);
                        }));

        assertThat(accountTry.isSuccess()).as(accountTry.get().toString()).isTrue();
    }

    @After
    public void tearDown() {
        repository.flush();
    }

}
