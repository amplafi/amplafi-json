package org.amplafi.flow.json;

/**
 * implementer will produce a JSON representation of the class in question.
 *
 * Roughly equivalent to java.io.Externalizable
 *
 * see a bunch of notes on {@link org.amplafi.flow.json.JsonRenderer}.
 *
 * @author Patrick Moore
 *
 */
//this is bad because it encourages the developer to serialize directly to an output format.
// however, in this case json is a nice generic format
@Deprecated
public interface JsonSelfRenderer {
    public IJsonWriter toJson(IJsonWriter jsonWriter);
    /**
     * @param <T> class returned.
     * @param object some JSON object. It should be the same kind of object written by the {@link #toJson(IJsonWriter)} method.
     * @return the created object. Usually this (but it doesn't have to be)
     */
    public <T> T fromJson(Object object);
}
