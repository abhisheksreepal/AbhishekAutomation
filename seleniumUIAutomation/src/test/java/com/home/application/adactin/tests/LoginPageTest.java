package com.home.application.adactin.tests;

import org.testng.annotations.Test;

import com.home.application.adactin.pages.HomePage;
import com.home.application.adactin.pages.LoginPage;
import com.home.application.tests.BaseWebPageTest;
import com.home.utilities.DataProviderFromMapUtility;

public class LoginPageTest extends BaseWebPageTest
{


  
    
    
   @Test(dataProvider = "initializeDataProvider",dataProviderClass=DataProviderFromMapUtility.class)
   
    public void testLogin(String userName, String password, String isValid)
    {
       
       
       new LoginPage().loginToApp(userName, password); 
     

        if (isValid.equalsIgnoreCase("Y"))
        {
            if (new HomePage().isLoggedIn())
            {
                logVerifyPass( "Home Page logged in for -"
                        + userName);
            }
            else
            {
                logVerifyFailure(
                        "Home Page NOT logged in for -" + userName,new HomePage().driver);
            }
        }
        else
        {
            if (new HomePage().isLoggedIn())
            {
                logVerifyFailure( "Home Page logged in for -"
                        + userName,new HomePage().driver);
            }
            else
            {
                logVerifyPass(
                        "Home Page NOT logged in for -" + userName);
            }
        }
    }

}
