import io.noorulhaq.functional.banking.domain.model.Amount;
import io.noorulhaq.functional.banking.domain.model.Balance;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Created by Noor on 1/15/17.
 */

public class Account {

    private String no;
    private String name;
    private DateTime dateOfOpening = DateTime.now();
    private DateTime dateOfClosing;
    private Balance balance = new io.noorulhaq.functional.banking.domain.model.Balance(new Amount(new BigDecimal(0)));

    public Account(String no, String name) {
        this.no = no;
        this.name = name;
    }

    public Account(String no, String name, DateTime dateOfOpening) {
        this.no = no;
        this.name = name;
        this.dateOfOpening = dateOfOpening;
    }

    public void setDateOfClosing(DateTime dateOfClosing) {
        this.dateOfClosing = dateOfClosing;
    }

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public DateTime getDateOfOpening() {
        return dateOfOpening;
    }

    public DateTime getDateOfClosing() {
        return dateOfClosing;
    }




}
