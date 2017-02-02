package io.noorulhaq.functional.banking.domain.algebra;

import io.noorulhaq.functional.util.FutureReader;
import javaslang.concurrent.Future;
import java.util.function.Function;

/**
 * Created by Noor on 1/21/17.
 */

public class AccountOperation<A> extends FutureReader<AccountRepository, A> {

    public AccountOperation(Function<AccountRepository, Future<A>> run) {
        super(run);
    }

    @Override
    public <B> AccountOperation<B> map(Function<A, B> f) {
        return (AccountOperation) super.map(f);
    }

    @Override
    public <B> AccountOperation<B> flatMap(Function<A, FutureReader<AccountRepository, B>> f) {
        return (AccountOperation) super.flatMap(f);
    }

    @Override
    public <B> FutureReader<AccountRepository, B> futureReader(Function f) {
        return new AccountOperation(f);
    }
}
