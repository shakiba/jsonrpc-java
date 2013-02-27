package me.shakiba.jsonrpc.server;

public class RawJson {
    private final String json;

    public RawJson(String json) {
        this.json = json;
    }

    String getJson() {
        return json;
    }
}