package my.sheshenya.samplenettyrestfulapi.repository;

import my.sheshenya.samplenettyrestfulapi.model.Account;

import java.math.BigDecimal;

public interface AccountRepository {
    Account getAccountById(String accountId);

    boolean add(Account account);

    BigDecimal getTotalBalance();
}
