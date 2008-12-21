/*
 * Created on Jun 24, 2006
 * Copyright 2006-8 by Amplafi, All rights reserved.
 * Confidential.
 */
package org.amplafi.json.renderers;

import org.amplafi.json.JSONWriter;
import org.amplafi.json.JsonRenderer;

public class BooleanJsonRenderer implements JsonRenderer<Boolean> {

    public static final BooleanJsonRenderer INSTANCE = new BooleanJsonRenderer();
    public JSONWriter toJson(JSONWriter jsonWriter, Boolean o) {
        return jsonWriter.append(o.toString());
    }
    @SuppressWarnings("unchecked")
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        if ( value instanceof Boolean ){
            return (K)value;
        } else if ( value instanceof CharSequence) {
            return (K) Boolean.valueOf(((CharSequence)value).toString());
        }
        return (K) (clazz == null || clazz == Boolean.class?null:Boolean.FALSE);
    }

    public Class<Boolean> getClassToRender() {
        return Boolean.class;
    }

}
