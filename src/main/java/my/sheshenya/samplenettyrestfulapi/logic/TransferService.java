package my.sheshenya.samplenettyrestfulapi.logic;

import my.sheshenya.samplenettyrestfulapi.model.Transaction;

public interface TransferService {

    /*
     * Register and acting new transaction
     *
     * @param transaction
     * @return transactionId
     */
    String newTransaction(Transaction transaction);

    /**
     * GetTransaction
     *
     * @param transactionId
     * @return transaction
     */
    Transaction getTransaction(String transactionId);


}
