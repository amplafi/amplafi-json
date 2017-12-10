package org.amplafi.json.renderers;

import org.amplafi.flow.json.JSONObject;
import org.amplafi.flow.json.JsonSelfRenderer;
import org.amplafi.flow.translator.SerializationWriter;

public class SampleJsonData implements JsonSelfRenderer {

    private String name;

    private int age;

    public SampleJsonData(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public SampleJsonData(JSONObject object) {
        fromSerialization(object);
    }

    @SuppressWarnings("unchecked")
    public <T> T fromSerialization(Object object) {
        JSONObject json = JSONObject.toJsonObject(object);
        this.name = json.getString("name");
        this.age = json.getInt("age");
        return (T) this;
    }
    public <W extends SerializationWriter> W toSerialization(W serializationWriter) {
        serializationWriter.object();
        serializationWriter.keyValue("name", this.name);
        serializationWriter.keyValue("age", this.age);
        serializationWriter.endObject();
        return serializationWriter;
    }

    @Override
    public int hashCode() {
        return 17 * age * this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SampleJsonData) {
            SampleJsonData sampleJsonData = (SampleJsonData) obj;
            return this.age == sampleJsonData.age &&
                this.name.equals(sampleJsonData.name);
        }
        return false;
    }

}
