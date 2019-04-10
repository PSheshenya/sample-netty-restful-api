package my.sheshenya.samplenettyrestfulapi.repository;

import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class InMemoryTransactionRepositoryImplTest {

    InMemoryTransactionRepositoryImpl transactionRepository;

    @Before
    public void setUp() throws Exception {
        transactionRepository = new InMemoryTransactionRepositoryImpl();
    }



    @Test
    public void createTransaction() {
        Transaction transactionRequest = new Transaction("a1", "a2", BigDecimal.valueOf(100));
        Transaction transactionRequestFullfield = transactionRepository.createTransaction(transactionRequest);

        Assert.assertEquals(transactionRequest,transactionRequestFullfield);
    }


    @Test
    public void getTransactionById() {
        Transaction transactionRequest = new Transaction("a1", "a2", BigDecimal.valueOf(100));
        Transaction transactionRequestFullfield = transactionRepository.createTransaction(transactionRequest);

        Transaction transactionFromRepo = transactionRepository.getTransactionById(transactionRequestFullfield.getId());

        Assert.assertNotNull(transactionFromRepo);
        Assert.assertEquals(transactionFromRepo, transactionRequestFullfield);
    }
}