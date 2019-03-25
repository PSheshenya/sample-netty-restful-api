package my.sheshenya.samplenettyrestfulapi;


import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import java.io.IOException;


public class SampleNettyRestfullApiApplication {

	public static void main(String[] args) throws IOException, InterruptedException {


		DisposableServer server =
				HttpServer
				.create()   // Prepares an HTTP server ready for configuration
				//.port(0)    // Configures the port number as zero, this will let the system pick up
				// an ephemeral port when binding the server
				.host("localhost").port(8080)
				.route(routes ->
						// The server will respond only on POST requests
						// where the path starts with /test and then there is path parameter
						routes.post("/moneytransfers", (request, response) ->
								response.sendString(request.receive()
										.asString()
										.map(s -> s + ' ' + request.param("param") + '!')
										.log("http-server"))))
				.bindNow(); // Starts the server in a blocking fashion, and waits for it to finish its initialization



		server.onDispose()
				.block();

		//Thread.currentThread().join();
	}



}
