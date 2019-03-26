package my.sheshenya.samplenettyrestfulapi;

import my.sheshenya.samplenettyrestfulapi.model.Transaction;

public class TransferService {
    private TransferRepository repository;



    public Transaction newTransaction(Transaction transaction) {
        return repository.createTransaction(transaction);
    }

    /**
     * GetTransactionStatus
     *
     * Think about limits from session!
     *
     * @param transactionId
     * @return transaction Status
     */
    public String getTransactionStatus(String transactionId) {
        Transaction transaction = repository.getTransactionById(transactionId);
        return transaction != null ? transaction.getStatus() : "Not found";
    }

}
