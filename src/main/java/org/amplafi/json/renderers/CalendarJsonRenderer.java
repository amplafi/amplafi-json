package org.amplafi.json.renderers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        if (value == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        String strValue = (String) value;
        if (strValue.startsWith("{")) {
            JSONObject jsonObject = JSONObject.toJsonObject(value);
            long millis = jsonObject.getLong(TIME_IN_MILLIS);
            // TODO: shouldn't TimezoneFlowTranslator mean this is not needed?
            String timeZoneId = jsonObject.getString(TIMEZONE_ID);
            cal.setTimeInMillis(millis);
            cal.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // HACK (Bruno): using Date as an intermediate is a hack.
            try {
                Date d = dateFormat.parse(strValue);
                cal.setTime(d);
            } catch (Exception e) {
                return null;
            }
        }
        return (K) cal;
    }

    public Class<? extends Calendar> getClassToRender() {
        return Calendar.class;
    }

    public IJsonWriter toJson(IJsonWriter jsonWriter, Calendar cal) {
        jsonWriter.object();
        jsonWriter.keyValue(TIME_IN_MILLIS, cal.getTimeInMillis());
        jsonWriter.keyValue(TIMEZONE_ID, cal.getTimeZone().getID());
        return jsonWriter.endObject();
    }

}
