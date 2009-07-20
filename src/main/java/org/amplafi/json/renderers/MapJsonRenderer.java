/*
 * Created on Jan 7, 2008
 * Copyright 2006 by Amplafi, Inc.
 */
package org.amplafi.json.renderers;

import java.util.Map;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JSONObject;
import org.amplafi.json.JsonRenderer;
import org.apache.commons.lang.ObjectUtils;

/**
 * Render a map of objects as a json map. Keys and values of the Map can be JsonSelfRenderer
 * instances also.
 *
 * @author Patrick Moore
 * @param <T>
 * @param <V>
 *
 */
@SuppressWarnings("unchecked")
public class MapJsonRenderer<T,V> implements JsonRenderer<Map<T,V>> {
    public static final MapJsonRenderer<?,?> INSTANCE = new MapJsonRenderer();

    public static final MapJsonRenderer<?, ?> ALLOW_NULLS_INSTANCE = new MapJsonRenderer(true);

    private boolean allowNullValues;

    public MapJsonRenderer(boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
    }

    public MapJsonRenderer() {
        this(false);
    }

    @Override
    public Class getClassToRender() {
        return Map.class;
    }

    @Override
    public IJsonWriter toJson(IJsonWriter jsonWriter, Map map) {
        jsonWriter.object();
        if ( map != null ) {
            for(Object entry: map.entrySet()) {
                Object key = ((Map.Entry)entry).getKey();
                Object value = ((Map.Entry)entry).getValue();
                if ( allowNullValues || (key != null && value != null)) {
                    jsonWriter.keyValue(ObjectUtils.toString(key), value);
                }
            }
        }
        return jsonWriter.endObject();
    }

    /**
     * @see org.amplafi.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, Object...)
     */
    @Override
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        JSONObject jsonObject = JSONObject.toJsonObject(value);
        return (K) jsonObject.asMap();
    }

}
