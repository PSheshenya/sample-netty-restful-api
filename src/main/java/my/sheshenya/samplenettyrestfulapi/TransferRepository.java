package my.sheshenya.samplenettyrestfulapi;

import my.sheshenya.samplenettyrestfulapi.model.Transaction;

public interface TransferRepository {
    Transaction createTransaction(Transaction transaction);

    Transaction getTransactionById(String transactionId);
}
