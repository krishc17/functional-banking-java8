package io.noorulhaq.functional.banking.domain.test;


import static io.noorulhaq.functional.banking.domain.test.model.tables.Accounts.*;
import static io.noorulhaq.functional.banking.domain.model.Accounts.*;
import static io.noorulhaq.functional.banking.domain.model.Balances.*;
import static io.noorulhaq.functional.banking.domain.model.Amounts.*;
import io.noorulhaq.functional.banking.domain.model.Account;
import javaslang.control.Option;
import javaslang.control.Try;
import org.joda.time.DateTime;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import static org.junit.Assert.*;

/**
 * Created by Noor on 1/22/17.
 */
public class JooqRepoTest {

    @Test
    public void persistAnRetrieve() throws Exception {

        String userName = "sa";
        String password = "";
        String url = "jdbc:h2:./banking";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, password);
            DSLContext ctxt = DSL.using(conn, SQLDialect.H2);

            ctxt.deleteFrom(ACCOUNTS).execute();

            ctxt.insertInto(ACCOUNTS, ACCOUNTS.NO, ACCOUNTS.NAME, ACCOUNTS.BALANCE, ACCOUNTS.OPENING_DATE)
                    .values("Acc1", "Account One", 100d, new Timestamp(System.currentTimeMillis()))
                    .execute();


            long recordCounts = ctxt.selectFrom(ACCOUNTS).where(ACCOUNTS.NO.eq("Acc1")).fetch().map(record -> {

                Try<Account> account = account(record.getNo(), record.getName(), Option.of(new DateTime(record.getOpeningDate())))
                        .map(acc ->
                                acc.withBalance(balance(amount(record.getBalance())))
                                        .withCloseDate(Option.of(new DateTime(record.getClosingDate()))));


                if (account.isFailure())
                    throw new RuntimeException(account.getCause());

                return account;
            }).stream().map((account ->
                    account.andThen(account1 -> {
                        assertEquals(account1.name(), "Account One");
                    }))).count();

            ctxt.close();

            assertTrue(recordCounts > 0);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        } finally {
            conn.close();
        }
    }


}
