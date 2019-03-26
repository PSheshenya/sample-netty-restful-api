package my.sheshenya.samplenettyrestfulapi;

import my.sheshenya.samplenettyrestfulapi.model.Account;

import java.util.List;

public interface AccountRepository {
    Account getAccountById(Long accountId);

    Account add(Account account);

    List<Account> findAll();
}
