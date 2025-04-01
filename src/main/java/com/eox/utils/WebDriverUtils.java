package com.eox.utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverUtils {
	private static WebDriver driver;
	
	// initiate webdriver with driver options 
	public static WebDriver getDriver() {
		if(driver != null) {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		}
		return driver;
	}
	
	//Close driver 
	public static void quitDriver() {
		if (driver != null) {
            driver.quit();
            driver = null;
        }
	}


}
