package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.util.Reader;
import javaslang.control.Try;

/**
 * Created by Noor on 1/26/17.
 */

public interface ShareCalculation<ShareComputation,Amount> {

     Reader<ShareHolderRepository, Try<ShareComputation>> computeShares(Amount amount);

}
