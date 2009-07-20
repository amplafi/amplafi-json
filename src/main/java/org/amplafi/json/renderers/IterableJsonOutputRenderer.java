package org.amplafi.json.renderers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JSONArray;
import org.amplafi.json.JSONWriter;
import org.amplafi.json.JsonRenderer;

/**
 * Convert any {@link Iterable} to a json array.
 *
 * @author Owner
 */
@SuppressWarnings("unchecked")
public class IterableJsonOutputRenderer<T extends Iterable> implements JsonRenderer<T> {
    public static final IterableJsonOutputRenderer DISALLOW_NULLS_INSTANCE = new IterableJsonOutputRenderer(false);
    public static final IterableJsonOutputRenderer INSTANCE = new IterableJsonOutputRenderer();
    private boolean allowNullValues;

    public IterableJsonOutputRenderer(boolean allowNullValues) {
        this.allowNullValues= allowNullValues;
    }

    /**
     * allow nulls to preserve positional relationship.
     */
    public IterableJsonOutputRenderer() {
        this(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class getClassToRender() {
        return Iterable.class;
    }

    @Override
    public IJsonWriter toJson(IJsonWriter jsonWriter, T iter) {
        jsonWriter.array();
        for (Object obj : iter) {
            if ( allowNullValues || obj != null ) {
                jsonWriter.value(obj);
            }
        }
        return jsonWriter.endArray();
    }

    /**
     * @see org.amplafi.json.JsonRenderer#fromJson(java.lang.Class, java.lang.Object, Object...)
     */
    @Override
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters) {
        JSONArray array = JSONArray.toJsonArray(value);
        K result = null;
        if ( array != null && !array.isEmpty() ) {
            if ( clazz == Set.class) {
                result = (K) new LinkedHashSet(array.asList());
            } else if (clazz == List.class) {
                result = (K) new ArrayList(array.asList());
            }
        }
        return result;
    }

}
