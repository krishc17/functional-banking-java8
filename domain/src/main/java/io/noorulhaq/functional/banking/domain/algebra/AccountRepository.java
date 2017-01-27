package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.banking.domain.model.Account;
import javaslang.collection.List;
import javaslang.control.Option;
import javaslang.control.Try;

/**
 * Created by Noor on 1/14/17.
 */
public abstract class AccountRepository{

    public abstract Try<List<Account>> query();

    public abstract Try<Option<Account>> query(String no);

    public abstract Try<Account> store(Account account);

    public abstract void flush();

}
