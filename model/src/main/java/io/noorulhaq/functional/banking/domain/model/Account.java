package io.noorulhaq.functional.banking.domain.model;

import static com.google.common.base.Strings.*;
import static org.joda.time.DateTime.now;
import javaslang.Function5;
import javaslang.control.Option;
import javaslang.control.Try;
import org.derive4j.*;
import org.joda.time.DateTime;


/**
 * Created by Noor on 1/17/17.
 */
@Data(value = @Derive(withVisibility = Visibility.Smart), flavour = Flavour.Javaslang)
public abstract class Account {

    @ExportAsPublic
    static Try<Account> account(String no, String name,Option<DateTime> openDate) {
            if (isNullOrEmpty(no) || isNullOrEmpty(name))
                return Try.failure(new RuntimeException("Account no or name cannot be blank."));
            else
                return  Try.of(()-> Accounts.Account0(no, name,Balances.balance(),openDate.getOrElse(now()),Option.none()));
    }

    public abstract <R> R match(@FieldNames({"no", "name", "balance","dateOfOpening","dateOfClosing"})
                                Function5<String, String, Balance, DateTime, Option<DateTime>, R> Account);


    public final boolean isZeroBalance(){
        return Accounts.getBalance(this).value().equals(0d);
    }

    public final Balance balance() {
        return Accounts.getBalance(this);
    }

    public final Account withBalance(Balance balance){
        return Accounts.setBalance0(balance).apply(this);
    }

    public final Account withCloseDate(Option<DateTime> dateOfClosing){ // close function alias
        return close(dateOfClosing);
    }

    public final Account debit(Amount amount){
        return Accounts.setBalance0(Balance.balance(Balances.getAmount(balance()).subtract(amount))).apply(this);
    }

    public final Account credit(Amount amount){
        return Accounts.setBalance0(Balance.balance(Balances.getAmount(balance()).add(amount))).apply(this);
    }

    public final DateTime dateOfOpening(){
        return Accounts.getDateOfOpening(this);
    }

    public final Account close(Option<DateTime> dateOfClosing){
        return Accounts.setDateOfClosing0(dateOfClosing).apply(this);
    }

    public final Option<DateTime> dateOfClosing(){
        return Accounts.getDateOfClosing(this);
    }

    public final String no() {
        return Accounts.getNo(this);
    }

    public final String name() {
        return Accounts.getName(this);
    }

    @Override
    public abstract String toString();

}
