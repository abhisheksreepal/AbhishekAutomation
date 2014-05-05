package com.home.application.tests;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.home.application.adactin.pages.HomePage;
import com.home.application.adactin.pages.LoginPage;
import com.home.application.pages.BaseWebPage;
import com.home.utilities.LoggerUtility;
import com.home.utilities.ObjectRepoUtility;
import com.home.utilities.ReportAndMail;
import com.home.utilities.TestBase;
import com.home.utilities.TestDataUtilty;

public class BaseWebPageTest extends TestBase
{

    private static Logger log = Logger.getLogger(BaseWebPageTest.class);
    private long startTime = 0L;
    private long endTime = 0L;
    public static long timeTaken = 0L;

    public BaseWebPageTest()
    {

    }

    public static PropertiesConfiguration envProperties;
    public static WebDriver driver;
    public LoginPage loginPage;
    public HomePage homePage;

    // Disable selenium logs, Making level as "warn"
    static
    {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty(
                "org.apache.commons.logging.simplelog.log.org.apache.http",
                "warn");
    }

    /**
     * MethodName:start Description:It does initialises my Properties
     * file&selects browser and invoke Url through WebDriver
     * 
     * @throws Exception
     */
    @BeforeSuite(alwaysRun = true)
    public void start() throws Exception
    {
        LoggerUtility.createFolders();
        LoggerUtility.updateLog4JXmlFile("BaseLog");
        startTime = System.currentTimeMillis();
        try
        {
            envProperties = new PropertiesConfiguration(
                    "src/test/resources/config/environment.properties");
        }
        catch (ConfigurationException e)
        {
            e.printStackTrace();
        }

        String environment = System.getProperty("env");
        if (environment == null)
        {
            environment = "default";

        }

        String defaultEnvironment = envProperties.getString("default");
        String application = envProperties.getString("application");
        String url = envProperties.getString(defaultEnvironment + ".url");
        String browser = envProperties.getString("BROWSER_NAME");

        ObjectRepoUtility.loadObjectRepoForAllPages();
        // This is required for class SuccessFailureLogTestListener
        TestDataUtilty.loadTestDataToMemory(defaultEnvironment, application);

        selectBrowser(browser);

        new BaseWebPage(driver).navigateTo(url);
    }

    /**
     * 
     * @throws Exception
     */
    @AfterSuite(alwaysRun = true)
    public void close() throws Exception
    {
        new BaseWebPage(driver).closeBrowser();
        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        ReportAndMail.createSummaryResultLog(envProperties
                .getString("BROWSER_NAME"));
        ReportAndMail.updateHTML(timeTaken);
        ReportAndMail.sendMail(timeTaken,
                envProperties.getString("emailUserName"),
                envProperties.getString("emailPassword"),
                envProperties.getString("ReportmailServer"),
                envProperties.getString("Reportrecipients"),
                envProperties.getString("ReportaddressFrom"),
                envProperties.getString("default"),
                envProperties.getString("BROWSER_NAME"));
    }

    public WebDriver selectBrowser(String browserName)
    {
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
                driver = new RemoteWebDriver(new URL(
                        envProperties.getString("hub")), capabilities);
            }
            catch (MalformedURLException e)
            {
                LoggerUtility.logErrorMessage(log,
                        "browser not able to open due to malform URL");
            }

        }
        else if (browserName.equalsIgnoreCase("iexplorer"))
        {
            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setBrowserName(browserName);

            try
            {
                driver = new RemoteWebDriver(new URL(
                        envProperties.getString("hub")), capabilities);
            }
            catch (MalformedURLException e)
            {
                LoggerUtility.logErrorMessage(log,
                        "browser not able to open due to malform URL");
            }
        }
        else if (browserName.equalsIgnoreCase("chrome"))
        {
            capabilities = DesiredCapabilities.chrome();
            capabilities.setBrowserName(browserName);

            try
            {
                driver = new RemoteWebDriver(new URL(
                        envProperties.getString("hub")), capabilities);
            }
            catch (MalformedURLException e)
            {
                LoggerUtility.logErrorMessage(log,
                        "browser not able to open due to malform URL");
            }
        }
        else if (browserName.equalsIgnoreCase("htmlunitenabled"))
        {
            capabilities = DesiredCapabilities.htmlUnit();
            capabilities.setBrowserName(browserName);

            try
            {
                driver = new RemoteWebDriver(new URL(
                        envProperties.getString("hub")), capabilities);
            }
            catch (MalformedURLException e)
            {
                LoggerUtility.logErrorMessage(log,
                        "browser not able to open due to malform URL");
            }
        }
        return driver;
    }

    @BeforeMethod(alwaysRun = true)
    public void launchApplicationToDefaultUrl(Method method)
    {
        // Create log file before every test case - log for each test case
        LoggerUtility.updateLog4JXmlFile(method.getName());
        restartBrowserToDefaultUrl();

    }

    public void restartBrowserToDefaultUrl()
    {
        new BaseWebPage(driver).closeBrowser();
        selectBrowser(envProperties.getString("BROWSER_NAME"));
        String defaultEnvironment = envProperties.getString("default");
        String url = envProperties.getString(defaultEnvironment + ".url");
        new BaseWebPage(driver).navigateTo(url);
        if (driver instanceof InternetExplorerDriver)
        {
            driver.get("javascript:document.getElementById('overridelink').click();");
        }
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
    }

}
