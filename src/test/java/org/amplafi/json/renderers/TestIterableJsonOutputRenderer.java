package org.amplafi.json.renderers;

import java.util.LinkedHashSet;
import java.util.Set;

import org.amplafi.json.JSONStringer;
import org.amplafi.json.renderers.IterableJsonOutputRenderer;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
@Test
public class TestIterableJsonOutputRenderer {

    public void testBasic() {
        Set<Object> s = new LinkedHashSet<Object>();
        s.add("abc");
        s.add("def");
        IterableJsonOutputRenderer renderer = new IterableJsonOutputRenderer(true);
        JSONStringer jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        jsonWriter.value(s);
        assertEquals(jsonWriter.toString(), "[\"abc\",\"def\"]");
    }
    public void testNullFiltering() {
        Set<Object> s = new LinkedHashSet<Object>();
        s.add("abc");
        s.add(null);
        s.add("def");
        IterableJsonOutputRenderer renderer = new IterableJsonOutputRenderer(false);
        JSONStringer jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        jsonWriter.value(s);
        assertEquals(jsonWriter.toString(), "[\"abc\",\"def\"]");
    }
    public void testAllowNulls() {
        Set<Object> s = new LinkedHashSet<Object>();
        s.add("abc");
        s.add(null);
        s.add("def");
        IterableJsonOutputRenderer renderer = new IterableJsonOutputRenderer(true);
        JSONStringer jsonWriter = new JSONStringer();
        jsonWriter.addRenderer(renderer);
        jsonWriter.value(s);
        assertEquals(jsonWriter.toString(), "[\"abc\",null,\"def\"]");
    }
}
