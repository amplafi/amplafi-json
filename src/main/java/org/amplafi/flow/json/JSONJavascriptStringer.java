/*
 * Created on Jun 24, 2006
 * Copyright 2006 by Patrick Moore
 */
package org.amplafi.flow.json;

import java.util.Calendar;

import org.amplafi.flow.json.renderers.JavascriptDateOutputRenderer;

// should have all renderers added by JSONRenderRegistry.
@Deprecated
public class JSONJavascriptStringer extends JSONStringer {
    public JSONJavascriptStringer() {
        this.addRenderer(JavascriptDateOutputRenderer.INSTANCE);
        this.addRenderer(Calendar.class, JavascriptDateOutputRenderer.INSTANCE);
    }

}
