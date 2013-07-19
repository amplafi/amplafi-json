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

import java.io.Serializable;

/**
 * @author patmoore
 *
 */
public interface JsonConstruct extends Serializable {
    /**
         * JSONObject.NULL is equivalent to the value that JavaScript calls null,
         * whilst Java's null is equivalent to the value that JavaScript calls
         * undefined.
         */
        public static final class Null implements Serializable {

		private static final long serialVersionUID = -1225253953875177600L;


			/**
             * There is only intended to be a single instance of the NULL object,
             * so the clone method returns itself.
             * @return     NULL.
             */
            @Override
            protected final Object clone() {
                return this;
            }


            /**
             * A Null object is equal to the null value and to itself.
             * @param object    An object to test for nullness.
             * @return true if the object parameter is the JSONObject.NULL object
             *  or null.
             */
            @Override
            public boolean equals(Object object) {
                return object == null || object == this
                    // to avoid serialization possibly resulting in more than 1 Null
                    || object instanceof Null;
            }

            @Override
			public int hashCode() {
            	return 1;
            }


            /**
             * Get the "null" string value.
             * @return The string "null".
             */
            @Override
            public String toString() {
                return "null";
            }
        }
        
        public static final class Parser {
            
            public static JsonConstruct toJsonConstruct(String serialized) {
                if (serialized == null) {
                    return null;
                } else if (serialized.startsWith("{")) {
                    return JSONObject.toJsonObject(serialized);
                } else if (serialized.startsWith("[")) {
                    return JSONArray.toJsonArray(serialized);
                } else {
                    return null;
                }
            }
            
        }

    /**
     *
     * @return true if there is no data present.
     */
    public boolean isEmpty();

	String toString(int indentationLevel);
}
