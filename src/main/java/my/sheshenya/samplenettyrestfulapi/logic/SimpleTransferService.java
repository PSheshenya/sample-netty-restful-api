package my.sheshenya.samplenettyrestfulapi.logic;

import com.google.inject.Inject;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import my.sheshenya.samplenettyrestfulapi.repository.TransactionRepository;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Log4j2
public class SimpleTransferService implements TransferService {
    private final TransactionRepository transactionRepository;

    @Inject
    public SimpleTransferService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;

        log.info("SimpleTransferService instantiated");
    }


    public String newTransaction(Transaction transaction) {
        log.info("New transaction request...");
        Transaction registeredTransaction = transactionRepository.createTransaction(transaction);
        log.info("New transaction registered as Id='{}'", registeredTransaction.getId());

        CompletableFuture
                .runAsync(() -> new SimpleTransactionRunner(registeredTransaction).run(), Executors.newCachedThreadPool());

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
        log.info("Get transaction  {}", transactionId);
        return transactionRepository.getTransactionById(transactionId);
    }

}
