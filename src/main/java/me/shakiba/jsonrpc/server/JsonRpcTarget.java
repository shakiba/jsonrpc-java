package me.shakiba.jsonrpc.server;

import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class JsonRpcTarget {
    private static Gson gsonmethod = new Gson();

    private final Gson gsonreq = new GsonBuilder().registerTypeAdapter(
            JsonRpcRequest.class, new RequestDeserializer()).create();

    private final HashMap<String, Method> methods = new HashMap<String, Method>();

    public JsonRpcTarget(Class<?> c) {
        for (Method method : c.getDeclaredMethods()) {
            int methodmodifiers = method.getModifiers();
            if (!Modifier.isPublic(methodmodifiers))
                continue;

            String methodName = method.getName();

            if (methods.containsKey(methodName)) {
                throw new DuplicateMethodName(
                        "Method overloading is not supported: " + methodName);
            }

            methods.put(methodName, method);
        }

        // enable calling inner class methods if declared at a super class or
        // interface
        if (c.isAnonymousClass() || c.isMemberClass() || c.isLocalClass()) {
            // for (Method method : c.getMethods()) {
            // if (methods.containsKey(method.getName())) {
            // methods.put(method.getName(), method);
            // }
            // }

            for (Class<?> iface : c.getInterfaces()) {
                for (Method method : iface.getMethods()) {
                    if (methods.containsKey(method.getName())) {
                        methods.put(method.getName(), method);
                    }
                }
            }
        }
    }

    public JsonRpcRequest fromJson(String str, Reader reader) {
        if (str != null) {
            return gsonreq.fromJson(str, JsonRpcRequest.class);
        } else if (reader != null) {
            return gsonreq.fromJson(reader, JsonRpcRequest.class);
        }
        throw new NullPointerException();
    }

    public Method getMethod(String method) {
        return methods.get(method);
    }

    private class RequestDeserializer implements
            JsonDeserializer<JsonRpcRequest> {
        @Override
        public JsonRpcRequest deserialize(JsonElement json, Type typeOfT,
                JsonDeserializationContext context) throws JsonParseException {
            JsonObject asJsonObject = json.getAsJsonObject();

            JsonRpcRequest request = new JsonRpcRequest();

            String id = "";
            try {
                id = asJsonObject.get("id").getAsString();
                request.setId(id);
            } catch (Exception e) {

            }

            String method;
            try {
                method = asJsonObject.get("method").getAsString();
            } catch (Exception e) {
                throw new JsonRpcException(id, JsonRpcError.INVALID_REQUEST);
            }

            Method javamethod = methods.get(method);
            if (javamethod == null) {
                throw new JsonRpcException(id, JsonRpcError.METHOD_NOT_FOUND);
            }

            JsonArray jsonparams;
            try {
                jsonparams = asJsonObject.get("params").getAsJsonArray();
            } catch (Exception e) {
                throw new JsonRpcException(id, JsonRpcError.INVALID_REQUEST);
            }

            Object[] params;
            try {
                params = deserialize(javamethod, jsonparams);
            } catch (Exception e) {
                throw new JsonRpcException(id, JsonRpcError.INVALID_PARAMS);
            }

            return request.setMethod(method).setParams(params);
        }

        private Object[] deserialize(Method method, JsonArray jsonObjects) {
            Type[] types = method.getGenericParameterTypes();
            Object[] objects = new Object[types.length];
            for (int i = 0; i < types.length; i++) {
                if (i >= jsonObjects.size()) {
                    objects[i] = null;
                } else {
                    objects[i] = gsonmethod.fromJson(jsonObjects.get(i),
                            types[i]);
                }
            }
            return objects;
        }

    }

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(JsonRpcTarget.class);

}