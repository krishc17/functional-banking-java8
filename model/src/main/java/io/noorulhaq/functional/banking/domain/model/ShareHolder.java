package io.noorulhaq.functional.banking.domain.model;

import javaslang.Function2;
import org.derive4j.*;

/**
 * Created by Noor on 1/26/17.
 */

@Data
public abstract class ShareHolder {

    public abstract <R> R match(@FieldNames({"account_no", "percentage"})
                                        Function2<String, Double, R> ShareHolder);


    public final String accountNo() {
        return ShareHolders.getAccount_no(this);
    }


    public final Double percentage() {
        return ShareHolders.getPercentage(this);
    }

    @Override
    public abstract String toString();

}
