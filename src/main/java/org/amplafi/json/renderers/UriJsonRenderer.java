/**
 * Copyright 2006-2008 by Amplafi. All rights reserved.
 * Confidential.
 */
package org.amplafi.json.renderers;

import java.net.URI;

import org.amplafi.json.JSONWriter;
import org.amplafi.json.JsonRenderer;
import org.apache.commons.lang.ObjectUtils;

/**
 * @author patmoore
 *
 */
public class UriJsonRenderer implements JsonRenderer<URI> {

    public static final UriJsonRenderer INSTANCE = new UriJsonRenderer();
    /**
     * @see org.amplafi.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, Object...)
     */
    @Override
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        if ( value == null) {
            return null;
        } else {
            return (K) URI.create(value.toString());
        }
    }

    /**
     * @see org.amplafi.json.JsonRenderer#getClassToRender()
     */
    @Override
    public Class<? extends URI> getClassToRender() {
        return URI.class;
    }

    /**
     * @see org.amplafi.json.JsonRenderer#toJson(org.amplafi.json.JSONWriter, java.lang.Object)
     */
    @Override
    public JSONWriter toJson(JSONWriter jsonWriter, URI o) {
        return jsonWriter.value(ObjectUtils.toString(o, null));
    }

}
