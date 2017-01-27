package io.noorulhaq.functional.banking.domain.model;

import javaslang.Function2;
import javaslang.Tuple2;
import javaslang.collection.List;
import org.derive4j.Data;
import org.derive4j.FieldNames;

/**
 * Created by Noor on 1/27/17.
 */
@Data
public abstract class ShareComputation {


    public abstract <R> R match(@FieldNames({"shareHolders", "totalCharges"})
                                        Function2<List<Tuple2<ShareHolder, Amount>>, Amount, R> ShareComputation);


    public final List<Tuple2<ShareHolder, Amount>> shareHolders(){
        return ShareComputations.getShareHolders(this);
    }

    public final Amount totalCharges(){
        return ShareComputations.getTotalCharges(this);
    }


    @Override
    public abstract String toString();


}
