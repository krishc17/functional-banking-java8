package io.noorulhaq.functional.banking.domain.test;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.algebra.ShareHolderRepository;
import io.noorulhaq.functional.banking.domain.interpreter.AccountServiceInterpreter;
import static io.noorulhaq.functional.banking.domain.model.ShareHolders.*;
import io.noorulhaq.functional.banking.domain.model.*;
import javaslang.Function2;
import javaslang.Function3;
import javaslang.Function4;
import javaslang.concurrent.Future;
import javaslang.control.Option;
import javaslang.test.Arbitrary;
import javaslang.test.Gen;
import java.util.Random;

/**
 * Created by Noor on 1/18/17.
 */
public interface Generators {

    Random RANDOM = new Random();
    Function2<Double,Double,Double> randomDecimal = (rangeMin,rangeMax) -> rangeMin + (rangeMax - rangeMin) * RANDOM.nextDouble();

    Arbitrary<Amount> ARBITRARY_AMOUNTS =  Gen.choose(10,1000).map((random)-> Amounts.amount(random.doubleValue())).arbitrary();


    Function3<Integer,AccountRepository,AccountServiceInterpreter,Arbitrary<Account>> ARBITRARY_ACCOUNTS =
            ((seed,accountRepository, accountServiceInterpreter) ->
                    Gen.of(seed,(last)->last+1).map((id)->  accountServiceInterpreter.open("Account#"+id,"Random Account#"+id, Option.none())
                            .apply(accountRepository).get() ).arbitrary());


    Function4<Integer,ShareHolderRepository,AccountServiceInterpreter,AccountRepository,Arbitrary<Future<ShareHolder>>> ARBITRARY_SHARES =
            ((seed, shareHolderRepository, accountServiceInterpreter, accountRepository) ->
                Gen.of(seed,(last)->last+1)
                        .map((id) -> ShareHolder("ShareHolder#"+id, randomDecimal.apply(0.01d,0.1d)))
                        .map(share -> {
                           accountServiceInterpreter.open(share.accountNo(),share.accountNo(), Option.none()).apply(accountRepository);
                            return shareHolderRepository.store(share);
                        }).arbitrary());

}
