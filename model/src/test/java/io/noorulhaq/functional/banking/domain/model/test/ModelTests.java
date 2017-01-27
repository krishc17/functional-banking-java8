package io.noorulhaq.functional.banking.domain.model.test;


/**
 * Created by Noor on 1/17/17.
 */

import io.noorulhaq.functional.banking.domain.model.Accounts;
import io.noorulhaq.functional.banking.domain.model.Balances;
import javaslang.control.Option;
import org.junit.Test;
import static org.junit.Assert.*;
import static io.noorulhaq.functional.banking.domain.model.Amounts.*;

public class ModelTests {

    @Test
    public void amountCreation(){
      assertTrue(amount(10d).value()==10d);
    }

    @Test
    public void amountArithmetics(){
        assertTrue(amount(10d).add(amount(10d)).value()==20d);
        assertTrue(amount(10d).subtract(amount(10d)).value()==0d);
        assertTrue(amount(10d).divide(amount(10d)).value()==1d);
        assertTrue(amount(10d).multiply(amount(10d)).value()==100d);
    }

    @Test
    public void balanceCreation(){
       assertTrue(Balances.balance().value()==0d);
        assertTrue(Balances.balance(amount(10d)).value()==10d);
    }

    @Test
    public void accountCreation(){
        assertTrue(Accounts.account(null,"b", Option.none()).isFailure());
        assertTrue(Accounts.account("a","b",Option.none()).isSuccess());
        assertTrue(Accounts.account("a","b",Option.none()).get().isZeroBalance());
        assertTrue(Accounts.account("a","b",Option.none()).get().withBalance(Balances.balance(amount(10d))).balance().value()==10d);
    }
}
