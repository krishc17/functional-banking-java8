package io.noorulhaq.functional.banking.domain.interpreter;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.algebra.AccountService;
import io.noorulhaq.functional.banking.domain.model.test.Account;
import io.noorulhaq.functional.banking.domain.model.test.Amount;
import io.noorulhaq.functional.banking.domain.model.test.Balance;
import io.noorulhaq.functional.util.Reader;
import javaslang.control.Option;
import javaslang.control.Try;
import org.joda.time.DateTime;
import static java.lang.String.format;
import static javaslang.API.*;
import static javaslang.Patterns.*;
import static io.noorulhaq.functional.banking.domain.model.test.Accounts.*;

/**
 * Created by Noor on 1/15/17.
 */
public class AccountServiceInterpreter extends AccountService<Account, Balance, Amount> {

    @Override
    public Reader<AccountRepository, Try<Account>> open(String no, String name, Option<DateTime> openDate) {
        return new Reader<>((repo) -> Match(repo.query(no)).of(
                Case(Success(Some($())), () -> Try.failure(new RuntimeException(format("Account already exists with account number %s.", no)))),
                Case(Success(None()), () -> account(no,name,openDate).flatMap(account -> repo.store(account))),
                Case(Failure($()), ex -> Try.failure(new RuntimeException("Failed to open account.", ex)))
        ));
    }

    @Override
    public Reader<AccountRepository, Try<Option<Account>>> close(String no, Option<DateTime> closeDate) {
        return new Reader<>((repo) -> Match(repo.query(no)).of(
                Case(Success(None()), () -> Try.failure(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Failure($()), ex -> Try.failure(new RuntimeException(format("Unable to close the account %s", no), ex))),
                Case(Success(Some($())), (acc) -> {
                    if(closeDate.getOrElse(DateTime.now()).isBefore(acc.get().dateOfOpening()))
                        return Try.<Option<Account>>failure(new RuntimeException(format("Date of closing cannot be before date of opening")));
                    else
                        return repo.store(acc.get().close(closeDate)).flatMap(account -> Try.of(()->Option.of(account)));
                })));
    }

    @Override
    public Reader<AccountRepository, Try<Option<Account>>> debit(String no, Amount amount) {
        return new Reader<>((repo) -> Match(repo.query(no)).of(
                Case(Success(None()), () -> Try.failure(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Failure($()), ex -> Try.failure(new RuntimeException(format("Unable to debit from account %s for %s", no, amount), ex))),
                Case(Success(Some($())), (acc) -> {
                    if (acc.get().balance().amount().subtract(amount).value() < 0)
                        return Try.<Option<Account>>failure(new RuntimeException(format("Insufficient account %s balance", no)));
                    else {
                        return repo.store(acc.get().debit(amount)).map(Option::of);
                        // return repo.store(acc.get().withBalance(Balances.balance(acc.get().balance().amount().add(amount)))).map(account -> Option.of(account));
                    }
                })
        ));
    }

    @Override
    public Reader<AccountRepository, Try<Option<Account>>> credit(String no, Amount amount) {
        return new Reader<>((repo) -> Match(repo.query(no)).of(
                Case(Success(None()), () -> Try.failure(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Failure($()), ex -> Try.failure(new RuntimeException(format("Unable to credit on account %s for %s", no, amount), ex))),
                Case(Success(Some($())), (acc) -> repo.store(acc.get().credit(amount)).map(Option::of))));
    }

    @Override
    public Reader<AccountRepository, Try<Option<Balance>>> balance(String no) {
        return new Reader<>((repo) -> Match(repo.query(no)).of(
                Case(Success(None()), () -> Try.failure(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Failure($()), ex -> Try.failure(new RuntimeException(format("Unable to retrieve balance of account %s", no), ex))),
                Case(Success(Some($())), (acc) -> Try.of( () -> Option.of(acc.get().balance()))
        )));
    }
}
