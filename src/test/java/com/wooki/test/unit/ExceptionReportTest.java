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

package com.wooki.test.unit;

import org.apache.tapestry5.dom.Document;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExceptionReportTest extends AbstractWookiUnitTestSuite
{

    @Test(enabled = true)
    public void verifyWookiExceptionHandling()
    {
        Document document = pageTester.renderPage("app0/ThrowIAE");
        Assert.assertNotNull(
                document.getElementById("specificReport"),
                "Wiki Exception should handle IllegalArgumentException");

    }

    @Test(enabled = true)
    public void verifyWookiExceptionNonHandling()
    {
        Document document = pageTester.renderPage("app0/ThrowNPE");
        Assert.assertNull(
                document.getElementById("specificReport"),
                "Wiki Exception has not handled NullPointerException");
    }

}
