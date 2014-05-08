package com.home.application.adactin.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.home.application.pages.BaseWebPage;
import com.home.utilities.SeleniumException;

/**
 * @author abhishek.sreepal
 * 
 */
public class LogoutPage extends BaseWebPage
{

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(LogoutPage.class);

    public LogoutPage(WebDriver driver)
    {
        super(driver);
    }

    public void goToLoginPage()
    {
        clickElement(logoutPageObjRepo, "loginLink", false, 0, "", "");
    }

    public boolean isInLogOutPage()
    {
        try
        {
            if (isElementDisplayed(logoutPageObjRepo, "loginLink", false, 0, ""))
            {
                return true;
            }
            else
            {
                waitForElementVisible(logoutPageObjRepo, "loginLink", false, 0,
                        "",
                        "[IGNORE this ERROR message]Verifying whether App is logged in");
                return true;
            }
        }
        catch (SeleniumException e)
        {
            return false;
        }
    }

}
