package my.sheshenya.samplenettyrestfulapi;

import my.sheshenya.samplenettyrestfulapi.model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class InMemoryAccountRepositoryImpl implements AccountRepository {

    private List<Account> accountList = new ArrayList<>();

    @Override
    public Account getAccountById(Long accountId) {
        Optional<Account> account = accountList.stream().filter(a -> a.getId().equals(accountId)).findFirst();
        if (account.isPresent())
            return account.get();
        else
            return null;
    }

    @Override
    public Account add(Account account) {
        account.setId((long) (accountList.size()+1));
        accountList.add(account);
        return account;
    }

    @Override
    public List<Account> findAll() {
        return accountList;
    }
}
