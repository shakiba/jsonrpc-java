package me.shakiba.jsonrpc.server;

public class TestException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String name;

    public TestException(String msg, String name) {
        super(msg);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
