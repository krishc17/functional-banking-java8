package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.util.Reader;
import javaslang.concurrent.Future;

/**
 * Created by Noor on 1/26/17.
 */

public interface ShareCalculation<ShareComputation,Amount> {

     Reader<ShareHolderRepository, Future<ShareComputation>> computeShares(Amount amount);

}
