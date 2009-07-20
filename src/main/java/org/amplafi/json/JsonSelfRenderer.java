package org.amplafi.json;

/**
 * implementer will produce a JSON representation of the class in question.
 * @author Patrick Moore
 *
 */
public interface JsonSelfRenderer {
    public void toJson(IJsonWriter jsonWriter);
    /**
     * @param <T> class returned.
     * @param object some JSON object. It should be the same kind of object written by the {@link #toJson(IJsonWriter)} method.
     * @return the created object. Usually this (but it doesn't have to be)
     */
    public <T> T fromJson(Object object);
}
