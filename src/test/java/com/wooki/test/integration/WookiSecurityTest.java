package com.wooki.test.integration;

import org.apache.tapestry5.test.AbstractIntegrationTestSuite;
import org.testng.annotations.Test;

/**
 * This test class is design to check that URL are secured by Wooki security
 * configuration.
 * 
 * @author ccordenier
 * 
 */
public class WookiSecurityTest extends AbstractIntegrationTestSuite {

	public WookiSecurityTest() {
		super("src/main/webapp", "*firefox");
	}

	@Test
	public void testAnymousUser() {
		open("/john");
		waitForPageToLoad("3000");

	}

}
