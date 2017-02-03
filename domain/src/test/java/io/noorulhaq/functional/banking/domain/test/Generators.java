package io.noorulhaq.functional.banking.domain.test;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.interpreter.AccountServiceInterpreter;
import io.noorulhaq.functional.banking.domain.model.test.Account;
import io.noorulhaq.functional.banking.domain.model.test.Amount;
import io.noorulhaq.functional.banking.domain.model.test.Amounts;
import javaslang.Function3;
import javaslang.control.Option;
import javaslang.control.Try;
import javaslang.test.Arbitrary;
import javaslang.test.Gen;

/**
 * Created by Noor on 1/18/17.
 */
public interface Generators {


    Arbitrary<Amount> ARBITRARY_AMOUNTS =  Gen.choose(10,1000).map((random)-> Amounts.amount(random.doubleValue())).arbitrary();


    Function3<Integer,AccountRepository,AccountServiceInterpreter,Arbitrary<Try<Account>>> ARBITRARY_ACCOUNTS =
            ((seed,accountRepository, accountServiceInterpreter) ->
                    Gen.of(seed,(last)->last+1).map((id)->  accountServiceInterpreter.open("Account#"+id,"Random Account#"+id, Option.none(),accountRepository))
                            .arbitrary());





}
