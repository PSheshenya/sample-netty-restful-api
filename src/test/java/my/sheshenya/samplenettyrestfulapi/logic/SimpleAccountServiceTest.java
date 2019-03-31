package my.sheshenya.samplenettyrestfulapi.logic;

import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleAccountServiceTest {

    SimpleAccountService accountService;

    @Before
    public void setUp() throws Exception {
        accountService = new SimpleAccountService(new InMemoryAccountRepositoryImpl());
    }

    @Test
    public void getAccount() {
        Account emptyAccount = accountService.getAccount("xxx");
        Assert.assertNull(emptyAccount);
    }

    @Test
    public void getTotalBalance() {
        double balance = accountService.getTotalBalance();
        Assert.assertEquals(balance, 600, 0);
    }
}