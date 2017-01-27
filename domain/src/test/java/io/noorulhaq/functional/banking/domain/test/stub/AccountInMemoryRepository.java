package io.noorulhaq.functional.banking.domain.test.stub;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.model.Account;
import javaslang.collection.List;
import javaslang.control.Option;
import javaslang.control.Try;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noor on 1/18/17.
 */

public class AccountInMemoryRepository extends AccountRepository {

    private static Map<String, Account> accountStore = new HashMap();

    public Try<List<Account>> query(){
        return Try.of(() -> List.ofAll(accountStore.values()));
    }

    public Try<Option<Account>> query(String no) {
        return Try.of(() -> Option.of(accountStore.get(no)));
    }

    public Try<Account> store(Account account) {

        return Try.of(()-> {
            accountStore.put(account.no(), account);
            return  accountStore.get(account.no());
        });
    }

    public void flush(){
        accountStore.clear();
    }
}
