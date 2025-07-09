package com.eox.utils;

import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverUtils {
	private static WebDriver driver;
	
	// initiate webdriver with driver options 
	public static WebDriver getDriver() {
		if(driver == null) {
		ChromeOptions options = new ChromeOptions();
		// 2. Start Maximized: Open the browser window in a maximized state.
		options.addArguments("--start-maximized");

		// 3. Incognito Mode: Open Chrome in incognito mode.
		options.addArguments("--incognito");

		// 4. Disable Infobars: Prevents "Chrome is being controlled by automated test software" bar.
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

		// 5. Disable Notifications: Blocks browser push notifications.
		options.addArguments("--disable-notifications");

		// 6. Disable Pop-up Blocking: Allows pop-up windows.
		options.addArguments("--disable-popup-blocking");
		driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(12));
		
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
