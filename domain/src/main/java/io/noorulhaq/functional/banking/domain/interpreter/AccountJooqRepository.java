package io.noorulhaq.functional.banking.domain.interpreter;

import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.model.Account;
import io.noorulhaq.functional.banking.domain.model.jooq.tables.Accounts;

import static javaslang.API.*;
import static javaslang.Patterns.*;

import io.noorulhaq.functional.banking.domain.model.jooq.tables.records.AccountsRecord;
import javaslang.control.Option;
import javaslang.control.Try;
import org.joda.time.DateTime;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;

import static io.noorulhaq.functional.banking.domain.model.Accounts.account;
import static io.noorulhaq.functional.banking.domain.model.Amounts.amount;
import static io.noorulhaq.functional.banking.domain.model.Balances.balance;


/**
 * Created by Noor on 1/22/17.
 */
public class AccountJooqRepository extends AccountRepository {

    private String userName = "sa", password = "", url = "jdbc:h2:./banking";

    @Override
    public Try<Option<Account>> query(String no) {
        return openDBConnection(userName, password, url)
                .flatMap(ctxt -> fetchAccount(ctxt, no)
                        .andThen(() -> closeDBConnection(ctxt)));
    }

    @Override
    public Try<Account> store(Account account) {

        return openDBConnection(userName, password, url)
                .flatMap(ctxt -> fetchAccount(ctxt, account.no())
                        .flatMap(acc -> acc.isDefined() ? updateccount(ctxt, account) : insertAccount(ctxt, account))
                        .andThen(() -> closeDBConnection(ctxt)));
    }


    private Try<DSLContext> openDBConnection(String userName, String password, String url) {
        return Try.of(() -> {
            Connection conn = DriverManager.getConnection(url, userName, password);
            return DSL.using(conn, SQLDialect.H2);
        });
    }

    private Try<DSLContext> closeDBConnection(DSLContext ctxt) {
        return Try.of(() -> {
            ctxt.close();
            return ctxt;
        });
    }


    private Try<Account> insertAccount(DSLContext ctxt, Account account) {
        return Try.of(() -> {
            ctxt.insertInto(Accounts.ACCOUNTS, Accounts.ACCOUNTS.NO, Accounts.ACCOUNTS.NAME, Accounts.ACCOUNTS.BALANCE, Accounts.ACCOUNTS.OPENING_DATE)
                    .values(account.no(), account.name(), account.balance().value(), new Timestamp(account.dateOfOpening().getMillis()))
                    .execute();
            return account;
        });
    }


    private Try<Account> updateccount(DSLContext ctxt, Account account) {
        return Try.of(() -> {
            UpdateSetMoreStep steps = ctxt.update(Accounts.ACCOUNTS)
                    .set(Accounts.ACCOUNTS.NO, account.no())
                    .set(Accounts.ACCOUNTS.NAME, account.name())
                    .set(Accounts.ACCOUNTS.BALANCE, account.balance().value())
                    .set(Accounts.ACCOUNTS.OPENING_DATE, new Timestamp(account.dateOfOpening().getMillis()));

            if (account.dateOfClosing().isDefined())
                steps.set(Accounts.ACCOUNTS.CLOSING_DATE, new Timestamp(account.dateOfClosing().get().getMillis()));

            steps.execute();
            return account;
        });
    }


    public Try<Option<Account>> fetchAccount(DSLContext ctxt, String no) {

        return Match(Try.of(() -> ctxt.selectFrom(Accounts.ACCOUNTS).where(Accounts.ACCOUNTS.NO.eq(no)).fetch())).of(
                Case(Success($()), records -> records
                        .map(this::accountMapper)
                        .stream()
                        .map(acc -> acc.map(account -> Option.of(account)))
                        .findFirst()
                        .orElse(Try.of(() -> Option.none()))),

                Case(Failure($()), error -> Try.failure(error))
        );
    }


    private Try<Account> accountMapper(AccountsRecord record) {
        Try<Account> account = account(record.getNo(), record.getName(), Option.of(new DateTime(record.getOpeningDate())))
                .map(acc ->
                        acc.withBalance(balance(amount(record.getBalance())))
                                .withCloseDate(Option.of(new DateTime(record.getClosingDate()))));
        return account;
    }

}
