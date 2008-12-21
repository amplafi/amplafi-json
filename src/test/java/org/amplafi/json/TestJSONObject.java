package org.amplafi.json;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.Assert;
import static org.amplafi.json.JSONObject.quote;
import static org.amplafi.json.JSONObject.unquote;

import java.io.StringWriter;

/**
 * Tests {@link JSONObject}.
 */
public class TestJSONObject extends Assert {
    @Test
    public void testWriteInitialization() {
        JSONObject json = new JSONObject("{a:1,b=true,c='ok'}");

        StringWriter writer = new StringWriter();
        json.writeInitialization(writer, "root");

        assertEquals(writer.toString(), "root.a=1;root.b=true;root.c=\"ok\";");
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
}
