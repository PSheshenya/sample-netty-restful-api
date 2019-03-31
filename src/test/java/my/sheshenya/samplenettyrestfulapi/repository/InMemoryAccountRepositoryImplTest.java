package my.sheshenya.samplenettyrestfulapi.repository;

import my.sheshenya.samplenettyrestfulapi.model.Account;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryAccountRepositoryImplTest {

    InMemoryAccountRepositoryImpl accountRepository;

    @Before
    public void setUp() throws Exception {
        accountRepository = new InMemoryAccountRepositoryImpl();
    }

    @Test
    public void getAccountById() {
        Account a1 = accountRepository.getAccountById("a1");

        Assert.assertNotNull(a1);
        Assert.assertEquals(100, a1.getBalance(), 0);
    }

    @Test
    public void addNew() {
        boolean newAccount = accountRepository.add(new Account("a0", "Zero account", 200));
        Assert.assertEquals(true, newAccount);

        Assert.assertEquals(800, accountRepository.getTotalBalance(), 0);
    }

    @Test
    public void addSame() {
        boolean doubleAccountConflict = accountRepository.add(new Account("a1", "First account", 200));
        Assert.assertEquals(false, doubleAccountConflict);

        Assert.assertEquals(600, accountRepository.getTotalBalance(), 0);
    }

    @Test
    public void getTotalBalance() {
        double totalBalance = accountRepository.getTotalBalance();
        Assert.assertEquals(600, totalBalance, 0);
    }
}