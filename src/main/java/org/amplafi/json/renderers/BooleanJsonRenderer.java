/*
 * Created on Jun 24, 2006
 * Copyright 2006-8 by Amplafi, All rights reserved.
 * Confidential.
 */
package org.amplafi.json.renderers;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JsonRenderer;
import org.apache.commons.lang.ObjectUtils;

public class BooleanJsonRenderer implements JsonRenderer<Boolean> {

    public static final BooleanJsonRenderer INSTANCE = new BooleanJsonRenderer();
    public IJsonWriter toJson(IJsonWriter jsonWriter, Boolean o) {
        return jsonWriter.append(ObjectUtils.toString(o));
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
