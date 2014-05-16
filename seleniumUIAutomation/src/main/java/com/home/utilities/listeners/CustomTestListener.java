package com.home.utilities.listeners;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

import com.home.application.pages.BaseWebPage;
import com.home.utilities.LocalDriverFactory;
import com.home.utilities.LocalDriverManager;
import com.home.utilities.LoggerUtility;
import com.home.utilities.TestBase;

public class CustomTestListener extends TestListenerAdapter
{

    private static PropertiesConfiguration envProperties;

    static
    {
        try
        {
            envProperties = new PropertiesConfiguration(
                    "src/test/resources/config/environment.properties");
        }
        catch (ConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result)
    {

        Reporter.setCurrentTestResult(result);

        if (method.isTestMethod())
        {

            List<Throwable> verificationFailures = new TestBase()
                    .getVerificationFailures();

            // if there are verification failures...
            if (verificationFailures.size() > 0)
            {

                // set the test to failed
                result.setStatus(ITestResult.FAILURE);

                // if there is an assertion failure add it to
                // verificationFailures
                if (result.getThrowable() != null)
                {
                    verificationFailures.add(result.getThrowable());
                }

                int size = verificationFailures.size();
                // if there's only one failure just set that
                if (size == 1)
                {
                    result.setThrowable(verificationFailures.get(0));
                }
                else
                {
                    // create a failure message with all failures and stack
                    // traces (except last failure)
                    StringBuffer failureMessage = new StringBuffer(
                            "Multiple failures (").append(size)
                            .append("):\n\n");
                    for (int i = 0; i < size - 1; i++)
                    {
                        failureMessage.append("Failure ").append(i + 1)
                                .append(" of ").append(size).append(":\n");
                        Throwable t = verificationFailures.get(i);
                        String fullStackTrace = Utils.stackTrace(t, false)[1];
                        failureMessage.append(fullStackTrace).append("\n\n");
                    }

                    // final failure
                    Throwable last = verificationFailures.get(size - 1);
                    failureMessage.append("Failure ").append(size)
                            .append(" of ").append(size).append(":\n");
                    failureMessage.append(last.toString());

                    // set merged throwable
                    Throwable merged = new Throwable(failureMessage.toString());
                    merged.setStackTrace(last.getStackTrace());

                    result.setThrowable(merged);
                }
            }
        }
        if (method.isTestMethod())
        {
            WebDriver driver = LocalDriverManager.getDriver();

            if (driver != null)
            {
                driver.quit();

            }
            String className = method.getTestMethod().getTestClass().getName();
            String methodName = method.getTestMethod().getMethodName();
            new LoggerUtility().log.info("[Terminating TestCase - ]"
                    + className + " - " + methodName);

        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result)
    {
        String className = method.getTestMethod().getTestClass().getName();
        String methodName = method.getTestMethod().getMethodName();
        if (method.isTestMethod())
        {
            String browser = method.getTestMethod().getXmlTest()
                    .getLocalParameters().get("browserName");
            String platform = method.getTestMethod().getXmlTest()
                    .getLocalParameters().get("platform");

            if (browser == null)
            {
                browser = envProperties.getString("BROWSER_NAME");
                platform = envProperties.getString("windows");
            }
            else if (browser.trim().equalsIgnoreCase(""))
            {
                browser = envProperties.getString("BROWSER_NAME");
                platform = envProperties.getString("windows");
            }

            String defaultEnvironment = envProperties.getString("default");
            String url = envProperties.getString(defaultEnvironment + ".url");

            String hub = envProperties.getString("hub");

            WebDriver driver = new LocalDriverFactory().createInstance(platform,browser,
                    hub);
            LocalDriverManager.setWebDriver(driver);
            LocalDriverManager.setLog(Logger.getLogger(className + " - "
                    + methodName));

            Logger log = LocalDriverManager.getLog();
            new LoggerUtility(log);
            new BaseWebPage(driver).navigateTo(url);

            new LoggerUtility().log.info("[Launching TestCase - ]" + className
                    + " - " + methodName);

        }
    }
}
