package io.noorulhaq.functional.banking.domain.model.test;


import org.derive4j.*;
import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Created by Noor on 1/17/17.
 */
@Data(value = @Derive(withVisibility = Visibility.Smart), flavour = Flavour.Javaslang)
public abstract class Amount {


    @ExportAsPublic
    static Amount amount(Double value) {
       return Amounts.Amount0(new BigDecimal((value==null)?0d:value));
    }

    @ExportAsPublic
    static Amount amount() {
        return Amounts.Amount0(new BigDecimal(0));
    }

    public abstract <R> R match(@FieldNames({"value"})
                                        Function<BigDecimal, R> Amount);

    public final Double value() {
        return Amounts.getValue(this).doubleValue();
    }

    public final BigDecimal preciseValue() {
        return Amounts.getValue(this);
    }

    public final Amount add(Amount amount){
        return Amounts.Amount0(Amounts.getValue(this).add(Amounts.getValue(amount)));
    }

    public final Amount subtract(Amount amount){
        return Amounts.Amount0(Amounts.getValue(this).subtract(Amounts.getValue(amount)));
    }


    public final Amount divide(Amount amount){
        return Amounts.Amount0(Amounts.getValue(this).divide(Amounts.getValue(amount)));
    }

    public final Amount multiply(Amount amount){
        return Amounts.Amount0(Amounts.getValue(this).multiply(Amounts.getValue(amount)));
    }

    @Override
    public abstract String toString();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

}
