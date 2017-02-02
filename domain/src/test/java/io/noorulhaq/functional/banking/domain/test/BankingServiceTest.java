package io.noorulhaq.functional.banking.domain.test;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.test.stub.AccountInMemoryRepository;
import io.noorulhaq.functional.banking.domain.test.stub.ShareInMemoryHolderRepository;
import javaslang.test.Property;
import org.junit.Test;
import static io.noorulhaq.functional.banking.domain.test.Generators.*;

/**
 * Created by Noor on 1/26/17.
 */
public class BankingServiceTest {


    private AccountRepository accountRepository = new AccountInMemoryRepository();
    private ShareHolderRepository shareRepository = new ShareHolderRepository();
    private BankingServiceInterpreter bankingService = new BankingServiceInterpreter();

    @Test
    public void credit() {

        Property.def("Sum of amount credited to the account and all the shares should be equal to the actual amount requested for credit")
                .forAll(ARBITRARY_ACCOUNTS.apply(1000, accountRepository, bankingService),
                        ARBITRARY_SHARES.apply(1000, shareRepository, bankingService, accountRepository), ARBITRARY_AMOUNTS)
                .suchThat((account, share, amount) ->
                        bankingService.creditAcc(account.no(), amount)
                        .apply(shareRepository, accountRepository)
                                .map((acc) -> accountRepository.query().get()
                                        .map(a -> a.balance().amount())
                                        .reduce((a1, a2) -> a1.add(a2))
                                        .value().equals(amount.value()))
                        .transform((result)-> result.getOrElseTry(() -> {
                            if (result.isFailure()) result.failed().get().printStackTrace();
                            return false;
                        })))
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
