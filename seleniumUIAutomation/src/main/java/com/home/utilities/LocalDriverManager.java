package com.home.utilities;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;



public class LocalDriverManager extends LoggerUtility
{


    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    
    private static ThreadLocal<Logger> log = new ThreadLocal<Logger>();
    
    public static Logger getLog()
    {
        return log.get();
    }

    public static void setLog(Logger log)
    {
        LocalDriverManager.log.set(log);
    }

    public static WebDriver getDriver() {
        return webDriver.get();
    }
 
    public static void setWebDriver(WebDriver driver) {
        webDriver.set(driver);
    }

}
