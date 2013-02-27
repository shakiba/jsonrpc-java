package me.shakiba.jsonrpc.server;

public class DuplicateMethodName extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateMethodName(String msg) {
        super(msg);
    }
}
