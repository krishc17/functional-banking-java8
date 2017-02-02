package io.noorulhaq.functional.banking.domain.algebra;


import javaslang.Function2;
import javaslang.concurrent.Future;

/**
 * Created by Noor on 1/28/17.
 */
public interface BankingService<Account,Balance, Amount,ShareComputation> extends AccountService<Account, Balance, Amount>, ShareCalculation<ShareComputation,Amount> {

    Function2<ShareHolderRepository, AccountRepository, Future<Account>>
    creditAcc(String account_no, Amount amount);

}
