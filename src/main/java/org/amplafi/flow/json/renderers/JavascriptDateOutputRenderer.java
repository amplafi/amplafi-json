/*
 * Created on Jun 4, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.flow.json.renderers;

import java.util.Calendar;
import java.util.Date;

import org.amplafi.flow.json.IJsonWriter;
import org.amplafi.flow.json.JsonRenderer;

/**
 * Render date and calendar object in the form of a javascript object.
 *
 * @author Patrick Moore
 */
public class JavascriptDateOutputRenderer implements JsonRenderer {
    public static final JavascriptDateOutputRenderer INSTANCE = new JavascriptDateOutputRenderer();

    public IJsonWriter toJson(IJsonWriter jsonWriter, Object o) {
        Calendar cal;
        if ( o instanceof Date ) {
            cal = Calendar.getInstance();
            cal.setTime((Date)o);
        } else {
            cal = (Calendar) o;
        }
        StringBuilder sb = new StringBuilder("new Date(");
        sb.append(cal.get(Calendar.YEAR)).append(',');
        sb.append(cal.get(Calendar.MONTH)).append(',');
        sb.append(cal.get(Calendar.DATE)).append(',');
        sb.append(cal.get(Calendar.HOUR)).append(',');
        sb.append(cal.get(Calendar.MINUTE)).append(')');
        return jsonWriter.append(sb.toString());
    }

    public Class<Date> getClassToRender() {
        return Date.class;
    }

    /**
     * @see org.amplafi.flow.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, Object...)
     */
    public Object fromJson(Class clazz, Object value, Object...parameters) {
        throw new UnsupportedOperationException();
    }
}
