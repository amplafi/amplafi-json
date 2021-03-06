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
package org.amplafi.flow.json.renderers;

import java.net.URI;

import org.amplafi.flow.json.JsonRenderer;
import org.amplafi.flow.translator.SerializationWriter;
import org.apache.commons.lang.ObjectUtils;

import com.sworddance.util.UriFactoryImpl;

/**
 * @author patmoore
 *
 */
public class UriJsonRenderer implements JsonRenderer<URI> {

    public static final UriJsonRenderer INSTANCE = new UriJsonRenderer();
    /**
     * @see org.amplafi.flow.json.JsonRenderer#fromSerialization(java.lang.Class, java.lang.Object, Object...)
     */
    @SuppressWarnings("unchecked")
    public <K> K fromSerialization(Class<K> clazz, Object value, Object... parameters) {
        if ( value == null) {
            return null;
        } else {
            return (K) UriFactoryImpl.createUri(value.toString());
        }
    }

    /**
     * @see org.amplafi.flow.json.JsonRenderer#getClassToRender()
     */
    public Class<? extends URI> getClassToRender() {
        return URI.class;
    }
    public <W extends SerializationWriter> W toSerialization(W serializationWriter, URI o) {
        return serializationWriter.value(ObjectUtils.toString(o, null));
    }

}
