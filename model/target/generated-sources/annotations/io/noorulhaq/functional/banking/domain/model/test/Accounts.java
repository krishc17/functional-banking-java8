package io.noorulhaq.functional.banking.domain.model.test;

import java.lang.Override;
import java.lang.String;
import java.util.function.Supplier;
import javaslang.Function1;
import javaslang.Function5;
import javaslang.control.Option;
import javaslang.control.Try;
import org.joda.time.DateTime;

public final class Accounts {
  private Accounts() {
  }

  public static Try<Account> account(String no, String name, Option<DateTime> openDate) {
    return Account.account(no, name, openDate);
  }

  static Account Account0(String no, String name, Balance balance, DateTime dateOfOpening,
      Option<DateTime> dateOfClosing) {
    return new Account_(no, name, balance, dateOfOpening, dateOfClosing);
  }

  public static Account lazy(Supplier<Account> account) {
    return new Lazy(account);
  }

  public static CasesMatchers.TotalMatcher_Account cases() {
    return CasesMatchers.totalMatcher_Account;
  }

  public static String getNo(Account account) {
    return account.match((no, name, balance, dateOfOpening, dateOfClosing) -> no);
  }

  public static String getName(Account account) {
    return account.match((no, name, balance, dateOfOpening, dateOfClosing) -> name);
  }

  public static Balance getBalance(Account account) {
    return account.match((no, name, balance, dateOfOpening, dateOfClosing) -> balance);
  }

  public static DateTime getDateOfOpening(Account account) {
    return account.match((no, name, balance, dateOfOpening, dateOfClosing) -> dateOfOpening);
  }

  public static Option<DateTime> getDateOfClosing(Account account) {
    return account.match((no, name, balance, dateOfOpening, dateOfClosing) -> dateOfClosing);
  }

  static Function1<Account, Account> setNo0(String newNo) {
    return modNo0(__ -> newNo);
  }

  static Function1<Account, Account> modNo0(Function1<String, String> noMod) {
    return account -> account.match((no, name, balance, dateOfOpening, dateOfClosing) -> Account0(noMod.apply(no), name, balance, dateOfOpening, dateOfClosing));
  }

  static Function1<Account, Account> setName0(String newName) {
    return modName0(__ -> newName);
  }

  static Function1<Account, Account> modName0(Function1<String, String> nameMod) {
    return account -> account.match((no, name, balance, dateOfOpening, dateOfClosing) -> Account0(no, nameMod.apply(name), balance, dateOfOpening, dateOfClosing));
  }

  static Function1<Account, Account> setBalance0(Balance newBalance) {
    return modBalance0(__ -> newBalance);
  }

  static Function1<Account, Account> modBalance0(Function1<Balance, Balance> balanceMod) {
    return account -> account.match((no, name, balance, dateOfOpening, dateOfClosing) -> Account0(no, name, balanceMod.apply(balance), dateOfOpening, dateOfClosing));
  }

  static Function1<Account, Account> setDateOfOpening0(DateTime newDateOfOpening) {
    return modDateOfOpening0(__ -> newDateOfOpening);
  }

  static Function1<Account, Account> modDateOfOpening0(Function1<DateTime, DateTime> dateOfOpeningMod) {
    return account -> account.match((no, name, balance, dateOfOpening, dateOfClosing) -> Account0(no, name, balance, dateOfOpeningMod.apply(dateOfOpening), dateOfClosing));
  }

  static Function1<Account, Account> setDateOfClosing0(Option<DateTime> newDateOfClosing) {
    return modDateOfClosing0(__ -> newDateOfClosing);
  }

  static Function1<Account, Account> modDateOfClosing0(Function1<Option<DateTime>, Option<DateTime>> dateOfClosingMod) {
    return account -> account.match((no, name, balance, dateOfOpening, dateOfClosing) -> Account0(no, name, balance, dateOfOpening, dateOfClosingMod.apply(dateOfClosing)));
  }

  private static final class Account_ extends Account {
    private final String no;

    private final String name;

    private final Balance balance;

    private final DateTime dateOfOpening;

    private final Option<DateTime> dateOfClosing;

    Account_(String no, String name, Balance balance, DateTime dateOfOpening,
        Option<DateTime> dateOfClosing) {
      this.no = no;
      this.name = name;
      this.balance = balance;
      this.dateOfOpening = dateOfOpening;
      this.dateOfClosing = dateOfClosing;
    }

    @Override
    public <R> R match(Function5<String, String, Balance, DateTime, Option<DateTime>, R> Account) {
      return Account.apply(this.no, this.name, this.balance, this.dateOfOpening, this.dateOfClosing);
    }

    @Override
    public String toString() {
      return "Account(" + this.no + ", " + this.name + ", " + this.balance + ", " + this.dateOfOpening + ", " + this.dateOfClosing + ")";
    }
  }

  private static final class Lazy extends Account {
    private volatile Supplier<Account> expression;

    private Account evaluation;

    Lazy(Supplier<Account> account) {
      this.expression = account;
    }

    private synchronized Account _evaluate() {
      Supplier<Account> e = expression;
      if (e != null) {
        evaluation = e.get();
        expression = null;
      }
      return evaluation;
    }

    @Override
    public <R> R match(Function5<String, String, Balance, DateTime, Option<DateTime>, R> Account) {
      return (this.expression == null ? this.evaluation : _evaluate()).match(Account);
    }

    @Override
    public String toString() {
      return (this.expression == null ? this.evaluation : _evaluate()).toString();
    }
  }

  public static class CasesMatchers {
    private static final TotalMatcher_Account totalMatcher_Account = new TotalMatcher_Account();

    private CasesMatchers() {
    }

    public static final class TotalMatcher_Account {
      TotalMatcher_Account() {
      }

      public final <R> Function1<Account, R> Account(Function5<String, String, Balance, DateTime, Option<DateTime>, R> Account) {
        return account -> account.match(Account);
      }

      public final <R> Function1<Account, R> Account_(R r) {
        return this.Account((no, name, balance, dateOfOpening, dateOfClosing) -> r);
      }
    }
  }
}
