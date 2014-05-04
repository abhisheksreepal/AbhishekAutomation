package com.home.application.pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.home.utilities.LoggerUtility;
import com.home.utilities.SeleniumException;

public class BaseWebPage extends com.home.utilities.ObjectRepoUtility
{

    private static Logger log = Logger.getLogger(BaseWebPage.class);

    private static long getCurrentTime()
    {
        Date now = new Date();
        long nowTime = now.getTime();
        LoggerUtility
                .logTraceMessage(log, "Returning Current Time -" + nowTime);
        return nowTime;
    }

    public static String getDateTime()
    {
        String DATE_FORMAT_NOW = "ddMMyyyyHHmmss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String dateTime = sdf.format(cal.getTime());
        LoggerUtility.logTraceMessage(log, "Returning Date Time -" + dateTime);
        return dateTime;
    }

    public static String randomAlphaNumericCharacters(int length)
    {
        if (length <= 0)
        {
            length = 4;
        }
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++)
        {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        LoggerUtility.logTraceMessage(log,
                "Returning randomAlphaNumericCharacters -" + output);
        return output;
    }

    public static String[] getStringTokenized(String stringToTokenize,
            String delimiter)
    {

        StringTokenizer st = new StringTokenizer(stringToTokenize, delimiter);
        int numberOfTokens = st.countTokens();
        int position = 0;
        String[] tokenArray = new String[numberOfTokens];
        while (st.hasMoreTokens())
        {
            String content = st.nextToken();
            tokenArray[position] = content;
            position++;
        }
        return tokenArray;

    }

    /**
     * 
     * @return
     */
    public static String getStandardWaitTime()
    {
        String waitTime = envProperties
                .getString("STANDARD_PAGE_LOAD_WAIT_TIME");
        LoggerUtility.logTraceMessage(log, "Returning standardWaitTime -"
                + waitTime);
        return waitTime;
    }

    public BaseWebPage(WebDriver driver)
    {
        super(driver);

    }

    /**
     * Method:getSeleniumHandle Description:it returns Native Selenium
     * supporting APIs
     * 
     * @return
     */
    private static WebDriverBackedSelenium getSeleniumHandle()
    {
        return new WebDriverBackedSelenium(driver, driver.getCurrentUrl());

    }

    /**
     * Method:getPageTitle Description:it returns pagetitle of current page
     * 
     * @return
     */
    public static String getPageTitle()
    {
        String title = driver.getTitle();
        LoggerUtility.logTraceMessage(log, "Returning PageTitle -"
                + title);
        return title;
    }

    /**
     * Method:goBack Description:it emulates browser back button
     * 
     * @return
     */
    public static void goBack()
    {
        getSeleniumHandle().goBack();
        getSeleniumHandle().waitForPageToLoad(getStandardWaitTime());
    }

    /**
     * 
     */
    public static void closeBrowser()
    {
        driver.quit();
        if(driver instanceof FirefoxDriver){
            LoggerUtility.logTraceMessage(log, "Successfully CLOSED Firefox Browser");
        }else if(driver instanceof InternetExplorerDriver){
            LoggerUtility.logTraceMessage(log, "Successfully CLOSED Internet Explorer Browser");
        }else{
            LoggerUtility.logTraceMessage(log, "Successfully CLOSED Browser");
        }
        
        
    }

    public static void navigateTo(String url)
    {
        driver.get(url);
        if(driver instanceof FirefoxDriver){
            LoggerUtility.logTraceMessage(log, "Successfully NAVIGATED Firefox Browser to -"+url);
        }else if(driver instanceof InternetExplorerDriver){
            LoggerUtility.logTraceMessage(log, "Successfully NAVIGATED Internet Explorer Browser to -"+url);
        }else{
            LoggerUtility.logTraceMessage(log, "Successfully NAVIGATED Browser to -"+url);
        }
    }

    /**
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static void clickElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .click();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] is successfully clicked"
                                    + " And Object Xpath = ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]," + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found AND [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And Object Xpath =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).click();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully clicked "
                                    + " And Object Xpath =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object Xpath -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .click();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "]  is successfully clicked" + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage + "]" + " And Object Id ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).click();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully clicked"
                                    + " And Object Id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " with id -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    /**
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static String getTextForElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            String actualText = null;
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    actualText = driver
                            .findElement(
                                    ByXPath.xpath(getObjectValue(objectRepo,
                                            objectName))).getText();
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully present and text is retrieved - ["+actualText+"]"
                                    + " And Object xpath ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is NOT present and text is NOT retrieved and [MESSAGE]=["
                                    + errorMessage + "]" + " And Object xpath["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    actualText = driver.findElement(
                            ByXPath.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).getText();
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully present and text is retrieved - ["+actualText+"]"
                                    + " And Object xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is NOT present and text is NOT retrieved and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            String actualText = null;
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    actualText = driver.findElement(
                            ByXPath.id(getObjectValue(objectRepo, objectName)))
                            .getText();
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully present and text is retrieved - ["+actualText+"]"
                                    + " And Object id ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is NOT present and text is NOT retrieved and [MESSSAGE]=["
                                    + errorMessage + "]" + " And Object id["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");

                }
            }
            else
            {
                try
                {
                    actualText = driver.findElement(
                            ByXPath.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).getText();
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully present and text is retrieved - ["+actualText+"]"
                                    + " Object id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is NOT present and text is NOT retrieved and [MESSAGE]= ["
                                    + errorMessage
                                    + "]"
                                    + " And Object id["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
        return null;
    }

    /**
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static void clearElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .clear();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] is successfully cleared"
                                    + " And Object Xpath ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]," + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And Object Xpath -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).clear();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully cleared"
                                    + " Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object Xpath -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .clear();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully cleared" + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage + "]" + " And Object id -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).clear();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully cleared"
                                    + " And Object Id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object id -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    /**
     * Clears Text and then Sets Text
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static void setTextAfterClearingElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            clearElement(objectRepo, objectName, modifyObjectValueInRuntime,
                    noOfOccurancesToBeReplaced, valuesToBeReplaced,
                    errorMessage);
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] is successfully SET text ["+ value+"]"
                                    + " And Object Xpath ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]," + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And Object Xpath -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully SET text["+ value+"]"
                                    + " And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object Xpath -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            clearElement(objectRepo, objectName, modifyObjectValueInRuntime,
                    noOfOccurancesToBeReplaced, valuesToBeReplaced,
                    errorMessage);
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully SET text["+ value+"]" + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found and [MESSAGE]="
                                    + errorMessage + "]" + " And Object id -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully SET text["+ value+"]"
                                    + " And Object Id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object id -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    /**
     * Appends Text
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static void appendTextElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] is successfully Appeneded text["+ value+"]"
                                    + " And Object Xpath ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]," + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And Object Xpath -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully Appeneded text["+ value+"]"
                                    + " And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found  AND [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object Xpath -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] is successfully Appended text["+ value+"]"
                                    + " And Object Id ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]," + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found AND [MESSAGE]=["
                                    + errorMessage + "]" + " And Object id -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully Appended text["+ value+"]"
                                    + " And Object Id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found AND [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object id -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    /**
     * Appends Text
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static void waitForElementVisible(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By
                            .xpath(getObjectValue(objectRepo, objectName))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object name -[" + objectName
                                    + "] is successfully waited and is present"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and timed out and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
            }
            else
            {
                try
                {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By
                            .xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and is present"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is NOT present and timed out and [MESSAGE] = ["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By
                            .id(getObjectValue(objectRepo, objectName))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object name -[" + objectName
                                    + "]is successfully waited and is present"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and timed out and [MESSAGE]= ["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
            }
            else
            {
                try
                {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By
                            .id(getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and is present"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is NOT present and timed out and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("LINKTEXT"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By
                            .linkText(getObjectValue(objectRepo, objectName))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object name -[" + objectName
                                    + "] is successfully waited and is present"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and timed out and [MESSAGE]= ["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
            }
            else
            {
                try
                {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By
                            .linkText(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and is present"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is NOT present and timed out and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    /**
     * Appends Text
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static void waitForElementNotVisible(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    wait.until(ExpectedConditions
                            .invisibilityOfElementLocated(By
                                    .xpath(getObjectValue(objectRepo,
                                            objectName))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and is invisible"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is still visible and timed out and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    wait.until(ExpectedConditions
                            .invisibilityOfElementLocated(By
                                    .xpath(getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and is invisible"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is still visible and timed out AND [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    wait.until(ExpectedConditions
                            .invisibilityOfElementLocated(By.id(getObjectValue(
                                    objectRepo, objectName))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and is invisible"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is still visible and timed out AND [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    wait.until(ExpectedConditions
                            .invisibilityOfElementLocated(By
                                    .id(getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced))));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and is invisible"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is still visible and timed out AND [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public static void waitForTextToBePresentInElementAttribute(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String text, String attributeName,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    final HashMap<String, HashMap<String, String>> finalObjectRepo = objectRepo;
                    final String finalObjName = objectName;
                    final String finalText = text;
                    final String finalAttributeName = attributeName;
                    final boolean finalModifyObjectValueInRuntime = modifyObjectValueInRuntime;
                    final int finalNoOfOccurancesToBeReplaced = noOfOccurancesToBeReplaced;
                    final String finalValuesToBeReplaced = valuesToBeReplaced;
                    final String finalErrorMessage = errorMessage;
                    wait.until(new ExpectedCondition<Boolean>()
                    {

                        public Boolean apply(WebDriver arg0)
                        {
                            String elementText = getAttributeValue(
                                    finalObjectRepo, finalObjName,
                                    finalAttributeName,
                                    finalModifyObjectValueInRuntime,
                                    finalNoOfOccurancesToBeReplaced,
                                    finalValuesToBeReplaced, finalErrorMessage);
                            if (elementText != null)
                            {
                                return elementText.contains(finalText);
                            }
                            else
                            {
                                return false;
                            }
                        }
                    });
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object name -[" + objectName
                                    + "] is successfully waited and text -  "
                                    + text + " is present in attribute "
                                    + attributeName + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object name -[" + objectName
                                    + "] is Timed out and text -  " + text
                                    + " is NOT present in attribute "
                                    + attributeName + " and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is NOT present OR attribute is NOT present - "
                                    + attributeName + " and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    final HashMap<String, HashMap<String, String>> finalObjectRepo = objectRepo;
                    final String finalObjName = objectName;
                    final String finalText = text;
                    final String finalAttributeName = attributeName;
                    final boolean finalModifyObjectValueInRuntime = modifyObjectValueInRuntime;
                    final int finalNoOfOccurancesToBeReplaced = noOfOccurancesToBeReplaced;
                    final String finalValuesToBeReplaced = valuesToBeReplaced;
                    final String finalErrorMessage = errorMessage;
                    wait.until(new ExpectedCondition<Boolean>()
                    {
                        public Boolean apply(WebDriver arg0)
                        {
                            String elementText = getAttributeValue(
                                    finalObjectRepo, finalObjName,
                                    finalAttributeName,
                                    finalModifyObjectValueInRuntime,
                                    finalNoOfOccurancesToBeReplaced,
                                    finalValuesToBeReplaced, finalErrorMessage);

                            if (elementText != null)
                            {
                                return elementText.contains(finalText);
                            }
                            else
                            {
                                return false;
                            }
                        }
                    });
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and text -  "
                                    + text
                                    + " is present in attribute "
                                    + attributeName
                                    + " And object value =["
                                    + getModifiedObjectValue(finalObjectRepo,
                                            finalObjName,
                                            finalNoOfOccurancesToBeReplaced,
                                            finalValuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is Timed out and text -  "
                                    + text
                                    + " is NOT present in attribute "
                                    + attributeName
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is NOT present OR attribute is NOT present - "
                                    + attributeName
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    final HashMap<String, HashMap<String, String>> finalObjectRepo = objectRepo;
                    final String finalObjName = objectName;
                    final String finalText = text;
                    final String finalAttributeName = attributeName;
                    final boolean finalModifyObjectValueInRuntime = modifyObjectValueInRuntime;
                    final int finalNoOfOccurancesToBeReplaced = noOfOccurancesToBeReplaced;
                    final String finalValuesToBeReplaced = valuesToBeReplaced;
                    final String finalErrorMessage = errorMessage;
                    wait.until(new ExpectedCondition<Boolean>()
                    {
                        public Boolean apply(WebDriver arg0)
                        {
                            String elementText = getAttributeValue(
                                    finalObjectRepo, finalObjName,
                                    finalAttributeName,
                                    finalModifyObjectValueInRuntime,
                                    finalNoOfOccurancesToBeReplaced,
                                    finalValuesToBeReplaced, finalErrorMessage);
                            if (elementText != null)
                            {
                                return elementText.contains(finalText);
                            }
                            else
                            {
                                return false;
                            }
                        }
                    });
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object name -[" + objectName
                                    + "] is successfully waited and text -  "
                                    + text + " is present in attribute "
                                    + attributeName + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object name -[" + objectName
                                    + "] is Timed out and text -  " + text
                                    + " is NOT present in attribute "
                                    + attributeName + " and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is NOT present OR attribute is NOT present - "
                                    + attributeName + " and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And object value =["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
            }
            else
            {
                try
                {
                    final HashMap<String, HashMap<String, String>> finalObjectRepo = objectRepo;
                    final String finalObjName = objectName;
                    final String finalText = text;
                    final String finalAttributeName = attributeName;
                    final int finalNoOfOccurancesToBeReplaced = noOfOccurancesToBeReplaced;
                    final String finalValuesToBeReplaced = valuesToBeReplaced;
                    final String finalErrorMessage = errorMessage;
                    final boolean finalModifyObjectValueInRuntime = modifyObjectValueInRuntime;
                    wait.until(new ExpectedCondition<Boolean>()
                    {
                        public Boolean apply(WebDriver arg0)
                        {
                            String elementText = getAttributeValue(
                                    finalObjectRepo, finalObjName,
                                    finalAttributeName,
                                    finalModifyObjectValueInRuntime,
                                    finalNoOfOccurancesToBeReplaced,
                                    finalValuesToBeReplaced, finalErrorMessage);
                            if (elementText != null)
                            {
                                return elementText.contains(finalText);
                            }
                            else
                            {
                                return false;
                            }
                        }
                    });
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -"
                                    + objectName
                                    + " is successfully waited and text -  "
                                    + text
                                    + " is present in attribute "
                                    + attributeName
                                    + " And object value ="
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name [-"
                                    + objectName
                                    + "] is Timed out and text -  "
                                    + text
                                    + " is NOT present in attribute "
                                    + attributeName
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "]  is NOT present OR attribute is NOT present - "
                                    + attributeName
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public static void waitForTextPresentInElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String text, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    wait.until(ExpectedConditions.textToBePresentInElement(
                            By.xpath(getObjectValue(objectRepo, objectName)),
                            text));
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and text - " + text
                            + "is present" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and timed out and text - "
                            + text + "is NOT present" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
            }
            else
            {
                try
                {
                    wait.until(ExpectedConditions.textToBePresentInElement(By
                            .xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)), text));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and and text - "
                                    + text
                                    + "is present"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is NOT present aand text - "
                                    + text
                                    + "is NOT present"
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    wait.until(ExpectedConditions.textToBePresentInElement(
                            By.id(getObjectValue(objectRepo, objectName)), text));
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and  text - " + text
                            + "is present" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and text - " + text
                            + "is NOT present" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");

                }
            }
            else
            {
                try
                {
                    wait.until(ExpectedConditions.textToBePresentInElement(By
                            .id(getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)), text));
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and text - "
                                    + text
                                    + "is present"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is NOT present and text - "
                                    + text
                                    + " is NOT present"
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public static void waitForTextNOTToBePresentInElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String text, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    final HashMap<String, HashMap<String, String>> finalObjectRepo = objectRepo;
                    final String finalObjName = objectName;
                    final String finalText = text;
                    final boolean finalModifyObjectValueInRuntime = modifyObjectValueInRuntime;
                    final int finalNoOfOccurancesToBeReplaced = noOfOccurancesToBeReplaced;
                    final String finalValuesToBeReplaced = valuesToBeReplaced;
                    final String finalErrorMessage = errorMessage;
                    wait.until(new ExpectedCondition<Boolean>()
                    {
                        public Boolean apply(WebDriver arg0)
                        {
                            String elementText = getTextForElement(
                                    finalObjectRepo, finalObjName,
                                    finalModifyObjectValueInRuntime,
                                    finalNoOfOccurancesToBeReplaced,
                                    finalValuesToBeReplaced, finalErrorMessage);
                            if (elementText != null)
                            {
                                return !elementText.contains(finalText);
                            }
                            else
                            {
                                return false;
                            }
                        }
                    });
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and text -  " + text
                            + " is now INVISIBLE" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is Timed out and text -  " + text
                            + " is STILL present" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "]  is NOT present" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
            }
            else
            {
                try
                {
                    final HashMap<String, HashMap<String, String>> finalObjectRepo = objectRepo;
                    final String finalObjName = objectName;
                    final String finalText = text;
                    final boolean finalModifyObjectValueInRuntime = modifyObjectValueInRuntime;
                    final int finalNoOfOccurancesToBeReplaced = noOfOccurancesToBeReplaced;
                    final String finalValuesToBeReplaced = valuesToBeReplaced;
                    final String finalErrorMessage = errorMessage;
                    wait.until(new ExpectedCondition<Boolean>()
                    {
                        public Boolean apply(WebDriver arg0)
                        {
                            String elementText = getTextForElement(
                                    finalObjectRepo, finalObjName,
                                    finalModifyObjectValueInRuntime,
                                    finalNoOfOccurancesToBeReplaced,
                                    finalValuesToBeReplaced, finalErrorMessage);
                            if (elementText != null)
                            {
                                return !elementText.contains(finalText);
                            }
                            else
                            {
                                return false;
                            }
                        }
                    });
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and text -  "
                                    + text
                                    + " is invisible"
                                    + " And object value =["
                                    + getModifiedObjectValue(finalObjectRepo,
                                            finalObjName,
                                            finalNoOfOccurancesToBeReplaced,
                                            finalValuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is Timed out and text -  "
                                    + text
                                    + " is still visible"
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + "  And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "]  is NOT present"
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    final HashMap<String, HashMap<String, String>> finalObjectRepo = objectRepo;
                    final String finalObjName = objectName;
                    final String finalText = text;

                    final boolean finalModifyObjectValueInRuntime = modifyObjectValueInRuntime;
                    final int finalNoOfOccurancesToBeReplaced = noOfOccurancesToBeReplaced;
                    final String finalValuesToBeReplaced = valuesToBeReplaced;
                    final String finalErrormessage = errorMessage;
                    wait.until(new ExpectedCondition<Boolean>()
                    {
                        public Boolean apply(WebDriver arg0)
                        {
                            String elementText = getTextForElement(
                                    finalObjectRepo, finalObjName,
                                    finalModifyObjectValueInRuntime,
                                    finalNoOfOccurancesToBeReplaced,
                                    finalValuesToBeReplaced, finalErrormessage);
                            if (elementText != null)
                            {
                                return !elementText.contains(finalText);
                            }
                            else
                            {
                                return false;
                            }
                        }
                    });
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and text -  " + text
                            + " is invisible" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is Timed out and text -  " + text
                            + " is still visible" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "]  is NOT present " + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
            }
            else
            {
                try
                {
                    final HashMap<String, HashMap<String, String>> finalObjectRepo = objectRepo;
                    final String finalObjName = objectName;
                    final String finalText = text;

                    final int finalNoOfOccurancesToBeReplaced = noOfOccurancesToBeReplaced;
                    final String finalValuesToBeReplaced = valuesToBeReplaced;
                    final boolean finalModifyObjectValueInRuntime = modifyObjectValueInRuntime;
                    final String finalErrormessage = errorMessage;
                    wait.until(new ExpectedCondition<Boolean>()
                    {
                        public Boolean apply(WebDriver arg0)
                        {
                            String elementText = getTextForElement(
                                    finalObjectRepo, finalObjName,
                                    finalModifyObjectValueInRuntime,
                                    finalNoOfOccurancesToBeReplaced,
                                    finalValuesToBeReplaced, finalErrormessage);
                            if (elementText != null)
                            {
                                return !elementText.contains(finalText);
                            }
                            else
                            {
                                return false;
                            }
                        }
                    });
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is successfully waited and text -  "
                                    + text
                                    + " is invisible"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is Timed out and text -  "
                                    + text
                                    + " is still visible"
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object name -["
                                    + objectName
                                    + "] is NOT present"
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And object value =["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    /**
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static boolean isElementDisplayed(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .isDisplayed();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "]  is present" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                    return true;
                }
                catch (NoSuchElementException e)
                {
                    return false;
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).isDisplayed();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is present"
                                    + " And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]"
                                    + timeDiff + "msecs");
                    return true;
                }
                catch (NoSuchElementException e)
                {

                    return false;
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .isDisplayed();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is present" + "  And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                    return true;
                }
                catch (NoSuchElementException e)
                {

                    return false;
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).isDisplayed();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "]  is present"
                                    + " And Object Id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                    return true;
                }
                catch (NoSuchElementException e)
                {

                    return false;
                }
            }

        }
        else if (objectType.equalsIgnoreCase("LINKTEXT"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.linkText(getObjectValue(objectRepo, objectName)))
                            .isDisplayed();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is present" + " And Object linktext ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                    return true;
                }
                catch (NoSuchElementException e)
                {
                    return false;
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.linkText(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).isDisplayed();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is present"
                                    + " And Object linktext ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                    return true;
                }
                catch (NoSuchElementException e)
                {

                    return false;
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");

        }

    }

    /**
     * Appends Text
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static boolean isElementSelected(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        boolean isSelected = false;
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    isSelected = driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .isSelected();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is Selected" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                    return isSelected;
                }
                catch (NoSuchElementException e)
                {

                    return isSelected;
                }
            }
            else
            {
                try
                {
                    isSelected = driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).isSelected();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is Selected"
                                    + " And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                    return isSelected;
                }
                catch (NoSuchElementException e)
                {

                    return isSelected;
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    isSelected = driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .isSelected();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is Selected" + " And Object id ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                    return isSelected;
                }
                catch (NoSuchElementException e)
                {

                    return isSelected;
                }
            }
            else
            {
                try
                {
                    isSelected = driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).isSelected();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is Selected"
                                    + " And Object id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                    return isSelected;
                }
                catch (NoSuchElementException e)
                {

                    return isSelected;
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    /**
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static boolean isElementEnabled(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        boolean isEnabled = false;
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    isEnabled = driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .isEnabled();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is Enabled" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                    return isEnabled;
                }
                catch (NoSuchElementException e)
                {

                    return isEnabled;
                }
            }
            else
            {
                try
                {
                    isEnabled = driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).isEnabled();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is Enabled"
                                    + "  And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                    return isEnabled;
                }
                catch (NoSuchElementException e)
                {

                    return isEnabled;
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    isEnabled = driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .isEnabled();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is Enabled" + " And Object id ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                    return isEnabled;
                }
                catch (NoSuchElementException e)
                {

                    return isEnabled;
                }
            }
            else
            {
                try
                {
                    isEnabled = driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).isEnabled();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is Enabled"
                                    + " And Object id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                    return isEnabled;
                }
                catch (NoSuchElementException e)
                {

                    return isEnabled;
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    /**
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    @Deprecated()
    public static boolean isElementVisible(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {

            if (!modifyObjectValueInRuntime)
            {
                if (isElementDisplayed(objectRepo, objectName,
                        modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                        valuesToBeReplaced))
                {
                    if (getSeleniumHandle().isVisible(
                            getObjectValue(objectRepo, objectName)))
                    {
                        logTraceMessage(log, "[" + methodName + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] is visible" + " And Object Xpath ["
                                + getObjectValue(objectRepo, objectName) + "]");
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if (isElementDisplayed(objectRepo, objectName,
                        modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                        valuesToBeReplaced))
                {
                    if (getSeleniumHandle().isVisible(
                            getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)))
                    {
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] is visible"
                                        + " And Object Xpath ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]");
                        return true;
                    }
                    else
                    {

                        return false;
                    }
                }
                else
                {
                    return false;
                }

            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {

            if (!modifyObjectValueInRuntime)
            {
                if (isElementDisplayed(objectRepo, objectName,
                        modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                        valuesToBeReplaced))
                {
                    if (getSeleniumHandle().isVisible(
                            getObjectValue(objectRepo, objectName)))
                    {
                        logTraceMessage(log, "[" + methodName + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] is visible" + " And Object ID ["
                                + getObjectValue(objectRepo, objectName) + "]");
                        return true;
                    }
                    else
                    {

                        return false;
                    }
                }
                else
                {
                    return false;
                }

            }
            else
            {
                if (isElementDisplayed(objectRepo, objectName,
                        modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                        valuesToBeReplaced))
                {
                    if (getSeleniumHandle().isVisible(
                            getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)))
                    {
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] is visible"
                                        + " And Object ID ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]");
                        return true;
                    }
                    else
                    {

                        return false;
                    }
                }
                else
                {
                    return false;
                }

            }

        }
        else if (objectType.equalsIgnoreCase("LINKTEXT"))
        {

            if (!modifyObjectValueInRuntime)
            {
                if (isElementDisplayed(objectRepo, objectName,
                        modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                        valuesToBeReplaced))
                {
                    if (getSeleniumHandle().isVisible(
                            getObjectValue(objectRepo, objectName)))
                    {
                        logTraceMessage(log, "[" + methodName + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] is visible" + " And Object linktext["
                                + getObjectValue(objectRepo, objectName) + "]");
                        return true;
                    }
                    else
                    {

                        return false;
                    }
                }
                else
                {
                    return false;
                }

            }
            else
            {
                if (isElementDisplayed(objectRepo, objectName,
                        modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                        valuesToBeReplaced))
                {
                    if (getSeleniumHandle().isVisible(
                            getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)))
                    {
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] is visible"
                                        + " And Object linktext ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]");
                        return true;
                    }
                    else
                    {

                        return false;
                    }
                }
                else
                {
                    return false;
                }

            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public static enum selectOptionValues
    {

        VALUE, INDEX, VISIBLEBYTEXT;
    }

    /**
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static void selectOptionElements(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, selectOptionValues option,
            String valueOrVisibleByText, int index,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("ID"))
        {
            switch (option)
            {
            case VALUE:
                long startTime = getCurrentTime();
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .id(getObjectValue(objectRepo, objectName))));
                        select.selectByValue(valueOrVisibleByText);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VALUE =["
                                        + valueOrVisibleByText
                                        + "] and is successfully Selected"
                                        + " And Object ID ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]," + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VALUE = ["
                                        + valueOrVisibleByText
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object ID ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]");

                    }
                }
                else
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .id(getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced))));
                        select.selectByValue(valueOrVisibleByText);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VALUE = ["
                                        + valueOrVisibleByText
                                        + "] is successfully Selected"
                                        + " and Object ID ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "],"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VALUE = ["
                                        + valueOrVisibleByText
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object ID ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]");

                    }
                }

                break;
            case INDEX:
                long startTime2 = getCurrentTime();
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .id(getObjectValue(objectRepo, objectName))));
                        select.selectByIndex(index);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime2;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - Index = ["
                                        + index
                                        + "] is successfully Selected"
                                        + "  and Object ID ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]," + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(log, "[" + methodName + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + " ]with Option - Index = [" + index
                                + "]  is NOT Selected," + " and [MESSAGE]=["
                                + errorMessage + "]" + " and Object ID ["
                                + getObjectValue(objectRepo, objectName) + "]");

                    }
                }
                else
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .id(getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced))));
                        select.selectByIndex(index);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime2;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - Index = ["
                                        + index
                                        + "]  is successfully Selected"
                                        + " and Object ID ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "],"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - Index = ["
                                        + index
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object ID ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]");
                    }
                }

                break;
            case VISIBLEBYTEXT:
                long startTime3 = getCurrentTime();
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .id(getObjectValue(objectRepo, objectName))));
                        select.selectByVisibleText(valueOrVisibleByText);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime3;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VisibleByText = ["
                                        + valueOrVisibleByText
                                        + "] is successfully Selected"
                                        + " and Object ID ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]," + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VisibleByText = ["
                                        + valueOrVisibleByText
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object ID ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]");

                    }
                }
                else
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .id(getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced))));
                        select.selectByVisibleText(valueOrVisibleByText);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime3;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VisibleByText = ["
                                        + valueOrVisibleByText
                                        + "] is successfully Selected"
                                        + " and Object ID[ "
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "],"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VisibleByText = ["
                                        + valueOrVisibleByText
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object ID ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]");

                    }
                }

                break;

            }

        }
        else if (objectType.equalsIgnoreCase("XPATH"))
        {
            switch (option)
            {
            case VALUE:
                long startTime = getCurrentTime();
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .xpath(getObjectValue(objectRepo, objectName))));
                        select.selectByValue(valueOrVisibleByText);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VALUE = ["
                                        + valueOrVisibleByText
                                        + "] is successfully Selected"
                                        + "  and Object Xpath ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]," + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VALUE = ["
                                        + valueOrVisibleByText
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object Xpath ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]");

                    }
                }
                else
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .xpath(getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced))));
                        select.selectByValue(valueOrVisibleByText);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VALUE = ["
                                        + valueOrVisibleByText
                                        + "]  is successfully Selected"
                                        + " and Object Xpath ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "],"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "]with Option - VALUE = ["
                                        + valueOrVisibleByText
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object Xpath ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]");

                    }
                }

                break;
            case INDEX:
                long startTime2 = getCurrentTime();
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .xpath(getObjectValue(objectRepo, objectName))));
                        select.selectByIndex(index);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime2;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - Index = ["
                                        + index
                                        + "] is successfully Selected"
                                        + " and Object Xpath ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]," + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(log, "[" + methodName + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] with Option - Index = [" + index
                                + "] is NOT Selected," + " and [MESSAGE]=["
                                + errorMessage + "]" + " and Object Xpath ["
                                + getObjectValue(objectRepo, objectName) + "]");

                    }
                }
                else
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .xpath(getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced))));
                        select.selectByIndex(index);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime2;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - Index = ["
                                        + index
                                        + "] is successfully Selected"
                                        + " and Object Xpath ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "],"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - Index = ["
                                        + index
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object Xpath["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]");

                    }
                }

                break;
            case VISIBLEBYTEXT:
                long startTime3 = getCurrentTime();
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .xpath(getObjectValue(objectRepo, objectName))));
                        select.selectByVisibleText(valueOrVisibleByText);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime3;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VisibleByText = ["
                                        + valueOrVisibleByText
                                        + "] is successfully Selected"
                                        + " and Object Xpath ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]," + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VisibleByText = ["
                                        + valueOrVisibleByText
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object Xpath ["
                                        + getObjectValue(objectRepo, objectName)
                                        + "]");

                    }
                }
                else
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .xpath(getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced))));
                        select.selectByVisibleText(valueOrVisibleByText);
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime3;
                        logTraceMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VisibleByText = ["
                                        + valueOrVisibleByText
                                        + "] is successfully Selected"
                                        + " and Object Xpath ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "["
                                        + methodName
                                        + "],"
                                        + "Page -["
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + "],"
                                        + "Object Name -["
                                        + objectName
                                        + "] with Option - VisibleByText = ["
                                        + valueOrVisibleByText
                                        + "] is NOT Selected,"
                                        + " and [MESSAGE]=["
                                        + errorMessage
                                        + "]"
                                        + " and Object Xpath ["
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced) + "]");

                    }
                }

                break;

            }
        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");

        }
    }

    /**
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public static int getChildCountForElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    int count = driver.findElements(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .size();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] has got count = " + count
                                    + " And Object Xpath ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]," + timeDiff + "msecs");
                    return count;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] has NOT  got count = " + " and [MESSAGE]=["
                            + errorMessage + "]" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");

                }
            }
            else
            {
                try
                {
                    int count = driver.findElements(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).size();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] has got count = "
                                    + count
                                    + " And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                    return count;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] has NOT  got count = "
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    int count = driver.findElements(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .size();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] has got count = " + count
                                    + " And Object Xpath ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]," + timeDiff + "msecs");
                    return count;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] has NOT  got count = " + " and [MESSAGE]=["
                            + errorMessage + "]" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");

                }
            }
            else
            {
                try
                {
                    int count = driver.findElements(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).size();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] has got count = "
                                    + count
                                    + " And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                    return count;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] has NOT  got count = "
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + "  And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
        return 0;
    }

    public static void uploadFile(HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] is successfully uploaded"
                                    + " AND Object Xpath ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]," + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "]is not found and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And Object Xpath -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");

                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully uploaded"
                                    + " And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object Xpath -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully uploaded" + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "],"
                            + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage + "]" + " And Object id -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");

                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "]  is successfully uploaded"
                                    + " And Object Id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "],"
                                    + timeDiff + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found  and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object id -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public static void mouseOverElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .xpath(getObjectValue(objectRepo, objectName)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.moveToElement(webelement).perform();
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] is successfully mouse overed,"
                                    + " And Object Xpath ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage + "]"
                                    + " And Object Xpath -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");

                }
            }
            else
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.moveToElement(webelement).perform();
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully mouse overed,"
                                    + " And Object Xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object Xpath -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .id(getObjectValue(objectRepo, objectName)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.moveToElement(webelement).perform();
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "Object Name -[" + objectName
                                    + "] is successfully mouse overed,"
                                    + " And Object Id ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + "],"
                                    + "ObjectName -[" + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage + "]" + " And Object id -["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");

                }
            }
            else
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .id(getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.moveToElement(webelement).perform();
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully mouse overed,"
                                    + " And Object Id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "ObjectName -["
                                    + objectName
                                    + "] is not found and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object Id -["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public static String getAttributeValue(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String attributeName,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            String actualText = null;
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    actualText = driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .getAttribute(attributeName);
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully present and attribute "
                                    + attributeName + " is retrieved"+" with value -["+actualText+"]"
                                    + " Object xpath ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is NOT present and attribute " + attributeName
                            + " is NOT retrieved" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And Object xpath["
                            + getObjectValue(objectRepo, objectName) + "]");

                }
            }
            else
            {
                try
                {
                    actualText = driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).getAttribute(
                            attributeName);
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully present and attribute "
                                    + attributeName
                                    + " is retrieved"+" with value -["+actualText+"]"
                                    + " And Object xpath ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "]is NOT present and text is NOT retrieved"
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + "  And Object xpath["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");

                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            String actualText = null;
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    actualText = driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .getAttribute(attributeName);
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully present and attribute "
                                    + attributeName + " is retrieved"+" with value -["+actualText+"]"
                                    + " And Object id ["
                                    + getObjectValue(objectRepo, objectName)
                                    + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is NOT present and attribute " + attributeName
                            + " is NOT retrieved" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And Object id["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
            }
            else
            {
                try
                {
                    actualText = driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).getAttribute(
                            attributeName);
                    logTraceMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is successfully present and attribute "
                                    + attributeName
                                    + " is retrieved"+" with value -["+actualText+"]"
                                    + " And Object id ["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "["
                                    + methodName
                                    + "],"
                                    + "Page -["
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + "],"
                                    + "Object Name -["
                                    + objectName
                                    + "] is NOT present and text is NOT retrieved"
                                    + " and [MESSAGE]=["
                                    + errorMessage
                                    + "]"
                                    + " And Object id["
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced) + "]");
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
        return null;

    }

    public static void switchToFrame(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            boolean switchToDefaultContent, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String className = s.getClassName();
        String methodName = s.getMethodName();
        Logger log = Logger.getLogger(className);

        if (!switchToDefaultContent)
        {
            waitForElementVisible(objectRepo, objectName,
                    modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                    valuesToBeReplaced, errorMessage);
            String objectType = getObjectType(objectRepo, objectName);

            if (objectType.equalsIgnoreCase("XPATH"))
            {
                long startTime = getCurrentTime();
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        driver.switchTo().frame(
                                driver.findElement(By.xpath(getObjectValue(
                                        objectRepo, objectName))));
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " whose Xpath "
                                        + getObjectValue(objectRepo, objectName)
                                        + " is successfully switched,"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " is not found with Xpath -"
                                        + getObjectValue(objectRepo, objectName));
                        // throw new
                        // NoSuchElementException("Page
                        // -"+objectRepo.get("fileDetails").get("fileName")+"
                        // "+"Object Name -"
                        // +
                        // objectName+ " is not found with Xpath
                        // -"+getObjectValue(objectRepo, objectName));
                    }
                }
                else
                {
                    try
                    {
                        driver.switchTo().frame(
                                driver.findElement(By
                                        .xpath(getModifiedObjectValue(
                                                objectRepo, objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced))));

                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " whose Xpath "
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced)
                                        + " is successfully switched,"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " is not found with Xpath -"
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced));
                        // throw new
                        // NoSuchElementException("Page
                        // -"+objectRepo.get("fileDetails").get("fileName")+"
                        // "+"Object Name -"
                        // +
                        // objectName+ " is not found with Xpath
                        // -"+getModifiedObjectValue(objectRepo,
                        // objectName,noOfOccurancesToBeReplaced,valuesToBeReplaced));
                    }
                }

            }
            else if (objectType.equalsIgnoreCase("ID"))
            {
                long startTime = getCurrentTime();
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        driver.switchTo().frame(
                                driver.findElement(By.id(getObjectValue(
                                        objectRepo, objectName))));
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " whose Id "
                                        + getObjectValue(objectRepo, objectName)
                                        + " is successfully switched,"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " is not found with id -"
                                        + getObjectValue(objectRepo, objectName));
                        // throw new
                        // NoSuchElementException("Page
                        // -"+objectRepo.get("fileDetails").get("fileName")+"
                        // "+"Object Name -"
                        // +
                        // objectName+ " is not found with id
                        // -"+getObjectValue(objectRepo, objectName));
                    }
                }
                else
                {
                    try
                    {
                        driver.switchTo().frame(
                                driver.findElement(By
                                        .id(getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced))));
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " whose Id "
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced)
                                        + " is successfully clicked,"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " is not found with id -"
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced));
                        // throw new
                        // NoSuchElementException("Page
                        // -"+objectRepo.get("fileDetails").get("fileName")+"
                        // "+"Object Name -"
                        // +
                        // objectName+ " is not found with id
                        // -"+getModifiedObjectValue(objectRepo,
                        // objectName,noOfOccurancesToBeReplaced,valuesToBeReplaced));
                    }
                }

            }
            else if (objectType.equalsIgnoreCase("NAME"))
            {
                long startTime = getCurrentTime();
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        driver.switchTo().frame(
                                driver.findElement(By.name(getObjectValue(
                                        objectRepo, objectName))));
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " whose Name "
                                        + getObjectValue(objectRepo, objectName)
                                        + " is successfully switched,"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " is not found with Name -"
                                        + getObjectValue(objectRepo, objectName));
                        // throw new
                        // NoSuchElementException("Page
                        // -"+objectRepo.get("fileDetails").get("fileName")+"
                        // "+"Object Name -"
                        // +
                        // objectName+ " is not found with id
                        // -"+getObjectValue(objectRepo, objectName));
                    }
                }
                else
                {
                    try
                    {
                        driver.switchTo().frame(
                                driver.findElement(By
                                        .name(getModifiedObjectValue(
                                                objectRepo, objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced))));
                        long endTime = getCurrentTime();
                        long timeDiff = endTime - startTime;
                        logTraceMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " whose Name "
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced)
                                        + " is successfully clicked,"
                                        + timeDiff + "msecs");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(
                                log,
                                "Page -"
                                        + objectRepo.get("fileDetails").get(
                                                "fileName")
                                        + " "
                                        + "["
                                        + methodName
                                        + "],"
                                        + "Object Name -"
                                        + objectName
                                        + " is not found with Name -"
                                        + getModifiedObjectValue(objectRepo,
                                                objectName,
                                                noOfOccurancesToBeReplaced,
                                                valuesToBeReplaced));
                        // throw new
                        // NoSuchElementException("Page
                        // -"+objectRepo.get("fileDetails").get("fileName")+"
                        // "+"Object Name -"
                        // +
                        // objectName+ " is not found with id
                        // -"+getModifiedObjectValue(objectRepo,
                        // objectName,noOfOccurancesToBeReplaced,valuesToBeReplaced));
                    }
                }

            }
            else
            {
                logErrorMessage(log, "Invalid Object Type -" + objectType);

            }
        }
        else
        {
            driver.switchTo().defaultContent();
        }

    }
    
    public static String getFirstSelectedOption(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,String errorMessage)
    {
        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced,errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        String firstSelected = null;

        if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    Select select = new Select(driver.findElement(By
                            .id(getObjectValue(objectRepo, objectName))));
                    firstSelected = select.getFirstSelectedOption().getText();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "Select list Value " + firstSelected
                            + " is successfully retrieved," + timeDiff
                            + "msecs");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "Select list Value " + firstSelected
                            + " is NOT retrieved,");
                }
            }
            else
            {
                try
                {
                    Select select = new Select(driver.findElement(By
                            .id(getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))));
                    firstSelected = select.getFirstSelectedOption().getText();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "Select list Value " + firstSelected
                            + " is successfully retrieved," + timeDiff
                            + "msecs");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "Select list Value " + firstSelected
                            + " is NOT retrieved,");

                }
            }

        }
        else if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    Select select = new Select(driver.findElement(By
                            .xpath(getObjectValue(objectRepo, objectName))));
                    firstSelected = select.getFirstSelectedOption().getText();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "Select list Value " + firstSelected
                            + " is successfully retrieved," + timeDiff
                            + "msecs");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "Select list Value " + firstSelected
                            + " is NOT retrieved,");
                }
            }
            else
            {
                try
                {
                    Select select = new Select(driver.findElement(By
                            .xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))));
                    firstSelected = select.getFirstSelectedOption().getText();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "Select list Value " + firstSelected
                            + " is successfully retrieved," + timeDiff
                            + "msecs");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "Select list Value " + firstSelected
                            + " is NOT retrieved,");

                }
            }
        }
        else if (objectType.equalsIgnoreCase("NAME"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    Select select = new Select(driver.findElement(By
                            .name(getObjectValue(objectRepo, objectName))));
                    firstSelected = select.getFirstSelectedOption().getText();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "Select list Value " + firstSelected
                            + " is successfully retrieved," + timeDiff
                            + "msecs");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "Select list Value " + firstSelected
                            + " is NOT retrieved,");
                }
            }
            else
            {
                try
                {
                    Select select = new Select(driver.findElement(By
                            .name(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))));
                    firstSelected = select.getFirstSelectedOption().getText();
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(log, "Select list Value " + firstSelected
                            + " is successfully retrieved," + timeDiff
                            + "msecs");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(log, "Select list Value " + firstSelected
                            + " is NOT retrieved,");

                }
            }

        }
        else
        {
            logErrorMessage(log, "Invalid Object Type -" + objectType);

        }
        return firstSelected;
    }
    
    

    public static void uploadFile(HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced)
    {
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " whose Xpath "
                                    + getObjectValue(objectRepo, objectName)
                                    + " is successfully uploaded," + timeDiff
                                    + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with Xpath -"
                                    + getObjectValue(objectRepo, objectName));
                    // throw new
                    // NoSuchElementException("Page
                    // -"+objectRepo.get("fileDetails").get("fileName")+"
                    // "+"Object Name -"
                    // +
                    // objectName+ " is not found with Xpath
                    // -"+getObjectValue(objectRepo, objectName));
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " whose Xpath "
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced)
                                    + " is successfully uploaded," + timeDiff
                                    + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " is not found with Xpath -"
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced));
                    // throw new
                    // NoSuchElementException("Page
                    // -"+objectRepo.get("fileDetails").get("fileName")+"
                    // "+"Object Name -"
                    // +
                    // objectName+ " is not found with Xpath
                    // -"+getModifiedObjectValue(objectRepo,
                    // objectName,noOfOccurancesToBeReplaced,valuesToBeReplaced));
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            long startTime = getCurrentTime();
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " whose Id "
                                    + getObjectValue(objectRepo, objectName)
                                    + " is successfully uploaded," + timeDiff
                                    + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with id -"
                                    + getObjectValue(objectRepo, objectName));
                    // throw new
                    // NoSuchElementException("Page
                    // -"+objectRepo.get("fileDetails").get("fileName")+"
                    // "+"Object Name -"
                    // +
                    // objectName+ " is not found with id
                    // -"+getObjectValue(objectRepo, objectName));
                }
            }
            else
            {
                try
                {
                    driver.findElement(
                            By.id(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced))).sendKeys(value);
                    long endTime = getCurrentTime();
                    long timeDiff = endTime - startTime;
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " whose Id "
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced)
                                    + " is successfully uploaded," + timeDiff
                                    + "msecs");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " is not found with id -"
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced));
                    // throw new
                    // NoSuchElementException("Page
                    // -"+objectRepo.get("fileDetails").get("fileName")+"
                    // "+"Object Name -"
                    // +
                    // objectName+ " is not found with id
                    // -"+getModifiedObjectValue(objectRepo,
                    // objectName,noOfOccurancesToBeReplaced,valuesToBeReplaced));
                }
            }

        }
        else
        {
            logErrorMessage(log, "Invalid Object Type -" + objectType);

        }
    }

    public static void mouseOverElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced)
    {

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .xpath(getObjectValue(objectRepo, objectName)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.moveToElement(webelement).perform();
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " whose Xpath "
                                    + getObjectValue(objectRepo, objectName)
                                    + " is successfully mouse overed,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with Xpath -"
                                    + getObjectValue(objectRepo, objectName));
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }
            else
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.moveToElement(webelement).perform();
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " whose Xpath "
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced)
                                    + " is successfully mouse overed,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " is not found with Xpath -"
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced));
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .id(getObjectValue(objectRepo, objectName)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.moveToElement(webelement).perform();
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " whose Id "
                                    + getObjectValue(objectRepo, objectName)
                                    + " is successfully mouse overed,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with id -"
                                    + getObjectValue(objectRepo, objectName));
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }
            else
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .id(getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.moveToElement(webelement).perform();
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " whose Id "
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced)
                                    + " is successfully mouse overed,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " is not found with Id -"
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced));
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }

        }
        else
        {
            logErrorMessage(log, "Invalid Object Type -" + objectType);

        }
    }

    

    public static void mouseRightClick(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,String errorMessage)
    {
        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced,errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .xpath(getObjectValue(objectRepo, objectName)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.contextClick(webelement).perform();
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " whose Xpath "
                                    + getObjectValue(objectRepo, objectName)
                                    + " is successfully right clicked,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with Xpath -"
                                    + getObjectValue(objectRepo, objectName));
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }
            else
            {
                try
                {

                    WebElement webelement = driver.findElement(By
                            .xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.contextClick(webelement).build().perform();
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " whose Xpath "
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced)
                                    + " is successfully rigth clicked,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " is not found with Xpath -"
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced));
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .id(getObjectValue(objectRepo, objectName)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.contextClick(webelement).perform();
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " whose Id "
                                    + getObjectValue(objectRepo, objectName)
                                    + " is successfully right clicked,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with id -"
                                    + getObjectValue(objectRepo, objectName));
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }
            else
            {
                try
                {
                    WebElement webelement = driver.findElement(By
                            .id(getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)));
                    // build and perform the mouseOver with Advanced User
                    // Interactions API
                    Actions builder = new Actions(driver);
                    builder.contextClick(webelement).perform();
                    logTraceMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " whose Id "
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced)
                                    + " is successfully right clicked,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(
                            log,
                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName")
                                    + " "
                                    + "Object Name -"
                                    + objectName
                                    + " is not found with Id -"
                                    + getModifiedObjectValue(objectRepo,
                                            objectName,
                                            noOfOccurancesToBeReplaced,
                                            valuesToBeReplaced));
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }

        }
        else
        {
            logErrorMessage(log, "Invalid Object Type -" + objectType);

        }
    }

}