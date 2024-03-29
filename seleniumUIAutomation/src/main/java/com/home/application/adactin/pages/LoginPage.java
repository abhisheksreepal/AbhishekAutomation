package com.home.application.adactin.pages;

import com.home.application.pages.BaseWebPage;
import com.home.utilities.SeleniumException;

/**
 * @author abhishek.sreepal
 * 
 */
public class LoginPage extends BaseWebPage
{

    public LoginPage()
    {
    }

    public void loginToApp(String username, String password)
    {
        setTextAfterClearingElement(loginObjRepo, "userName", username, false,
                0, "", "UserName field in Login Page not visible");
        setTextAfterClearingElement(loginObjRepo, "passWord", password, false,
                0, "", "Password field in  Login Page not visible");
        clickElement(loginObjRepo, "loginButton", false, 0, "",
                "Login field in Login Page not visible");
        return;
    }

    public boolean isInLoginPage()
    {
        try
        {
            if (isElementDisplayed(loginObjRepo, "userName", false, 0, ""))
            {
                return true;
            }
            else
            {
                waitForElementVisible(loginObjRepo, "userName", false, 0, "",
                        "Login field -userName in Login Page not visible");
                return true;
            }
        }
        catch (SeleniumException e)
        {
            return false;
        }
    }
}
