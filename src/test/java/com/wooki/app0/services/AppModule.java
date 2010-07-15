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

package com.wooki.app0.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.mockito.Mockito;

import com.wooki.services.internal.TapestryOverrideModule;

/**
 * Test application module.
 * 
 * @author ccordenier
 */
@SubModule(TapestryOverrideModule.class)
public class AppModule
{
    public void contributeWookiRequestExceptionHandler(
            MappedConfiguration<Class, String> exceptionMap)
    {
        exceptionMap.add(IllegalArgumentException.class, "app0/IAEReport");
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("app0", "com.wooki.app0"));
    }

    /**
     * Use to allow pageTester to run with spring and spring-security.
     * 
     * @param config
     * @param requestGlobals
     */
    public static void contributeRequestHandler(OrderedConfiguration<RequestFilter> config,
            final RequestGlobals requestGlobals)
    {
        RequestFilter filter = new RequestFilter()
        {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException
            {
                requestGlobals.storeServletRequestResponse(
                        Mockito.mock(HttpServletRequest.class),
                        Mockito.mock(HttpServletResponse.class));
                return handler.service(request, response);
            }
        };
        config.add("EnsureNonNullHttpRequestAndResponse", filter, "before:*");
    }

}
