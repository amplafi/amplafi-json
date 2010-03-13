package org.amplafi.json.renderers;

import org.amplafi.json.IJsonWriter;
import org.amplafi.json.JSONObject;
import org.amplafi.json.JsonSelfRenderer;

public class SampleJsonData implements JsonSelfRenderer {

    private String name;

    private int age;

    public SampleJsonData(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public SampleJsonData(JSONObject object) {
        fromJson(object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T fromJson(Object object) {
        JSONObject json = JSONObject.toJsonObject(object);
        this.name = json.getString("name");
        this.age = json.getInt("age");
        return (T) this;
    }

    @Override
    public IJsonWriter toJson(IJsonWriter jsonWriter) {
        jsonWriter.object();
        jsonWriter.keyValue("name", this.name);
        jsonWriter.keyValue("age", this.age);
        jsonWriter.endObject();
        return jsonWriter;
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
