package com.home.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

public class LoggerUtility
{

    private static String logDate;

    public static String cClassName;
    public static String testCaseName;

    public static String errorScreenShotFileNamePath = null;
    public static String verificationErrorScreenShotFileNamePath = null;
    public static String configurationFailure = null;

    public static HashMap<String, String> tcDetails = new HashMap<String, String>();
    public static HashMap<String, HashMap<String, String>> tcMethodDetails = new HashMap<String, HashMap<String, String>>();
    public static HashMap<String, HashMap<String, HashMap<String, String>>> classDetails = new HashMap<String, HashMap<String, HashMap<String, String>>>();

    public  Logger log;

    public LoggerUtility(Logger log)
    {
        
        this.log = log;
    }
    
    // Disable selenium logs, Making level as "warn"
    static
    {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty(
                "org.apache.commons.logging.simplelog.log.org.apache.http",
                "warn");
    }
    
    public LoggerUtility()
    {
        //TODO ad logger object to threadLocal
        this.log = LocalDriverManager.getLog();
        
        if(this.log==null){
            this.log = Logger.getLogger("Base Log");
        }
    }


    private static final String DATEFOLDER = (new Date()).toString()
            .replace(":", "_").replace(" ", "_");

    public static PropertiesConfiguration envProperties;

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

    public void createFolders()
    {
        File file = new File("log/logs/" + DATEFOLDER);
        boolean s = file.mkdir();

        if (s)
        {
            File file2 = new File("log/logs/" + DATEFOLDER + "/"
                    + "screenShots");
            file2.mkdir();

            File infoFilePath = new File("log/logs/" + DATEFOLDER + "/"
                    + "infoLogs");
            infoFilePath.mkdir();

            File detailedFilePath = new File("log/logs/" + DATEFOLDER + "/"
                    + "detailedLogs");
            detailedFilePath.mkdir();
        }
        else
        {
            new LoggerUtility().log.error("Not able to create Log folders");

        }
    }

    public void captureScreenShot(String fileName, WebDriver driver)
    {
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        File scrFile = ((TakesScreenshot) augmentedDriver)
                .getScreenshotAs(OutputType.FILE);
        try
        {
            FileUtils.copyFile(scrFile, new File(fileName));
        }
        catch (IOException e)
        {
            new LoggerUtility().log.error("Error writing screencapture to a file -" + fileName);
            new LoggerUtility().log.error(e.getStackTrace());
        }
    }

    private String getTestSuiteName()
    {
        String testSuitePath = envProperties.getString("testNgXMLLocation");
        File testngFile = new File(testSuitePath);
        Builder builder = new Builder();
        nu.xom.Document doc = null;
        Element element = null;
        String suiteName = null;
        try
        {
            doc = builder.build(testngFile);
        }
        catch (ValidityException e)
        {
            new LoggerUtility().log.error(e.getStackTrace());
            throw new RuntimeException(e.getMessage());
        }
        catch (ParsingException e)
        {
            new LoggerUtility().log.error(e.getStackTrace());
            throw new RuntimeException(e.getMessage());
        }
        catch (IOException e)
        {
            new LoggerUtility().log.error(e.getStackTrace());
            throw new RuntimeException(e.getMessage());
        }
        element = doc.getRootElement();
        if (element.getLocalName().equalsIgnoreCase("suite"))
        {
            suiteName = element.getAttributeValue("name");
            new LoggerUtility().log.debug("Returning Test Suite name -" + suiteName);
            return suiteName;
        }
        else
        {
            Elements elements = element.getChildElements();
            for (int i = 0; i < elements.size();)
            {
                if (elements.get(i).getLocalName().equalsIgnoreCase("suite"))
                {
                    suiteName = element.getAttributeValue("name");
                    new LoggerUtility().log.debug("Returning Test Suite name -" + suiteName);
                    return suiteName;
                }
                else
                {
                    new LoggerUtility().log.error("Suite tag not found in testng xml file");
                    throw new RuntimeException(
                            "Suite tag not found in testng xml file");
                }
            }
            new LoggerUtility().log.error("Suite tag not found in testng xml file");
            throw new RuntimeException("Suite tag not found in testng xml file");
        }
    }

    public void updateLog4JXmlFile(String className)
    {
        String logXMLPath = envProperties.getString("log4jXmlLocation");
        String testngXMLSuiteName = getTestSuiteName();
        String logPath = envProperties.getString("logPath");

        logDate = (new Date()).toString().replace(":", "_").replace(" ", "_");

        Builder builder = new Builder();
        nu.xom.Document doc = null;
        Element element = null;

        try
        {
            doc = builder.build(logXMLPath);
        }
        catch (ValidityException e)
        {
            new LoggerUtility().log.error(e.getStackTrace());
            throw new RuntimeException(e.getMessage());
        }
        catch (ParsingException e)
        {
            new LoggerUtility().log.error(e.getStackTrace());
            throw new RuntimeException(e.getMessage());
        }
        catch (IOException e)
        {
            new LoggerUtility().log.error(e.getStackTrace());
            throw new RuntimeException(e.getMessage());
        }
        element = doc.getRootElement();
        Elements elements = element.getChildElements();
        for (int i = 0; i < elements.size(); i++)
        {
            if (elements.get(i).getLocalName().equalsIgnoreCase("appender"))
            {
                if (elements.get(i).getAttributeValue("name")
                        .equalsIgnoreCase("infoFileLog"))
                {
                    Elements grandChild = elements.get(i).getChildElements();
                    for (int j = 0; j < grandChild.size(); j++)
                    {
                        if (grandChild.get(j).getLocalName()
                                .equalsIgnoreCase("param"))
                        {
                            if (grandChild.get(j).getAttributeValue("name")
                                    .equalsIgnoreCase("File"))
                            {
                                Attribute attr = new Attribute("value", logPath
                                        + "/" + DATEFOLDER + "/" + "infoLogs/"
                                        + className + "_" + testngXMLSuiteName
                                        + "_" + logDate + ".csv");
                                grandChild.get(j).addAttribute(attr);
                                new LoggerUtility().log.debug("Updated infoLogPath to value - "
                                        + logPath + "/" + DATEFOLDER + "/"
                                        + "infoLogs/" + className + "_"
                                        + testngXMLSuiteName + "_" + logDate
                                        + ".csv");
                            }
                        }
                    }
                }
                else
                {
                    Elements grandChild = elements.get(i).getChildElements();
                    for (int j = 0; j < grandChild.size(); j++)
                    {
                        if (grandChild.get(j).getLocalName()
                                .equalsIgnoreCase("param"))
                        {
                            if (grandChild.get(j).getAttributeValue("name")
                                    .equalsIgnoreCase("File"))
                            {
                                Attribute attr = new Attribute("value", logPath
                                        + "/" + DATEFOLDER + "/"
                                        + "detailedLogs/" + className + "_"
                                        + testngXMLSuiteName + "_" + logDate
                                        + ".csv");
                                grandChild.get(j).addAttribute(attr);
                                new LoggerUtility().log.debug("Updated infoLogPath to value - "
                                        + logPath + "/" + DATEFOLDER + "/"
                                        + "detailedLogs/" + className + "_"
                                        + testngXMLSuiteName + "_" + logDate
                                        + ".csv");
                            }
                        }
                    }
                }
            }
        }
        File file = new File(logXMLPath);
        FileOutputStream fop = null;
        try
        {
            fop = new FileOutputStream(file);
        }
        catch (FileNotFoundException e)
        {
            new LoggerUtility().log.error("File not found -" + logXMLPath);
            throw new RuntimeException("File not found -" + logXMLPath);
        }

        Serializer serializer = new Serializer(fop);
        try
        {
            serializer.write(doc);
        }
        catch (IOException e)
        {
            new LoggerUtility().log.error("IO exception - unable to write " + logXMLPath);
            throw new RuntimeException("IO exception - unable to write"
                    + logXMLPath);
        }
        // Tell log4j to read log4j xml file again and write new logs to new
        // file defined
        DOMConfigurator.configureAndWatch(logXMLPath, 6000);
    }

    public  void logTraceMessage(String cTraceMessage)
    {
        new LoggerUtility().log.info(cTraceMessage);

    }

    public  void logVerifyPass( String cTraceMessage)
    {
        new LoggerUtility().log.info("[VERIFICATION PASS]" + cTraceMessage);

    }

    public void logErrorMessage(String cErrorMessage,
            WebDriver driver)
    {

        String cScreenShotPath = envProperties.getString("logPath") + "/"
                + DATEFOLDER + "/screenShots/";
        String cErrorScreenShotFileName = cScreenShotPath + "/"
                + "ERROR_FAILURE_" + testCaseName + logDate + ".png";
        errorScreenShotFileNamePath = cErrorScreenShotFileName;
        captureScreenShot(cErrorScreenShotFileName, driver);
        new LoggerUtility().log.error(cErrorMessage);

        throw new SeleniumException(cErrorMessage);
    }

    // Verifies , capture screenshot and does not throw runtime exception
    public void logVerifyFailure( String cErrorMessage,
            WebDriver driver)
    {
        String cScreenShotPath = envProperties.getString("logPath") + "/"
                + DATEFOLDER + "/screenShots/";
        String cErrorScreenShotFileName = cScreenShotPath + "/"
                + "ERROR_FAILURE_" + testCaseName + logDate + ".png";
        new TestBase(new LoggerUtility().log).failTestNgOnVerificationFailures( cErrorMessage,
                cErrorScreenShotFileName, driver);
    }
    
        

}
