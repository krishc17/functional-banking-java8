package io.noorulhaq.functional.banking.domain.model.test;

import static com.google.common.base.Strings.*;
import static org.joda.time.DateTime.now;
import javaslang.Function5;
import javaslang.control.Option;
import javaslang.control.Try;
import org.derive4j.*;
import org.joda.time.DateTime;
import static io.noorulhaq.functional.banking.domain.model.test.Accounts.*;
import static io.noorulhaq.functional.banking.domain.model.test.Balances.*;


/**
 * Created by Noor on 1/17/17.
 */
@Data(value = @Derive(withVisibility = Visibility.Smart), flavour = Flavour.Javaslang)
public abstract class Account {

    @ExportAsPublic
    static Try<Account> account(String no, String name,Option<DateTime> openDate) {
            DateTime today = now();
            if (isNullOrEmpty(no) || isNullOrEmpty(name))
                return Try.failure(new RuntimeException("Account no or name cannot be blank."));
            else if(openDate.getOrElse(today).isBefore(today))
                return Try.failure(new  RuntimeException("Cannot open account in the past."));
            else
                return  Try.of(()->Account0(no, name,Balances.balance(),openDate.getOrElse(today),Option.none()));
    }

    public abstract <R> R match(@FieldNames({"no", "name", "balance","dateOfOpening","dateOfClosing"})
                                Function5<String, String, Balance, DateTime, Option<DateTime>, R> Account);


    public final boolean isZeroBalance(){
        return getBalance(this).value().equals(0d);
    }

    public final Balance balance() {
        return getBalance(this);
    }

    public final Account withBalance(Balance balance){
        return setBalance0(balance).apply(this);
    }

    public final Account debit(Amount amount){
        return setBalance0(Balance.balance(getAmount(balance()).subtract(amount))).apply(this);
    }

    public final Account credit(Amount amount){
        return setBalance0(Balance.balance(getAmount(balance()).add(amount))).apply(this);
    }

    public final DateTime dateOfOpening(){
        return getDateOfOpening(this);
    }

    public final Account close(Option<DateTime> dateOfClosing){
        return setDateOfClosing0(dateOfClosing).apply(this);
    }

    public final Option<DateTime> dateOfClosing(){
        return getDateOfClosing(this);
    }

    public final String no() {
        return getNo(this);
    }

    public final String name() {
        return getName(this);
    }

    @Override
    public abstract String toString();

}
