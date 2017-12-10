package org.amplafi.flow.json.renderers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.amplafi.flow.json.JSONArray;
import org.amplafi.flow.translator.FlowRenderer;
import org.amplafi.flow.translator.SerializationWriter;

/**
 * Convert any {@link Iterable} to a json array.
 *
 * @author Owner
 */
@SuppressWarnings("unchecked")
public class IterableJsonOutputRenderer<T extends Iterable> implements FlowRenderer<T> {
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
    public Class getClassToRender() {
        return Iterable.class;
    }
    public <W extends SerializationWriter> W toSerialization(W serializationWriter, T iter) {
        serializationWriter.array();
        for (Object obj : iter) {
            if ( allowNullValues || obj != null ) {
                serializationWriter.value(obj);
            }
        }
        return serializationWriter.endArray();
    }
    public <K> K fromSerialization(Class<K> clazz, Object value, Object... parameters) {
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
