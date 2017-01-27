package io.noorulhaq.functional.banking.domain.algebra;

import javaslang.*;
import javaslang.control.Option;
import org.joda.time.DateTime;

/**
 * Created by Noor on 1/14/17.
 */
public interface AccountService<Account,Balance,Amount> {

    AccountOperation<Account> open(String no, String name, Option<DateTime> openDate);

    AccountOperation<Option<Account>> close(String no, Option<DateTime> closeDate);

    AccountOperation<Option<Account>> debit(String no, Amount amount);

    AccountOperation<Option<Account>> credit(String no, Amount amount);

    AccountOperation<Option<Balance>> balance(String no);

    default AccountOperation<Option<Tuple2<Account, Account>>> transfer(String from, String to, Amount amount) {

        debit(from,amount)
                .flatMap(tDebitAcc -> credit(to,amount));


       return  debit(from,amount)
                .flatMap(tDebitAcc -> credit(to,amount)
                        .map(tCreditAcc -> tDebitAcc
                                .flatMap( oDebitAcc -> tCreditAcc
                                        .map( oCreditAcc -> oDebitAcc
                                                .flatMap( debitAcc -> oCreditAcc
                                                        .map( creditAcc -> Tuple.of(debitAcc,creditAcc)))))));
    }
}
