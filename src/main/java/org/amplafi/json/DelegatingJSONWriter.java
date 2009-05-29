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
        realWriter.append(s);
        return this;
    }

    public JSONWriter array() throws JSONException {
        realWriter.array();
        return this;
    }

    public JSONWriter endArray() throws JSONException {
        realWriter.endArray();
        return this;
    }

    public JSONWriter endObject() throws JSONException {
        realWriter.endObject();
        return this;
    }

    public JSONWriter key(String s) throws JSONException {
        realWriter.key(s);
        return this;
    }

    public boolean isInKeyMode() {
        return realWriter.isInKeyMode();
    }

    public JSONWriter object() throws JSONException {
        realWriter.object();
        return this;
    }

    public boolean isInArrayMode() {
        return realWriter.isInArrayMode();
    }

    public boolean isInObjectMode() {
        return realWriter.isInObjectMode();
    }

    public JSONWriter value(boolean b) throws JSONException {
        realWriter.value(b);
        return this;
    }

    public JSONWriter value(double d) throws JSONException {
        realWriter.value(d);
        return this;
    }

    public JSONWriter value(long l) throws JSONException {
        realWriter.value(l);
        return this;
    }

    public JSONWriter keyValueIfNotBlankValue(String key, String value) {
        realWriter.keyValueIfNotBlankValue(key, value);
        return this;
    }

    @Override
    public String toString() {
        return realWriter.toString();
    }
}
