package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.util.Reader;
import javaslang.*;
import javaslang.control.Option;
import javaslang.control.Try;
import org.joda.time.DateTime;

/**
 * Created by Noor on 1/14/17.
 */
public abstract class AccountService<Account,Balance,Amount> {

    public abstract Reader<AccountRepository, Try<Account>> open(String no, String name, Option<DateTime> openDate);

    public abstract Reader<AccountRepository, Try<Option<Account>>> close(String no, Option<DateTime> closeDate);

    public abstract Reader<AccountRepository, Try<Option<Account>>> debit(String no, Amount amount);

    public abstract Reader<AccountRepository, Try<Option<Account>>> credit(String no, Amount amount);

    public abstract Reader<AccountRepository, Try<Option<Balance>>> balance(String no);

    public Reader<AccountRepository, Try<Option<Tuple2<Account, Account>>>> transfer(String from, String to, Amount amount) {

       return  debit(from,amount)
                .flatMap(tDebitAcc -> credit(to,amount)
                        .map(tCreditAcc -> tDebitAcc
                                .flatMap( oDebitAcc -> tCreditAcc
                                        .map( oCreditAcc -> oDebitAcc
                                                .flatMap( debitAcc -> oCreditAcc
                                                        .map( creditAcc -> Tuple.of(debitAcc,creditAcc)))))));
    }
}
