jsonrpc-java
============

JSONRPC for Java

#### Usage

```java
// create once
JsonRpcServer server = new JsonRpcServer();

// jsonreq can be String, InputStream or Reader
// service is the Object that json request is executed against
String jsonres = server.serve(service, jsonreq).toJson();

```

See [TestService](src/test/java/me/shakiba/jsonrpc/server/TestService.java) for example.    