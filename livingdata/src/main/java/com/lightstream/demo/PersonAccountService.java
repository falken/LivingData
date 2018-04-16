package com.lightstream.demo;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Insert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.now;

public class PersonAccountService {
    private Cluster cluster;

    public PersonAccountService(Cluster cluster) {
        this.cluster = cluster;
    }

    public PersonAccount getPersonAccount(UUID personId) {
        PersonAccount result = new PersonAccount();

        List<AccountTransaction> transactions = getAccountTransactions(personId);
        result.setCurrentBalance(transactions.stream()
                .map(AccountTransaction::getAmount)
                .reduce(new BigDecimal(0.0),BigDecimal::add));

        Optional<AccountTransaction> newestTrans = transactions.stream().max(Comparator.comparing(AccountTransaction::getTimeStamp));
        result.setLastUpdate(newestTrans.map(AccountTransaction::getTimeStamp).orElse(null));

        return result;
    }

    public List<AccountTransaction> getAccountTransactions(UUID personId){
        List<AccountTransaction> result = new ArrayList<>();
        try (final Session session = cluster.connect("Demo1")) {
            final ResultSet rs = session.execute("select update_ts,amount from person_account where person_id=" + personId + " ORDER BY update_ts ASC");
            for (Row row : rs) {
                AccountTransaction transaction = new AccountTransaction();
                UUID updateTs = row.getUUID("update_ts");
                float amount = row.getFloat("amount");
                transaction.setAmount(new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN));
               transaction.setTimeStamp(updateTs);
                result.add(transaction);
            }
        }
        return result;
    }

    public void postTransaction(UUID personId, float value){
        try (final Session session = cluster.connect("Demo1")) {
            Insert insert = insertInto("person_account")
                    .value("person_id", personId)
                    .value("update_ts", now())
                    .value("amount", value);
            session.execute(insert);
        }
    }
}
