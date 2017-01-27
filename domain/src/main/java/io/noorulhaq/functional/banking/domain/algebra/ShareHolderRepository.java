package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.banking.domain.model.ShareHolder;
import javaslang.collection.List;
import javaslang.control.Try;

/**
 * Created by Noor on 1/26/17.
 */
public interface ShareHolderRepository {

    Try<List<ShareHolder>> query();

    Try<ShareHolder> store(ShareHolder account);

    void flush();

}
