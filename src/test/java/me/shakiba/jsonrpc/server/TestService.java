package me.shakiba.jsonrpc.server;

import java.util.List;
import java.util.Map;

public class TestService {
    public boolean echoBool(boolean b) {
        return b;
    }

    public String echoStr(int x, int y) {
        return x + y + "";
    }

    public Object echoObj(Object obj) {
        return obj;
    }

    public List<String> echoList(List<String> list) {
        return list;
    }

    public Map<String, Integer> echoMap(Map<String, Integer> map) {
        return map;
    }

    public String exception(String msg, String name) throws TestException {
        throw new TestException(msg, name);
    }

    public String internalError(String msg) {
        throw new RuntimeException(msg);
    }

    public static final String[] cases = {
            "{\"id\":#,\"method\":\"echoBool\",\"params\":[true]}",
            "{\"id\":\"#\",\"result\":true}",

            "{\"id\":#,\"method\":\"echoStr\",\"params\":[1, 9]}",
            "{\"id\":\"#\",\"result\":\"10\"}",

            "{\"id\":#,\"method\":\"echoObj\",\"params\":[null]}",
            "{\"id\":\"#\",\"result\":null}",

            "{\"id\":#,\"method\":\"echoList\",\"params\":[[\"a\", \"b\", \"c\", \"d\"]]}",
            "{\"id\":\"#\",\"result\":[\"a\",\"b\",\"c\",\"d\"]}",

            "{\"id\":#,\"method\":\"echoMap\",\"params\":[{\"a\":1, \"b\":2, \"c\":3, \"d\":4}]}",
            "{\"id\":\"#\",\"result\":{\"a\":1,\"b\":2,\"c\":3,\"d\":4}}",

            "{\"id\":#,\"params\":[true]}",
            "{\"id\":\"#\",\"error\":{\"code\":-32600,\"name\":\"INVALID_REQUEST\",\"message\":\"Error!\"}}",

            "{\"id\":#,\"method\":\"echoBool\"}",
            "{\"id\":\"#\",\"error\":{\"code\":-32600,\"name\":\"INVALID_REQUEST\",\"message\":\"Error!\"}}",

            "{\"id\":#,\"method\":\"echo\",\"params\":[true]}",
            "{\"id\":\"#\",\"error\":{\"code\":-32601,\"name\":\"METHOD_NOT_FOUND\",\"message\":\"Error!\"}}",

            "{\"id\":#,\"method\":\"echoMap\",\"params\":[true]}",
            "{\"id\":\"#\",\"error\":{\"code\":-32602,\"name\":\"INVALID_PARAMS\",\"message\":\"Error!\"}}",

            "{\"id\":#,\"method\":\"exception\",\"params\":[\"Errrr!\",\"TEST_ERROR\"]}",
            "{\"id\":\"#\",\"error\":{\"code\":-1,\"name\":\"TEST_ERROR\",\"message\":\"Errrr!\"}}",

            "{\"id\":#,\"method\":\"internalError\",\"params\":[\"Errrr!\"]}",
            "{\"id\":\"#\",\"error\":{\"code\":-32603,\"name\":\"INTERNAL_ERROR\",\"message\":\"Error!\"}}",

    };

}
