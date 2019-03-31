package my.sheshenya.samplenettyrestfulapi;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import my.sheshenya.samplenettyrestfulapi.encoders.AccountEncoder;
import my.sheshenya.samplenettyrestfulapi.encoders.TransactionEncoder;
import my.sheshenya.samplenettyrestfulapi.logic.SimpleAccountService;
import my.sheshenya.samplenettyrestfulapi.logic.SimpleTransactionListener;
import my.sheshenya.samplenettyrestfulapi.logic.SimpleTransferService;
import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl;
import my.sheshenya.samplenettyrestfulapi.repository.InMemoryTransactionRepositoryImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.ByteBufMono;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.nio.charset.Charset;
import java.time.Duration;

public class SampleNettyRestfullApiApplicationTests {

    /*
     * https://github.com/reactor/reactor-netty/blob/master/src/test/java/reactor/netty/http/client/HttpClientTest.java
     */


    @Test
    public void GET_helthcheck() {
        DisposableServer server =
                HttpServer
                        .create()   // Prepares an HTTP server ready for configuration
                        .port(0)    // Configures the port number as zero, this will let the system pick up
                        // an ephemeral port when binding the server
                        .wiretap(true) // 	Enables the wire logging
                        //.host("localhost")
                        .route(routes -> routes
                                //helth check
                                .get("/helth", (request, response) -> response.sendString(Mono.just("OK"))))
                        .bindNow();

        ConnectionProvider pool = ConnectionProvider.fixed("test", 1);
        HttpClient httpClient = createHttpClientForContextWithPort(server, pool);

        Mono<String> mono_helth =
                httpClient
                        .get()
                        .uri("/helth")
                        .responseSingle((r, buf) -> buf.asString())
                        .log("mono_helth");

        StepVerifier.create(mono_helth)
                .expectNextMatches(s -> s.equals("OK"))
                .expectComplete()
                .verify(Duration.ofSeconds(3));

        server.disposeNow();
    }

    @Test
    public void GET_account() {
        SimpleAccountService accountService = new SimpleAccountService(new InMemoryAccountRepositoryImpl());

        DisposableServer server =
                HttpServer
                        .create()   // Prepares an HTTP server ready for configuration
                        .port(0)    // Configures the port number as zero, this will let the system pick up
                        // an ephemeral port when binding the server
                        .wiretap(true) // 	Enables the wire logging
                        //.host("localhost")
                        .route(routes -> routes
                                //get accountId
                                .get("/account/{accountId}",  (request, response) ->
                                {
                                    Account account = accountService.getAccount(request.param("accountId"));

                                    if (account != null) {
                                        return response
                                                .send(ByteBufFlux
                                                        .fromInbound(Mono.just(TransactionEncoder.toByteBuf(account)))
                                                        .retain());
                                    }
                                    return response.sendNotFound().log("http-server");
                                }))
                                .bindNow();



        ConnectionProvider pool = ConnectionProvider.fixed("test", 1);
        HttpClient httpClient = createHttpClientForContextWithPort(server, pool);

        Flux<Account> mono_account =
                httpClient
                        .get()
                        .uri("/account/a1")
                        .response((r, buf) -> {
                            ByteBufMono bMono = buf.aggregate().retain();
                            return bMono.flatMap(byteBuf -> Mono.just(AccountEncoder.fromByteBuf(byteBuf)));

                        })
                        .log("mono_account");

        StepVerifier.create(mono_account)
                .expectNextMatches(account -> account.getId().equals("a1"))
                .expectComplete()
                .verify(Duration.ofSeconds(3));

        server.disposeNow();
    }


    @Test
    public void GET_transaction_unknown() {
        SimpleTransferService transferService = new SimpleTransferService(
                new InMemoryTransactionRepositoryImpl()
                ,new SimpleTransactionListener(new InMemoryAccountRepositoryImpl()));

        DisposableServer server =
                HttpServer
                        .create()   // Prepares an HTTP server ready for configuration
                        .port(0)    // Configures the port number as zero, this will let the system pick up
                        // an ephemeral port when binding the server
                        .wiretap(true) // 	Enables the wire logging
                        //.host("localhost")
                        .route(routes -> routes
                                // getters
                                .get("/transaction/{transactionId}",  (request, response) ->
                                {
                                    Transaction transaction = transferService.getTransaction(request.param("transactionId"));

                                    if (transaction != null) {
                                        return response
                                                .send(ByteBufFlux
                                                        .fromInbound(Mono.just(TransactionEncoder.toByteBuf(transaction)))
                                                        .retain());
                                    }
                                    return response.sendNotFound().log("http-server");
                                }))
                        .bindNow();



        ConnectionProvider pool = ConnectionProvider.fixed("test", 1);
        HttpClient httpClient = createHttpClientForContextWithPort(server, pool);

        Flux<Integer> responseStatus_transaction =
                httpClient
                        .get()
                        .uri("/transaction/xxx")
                        .response((r, buf) -> Mono.just(r.status().code()))
                        .log("responseStatus_transactionId");

        StepVerifier.create(responseStatus_transaction)
                .expectNextMatches(status -> status > 400)
                .expectComplete()
                .verify();

        server.disposeNow();
    }

    @Test
    public void POST_transaction() {

        SimpleTransferService transferService = new SimpleTransferService(
                new InMemoryTransactionRepositoryImpl()
                ,new SimpleTransactionListener(new InMemoryAccountRepositoryImpl()));


        DisposableServer server =
                HttpServer
                        .create()   // Prepares an HTTP server ready for configuration
                        .port(0)    // Configures the port number as zero, this will let the system pick up
                        // an ephemeral port when binding the server
                        .wiretap(true) // 	Enables the wire logging
                        //.host("localhost")
                        .route(routes -> routes

                                // transaction create
                                .post("/transaction", (request, response) ->
                                {
                                    return response.sendString(
                                            request.receive()
                                                    .map(buf -> TransactionEncoder.fromByteBuf(buf))
                                                    .flatMap(t -> {
                                                        if (t instanceof Transaction) {
                                                            return Mono.just(transferService.newTransaction(t));
                                                        }
                                                        else {
                                                            return Mono.error(
                                                                    new Exception("Unexpected type of the message: " + t));
                                                        }

                                                    }));
                                }))
                        .bindNow();

        ConnectionProvider pool = ConnectionProvider.fixed("test", 1);
        HttpClient httpClient = createHttpClientForContextWithPort(server, pool);


        Transaction postTransaction  = new Transaction("a1", "a2", 100);

        Flux<Integer> responseStatus_transactionId =
                httpClient
                        .post()
                        .uri("/transaction")
                        .send((r, out) -> out.send(Mono.just(TransactionEncoder.toByteBuf(postTransaction))))
                        .response((r, buf) -> Mono.just(r.status().code()))
                        .log("responseStatus_transactionId");

        StepVerifier.create(responseStatus_transactionId)
                .expectNextMatches(status -> status >= 200 && status < 400)
                .expectComplete()
                .verify();






        Mono<String> mono_transactionId =
                httpClient
                        .post()
                        .uri("/transaction")
                        .send((r, out) -> out.send(Mono.just(TransactionEncoder.toByteBuf(postTransaction))))
                        .responseSingle((r, buf) -> buf.asString())
                        .log("mono_transactionId");

        StepVerifier.create(mono_transactionId)
                .expectNextMatches(transactionId -> !transactionId.isEmpty() )
                .expectComplete()
                .verify(Duration.ofSeconds(5));

        server.disposeNow();
    }






    private HttpClient createHttpClientForContextWithPort(DisposableServer context) {
        return createHttpClientForContextWithPort(context, null);
    }

    private HttpClient createHttpClientForContextWithPort(DisposableServer context,
                                                          ConnectionProvider pool) {
        HttpClient client;
        if (pool == null) {
            client = HttpClient.create();
        }
        else {
            client = HttpClient.create(pool);
        }
        return client.port(context.port())
                .wiretap(true);
    }
}
