/**
 * Copyright 2006-2011 by Amplafi. All rights reserved.
 * Confidential.
 */
package org.amplafi.json.renderers;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * @author patmoore
 *
 */
public class TestClassJsonRenderer {

    @Test
    public void testBasic() {

        Class<?> outCal = TestJsonRendererUtil.toFromJsonInArray(new ClassJsonRenderer(), String.class);
        assertEquals(outCal, String.class);
    }
}
