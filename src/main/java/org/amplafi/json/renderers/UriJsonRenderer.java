/**
 * Copyright 2006-2008 by Amplafi. All rights reserved.
 * Confidential.
 */
package org.amplafi.json.renderers;

import java.net.URI;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JsonRenderer;
import org.apache.commons.lang.ObjectUtils;

import com.sworddance.util.UriFactoryImpl;

/**
 * @author patmoore
 *
 */
public class UriJsonRenderer implements JsonRenderer<URI> {

    public static final UriJsonRenderer INSTANCE = new UriJsonRenderer();
    /**
     * @see org.amplafi.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, Object...)
     */
    @SuppressWarnings("unchecked")
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        if ( value == null) {
            return null;
        } else {
            return (K) UriFactoryImpl.createUri(value.toString());
        }
    }

    /**
     * @see org.amplafi.json.JsonRenderer#getClassToRender()
     */
    public Class<? extends URI> getClassToRender() {
        return URI.class;
    }
    public IJsonWriter toJson(IJsonWriter jsonWriter, URI o) {
        return jsonWriter.value(ObjectUtils.toString(o, null));
    }

}
