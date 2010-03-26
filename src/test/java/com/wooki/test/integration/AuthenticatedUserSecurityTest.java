package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.SeleniumException;

/**
 * This test class is design to check that URL are secured by Wooki security configuration in the
 * case an authenticated user access to unauthorized resources.
 * 
 * @author ccordenier
 */
public class AuthenticatedUserSecurityTest extends AbstractWookiIntegrationTestSuite
{

    /**
     * Register a new user, this is the first method to execute in the test.
     */
    @Test
    public void signup()
    {
        open("/signup");
        waitForPageToLoad();
        Assert.assertTrue(isElementPresent("id=signupForm"), "Cannot load signup page");
        type("id=username", "author");
        type("id=fullname", "Author Doe");
        type("id=email", "author@gmail.com");
        type("id=password", "mylongpassword");
        click("//form[@id='signupForm']//input[@type='submit']");
        waitForPageToLoad();
        checkDashboard("author");
    }

    /**
     * Check index page.
     */
    @Test(dependsOnMethods =
    { "signup" })
    public void testIndex()
    {

        open("/userNotExist", "true");
        waitForPageToLoad();
        checkNotFound();

        open("/");
        waitForPageToLoad();
        checkIndex();

        open("/index");
        waitForPageToLoad();
        checkIndex();

        open("/ccordenier");
        waitForPageToLoad();
        checkProfile("ccordenier");

        open("/1/2", "true");
        waitForPageToLoad();
        checkNotFound();

    }

    /**
     * Test access to dashboard.
     */
    @Test(dependsOnMethods =
    { "signup" })
    public void testDashboard()
    {
        open("/dashboard");
        waitForPageToLoad();
        checkDashboard("author");

        // Try to delete a book that exist but that john does not own
        open("/dashboard:removebook/1", "true");
        waitForPageToLoad();
        checkAccessDenied();

        // Not allowad context
        open("/dashboard/1", "true");
        waitForPageToLoad();
        checkNotFound();
    }

    /**
     * Test access to dashboard.
     */
    @Test(dependsOnMethods =
    { "signup" })
    public void testAccountSettings()
    {
        open("/accountSettings");
        waitForPageToLoad();
        checkAccountSettings("author");

        // Bad URL
        try
        {
            open("/accountSettings/1");
            waitForPageToLoad();
        }
        catch (SeleniumException se)
        {
            assertTrue(se.getMessage().contains("404"));
        }

    }

    /**
     * Verify that the user cannot delete a comment when he is not the owner
     */
    @Test(dependsOnMethods =
    { "signup" })
    public void testComment()
    {
        open("/book/index.commentbubbles.commentdialogcontent.clickandremove:clickandremove/1?t:ac=1", "true");
        checkAccessDenied();
    }

    /**
     * Verify that elements are not present if the user is not author of the book.
     */
    @Test(dependsOnMethods =
    { "signup" })
    public void testBookIndex()
    {
        open("/book/1");
        waitForPageToLoad();
        checkBookTitle(BookNavigationTest.BOOK_TITLE);
        Assert.assertFalse(
                isElementPresent("id='book-admin'"),
                "Admin button should not be present for this book");
        Assert.assertFalse(
                isElementPresent("id='add-chapter-link'"),
                "Admin button should not be present for this book");

        // User is not owner so he has not access to the last copy
        open("/book/1/last", "true");
        waitForPageToLoad();
        checkAccessDenied();
    }

    /**
     * When the user is authenticated, it should not be able to see signin.
     */
    @Test(dependsOnMethods =
    { "signup" })
    public void testSignin()
    {
        open("/signin");
        waitForPageToLoad();
        checkIndex();
    }

    /**
     * When the user is authenticated, it should not be able to see signup.
     */
    @Test(dependsOnMethods =
    { "signup" })
    public void testSignup()
    {
        open("/signup");
        waitForPageToLoad();
        checkIndex();
    }

    /**
     * Logout of the application.
     */
    @Override
    @AfterClass(alwaysRun = true)
    public void cleanup()
    {
        open("/index");
        waitForPageToLoad();
        Assert.assertTrue(
                isElementPresent("id=logout"),
                "Authenticated user should be able to logout");
        click("id=logout");
        waitForPageToLoad();
        checkIndex();
        super.cleanup();
    }
}
