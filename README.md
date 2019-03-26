# sample-netty-restful-api
Sample project that shows how to create microservice on Netty without using Spring

In this project I going to use [Reactor Netty](https://github.com/reactor/reactor-netty) as a server

Check all my dependency
```
$ mvn dependency:tree

[INFO] ---------------< my.sheshenya:sample-netty-restful-api >----------------
[INFO] Building sample-netty-restful-api 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ sample-netty-restful-api ---
[INFO] my.sheshenya:sample-netty-restful-api:jar:1.0-SNAPSHOT
[INFO] +- io.projectreactor.netty:reactor-netty:jar:0.8.5.RELEASE:compile
[INFO] |  +- io.netty:netty-codec-http:jar:4.1.33.Final:compile
[INFO] |  |  +- io.netty:netty-common:jar:4.1.33.Final:compile
[INFO] |  |  +- io.netty:netty-buffer:jar:4.1.33.Final:compile
[INFO] |  |  +- io.netty:netty-transport:jar:4.1.33.Final:compile
[INFO] |  |  |  \- io.netty:netty-resolver:jar:4.1.33.Final:compile
[INFO] |  |  \- io.netty:netty-codec:jar:4.1.33.Final:compile
[INFO] |  +- io.netty:netty-codec-http2:jar:4.1.33.Final:compile
[INFO] |  +- io.netty:netty-handler:jar:4.1.33.Final:compile
[INFO] |  +- io.netty:netty-handler-proxy:jar:4.1.33.Final:compile
[INFO] |  |  \- io.netty:netty-codec-socks:jar:4.1.33.Final:compile
[INFO] |  +- io.netty:netty-transport-native-epoll:jar:linux-x86_64:4.1.33.Final:compile
[INFO] |  |  \- io.netty:netty-transport-native-unix-common:jar:4.1.33.Final:compile
[INFO] |  \- io.projectreactor:reactor-core:jar:3.2.6.RELEASE:compile
[INFO] |     \- org.reactivestreams:reactive-streams:jar:1.0.2:compile
[INFO] \- junit:junit:jar:4.11:test
[INFO]    \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] ------------------------------------------------------------------------

```



Request
```
$ http -v POST :8080/moneytransfers account-source-id=123, account-target-id=456, amount=100
POST /moneytransfers HTTP/1.1
Accept: application/json, */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 75
Content-Type: application/json
Host: localhost:8080
User-Agent: HTTPie/1.0.2

{
    "account-from-id": "123,",
    "account-to-id": "456,",
    "amount": "100"
}

HTTP/1.1 200 OK
transfer-encoding: chunked

{"account-source-id": "123,", "account-target-id": "456,", "amount": "100"}
```