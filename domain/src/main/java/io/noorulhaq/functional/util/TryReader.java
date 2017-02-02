package io.noorulhaq.functional.util;

import javaslang.control.Try;
import java.util.function.Function;

/**
 * Created by Noor on 1/28/17.
 */
public class TryReader<R, A> {

    private final Function<R, Try<A>> run;

    public TryReader(Function<R, Try<A>> run) {
        this.run = run;
    }

    public <B> TryReader<R, B> map(Function<A, B> f) {
        return tryReader(r -> apply((R) r).map(f::apply));
    }

    public <B> TryReader<R, B> flatMap(Function<A, TryReader<R, B>> f) {
        return tryReader(r -> apply((R) r).flatMap(a -> f.apply(a).apply((R)r)));
    }

    public <B> TryReader<R, B> lift(Function<A, Reader<R, B>> f) {
        return tryReader(r -> apply((R)r).map(a -> f.apply(a).apply((R)r)));
    }

    public <B> TryReader<R, B> tryReader(Function f) {
        return new TryReader(f);
    }

    public Try<A> apply(R r) {
        return run.apply(r);
    }
}
