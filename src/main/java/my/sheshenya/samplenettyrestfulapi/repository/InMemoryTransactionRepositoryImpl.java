package my.sheshenya.samplenettyrestfulapi.repository;

import lombok.extern.log4j.Log4j2;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
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

        log.info("New transaction persisted in repository: {}", transaction.toString());
        return transaction;
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        log.info("Get transaction by id {}", transactionId);
        return transactions.get(transactionId);
    }
}
