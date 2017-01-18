package io.noorulhaq.functional.banking.domain.algebra;

import javaslang.control.Option;
import javaslang.control.Try;

/**
 * Created by Noor on 1/14/17.
 */
public interface Repository<A,ID> {

    public Try<Option<A>> query(ID id);

    public Try<A> store(A a);

}
