package my.sheshenya.samplenettyrestfulapi.repository;

import lombok.extern.java.Log;
import my.sheshenya.samplenettyrestfulapi.model.Account;

import java.util.*;

@Log
public class InMemoryAccountRepositoryImpl implements AccountRepository {

    private HashSet<Account> accountList = new HashSet<Account>();

    public InMemoryAccountRepositoryImpl() {

        this.accountList.add(new Account("a1", "First account", 100)) ;
        this.accountList.add(new Account("a2", "Second account", 200)) ;
        this.accountList.add(new Account("a3", "Thirdth account", 300)) ;

        log.info("InMemoryAccountRepositoryImpl instantiated");
    }

    @Override
    public Account getAccountById(String accountId) {
        log.info(String.format("Get account by id %s", accountId));

        Optional<Account> account = accountList.stream().filter(a -> a.getId().equals(accountId)).findFirst();
        if (account.isPresent())
            return account.get();
        else {
            log.warning(String.format("The account id %s is unreachable", accountId));
            return null;
        }
    }

    @Override
    public boolean add(Account account) {
        log.info(String.format("Adding new account"));

        if (accountList.stream().anyMatch(a -> a.getId() == account.getId())) {
            log.warning(String.format("The same account Id conflict"));
            return false;
        }

        return accountList.add(account);
    }



    public double getTotalBalance()
    {
        return accountList.stream().mapToDouble(a -> a.getBalance()).sum();
    }
}
