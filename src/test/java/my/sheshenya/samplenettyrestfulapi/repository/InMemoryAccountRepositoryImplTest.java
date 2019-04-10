package my.sheshenya.samplenettyrestfulapi.repository;

import my.sheshenya.samplenettyrestfulapi.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class InMemoryAccountRepositoryImplTest {

    InMemoryAccountRepositoryImpl accountRepository;

    @Before
    public void setUp() throws Exception {
        accountRepository = new InMemoryAccountRepositoryImpl();
    }

    @Test
    public void AccountDatatypeBalance_Test(){
        double x = 1.03;
        double y = .42;

        BigDecimal a = BigDecimal.valueOf(1.03);
        BigDecimal b = BigDecimal.valueOf(.42);

        Assert.assertNotEquals(x - y, 0.61);
        Assert.assertEquals(x - y, 0.6100000000000001, 0);
        Assert.assertEquals(a.subtract(b), BigDecimal.valueOf(0.61));

        System.out.println(x - y);
        System.out.println(a.subtract(b));
    }



    @Test
    public void getAccountById() {
        Account a1 = accountRepository.getAccountById("a1");

        Assert.assertNotNull(a1);
        Assert.assertEquals(BigDecimal.valueOf(100), a1.getBalance());
    }

    @Test
    public void addNewAccount() {
        boolean newAccount = accountRepository.add(new Account("a0", "Zero account", BigDecimal.valueOf(200)));
        Assert.assertEquals(true, newAccount);

        Assert.assertEquals(BigDecimal.valueOf(800), accountRepository.getTotalBalance());
    }

    @Test
    public void addSameAccount() {
        boolean doubleAccountConflict = accountRepository.add(new Account("a1", "First account", BigDecimal.valueOf(200)));
        Assert.assertEquals(false, doubleAccountConflict);

        Assert.assertEquals(BigDecimal.valueOf(600), accountRepository.getTotalBalance());
    }

    @Test
    public void getTotalBalance() {
        BigDecimal totalBalance = accountRepository.getTotalBalance();
        Assert.assertEquals(BigDecimal.valueOf(600), totalBalance);
    }
}