package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.banking.domain.model.ShareHolder;
import javaslang.collection.List;
import javaslang.concurrent.Future;

/**
 * Created by Noor on 1/26/17.
 */
public interface ShareHolderRepository {

    Future<List<ShareHolder>> query();

    Future<ShareHolder> store(ShareHolder account);

    void flush();

}
