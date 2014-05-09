package com.home.utilities;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class LocalDriverFactory
{

    private static Logger log = Logger.getLogger(LocalDriverFactory.class);
    
    public LocalDriverFactory()
    {
        // TODO Auto-generated constructor stub
    }

    public static WebDriver createInstance(String browserName, String hub)
    {
        WebDriver driver = null;
        DesiredCapabilities capabilities = null;
        if (browserName.equalsIgnoreCase("firefox"))
        {
            ProfilesIni ffProfiles = new ProfilesIni();
            FirefoxProfile firefoxprofile;

            try
            {
                firefoxprofile = ffProfiles.getProfile("WebDriver");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                firefoxprofile = ffProfiles.getProfile("default");
            }
            capabilities = DesiredCapabilities.firefox();
            capabilities.setBrowserName(browserName);

            capabilities.setCapability(FirefoxDriver.PROFILE, firefoxprofile);
            try
            {
                driver = new RemoteWebDriver(new URL(hub), capabilities);
            }
            catch (MalformedURLException e)
            {
                LoggerUtility.logErrorMessage(log,
                        "browser not able to open due to malform URL",driver);
            }

        }
        else if (browserName.equalsIgnoreCase("iexplorer"))
        {
            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setBrowserName(browserName);

            try
            {
                driver = new RemoteWebDriver(new URL(hub), capabilities);
            }
            catch (MalformedURLException e)
            {
                LoggerUtility.logErrorMessage(log,
                        "browser not able to open due to malform URL",driver);
            }
        }
        else if (browserName.equalsIgnoreCase("chrome"))
        {
            capabilities = DesiredCapabilities.chrome();
            capabilities.setBrowserName(browserName);

            try
            {
                driver = new RemoteWebDriver(new URL(hub), capabilities);
            }
            catch (MalformedURLException e)
            {
                LoggerUtility.logErrorMessage(log,
                        "browser not able to open due to malform URL",driver);
            }
        }
        else if (browserName.equalsIgnoreCase("htmlunitenabled"))
        {
            capabilities = DesiredCapabilities.htmlUnit();
            capabilities.setBrowserName(browserName);

            try
            {
                driver = new RemoteWebDriver(new URL(hub), capabilities);
            }
            catch (MalformedURLException e)
            {
                LoggerUtility.logErrorMessage(log,
                        "browser not able to open due to malform URL",driver);
            }
        }
        return driver;
    }
}
