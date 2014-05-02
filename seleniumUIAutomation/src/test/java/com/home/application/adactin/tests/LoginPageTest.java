package com.home.application.adactin.tests;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.home.application.tests.BaseWebPageTest;
import com.home.utilities.LoggerUtility;

public class LoginPageTest extends BaseWebPageTest
{

    private static Logger log = Logger.getLogger(LoginPageTest.class);

    public LoginPageTest()
    {

    }

    @Test
    public void testLoginWithInvalidUserName(Method methodName)
    {
        loginPage.launchApplicationToDefaultUrl();
        loginPage.loginToApp("invalidUSer", "a");
        if (!loginPage.isLoggedOut())
        {
            LoggerUtility.logErrorMessage(log, "Invalid user able to log in");
        }
        else
        {
            LoggerUtility.logTraceMessage(log,
                    "Invalid user is NOT able to log in");
        }
    }

}
