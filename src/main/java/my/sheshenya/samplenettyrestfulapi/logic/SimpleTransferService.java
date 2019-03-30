package my.sheshenya.samplenettyrestfulapi.logic;

import com.google.inject.Inject;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import lombok.extern.java.Log;
import my.sheshenya.samplenettyrestfulapi.repository.TransactionRepository;

@Log
public class SimpleTransferService implements TransferService {
    //private final  AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionListener transactionListener;

    @Inject
    SimpleTransferService(TransactionRepository transactionRepository,
                          TransactionListener transactionListener) {
        this.transactionRepository = transactionRepository;
        this.transactionListener = transactionListener;

        log.info("SimpleTransferService instantiated");
    }


    public String newTransaction(Transaction transaction) {
        log.info("New transaction request...");
        Transaction registeredTransaction = transactionRepository.createTransaction(transaction);
        log.info(String.format("Transaction registered as Id='%s'", registeredTransaction.getId()));

        CompletableFuture<Void> future = CompletableFuture
                .runAsync(() -> transactionListener.doTransaction(registeredTransaction), Executors.newCachedThreadPool());

        return registeredTransaction.getId();
    }


    /**
     * GetTransaction
     *
     * Think about limits for the each session!
     *
     * @param transactionId
     * @return transaction
     */
    public Transaction getTransaction(String transactionId) {
        log.info(String.format("Get transaction  %s", transactionId));
        return transactionRepository.getTransactionById(transactionId);
    }

}
