/**
 *
 */
package org.amplafi.json.renderers;

import static org.testng.Assert.assertEquals;

import java.util.Calendar;
import java.util.TimeZone;
import static java.util.concurrent.TimeUnit.*;

import org.amplafi.json.renderers.CalendarJsonRenderer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestCalendarJsonRenderer {
    @Test
    public void testBasic() {
        Calendar inCal = Calendar.getInstance();
        Calendar outCal = TestJsonRendererUtil.toFromJson(CalendarJsonRenderer.INSTANCE, inCal);
        assertEquals(outCal, inCal);
    }

    @Test(dataProvider="dates", enabled=false)
    public void testIsoSerialization(String dateStr, Calendar expected) {
        Calendar actual = CalendarJsonRenderer.INSTANCE.fromJson(Calendar.class, dateStr);
        assertEquals(actual.getTimeInMillis(), expected.getTimeInMillis(), MILLISECONDS.toDays(expected.getTimeInMillis()));
    }

    @DataProvider(name="dates")
    public Object[][] getDates() {
        return new Object[][] {
            new Object[] { "1970-01-01", getCal(1970,0,1) },
            new Object[] { "{\"" + CalendarJsonRenderer.TIME_IN_MILLIS+ "\":0, }", getCal(1970,0,1) },
            new Object[] { "{\"" + CalendarJsonRenderer.TIME_IN_MILLIS+ "\":0, \""+CalendarJsonRenderer.TIMEZONE_ID +"\": \"GMT\" }", getCal(1970,0,1) },
            new Object[] { "1970-01-05", getCal(1970,0,5) },
            // 1970-01-05 ( 4 full days have passed... )
            new Object[] { "{\"" + CalendarJsonRenderer.TIME_IN_MILLIS+ "\": "+ DAYS.toMillis(4) +" }", getCal(1970,0,5) },
            new Object[] { "{\"" + CalendarJsonRenderer.TIME_IN_MILLIS+ "\": "+ DAYS.toMillis(4) +", \""+CalendarJsonRenderer.TIMEZONE_ID +"\": \"GMT\" }", getCal(1970,0,5) },
        };
    }

    private Calendar getCal(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.clear();
        calendar.set(year, month, date);
        return calendar;
    }
}