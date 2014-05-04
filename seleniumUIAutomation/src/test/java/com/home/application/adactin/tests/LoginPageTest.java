package com.home.application.adactin.tests;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.home.application.tests.BaseWebPageTest;
import com.home.utilities.DataProviderFromMapUtility;
import com.home.utilities.LoggerUtility;

public class LoginPageTest extends BaseWebPageTest
{

    private static Logger log = Logger.getLogger(LoginPageTest.class);

  
    
    
   @Test(dataProvider = "initializeDataProvider",dataProviderClass=DataProviderFromMapUtility.class)
   
    public void testLogin(String userName, String password, String isValid)
    {
        loginPage.loginToApp(userName, password);

        if (isValid.equalsIgnoreCase("Y"))
        {
            if (homePage.isLoggedIn())
            {
                LoggerUtility.logVerifyPass(log, "Home Page logged in for -"
                        + userName);
            }
            else
            {
                LoggerUtility.logVerifyFailure(log,
                        "Home Page NOT logged in for -" + userName);
            }
        }
        else
        {
            if (homePage.isLoggedIn())
            {
                LoggerUtility.logVerifyFailure(log, "Home Page logged in for -"
                        + userName);
            }
            else
            {
                LoggerUtility.logVerifyPass(log,
                        "Home Page NOT logged in for -" + userName);
            }
        }
    }

}
