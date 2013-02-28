package me.shakiba.jsonrpc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.google.gson.JsonParseException;

/**
 * Overloading is not supported. Only Maps of &lt;String, ?&gt; is supported.
 */
public class JsonRpcServer {

    public JsonRpcResponse serve(Object object, Reader reader)
            throws IOException {
        return serve(object, null, reader);
    }

    public JsonRpcResponse serve(Object object, InputStream in, String encoding)
            throws IOException {
        return serve(object, null, new InputStreamReader(in, encoding));
    }

    public JsonRpcResponse serve(Object object, String str) throws IOException {
        return serve(object, str, null);
    }

    public JsonRpcResponse serve(Object object, String str, Reader reader)
            throws IOException {

        if (str == null && reader == null) {
            throw new NullPointerException();
        }

        JsonRpcTarget target = target(object.getClass());
        JsonRpcResponse res = new JsonRpcResponse();
        try {
            JsonRpcRequest req = target.fromJson(str, reader);
            try {

                res.setId(req.getId());

                String method = req.getMethod();
                Object params[] = req.getParams();

                // int modifiers = m.getModifiers();
                // static method? no param method?
                Method javamethod = target.getMethod(method);
                Object result = javamethod.invoke(object, params);
                res.setResult(result);

                if (JsonRpcSyncable.class.isInstance(object)) {
                    try {
                        res.setSync(((JsonRpcSyncable) object).sync());
                    } catch (Throwable e) {
                        logger.warn(e, e);
                    }
                }

            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (onException(req, res, cause)) {
                    res.setError(JsonRpcError.INTERNAL_ERROR);
                }
            }
        } catch (JsonRpcException e) {
            res.setId(e.getRequestId());
            res.setError(e.getError());

        } catch (JsonParseException e) {
            res.setError(JsonRpcError.PARSE_ERROR);

        } catch (Exception e) {
            logger.fatal(e, e);
            res.setError(JsonRpcError.INTERNAL_ERROR);
        }

        return res;
    }

    protected boolean onException(JsonRpcRequest req, JsonRpcResponse res,
            Throwable t) {
        return true;
    }

    private HashMap<Class<?>, JsonRpcTarget> targets = new HashMap<Class<?>, JsonRpcTarget>();

    private synchronized JsonRpcTarget target(Class<?> c) {
        JsonRpcTarget server = targets.get(c);
        if (server == null) {
            server = new JsonRpcTarget(c);
            // if (targets.size() > 20) {
            // logger.error("More than 20 targets!");
            // targets.clear();
            // }
            targets.put(c, server);
        }
        return server;
    }

    private static Logger logger = Logger.getLogger(JsonRpcServer.class);
}
