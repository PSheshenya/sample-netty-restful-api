package my.sheshenya.samplenettyrestfulapi.logic;

import my.sheshenya.samplenettyrestfulapi.model.Account;

public interface AccountService {

    /**
     * GetAccount
     *
     * @param accountId
     * @return account
     */
    Account getAccount(String accountId);

    double getTotalBalance();

}
