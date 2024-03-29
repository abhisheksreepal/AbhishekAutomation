package com.home.application.adactin.pages;

import com.home.application.pages.BaseWebPage;
import com.home.utilities.SeleniumException;

/**
 * @author abhishek.sreepal
 * 
 */
public class HomePage extends BaseWebPage
{



    public HomePage()
    {
        
    }

    public void logOffFromApp()
    {
        clickHyperlinks("Logout");

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

    public void clickHyperlinks(String hyperLinks)
    {
        clickElement(homePageObjRepo, "hyperLinks", true, 1, hyperLinks,
                "hyperLinks -" + hyperLinks.toString() + " NOT clicked");

    }
    
    public boolean isHyperLinkPresent(String hyperLink){
        try
        {
            if (isElementDisplayed(homePageObjRepo, "hyperLinks", true, 1, hyperLink))
            {
                return true;
            }
            else
            {
                waitForElementVisible(homePageObjRepo, "hyperLinks", true, 1, hyperLink,
                        "[IGNORE this ERROR message]Verifying whether hyperLink is present -["+hyperLink+"]");
                return true;
            }
        }
        catch (SeleniumException e)
        {
            return false;
        }
    }
    
    
    
       

}
