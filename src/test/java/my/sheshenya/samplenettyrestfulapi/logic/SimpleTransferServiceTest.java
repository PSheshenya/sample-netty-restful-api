package my.sheshenya.samplenettyrestfulapi.logic;

import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryTransactionRepositoryImpl;
import my.sheshenya.samplenettyrestfulapi.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTransferServiceTest {

    private SimpleTransferService transferService;


    @Before
    public void setUp() throws Exception {
        transferService = new SimpleTransferService(new InMemoryTransactionRepositoryImpl(), new SimpleTransactionListener(new InMemoryAccountRepositoryImpl())) ;

    }

    @Test
    public void newTransaction() {
        String newTransactionId = transferService.newTransaction(new Transaction("a1", "a2", 50));
        Assert.assertNotNull(newTransactionId);
    }

    @Test
    public void getTransaction_NotExists() {
        Transaction emptyTransaction = transferService.getTransaction("XXX");
        Assert.assertNull(emptyTransaction);
    }

    public void getTransaction_Exists() {
        String newTransactionId = transferService.newTransaction(new Transaction("a1", "a2", 50));
        Transaction newTransaction = transferService.getTransaction(newTransactionId);

        Assert.assertNotNull(newTransaction);
        Assert.assertEquals(newTransactionId, newTransaction.getId());
    }
}