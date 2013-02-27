package me.shakiba.jsonrpc.server;

public class JsonRpcRequest {
    private String id;
    private String method;
    private Object[] params;

    public String getId() {
        return id;
    }

    public JsonRpcRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public JsonRpcRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public Object[] getParams() {
        return params;
    }

    public JsonRpcRequest setParams(Object[] params) {
        this.params = params;
        return this;
    }
}