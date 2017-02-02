package io.noorulhaq.functional.util;


import javaslang.concurrent.Future;
import java.util.function.Function;

/**
 * Created by Noor on 1/31/17.
 */
public class FutureReader<R, A> {

    private final Function<R, Future<A>> run;

    public FutureReader(Function<R, Future<A>> run) {
        this.run = run;
    }

    public <B> FutureReader<R, B> map(Function<A, B> f) {
        return futureReader(r -> apply((R) r).map(f::apply));
    }

    public <B> FutureReader<R, B> flatMap(Function<A, FutureReader<R, B>> f) {
        return futureReader(r -> apply((R) r).flatMap(a -> f.apply(a).apply((R)r)));
    }

    public <B> FutureReader<R, B> lift(Function<A, Reader<R, B>> f) {
        return futureReader(r -> apply((R)r).map(a -> f.apply(a).apply((R)r)));
    }


    public <B> FutureReader<R, B> futureReader(Function f) {
        return new FutureReader(f);
    }


    public Future<A> apply(R r) {
        return run.apply(r);
    }

}
