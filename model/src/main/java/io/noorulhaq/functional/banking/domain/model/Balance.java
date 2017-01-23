package io.noorulhaq.functional.banking.domain.model;


import org.derive4j.*;
import java.math.BigDecimal;
import java.util.function.Function;
/**
 * Created by Noor on 1/17/17.
 */
@Data(value = @Derive(withVisibility = Visibility.Smart), flavour = Flavour.Javaslang)
public abstract class Balance {

    @ExportAsPublic
    static Balance balance(){ return Balances.Balance0(Amounts.amount(0d)); }

    @ExportAsPublic
    static Balance balance(Amount amount){ return Balances.Balance0(amount); }

    public abstract <R> R match(@FieldNames({"amount"})
                                        Function<Amount, R> Balance);

    public final Amount amount() {
        return Balances.getAmount(this);
    }

    public final Double value() {
        return Balances.getAmount(this).value();
    }

    public final BigDecimal preciseValue() {
        return Balances.getAmount(this).preciseValue();
    }

    @Override
    public abstract String toString();

}
