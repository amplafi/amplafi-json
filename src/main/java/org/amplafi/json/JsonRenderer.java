/*
 * Created on Jun 4, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.json;

/**
 * Implementers will generate a JSON representation of a class instance.
 *
 * @author Patrick Moore
 * @param <T>
 */
public interface JsonRenderer<T> {
    public IJsonWriter toJson(IJsonWriter jsonWriter, T o);
    /**
     *
     * @param <K>
     * @param clazz the class of the object that is the expected output ( not necessarily <K> as the result may be something else (for example an id))
     * @param value json representation of some sort, JSONObject, JSONArray, string, etc.
     * @param parameters optional additional parameters
     * @return the object translated from the json object.
     */
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters);
    public Class<? extends T> getClassToRender();
}
