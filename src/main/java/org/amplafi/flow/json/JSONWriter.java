package org.amplafi.flow.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.sworddance.beans.MapByClass;

import org.amplafi.flow.FlowSelfRenderer;
import org.amplafi.flow.json.renderers.CalendarFlowRenderer;
import org.amplafi.flow.json.renderers.IterableJsonOutputRenderer;
import org.amplafi.flow.json.renderers.MapJsonRenderer;
import org.amplafi.flow.json.renderers.StringJSONOutputRenderer;
import org.amplafi.flow.translator.BooleanJsonRenderer;
import org.amplafi.flow.translator.ClassFlowRenderer;
import org.amplafi.flow.translator.FlowRenderer;
import org.amplafi.flow.translator.NumberFlowRenderer;
import org.amplafi.flow.translator.SerializationWriter;
import org.apache.commons.lang.StringUtils;


/*
Copyright (c) 2006 JSON.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

/**
 * JSONWriter provides a quick and convenient way of producing JSON text.
 * The texts produced strictly conform to JSON syntax rules. No whitespace is
 * added, so the results are ready for transmission or storage. Each instance of
 * JSONWriter can produce one JSON text.
 * <p>
 * A JSONWriter instance provides a <code>value</code> method for appending
 * values to the
 * text, and a <code>key</code>
 * method for adding keys before values in objects. There are <code>array</code>
 * and <code>endArray</code> methods that make and bound array values, and
 * <code>object</code> and <code>endObject</code> methods which make and bound
 * object values. All of these methods return the JSONWriter instance,
 * permitting a cascade style. For example, <pre>
 * new JSONWriter(myWriter)
 *     .object()
 *         .key("JSON")
 *         .value("Hello, World!")
 *     .endObject();</pre> which writes <pre>
 * {"JSON":"Hello, World!"}</pre>
 * <p>
 * The first method called must be <code>array</code> or <code>object</code>.
 * There are no methods for adding commas or colons. JSONWriter adds them for
 * you. Objects and arrays can be nested up to 20 levels deep.
 * <p>
 * This can sometimes be easier than using a JSONObject to build a string.
 * @author JSON.org
 * @version 2
 */
public class JSONWriter implements SerializationWriter {
    private static final int maxdepth = 20;
    private MapByClass<FlowRenderer<?>> renderers;

    /**
     * The comma flag determines if a comma should be output before the next
     * value.
     */
    boolean comma;

    /**
     * The current mode. Values:
     * 'a' (array),
     * 'd' (done),
     * 'i' (initial),
     * 'k' (key),
     * 'o' (object).
     */
    protected char mode;
    private static final char ARRAY_MODE = 'a';
    static final char OBJECT_MODE = 'o';
    private static final char KEY_MODE = 'k';
    private static final char INITIAL_MODE = 'i';

    /**
     * The object/array stack.
     */
    private char stack[];

    /**
     * The stack top index. A value of 0 indicates that the stack is empty.
     */
    private int top;

    /**
     * The writer that will receive the output.
     */
    protected Writer writer;

    public JSONWriter() {
        this(new StringWriter());
    }
    /**
     * Make a fresh JSONWriter. It can be used to build one JSON text.
     *
     * @param w a writer to be wrapped
     */
    public JSONWriter(Writer w) {
        this(w, new MapByClass<FlowRenderer<?>>());
        this.addRenderer(NumberFlowRenderer.INSTANCE);
        this.addRenderer(StringJSONOutputRenderer.INSTANCE);
        this.addRenderer(BooleanJsonRenderer.INSTANCE);
        this.addRenderer(ClassFlowRenderer.INSTANCE);
        this.addRenderer(CalendarFlowRenderer.INSTANCE);
        this.addRenderer(MapJsonRenderer.ALLOW_NULLS_INSTANCE);
        this.addRenderer(IterableJsonOutputRenderer.INSTANCE);
    }

    /**
     * @param w a writer to be wrapped
     * @param renderers
     */
    public JSONWriter(Writer w, MapByClass<FlowRenderer<?>> renderers) {
        this.renderers = renderers;
        comma = false;
        mode = INITIAL_MODE;
        stack = new char[maxdepth];
        top = 0;
        writer = w;
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#append(java.lang.String)
     */
    public <W extends SerializationWriter> W append(String s) {
        if (s == null) {
            throw new JSONException("Null pointer");
        }
        if ( mode == INITIAL_MODE) {
            try {
                writer.write(s);
            } catch (IOException e) {
                throw new JSONException(e);
            }
            // initial object so we are immediately done.
            mode = 'd';
            return (W) this;
        }
        if (isInObjectMode() || isInArrayMode()) {
            try {
                if (comma && isInArrayMode()) {
                    writer.write(',');
                }
                writer.write(s);
            } catch (IOException e) {
                throw new JSONException(e);
            }
            if (isInObjectMode()) {
                mode = KEY_MODE;
            }
            comma = true;
            return (W) this;
        } else if ( isInKeyMode()) {
            try {
                writer.write(s);
            } catch (IOException e) {
                throw new JSONException(e);
            }
            return (W) this;
        }
        throw new JSONException("Value out of sequence.");
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#array()
     */
    public <W extends SerializationWriter> W array() throws JSONException {
        if (mode == INITIAL_MODE || isInObjectMode() || isInArrayMode()) {
            this.push(ARRAY_MODE);
            this.append("[");
            comma = false;
            return (W) this;
        }
        throw new JSONException("Misplaced array.");
    }

    /**
     * End something.
     * @param m Mode
     * @param c Closing character
     * @return this
     * @throws JSONException If unbalanced.
     */
    private JSONWriter end(char m, char c) {
        if (mode != m) {
            throw new JSONException(m == OBJECT_MODE ? "Misplaced endObject." :
            "Misplaced endArray." + writer.toString());
        }
        this.pop(m);
        try {
            writer.write(c);
        } catch (IOException e) {
            throw new JSONException(e);
        }
        comma = true;
        return this;
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#endArray()
     */
    @SuppressWarnings("unchecked")
    public <W extends SerializationWriter> W endArray() {
        return (W) this.end(ARRAY_MODE, ']');
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#endObject()
     */
    @SuppressWarnings("unchecked")
    public <W extends SerializationWriter> W endObject() {
        return (W) this.end(KEY_MODE, '}');
    }

    @SuppressWarnings("unchecked")
    public <K, W extends SerializationWriter> W key(K o) {
        if (o == null) {
            throw new JSONException("Null key.");
        } else if ( !isInKeyMode()){
            throw new JSONException("Misplaced key. :" +o);
        } else {
            try {
                if (comma) {
                    writer.write(',');
                }
                JsonRenderer<K> renderer = (JsonRenderer<K>) renderers.getRaw(o.getClass());
                if ( renderer == null ) {
                    if ( o instanceof FlowSelfRenderer) {
                    // we check after looking in map so that it has a chance to have been overridden.
                        ((FlowSelfRenderer) o).toSerialization(this);
                        return (W) this;
                    } else {
                        // o.k. go search for a loose match.
                        renderer = (JsonRenderer<K>) renderers.get(o.getClass());
                    }
                }
                if ( renderer != null ) {
                    renderer.toSerialization(this, o);
                    return (W) this;
                }
                return this.append(JSONObject.valueToString(o));
            } catch (IOException e) {
                throw new JSONException(e);
            } finally {
                try {
                    writer.write(':');
                } catch (IOException e) {
                    throw new JSONException(e);
                }
                comma = false;
                mode = OBJECT_MODE;
            }
        }
    }

    /**
     *
     * @see org.amplafi.flow.json.IJsonWriter#keyValueIfNotBlankValue(java.lang.Object, java.lang.String)
     */
    public <K, W extends SerializationWriter> W keyValueIfNotBlankValue(K key, String value) {
        if ( !StringUtils.isBlank(value)) {
            this.keyValue(key, value);
        }
        return (W) this;
    }

    /**
     *
     * @see org.amplafi.flow.json.IJsonWriter#keyValueIfNotNullValue(java.lang.Object, java.lang.Object)
     */
    public <K,V, W extends SerializationWriter> W keyValueIfNotNullValue(K key, V value) {
        if ( value != null) {
            this.keyValue(key, value);
        }
        return (W) this;
    }

    /**
     *
     * @see org.amplafi.flow.json.IJsonWriter#keyValue(java.lang.Object, java.lang.Object)
     */
    public <K,V, W extends SerializationWriter> W keyValue(K key, V value) {
        this.key(key).value(value);
        return (W) this;
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#isInKeyMode()
     */
    public boolean isInKeyMode() {
        return mode == KEY_MODE;
    }


    /**
     * @see org.amplafi.flow.json.IJsonWriter#object()
     */
    public <W extends SerializationWriter> W object() {
        if (mode == INITIAL_MODE) {
            mode = OBJECT_MODE;
        }
        if (isInObjectMode() || isInArrayMode()) {
            this.append("{");
            this.push(KEY_MODE);
            comma = false;
            return (W) this;
        }
        throw new JSONException("Misplaced object.");
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#isInArrayMode()
     */
    public boolean isInArrayMode() {
        return mode == ARRAY_MODE;
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#isInObjectMode()
     */
    public boolean isInObjectMode() {
        return mode == OBJECT_MODE;
    }
    /**
     * @see org.amplafi.flow.json.IJsonWriter#isInInitialMode()
     */
    public boolean isInInitialMode() {
        return mode == INITIAL_MODE;
    }

    /**
     * Pop an array or object scope.
     * @param c The scope to close.
     * @throws JSONException If nesting is wrong.
     */
    private void pop(char c) throws JSONException {
        if (top <= 0 || stack[top - 1] != c) {
            throw new JSONException("Nesting error.");
        }
        top -= 1;
        mode = top == 0 ? 'd' : stack[top - 1];
    }

    /**
     * Push an array or object scope.
     * @param c The scope to open.
     * @throws JSONException If nesting is too deep.
     */
    private void push(char c) throws JSONException {
        if (top >= maxdepth) {
            throw new JSONException("Nesting too deep.");
        }
        stack[top] = c;
        mode = c;
        top += 1;
    }


    /**
     * @see org.amplafi.flow.json.IJsonWriter#value(boolean)
     */
    public <W extends SerializationWriter> W value(boolean b) throws JSONException {
        return this.value(Boolean.valueOf(b));
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#value(double)
     */
    public <W extends SerializationWriter> W value(double d) throws JSONException {
        return this.value(Double.valueOf(d));
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#value(long)
     */
    public <W extends SerializationWriter> W value(long l) throws JSONException {
        return this.value(Long.valueOf(l));
    }

    @SuppressWarnings("unchecked")
    public <T, W extends SerializationWriter> W value(T o) throws JSONException {
        if ( o != null ) {
            JsonRenderer<T> renderer = (JsonRenderer<T>) renderers.getRaw(o.getClass());
            if ( renderer == null) {
                if (o instanceof FlowSelfRenderer<?>) {
                    // we check after looking in map so that it has a chance to have been overridden.
                    ((FlowSelfRenderer) o).toSerialization(this);
                    return (W) this;
                } else {
                    // o.k. go search for a loose match.
                    renderer = (JsonRenderer<T>) renderers.get(o.getClass());
                }
            }
            if ( renderer != null ) {
                renderer.toSerialization(this, o);
                return (W) this;
            }
        }

        return this.append(JSONObject.valueToString(o));
    }

    /**
     * @see org.amplafi.flow.json.IJsonWriter#addRenderer(java.lang.Class, org.amplafi.flow.json.JsonRenderer)
     */
    public void addRenderer(Class<?> name, FlowRenderer<?> renderer) {
        renderers.put(name, renderer);
    }
    /**
     * @see org.amplafi.flow.json.IJsonWriter#addRenderer(org.amplafi.flow.json.JsonRenderer)
     */
    public void addRenderer(FlowRenderer<?> renderer) {
        this.addRenderer(renderer.getClassToRender(), renderer);
    }

    @Override
    public String toString() {
        if (this.writer instanceof StringWriter) {
            return this.writer.toString();
        } else {
            return super.toString();
        }
    }
}
