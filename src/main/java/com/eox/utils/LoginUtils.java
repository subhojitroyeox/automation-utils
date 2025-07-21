package com.eox.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
public class LoginUtils {

	private static WebDriver driver;
	private static String url;
	private static String uname;
	private static String password;
	public LoginUtils(WebDriver driver, String url, String uname, String password) {
		System.out.println(url);
		LoginUtils.driver = driver;
		LoginUtils.url = url;
		LoginUtils.uname = uname;
		LoginUtils.password = password;
	}
	// Login to the application
	public static void loginToApplication() {
		loginToApplication(null);
	}

	public static void loginToApplication(String productName) {
	    WebElement usernameElement = driver.findElement(By.id("username"));
	    WebElement passwordElement = driver.findElement(By.id("password"));

	    if(usernameElement != null && passwordElement != null) {
	        CommonFunctionUtils.enterText(usernameElement, uname);
	        CommonFunctionUtils.enterText(passwordElement, password);

	        WebElement buttonElement = null;

	        if(productName != null) {
	            buttonElement = driver.findElement(By.xpath("//button"));
	        } else {
	            try {
	                buttonElement = driver.findElement(By.id("kc-login"));
	            } catch (Exception e) {
	                buttonElement = driver.findElement(By.xpath("//button"));
	            }
	        }

	        if(buttonElement != null) {
	            CommonFunctionUtils.elementClick(buttonElement);
	        }
	    }
	}
		//re-Login to the application after logout
		public static void switchUser(String userName,String Password,String productName) {
			logOutToTheApplication();
			driver.get(url);
			WebElement usernameElement = driver.findElement(By.id("username"));
		    WebElement passwordElement = driver.findElement(By.id("password"));

		    if(usernameElement != null && passwordElement != null) {
		        CommonFunctionUtils.enterText(usernameElement, userName);
		        CommonFunctionUtils.enterText(passwordElement, Password);

		        WebElement buttonElement = null;

		        if(productName != null) {
		            buttonElement = driver.findElement(By.xpath("//button"));
		        } else {
		            try {
		                buttonElement = driver.findElement(By.id("kc-login"));
		            } catch (Exception e) {
		                buttonElement = driver.findElement(By.xpath("//button"));
		            }
		        }

		        if(buttonElement != null) {
		            CommonFunctionUtils.elementClick(buttonElement);
		        }
		    }			
		}
		
		//re-Login to the application after logout
		public static void logOutToTheApplication() {
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//div[@class='panel-profile-icon']")));
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//div[text()='Sign Out']/parent::div")));
			
		}
}
