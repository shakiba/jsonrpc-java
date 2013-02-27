jsonrpc-java
============

JSONRPC for Java

#### Usage

    // create once
    private JsonRpcServer server = new JsonRpcServer();
    
    // jsonreq can be String, InputStream or Reader
    // service is the Object that json request is executed against
    String jsonres = server.serve(service, jsonreq).toJson();
    
    