package my.sheshenya.samplenettyrestfulapi.logic;

import my.sheshenya.samplenettyrestfulapi.model.Transaction;

public interface TransactionListener {
    void doTransaction(Transaction transaction);
}
