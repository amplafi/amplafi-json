package org.amplafi.flow.json.renderers;

import org.amplafi.flow.json.IJsonWriter;
import org.amplafi.flow.json.JSONException;
import org.amplafi.flow.json.JsonRenderer;

/**
 * default implementation to output {@link Enum}.
 * @author Patrick Moore
 * @param <T>
 *
 */
@SuppressWarnings("unchecked")
public class EnumJsonRenderer<T extends Enum<T>> implements JsonRenderer<T> {

    public static final EnumJsonRenderer INSTANCE = new EnumJsonRenderer();
    public Class getClassToRender() {
        return Enum.class;
    }
    public IJsonWriter toJson(IJsonWriter jsonWriter, Enum o) {
    	if (jsonWriter.isInKeyMode()) {
    		jsonWriter.key(o.toString());
    	} else {
    		jsonWriter.value(o.toString());
    	}
        return jsonWriter;
    }

    public <K> K fromJson(Class<K> clazz, Object value, Object...parameters) {
        if ( value == null ) {
            return null;
        } else if ( clazz.isAssignableFrom(value.getClass())) {
        	return (K) value;
        } else {
	        try {
	            return (K) Enum.valueOf((Class<? extends Enum>) clazz, value.toString());
	        } catch (IllegalArgumentException e) {
	            throw new JSONException(e);
	        }
	    }
	}
}