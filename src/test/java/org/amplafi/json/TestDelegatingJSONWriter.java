package org.amplafi.json;

import org.amplafi.flow.json.DelegatingJSONWriter;
import org.amplafi.flow.json.IJsonWriter;
import org.amplafi.flow.json.JSONStringer;
import org.amplafi.flow.json.renderers.JavascriptDateOutputRenderer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author Andreas Andreou
 */
public class TestDelegatingJSONWriter extends Assert {

    /**
     * Checks delegating works and doesn't affect original writer.
     */
    @Test
    public void testDelegating() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(1234567890000L);

        IJsonWriter writer = new JSONStringer();
        writer.object();
        writer.key("1");
        writer.value(calendar);

        IJsonWriter innerWriter = new DelegatingJSONWriter(writer);
        innerWriter.addRenderer(Calendar.class, new JavascriptDateOutputRenderer());
        innerWriter.key("2");
        innerWriter.value(calendar);

        writer.key("3");
        writer.value(calendar);

        innerWriter.keyValueIfNotNullValue("4", calendar);

        writer.endObject();

        String dateStr = "{\"timeInMillis\":1234567890000,\"timeZoneID\":\"GMT\"}";
//        dateStr = dateStr.replaceAll("\"", "\\\\\"");

        assertEquals(writer.toString(), "{\"1\":" + dateStr + "" +
            ",\"2\":new Date(2009,1,13,11,31)" +
            ",\"3\":" + dateStr + "" +
            ",\"4\":new Date(2009,1,13,11,31)" +
            "}"
        );
    }
}
