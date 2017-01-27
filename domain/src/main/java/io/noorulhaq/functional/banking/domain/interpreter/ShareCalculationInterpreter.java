package io.noorulhaq.functional.banking.domain.interpreter;

import io.noorulhaq.functional.banking.domain.algebra.ShareCalculation;
import io.noorulhaq.functional.banking.domain.algebra.ShareHolderRepository;
import io.noorulhaq.functional.banking.domain.model.*;
import io.noorulhaq.functional.util.Reader;
import javaslang.Tuple;
import javaslang.control.Try;

/**
 * Created by Noor on 1/26/17.
 */
public interface ShareCalculationInterpreter extends ShareCalculation<ShareComputation, Amount> {

    default Reader<ShareHolderRepository, Try<ShareComputation>> computeShares(Amount amount) {

        return new Reader<>(repo -> repo.query()
                .map(shareHolders -> shareHolders
                        .map(share -> Tuple.of(share, amount.multiply(Amounts.amount(share.percentage()))))
                        .transform(computedShares ->
                                ShareComputations.ShareComputation(computedShares,computedShares
                                        .map(share -> share._2).reduce((sh1,sh2)->sh1.add(sh2))))));
    }

}
