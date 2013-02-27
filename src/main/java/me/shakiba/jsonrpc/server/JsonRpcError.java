package me.shakiba.jsonrpc.server;

/**
 * Server error -32099 to -32000 Reserved for implementation-defined
 * server-errors.
 */
public class JsonRpcError {
    private int code;
    private String name, message;

    public JsonRpcError(int code, String name, String message) {
        this.code = code;
        this.name = name;
        this.message = message;
    }

    public JsonRpcError(String name, String message) {
        this(-1, name, message);
    }

    public JsonRpcError(int code, String message) {
        this(code, "", message);
    }

    public JsonRpcError(String message) {
        this(-1, message);
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Invalid JSON was received by the server. An error occurred on the server
     * while parsing the JSON text.
     */
    public static final JsonRpcError PARSE_ERROR = new JsonRpcError(-32700,
            "PARSE_ERROR", "Error!");

    /**
     * The JSON sent is not a valid Request object.
     */
    public static final JsonRpcError INVALID_REQUEST = new JsonRpcError(-32600,
            "INVALID_REQUEST", "Error!");

    /**
     * The method does not exist / is not available.
     */
    public static final JsonRpcError METHOD_NOT_FOUND = new JsonRpcError(
            -32601, "METHOD_NOT_FOUND", "Error!");

    /**
     * Invalid method parameter(s).
     */
    public static final JsonRpcError INVALID_PARAMS = new JsonRpcError(-32602,
            "INVALID_PARAMS", "Error!");

    /**
     * Internal JSON-RPC error.
     */
    public static final JsonRpcError INTERNAL_ERROR = new JsonRpcError(-32603,
            "INTERNAL_ERROR", "Error!");
}