package com.home.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestBase extends LoggerUtility
{

    public TestBase(Logger log)
    {
        super(log);

    }

    public TestBase()
    {
        super();
    }

    private static Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();

    public void assertTrue(boolean condition)
    {
        try
        {
            Assert.assertTrue(condition);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("Condition [" + condition + "] is NOT True");
            throw new AssertionError("Condition [" + condition
                    + "] is NOT True");
        }

    }

    public void assertTrue(boolean condition, String message)
    {
        try
        {
            Assert.assertTrue(condition, message);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("Condition [" + condition
                    + "] is NOT True and failure message = " + message);
            throw new AssertionError("Condition [" + condition
                    + "] is NOT True and failure message = " + message);
        }
    }

    public void assertFalse(boolean condition)
    {
        try
        {
            Assert.assertFalse(condition);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("Condition [" + condition + "] is NOT False");
            throw new AssertionError("Condition [" + condition
                    + "] is NOT False");
        }
    }

    public void assertFalse(boolean condition, String message)
    {
        try
        {
            Assert.assertFalse(condition, message);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("Condition [" + condition
                    + "] is NOT False and failure message = " + message);
            throw new AssertionError("Condition [" + condition
                    + "] is NOT False and failure message = " + message);
        }
    }

    public void assertEquals(boolean actual, boolean expected)
    {
        try
        {
            Assert.assertEquals(actual, expected);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("Actual [" + actual + "] is NOT Equal to Expected ["
                    + expected + "]");
            throw new AssertionError("Actual [" + actual
                    + "] is NOT Equal to Expected [" + expected + "]");
        }
    }

    public void assertEquals(Object actual, Object expected)
    {
        try
        {
            Assert.assertEquals(actual, expected);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("Actual [" + actual + "] is NOT Equal to Expected ["
                    + expected + "]");
            throw new AssertionError("Actual [" + actual
                    + "] is NOT Equal to Expected [" + expected + "]");
        }
    }

    public void assertEquals(Object[] actual, Object[] expected)
    {
        try
        {
            Assert.assertEquals(actual, expected);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("Actual [" + actual + "] is NOT Equal to Expected ["
                    + expected + "]");
            throw new AssertionError("Actual [" + actual
                    + "] is NOT Equal to Expected [" + expected + "]");
        }
    }

    public void assertEquals(Object actual, Object expected, String message)
    {
        try
        {
            Assert.assertEquals(actual, expected, message);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("Actual [" + actual + "] is NOT Equal to Expected ["
                    + expected + "] and message = " + message);
            throw new AssertionError("Actual [" + actual
                    + "] is NOT Equal to Expected [" + expected
                    + "] and message =" + message);
        }
    }

    public void verifyTrue(boolean condition)
    {
        try
        {
            assertTrue(condition);
        }
        catch (Throwable e)
        {
            addVerificationFailure(e);
        }
    }

    public void verifyTrue(boolean condition, String message)
    {
        try
        {
            assertTrue(condition, message);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("[VERIFICATION FAILURE] Condition [" + condition
                    + "] is NOT True");
            addVerificationFailure(e);
        }
    }

    public void verifyFalse(boolean condition)
    {
        try
        {
            assertFalse(condition);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("[VERIFICATION FAILURE] Condition [" + condition
                    + "] is NOT False");
            addVerificationFailure(e);
        }
    }

    public void verifyFalse(boolean condition, String message)
    {
        try
        {
            assertFalse(condition, message);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("[VERIFICATION FAILURE] Condition [" + condition
                    + "] is NOT False and failure message = " + message);
            addVerificationFailure(e);
        }
    }

    public void verifyEquals(boolean actual, boolean expected)
    {
        try
        {
            assertEquals(actual, expected);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("[VERIFICATION FAILURE] Actual [" + actual
                    + "] is NOT Equal to Expected [" + expected + "]");
            addVerificationFailure(e);
        }
    }

    public void verifyEquals(Object actual, Object expected)
    {
        try
        {
            assertEquals(actual, expected);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("[VERIFICATION FAILURE] Actual [" + actual
                    + "] is NOT Equal to Expected [" + expected + "]");
            addVerificationFailure(e);
        }
    }

    public void verifyEquals(Object[] actual, Object[] expected)
    {
        try
        {
            assertEquals(actual, expected);
        }
        catch (Throwable e)
        {
            new LoggerUtility().log.error("[VERIFICATION FAILURE] Actual [" + actual
                    + "] is NOT Equal to Expected [" + expected + "]");
            addVerificationFailure(e);
        }
    }

    public void fail(String message)
    {
        new LoggerUtility().log.error("Failing with following message " + message);
        Assert.fail(message);
    }

    public List<Throwable> getVerificationFailures()
    {
        List<Throwable> verificationFailures = verificationFailuresMap
                .get(Reporter.getCurrentTestResult());
        return verificationFailures == null ? new ArrayList<Throwable>()
                : verificationFailures;
    }

    private void addVerificationFailure(Throwable e)
    {
        List<Throwable> verificationFailures = getVerificationFailures();
        verificationFailuresMap.put(Reporter.getCurrentTestResult(),
                verificationFailures);
        verificationFailures.add(e);
    }

    public void failTestNgOnVerificationFailures(
            String cErrorMessage, String cErrorScreenShotFileName,
            WebDriver driver)
    {
        /*
         * String cScreenShotPath = "log/screenShots/"; String
         * cErrorScreenShotFileName = cScreenShotPath +
         * "VERIFICATION_FAILURE_"+LoggerUtility.testCaseName +
         * LoggerUtility.logDate + ".png";
         */
        if (LoggerUtility.verificationErrorScreenShotFileNamePath != null)
        {
            LoggerUtility.verificationErrorScreenShotFileNamePath = LoggerUtility.verificationErrorScreenShotFileNamePath
                    + "|" + cErrorScreenShotFileName;
        }
        else
        {
            LoggerUtility.verificationErrorScreenShotFileNamePath = cErrorScreenShotFileName;
        }

        new LoggerUtility(log).captureScreenShot(
                cErrorScreenShotFileName, driver);

        new LoggerUtility().log.error("[VERIFICATION FAILURE]," + cErrorMessage);
        addVerificationFailure(new SeleniumException("[VERIFICATION FAILURE],"
                + cErrorMessage));
    }

}
