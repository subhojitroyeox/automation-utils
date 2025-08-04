package com.eox.utils;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverUtils {
	private static WebDriver driver;

	public static final String DOWNLOAD_PATH = System.getProperty("user.dir") + File.separator + "Downloads"
			+ File.separator;

	// initiate webdriver with driver options
	public static WebDriver getDriver() {

		File downloadFolder = new File(DOWNLOAD_PATH);
		downloadFolder.mkdir();

		if (driver == null) {
			Map<String, Object> preferences = new HashMap<>();
			preferences.put("profile.default_content_settings.popups", 0);
			preferences.put("download.default_directory", DOWNLOAD_PATH);
			preferences.put("download.prompt_for_download", false);
			preferences.put("safebrowsing.enabled", true);
			
			// Disable password manager and avoids pop-ups for saving passwords
			preferences.put("credentials_enable_service", false);
			preferences.put("profile.password_manager_enabled", false);
			preferences.put("profile.password_manager_leak_detection", false);

			ChromeOptions options = new ChromeOptions();
			// 2. Start Maximized: Open the browser window in a maximized state.
			options.addArguments("--start-maximized");

			// Adding download preferences
			options.setExperimentalOption("prefs", preferences);

			// 3. Incognito Mode: Open Chrome in incognito mode.
			// options.addArguments("--incognito");

			// 4. Disable Infobars: Prevents "Chrome is being controlled by automated test
			// software" bar.
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

	// Close driver
	public static void quitDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

}
