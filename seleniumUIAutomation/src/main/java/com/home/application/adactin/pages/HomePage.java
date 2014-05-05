package com.home.application.adactin.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.home.application.pages.BaseWebPage;
import com.home.utilities.SeleniumException;

/**
 * @author abhishek.sreepal
 * 
 */
public class HomePage extends BaseWebPage
{

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(HomePage.class);

    public HomePage(WebDriver driver)
    {
        super(driver);
    }

    public LoginPage logOffFromApp()
    {
        clickElement(homePageObjRepo, "Logout", false, 0, "",
                "Logout field in Home Page not visible");
        return new LoginPage(driver);

    }

    public boolean isLoggedIn()
    {
        try
        {
            if (isElementDisplayed(homePageObjRepo, "Logout", false, 0, ""))
            {
                return true;
            }
            else
            {
                waitForElementVisible(homePageObjRepo, "Logout", false, 0, "",
                        "[IGNORE this ERROR message]Verifying whether App is logged in");
                return true;
            }
        }
        catch (SeleniumException e)
        {
            return false;
        }
    }

    public String getWelcomeMessage()
    {

        return getAttributeValue(homePageObjRepo, "welcomeMessageString",
                "value", false, 0, "",
                "welcome Message String in Home Page is NOT present");
    }

}
