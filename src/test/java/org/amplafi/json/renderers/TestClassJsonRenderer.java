/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */
package org.amplafi.json.renderers;

import static org.testng.Assert.*;

import org.amplafi.flow.translator.ClassFlowRenderer;
import org.testng.annotations.Test;

/**
 * @author patmoore
 *
 */
public class TestClassJsonRenderer {

    @Test
    public void testBasic() {

        Class<?> outCal = TestJsonRendererUtil.toFromJsonInArray(new ClassFlowRenderer(), String.class);
        assertEquals(outCal, String.class);
    }
}
