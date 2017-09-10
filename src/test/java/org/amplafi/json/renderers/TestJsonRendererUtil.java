package org.amplafi.json.renderers;

import static org.testng.Assert.*;

import org.amplafi.flow.json.JSONArray;
import org.amplafi.flow.json.JSONObject;
import org.amplafi.flow.json.JSONStringer;
import org.amplafi.flow.json.JsonRenderer;

public class TestJsonRendererUtil {

    /**
     * Uses the renderer to first convert the obj to JSON and then back again.
     * 
     * @param <T> object type
     * @param renderer renderer to use for this object
     * @param obj object to render(serialize)
     * @return the object after having been converted to JSON, and back again.
     */
    @SuppressWarnings("unchecked")
    static <T> T toFromJson(JsonRenderer<T> renderer, T obj)
    {
        JSONStringer jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        jsonWriter.value(obj);
        String serializedData = jsonWriter.toString();
        return (T) renderer.fromJson(renderer.getClass(), JSONObject.toJsonObject(serializedData));
    }

    /**
     * Just like {@link TestJsonRendererUtil#toFromJson(JsonRenderer, Object)} only the obj is
     * placed in an array before being serialized to JSON.
     * 
     */
    static <T> T toFromJsonInArray(JsonRenderer<T> renderer, T obj)
    {
        JSONStringer jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        jsonWriter.array();
        jsonWriter.value(obj);
        jsonWriter.endArray();
        String serializedData = jsonWriter.toString();
        JSONArray jsonArray = JSONArray.toJsonArray(serializedData);
        assertNotNull(jsonArray);
        assertEquals(jsonArray.size(), 1);
        T result = (T) renderer.fromJson(renderer.getClass(), jsonArray.get(0));
        return result;
    }
}
