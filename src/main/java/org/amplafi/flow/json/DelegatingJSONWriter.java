package org.amplafi.flow.json;

import java.io.IOException;

import com.sworddance.beans.MapByClass;

import org.amplafi.flow.FlowSelfRenderer;
import org.amplafi.flow.translator.FlowRenderer;
import org.amplafi.flow.translator.SerializationWriter;
import org.apache.commons.lang.StringUtils;

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
public class DelegatingJSONWriter implements SerializationWriter {

    private MapByClass<FlowRenderer<?>> renderers = new MapByClass<>();
    private JSONWriter realWriter;

    public DelegatingJSONWriter(SerializationWriter writer) {
        this.realWriter = (JSONWriter) writer;
    }
    public <W extends SerializationWriter> W append(String s) throws JSONException {
        realWriter.append(s);
        return (W) this;
    }
    public <W extends SerializationWriter> W array() throws JSONException {
        realWriter.array();
        return (W) this;
    }
    public <W extends SerializationWriter> W endArray() throws JSONException {
        realWriter.endArray();
        return (W) this;
    }
    public <W extends SerializationWriter> W endObject() throws JSONException {
        realWriter.endObject();
        return (W) this;
    }
    public boolean isInKeyMode() {
        return realWriter.isInKeyMode();
    }
    public boolean isInInitialMode() {
        return realWriter.isInInitialMode();
    }
    public <W extends SerializationWriter> W object() throws JSONException {
        realWriter.object();
        return (W) this;
    }
    public boolean isInArrayMode() {
        return realWriter.isInArrayMode();
    }
    public boolean isInObjectMode() {
        return realWriter.isInObjectMode();
    }
    public <W extends SerializationWriter> W value(boolean b) throws JSONException {
        realWriter.value(b);
        return (W) this;
    }
    public <W extends SerializationWriter> W value(double d) throws JSONException {
        realWriter.value(d);
        return (W) this;
    }
    @SuppressWarnings("unchecked")
    public <W extends SerializationWriter> W value(long l) throws JSONException {
        realWriter.value(l);
        return (W) this;
    }
    @SuppressWarnings("unchecked")
    public <T, W extends SerializationWriter> W value(T o) throws JSONException {
        if ( o != null ) {
            FlowRenderer<T> renderer = (FlowRenderer<T>) renderers.getRaw(o.getClass());
            if ( renderer == null) {
                if (o instanceof FlowSelfRenderer) {
                    // we check after looking in map so that it has a chance to have been overridden.
                    ((FlowSelfRenderer) o).toSerialization(realWriter);
                    return (W) this;
                } else {
                    // o.k. go search for a loose match.
                    renderer = (JsonRenderer<T>) renderers.get(o.getClass());
                }
            }
            if ( renderer != null ) {
                renderer.toSerialization(this, o);
                return (W) this;
            } else {
                realWriter.value(o);
                return (W) this;
            }
        } else {
            realWriter.append(JSONObject.valueToString(o));
            return (W) this;
        }
    }

    @Override
    public String toString() {
        return realWriter.toString();
    }


    /**
     * @see org.amplafi.flow.json.<W extends SerializationWriter> W#addRenderer(java.lang.Class, org.amplafi.flow.json.JsonRenderer)
     */
    public void addRenderer(Class<?> name, FlowRenderer<?> renderer) {
        renderers.put(name, renderer);
    }
    /**
     * @see org.amplafi.flow.json.<W extends SerializationWriter> W#addRenderer(org.amplafi.flow.json.JsonRenderer)
     */
    public void addRenderer(FlowRenderer<?> renderer) {
        this.addRenderer(renderer.getClassToRender(), renderer);
    }

    @SuppressWarnings("unchecked")
    public <K, W extends SerializationWriter> W key(K key) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        } else if ( !isInKeyMode()){
            throw new JSONException("Misplaced key:"+key);
        } else {
            try {
                if (realWriter.comma) {
                    realWriter.writer.write(',');
                }
                JsonRenderer<K> renderer = (JsonRenderer<K>) renderers.getRaw(key.getClass());
                if ( renderer == null ) {
                    if ( key instanceof FlowSelfRenderer) {
                    // we check after looking in map so that it has a chance to have been overridden.
                        ((FlowSelfRenderer) key).toSerialization(this);
                        return (W) this;
                    } else {
                        // o.k. go search for a loose match.
                        renderer = (JsonRenderer<K>) renderers.get(key.getClass());
                    }
                }
                if ( renderer != null ) {
                    renderer.toSerialization(this, key);
                    return (W) this;
                }
                return (W) this.append(JSONObject.valueToString(key));
            } catch (IOException e) {
                throw new JSONException(e);
            } finally {
                try {
                    realWriter.writer.write(':');
                } catch (IOException e) {
                    throw new JSONException(e);
                }
                realWriter.comma = false;
                realWriter.mode = JSONWriter.OBJECT_MODE;
            }
        }
    }

    /**
     *
     * @see org.amplafi.flow.json.<W extends SerializationWriter> W#keyValueIfNotBlankValue(java.lang.Object, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public <K, W extends SerializationWriter> W keyValueIfNotBlankValue(K key, String value) {
        if ( !StringUtils.isBlank(value)) {
            this.keyValue(key, value);
        }
        return (W) this;
    }

    /**
     *
     * @see org.amplafi.flow.json.<W extends SerializationWriter> W#keyValueIfNotNullValue(java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public <K,V, W extends SerializationWriter> W keyValueIfNotNullValue(K key, V value) {
        if ( value != null) {
            this.keyValue(key, value);
        }
        return (W) this;
    }

    /**
     *
     * @see org.amplafi.flow.json.<W extends SerializationWriter> W#keyValue(java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public <K, V, W extends SerializationWriter> W keyValue(K key, V value) {
        this.key(key).value(value);
        return (W) this;
    }
}
