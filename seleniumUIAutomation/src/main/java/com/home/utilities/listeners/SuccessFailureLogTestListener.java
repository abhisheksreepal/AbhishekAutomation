package com.home.utilities.listeners;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestNG;

import com.home.utilities.LoggerUtility;
import com.home.utilities.TestDataUtilty;

public class SuccessFailureLogTestListener extends TestNG.ExitCodeListener {
	
	private static Logger log = Logger.getLogger(SuccessFailureLogTestListener.class);
	
	@Override
	public void onTestStart(ITestResult result) {		
		LoggerUtility.testCaseName = result.getMethod().getMethodName();		
		String[] cClassNameArr = result.getTestClass().getName().split("\\.");
		int size = result.getTestClass().getName().split("\\.").length;
		LoggerUtility.cClassName = cClassNameArr[size-1];	
		LoggerUtility.testCaseID = TestDataUtilty.getTestDataValueForGivenTestCase(TestDataUtilty.testCaseToTestDataMap.get(LoggerUtility.cClassName), LoggerUtility.testCaseName, "TestCaseID");
		log.info("##############################                 INITIALIZING TEST CLASS - "+LoggerUtility.cClassName+"                #############################################");
		log.info("-------------------------------STARTING TEST CASE - "+LoggerUtility.testCaseName+" with testCaseID Id -"+LoggerUtility.testCaseID+"-------------------------------");
		super.onTestStart(result);
	}
	
	@Override	
	public void onTestFailure(ITestResult result) {		
		LoggerUtility.tcDetails =  new HashMap<String, String>();
		LoggerUtility.tcDetails.put("Status", "Fail");
		LoggerUtility.tcDetails.put("TestCaseID", LoggerUtility.testCaseID);
		if(LoggerUtility.errorScreenShotFileNamePath == null){
			LoggerUtility.tcDetails.put("ErrorScreenshotName", null);
		}else{
			LoggerUtility.tcDetails.put("ErrorScreenshotName", LoggerUtility.errorScreenShotFileNamePath);
		}	
		if(LoggerUtility.verificationErrorScreenShotFileNamePath == null){
			LoggerUtility.tcDetails.put("VerificationErrorScreenshotName", null);
		}else{
			LoggerUtility.tcDetails.put("VerificationErrorScreenshotName", LoggerUtility.verificationErrorScreenShotFileNamePath);
		}	
		
		LoggerUtility.tcMethodDetails.put(LoggerUtility.testCaseName, LoggerUtility.tcDetails);
		
		super.onTestFailure(result);
	}
	
	
	@Override
	public void onTestSuccess(ITestResult result) {
		LoggerUtility.tcDetails =  new HashMap<String, String>();
		LoggerUtility.tcDetails.put("Status", "Pass");
		LoggerUtility.tcDetails.put("TestCaseID", LoggerUtility.testCaseID);
		LoggerUtility.tcDetails.put("ErrorScreenshotName", null);
		LoggerUtility.tcDetails.put("VerificationErrorScreenshotName", null);
		
		LoggerUtility.tcMethodDetails.put(LoggerUtility.testCaseName, LoggerUtility.tcDetails);
		super.onTestSuccess(result);
	}	
	
	@Override
	public void onTestSkipped(ITestResult result) {
		LoggerUtility.tcDetails =  new HashMap<String, String>();
		LoggerUtility.tcDetails.put("Status", "Skipped");
		LoggerUtility.tcDetails.put("TestCaseID", LoggerUtility.testCaseID);
		if(LoggerUtility.errorScreenShotFileNamePath == null){
			LoggerUtility.tcDetails.put("ErrorScreenshotName", null);
		}else{
			LoggerUtility.tcDetails.put("ErrorScreenshotName", LoggerUtility.errorScreenShotFileNamePath);
		}	
		if(LoggerUtility.verificationErrorScreenShotFileNamePath == null){
			LoggerUtility.tcDetails.put("VerificationErrorScreenshotName", null);
		}else{
			LoggerUtility.tcDetails.put("VerificationErrorScreenshotName", LoggerUtility.verificationErrorScreenShotFileNamePath);
		}	
		
		LoggerUtility.tcMethodDetails.put(LoggerUtility.testCaseName, LoggerUtility.tcDetails);
		super.onTestSkipped(result);
	}
	
	@Override
	public void onConfigurationFailure(ITestResult itr) {
	LoggerUtility.configurationFailure="";	
	LoggerUtility.configurationFailure = LoggerUtility.configurationFailure+"|"+itr.getMethod().toString();
	super.onConfigurationFailure(itr);
	}	
		
	@Override
	public void onFinish(ITestContext context) {		
		LoggerUtility.classDetails.put(LoggerUtility.cClassName , LoggerUtility.tcMethodDetails);
		super.onFinish(context);
		log.info("##############################                 TERMINATING TEST CLASS - "+LoggerUtility.cClassName+"                #############################################");
	}
	
}
