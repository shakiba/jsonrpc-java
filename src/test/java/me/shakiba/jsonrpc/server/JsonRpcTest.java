package me.shakiba.jsonrpc.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import me.shakiba.jsonrpc.server.JsonRpcError;
import me.shakiba.jsonrpc.server.JsonRpcRequest;
import me.shakiba.jsonrpc.server.JsonRpcResponse;
import me.shakiba.jsonrpc.server.JsonRpcServer;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonRpcTest {
    @Test
    public void test() throws IOException {
        JsonRpcServer server = new JsonRpcServer() {
            @Override
            protected boolean onException(JsonRpcRequest req,
                    JsonRpcResponse res, Throwable t) {
                if (!TestException.class.isInstance(t)) {
                    return true;
                }
                TestException ex = (TestException) t;
                res.setError(new JsonRpcError(ex.getName(), ex.getMessage()));
                return false;
            }
        };
        for (int i = 0; i < cases.length; i += 2) {
            String req = cases[i].replace("#", i / 2 + "");
            String expectedRes = cases[i + 1].replace("#", i / 2 + "");
            String actualRes = server.serve(this, req).toJson();
            logger.debug(actualRes);
            Assert.assertEquals(actualRes, expectedRes);
        }
    }

    public boolean echoBool(boolean x) {
        return x;
    }

    public String echoStr(boolean x, String y) {
        return x + y;
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

    class TestException extends Exception {
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

    private static final String[] cases = {
            "{\"method\":\"echoBool\",\"params\":[true],\"id\":#}",
            "{\"id\":\"#\",\"result\":true}",

            "{\"method\":\"echoStr\",\"params\":[true, \"Hiii\"],\"id\":#}",
            "{\"id\":\"#\",\"result\":\"trueHiii\"}",

            "{\"method\":\"echoObj\",\"params\":[null],\"id\":#}",
            "{\"id\":\"#\",\"result\":null}",

            "{\"method\":\"echoList\",\"params\":[[\"a\", \"b\", \"c\", \"d\"]],\"id\":#}",
            "{\"id\":\"#\",\"result\":[\"a\",\"b\",\"c\",\"d\"]}",

            "{\"method\":\"echoMap\",\"params\":[{\"a\":1, \"b\":2, \"c\":3, \"d\":4}],\"id\":#}",
            "{\"id\":\"#\",\"result\":{\"a\":1,\"b\":2,\"c\":3,\"d\":4}}",

            "{\"params\":[true],\"id\":#}",
            "{\"id\":\"#\",\"error\":{\"code\":-32600,\"name\":\"INVALID_REQUEST\",\"message\":\"Error!\"}}",

            "{\"method\":\"echoBool\",\"id\":#}",
            "{\"id\":\"#\",\"error\":{\"code\":-32600,\"name\":\"INVALID_REQUEST\",\"message\":\"Error!\"}}",

            "{\"method\":\"echo\",\"params\":[true],\"id\":#}",
            "{\"id\":\"#\",\"error\":{\"code\":-32601,\"name\":\"METHOD_NOT_FOUND\",\"message\":\"Error!\"}}",

            "{\"method\":\"echoMap\",\"params\":[true],\"id\":#}",
            "{\"id\":\"#\",\"error\":{\"code\":-32602,\"name\":\"INVALID_PARAMS\",\"message\":\"Error!\"}}",

            "{\"method\":\"exception\",\"params\":[\"Errrr!\",\"TEST_ERROR\"],\"id\":#}",
            "{\"id\":\"#\",\"error\":{\"code\":-1,\"name\":\"TEST_ERROR\",\"message\":\"Errrr!\"}}",

            "{\"method\":\"internalError\",\"params\":[\"Errrr!\"],\"id\":#}",
            "{\"id\":\"#\",\"error\":{\"code\":-32603,\"name\":\"INTERNAL_ERROR\",\"message\":\"Error!\"}}",

    };

    private static Logger logger = Logger.getLogger(JsonRpcTest.class);
}
