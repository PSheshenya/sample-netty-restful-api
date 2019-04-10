package my.sheshenya.samplenettyrestfulapi.logic;

import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryTransactionRepositoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class SimpleTransferServiceTest {

    private SimpleTransferService transferService;


    @Before
    public void setUp() throws Exception {
        transferService = new SimpleTransferService(new InMemoryTransactionRepositoryImpl()) ;

    }

    @Test
    public void newTransaction() {
        String newTransactionId = transferService.newTransaction(new Transaction("a1", "a2", BigDecimal.valueOf(50)));
        Assert.assertNotNull(newTransactionId);
    }

    @Test
    public void getTransaction_NotExists() {
        Transaction emptyTransaction = transferService.getTransaction("XXX");
        Assert.assertNull(emptyTransaction);
    }

    @Test
    public void getTransaction_Exists() {
        String newTransactionId = transferService.newTransaction(new Transaction("a1", "a2", BigDecimal.valueOf(50)));
        Transaction newTransaction = transferService.getTransaction(newTransactionId);

        Assert.assertNotNull(newTransaction);
        Assert.assertEquals(newTransactionId, newTransaction.getId());
    }
}