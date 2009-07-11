/*
 * Created on Jan 7, 2008 Copyright 2006 by Amplafi, Inc.
 */
package org.amplafi.json.renderers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.amplafi.json.JSONArray;
import org.amplafi.json.JSONObject;
import org.amplafi.json.JSONWriter;
import org.amplafi.json.JsonRenderer;
import org.amplafi.json.JsonSelfRenderer;
import org.apache.commons.lang.ObjectUtils;

/**
 * Render a map of objects as a json map. Keys and values of the Map can be JsonSelfRenderer
 * instances also.
 * 
 * @author Patrick Moore
 */
@SuppressWarnings("unchecked")
public class MapJsonRenderer<T, V> implements JsonRenderer<Map<T, V>> {
    private static final String VALUES = "values";

    public static final MapJsonRenderer<?, ?> INSTANCE = new MapJsonRenderer();

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
    public JSONWriter toJson(JSONWriter jsonWriter, Map<T, V> map) {
        jsonWriter.object();
        if (map != null) {
            jsonWriter.key(VALUES);
            jsonWriter.array();
            for (Map.Entry<T, V> entry : map.entrySet()) {
                T key = entry.getKey();
                V value = entry.getValue();
                if (allowNullValues || (key != null && value != null)) {
                    key = key == null ? (T) ObjectUtils.toString(key) : key;
                    KeyValuePair<T, V> keyValuePair = new KeyValuePair<T, V>(key, value);
                    jsonWriter.value(keyValuePair);
                }
            }
            jsonWriter.endArray();
        }
        return jsonWriter.endObject();
    }

    /**
     * @see org.amplafi.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, Object...)
     */
    @Override
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        JSONObject jsonObject = JSONObject.toJsonObject(value);
        JSONArray array = jsonObject.optJSONArray(VALUES);
        Map<T, V> map = new HashMap<T, V>();
        for (JSONObject keyValuePairAsJsonObj : array.asList(JSONObject.class)) {
            KeyValuePair<T, V> keyValuePair = new KeyValuePair<T, V>(keyValuePairAsJsonObj);
            map.put(keyValuePair.key, keyValuePair.value);
        }
        return (K) map;
    }

}

class KeyValuePair<X, Y> implements JsonSelfRenderer {

    private static final String KEY = "key";

    private static final String KEY_CLASS = "keyClass";

    private static final String VALUE = "value";

    private static final String VALUE_CLASS = "valueClass";

    X key;

    Y value;

    KeyValuePair(X keyArg, Y valueArg) {
        key = keyArg;
        value = valueArg;
    }

    public KeyValuePair(Object object) {
        fromJson(object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T fromJson(Object object) {
        JSONObject json = JSONObject.toJsonObject(object);
        key = json.opt(KEY);
        String keyClassName = json.opt(KEY_CLASS);
        key = keyClassName == null ? key : getDeserializedObject(keyClassName, key);
        value = json.opt(VALUE);
        String valClassName = json.opt(VALUE_CLASS);
        value = valClassName == null ? value : getDeserializedObject(valClassName, value);
        return (T) this;
    }

    @Override
    public void toJson(JSONWriter jsonWriter) {
        jsonWriter.object();
        if (key instanceof JsonSelfRenderer) {
            jsonWriter.keyValue(KEY_CLASS, key.getClass().getName());
        }
        jsonWriter.keyValue(KEY, key);
        if (value instanceof JsonSelfRenderer) {
            jsonWriter.keyValue(VALUE_CLASS, value.getClass().getName());
        }
        jsonWriter.keyValue(VALUE, value);
        jsonWriter.endObject();
    }

    @SuppressWarnings("unchecked")
    private <T> T getDeserializedObject(String className, T val) {
        try {
            Class<?> forName = Class.forName(className);
            if (JsonSelfRenderer.class.isAssignableFrom(forName)) {
                Constructor<?> declaredConstructor = forName.getDeclaredConstructor(JSONObject.class);
                Object newInstance = declaredConstructor.newInstance(val);
                return (T) newInstance;
            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }
}
