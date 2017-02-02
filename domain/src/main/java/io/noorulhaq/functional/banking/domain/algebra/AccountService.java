package io.noorulhaq.functional.banking.domain.algebra;

import javaslang.*;
import javaslang.control.Option;
import org.joda.time.DateTime;

/**
 * Created by Noor on 1/14/17.
 */
public interface AccountService<Account, Balance, Amount> {

    AccountOperation<Account> open(String no, String name, Option<DateTime> openDate);

    AccountOperation<Account> close(String no, Option<DateTime> closeDate);

    AccountOperation<Account> debit(String no, Amount amount);

    AccountOperation<Account> credit(String no, Amount amount);

    AccountOperation<Balance> balance(String no);

    default AccountOperation<Tuple2<Account, Account>> transfer(String from, String to, Amount amount) {

         return  debit(from,amount)
                .flatMap(debitAcc -> credit(to,amount)
                        .map(creditAcc ->
                                Tuple.of(debitAcc,creditAcc)));
    }
}
