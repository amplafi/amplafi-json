package org.amplafi.json.renderers;

import java.util.Calendar;
import java.util.TimeZone;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JSONObject;
import org.amplafi.json.JsonRenderer;

public class CalendarJsonRenderer implements JsonRenderer<Calendar> {

    /**
     *
     */
    public static final String TIMEZONE_ID = "timeZoneID";
    /**
     *
     */
    public static final String TIME_IN_MILLIS = "timeInMillis";
    public static final CalendarJsonRenderer INSTANCE = new CalendarJsonRenderer();

    @SuppressWarnings("unchecked")
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        if(value == null) {
            return null;
        }
        JSONObject jsonObject = JSONObject.toJsonObject(value);
        long millis = jsonObject.getLong(TIME_IN_MILLIS);
        // TODO: shouldn't TimezoneFlowTranslator mean this is not needed?
        String timeZoneId = jsonObject.getString(TIMEZONE_ID);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        return (K)cal;
    }
    public Class<? extends Calendar> getClassToRender() {
        return Calendar.class;
    }
    public IJsonWriter toJson(IJsonWriter jsonWriter, Calendar cal) {
        jsonWriter.object();
        jsonWriter.keyValue(TIME_IN_MILLIS,cal.getTimeInMillis());
        jsonWriter.keyValue(TIMEZONE_ID,cal.getTimeZone().getID());
        return jsonWriter.endObject();
    }

}
