package org.amplafi.json.renderers;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JSONException;
import org.amplafi.json.JSONWriter;
import org.amplafi.json.JsonRenderer;

/**
 * default implementation to output {@link Enum}.
 * @author Patrick Moore
 * @param <T>
 *
 */
@SuppressWarnings("unchecked")
public class EnumJsonRenderer<T extends Enum<T>> implements JsonRenderer<T> {

    public static final EnumJsonRenderer INSTANCE = new EnumJsonRenderer();

    @Override
    public Class getClassToRender() {
        return Enum.class;
    }

    @Override
    public IJsonWriter toJson(IJsonWriter jsonWriter, Enum o) {
        return jsonWriter.append(o.name());
    }

    public <K> K fromJson(Class<K> clazz, Object value, Object...parameters) {
        if ( value == null ) {
            return null;
        }
        try {
            return (K) Enum.valueOf((Class<? extends Enum>) clazz, value.toString());
        } catch (IllegalArgumentException e) {
            throw new JSONException(e);
        }
    }
}
