package org.amplafi.flow.json.renderers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.amplafi.flow.FlowRenderer;
import org.amplafi.flow.json.JSONObject;
import org.amplafi.flow.translator.SerializationWriter;

public class CalendarFlowRenderer implements FlowRenderer<Calendar> {

    /**
     *
     */
    public static final String TIMEZONE_ID = "timeZoneID";

    /**
     *
     */
    public static final String TIME_IN_MILLIS = "timeInMillis";

    public static final CalendarFlowRenderer INSTANCE = new CalendarFlowRenderer();

    @SuppressWarnings("unchecked")
    public <K> K fromSerialization(Class<K> clazz, Object value, Object... parameters) {
        if (value == null) {
            return null;
        }
        Calendar cal;
        if (value instanceof JSONObject || ((String)value).startsWith("{")) {
            JSONObject jsonObject = JSONObject.toJsonObject(value);
            long millis = jsonObject.getLong(TIME_IN_MILLIS);
            // TODO: shouldn't TimezoneFlowTranslator mean this is not needed?
            String timeZoneId = jsonObject.optString(TIMEZONE_ID);
            if (timeZoneId == null) {
                timeZoneId = "GMT";
            }
            cal = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));
            cal.setTimeInMillis(millis);
        } else {
            cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            cal.clear();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // HACK (Bruno): using Date as an intermediate is a hack.
            try {
                Date d = dateFormat.parse((String)value);
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

    public <W extends SerializationWriter> W toSerialization(W jsonWriter, Calendar cal) {
        jsonWriter.object();
        jsonWriter.keyValue(TIME_IN_MILLIS, cal.getTimeInMillis());
        jsonWriter.keyValue(TIMEZONE_ID, cal.getTimeZone().getID());
        return jsonWriter.endObject();
    }

}
