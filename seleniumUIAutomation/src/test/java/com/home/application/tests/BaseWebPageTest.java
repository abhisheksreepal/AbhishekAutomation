package com.home.application.tests;

import java.lang.reflect.Method;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.home.application.pages.BaseWebPage;
import com.home.utilities.LocalDriverFactory;
import com.home.utilities.LocalDriverManager;
import com.home.utilities.LoggerUtility;
import com.home.utilities.ObjectRepoUtility;
import com.home.utilities.ReportAndMail;
import com.home.utilities.TestBase;
import com.home.utilities.TestDataUtilty;

public class BaseWebPageTest extends TestBase
{

    private long startTime = 0L;
    private long endTime = 0L;
    public static long timeTaken = 0L;

    public BaseWebPageTest()
    {

    }

    public static PropertiesConfiguration envProperties;

    /**
     * MethodName:start Description:It does initialises my Properties
     * file&selects browser and invoke Url through WebDriver
     * 
     * @throws Exception
     */
    @BeforeSuite(alwaysRun = true)
    public void start() throws Exception
    {

        createFolders();
        updateLog4JXmlFile("LogFile");
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
        new ObjectRepoUtility().loadObjectRepoForAllPages();
        // This is required for class SuccessFailureLogTestListener
        TestDataUtilty.loadTestDataToMemory(defaultEnvironment, application);

    }

    /**
     * 
     * @throws Exception
     */
    @AfterSuite(alwaysRun = true)
    public void close() throws Exception
    {

        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        new ReportAndMail().createSummaryResultLog(envProperties
                .getString("BROWSER_NAME"));

        new ReportAndMail().updateHTML(timeTaken);
        new ReportAndMail().sendMail(timeTaken,
                envProperties.getString("emailUserName"),
                envProperties.getString("emailPassword"),
                envProperties.getString("ReportmailServer"),
                envProperties.getString("Reportrecipients"),
                envProperties.getString("ReportaddressFrom"),
                envProperties.getString("default"),
                envProperties.getString("BROWSER_NAME"));
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpForTestCase(Method method)
    {

        String defaultEnvironment = envProperties.getString("default");
        String url = envProperties.getString(defaultEnvironment + ".url");
        String browser = envProperties.getString("BROWSER_NAME");
        String hub = envProperties.getString("hub");

        WebDriver driver = new LocalDriverFactory()
                .createInstance(browser, hub);
        LocalDriverManager.setWebDriver(driver);
        LocalDriverManager.setLog(Logger.getLogger(method.getName()
                + method.getClass().getName()));

        Logger log = LocalDriverManager.getLog();
        new LoggerUtility(log);
        new BaseWebPage(driver).navigateTo(url);

        logTraceMessage("[OPENING  Driver HASHCODE]" + driver.hashCode());
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUpForTestCase(Method method)
    {
        WebDriver driver = LocalDriverManager.getDriver();
        logTraceMessage("[CLOSING Driver HASHCODE]" + driver.hashCode());
        if (driver != null)
        {
            driver.quit();
        }

    }

}
