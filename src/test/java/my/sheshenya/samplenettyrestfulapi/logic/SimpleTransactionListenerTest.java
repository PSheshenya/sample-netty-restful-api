package my.sheshenya.samplenettyrestfulapi.logic;

import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import my.sheshenya.samplenettyrestfulapi.repository.AccountRepository;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class SimpleTransactionListenerTest {

    private final int CONCURENT = 10;

    TransactionListener simpleTransactionListener;
    AccountRepository accountRepository;

    @Before
    public void setUp() throws Exception {

        accountRepository = new InMemoryAccountRepositoryImpl();
        accountRepository.add(new Account("a1", "First account", 200));
        accountRepository.add(new Account("a2", "Second account", 200));
        accountRepository.add(new Account("a3", "3th account", 200));
        accountRepository.add(new Account("a4", "4th account", 200));
        accountRepository.add(new Account("a5", "5th account", 200));
        accountRepository.add(new Account("a6", "6th account", 200));
        accountRepository.add(new Account("a7", "7th account", 200));
        accountRepository.add(new Account("a8", "8th account", 200));
        accountRepository.add(new Account("a9", "9th account", 200));
        accountRepository.add(new Account("a10", "10th account", 200));


        simpleTransactionListener = new SimpleTransactionListener(accountRepository);
    }

    void doTransaction(String from, String to, double amount) {
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), new Date(), from, to, amount, "New");
        simpleTransactionListener.doTransaction(transaction);
    }

    @Test
    public void doTransaction_once() {

        double initTotal = accountRepository.getTotalBalance();
        doTransaction("a1", "a2", 100);
        double finishTotal = accountRepository.getTotalBalance();

        Assert.assertEquals(initTotal, finishTotal,0 );
        Assert.assertEquals(initTotal, 2000,0 );

    }


    @Test
    public void doTransaction_many() throws InterruptedException {

        List<Callable<Integer>> callables = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < CONCURENT; i++)
        {
            int fromInsex = r.nextInt(11);
            int toInsex = r.nextInt(11);
            int ammount = r.nextInt(150);

            callables.add(() -> {
                doTransaction("a" + fromInsex, "a" + toInsex, ammount);
                return 0;
            });

        }


        double initTotal = accountRepository.getTotalBalance();

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }).close();


        double finishTotal = accountRepository.getTotalBalance();

        Assert.assertEquals(initTotal, finishTotal,0 );
        Assert.assertEquals(initTotal, 2000,0 );

    }

}