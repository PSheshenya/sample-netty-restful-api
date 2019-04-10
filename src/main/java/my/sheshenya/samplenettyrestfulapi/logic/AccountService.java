package my.sheshenya.samplenettyrestfulapi.logic;

import my.sheshenya.samplenettyrestfulapi.model.Account;

import java.math.BigDecimal;

public interface AccountService {

    /**
     * GetAccount
     *
     * @param accountId
     * @return account
     */
    Account getAccount(String accountId);

    /**
     * Get total balance of all accounts
     *
     * @return
     */
    BigDecimal getTotalBalance();

}
