package io.noorulhaq.functional.banking.domain.model.test;

import java.lang.Double;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.util.function.Function;
import java.util.function.Supplier;
import javaslang.Function1;

public final class Amounts {
  private static Amount amount;

  private Amounts() {
  }

  public static Amount amount(Double value) {
    return Amount.amount(value);
  }

  public static Amount amount() {
    Amount _amount = amount;
    if (_amount == null) {
      amount = _amount = Amount.amount();
    }
    return _amount;
  }

  static Amount Amount0(BigDecimal value) {
    return new Amount_(value);
  }

  public static Amount lazy(Supplier<Amount> amount) {
    return new Lazy(amount);
  }

  public static CasesMatchers.TotalMatcher_Amount cases() {
    return CasesMatchers.totalMatcher_Amount;
  }

  public static BigDecimal getValue(Amount amount) {
    return amount.match((value) -> value);
  }

  static Function1<Amount, Amount> setValue0(BigDecimal newValue) {
    return modValue0(__ -> newValue);
  }

  static Function1<Amount, Amount> modValue0(Function1<BigDecimal, BigDecimal> valueMod) {
    return amount -> amount.match((value) -> Amount0(valueMod.apply(value)));
  }

  private static final class Amount_ extends Amount {
    private final BigDecimal value;

    Amount_(BigDecimal value) {
      this.value = value;
    }

    @Override
    public <R> R match(Function<BigDecimal, R> Amount) {
      return Amount.apply(this.value);
    }

    @Override
    public boolean equals(Object obj) {
      return (obj instanceof Amount) && ((Amount) obj).match((value) -> this.value.equals(value));
    }

    @Override
    public int hashCode() {
      return 23 + this.value.hashCode();
    }

    @Override
    public String toString() {
      return "Amount(" + this.value + ")";
    }
  }

  private static final class Lazy extends Amount {
    private volatile Supplier<Amount> expression;

    private Amount evaluation;

    Lazy(Supplier<Amount> amount) {
      this.expression = amount;
    }

    private synchronized Amount _evaluate() {
      Supplier<Amount> e = expression;
      if (e != null) {
        evaluation = e.get();
        expression = null;
      }
      return evaluation;
    }

    @Override
    public <R> R match(Function<BigDecimal, R> Amount) {
      return (this.expression == null ? this.evaluation : _evaluate()).match(Amount);
    }

    @Override
    public boolean equals(Object obj) {
      return (this.expression == null ? this.evaluation : _evaluate()).equals(obj);
    }

    @Override
    public int hashCode() {
      return (this.expression == null ? this.evaluation : _evaluate()).hashCode();
    }

    @Override
    public String toString() {
      return (this.expression == null ? this.evaluation : _evaluate()).toString();
    }
  }

  public static class CasesMatchers {
    private static final TotalMatcher_Amount totalMatcher_Amount = new TotalMatcher_Amount();

    private CasesMatchers() {
    }

    public static final class TotalMatcher_Amount {
      TotalMatcher_Amount() {
      }

      public final <R> Function1<Amount, R> Amount(Function<BigDecimal, R> Amount) {
        return amount -> amount.match(Amount);
      }

      public final <R> Function1<Amount, R> Amount_(R r) {
        return this.Amount((value) -> r);
      }
    }
  }
}
