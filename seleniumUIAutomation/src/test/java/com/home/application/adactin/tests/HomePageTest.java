package com.home.application.adactin.tests;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

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
        try
        {
            LoggerUtility.logTraceMessage(log,
                    Long.toString(Thread.currentThread().getId()));
            Thread.sleep(60000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        loginPage.loginToApp(userName, password);
        String message = homePage.getWelcomeMessage();
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
                            + expectedWelcomeMessage + "]");
        }

    }

    @Test(dataProvider = "initializeDataProvider", dataProviderClass = DataProviderFromMapUtility.class)
    public void testLogout(String userName, String password,
            String expectedWelcomeMessage)
    {
        try
        {
            LoggerUtility.logTraceMessage(log,
                    Long.toString(Thread.currentThread().getId()));

            Thread.sleep(60000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        loginPage.loginToApp(userName, password);
        homePage.clickHyperlinks("Logout");
        if (logoutPage.isInLogOutPage())
        {
            LoggerUtility.logVerifyPass(log, "Application is Logged out");
        }
        else
        {
            LoggerUtility
                    .logVerifyFailure(log, "Application is Not Logged out");
        }

    }

}
