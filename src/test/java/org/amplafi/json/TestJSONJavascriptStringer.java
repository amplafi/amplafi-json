/*
 * Created on Aug 1, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.json;

import static org.testng.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.amplafi.flow.json.JSONJavascriptStringer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * test the javascript json generation.
 *
 * @author Patrick Moore
 */
@Test()
@SuppressWarnings("deprecation")
public class TestJSONJavascriptStringer {
    private JSONJavascriptStringer jsonWriter;

    @BeforeMethod
    protected void setUpWriter() {
        jsonWriter = new JSONJavascriptStringer();
    }

    /**
     * Date generation when outputing JSON.
     */
    public void testToJSONDate() {
        jsonWriter.object().key("test1").value(new Date(106, 7, 20));
        jsonWriter.endObject();
        assertEquals(jsonWriter.toString(),"{\"test1\":new Date(2006,7,20,0,0)}");
    }

    /**
     * Calendar generation when outputing JSON.
     */
    public void testToJSONCalendar() {
        Date date = new Date(106, 7, 20);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        jsonWriter.object().key("test1").value(cal);
        jsonWriter.endObject();
        assertEquals(jsonWriter.toString(),"{\"test1\":new Date(2006,7,20,0,0)}");
    }
}
