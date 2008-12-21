/**
 *
 */
package org.amplafi.json.renderers;

import static org.testng.Assert.assertEquals;

import java.util.Calendar;

import org.amplafi.json.renderers.CalendarJsonRenderer;
import org.testng.annotations.Test;

@Test
public class TestCalendarJsonRenderer {
    public void testBasic() {
        Calendar inCal = Calendar.getInstance();
        Calendar outCal = TestJsonRendererUtil.toFromJson(new CalendarJsonRenderer(), inCal);
        assertEquals(outCal, inCal);
    }
    
}