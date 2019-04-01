# sample-netty-restful-api
Sample project that shows how to create microservice on Netty without using Spring

In this project I going to use [Reactor Netty](https://github.com/reactor/reactor-netty) as a server. Before starting I suggest to read useful documentation [Reactor Netty Reference Guide](https://next.projectreactor.io/docs/netty/snapshot/reference/index.html) and [Reactor Netty Workshop](https://violetagg.github.io/reactor-netty-workshop/).

As a dependency injection framework I choose [Guice](https://github.com/google/guice ) 

The project dependency tree looks like
```
$ mvn dependency:tree

[INFO] ---------------< my.sheshenya:sample-netty-restful-api >----------------
[INFO] Building sample-netty-restful-api 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ sample-netty-restful-api ---
[INFO] my.sheshenya:sample-netty-restful-api:jar:1.0-SNAPSHOT
[INFO] +- io.projectreactor.netty:reactor-netty:jar:0.8.6.RELEASE:compile
[INFO] |  +- io.netty:netty-codec-http:jar:4.1.34.Final:compile
[INFO] |  |  +- io.netty:netty-common:jar:4.1.34.Final:compile
[INFO] |  |  +- io.netty:netty-buffer:jar:4.1.34.Final:compile
[INFO] |  |  +- io.netty:netty-transport:jar:4.1.34.Final:compile
[INFO] |  |  |  \- io.netty:netty-resolver:jar:4.1.34.Final:compile
[INFO] |  |  \- io.netty:netty-codec:jar:4.1.34.Final:compile
[INFO] |  +- io.netty:netty-codec-http2:jar:4.1.34.Final:compile
[INFO] |  +- io.netty:netty-handler:jar:4.1.34.Final:compile
[INFO] |  +- io.netty:netty-handler-proxy:jar:4.1.34.Final:compile
[INFO] |  |  \- io.netty:netty-codec-socks:jar:4.1.34.Final:compile
[INFO] |  +- io.netty:netty-transport-native-epoll:jar:linux-x86_64:4.1.34.Final:compile
[INFO] |  |  \- io.netty:netty-transport-native-unix-common:jar:4.1.34.Final:compile
[INFO] |  \- io.projectreactor:reactor-core:jar:3.2.8.RELEASE:compile
[INFO] |     \- org.reactivestreams:reactive-streams:jar:1.0.2:compile
[INFO] +- com.google.inject:guice:jar:4.2.2:compile
[INFO] |  +- javax.inject:javax.inject:jar:1:compile
[INFO] |  +- aopalliance:aopalliance:jar:1.0:compile
[INFO] |  \- com.google.guava:guava:jar:25.1-android:compile
[INFO] |     +- com.google.code.findbugs:jsr305:jar:3.0.2:compile
[INFO] |     +- org.checkerframework:checker-compat-qual:jar:2.0.0:compile
[INFO] |     +- com.google.errorprone:error_prone_annotations:jar:2.1.3:compile
[INFO] |     +- com.google.j2objc:j2objc-annotations:jar:1.1:compile
[INFO] |     \- org.codehaus.mojo:animal-sniffer-annotations:jar:1.14:compile
[INFO] +- com.fasterxml.jackson.core:jackson-databind:jar:2.9.8:compile
[INFO] |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.9.0:compile
[INFO] |  \- com.fasterxml.jackson.core:jackson-core:jar:2.9.8:compile
[INFO] +- org.projectlombok:lombok:jar:1.18.6:provided
[INFO] \- junit:junit:jar:4.11:test
[INFO]    \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] ------------------------------------------------------------------------

```

##Tests

The main test is in **SimpleTransactionListenerTest** class and **doTransaction_many** method.
Exactly this one test describe concurrency

The project has two type resources: **transaction** and **account**

###creating a transaction
The POST request returns transaction id in response 
```
sheshenya@mbp-sheshenya: ~/sample-netty-restful-api (master) $ http -v POST :8080/transaction  "fromAccountId"="a1" "toAccountId"="a2" "amount"=12
POST /transaction HTTP/1.1
Accept: application/json, */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 60
Content-Type: application/json
Host: localhost:8080
User-Agent: HTTPie/1.0.2

{
    "amount": "12",
    "fromAccountId": "a1",
    "toAccountId": "a2"
}

HTTP/1.1 200 OK
transfer-encoding: chunked

85602986-91ac-4b28-9fd1-73cf6df9b739
```

###info about transaction
The GET request with transaction id returns transaction in response 
```
sheshenya@mbp-sheshenya: ~/sample-netty-restful-api (master) $ http -v :8080/transaction/85602986-91ac-4b28-9fd1-73cf6df9b739
GET /transaction/85602986-91ac-4b28-9fd1-73cf6df9b739 HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:8080
User-Agent: HTTPie/1.0.2



HTTP/1.1 200 OK
transfer-encoding: chunked

{"id":"85602986-91ac-4b28-9fd1-73cf6df9b739","date":"2019-03-31 00:48 MSK","fromAccountId":"a1","toAccountId":"a2","amount":12.0,"status":"Done"}

```
or just transaction **status** in response 
```
sheshenya@mbp-sheshenya: ~/sample-netty-restful-api (master) $ http -v :8080/transaction/85602986-91ac-4b28-9fd1-73cf6df9b739/status
GET /transaction/85602986-91ac-4b28-9fd1-73cf6df9b739/status HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:8080
User-Agent: HTTPie/1.0.2



HTTP/1.1 200 OK
transfer-encoding: chunked

Done

```
###info about account
Also we can check account

```
sheshenya@mbp-sheshenya: ~/sample-netty-restful-api (master) $ http -v :8080/account/a1
GET /account/a1 HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:8080
User-Agent: HTTPie/1.0.2



HTTP/1.1 200 OK
transfer-encoding: chunked

{"id":"a1","name":"First account","balance":40.0}
```

Typical app logs look like

```
/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/bin/java -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:58616,suspend=y,server=n -javaagent:/Users/sheshenya/Library/Caches/IntelliJIdea2018.3/captureAgent/debugger-agent.jar -Dfile.encoding=UTF-8 -classpath "/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_172.jdk/Contents/Home/lib/tools.jar:/Users/sheshenya/Documents/GitHubProjects/sample-netty-restful-api/target/classes:/Users/sheshenya/.m2/repository/io/projectreactor/netty/reactor-netty/0.8.6.RELEASE/reactor-netty-0.8.6.RELEASE.jar:/Users/sheshenya/.m2/repository/io/netty/netty-codec-http/4.1.34.Final/netty-codec-http-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-common/4.1.34.Final/netty-common-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-buffer/4.1.34.Final/netty-buffer-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-transport/4.1.34.Final/netty-transport-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-resolver/4.1.34.Final/netty-resolver-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-codec/4.1.34.Final/netty-codec-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-codec-http2/4.1.34.Final/netty-codec-http2-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-handler/4.1.34.Final/netty-handler-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-handler-proxy/4.1.34.Final/netty-handler-proxy-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-codec-socks/4.1.34.Final/netty-codec-socks-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/netty/netty-transport-native-epoll/4.1.34.Final/netty-transport-native-epoll-4.1.34.Final-linux-x86_64.jar:/Users/sheshenya/.m2/repository/io/netty/netty-transport-native-unix-common/4.1.34.Final/netty-transport-native-unix-common-4.1.34.Final.jar:/Users/sheshenya/.m2/repository/io/projectreactor/reactor-core/3.2.8.RELEASE/reactor-core-3.2.8.RELEASE.jar:/Users/sheshenya/.m2/repository/org/reactivestreams/reactive-streams/1.0.2/reactive-streams-1.0.2.jar:/Users/sheshenya/.m2/repository/com/google/inject/guice/4.2.2/guice-4.2.2.jar:/Users/sheshenya/.m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar:/Users/sheshenya/.m2/repository/aopalliance/aopalliance/1.0/aopalliance-1.0.jar:/Users/sheshenya/.m2/repository/com/google/guava/guava/25.1-android/guava-25.1-android.jar:/Users/sheshenya/.m2/repository/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar:/Users/sheshenya/.m2/repository/org/checkerframework/checker-compat-qual/2.0.0/checker-compat-qual-2.0.0.jar:/Users/sheshenya/.m2/repository/com/google/errorprone/error_prone_annotations/2.1.3/error_prone_annotations-2.1.3.jar:/Users/sheshenya/.m2/repository/com/google/j2objc/j2objc-annotations/1.1/j2objc-annotations-1.1.jar:/Users/sheshenya/.m2/repository/org/codehaus/mojo/animal-sniffer-annotations/1.14/animal-sniffer-annotations-1.14.jar:/Users/sheshenya/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.9.8/jackson-databind-2.9.8.jar:/Users/sheshenya/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar:/Users/sheshenya/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.9.8/jackson-core-2.9.8.jar:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar" my.sheshenya.samplenettyrestfulapi.SampleNettyRestfullApiApplication
Connected to the target VM, address: '127.0.0.1:58616', transport: 'socket'
мар 31, 2019 1:15:37 PM my.sheshenya.samplenettyrestfulapi.repository.InMemoryTransactionRepositoryImpl <init>
INFO: InMemoryTransactionRepositoryImpl instantiated
мар 31, 2019 1:15:37 PM my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl <init>
INFO: InMemoryAccountRepositoryImpl instantiated
мар 31, 2019 1:15:37 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleTransactionListener <init>
INFO: SimpleTransactionListener instantiated
мар 31, 2019 1:15:37 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleTransferService <init>
INFO: SimpleTransferService instantiated
мар 31, 2019 1:15:37 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleAccountService <init>
INFO: SimpleAccountService instantiated
мар 31, 2019 1:15:54 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleAccountService getAccount
INFO: Get account  a1
мар 31, 2019 1:15:54 PM my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl getAccountById
INFO: Get account by id a1
мар 31, 2019 1:15:59 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleAccountService getAccount
INFO: Get account  a2
мар 31, 2019 1:15:59 PM my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl getAccountById
INFO: Get account by id a2
мар 31, 2019 1:16:10 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleTransferService newTransaction
INFO: New transaction request...
мар 31, 2019 1:16:10 PM my.sheshenya.samplenettyrestfulapi.repository.InMemoryTransactionRepositoryImpl createTransaction
INFO: New transaction persisted in repository: id=6e470d5b-5354-49c6-bb3e-e1c6ca36d72b,date=Sun Mar 31 13:16:10 MSK 2019,fromAccountId=a1,toAccountId=a2,amount=12.0,status=New
мар 31, 2019 1:16:10 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleTransferService newTransaction
INFO: Transaction registered as Id='6e470d5b-5354-49c6-bb3e-e1c6ca36d72b'
мар 31, 2019 1:16:10 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleTransactionListener doTransaction
INFO: Transaction starting... 6e470d5b-5354-49c6-bb3e-e1c6ca36d72b
мар 31, 2019 1:16:10 PM my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl getAccountById
INFO: Get account by id a1
мар 31, 2019 1:16:10 PM my.sheshenya.samplenettyrestfulapi.repository.InMemoryAccountRepositoryImpl getAccountById
INFO: Get account by id a2
мар 31, 2019 1:16:20 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleTransactionListener doTransaction
INFO: Transaction Finish 6e470d5b-5354-49c6-bb3e-e1c6ca36d72b
мар 31, 2019 1:16:40 PM my.sheshenya.samplenettyrestfulapi.logic.SimpleTransferService getTransaction
INFO: Get transaction  6e470d5b-5354-49c6-bb3e-e1c6ca36d72b
мар 31, 2019 1:16:40 PM my.sheshenya.samplenettyrestfulapi.repository.InMemoryTransactionRepositoryImpl getTransactionById
INFO: Get transaction by id 6e470d5b-5354-49c6-bb3e-e1c6ca36d72b
```