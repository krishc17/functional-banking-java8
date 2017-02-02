package io.noorulhaq.functional.banking.domain.test.stub;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.model.Account;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.control.Option;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noor on 1/18/17.
 */

public class AccountInMemoryRepository extends AccountRepository {

    private static Map<String, Account> accountStore = new HashMap();

    public Future<List<Account>> query(){
        return Future.of(() -> List.ofAll(accountStore.values()));
    }

    public Future<Option<Account>> query(String no) {
        return Future.of(() -> Option.of(accountStore.get(no)));
    }

    public Future<Account> store(Account account) {

        return Future.of(()-> {
            accountStore.put(account.no(), account);
            return  accountStore.get(account.no());
        });
    }

    public void flush(){
        accountStore.clear();
    }
}
