/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */
package org.amplafi.json;

/*
 * @author patmoore
 */
public interface IJsonWriter {

    /**
     * Append a value.
     * @param s A string value.
     * @return this
     * @throws JSONException If the value is out of sequence.
     */
    public abstract IJsonWriter append(String s) throws JSONException;

    /**
     * Begin appending a new array. All values until the balancing
     * <code>endArray</code> will be appended to this array. The
     * <code>endArray</code> method must be called to mark the array's end.
     * @return this
     * @throws JSONException If the nesting is too deep, or if the object is
     * started in the wrong place (for example as a key or after the end of the
     * outermost array or object).
     */
    public abstract IJsonWriter array() throws JSONException;

    /**
     * End an array. This method most be called to balance calls to
     * <code>array</code>.
     * @return this
     * @throws JSONException If incorrectly nested.
     */
    public abstract IJsonWriter endArray() throws JSONException;

    /**
     * End an object. This method most be called to balance calls to
     * <code>object</code>.
     * @return this
     * @throws JSONException If incorrectly nested.
     */
    public abstract IJsonWriter endObject() throws JSONException;

    /**
     * Append a key. The key will be associated with the next value. In an
     * object, every value must be preceded by a key.
     * @param <K>
     * @param o A key string.
     * @return this
     * @throws JSONException If the key is out of place. For example, keys
     *  do not belong in arrays or if the key is null.
     */
    //    public <K> JSONWriter key(K o) throws JSONException {
    //        if (o == null) {
    //            throw new JSONException("Null key.");
    //        }
    //        String s = ObjectUtils.toString(o);
    //        if (isInKeyMode()) {
    //            try {
    //                if (comma) {
    //                    writer.write(',');
    //                }
    //                writer.write(JSONObject.quote(s));
    //                writer.write(':');
    //                comma = false;
    //                mode = OBJECT_MODE;
    //                return this;
    //            } catch (IOException e) {
    //                throw new JSONException(e);
    //            }
    //        }
    //        throw new JSONException("Misplaced key.");
    //    }
    public abstract <K> IJsonWriter key(K o) throws JSONException;

    /**
     * render key(key).value(value) if value is not a blank string.
     * @param key
     * @param value
     * @return this
     */
    public abstract <K> IJsonWriter keyValueIfNotBlankValue(K key, String value);

    /**
     * render key(key).value(value) if value is not null.
     * @param key
     * @param value
     * @return this
     */
    public abstract <K,V> IJsonWriter keyValueIfNotNullValue(K key, V value);

    /**
     * @param <K>
     * @param <V>
     * @param key
     * @param value
     * @return this
     */
    public abstract <K, V> IJsonWriter keyValue(K key, V value);

    /**
     * @return true if creating an {@link JSONObject} and expecting a key.
     */
    public abstract boolean isInKeyMode();

    /**
     * Begin appending a new object. All keys and values until the balancing
     * <code>endObject</code> will be appended to this object. The
     * <code>endObject</code> method must be called to mark the object's end.
     * @return this
     * @throws JSONException If the nesting is too deep, or if the object is
     * started in the wrong place (for example as a key or after the end of the
     * outermost array or object).
     */
    public abstract IJsonWriter object() throws JSONException;

    /**
     * @return true if an array is currently being created.
     */
    public abstract boolean isInArrayMode();

    /**
     * @return true if expecting a value, a call to {@link #object()}, or {@link #array()}
     */
    public abstract boolean isInObjectMode();
    /**
     * @return true if expecting a value, a call to {@link #object()}, or {@link #array()}
     */
    public abstract boolean isInInitialMode();
    /**
     * Append either the value <code>true</code> or the value
     * <code>false</code>.
     * @param b A boolean.
     * @return this
     * @throws JSONException
     */
    public abstract IJsonWriter value(boolean b) throws JSONException;

    /**
     * Append a double value.
     * @param d A double.
     * @return this
     * @throws JSONException If the number is not finite.
     */
    public abstract IJsonWriter value(double d) throws JSONException;

    /**
     * Append a long value.
     * @param l A long.
     * @return this
     * @throws JSONException
     */
    public abstract IJsonWriter value(long l) throws JSONException;

    /**
     * Append an object value.
     * @param <T> o's type.
     * @param o The object to append. It can be null, or a Boolean, Number,
     *   String, JSONObject, or JSONArray.
     * @return this
     * @throws JSONException If the value is out of sequence.
     */
    public abstract <T> IJsonWriter value(T o) throws JSONException;

    /**
     * add a renderer to handle different different classes of objects.
     * @param name
     * @param renderer
     */
    public abstract void addRenderer(Class<?> name, JsonRenderer<?> renderer);

    public abstract void addRenderer(JsonRenderer<?> renderer);

}