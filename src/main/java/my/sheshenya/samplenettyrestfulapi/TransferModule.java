package my.sheshenya.samplenettyrestfulapi;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import my.sheshenya.samplenettyrestfulapi.logic.AccountService;
import my.sheshenya.samplenettyrestfulapi.logic.SimpleAccountService;
import my.sheshenya.samplenettyrestfulapi.logic.SimpleTransferService;
import my.sheshenya.samplenettyrestfulapi.logic.TransferService;
import my.sheshenya.samplenettyrestfulapi.repository.AccountRepository;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryTransactionRepositoryImpl;
import my.sheshenya.samplenettyrestfulapi.repository.TransactionRepository;


public class TransferModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(TransferService.class).to(SimpleTransferService.class).in(Singleton.class);
        bind(AccountService.class).to(SimpleAccountService.class).in(Singleton.class);

        bind(AccountRepository.class).to(InMemoryAccountRepositoryImpl.class).in(Singleton.class);
        bind(TransactionRepository.class).to(InMemoryTransactionRepositoryImpl.class).in(Singleton.class);
    }
}