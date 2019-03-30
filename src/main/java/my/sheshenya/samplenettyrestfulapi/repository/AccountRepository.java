package my.sheshenya.samplenettyrestfulapi.repository;

import my.sheshenya.samplenettyrestfulapi.model.Account;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AccountRepository {
    Account getAccountById(String accountId);

    boolean add(Account account);
    Account update(Account account);

    Set<Account> findAll();

    double getTotalBalance();
}
