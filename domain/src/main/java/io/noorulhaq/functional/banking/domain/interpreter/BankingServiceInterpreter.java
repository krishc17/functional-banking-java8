package io.noorulhaq.functional.banking.domain.interpreter;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.algebra.BankingService;
import io.noorulhaq.functional.banking.domain.algebra.ShareHolderRepository;
import io.noorulhaq.functional.banking.domain.model.Account;
import io.noorulhaq.functional.banking.domain.model.Amount;
import io.noorulhaq.functional.banking.domain.model.Balance;
import io.noorulhaq.functional.banking.domain.model.ShareComputation;
import javaslang.Function2;
import javaslang.concurrent.Future;

/**
 * Created by Noor on 1/28/17.
 */
public interface BankingServiceInterpreter extends BankingService<Account,Balance,Amount,ShareComputation>,
                                                    AccountServiceInterpreter,
                                                    ShareCalculationInterpreter {

    default Function2<ShareHolderRepository, AccountRepository, Future<Account>>
    creditAcc(String account_no, Amount amount) {
        return (Function2<ShareHolderRepository, AccountRepository, Future<Account>>)
                (shareRepository, accountRepository) ->
                        computeShares(amount).apply(shareRepository)
                                .flatMap(shareComputation ->
                                        shareComputation.shareHolders()
                                                .map(shareHolder -> credit(shareHolder._1.accountNo(), shareHolder._2))
                                                .reduce((accountOperation1, accountOperation2) -> accountOperation1.flatMap((shareHolderAcc) -> accountOperation2))
                                                .flatMap((shareHolderAcc) -> credit(account_no, amount.subtract(shareComputation.totalCharges())))
                                                .apply(accountRepository));

    }

}
