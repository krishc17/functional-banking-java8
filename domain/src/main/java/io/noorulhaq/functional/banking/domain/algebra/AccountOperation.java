package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.util.Reader;
import javaslang.control.Try;
import java.util.function.Function;

/**
 * Created by Noor on 1/21/17.
 */
public final class AccountOperation<A> extends Reader<AccountRepository, Try<A>> {

    public AccountOperation(Function<AccountRepository, Try<A>> run) {
        super(run);
    }

    protected <B, RD extends Reader<AccountRepository, B>> RD reader(Function<AccountRepository, B> f) {
        return (RD)new AccountOperation(f);
    }
}
