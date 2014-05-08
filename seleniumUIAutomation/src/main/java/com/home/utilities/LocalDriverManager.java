package com.home.utilities;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;



public class LocalDriverManager 
{

    private static Logger log = Logger.getLogger(LocalDriverManager.class);

    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    
    public static WebDriver getDriver() {
        return webDriver.get();
    }
 
    public static void setWebDriver(WebDriver driver) {
        webDriver.set(driver);
    }

}
