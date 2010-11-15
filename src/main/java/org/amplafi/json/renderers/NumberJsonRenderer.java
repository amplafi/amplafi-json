/*
 * Created on Jun 24, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.json.renderers;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JSONObject;
import org.amplafi.json.JsonRenderer;

public class NumberJsonRenderer<T extends Number> implements JsonRenderer<T> {

    public static final NumberJsonRenderer INSTANCE = new NumberJsonRenderer();

    public IJsonWriter toJson(IJsonWriter jsonWriter, Number o) {
        return jsonWriter.append(JSONObject.numberToString(o));
    }

    public Class<T> getClassToRender() {
        return (Class<T>) Number.class;
    }

    /**
     * @see org.amplafi.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, Object...)
     */
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        return (K) new Long(value.toString());
    }

}
