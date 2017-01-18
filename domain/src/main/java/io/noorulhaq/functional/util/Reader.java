package io.noorulhaq.functional.util;

import java.util.function.Function;

/**
 * Created by Noor on 1/15/17.
 */
public class Reader<R, A> {

    private final Function<R, A> run;

    public Reader(Function<R, A> run) {
        this.run = run;
    }

    public <B> Reader<R, B> map(Function<A, B> f) {
        return new Reader<>(r -> f.apply((apply(r))));
    }

    public <B> Reader<R, B> flatMap(Function<A, Reader<R, B>> f) {
        return new Reader<>(r -> f.apply(apply(r)).apply(r));
    }

    public A apply(R r) {
        return run.apply(r);
    }
}
