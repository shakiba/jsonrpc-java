JSON-RPC for Java
============

Minimal and easy to use JSONRPC implementation for Java.

#### Usage

```java
// create once
JsonRpcServer server = new JsonRpcServer();

// jsonreq can be String, InputStream or Reader
// service is the Object that json request is executed against
String jsonres = server.serve(service, jsonreq).toJson();

```

Please see [TestService](src/test/java/me/shakiba/jsonrpc/server/TestService.java) and [test cases](src/test/java/me/shakiba/jsonrpc/server/cases.yaml) for example.

    
#### Maven

Simply use Maven to add it to your project.

```xml
<dependency>
    <groupId>me.shakiba.jsonrpc</groupId>
    <artifactId>jsonrpc</artifactId>
    <version>0.1.0</version>
</dependency>
```
