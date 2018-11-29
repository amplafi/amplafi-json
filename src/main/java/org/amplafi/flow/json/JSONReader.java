package org.amplafi.flow.json;

import java.io.Reader;
import java.util.Optional;

import org.amplafi.flow.FlowTranslatorResolver;
import org.amplafi.flow.translator.FlowRenderer;
import org.amplafi.flow.translator.FlowRendererProvider;
import org.amplafi.flow.translator.SerializationReader;

/**
 * Takes in json object and converts to java objects
 * @author patmoore
 *
 */
public class JSONReader implements SerializationReader {

    private FlowRendererProvider flowRendererProvider;
    private FlowTranslatorResolver flowTranslatorResolver;
    private Reader reader;
    private JSONObject object;

    public JSONReader(Reader reader) {
        this.reader = reader;
    }
    public JSONReader(JSONObject object) {
        this.object = object;
    }
    @Override
    public <T, C> FlowRenderer<T> getFlowRenderer(Class<? extends C> clazz) {
        return (FR) this.flowRendererProvider.getFlowRenderer(clazz);
    }

    @Override
    public void setFlowRendererProvider(FlowRendererProvider flowRendererProvider) {
        this.flowRendererProvider = flowRendererProvider;
    }

    @Override
    public void setFlowTranslatorResolver(FlowTranslatorResolver flowTranslatorResolver) {
        this.flowTranslatorResolver = flowTranslatorResolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends SerializationReader> R optObject(String key) {
        return (R) Optional.ofNullable(this.object)
                .map(json -> json.optJSONObject(key))
                .map(json -> new JSONReader(json))
                .orElse(null);
    }

}
