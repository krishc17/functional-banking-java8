package io.noorulhaq.functional.banking.domain.model.test;

import java.lang.Override;
import java.lang.String;
import java.util.function.Function;
import java.util.function.Supplier;
import javaslang.Function1;

public final class Balances {
  private static Balance balance;

  private Balances() {
  }

  public static Balance balance() {
    Balance _balance = balance;
    if (_balance == null) {
      balance = _balance = Balance.balance();
    }
    return _balance;
  }

  public static Balance balance(Amount amount) {
    return Balance.balance(amount);
  }

  static Balance Balance0(Amount amount) {
    return new Balance_(amount);
  }

  public static Balance lazy(Supplier<Balance> balance) {
    return new Lazy(balance);
  }

  public static CasesMatchers.TotalMatcher_Balance cases() {
    return CasesMatchers.totalMatcher_Balance;
  }

  public static Amount getAmount(Balance balance) {
    return balance.match((amount) -> amount);
  }

  static Function1<Balance, Balance> setAmount0(Amount newAmount) {
    return modAmount0(__ -> newAmount);
  }

  static Function1<Balance, Balance> modAmount0(Function1<Amount, Amount> amountMod) {
    return balance -> balance.match((amount) -> Balance0(amountMod.apply(amount)));
  }

  private static final class Balance_ extends Balance {
    private final Amount amount;

    Balance_(Amount amount) {
      this.amount = amount;
    }

    @Override
    public <R> R match(Function<Amount, R> Balance) {
      return Balance.apply(this.amount);
    }

    @Override
    public String toString() {
      return "Balance(" + this.amount + ")";
    }
  }

  private static final class Lazy extends Balance {
    private volatile Supplier<Balance> expression;

    private Balance evaluation;

    Lazy(Supplier<Balance> balance) {
      this.expression = balance;
    }

    private synchronized Balance _evaluate() {
      Supplier<Balance> e = expression;
      if (e != null) {
        evaluation = e.get();
        expression = null;
      }
      return evaluation;
    }

    @Override
    public <R> R match(Function<Amount, R> Balance) {
      return (this.expression == null ? this.evaluation : _evaluate()).match(Balance);
    }

    @Override
    public String toString() {
      return (this.expression == null ? this.evaluation : _evaluate()).toString();
    }
  }

  public static class CasesMatchers {
    private static final TotalMatcher_Balance totalMatcher_Balance = new TotalMatcher_Balance();

    private CasesMatchers() {
    }

    public static final class TotalMatcher_Balance {
      TotalMatcher_Balance() {
      }

      public final <R> Function1<Balance, R> Balance(Function<Amount, R> Balance) {
        return balance -> balance.match(Balance);
      }

      public final <R> Function1<Balance, R> Balance_(R r) {
        return this.Balance((amount) -> r);
      }
    }
  }
}
