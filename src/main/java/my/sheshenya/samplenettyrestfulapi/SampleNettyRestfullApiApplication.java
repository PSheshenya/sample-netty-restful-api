package my.sheshenya.samplenettyrestfulapi;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.netty.handler.codec.http.HttpResponseStatus;
import my.sheshenya.samplenettyrestfulapi.encoders.TransactionEncoder;
import my.sheshenya.samplenettyrestfulapi.logic.AccountService;
import my.sheshenya.samplenettyrestfulapi.logic.TransferService;
import my.sheshenya.samplenettyrestfulapi.model.Account;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;


public class SampleNettyRestfullApiApplication {

	public static void main(String[] args) throws IOException, InterruptedException {

		/*
		 * Guice.createInjector() takes your Modules, and returns a new Injector
		 * instance. Most applications will call this method exactly once, in their
		 * main() method.
		 */
		Injector injector = Guice.createInjector(new TransferModule());

		/*
		 * Now that we've got the injector, we can build objects.
		 */
		TransferService transferService = injector.getInstance(TransferService.class);
		AccountService accountService = injector.getInstance(AccountService.class);





		DisposableServer server =
				HttpServer
						.create()   // Prepares an HTTP server ready for configuration
						//.port(0)    // Configures the port number as zero, this will let the system pick up
						// an ephemeral port when binding the server
						.wiretap(true) // 	Enables the wire logging
						.host("localhost").port(8080)
				.route(routes -> routes
						//helth check
						.get("/helth", (request, response) -> response.sendString(Mono.just("OK")))
						.post("/echo", (request, response) -> response.send(request.receive().retain()))

						// getters
						.get("/transaction/{transactionId}",  (request, response) ->
						{
							//Transaction transaction = new Transaction("1", new Date(), "a1", "a2", 1212, "SSS");
							//ByteBuf buf = Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8);
							Transaction transaction = transferService.getTransaction(request.param("transactionId"));

							if (transaction != null) {
								return response
										.send(ByteBufFlux
												.fromInbound(Mono.just(TransactionEncoder.toByteBuf(transaction)))
												.retain());
							}
							return response.sendNotFound().log("http-server");
						})
						.get("/transaction/{transactionId}/status",  (request, response) ->
						{
							Transaction transaction = transferService.getTransaction(request.param("transactionId"));

							if (transaction != null) {
								return response
										.status(HttpResponseStatus.OK)
										.header("transactionId", request.param("transactionId"))
										.sendString(Mono.just(transaction.getStatus()));
							}
							return response.sendNotFound().log("http-server");
						})
						// getters
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
						})




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
						})


				)
				.bindNow(); // Starts the server in a blocking fashion, and waits for it to finish its initialization



		server.onDispose()
				.block();

		//Thread.currentThread().join();
	}



}
