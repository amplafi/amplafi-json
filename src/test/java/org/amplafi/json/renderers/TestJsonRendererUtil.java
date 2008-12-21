package org.amplafi.json.renderers;

import org.amplafi.json.JSONObject;
import org.amplafi.json.JSONStringer;
import org.amplafi.json.JsonRenderer;

public class TestJsonRendererUtil {
    
    @SuppressWarnings("unchecked")
    static <T> T toFromJson(JsonRenderer<T> renderer, T obj)
    {
        JSONStringer jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        jsonWriter.value(obj);
        String serializedData = jsonWriter.toString();
        return (T) renderer.fromJson(renderer.getClass(), JSONObject.toJsonObject(serializedData));
    }

}
