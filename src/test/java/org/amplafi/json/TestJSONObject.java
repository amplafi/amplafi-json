package org.amplafi.json;

import java.io.StringWriter;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.amplafi.json.JSONObject.*;

/**
 * Tests {@link JSONObject}.
 */
public class TestJSONObject extends Assert {
    @Test(dataProvider = "writeInitData")
    public void testWriteInitialization(boolean onlyIfUndefined, String expected) {
        JSONObject json = new JSONObject("{a:1,b=true,c='ok'}");

        StringWriter writer = new StringWriter();
        json.writeInitialization(writer, onlyIfUndefined, "root");

        String actual = writer.toString().replace("\n", "");
		assertEquals(actual, expected);
    }

    @DataProvider(name =  "writeInitData")
    public Object[][] getWriteInitData() {
    	return new Object[][] {
    	    new Object[] { false, "if(typeof(root)==\"undefined\"){root={};}root.a=1;root.b=true;root.c=\"ok\";" },
    	    new Object[] { true, "if(typeof(root)==\"undefined\"){root={};}if(typeof(root.a)==\"undefined\"){root.a=1;}" +
    	    		"if(typeof(root.b)==\"undefined\"){root.b=true;}" +
    	    		"if(typeof(root.c)==\"undefined\"){root.c=\"ok\";}" },
    	};
    }
    @Test
    public void testQuoteBeforeSpaceChars() {
        String s = new String(new char[]{31,30});
        assertEquals(quote(s), "\"\\u001f\\u001e\"");
    }

    @Test(dataProvider = "QuoteData")
    public void testQuote(String text) {
        String quoted = quote(text);
        assertEquals(unquote(quoted), text);
    }

    @Test(dataProvider = "QuoteData")
    public void testQuoteExtreme(String text) {
        String quoted = quote(text);

        quoted = quote(quoted);
        quoted = quote(quoted);

        quoted = unquote(quoted);
        quoted = unquote(quoted);

        assertEquals(unquote(quoted), text);
    }

    @DataProvider(name = "QuoteData")
    protected Object[][] getQuoteData() {
        return new Object[][]{
                new Object[]{ "hello" },
                new Object[]{ "hello <b>there</b>" },
                new Object[]{ "hel\nlo/ \t<b>there</b>" },
                new Object[]{ "he\bllo\f <b>there</b>\r" },
                new Object[]{ "hi" + (char)11 + (char)20 + "<img/>" },
        };
    }

    @Test
    public void testNullInput(){
    	JSONObject o;
    	o = JSONObject.toJsonObject(null);
    	assertTrue(o.isEmpty());
    	o = JSONObject.toJsonObject("");
    	assertTrue(o.isEmpty());
    	o = JSONObject.toJsonObject("null");
    	assertTrue(o.isEmpty());
    }

    @Test
    public void testOptInteger() {
    	JSONObject json = new JSONObject("{a:1}");
    	assertEquals(json.optInteger("a", 2), Integer.valueOf(1));
    	assertEquals(json.optInteger("b", 2), Integer.valueOf(2));
    }

    @Test(dataProvider="toFlatten")
    public void testFlatten(JSONObject toFlatten, Object expected) {
        Object actual = toFlatten.flatten();
        assertEquals(actual, expected);
    }

    @DataProvider(name="toFlatten")
    public Object[][] getToFlatten() {
        return new Object[][] {
            // empty objects are flattened to nothing
            new Object[] { JSONObject.toJsonObject(null), null },
            new Object[] { JSONObject.toJsonObject("{'key': []}"), null },
            new Object[] { JSONObject.toJsonObject("{'key': {}}"), null },
        };
    }
}
