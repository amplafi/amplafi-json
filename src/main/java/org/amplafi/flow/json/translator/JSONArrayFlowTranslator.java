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
package org.amplafi.flow.json.translator;

import org.amplafi.flow.FlowPropertyDefinition;
import org.amplafi.flow.flowproperty.DataClassDefinition;
import org.amplafi.flow.flowproperty.FlowPropertyProvider;
import org.amplafi.flow.json.JSONArray;
import org.amplafi.flow.translator.AbstractFlowTranslator;
import org.amplafi.flow.translator.SerializationWriter;
import org.amplafi.flow.validation.FlowValidationException;

public class JSONArrayFlowTranslator extends AbstractFlowTranslator<JSONArray> {
    @Override
    public Class<?> getTranslatedClass() {
        return JSONArray.class;
    }

    @Override
    @SuppressWarnings("unused")
    protected <W extends SerializationWriter> W doSerialize(FlowPropertyDefinition flowPropertyDefinition, DataClassDefinition dataClassDefinition,
        W jsonWriter, JSONArray object) {
        // empty json are appended as null but here we want to preserve them, so write them as {}
        if (object!=null && object.equals(null)) {
            jsonWriter.array();
            jsonWriter.endArray();
            return jsonWriter;
        }
        return jsonWriter.value(object);
    }

    @Override
    @SuppressWarnings("unused")
    protected JSONArray doDeserialize(FlowPropertyProvider flowPropertyProvider, FlowPropertyDefinition flowPropertyDefinition,
                                       DataClassDefinition dataClassDefinition, Object serializedObject) throws FlowValidationException {
        return JSONArray.toJsonArray(serializedObject);
    }
}
