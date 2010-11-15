/*
 * Created on Jun 24, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.json.renderers;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JSONObject;
import org.amplafi.json.JsonRenderer;
import org.apache.commons.lang.ObjectUtils;

public class StringJSONOutputRenderer implements JsonRenderer<CharSequence> {

    public static StringJSONOutputRenderer INSTANCE = new StringJSONOutputRenderer();

    public IJsonWriter toJson(IJsonWriter jsonWriter, CharSequence o) {
        return jsonWriter.append(JSONObject.quote(ObjectUtils.toString(o)));
    }

    public Class<CharSequence> getClassToRender() {
        return CharSequence.class;
    }

    /**
     * @see org.amplafi.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, Object...)
     */
    @SuppressWarnings("unchecked")
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        return (K) ObjectUtils.toString(value);
    }

}
