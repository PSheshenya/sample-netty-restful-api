package my.sheshenya.samplenettyrestfulapi;

import com.google.inject.AbstractModule;

import com.google.inject.Singleton;
import my.sheshenya.samplenettyrestfulapi.logic.*;
import my.sheshenya.samplenettyrestfulapi.repository.AccountRepository;
import my.sheshenya.samplenettyrestfulapi.repository.TransactionRepository;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryTransactionRepositoryImpl;


public class TransferModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(TransferService.class).to(SimpleTransferService.class).in(Singleton.class);
        bind(AccountService.class).to(SimpleAccountService.class).in(Singleton.class);


        bind(AccountRepository.class).to(InMemoryAccountRepositoryImpl.class).in(Singleton.class);

        bind(TransactionRepository.class).to(InMemoryTransactionRepositoryImpl.class).in(Singleton.class);
        bind(TransactionListener.class).to(SimpleTransactionListener.class).in(Singleton.class);
    }
}