//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.ComponentEventException;

import com.wooki.WookiEventConstants;

/**
 * This component send a request to the server and remove an element on the client side if the
 * server returns true.
 * 
 * @author ccordenier
 */
public class ClickAndRemove extends AbstractLink
{

    private static final String CLICK_AND_REMOVE = "clickAndRemove";

    /** The id of entity to remove */
    @Parameter(required = true, allowNull = false)
    private Long entityId;

    /** The prefix associated to element to remove */
    @Parameter(value = "ent-", defaultPrefix = BindingConstants.LITERAL)
    private String domPrefix;

    @Inject
    private ComponentResources resources;

    @Inject
    private RenderSupport support;

    void beginRender(MarkupWriter writer)
    {
        if (isDisabled()) return;
        Link link = resources.createEventLink(CLICK_AND_REMOVE, this.entityId);
        writeLink(writer, link);
    }

    void afterRender(MarkupWriter writer)
    {
        if (isDisabled()) return;
        writer.end(); // <a>
    }

    @AfterRender
    public void initClickAndPlay()
    {
        Link link = resources.createEventLink(CLICK_AND_REMOVE, this.entityId);
        JSONObject data = new JSONObject();
        data.put("elt", this.getClientId());
        data.put("url", link.toURI());
        data.put("toRemove", domPrefix + entityId);
        support.addInit("initClickAndRemove", data);
    }

    @OnEvent(value = CLICK_AND_REMOVE)
    public Object clickAndRemove(Long entityId) throws Throwable
    {
        final JSONObject data = new JSONObject();
        try
        {
            resources.triggerEvent(WookiEventConstants.REMOVE, new Object[]
            { entityId }, null);
            data.put("result", true);
            return data;
        }
        catch (ComponentEventException cEx)
        {
            throw cEx.getCause();
        }
    }

}
