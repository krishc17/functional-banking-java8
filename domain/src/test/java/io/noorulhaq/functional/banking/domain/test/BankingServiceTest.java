package io.noorulhaq.functional.banking.domain.test;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.test.stub.AccountInMemoryRepository;
import io.noorulhaq.functional.banking.domain.test.stub.ShareInMemoryHolderRepository;
import javaslang.test.Property;
import org.junit.Test;
import static io.noorulhaq.functional.banking.domain.test.Generators.*;
import static javaslang.API.*;
import static javaslang.Patterns.*;


/**
 * Created by Noor on 1/26/17.
 */
public class BankingServiceTest {


    private AccountRepository accountRepository = new AccountInMemoryRepository();
    private AccountServiceInterpreter accountService = new AccountServiceInterpreter();
    private ShareCalculationInterpreter shareCalculationService = new ShareCalculationInterpreter();
    private ShareHolderRepository shareRepository = new ShareHolderRepository();
    private BankingServiceInterpreter bankingService = new BankingServiceInterpreter();

    @Test
    public void credit() {

        Property.def("Sum of amount credited to the account and all the shares should be equal to the actual amount requested for credit")
                .forAll(ARBITRARY_ACCOUNTS.apply(1000, accountRepository, accountService),
                        ARBITRARY_SHARES.apply(1000, shareRepository, accountService, accountRepository), ARBITRARY_AMOUNTS)
                .suchThat((account, share, amount) ->
                        bankingService.credit(account.get().no(), amount)
                        .apply(shareCalculationService, accountService, shareRepository, accountRepository)
                        .transform((primeAcc)-> Match(primeAcc).of(
                                Case(Success($()), (acc) ->
                                        accountRepository.query().get()
                                                .map(a -> a.balance().amount())
                                                .reduce((a1, a2) -> a1.add(a2))
                                                .value().equals(amount.value())),
                                Case(Failure($()), (throwable) -> {throwable.printStackTrace(); return false;}))))
                .check(0, 1)
                .assertIsSatisfied();
    }


    private class BankingServiceInterpreter implements io.noorulhaq.functional.banking.domain.interpreter.BankingServiceInterpreter{
    }

    private class AccountServiceInterpreter implements io.noorulhaq.functional.banking.domain.interpreter.AccountServiceInterpreter {
    }

    private class ShareCalculationInterpreter implements io.noorulhaq.functional.banking.domain.interpreter.ShareCalculationInterpreter {
    }

    private class ShareHolderRepository implements ShareInMemoryHolderRepository {
    }

}
