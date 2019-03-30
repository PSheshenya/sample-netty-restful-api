package my.sheshenya.samplenettyrestfulapi.repository;

import my.sheshenya.samplenettyrestfulapi.model.Transaction;

public interface TransactionRepository {
    Transaction createTransaction(Transaction transaction);

    Transaction getTransactionById(String transactionId);
}
