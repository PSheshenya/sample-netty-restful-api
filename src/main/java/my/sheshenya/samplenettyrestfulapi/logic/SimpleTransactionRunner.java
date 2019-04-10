package my.sheshenya.samplenettyrestfulapi.logic;

import com.google.inject.Inject;
import lombok.extern.log4j.Log4j2;
import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import my.sheshenya.samplenettyrestfulapi.repository.AccountRepository;

import java.math.BigDecimal;

@Log4j2
public class SimpleTransactionRunner implements Runnable {

    private final int DELAY = 4000;
    private AccountRepository accountRepository;
    //private AccountOperationValidators validators;

    private Transaction transaction;

    public SimpleTransactionRunner(Transaction transaction) {
        this.transaction = transaction;
        log.info("SimpleTransactionRunner instantiated");
    }

    @Inject
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }



    @Override
    public void run() {
        log.info("Transaction Runing... {}", transaction.getId());
        transaction.setStatus("Runing");

        BigDecimal transactionAmount = transaction.getAmount();
        String fromAccountId = transaction.getFromAccountId();
        String toAccountId = transaction.getToAccountId();

        Account fromAccount = accountRepository.getAccountById(fromAccountId);
        if (fromAccount == null) {
            transaction.setStatus("Failed");
            log.error("Transaction failed {}. The fromAccount is Null", transaction.getId());
            return;
        }

        Account toAccount = accountRepository.getAccountById(toAccountId);
        if (toAccount == null) {
            transaction.setStatus("Failed");
            log.error("Transaction failed {}. The toAccount is Null", transaction.getId());
            return;
        }

        if (fromAccountId.equals(toAccountId)) {
            transaction.setStatus("Failed");
            log.error("Transaction failed {}. Use the same accounts", transaction.getId());
            return;
        }


        log.info("Transaction Executing ({}->{}):{}", fromAccountId, toAccountId, transaction.getId());
        transaction.setStatus("Executing");

        BigDecimal curBalance = fromAccount.getBalance();
        if (curBalance.compareTo(transactionAmount) >= 0) {

            fromAccount.withdraw(transactionAmount);
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.getMessage();
            }

            toAccount.deposit(transactionAmount);
            transaction.setStatus("Done");
            log.info("Transaction Done ({}->{}):{}", fromAccountId, toAccountId, transaction.getId());
        } else {
            transaction.setStatus("Cancel");
            log.warn("Transaction Cancel ({}->{}):{}", fromAccountId, toAccountId, transaction.getId());
        }

        log.info("Transaction finished {}", transaction.getId());
    }
}