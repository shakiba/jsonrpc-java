package me.shakiba.jsonrpc.server;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonRpcServerTest {
    @Test
    public void simple() throws IOException {
        String[] cases = TestService.cases;
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
        for (int i = 0; i < cases.length; i += 2) {
            String req = cases[i].replace("#", i / 2 + "");
            String expectedRes = cases[i + 1].replace("#", i / 2 + "");
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
