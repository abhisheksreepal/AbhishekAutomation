package com.home.application.adactin.tests;

import org.testng.annotations.Test;

import com.home.application.adactin.pages.HomePage;
import com.home.application.adactin.pages.LoginPage;
import com.home.application.adactin.pages.LogoutPage;
import com.home.application.tests.BaseWebPageTest;
import com.home.utilities.DataProviderFromMapUtility;

public class HomePageTest extends BaseWebPageTest
{

    @Test(dataProvider = "initializeDataProvider", dataProviderClass = DataProviderFromMapUtility.class)
    public void testWelcomeMessage(String userName, String password,
            String expectedWelcomeMessage)
    {

        new LoginPage().loginToApp(userName, password);
        String message = new HomePage().getWelcomeMessage();
        assertEquals(message, expectedWelcomeMessage,
                "Welcome Message is Not as Expected");

    }

    @Test(dataProvider = "initializeDataProvider", dataProviderClass = DataProviderFromMapUtility.class)
    public void testLogout(String userName, String password)
    {

        new LoginPage().loginToApp(userName, password);
        new HomePage().clickHyperlinks("Logout");
        assertTrue(new LogoutPage().isInLogOutPage(),
                "Aplication is NOT logged out");

    }

    @Test(dataProvider = "initializeDataProvider", dataProviderClass = DataProviderFromMapUtility.class)
    public void testIsHyperLinksPresent(String userName, String password,
            String hyperLinks)
    {

        new LoginPage().loginToApp(userName, password);
        assertTrue(new HomePage().isHyperLinkPresent(hyperLinks), "HyperLink -"
                + hyperLinks + " Not present");

    }

    @Test(dataProvider = "initializeDataProvider", dataProviderClass = DataProviderFromMapUtility.class)
    public void testWelcomeMessageIndifferentPages(String userName,
            String password, String hyperLinks, String expectedWelcomeMessage)
    {

        new LoginPage().loginToApp(userName, password);

        assertTrue(new HomePage().isHyperLinkPresent(hyperLinks), "HyperLink -"
                + hyperLinks + " Not present");

        new HomePage().clickHyperlinks(hyperLinks);
        String message = new HomePage().getWelcomeMessage();
        assertEquals(message, expectedWelcomeMessage,
                "Application is showing incorrect Welcome Message -[" + message
                        + "] in [" + hyperLinks
                        + "] page and Expected message -["
                        + expectedWelcomeMessage + "]");
    }

}
