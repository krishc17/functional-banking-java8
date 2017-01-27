package io.noorulhaq.functional.banking.domain.interpreter;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.algebra.BankingService;
import io.noorulhaq.functional.banking.domain.algebra.ShareHolderRepository;
import io.noorulhaq.functional.banking.domain.model.Account;
import io.noorulhaq.functional.banking.domain.model.Amount;
import javaslang.Function4;
import javaslang.control.Option;
import javaslang.control.Try;

/**
 * Created by Noor on 1/28/17.
 */
public interface BankingServiceInterpreter extends BankingService<Account, Amount> {

    default Function4<ShareCalculationInterpreter, AccountServiceInterpreter, ShareHolderRepository, AccountRepository, Try<Option<Account>>>
    credit(String account_no, Amount amount) {
        return (Function4<ShareCalculationInterpreter, AccountServiceInterpreter, ShareHolderRepository, AccountRepository, Try<Option<Account>>>)
                (shareCalculationService, accountService, shareRepository, accountRepository) ->
                        shareCalculationService.computeShares(amount).apply(shareRepository)
                                .flatMap(shareComputation ->
                                        shareComputation.shareHolders()
                                                .map(shareHolder -> accountService.credit(shareHolder._1.accountNo(), shareHolder._2))
                                                .reduce((accountOperation1, accountOperation2) -> accountOperation1.flatMap((shareHolderAcc) -> accountOperation2))
                                                .flatMap((shareHolderAcc) -> accountService.credit(account_no, amount.subtract(shareComputation.totalCharges())))
                                                .apply(accountRepository));

    }

}
