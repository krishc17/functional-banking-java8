package io.noorulhaq.functional.banking.domain.test.stub;

import io.noorulhaq.functional.banking.domain.algebra.ShareHolderRepository;
import io.noorulhaq.functional.banking.domain.model.ShareHolder;
import javaslang.collection.List;
import javaslang.control.Try;
import java.util.HashMap;
import java.util.Map;
import static io.noorulhaq.functional.banking.domain.test.stub.ShareInMemoryHolderRepository.KVStore.*;

/**
 * Created by Noor on 1/26/17.
 */
public interface ShareInMemoryHolderRepository extends ShareHolderRepository {

    class KVStore{
        static Map<String, ShareHolder> shareStore = new HashMap();
    }

    default Try<List<ShareHolder>> query(){
        return Try.of(() -> List.ofAll(shareStore.values()));
    }

    default Try<ShareHolder> store(ShareHolder shareHolder) {

        return Try.of(()-> {
            shareStore.put(shareHolder.accountNo(), shareHolder);
            return  shareStore.get(shareHolder.accountNo());
        });
    }

    default void flush(){
        shareStore.clear();
    }

}
