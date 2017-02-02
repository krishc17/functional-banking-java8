package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.banking.domain.model.Account;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.control.Option;

/**
 * Created by Noor on 1/14/17.
 */
public abstract class AccountRepository{

    public abstract Future<List<Account>> query();

    public abstract Future<Option<Account>> query(String no);

    public abstract Future<Account> store(Account account);

    public abstract void flush();

}
