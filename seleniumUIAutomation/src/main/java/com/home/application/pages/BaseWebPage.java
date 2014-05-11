package com.home.application.pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.home.utilities.LocalDriverManager;
import com.home.utilities.SeleniumException;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

public class BaseWebPage extends com.home.utilities.ObjectRepoUtility
{

    public WebDriver driver;

    private long getCurrentTime()
    {
        Date now = new Date();
        long nowTime = now.getTime();
        logTraceMessage("Returning Current Time -" + nowTime);
        return nowTime;
    }

    public String getDateTime()
    {
        String DATE_FORMAT_NOW = "ddMMyyyyHHmmss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String dateTime = sdf.format(cal.getTime());
        logTraceMessage("Returning Date Time -" + dateTime);
        return dateTime;
    }

    public String randomAlphaNumericCharacters(int length)
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
        logTraceMessage("Returning randomAlphaNumericCharacters -" + output);
        return output;
    }

    public String[] getStringTokenized(String stringToTokenize, String delimiter)
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
    public String getStandardWaitTime()
    {
        String waitTime = envProperties
                .getString("STANDARD_PAGE_LOAD_WAIT_TIME");
        logTraceMessage("Returning standardWaitTime -" + waitTime);
        return waitTime;
    }

    public BaseWebPage(WebDriver driver)
    {
        this.driver = driver;

    }

    public BaseWebPage()
    {
        this.driver = LocalDriverManager.getDriver();
    }

    /**
     * Method:getSeleniumHandle Description:it returns Native Selenium
     * supporting APIs
     * 
     * @return
     */
    private WebDriverBackedSelenium getSeleniumHandle()
    {
        return new WebDriverBackedSelenium(driver, driver.getCurrentUrl());

    }

    /**
     * Method:getPageTitle Description:it returns pagetitle of current page
     * 
     * @return
     */
    public String getPageTitle()
    {
        String title = driver.getTitle();
        logTraceMessage("Returning PageTitle -" + title);
        return title;
    }

    /**
     * Method:goBack Description:it emulates browser back button
     * 
     * @return
     */
    public void goBack()
    {
        getSeleniumHandle().goBack();
        getSeleniumHandle().waitForPageToLoad(getStandardWaitTime());
    }

    /**
     * 
     */
    public void closeBrowser()
    {
        driver.quit();
        if (driver instanceof FirefoxDriver)
        {
            logTraceMessage("Successfully CLOSED Firefox Browser");
        }
        else if (driver instanceof InternetExplorerDriver)
        {
            logTraceMessage("Successfully CLOSED Internet Explorer Browser");
        }
        else
        {
            logTraceMessage("Successfully CLOSED Browser");
        }

    }

    public void navigateTo(String url)
    {
        driver.get(url);
        if (driver instanceof FirefoxDriver)
        {
            logTraceMessage("Successfully NAVIGATED Firefox Browser to -" + url);
        }
        else if (driver instanceof InternetExplorerDriver)
        {
            logTraceMessage("Successfully NAVIGATED Internet Explorer Browser to -"
                    + url);
        }
        else
        {
            logTraceMessage("Successfully NAVIGATED Browser to -" + url);
        }
    }

    /**
     * 
     * @param ObjRepo
     * @param objectName
     * @param waitForTimeout
     * 
     */
    public void clickElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected
        String methodName = s.getMethodName();

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
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .click();
                   
                    logTraceMessage(

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully clicked"
                            + " And Object Xpath = ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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
                  
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully clicked "
                            + " And Object Xpath =["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
           
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .click();
                  
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "]  is successfully clicked" + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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
                 
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully clicked"
                            + " And Object Id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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
    public String getTextForElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully present and text is retrieved - ["
                            + actualText + "]" + " And Object xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully present and text is retrieved - ["
                            + actualText
                            + "]"
                            + " And Object xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully present and text is retrieved - ["
                            + actualText + "]" + " And Object id ["
                            + getObjectValue(objectRepo, objectName) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);

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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully present and text is retrieved - ["
                            + actualText
                            + "]"
                            + " Object id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

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
    public void clearElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .clear();
                    
                    logTraceMessage(

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully cleared"
                            + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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
                  
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully cleared"
                            + " Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .clear();
                   
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully cleared" + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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
                  
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully cleared"
                            + " And Object Id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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
    public void setTextAfterClearingElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            clearElement(objectRepo, objectName, modifyObjectValueInRuntime,
                    noOfOccurancesToBeReplaced, valuesToBeReplaced,
                    errorMessage);
           
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                 
                    logTraceMessage(

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully SET text [" + value + "]"
                            + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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
                   
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully SET text["
                            + value
                            + "]"
                            + " And Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            clearElement(objectRepo, objectName, modifyObjectValueInRuntime,
                    noOfOccurancesToBeReplaced, valuesToBeReplaced,
                    errorMessage);
           
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                  
                    logTraceMessage(

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully SET text[" + value + "]"
                            + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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
                   
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully SET text["
                            + value
                            + "]"
                            + " And Object Id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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
    public void appendTextElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                    
                    
                    logTraceMessage(

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully Appeneded text[" + value + "]"
                            + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]"
                            );
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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
                   
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully Appeneded text["
                            + value
                            + "]"
                            + " And Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                   
                    logTraceMessage(

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully Appended text[" + value + "]"
                            + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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
                    
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully Appended text["
                            + value
                            + "]"
                            + " And Object Id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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
    public void waitForElementVisible(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and is present"
                            + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and timed out and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object name -["
                            + objectName
                            + "] is successfully waited and is present"
                            + " And object value =["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "]is successfully waited and is present"
                            + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and timed out and [MESSAGE]= ["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object name -["
                            + objectName
                            + "] is successfully waited and is present"
                            + " And object value =["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and is present"
                            + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and timed out and [MESSAGE]= ["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object name -["
                            + objectName
                            + "] is successfully waited and is present"
                            + " And object value =["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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
    public void waitForElementNotVisible(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and is invisible"
                            + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object name -["
                            + objectName
                            + "] is successfully waited and is invisible"
                            + " And object value =["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and is invisible"
                            + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object name -["
                            + objectName
                            + "] is successfully waited and is invisible"
                            + " And object value =["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public void waitForTextToBePresentInElementAttribute(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String text, String attributeName,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and text -  " + text
                            + " is present in attribute " + attributeName
                            + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
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
                                            valuesToBeReplaced) + "]", driver);
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and text -  " + text
                            + " is present in attribute " + attributeName
                            + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object name -"
                            + objectName
                            + " is successfully waited and text -  "
                            + text
                            + " is present in attribute "
                            + attributeName
                            + " And object value ="
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public void waitForTextPresentInElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String text, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            WebDriverWait wait = new WebDriverWait(driver,
                    envProperties.getLong("STANDARD_WEBELEMENT_LOAD_WAIT_TIME"));
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    wait.until(ExpectedConditions.textToBePresentInElementLocated(
                            By.xpath(getObjectValue(objectRepo, objectName)),
                            text));
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and text - " + text
                            + "is present" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and timed out and text - "
                            + text + "is NOT present" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);
                }
            }
            else
            {
                try
                {
                    wait.until(ExpectedConditions.textToBePresentInElementLocated(By
                            .xpath(getModifiedObjectValue(objectRepo,
                                    objectName, noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)), text));
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object name -["
                            + objectName
                            + "] is successfully waited and and text - "
                            + text
                            + "is present"
                            + " And object value =["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

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
                    wait.until(ExpectedConditions.textToBePresentInElementLocated(
                            By.id(getObjectValue(objectRepo, objectName)), text));
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and  text - " + text
                            + "is present" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is NOT present and text - " + text
                            + "is NOT present" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);

                }
            }
            else
            {
                try
                {
                    wait.until(ExpectedConditions.textToBePresentInElementLocated(By
                            .id(getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)), text));
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object name -["
                            + objectName
                            + "] is successfully waited and text - "
                            + text
                            + "is present"
                            + " And object value =["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public void waitForTextNOTToBePresentInElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String text, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and text -  " + text
                            + " is now INVISIBLE" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is Timed out and text -  " + text
                            + " is STILL present" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);

                }
                catch (SeleniumException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "]  is NOT present" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
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
                                            valuesToBeReplaced) + "]", driver);

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is successfully waited and text -  " + text
                            + " is invisible" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "] is Timed out and text -  " + text
                            + " is still visible" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);

                }
                catch (SeleniumException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object name -[" + objectName
                            + "]  is NOT present " + " and [MESSAGE]=["
                            + errorMessage + "]" + " And object value =["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object name -["
                            + objectName
                            + "] is successfully waited and text -  "
                            + text
                            + " is invisible"
                            + " And object value =["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (TimeoutException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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
    public boolean isElementDisplayed(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
           
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .isDisplayed();
                    
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "]  is present" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
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
                  
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is present"
                            + " And Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
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
           
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .isDisplayed();
                    
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is present" + "  And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "]");
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
                  
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "]  is present"
                            + " And Object Id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
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
         
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.linkText(getObjectValue(objectRepo, objectName)))
                            .isDisplayed();
                 
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is present" + " And Object linktext ["
                            + getObjectValue(objectRepo, objectName) + "]");
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
                    
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is present"
                            + " And Object linktext ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
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
    public boolean isElementSelected(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

        boolean isSelected = false;
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
          
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    isSelected = driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .isSelected();
                  
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is Selected" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
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
                    
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is Selected"
                            + " And Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
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
           
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    isSelected = driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .isSelected();
                    
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is Selected" + " And Object id ["
                            + getObjectValue(objectRepo, objectName) + "]");
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
                   
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is Selected"
                            + " And Object id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
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
    public boolean isElementEnabled(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

        boolean isEnabled = false;
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
            
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    isEnabled = driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .isEnabled();
                  
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is Enabled" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
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
                   
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is Enabled"
                            + "  And Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
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
           
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    isEnabled = driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .isEnabled();
                   
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is Enabled" + " And Object id ["
                            + getObjectValue(objectRepo, objectName) + "]");
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
                   
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is Enabled"
                            + " And Object id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
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
    public boolean isElementVisible(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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
                        logTraceMessage("[" + methodName + "]," + "Page -["
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

                        "["
                                + methodName
                                + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "],"
                                + "Object Name -["
                                + objectName
                                + "] is visible"
                                + " And Object Xpath ["
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
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
                        logTraceMessage("[" + methodName + "]," + "Page -["
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

                        "["
                                + methodName
                                + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "],"
                                + "Object Name -["
                                + objectName
                                + "] is visible"
                                + " And Object ID ["
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
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
                        logTraceMessage("[" + methodName + "]," + "Page -["
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

                        "["
                                + methodName
                                + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "],"
                                + "Object Name -["
                                + objectName
                                + "] is visible"
                                + " And Object linktext ["
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
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

    public enum selectOptionValues
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
    public void selectOptionElements(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, selectOptionValues option,
            String valueOrVisibleByText, int index,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("ID"))
        {
            switch (option)
            {
            case VALUE:
               
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .id(getObjectValue(objectRepo, objectName))));
                        select.selectByValue(valueOrVisibleByText);
                       
                        logTraceMessage(

                        "[" + methodName + "]," + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] with Option - VALUE =["
                                + valueOrVisibleByText
                                + "] and is successfully Selected"
                                + " And Object ID ["
                                + getObjectValue(objectRepo, objectName) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                        + "]", driver);

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
                        
                        logTraceMessage(

                        "["
                                + methodName
                                + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "],"
                                + "Object Name -["
                                + objectName
                                + "] with Option - VALUE = ["
                                + valueOrVisibleByText
                                + "] is successfully Selected"
                                + " and Object ID ["
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                                valuesToBeReplaced) + "]",
                                driver);

                    }
                }

                break;
            case INDEX:
                
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .id(getObjectValue(objectRepo, objectName))));
                        select.selectByIndex(index);
                       
                        logTraceMessage(

                        "[" + methodName + "]," + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] with Option - Index = [" + index
                                + "] is successfully Selected"
                                + "  and Object ID ["
                                + getObjectValue(objectRepo, objectName) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage("[" + methodName + "]," + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + " ]with Option - Index = [" + index
                                + "]  is NOT Selected," + " and [MESSAGE]=["
                                + errorMessage + "]" + " and Object ID ["
                                + getObjectValue(objectRepo, objectName) + "]",
                                driver);

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
                       
                        logTraceMessage(

                        "["
                                + methodName
                                + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "],"
                                + "Object Name -["
                                + objectName
                                + "] with Option - Index = ["
                                + index
                                + "]  is successfully Selected"
                                + " and Object ID ["
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                                valuesToBeReplaced) + "]",
                                driver);
                    }
                }

                break;
            case VISIBLEBYTEXT:
             
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .id(getObjectValue(objectRepo, objectName))));
                        select.selectByVisibleText(valueOrVisibleByText);
                      
                        logTraceMessage(

                        "[" + methodName + "]," + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] with Option - VisibleByText = ["
                                + valueOrVisibleByText
                                + "] is successfully Selected"
                                + " and Object ID ["
                                + getObjectValue(objectRepo, objectName) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                        + "]", driver);

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
                       
                        logTraceMessage(

                        "["
                                + methodName
                                + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "],"
                                + "Object Name -["
                                + objectName
                                + "] with Option - VisibleByText = ["
                                + valueOrVisibleByText
                                + "] is successfully Selected"
                                + " and Object ID[ "
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                                valuesToBeReplaced) + "]",
                                driver);

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
               
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .xpath(getObjectValue(objectRepo, objectName))));
                        select.selectByValue(valueOrVisibleByText);
                       
                        logTraceMessage(

                        "[" + methodName + "]," + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] with Option - VALUE = ["
                                + valueOrVisibleByText
                                + "] is successfully Selected"
                                + "  and Object Xpath ["
                                + getObjectValue(objectRepo, objectName) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                        + "]", driver);

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
                      
                        logTraceMessage(

                        "["
                                + methodName
                                + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "],"
                                + "Object Name -["
                                + objectName
                                + "] with Option - VALUE = ["
                                + valueOrVisibleByText
                                + "]  is successfully Selected"
                                + " and Object Xpath ["
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                                valuesToBeReplaced) + "]",
                                driver);

                    }
                }

                break;
            case INDEX:
                
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .xpath(getObjectValue(objectRepo, objectName))));
                        select.selectByIndex(index);
                       
                        logTraceMessage(

                        "[" + methodName + "]," + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] with Option - Index = [" + index
                                + "] is successfully Selected"
                                + " and Object Xpath ["
                                + getObjectValue(objectRepo, objectName) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage("[" + methodName + "]," + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] with Option - Index = [" + index
                                + "] is NOT Selected," + " and [MESSAGE]=["
                                + errorMessage + "]" + " and Object Xpath ["
                                + getObjectValue(objectRepo, objectName) + "]",
                                driver);

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
                        
                        logTraceMessage(

                        "["
                                + methodName
                                + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "],"
                                + "Object Name -["
                                + objectName
                                + "] with Option - Index = ["
                                + index
                                + "] is successfully Selected"
                                + " and Object Xpath ["
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                                valuesToBeReplaced) + "]",
                                driver);

                    }
                }

                break;
            case VISIBLEBYTEXT:
               
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        Select select = new Select(driver.findElement(By
                                .xpath(getObjectValue(objectRepo, objectName))));
                        select.selectByVisibleText(valueOrVisibleByText);
                     
                        logTraceMessage(

                        "[" + methodName + "]," + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "]," + "Object Name -[" + objectName
                                + "] with Option - VisibleByText = ["
                                + valueOrVisibleByText
                                + "] is successfully Selected"
                                + " and Object Xpath ["
                                + getObjectValue(objectRepo, objectName) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                        + "]", driver);

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
                      
                        logTraceMessage(

                        "["
                                + methodName
                                + "],"
                                + "Page -["
                                + objectRepo.get("fileDetails").get("fileName")
                                + "],"
                                + "Object Name -["
                                + objectName
                                + "] with Option - VisibleByText = ["
                                + valueOrVisibleByText
                                + "] is successfully Selected"
                                + " and Object Xpath ["
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced) + "]");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                                valuesToBeReplaced) + "]",
                                driver);

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
    public int getChildCountForElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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
                    int count = driver.findElements(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .size();
                 
                    logTraceMessage(

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] has got count = " + count
                            + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
                    return count;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] has NOT  got count = " + " and [MESSAGE]=["
                            + errorMessage + "]" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);

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
                  
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] has got count = "
                            + count
                            + " And Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                    return count;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
          
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    int count = driver.findElements(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .size();
                  
                    logTraceMessage(

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] has got count = " + count
                            + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
                    return count;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] has NOT  got count = " + " and [MESSAGE]=["
                            + errorMessage + "]" + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);

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
                 
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] has got count = "
                            + count
                            + " And Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                    return count;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

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

    public void uploadFile(HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                  
                    logTraceMessage(

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully uploaded"
                            + " AND Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);

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
                 
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully uploaded"
                            + " And Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
           
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                   
                    logTraceMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully uploaded" + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);

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
                  
                    logTraceMessage(

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "]  is successfully uploaded"
                            + " And Object Id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public void mouseOverElement(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully mouse overed,"
                            + " And Object Xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);

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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully mouse overed,"
                            + " And Object Xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully mouse overed,"
                            + " And Object Id ["
                            + getObjectValue(objectRepo, objectName) + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                    + "]", driver);

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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully mouse overed,"
                            + " And Object Id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                }
                catch (SeleniumException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
                }
            }

        }
        else
        {
            throw new RuntimeException("Invalid Object Type -[" + objectType
                    + "]");
        }
    }

    public String getAttributeValue(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String attributeName,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully present and attribute "
                            + attributeName + " is retrieved"
                            + " with value -[" + actualText + "]"
                            + " Object xpath ["
                            + getObjectValue(objectRepo, objectName) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is NOT present and attribute " + attributeName
                            + " is NOT retrieved" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And Object xpath["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);

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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully present and attribute "
                            + attributeName
                            + " is retrieved"
                            + " with value -["
                            + actualText
                            + "]"
                            + " And Object xpath ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);

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

                    "[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is successfully present and attribute "
                            + attributeName + " is retrieved"
                            + " with value -[" + actualText + "]"
                            + " And Object id ["
                            + getObjectValue(objectRepo, objectName) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("[" + methodName + "]," + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "]," + "Object Name -[" + objectName
                            + "] is NOT present and attribute " + attributeName
                            + " is NOT retrieved" + " and [MESSAGE]=["
                            + errorMessage + "]" + " And Object id["
                            + getObjectValue(objectRepo, objectName) + "]",
                            driver);
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

                    "["
                            + methodName
                            + "],"
                            + "Page -["
                            + objectRepo.get("fileDetails").get("fileName")
                            + "],"
                            + "Object Name -["
                            + objectName
                            + "] is successfully present and attribute "
                            + attributeName
                            + " is retrieved"
                            + " with value -["
                            + actualText
                            + "]"
                            + " And Object id ["
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced) + "]");
                    return actualText;

                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced) + "]", driver);
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

    public void switchToFrame(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            boolean switchToDefaultContent, String errorMessage)
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stacktrace[2];// maybe this number needs to be
                                            // corrected

        String methodName = s.getMethodName();

        if (!switchToDefaultContent)
        {
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
                        driver.switchTo().frame(
                                driver.findElement(By.xpath(getObjectValue(
                                        objectRepo, objectName))));
                        
                        logTraceMessage(

                        "Page -"
                                + objectRepo.get("fileDetails").get("fileName")
                                + " " + "[" + methodName + "],"
                                + "Object Name -" + objectName
                                + " whose Xpath "
                                + getObjectValue(objectRepo, objectName)
                                + " is successfully switched");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

                        "Page -"
                                + objectRepo.get("fileDetails").get("fileName")
                                + " " + "[" + methodName + "],"
                                + "Object Name -" + objectName
                                + " is not found with Xpath -"
                                + getObjectValue(objectRepo, objectName),
                                driver);
                     
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

                       
                        logTraceMessage(

                        "Page -"
                                + objectRepo.get("fileDetails").get("fileName")
                                + " "
                                + "["
                                + methodName
                                + "],"
                                + "Object Name -"
                                + objectName
                                + " whose Xpath "
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced)
                                + " is successfully switched");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                                valuesToBeReplaced), driver);
                      
                    }
                }

            }
            else if (objectType.equalsIgnoreCase("ID"))
            {
               
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        driver.switchTo().frame(
                                driver.findElement(By.id(getObjectValue(
                                        objectRepo, objectName))));
                       
                        logTraceMessage(

                        "Page -"
                                + objectRepo.get("fileDetails").get("fileName")
                                + " " + "[" + methodName + "],"
                                + "Object Name -" + objectName + " whose Id "
                                + getObjectValue(objectRepo, objectName)
                                + " is successfully switched");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                        + getObjectValue(objectRepo, objectName),
                                driver);
                      
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
                      
                        logTraceMessage(

                        "Page -"
                                + objectRepo.get("fileDetails").get("fileName")
                                + " "
                                + "["
                                + methodName
                                + "],"
                                + "Object Name -"
                                + objectName
                                + " whose Id "
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced)
                                + " is successfully clicked");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                                valuesToBeReplaced), driver);
                 
                    }
                }

            }
            else if (objectType.equalsIgnoreCase("NAME"))
            {
               
                if (!modifyObjectValueInRuntime)
                {
                    try
                    {
                        driver.switchTo().frame(
                                driver.findElement(By.name(getObjectValue(
                                        objectRepo, objectName))));
                        
                        logTraceMessage(

                        "Page -"
                                + objectRepo.get("fileDetails").get("fileName")
                                + " " + "[" + methodName + "],"
                                + "Object Name -" + objectName + " whose Name "
                                + getObjectValue(objectRepo, objectName)
                                + " is successfully switched");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                        + getObjectValue(objectRepo, objectName),
                                driver);
                     
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
                      
                        logTraceMessage(

                        "Page -"
                                + objectRepo.get("fileDetails").get("fileName")
                                + " "
                                + "["
                                + methodName
                                + "],"
                                + "Object Name -"
                                + objectName
                                + " whose Name "
                                + getModifiedObjectValue(objectRepo,
                                        objectName, noOfOccurancesToBeReplaced,
                                        valuesToBeReplaced)
                                + " is successfully clicked");
                    }
                    catch (NoSuchElementException e)
                    {
                        logErrorMessage(

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
                                                valuesToBeReplaced), driver);
                      
                    }
                }

            }
            else
            {
                logErrorMessage("Invalid Object Type -" + objectType, driver);

            }
        }
        else
        {
            driver.switchTo().defaultContent();
        }

    }

    public String getFirstSelectedOption(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
        waitForElementVisible(objectRepo, objectName,
                modifyObjectValueInRuntime, noOfOccurancesToBeReplaced,
                valuesToBeReplaced, errorMessage);
        String objectType = getObjectType(objectRepo, objectName);
        String firstSelected = null;

        if (objectType.equalsIgnoreCase("ID"))
        {
            
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    Select select = new Select(driver.findElement(By
                            .id(getObjectValue(objectRepo, objectName))));
                    firstSelected = select.getFirstSelectedOption().getText();
                   
                    logTraceMessage("Select list Value " + firstSelected
                            + " is successfully retrieved");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("Select list Value " + firstSelected
                            + " is NOT retrieved,", driver);
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
                    
                    logTraceMessage("Select list Value " + firstSelected
                            + " is successfully retrieved");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("Select list Value " + firstSelected
                            + " is NOT retrieved,", driver);

                }
            }

        }
        else if (objectType.equalsIgnoreCase("XPATH"))
        {
            
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    Select select = new Select(driver.findElement(By
                            .xpath(getObjectValue(objectRepo, objectName))));
                    firstSelected = select.getFirstSelectedOption().getText();
                   
                    logTraceMessage("Select list Value " + firstSelected
                            + " is successfully retrieved");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("Select list Value " + firstSelected
                            + " is NOT retrieved,", driver);
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
                   
                    logTraceMessage("Select list Value " + firstSelected
                            + " is successfully retrieved");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("Select list Value " + firstSelected
                            + " is NOT retrieved,", driver);

                }
            }
        }
        else if (objectType.equalsIgnoreCase("NAME"))
        {
          
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    Select select = new Select(driver.findElement(By
                            .name(getObjectValue(objectRepo, objectName))));
                    firstSelected = select.getFirstSelectedOption().getText();
                   
                    logTraceMessage("Select list Value " + firstSelected
                            + " is successfully retrieved");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("Select list Value " + firstSelected
                            + " is NOT retrieved,", driver);
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
                  
                    logTraceMessage("Select list Value " + firstSelected
                            + " is successfully retrieved");
                    return firstSelected;
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage("Select list Value " + firstSelected
                            + " is NOT retrieved,", driver);

                }
            }

        }
        else
        {
            logErrorMessage("Invalid Object Type -" + objectType, driver);

        }
        return firstSelected;
    }

    public void uploadFile(HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, String value,
            boolean modifyObjectValueInRuntime, int noOfOccurancesToBeReplaced,
            String valuesToBeReplaced)
    {
        String objectType = getObjectType(objectRepo, objectName);
        if (objectType.equalsIgnoreCase("XPATH"))
        {
          
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.xpath(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                  
                    logTraceMessage(

                    "Page -" + objectRepo.get("fileDetails").get("fileName")
                            + " " + "Object Name -" + objectName
                            + " whose Xpath "
                            + getObjectValue(objectRepo, objectName)
                            + " is successfully uploaded");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with Xpath -"
                                    + getObjectValue(objectRepo, objectName),
                            driver);
                   
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
                  
                    logTraceMessage(

                    "Page -"
                            + objectRepo.get("fileDetails").get("fileName")
                            + " "
                            + "Object Name -"
                            + objectName
                            + " whose Xpath "
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)
                            + " is successfully uploaded");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced), driver);
                 
                }
            }

        }
        else if (objectType.equalsIgnoreCase("ID"))
        {
            
            if (!modifyObjectValueInRuntime)
            {
                try
                {
                    driver.findElement(
                            By.id(getObjectValue(objectRepo, objectName)))
                            .sendKeys(value);
                   
                    logTraceMessage(

                    "Page -" + objectRepo.get("fileDetails").get("fileName")
                            + " " + "Object Name -" + objectName + " whose Id "
                            + getObjectValue(objectRepo, objectName)
                            + " is successfully uploaded");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with id -"
                                    + getObjectValue(objectRepo, objectName),
                            driver);
                  
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
                
                    logTraceMessage(

                    "Page -"
                            + objectRepo.get("fileDetails").get("fileName")
                            + " "
                            + "Object Name -"
                            + objectName
                            + " whose Id "
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)
                            + " is successfully uploaded");
                }
                catch (NoSuchElementException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced), driver);
                 
                }
            }

        }
        else
        {
            logErrorMessage("Invalid Object Type -" + objectType, driver);

        }
    }

    public void mouseOverElement(
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

                    "Page -" + objectRepo.get("fileDetails").get("fileName")
                            + " " + "Object Name -" + objectName
                            + " whose Xpath "
                            + getObjectValue(objectRepo, objectName)
                            + " is successfully mouse overed,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(

                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with Xpath -"
                                    + getObjectValue(objectRepo, objectName),
                            driver);
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

                    "Page -"
                            + objectRepo.get("fileDetails").get("fileName")
                            + " "
                            + "Object Name -"
                            + objectName
                            + " whose Xpath "
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)
                            + " is successfully mouse overed,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced), driver);
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

                    "Page -" + objectRepo.get("fileDetails").get("fileName")
                            + " " + "Object Name -" + objectName + " whose Id "
                            + getObjectValue(objectRepo, objectName)
                            + " is successfully mouse overed,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(

                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with id -"
                                    + getObjectValue(objectRepo, objectName),
                            driver);
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

                    "Page -"
                            + objectRepo.get("fileDetails").get("fileName")
                            + " "
                            + "Object Name -"
                            + objectName
                            + " whose Id "
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)
                            + " is successfully mouse overed,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced), driver);
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }

        }
        else
        {
            logErrorMessage("Invalid Object Type -" + objectType, driver);

        }
    }

    public void mouseRightClick(
            HashMap<String, HashMap<String, String>> objectRepo,
            String objectName, boolean modifyObjectValueInRuntime,
            int noOfOccurancesToBeReplaced, String valuesToBeReplaced,
            String errorMessage)
    {
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
                    builder.contextClick(webelement).perform();
                    logTraceMessage(

                    "Page -" + objectRepo.get("fileDetails").get("fileName")
                            + " " + "Object Name -" + objectName
                            + " whose Xpath "
                            + getObjectValue(objectRepo, objectName)
                            + " is successfully right clicked,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(

                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with Xpath -"
                                    + getObjectValue(objectRepo, objectName),
                            driver);
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

                    "Page -"
                            + objectRepo.get("fileDetails").get("fileName")
                            + " "
                            + "Object Name -"
                            + objectName
                            + " whose Xpath "
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)
                            + " is successfully rigth clicked,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced), driver);
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

                    "Page -" + objectRepo.get("fileDetails").get("fileName")
                            + " " + "Object Name -" + objectName + " whose Id "
                            + getObjectValue(objectRepo, objectName)
                            + " is successfully right clicked,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(

                            "Page -"
                                    + objectRepo.get("fileDetails").get(
                                            "fileName") + " " + "Object Name -"
                                    + objectName + " is not found with id -"
                                    + getObjectValue(objectRepo, objectName),
                            driver);
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

                    "Page -"
                            + objectRepo.get("fileDetails").get("fileName")
                            + " "
                            + "Object Name -"
                            + objectName
                            + " whose Id "
                            + getModifiedObjectValue(objectRepo, objectName,
                                    noOfOccurancesToBeReplaced,
                                    valuesToBeReplaced)
                            + " is successfully right clicked,");
                }
                catch (RuntimeException e)
                {
                    logErrorMessage(

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
                                            valuesToBeReplaced), driver);
                    // throw new RuntimeException("mouse over an element is not
                    // successful");
                }
            }

        }
        else
        {
            logErrorMessage("Invalid Object Type -" + objectType, driver);

        }
    }

}