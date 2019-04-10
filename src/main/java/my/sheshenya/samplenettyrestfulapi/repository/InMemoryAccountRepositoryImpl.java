package my.sheshenya.samplenettyrestfulapi.repository;

import lombok.extern.log4j.Log4j2;
import my.sheshenya.samplenettyrestfulapi.model.Account;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

@Log4j2
public class InMemoryAccountRepositoryImpl implements AccountRepository {

    private HashSet<Account> accountList = new HashSet<Account>();

    public InMemoryAccountRepositoryImpl() {

        this.accountList.add(new Account("a1", "First account", BigDecimal.valueOf(100)));
        this.accountList.add(new Account("a2", "Second account", BigDecimal.valueOf(200)));
        this.accountList.add(new Account("a3", "Thirdth account", BigDecimal.valueOf(300)));

        log.info("InMemoryAccountRepositoryImpl instantiated");
    }

    @Override
    public Account getAccountById(String accountId) {
        log.info("Get account by id {}", accountId);

        Optional<Account> account = accountList.stream().filter(a -> a.getId().equals(accountId)).findFirst();
        if (account.isPresent())
            return account.get();
        else {
            log.warn("The account id {} is unreachable", accountId);
            return null;
        }
    }

    @Override
    public boolean add(Account account) {
        log.info("Adding new account");

        if (accountList.stream().anyMatch(a -> a.getId() == account.getId())) {
            log.warn("The same account Id conflict");
            return false;
        }

        return accountList.add(account);
    }



    public BigDecimal getTotalBalance()
    {
        return accountList.stream().map(a -> a.getBalance()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
