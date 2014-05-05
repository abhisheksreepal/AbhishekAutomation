package com.home.utilities.listeners;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestNG;

import com.home.utilities.LoggerUtility;

public class SuccessFailureLogTestListener extends TestNG.ExitCodeListener
{

    private static Logger log = Logger
            .getLogger(SuccessFailureLogTestListener.class);

    @Override
    public void onTestStart(ITestResult result)
    {
        LoggerUtility.testCaseName = result.getMethod().getMethodName();
        String[] cClassNameArr = result.getTestClass().getName().split("\\.");
        int size = result.getTestClass().getName().split("\\.").length;
        LoggerUtility.cClassName = cClassNameArr[size - 1];

        super.onTestStart(result);
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        LoggerUtility.tcDetails = new HashMap<String, String>();
        LoggerUtility.tcDetails.put("Status", "Fail");

        if (LoggerUtility.errorScreenShotFileNamePath == null)
        {
            LoggerUtility.tcDetails.put("ErrorScreenshotName", null);
        }
        else
        {
            LoggerUtility.tcDetails.put("ErrorScreenshotName",
                    LoggerUtility.errorScreenShotFileNamePath);
        }
        if (LoggerUtility.verificationErrorScreenShotFileNamePath == null)
        {
            LoggerUtility.tcDetails
                    .put("VerificationErrorScreenshotName", null);
        }
        else
        {
            LoggerUtility.tcDetails.put("VerificationErrorScreenshotName",
                    LoggerUtility.verificationErrorScreenShotFileNamePath);
        }

        LoggerUtility.tcMethodDetails.put(LoggerUtility.testCaseName,
                LoggerUtility.tcDetails);

        super.onTestFailure(result);
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        String testStatus = null;
        for (String testCasekeys : LoggerUtility.tcMethodDetails.keySet())
        {
            if (testCasekeys.equals(result.getMethod().getMethodName()))
            {
                testStatus = LoggerUtility.tcMethodDetails.get(testCasekeys)
                        .get("Status");
            }
        }

        if (testStatus != null)
        {
            if (!testStatus.equals("Fail") && !testStatus.equals("Skipped"))
            {
                LoggerUtility.tcDetails = new HashMap<String, String>();
                LoggerUtility.tcDetails.put("Status", "Pass");

                LoggerUtility.tcDetails.put("ErrorScreenshotName", null);
                LoggerUtility.tcDetails.put("VerificationErrorScreenshotName",
                        null);

                LoggerUtility.tcMethodDetails.put(LoggerUtility.testCaseName,
                        LoggerUtility.tcDetails);
            }

        }
        else
        {
            LoggerUtility.tcDetails = new HashMap<String, String>();
            LoggerUtility.tcDetails.put("Status", "Pass");

            LoggerUtility.tcDetails.put("ErrorScreenshotName", null);
            LoggerUtility.tcDetails
                    .put("VerificationErrorScreenshotName", null);

            LoggerUtility.tcMethodDetails.put(LoggerUtility.testCaseName,
                    LoggerUtility.tcDetails);
        }
        super.onTestSuccess(result);
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        LoggerUtility.tcDetails = new HashMap<String, String>();
        LoggerUtility.tcDetails.put("Status", "Skipped");

        if (LoggerUtility.errorScreenShotFileNamePath == null)
        {
            LoggerUtility.tcDetails.put("ErrorScreenshotName", null);
        }
        else
        {
            LoggerUtility.tcDetails.put("ErrorScreenshotName",
                    LoggerUtility.errorScreenShotFileNamePath);
        }
        if (LoggerUtility.verificationErrorScreenShotFileNamePath == null)
        {
            LoggerUtility.tcDetails
                    .put("VerificationErrorScreenshotName", null);
        }
        else
        {
            LoggerUtility.tcDetails.put("VerificationErrorScreenshotName",
                    LoggerUtility.verificationErrorScreenShotFileNamePath);
        }

        LoggerUtility.tcMethodDetails.put(LoggerUtility.testCaseName,
                LoggerUtility.tcDetails);
        super.onTestSkipped(result);
    }

    @Override
    public void onConfigurationFailure(ITestResult itr)
    {
        LoggerUtility.configurationFailure = "";
        LoggerUtility.configurationFailure = LoggerUtility.configurationFailure
                + "|" + itr.getMethod().toString();
        super.onConfigurationFailure(itr);
    }

    @Override
    public void onFinish(ITestContext context)
    {
        LoggerUtility.classDetails.put(LoggerUtility.cClassName,
                LoggerUtility.tcMethodDetails);
        super.onFinish(context);
    }

}
