package com.home.utilities;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class LocalDriverFactory extends LoggerUtility
{

    public LocalDriverFactory(Logger log)
    {
        super(log);
    }

    public LocalDriverFactory()
    {
        super();
    }

    public WebDriver createInstance(String platform, String browserName,
            String hub)
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
                logErrorMessage("browser not able to open due to malform URL",
                        driver);
            }

        }
        else if (browserName.equalsIgnoreCase("iexplorer"))
        {
            System.setProperty(
                    "webdriver.ie.driver",
                    envProperties.getString("IEFilePath") + "/"
                            + envProperties.getString("whichBit") + "/"
                            + envProperties.getString("IEEXEFileName"));
            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setBrowserName(browserName);

            try
            {
                driver = new RemoteWebDriver(new URL(hub), capabilities);
            }
            catch (MalformedURLException e)
            {
                logErrorMessage("browser not able to open due to malform URL",
                        driver);
            }
        }
        else if (browserName.equalsIgnoreCase("chrome"))
        {
            System.setProperty("webdriver.chrome.driver",
                    envProperties.getString("ChromeFilePath") + "/"
                            + envProperties.getString("whichBit") + "/"
                            + envProperties.getString("ChromeEXEFileName"));
            capabilities = DesiredCapabilities.chrome();
            capabilities.setBrowserName(browserName);

            try
            {
                driver = new RemoteWebDriver(new URL(hub), capabilities);
            }
            catch (MalformedURLException e)
            {
                logErrorMessage("browser not able to open due to malform URL",
                        driver);
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
                logErrorMessage("browser not able to open due to malform URL",
                        driver);
            }
        }

        if (platform.equalsIgnoreCase("windows"))
        {
            capabilities.setPlatform(Platform.WIN8);
        }
        else if (platform.equalsIgnoreCase("linux"))
        {
            capabilities.setPlatform(Platform.LINUX);
        }
        else
        {
            capabilities.setPlatform(Platform.WIN8);
        }
        return driver;
    }
}
