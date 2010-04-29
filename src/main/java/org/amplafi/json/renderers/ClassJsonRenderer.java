/**
 * Copyright 2006-2011 by Amplafi. All rights reserved.
 * Confidential.
 */
package org.amplafi.json.renderers;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JsonRenderer;

/**
 * @author patmoore
 *
 */
public class ClassJsonRenderer implements JsonRenderer<Class<?>> {

    public static final ClassJsonRenderer INSTANCE = new ClassJsonRenderer();

    /**
     * @see org.amplafi.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        if ( value == null ) {
            return null;
        } else {
            try {
                return (K) Class.forName(value.toString());
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }

    /**
     * @see org.amplafi.json.JsonRenderer#getClassToRender()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class/*<? extends Class<?>>*/ getClassToRender() {
        return /*(Class<? extends Class<?>>)*/ Class.class;
    }

    /**
     * @see org.amplafi.json.JsonRenderer#toJson(org.amplafi.json.IJsonWriter, java.lang.Object)
     */
    @Override
    public IJsonWriter toJson(IJsonWriter jsonWriter, Class<?> o) {
        jsonWriter.value(o.getCanonicalName());
        return jsonWriter;
    }

}
