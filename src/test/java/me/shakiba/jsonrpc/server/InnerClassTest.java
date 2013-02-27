package me.shakiba.jsonrpc.server;

import java.io.IOException;

import me.shakiba.jsonrpc.server.JsonRpcServer;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InnerClassTest {

    @Test
    public static void test() throws IOException {

        class InnerClass extends ParentClass {
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

    public abstract static class ParentClass {
        public abstract boolean echo(boolean x);
    }

    private static Logger logger = Logger.getLogger(InnerClassTest.class);
}
