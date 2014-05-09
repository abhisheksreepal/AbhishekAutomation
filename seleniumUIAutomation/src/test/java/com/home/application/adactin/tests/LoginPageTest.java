package com.home.application.adactin.tests;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.home.application.adactin.pages.HomePage;
import com.home.application.adactin.pages.LoginPage;
import com.home.application.tests.BaseWebPageTest;
import com.home.utilities.DataProviderFromMapUtility;
import com.home.utilities.LoggerUtility;

public class LoginPageTest extends BaseWebPageTest
{

    private static Logger log = Logger.getLogger(LoginPageTest.class);

  
    
    
   @Test(dataProvider = "initializeDataProvider",dataProviderClass=DataProviderFromMapUtility.class)
   
    public void testLogin(String userName, String password, String isValid)
    {
       
       
       new LoginPage().loginToApp(userName, password); 
     

        if (isValid.equalsIgnoreCase("Y"))
        {
            if (new HomePage().isLoggedIn())
            {
                LoggerUtility.logVerifyPass(log, "Home Page logged in for -"
                        + userName);
            }
            else
            {
                LoggerUtility.logVerifyFailure(log,
                        "Home Page NOT logged in for -" + userName,new HomePage().driver);
            }
        }
        else
        {
            if (new HomePage().isLoggedIn())
            {
                LoggerUtility.logVerifyFailure(log, "Home Page logged in for -"
                        + userName,new HomePage().driver);
            }
            else
            {
                LoggerUtility.logVerifyPass(log,
                        "Home Page NOT logged in for -" + userName);
            }
        }
    }

}
