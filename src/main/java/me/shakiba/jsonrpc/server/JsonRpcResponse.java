package me.shakiba.jsonrpc.server;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonStreamParser;

public class JsonRpcResponse {
    private String id = "";
    private Object result;
    private Object sync;
    private JsonRpcError error;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setSync(Object sync) {
        this.sync = sync;
    }

    public Object getSync() {
        return sync;
    }

    public void setError(JsonRpcError error) {
        this.error = error;
    }

    public JsonRpcError getError() {
        return this.error;
    }

    public boolean isError() {
        return this.error != null;
    }

    public String toJson() {
        return gsonres.toJson(this);
    }

    public void toJson(Appendable x) {
        gsonres.toJson(this, x);
    }

    private static Gson gsonres = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(JsonRpcResponse.class,
                    new ResponseSerializer())
            .registerTypeAdapter(RawJson.class, new RawJsonSerializer())
            .create();

    private static class ResponseSerializer implements
            JsonSerializer<JsonRpcResponse> {

        @Override
        public JsonElement serialize(JsonRpcResponse src, Type typeOfSrc,
                JsonSerializationContext context) {
            JsonObject res = new JsonObject();
            res.add("id", new JsonPrimitive(src.getId()));
            if (src.getError() != null) {
                res.add("error", context.serialize(src.getError()));
            } else /* if (src.getResult() != null) */{
                res.add("result", context.serialize(src.getResult()));
                if (src.getSync() != null) {
                    res.add("sync", context.serialize(src.getSync()));
                }
            }
            return res;
        }

    }

    private static class RawJsonSerializer implements JsonSerializer<RawJson> {

        @Override
        public JsonElement serialize(RawJson src, Type typeOfSrc,
                JsonSerializationContext context) {
            return new JsonStreamParser(src.getJson()).next();
        }

    }

}