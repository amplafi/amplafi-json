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
import org.amplafi.flow.flowproperty.FlowPropertyProviderWithValues;
import org.amplafi.flow.translator.AbstractFlowTranslator;
import org.amplafi.flow.translator.SerializationWriter;
import org.amplafi.flow.validation.FlowValidationException;

import com.sworddance.util.ApplicationIllegalStateException;


/**
 * @author patmoore
 *
 */
@Deprecated // narrow use case that needs to be handled outside
public class FlowAwareJsonSelfRendererFlowTranslator extends AbstractFlowTranslator<Object> {

    /**
     * @see org.amplafi.flow.translator.FlowTranslator#getTranslatedClass()
     */
    @Override
    public Class<FlowAwareJsonSelfRenderer> getTranslatedClass() {
        return FlowAwareJsonSelfRenderer.class;
    }

    /**
     * @see org.amplafi.flow.translator.FlowTranslator#serialize(org.amplafi.flow.FlowPropertyDefinition, org.amplafi.flow.flowproperty.DataClassDefinition , java.lang.Object, java.lang.Object)
     */
    @Override
    protected <W extends SerializationWriter> W doSerialize(FlowPropertyDefinition flowPropertyDefinition, DataClassDefinition dataClassDefinition,
            W jsonWriter, Object object) {
        return jsonWriter.value(object);
    }

    @Override
    protected Object doDeserialize(FlowPropertyProvider flowPropertyProvider, FlowPropertyDefinition flowPropertyDefinition, DataClassDefinition dataClassDefinition, Object serializedObject) throws FlowValidationException {
        try {
            Class<?> dataClass = dataClassDefinition.getDataClass();
            ApplicationIllegalStateException.checkState(!dataClass.isInterface()&&!dataClass.isEnum()&&!dataClass.isAnnotation(), dataClass,": Cannot create new instance of an interface, enum or annotation");
            FlowAwareJsonSelfRenderer jsonSelfRenderer = (FlowAwareJsonSelfRenderer) dataClass.newInstance();
            jsonSelfRenderer.setValuesProvider((FlowPropertyProviderWithValues) flowPropertyProvider);
            return jsonSelfRenderer.fromSerialization(serializedObject);
        } catch (InstantiationException e) {
            throw new ApplicationIllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new ApplicationIllegalStateException(e);
        }
    }
}
