package my.sheshenya.samplenettyrestfulapi;

import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;

import java.util.*;

public class InMemoryTransferRepositoryImpl implements TransferRepository {

    private Map<String,Transaction> transactions = new HashMap<>();

    @Override
    public Transaction createTransaction(Transaction transaction) {

        String newTransactionId = UUID.randomUUID().toString();
        transaction.setId(newTransactionId);
        transaction.setDate(new Date());

//        Account fromAccount = transaction.getFromAccountId();
//
//        transaction
        transaction.setStatus("Done");

        transactions.put(newTransactionId, transaction);

        return transaction;

    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        return null;
    }
}
