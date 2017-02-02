package io.noorulhaq.functional.banking.domain.interpreter;

import io.noorulhaq.functional.banking.domain.algebra.AccountOperation;
import io.noorulhaq.functional.banking.domain.algebra.AccountService;
import io.noorulhaq.functional.banking.domain.model.*;
import javaslang.concurrent.Future;
import javaslang.control.Option;
import org.joda.time.DateTime;
import static java.lang.String.format;
import static javaslang.API.*;
import static javaslang.Patterns.*;
import static io.noorulhaq.functional.banking.domain.model.Accounts.*;
import static org.joda.time.DateTime.now;

/**
 * Created by Noor on 1/15/17.
 */
public interface AccountServiceInterpreter extends AccountService<Account, Balance, Amount> {

    @Override
    default AccountOperation<Account> open(String no, String name, Option<DateTime> openDate) {
        return new AccountOperation<>((repo) -> repo.query(no).flatMap(result -> Match(result).of(
                Case(Some($()),
                        (v) -> Future.<Account>failed(new RuntimeException(format("Account already exists with account number %s.", no)))),
                Case(None(), () -> {
                    DateTime today = now();
                    if(openDate.getOrElse(today).isBefore(today))
                        return Future.<Account>failed(new  RuntimeException("Cannot open account in the past."));
                    else
                        return  Future.fromTry(account(no,name,openDate)).flatMap(account -> repo.store(account));
                })
        )));
    }

    @Override
    default AccountOperation<Account> close(String no, Option<DateTime> closeDate) {
        return new AccountOperation<>((repo) -> repo.query(no).flatMap(result -> Match(result).of(
                Case(None(), () -> Future.<Account>failed(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Some($()), (acc) -> {
                    if(closeDate.getOrElse(DateTime.now()).isBefore(acc.dateOfOpening()))
                        return Future.<Account>failed(new RuntimeException(format("Date of closing cannot be before date of opening")));
                    else
                        return repo.store(acc.close(closeDate));
                }))));
    }

    @Override
    default AccountOperation<Account> debit(String no, Amount amount) {
        return new AccountOperation<>((repo) -> repo.query(no).flatMap(result -> Match(result).of(
                Case(None(), () -> Future.<Account>failed(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Some($()), (acc) -> {
                    if (acc.balance().amount().subtract(amount).value() < 0)
                        return Future.<Account>failed(new RuntimeException(format("Insufficient account %s balance", no)));
                    else
                        return repo.store(acc.withBalance(Balances.balance(acc.balance().amount().subtract(amount))));
                })
        )));
    }

    @Override
    default AccountOperation<Account> credit(String no, Amount amount) {
        return new AccountOperation<>((repo) -> repo.query(no).flatMap(result -> Match(result).of(
                Case(None(), () -> Future.<Account>failed(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Some($()), (acc) -> repo.store(acc.withBalance(Balances.balance(acc.balance().amount().add(amount)))))
        )));
    }

    @Override
    default AccountOperation<Balance> balance(String no) {
        return new AccountOperation<>((repo) -> repo.query(no).flatMap(result -> Match(result).of(
                Case(None(), () -> Future.<Balance>failed(new RuntimeException(format("No account found with account number %s.", no)))),
                Case(Some($()), (acc) -> Future.of( () -> acc.balance()))
        )));
    }
}
