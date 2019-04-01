package my.sheshenya.samplenettyrestfulapi.logic;

import com.google.inject.Inject;
import lombok.extern.java.Log;
import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import my.sheshenya.samplenettyrestfulapi.repository.AccountRepository;
import my.sheshenya.samplenettyrestfulapi.repository.TransactionRepository;

@Log
public class SimpleTransactionListener implements TransactionListener {

    private final int DELAY = 5000;
    private AccountRepository accountRepository;
    //private AccountOperationValidators validators;

    @Inject
    public SimpleTransactionListener(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

        log.info("SimpleTransactionListener instantiated");
    }

    Object lock = new Object();

    @Override
    public void doTransaction(Transaction transaction) {
        log.info(String.format("Transaction starting... %s", transaction.getId()));

        Double transactionAmount = transaction.getAmount();
        String fromAccountId = transaction.getFromAccountId();
        String toAccountId = transaction.getToAccountId();

        Account fromAccount = accountRepository.getAccountById(fromAccountId);
        if (fromAccount == null) {
            transaction.setStatus("Failed");
            log.warning(String.format("Transaction failed %s. The fromAccount is Null", transaction.getId()));
            return;
        }

        Account toAccount = accountRepository.getAccountById(toAccountId);
        if (toAccount == null) {
            transaction.setStatus("Failed");
            log.warning(String.format("Transaction failed %s. The toAccount is Null", transaction.getId()));
            return;
        }

        if (fromAccountId == toAccountId) {
            transaction.setStatus("Failed");
            log.warning(String.format("Transaction failed %s. Use the same accounts", transaction.getId()));
            return;
        }



        //synchronized(lock) {

        synchronized(fromAccount) {
            synchronized(toAccount) {
                log.info(String.format("Transaction Runing (%s->%s):%s", fromAccountId, toAccountId, transaction.getId()));
                transaction.setStatus("Runing");

                double curBalance = fromAccount.getBalance();
                if (curBalance > transactionAmount) {

                    fromAccount.withdraw(transactionAmount);
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    toAccount.deposit(transactionAmount);
                    transaction.setStatus("Done");
                } else
                    transaction.setStatus("Wrong");

                log.info(String.format("Transaction Done (%s->%s):%s", fromAccountId, toAccountId, transaction.getId()));

            }
        }

        log.info(String.format("Transaction finished %s", transaction.getId()));

    }
}