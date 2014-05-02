package com.home.application.adactin.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.home.application.pages.BaseWebPage;
import com.home.utilities.SeleniumException;

/**
 * @author abhishek.sreepal
 *
 */
public class LoginPage extends BaseWebPage {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(LoginPage.class);
	
	
	public LoginPage(WebDriver driver) 
    {
        super(driver);                    
    }
	
	  /**
     * Forces to defult URL in env file if app is logged in or not
     */
 
	public LoginPage launchApplicationToDefaultUrl() {		
		if(!isLoggedOut()){
			new HomePage(driver).logOffFromApp();			
		} 
		return new LoginPage(driver);
	}
	
	 public HomePage loginToApp(String username, String password)     
	{    	 
		 setTextAfterClearingElement(loginObjRepo, "userName", username,false, 0, "","UserName field in Login Page not visible");
		 setTextAfterClearingElement(loginObjRepo, "passWord", password,false, 0, "","Password field in  Login Page not visible");
		 clickElement(loginObjRepo, "loginButton",false, 0, "","Login field in Login Page not visible");	
		 return new HomePage(driver);
	}	 
	
	 public boolean isLoggedOut(){
		 try {
			 if(isElementDisplayed(loginObjRepo, "userName",  false, 0, "")){
				return true; 
			 }else{
				 waitForElementVisible(loginObjRepo, "userName",  false, 0, "","Login field in Login Page not visible");				 
				 return true;
			 }
		} catch (SeleniumException e) {
			return false;
		}
	  }
    }
	
	 

