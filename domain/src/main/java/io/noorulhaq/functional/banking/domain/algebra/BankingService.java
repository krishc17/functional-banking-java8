package io.noorulhaq.functional.banking.domain.algebra;


import javaslang.Function4;
import javaslang.control.Option;
import javaslang.control.Try;

/**
 * Created by Noor on 1/28/17.
 */
public interface BankingService<Account, Amount> {

    Function4<? extends ShareCalculation,? extends  AccountService, ShareHolderRepository, AccountRepository, Try<Option<Account>>>
    credit(String account_no, Amount amount);

}
