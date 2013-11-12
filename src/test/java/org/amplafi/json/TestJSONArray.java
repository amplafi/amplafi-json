package org.amplafi.json;

import java.io.StringWriter;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestJSONArray extends Assert {

    @Test
    public void testBooleanArray() {
        JSONArray<Boolean> arr = new JSONArray<Boolean>();
        arr.put(Boolean.TRUE);
        arr.put(false);

        StringWriter writer = new StringWriter();
        arr.write(writer);

        assertEquals(writer.toString(), "[true,false]");
    }

    @Test(dataProvider="arraysToFlatten")
    public void testFlatten(JSONArray arrayToFlatten, Object expected) {
        Object actual = arrayToFlatten.flatten();
        assertEquals(actual, expected);
    }

    @DataProvider(name="arraysToFlatten")
    public Object[][] getArraysToFlatten() {
        return new Object[][] {
            // notice that the single element array in the jsonobject is flattened
            new Object[] { JSONArray.toJsonArray(Arrays.asList(JSONArray.toJsonArray(Arrays.asList(JSONObject.toJsonObject("{'key':[1]}"))))), JSONObject.toJsonObject("{'key':1}") },
            new Object[] { JSONArray.toJsonArray(Arrays.asList(JSONArray.toJsonArray(Arrays.asList(JSONObject.toJsonObject("{'key':[1,2]}"))))), JSONObject.toJsonObject("{'key':[1,2]}") },

            // empty arrays are flattened to nothing
            new Object[] { JSONArray.toJsonArray(Arrays.asList()), null },
            // .. even lists of empty lists
            new Object[] { JSONArray.toJsonArray(Arrays.asList(JSONArray.toJsonArray(Arrays.asList()))), null },
            new Object[] { JSONArray.toJsonArray(Arrays.asList(JSONArray.toJsonArray(Arrays.asList(JSONObject.toJsonObject(null))))), null },
            new Object[] { JSONArray.toJsonArray(Arrays.asList(JSONArray.toJsonArray(Arrays.asList(JSONObject.toJsonObject("{'key':[]}"))))), null },
        };
    }
}
