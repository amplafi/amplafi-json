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

import org.amplafi.flow.DataClassDefinition;
import org.amplafi.flow.FlowPropertyDefinition;
import org.amplafi.flow.FlowSelfRenderer;
import org.amplafi.flow.flowproperty.FlowPropertyProvider;
import org.amplafi.flow.translator.AbstractFlowTranslator;
import org.amplafi.flow.translator.SerializationWriter;
import org.amplafi.flow.validation.FlowValidationException;

import com.sworddance.util.ApplicationIllegalStateException;


/**
 * @author patmoore
 *
 */
public class JsonSelfRendererFlowTranslator<T> extends AbstractFlowTranslator<T> {

    /**
     * @see org.amplafi.flow.translator.FlowTranslator#getTranslatedClass()
     */
    @Override
    public Class<FlowSelfRenderer> getTranslatedClass() {
        return FlowSelfRenderer.class;
    }

    @Override
    protected <W extends SerializationWriter> W doSerialize(FlowPropertyDefinition flowPropertyDefinition,
            DataClassDefinition dataClassDefinition, W jsonWriter, T object) {
        return jsonWriter.value(object);
    }

    @Override
    protected T doDeserialize(FlowPropertyProvider flowPropertyProvider, FlowPropertyDefinition flowPropertyDefinition, DataClassDefinition dataClassDefinition, Object serializedObject) throws FlowValidationException {
        try {
            Class<?> dataClass = dataClassDefinition.getDataClass();
            ApplicationIllegalStateException.checkState(!dataClass.isInterface()&&!dataClass.isEnum()&&!dataClass.isAnnotation(), dataClass,": Cannot create new instance of an interface, enum or annotation");
            FlowSelfRenderer jsonSelfRenderer = (FlowSelfRenderer) dataClass.newInstance();
            return (T) jsonSelfRenderer.fromSerialization(serializedObject);
        } catch (InstantiationException e) {
            throw new ApplicationIllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new ApplicationIllegalStateException(e);
        }
    }
}
