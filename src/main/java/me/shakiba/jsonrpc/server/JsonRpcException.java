package me.shakiba.jsonrpc.server;

class JsonRpcException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final JsonRpcError error;
    private final String requestId;

    public JsonRpcException(String requestId, JsonRpcError error,
            Throwable cause) {
        super(error.getMessage(), cause);
        this.requestId = requestId;
        this.error = error;
    }

    public JsonRpcException(String requestId, JsonRpcError error) {
        super(error.getMessage());
        this.requestId = requestId;
        this.error = error;
    }

    public JsonRpcError getError() {
        return error;
    }

    public String getRequestId() {
        return requestId;
    }
}