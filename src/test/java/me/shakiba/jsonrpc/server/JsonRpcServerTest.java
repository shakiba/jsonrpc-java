package me.shakiba.jsonrpc.server;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

public class JsonRpcServerTest {
    @Test
    public void simple() throws IOException {
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        List<String> cases = (List<String>) yaml.load(JsonRpcServerTest.class
                .getResourceAsStream("cases.yaml"));

        TestService service = new TestService();
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
        for (int i = 0; i < cases.size(); i += 2) {
            String req = cases.get(i);
            String expectedRes = cases.get(i + 1);
            String actualRes = server.serve(service, req).toJson();
            logger.debug(actualRes);
            Assert.assertEquals(actualRes, expectedRes);
        }
    }

    @Test
    public static void innerClass() throws IOException {

        class InnerClass implements ParentClass {
            @Override
            public boolean echo(boolean x) {
                return x;
            }
        }
        JsonRpcServer server = new JsonRpcServer();
        String req = "{\"method\":\"echo\",\"params\":[true],\"id\":0}";
        String expected = "{\"id\":\"0\",\"result\":true}";
        String actual = server.serve(new InnerClass(), req).toJson();
        logger.debug(actual);
        Assert.assertEquals(actual, expected);
    }

    public interface ParentClass {
        boolean echo(boolean x);
    }

    private static Logger logger = Logger.getLogger(JsonRpcServerTest.class);
}
