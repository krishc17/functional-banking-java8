package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.util.TryReader;
import javaslang.control.Try;
import java.util.function.Function;

/**
 * Created by Noor on 1/21/17.
 */

public class AccountOperation<A> extends TryReader<AccountRepository, A> {

    public AccountOperation(Function<AccountRepository, Try<A>> run) {
        super(run);
    }

    @Override
    public <B> AccountOperation<B> map(Function<A, B> f) {
        return (AccountOperation) super.map(f);
    }

    @Override
    public <B> AccountOperation<B> flatMap(Function<A, TryReader<AccountRepository, B>> f) {
        return (AccountOperation) super.flatMap(f);
    }

    @Override
    public <B> TryReader<AccountRepository, B> tryReader(Function f) {
        return new AccountOperation(f);
    }
}
