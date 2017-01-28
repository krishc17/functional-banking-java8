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

    public <B, RD extends Reader<R, B>> RD map(Function<A, B> f) {
        return reader(r -> f.apply((apply((R)r))));
    }

    public  <B, RD extends Reader<R, B>> RD  flatMap(Function<A, RD> f) {
        return reader(r -> f.apply(apply(r)).apply(r));
    }

    protected <B, RD extends Reader<R, B>> RD reader(Function<R, B> f) {
        return (RD)new Reader<>(f);
    }

    public A apply(R r) {
        return run.apply(r);
    }
}
