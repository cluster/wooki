package com.wooki.core.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Append a loader icon to wait zone update completes.
 * 
 * @author ccordenier
 */
@MixinAfter
public class AjaxLoader
{
    /**
     * The class of the element that shows the ajax loader image
     */
    @Parameter(value = "load-more", defaultPrefix = BindingConstants.LITERAL)
    private String loaderClass;

    /**
     * The element to render. The default is derived from the component template.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String element;

    /**
     * The zone to observe.
     */
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String zone;

    /**
     * The id of the element that triggers the zone update.
     */
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String trigger;
    
    @Inject
    private JavaScriptSupport renderSupport;

    @Inject
    private ComponentResources resources;

    private String loader;

    String defaultElement()
    {
        return resources.getElementName("div");
    }

    @BeginRender
    void initMoreLink(MarkupWriter writer)
    {
        loader = renderSupport.allocateClientId("loader");

        JSONObject data = new JSONObject();
        data.put("zone", zone);
        data.put("trigger", trigger);
        data.put("loader", loader);
        renderSupport.addInitializerCall(InitializationPriority.LATE, "initAjaxLoader", data);
    }

    @AfterRender
    void afterRender(MarkupWriter writer)
    {
        writer.element(element, "id", loader, "class", this.loaderClass, "style", "display:none;");
        writer.end();
    }
}
