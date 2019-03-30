package my.sheshenya.samplenettyrestfulapi.repository;

import lombok.extern.java.Log;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;

import java.util.*;

@Log
public class InMemoryTransactionRepositoryImpl implements TransactionRepository {

    private Map<String,Transaction> transactions = new HashMap<>();

    public InMemoryTransactionRepositoryImpl() {
        log.info("InMemoryTransactionRepositoryImpl instantiated");
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {

        String newTransactionId = UUID.randomUUID().toString();
        transaction.setId(newTransactionId);
        transaction.setDate(new Date());
        transaction.setStatus("New");

        transactions.put(newTransactionId, transaction);

        log.info(String.format("New transaction persisted in repository: %s", transaction.toString()));
        return transaction;
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        log.info(String.format("Get transaction by id %s", transactionId));
        return transactions.get(transactionId);
    }
}
