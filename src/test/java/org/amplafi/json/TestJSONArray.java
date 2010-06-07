package org.amplafi.json;

import java.io.StringWriter;

import org.testng.Assert;
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

}
