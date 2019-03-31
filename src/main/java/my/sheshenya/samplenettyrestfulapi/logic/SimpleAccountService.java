package my.sheshenya.samplenettyrestfulapi.logic;

import com.google.inject.Inject;
import lombok.extern.java.Log;
import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import my.sheshenya.samplenettyrestfulapi.repository.AccountRepository;
import my.sheshenya.samplenettyrestfulapi.repository.TransactionRepository;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Log
public class SimpleAccountService implements AccountService {
    private final AccountRepository accountRepository;


    @Inject
    public SimpleAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        log.info("SimpleAccountService instantiated");
    }


    public Account getAccount(String accountId) {
        log.info(String.format("Get account  %s", accountId));
        return accountRepository.getAccountById(accountId);
    }

    @Override
    public double getTotalBalance() {
        return accountRepository.getTotalBalance();
    }

}
