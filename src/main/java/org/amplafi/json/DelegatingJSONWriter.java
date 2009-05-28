package org.amplafi.json;

/**
 * Delegates everything apart from
 * {@link #addRenderer(Class, JsonRenderer)}, {@link #addRenderer(JsonRenderer)}
 * and {@link #value(Object)}.<p/>
 *
 * The main purpose of such a delegator/decorator is to allow using JsonRenderers
 * for a specific part of a json rendering without affecting the original
 * {@link JSONWriter}.  
 *
 * @author Andreas Andreou
 */
public class DelegatingJSONWriter extends JSONWriter {
    private JSONWriter realWriter;

    public DelegatingJSONWriter(JSONWriter writer) {
        this.realWriter = writer;
    }

    public JSONWriter append(String s) throws JSONException {
        return realWriter.append(s);
    }

    public JSONWriter array() throws JSONException {
        return realWriter.array();
    }

    public JSONWriter endArray() throws JSONException {
        return realWriter.endArray();
    }

    public JSONWriter endObject() throws JSONException {
        return realWriter.endObject();
    }

    public JSONWriter key(String s) throws JSONException {
        return realWriter.key(s);
    }

    public boolean isInKeyMode() {
        return realWriter.isInKeyMode();
    }

    public JSONWriter object() throws JSONException {
        return realWriter.object();
    }

    public boolean isInArrayMode() {
        return realWriter.isInArrayMode();
    }

    public boolean isInObjectMode() {
        return realWriter.isInObjectMode();
    }

    public JSONWriter value(boolean b) throws JSONException {
        return realWriter.value(b);
    }

    public JSONWriter value(double d) throws JSONException {
        return realWriter.value(d);
    }

    public JSONWriter value(long l) throws JSONException {
        return realWriter.value(l);
    }

    public JSONWriter keyValueIfNotBlankValue(String key, String value) {
        return realWriter.keyValueIfNotBlankValue(key, value);
    }

    public JSONWriter keyValueIfNotNullValue(String key, Object value) {
        return realWriter.keyValueIfNotNullValue(key, value);
    }

    public <T> JSONWriter keyValue(String key, T value) {
        return realWriter.keyValue(key, value);
    }

    @Override
    public String toString() {
        return realWriter.toString();
    }
}
