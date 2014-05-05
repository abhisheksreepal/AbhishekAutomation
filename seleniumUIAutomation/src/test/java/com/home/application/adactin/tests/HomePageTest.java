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

}
