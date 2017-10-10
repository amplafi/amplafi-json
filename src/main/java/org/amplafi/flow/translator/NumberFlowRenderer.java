/*
 * Created on Jun 24, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.flow.translator;

import org.amplafi.flow.FlowRenderer;
import org.amplafi.flow.json.JSONObject;

public class NumberFlowRenderer<T extends Number> implements FlowRenderer<T> {

    public static final NumberFlowRenderer INSTANCE = new NumberFlowRenderer();

    public <W extends SerializationWriter> W toSerialization(W jsonWriter, T o) {
        return jsonWriter.append(JSONObject.numberToString(o));
    }

    public Class<T> getClassToRender() {
        return (Class<T>) Number.class;
    }

    /**
     * @see org.amplafi.flow.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, Object...)
     */
    public <K> K fromSerialization(Class<K> clazz, Object value, Object... parameters) {
        return (K) Long.valueOf(value.toString());
    }

}
