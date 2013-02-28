package me.shakiba.jsonrpc.server;

public class TestService {
    public Object echo(Object o) {
        return o;
    }

    public boolean paramError(boolean x) {
        return x;
    }

    public String userError(String msg, String name) throws TestException {
        throw new TestException(msg, name);
    }

    public String internalError(String msg) {
        throw new RuntimeException(msg);
    }
}
