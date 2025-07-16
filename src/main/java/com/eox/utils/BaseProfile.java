package com.eox.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseProfile {

	private final WebDriver driver;

    public BaseProfile(WebDriver driver) {
        this.driver = driver;
    }

    public String getProfileName() {
        try {
            WebElement profileName = driver.findElement(By.cssSelector(".profile-info-content .name.text-overflow"));
            CommonFunctionUtils.waitForVisibility(profileName);
            return profileName.getText();
        } catch (Exception e) {
            System.err.println("Element not found: Profile Name");
            return "";
        }
    }

    public String getProfileDesignation() {
        try {
            WebElement designationElement = driver.findElement(By.cssSelector(".profile-info-content .designation.text-overflow"));
            return designationElement.getText();
        } catch (Exception e) {
            System.err.println("Element not found: Profile Designation");
            return "";
        }
    }

    public void clickEditPreferences() {
        try {
            WebElement editButton = driver.findElement(By.xpath("//*[@class='action-title' and text()='Edit Preferences']"));
            CommonFunctionUtils.waitForVisibility(editButton);
			CommonFunctionUtils.elementClick(editButton);
        } catch (Exception e) {
            System.err.println("Click failed on 'Edit Preferences' button: " + e.getMessage());
        }
    }

	public boolean clickSignOut() {
		try {
			WebElement signOutButton = driver.findElement(By.xpath("//*[@class='action-title' and text()='Sign Out']"));
			CommonFunctionUtils.waitForVisibility(signOutButton);
			CommonFunctionUtils.elementClick(signOutButton);
			CommonFunctionUtils.waitForInvisibility(signOutButton);
			
			// Check if either "username" or "identifier" fields appear
            return isElementVisible(driver, By.name("username")) || isElementVisible(driver, By.name("identifier"));
        } catch (Exception e) {
            System.err.println("Click failed on 'Sign Out' button: " + e.getMessage());
            return false;
        }
			
	}
	
	/**
     * Checks if the profile panel is active and clicks it if it's not.
     *
     * @param driver The WebDriver instance.
     */
    public static void profileVisibilityCheck(WebDriver driver) {

        // Check if the profile panel is already active using findElements()
        List<WebElement> activePanel = driver.findElements(By.cssSelector(".panel-profile-content.panel-active"));

        if (!activePanel.isEmpty()) {
            System.out.println("Profile panel is already active.");
        } else {
            System.out.println("Profile panel is not active. Attempting to click and activate it.");

            try {
                // Wait for the inactive profile element to be clickable
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement profileElementToClick = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".panel-profile-icon")));
                
                CommonFunctionUtils.waitForVisibility(profileElementToClick);
                // Click the profile panel to make it active
                CommonFunctionUtils.elementClick(profileElementToClick);
                
                System.out.println("Clicked the profile panel to make it active.");

                // Re-find the element to avoid StaleElementReferenceException
                WebElement activatedProfileElement = driver.findElement(By.cssSelector(".panel-profile-content.panel-active"));

                // Verify that the element now has the 'panel-active' class
                String newClassAttribute = activatedProfileElement.getDomAttribute("class");
                if (newClassAttribute != null && newClassAttribute.contains("panel-active")) {
                    System.out.println("Verification successful: 'panel-active' class is now present.");
                } else {
                    System.err.println("Verification failed: 'panel-active' class was not added after the click.");
                }

            } catch (Exception e) {
                // Catch specific exceptions and provide a meaningful error message
                System.err.println("An error occurred while trying to click or verify the profile panel: " + e.getMessage());
            }
        }
    }
    
    private static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            CommonFunctionUtils.waitForVisibility(element);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
