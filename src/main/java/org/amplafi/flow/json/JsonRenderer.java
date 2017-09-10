/*
 * Created on Jun 4, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.flow.json;

/**
 * Implementers will generate a JSON representation of a class instance.
 *
 * json serialization deserialization is inconsistent because:
1) deserialization of objects can require db access ( getting AmpEntityImpl )
2) JSONWriters /JSONObjects aren't always created in such a way that the JSONRenderers are available. (objects are created that do not have access to services - db objects for example, we don't want to be injecting services into db objects - a couple of years ago ran into various issues with this )
3) Flows required the ability to have a db object + changes to db object be serialized ( RealizedExternalServiceInstanceImpl / ConfiguringESII - is the primary example )
4) some times we need to serialize the entire object and some times just the db id.
5) ProxyMapper (in sworddance package ) is an ongoing experiment for handling (#4 )
6 ) Json library is kind of old other libraries out there seem better suited to providing the hooks that we need ( see #2215)
7) (to the above list ) sometimes we are serializing to the browser and so we serialize calendars and dates as javascript new Date(.... )
[6/29/10 12:09:39 PM] patrick moore: I haven't had the time to mentally work through the json issues to make them consistent... I just kind of muddle my way through the latest issue.... but the ideal goal is to :

1) get a better json library
2) decide if ProxyMapper should be fully realized ( would rather use someone else's code )
3) have json be our universal serialization mechanism. ( to/from both db and browser )
[6/29/10 12:13:51 PM] patrick moore: so JsonSelfRender concept is equivalent to java.io.Externalizable ... however Externalizable has the same problem of lack of external awareness:
1) should just the key be serialized or the entire object.
2) when deserializing is the object representing the db state or the db state + changes.
3) which properties are allowed to be changed ( readonly objects ) - this is a security feature to protect against   buggy code that is trying to change things that are not to be changed
 *
 * @author Patrick Moore
 * @param <T>
 */
public interface JsonRenderer<T> {
    public IJsonWriter toJson(IJsonWriter jsonWriter, T o);
    /**
     *
     * @param <K>
     * @param clazz the class of the object that is the expected output ( not necessarily <K> as the result may be something else (for example an id))
     * @param value json representation of some sort, JSONObject, JSONArray, string, etc.
     * @param parameters optional additional parameters
     * @return the object translated from the json object.
     */
    public <K> K fromJson(Class<K> clazz, Object value, Object... parameters);
    public Class<? extends T> getClassToRender();
}
