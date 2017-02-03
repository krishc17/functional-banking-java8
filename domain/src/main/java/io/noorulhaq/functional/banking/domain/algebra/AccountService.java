package io.noorulhaq.functional.banking.domain.algebra;

import javaslang.*;
import javaslang.control.Option;
import javaslang.control.Try;
import org.joda.time.DateTime;

/**
 * Created by Noor on 1/14/17.
 */
public interface AccountService<Account,Balance,Amount> {

    Try<Account> open(String no, String name, Option<DateTime> openDate, AccountRepository repository);

    Try<Account> close(String no, Option<DateTime> closeDate, AccountRepository repository);

    Try<Account> debit(String no, Amount amount, AccountRepository repository);

    Try<Account> credit(String no, Amount amount, AccountRepository repository);

    Try<Balance> balance(String no, AccountRepository repository);

    default Try<Tuple2<Account, Account>> transfer(String from, String to, Amount amount, AccountRepository repository) {

       return  debit(from,amount,repository)
               .flatMap(debitAcc -> credit(to,amount,repository)
                       .map(creditAcc -> Tuple.of(debitAcc,creditAcc)));
    }
}
