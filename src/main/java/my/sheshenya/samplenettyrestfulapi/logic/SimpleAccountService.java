package my.sheshenya.samplenettyrestfulapi.logic;

import com.google.inject.Inject;
import lombok.extern.log4j.Log4j2;
import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.repository.AccountRepository;

import java.math.BigDecimal;

@Log4j2
public class SimpleAccountService implements AccountService {
    private final AccountRepository accountRepository;


    @Inject
    public SimpleAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        log.info("SimpleAccountService instantiated");
    }


    public Account getAccount(String accountId) {
        log.info("Get account {}", accountId);
        return accountRepository.getAccountById(accountId);
    }

    @Override
    public BigDecimal getTotalBalance() {
        return accountRepository.getTotalBalance();
    }

}
