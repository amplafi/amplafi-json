/**
 *
 */
package org.amplafi.json.renderers;

import static org.testng.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.amplafi.json.JSONObject;
import org.amplafi.json.JSONStringer;
import org.amplafi.json.renderers.MapJsonRenderer;
import org.testng.annotations.Test;

public class TestMapJsonRenderer {

    @Test
    public void testTofromJson() {
        Map<String, Object> inMap = new LinkedHashMap<String, Object>();
        MapJsonRenderer<String, Object> renderer = new MapJsonRenderer<String, Object>(false);
        Map<String, Object> outMap = TestJsonRendererUtil.toFromJson(renderer, inMap);
        assertEquals(outMap, inMap);

        inMap.put("foo", true);
        // check recursion
        inMap.put("bar", new LinkedHashMap<Object,Object>());
        inMap.put("str", "\'\":);");
        outMap = TestJsonRendererUtil.toFromJson(renderer, inMap);
        assertEquals(outMap.size(), inMap.size());
        assertEquals(outMap.get("foo"), inMap.get("foo"));
        assertEquals(outMap.get("str"), inMap.get("str"));

        // we can't know that the nested map should be interpreted as a map.
        JSONObject actual = (JSONObject) outMap.get("bar");
        assertEquals(actual.asMap(), inMap.get("bar"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBasic() {
        MapJsonRenderer renderer = new MapJsonRenderer(false);
        JSONStringer jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        jsonWriter.value(map);
        assertEquals(jsonWriter.toString(), "{}");

        jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        map.put("foo", true);
        // check recursion
        map.put("bar", new LinkedHashMap());
        map.put("str", "\'\":);");
        jsonWriter.value(map);
        assertEquals(jsonWriter.toString(), "{\"foo\":true,\"bar\":{},\"str\":\"'\\\":);\"}");
    }

    @Test
    public void testNullFiltering() {
        MapJsonRenderer renderer = new MapJsonRenderer(false);
        JSONStringer jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(null, "gg");
        map.put("ff", null);
        jsonWriter.value(map);
        assertEquals(jsonWriter.toString(), "{}");
    }

    @Test
    public void testAllowNulls() {
        MapJsonRenderer renderer = new MapJsonRenderer(true);
        JSONStringer jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(null, "gg");
        map.put("ff", null);
        jsonWriter.value(map);

        assertEquals(jsonWriter.toString(), "{\"\":\"gg\",\"ff\":null}");
    }

    @Test
    public void testFromJsonBasic() {
        MapJsonRenderer<String, String> renderer = new MapJsonRenderer<String, String>(false);
        String input = "{\"foo\":true,\"bar\":{},\"str\":\"'\\\":);\"}";
        Map<String, String> map = renderer.fromJson(Map.class, JSONObject.toJsonObject(input));
        assertEquals(map.entrySet().size(), 3);
        assertEquals(map.get("foo"), Boolean.TRUE);
        assertEquals(map.get("str"), "\'\":);");
        Object obj = map.get("bar");
        Map<String, String> mapElement = renderer.fromJson(Map.class, obj);
        assertEquals(mapElement.entrySet().size(), 0);
    }
}
