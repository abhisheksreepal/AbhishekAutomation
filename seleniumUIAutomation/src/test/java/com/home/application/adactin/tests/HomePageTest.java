package com.home.application.adactin.tests;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.home.application.adactin.pages.HomePage;
import com.home.application.adactin.pages.LoginPage;
import com.home.application.adactin.pages.LogoutPage;
import com.home.application.tests.BaseWebPageTest;
import com.home.utilities.DataProviderFromMapUtility;
import com.home.utilities.LoggerUtility;

public class HomePageTest extends BaseWebPageTest
{

    private static Logger log = Logger.getLogger(HomePageTest.class);

    @Test(dataProvider = "initializeDataProvider", dataProviderClass = DataProviderFromMapUtility.class)
    public void testWelcomeMessage(String userName, String password,
            String expectedWelcomeMessage)
    {

        new LoginPage().loginToApp(userName, password);
        String message = new HomePage().getWelcomeMessage();
        if (expectedWelcomeMessage.equals(message))
        {
            LoggerUtility.logVerifyPass(log,
                    "Application is showing correct Welcome Message -"
                            + message);
        }
        else
        {
            LoggerUtility.logVerifyFailure(log,
                    "Application is showing incorrect Welcome Message -["
                            + message + "] and Expected message -["
                            + expectedWelcomeMessage + "]",
                    new LoginPage().driver);
        }

    }

    @Test(dataProvider = "initializeDataProvider", dataProviderClass = DataProviderFromMapUtility.class)
    public void testLogout(String userName, String password)
    {

        new LoginPage().loginToApp(userName, password);
        new HomePage().clickHyperlinks("Logout");
        if (new LogoutPage().isInLogOutPage())
        {
            LoggerUtility.logVerifyPass(log, "Application is Logged out");
        }
        else
        {
            LoggerUtility.logVerifyFailure(log,
                    "Application is Not Logged out", new LogoutPage().driver);
        }

    }

    @Test(dataProvider = "initializeDataProvider", dataProviderClass = DataProviderFromMapUtility.class)
    public void testIsHyperLinksPresent(String userName, String password,
            String hyperLinks)
    {

        new LoginPage().loginToApp(userName, password);

        if (new HomePage().isHyperLinkPresent(hyperLinks))
        {
            LoggerUtility.logVerifyPass(log, "HyperLink - " + hyperLinks
                    + " present");
        }
        else
        {
            LoggerUtility.logVerifyFailure(log, "HyperLink - " + hyperLinks
                    + " NOT present", new HomePage().driver);
        }

    }

    @Test(dataProvider = "initializeDataProvider", dataProviderClass = DataProviderFromMapUtility.class)
    public void testWelcomeMessageIndifferentPages(String userName,
            String password, String hyperLinks, String expectedWelcomeMessage)
    {

        new LoginPage().loginToApp(userName, password);

        if (new HomePage().isHyperLinkPresent(hyperLinks))
        {
            LoggerUtility.logVerifyPass(log, "HyperLink - " + hyperLinks
                    + " present");
        }
        else
        {
            LoggerUtility.logVerifyFailure(log, "HyperLink - " + hyperLinks
                    + " NOT present", new HomePage().driver);
        }

        new HomePage().clickHyperlinks(hyperLinks);
        String message = new HomePage().getWelcomeMessage();
        if (expectedWelcomeMessage.equals(message))
        {
            LoggerUtility.logVerifyPass(log,
                    "Application is showing correct Welcome Message -"
                            + message);
        }
        else
        {
            LoggerUtility.logVerifyFailure(log,
                    "Application is showing incorrect Welcome Message -["
                            + message + "] in ["+hyperLinks+"] page and Expected message -["
                            + expectedWelcomeMessage + "]",
                    new LoginPage().driver);
        }

    }

}
