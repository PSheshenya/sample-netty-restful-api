package my.sheshenya.samplenettyrestfulapi.logic;

import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import my.sheshenya.samplenettyrestfulapi.repository.AccountRepository;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleTransactionRunnerTest {
    private static final int NUM_THREADS = 1000;

    AccountRepository accountRepository;

    @Before
    public void setUp() throws Exception {

        accountRepository = new InMemoryAccountRepositoryImpl();
        accountRepository.add(new Account("a1", "First account", BigDecimal.valueOf(200)));
        accountRepository.add(new Account("a2", "Second account", BigDecimal.valueOf(200)));
        accountRepository.add(new Account("a3", "3th account", BigDecimal.valueOf(200)));
        accountRepository.add(new Account("a4", "4th account", BigDecimal.valueOf(200)));
        accountRepository.add(new Account("a5", "5th account", BigDecimal.valueOf(200)));
        accountRepository.add(new Account("a6", "6th account", BigDecimal.valueOf(200)));
        accountRepository.add(new Account("a7", "7th account", BigDecimal.valueOf(200)));
        accountRepository.add(new Account("a8", "8th account", BigDecimal.valueOf(200)));
        accountRepository.add(new Account("a9", "9th account", BigDecimal.valueOf(200)));
        accountRepository.add(new Account("a10", "10th account", BigDecimal.valueOf(200)));
    }

    Transaction getRegisteredTransaction(String from, String to, BigDecimal amount) {
        return new Transaction(UUID.randomUUID().toString(), new Date(), from, to, amount, "New");
    }

    @Test
    public void doTransaction_once() throws ExecutionException, InterruptedException {
        BigDecimal beforeTotal = accountRepository.getTotalBalance();

        Transaction registeredTransaction = getRegisteredTransaction("a1", "a2", BigDecimal.valueOf(100));

        CompletableFuture<Void> future = CompletableFuture
                .runAsync(() ->
                        {
                            SimpleTransactionRunner runner = new SimpleTransactionRunner(registeredTransaction);
                            runner.setAccountRepository(accountRepository);
                            runner.run();
                        }
                        , Executors.newCachedThreadPool());
        // Блокировка и ожидание завершения Future
        future.get();

        BigDecimal afterTotal = accountRepository.getTotalBalance();

        Assert.assertEquals(beforeTotal, afterTotal );
        Assert.assertEquals(BigDecimal.valueOf(2000), afterTotal);
    }


    @Test
    public void doTransaction_many() throws InterruptedException, ExecutionException {
        BigDecimal beforeTotal = accountRepository.getTotalBalance();

        ExecutorService executor = Executors.newCachedThreadPool();
        List<CompletableFuture<Void>> runnableList = new ArrayList<>();

        Random r = new Random();
        for(int i = 0; i < NUM_THREADS; i++) {
            int fromInsex = r.nextInt(11);
            int toInsex = r.nextInt(11);
            BigDecimal ammount = BigDecimal.valueOf(r.nextInt(150));

            Transaction registeredTransaction = getRegisteredTransaction("a" + fromInsex, "a" + toInsex, ammount);
            CompletableFuture<Void> future = CompletableFuture
                    .runAsync(() ->  {
                        SimpleTransactionRunner runner = new SimpleTransactionRunner(registeredTransaction);
                        runner.setAccountRepository(accountRepository);
                        runner.run();
                    }, executor);

            runnableList.add(future);
        }

//        executor.invokeAll(runnableList)
//                .stream()
//                .map(future -> {
//                    try {
//                        return future.get();
//                    }
//                    catch (Exception e) {
//                        throw new IllegalStateException(e);
//                    }
//                }).close();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(runnableList.toArray(new CompletableFuture[runnableList.size()]));
        allFutures.get();

        BigDecimal afterTotal = accountRepository.getTotalBalance();

        Assert.assertEquals(beforeTotal, afterTotal);
        Assert.assertEquals(BigDecimal.valueOf(2000), afterTotal);
    }
}