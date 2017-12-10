/*
 * Created on Aug 1, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.json.renderers;

import static org.testng.Assert.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

import org.amplafi.flow.json.JSONWriter;
import org.amplafi.flow.json.renderers.JavascriptDateOutputRenderer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test()
@SuppressWarnings("deprecation")
public class TestJavascriptDateOutputRenderer {
    private JavascriptDateOutputRenderer renderer;

    private Writer result;

    private JSONWriter jsonWriter;

    @BeforeMethod()
    protected void setUpObjects() {
        renderer = new JavascriptDateOutputRenderer();
        result = new StringWriter();
        jsonWriter = new JSONWriter(result);
    }

    public void testToJSONDate() {
        jsonWriter.object().key("test1");
        renderer.toSerialization(jsonWriter, new Date(106, 7, 20));
        jsonWriter.endObject();
        assertEquals(result.toString(), "{\"test1\":new Date(2006,7,20,0,0)}");
    }

    public void testToJSONCalendar() {
        Date date = new Date(106, 7, 20);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        jsonWriter.object().key("test1");
        renderer.toSerialization(jsonWriter, cal);
        jsonWriter.endObject();
        assertEquals(result.toString(), "{\"test1\":new Date(2006,7,20,0,0)}");
    }
}
