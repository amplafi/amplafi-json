/*
 * Created on Jun 24, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.flow.json.renderers;

import java.util.Objects;

import org.amplafi.flow.json.JSONObject;
import org.amplafi.flow.translator.FlowRenderer;
import org.amplafi.flow.translator.SerializationWriter;

public class StringJSONOutputRenderer implements FlowRenderer<CharSequence> {

    public static StringJSONOutputRenderer INSTANCE = new StringJSONOutputRenderer();

    public <W extends SerializationWriter> W toSerialization(W jsonWriter, CharSequence o) {
        return jsonWriter.append(JSONObject.quote(Objects.toString(o)));
    }

    public Class<CharSequence> getClassToRender() {
        return CharSequence.class;
    }

    /**
     * @see org.amplafi.flow.json.JsonRenderer#fromSerialization(java.lang.Class, java.lang.Object, Object...)
     */
    @SuppressWarnings("unchecked")
    public <K> K fromSerialization(Class<K> clazz, Object value, Object... parameters) {
        return (K) Objects.toString(value);
    }

}
