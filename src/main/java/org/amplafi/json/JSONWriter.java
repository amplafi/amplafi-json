package org.amplafi.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.amplafi.json.renderers.BooleanJsonRenderer;
import org.amplafi.json.renderers.NumberJsonRenderer;
import org.amplafi.json.renderers.StringJSONOutputRenderer;
import org.apache.commons.lang.StringUtils;

import com.sworddance.beans.MapByClass;


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
public class JSONWriter {
    private static final int maxdepth = 20;
    private MapByClass<JsonRenderer<?>> renderers;

    /**
     * The comma flag determines if a comma should be output before the next
     * value.
     */
    private boolean comma;

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
    private static final char OBJECT_MODE = 'o';
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
        this(w, new MapByClass<JsonRenderer<?>>());
        this.addRenderer(NumberJsonRenderer.INSTANCE);
        this.addRenderer(StringJSONOutputRenderer.INSTANCE);
        this.addRenderer(BooleanJsonRenderer.INSTANCE);
    }

    /**
     * @param w a writer to be wrapped
     * @param renderers
     */
    public JSONWriter(Writer w, MapByClass<JsonRenderer<?>> renderers) {
        this.renderers = renderers;
        comma = false;
        mode = INITIAL_MODE;
        stack = new char[maxdepth];
        top = 0;
        writer = w;
    }

    /**
     * Append a value.
     * @param s A string value.
     * @return this
     * @throws JSONException If the value is out of sequence.
     */
    public JSONWriter append(String s) throws JSONException {
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
            return this;
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
            return this;
        }
        throw new JSONException("Value out of sequence.");
    }

    /**
     * Begin appending a new array. All values until the balancing
     * <code>endArray</code> will be appended to this array. The
     * <code>endArray</code> method must be called to mark the array's end.
     * @return this
     * @throws JSONException If the nesting is too deep, or if the object is
     * started in the wrong place (for example as a key or after the end of the
     * outermost array or object).
     */
    public JSONWriter array() throws JSONException {
        if (mode == INITIAL_MODE || isInObjectMode() || isInArrayMode()) {
            this.push(ARRAY_MODE);
            this.append("[");
            comma = false;
            return this;
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
    private JSONWriter end(char m, char c) throws JSONException {
        if (mode != m) {
            throw new JSONException(m == OBJECT_MODE ? "Misplaced endObject." :
            "Misplaced endArray.");
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
     * End an array. This method most be called to balance calls to
     * <code>array</code>.
     * @return this
     * @throws JSONException If incorrectly nested.
     */
    public JSONWriter endArray() throws JSONException {
        return this.end(ARRAY_MODE, ']');
    }

    /**
     * End an object. This method most be called to balance calls to
     * <code>object</code>.
     * @return this
     * @throws JSONException If incorrectly nested.
     */
    public JSONWriter endObject() throws JSONException {
        return this.end(KEY_MODE, '}');
    }

    /**
     * Append a key. The key will be associated with the next value. In an
     * object, every value must be preceded by a key.
     * @param s A key string.
     * @return this
     * @throws JSONException If the key is out of place. For example, keys
     *  do not belong in arrays or if the key is null.
     */
    public JSONWriter key(String s) throws JSONException {
        if (s == null) {
            throw new JSONException("Null key.");
        }
        if (isInKeyMode()) {
            try {
                if (comma) {
                    writer.write(',');
                }
                writer.write(JSONObject.quote(s));
                writer.write(':');
                comma = false;
                mode = OBJECT_MODE;
                return this;
            } catch (IOException e) {
                throw new JSONException(e);
            }
        }
        throw new JSONException("Misplaced key.");
    }

    /**
     * @return true if creating an {@link JSONObject} and expecting a key.
     */
    public boolean isInKeyMode() {
        return mode == KEY_MODE;
    }


    /**
     * Begin appending a new object. All keys and values until the balancing
     * <code>endObject</code> will be appended to this object. The
     * <code>endObject</code> method must be called to mark the object's end.
     * @return this
     * @throws JSONException If the nesting is too deep, or if the object is
     * started in the wrong place (for example as a key or after the end of the
     * outermost array or object).
     */
    public JSONWriter object() throws JSONException {
        if (mode == INITIAL_MODE) {
            mode = OBJECT_MODE;
        }
        if (isInObjectMode() || isInArrayMode()) {
            this.append("{");
            this.push(KEY_MODE);
            comma = false;
            return this;
        }
        throw new JSONException("Misplaced object.");

    }

    /**
     * @return true if an array is currently being created.
     */
    public boolean isInArrayMode() {
        return mode == ARRAY_MODE;
    }

    /**
     * @return true if expecting a key for the object.
     */
    public boolean isInObjectMode() {
        return mode == OBJECT_MODE;
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
     * Append either the value <code>true</code> or the value
     * <code>false</code>.
     * @param b A boolean.
     * @return this
     * @throws JSONException
     */
    public JSONWriter value(boolean b) throws JSONException {
        return this.value(Boolean.valueOf(b));
    }

    /**
     * Append a double value.
     * @param d A double.
     * @return this
     * @throws JSONException If the number is not finite.
     */
    public JSONWriter value(double d) throws JSONException {
        return this.value(new Double(d));
    }

    /**
     * Append a long value.
     * @param l A long.
     * @return this
     * @throws JSONException
     */
    public JSONWriter value(long l) throws JSONException {
        return this.value(new Long(l));
    }

    /**
     * Append an object value.
     * @param <T> o's type.
     * @param o The object to append. It can be null, or a Boolean, Number,
     *   String, JSONObject, or JSONArray.
     * @return this
     * @throws JSONException If the value is out of sequence.
     */
    @SuppressWarnings("unchecked")
    public <T> JSONWriter value(T o) throws JSONException {
        if ( o != null ) {
            JsonRenderer<T> renderer = (JsonRenderer<T>) renderers.getRaw(o.getClass());
            if ( renderer == null && o instanceof JsonSelfRenderer) {
                // we check after looking in map so that it has a chance to have been overridden.
                ((JsonSelfRenderer) o).toJson(this);
                return this;
            } else {
                // o.k. go search for a loose match.
                renderer = (JsonRenderer<T>) renderers.get(o.getClass());
            }
            if ( renderer != null ) {
                renderer.toJson(this, o);
                return this;
            }
        }

        return this.append(JSONObject.valueToString(o));
    }

    /**
     * add a renderer to handle different different classes of objects.
     * @param name
     * @param renderer
     */
    public void addRenderer(Class<?> name, JsonRenderer<?> renderer) {
        renderers.put(name, renderer);
    }
    public void addRenderer(JsonRenderer<?> renderer) {
        this.addRenderer(renderer.getClassToRender(), renderer);
    }

    /**
     * render key(key).value(value) if value is not a blank string.
     * @param key
     * @param value
     * @return this
     */
    public JSONWriter keyValueIfNotBlankValue(String key, String value) {
        if ( !StringUtils.isBlank(value)) {
            this.keyValue(key, value);
        }
        return this;
    }

    /**
     * render key(key).value(value) if value is not null.
     * @param key
     * @param value
     * @return this
     */
    public JSONWriter keyValueIfNotNullValue(String key, Object value) {
        if ( value != null) {
            this.keyValue(key, value);
        }
        return this;
    }

    /**
     * @param <T>
     * @param key
     * @param value
     */
    public <T> JSONWriter keyValue(String key, T value) {
        this.key(key).value(value);
        return this;
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
