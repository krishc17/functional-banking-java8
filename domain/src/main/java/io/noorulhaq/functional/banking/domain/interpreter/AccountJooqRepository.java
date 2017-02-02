package io.noorulhaq.functional.banking.domain.interpreter;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.noorulhaq.functional.banking.domain.algebra.AccountRepository;
import io.noorulhaq.functional.banking.domain.model.Account;
import static io.noorulhaq.functional.banking.domain.model.jooq.tables.Accounts.ACCOUNTS;
import static javaslang.API.*;
import static javaslang.Patterns.*;
import io.noorulhaq.functional.banking.domain.model.jooq.tables.Accounts;
import io.noorulhaq.functional.banking.domain.model.jooq.tables.records.AccountsRecord;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.control.Option;
import javaslang.control.Try;
import org.joda.time.DateTime;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import javax.sql.DataSource;
import java.sql.Timestamp;
import static io.noorulhaq.functional.banking.domain.model.Accounts.account;
import static io.noorulhaq.functional.banking.domain.model.Amounts.amount;
import static io.noorulhaq.functional.banking.domain.model.Balances.balance;


/**
 * Created by Noor on 1/22/17.
 */
public class AccountJooqRepository extends AccountRepository {

    private static DataSource dataSource;

    public AccountJooqRepository(String userName, String password, String url) {
        if(dataSource==null){
            dataSource = datasource(userName,password,url);
        }
    }

    @Override
    public Future<List<Account>> query() {
        return null;
    }

    @Override
    public Future<Option<Account>> query(String no) {
        return  Future.fromTry(openDBConnection(dataSource)
                .flatMap(ctxt -> fetchAccount(ctxt, no)
                        .andThen(() -> closeDBConnection(ctxt))));
    }

    @Override
    public Future<Account> store(Account account) {

        return Future.fromTry(openDBConnection(dataSource)
                .flatMap(ctxt -> fetchAccount(ctxt, account.no())
                        .flatMap(acc -> acc.isDefined() ? updateAccount(ctxt, account) : insertAccount(ctxt, account))
                        .andThen(() -> closeDBConnection(ctxt))));
    }

    @Override
    public void flush() {
        openDBConnection(dataSource)
                .flatMap(ctxt -> Try.of(() -> {
                    ctxt.deleteFrom(ACCOUNTS).execute();
                    return ctxt;
                })).andThen(ctxt -> closeDBConnection(ctxt));
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

    private Try<DSLContext> openDBConnection(DataSource dataSource) {
        return Try.of(() -> DSL.using(dataSource, SQLDialect.H2));
    }

    private Try<DSLContext> closeDBConnection(DSLContext ctxt) {
        return Try.of(() -> {
            ctxt.close();
            return ctxt;
        });
    }

    private Try<Account> accountMapper(AccountsRecord record) {
        Try<Account> account = account(record.getNo(), record.getName(), Option.of(new DateTime(record.getOpeningDate())))
                .map(acc ->
                        acc.withBalance(balance(amount(record.getBalance())))
                                .withCloseDate(Option.of(new DateTime(record.getClosingDate()))));
        return account;
    }


    private Try<Account> insertAccount(DSLContext ctxt, Account account) {
        return Try.of(() -> {
            ctxt.insertInto(Accounts.ACCOUNTS, Accounts.ACCOUNTS.NO, Accounts.ACCOUNTS.NAME, Accounts.ACCOUNTS.BALANCE, Accounts.ACCOUNTS.OPENING_DATE)
                    .values(account.no(), account.name(), account.balance().preciseValue(), new Timestamp(account.dateOfOpening().getMillis()))
                    .execute();
            return account;
        });
    }


    private Try<Account> updateAccount(DSLContext ctxt, Account account) {
        return Try.of(() -> {
            UpdateSetMoreStep steps = ctxt.update(Accounts.ACCOUNTS)
                    .set(Accounts.ACCOUNTS.NAME, account.name())
                    .set(Accounts.ACCOUNTS.BALANCE, account.balance().preciseValue())
                    .set(Accounts.ACCOUNTS.OPENING_DATE, new Timestamp(account.dateOfOpening().getMillis()));

            if (account.dateOfClosing().isDefined())
                steps.set(Accounts.ACCOUNTS.CLOSING_DATE, new Timestamp(account.dateOfClosing().get().getMillis()));

            steps.where(ACCOUNTS.NO.eq(account.no()));

            steps.execute();
            return account;
        });
    }



    private DataSource datasource(String userName, String password, String url){
        ComboPooledDataSource cpds = new ComboPooledDataSource();
       try {
           cpds.setDriverClass("org.h2.Driver");
           cpds.setJdbcUrl(url);
           cpds.setUser(userName);
           cpds.setPassword(password);

           cpds.setMinPoolSize(5);
           cpds.setAcquireIncrement(5);
           cpds.setMaxPoolSize(20);
           cpds.setMaxStatements(180);
       }catch(Exception e){
            throw new RuntimeException(e);
        }
        return cpds;
    }
}
