package io.noorulhaq.functional.banking.domain.interpreter;

import io.noorulhaq.functional.banking.domain.algebra.AccountService;
import io.noorulhaq.functional.banking.domain.model.Account;
import io.noorulhaq.functional.banking.domain.model.Amount;
import io.noorulhaq.functional.banking.domain.model.Balance;
import io.noorulhaq.functional.banking.domain.repository.AccountRepository;
import io.noorulhaq.functional.util.Reader;
import javaslang.control.Option;
import javaslang.control.Try;
import org.joda.time.DateTime;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static javaslang.API.*;
import static javaslang.Patterns.*;
import static org.joda.time.DateTime.now;

/**
 * Created by Noor on 1/15/17.
 */
public class AccountServiceInterpreter extends AccountService<Account, Balance, Amount> {

    @Override
    public Reader<AccountRepository, Try<Account>> open(String no, String name, Option<DateTime> openDate) {
        return new Reader<>((repo) -> Match(repo.query(no)).of(
                Case(Success(Some($())), () -> Try.failure(new RuntimeException(format("io.noorulhaq.functional.banking.domain.model.Account already exists with account number %s.", no)))),
                Case(Success(None()), () -> {
                    DateTime today = now();
                    if (isNull(no) || isNull(name))
                        return Try.failure(new RuntimeException("io.noorulhaq.functional.banking.domain.model.Account cannot be opened blank."));
                    else if (openDate.getOrElse(today).isBefore(today))
                        return Try.failure(new RuntimeException("Cannot open account in the past."));
                    else return repo.store(new Account(no,name,openDate.getOrElse(now())));
                }),
                Case(Failure($()), ex -> Try.failure(new RuntimeException("Failed to open account.", ex)))
        ));
    }

    @Override
    public Reader<AccountRepository, Try<Option<Account>>> close(String no, Option<DateTime> closeDate) {
        return null;
    }

    @Override
    public Reader<AccountRepository, Try<Option<Account>>> debit(String no, Amount amount) {
        return new Reader<>((repo) -> Match(repo.query(no)).of(
                Case(Success(None()), () -> Try.failure(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Failure($()), ex -> Try.failure(new RuntimeException(format("Unable to debit from account %s for %s", no, amount), ex))),
                Case(Success(Some($())), (acc) -> {
                    if (acc.get().getBalance().getAmount().getValue().subtract(amount.getValue()).doubleValue() < 0)
                        return Try.<Option<Account>>failure(new RuntimeException(format("Insufficient account %s balance", no)));
                    else {
                        acc.get().setBalance(new Balance(new Amount(acc.get().getBalance().getAmount().getValue().subtract(amount.getValue()))));
                        repo.store(acc.get());
                        return Try.of(() -> Option.of(acc.get()));
                    }
                })
        ));
    }

    @Override
    public Reader<AccountRepository, Try<Option<Account>>> credit(String no, Amount amount) {
        return new Reader<>((repo) -> Match(repo.query(no)).of(
                Case(Success(None()), () -> Try.failure(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Failure($()), ex -> Try.failure(new RuntimeException(format("Unable to credit on account %s for %s", no, amount), ex))),
                Case(Success(Some($())), (acc) -> {
                    acc.get().setBalance(new Balance(new Amount(acc.get().getBalance().getAmount().getValue().add(amount.getValue()))));
                    repo.store(acc.get());
                    return Try.of(() -> Option.of(acc.get()));
                })
        ));
    }

    @Override
    public Reader<AccountRepository, Try<Option<Balance>>> balance(String no) {
        return new Reader<>((repo) -> Match(repo.query(no)).of(
                Case(Success(None()), () -> Try.failure(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Failure($()), ex -> Try.failure(new RuntimeException(format("Unable to retrieve balance of account %s", no), ex))),
                Case(Success(Some($())), (acc) -> Try.of( () -> Option.of(acc.get().getBalance()))
        )));
    }
}
