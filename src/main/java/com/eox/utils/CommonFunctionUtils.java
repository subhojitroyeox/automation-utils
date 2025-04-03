package com.eox.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonFunctionUtils {
	
	protected WebDriver driver;
    private WebDriverWait wait;

    // Constructor to initialize driver and explicit wait
    public CommonFunctionUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Click method with explicit wait
    protected void elementClick(WebElement element) {
    	wait.until(ExpectedConditions.elementToBeClickable(element));
       // wait.until(ExpectedConditions.elementToBeClickable(element));
        SupportUtils.safeClick(element,driver,3);
    }

    // Enter text method
    protected void enterText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    }

    // Get text from element
    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText();
    }

    // Wait for an element to be visible
    protected void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    // open an app from side panel 
    
    protected String launchAnApp(String appName) {
    	elementClick(driver.findElement(By.xpath("//div[@class='logo-here']/img"))); // open side bar
    	driver.findElement(By.xpath("//input[@id='appsearch']")).sendKeys(appName); //Search an app 
    	elementClick(driver.findElement(By.xpath("//div[text()='"+appName+"']/ancestor::div[@class='app app-item']")));
    	return wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@id='NavigationHeaderContainer_tempId']//span[contains(text(),'"+appName+"')]"))))
    			.getText();
    	
    }
    
    // Open left nav bar for any app
    protected void openSideNavBar(String menuName) {
    	elementClick(
    			(driver.findElement
    					(By.xpath("//div[contains(@id,'left-navigation')]//button[contains(@class,'sidenav-toggle')]"))));
    	
    	waitForVisibility(driver.findElement(By.xpath("//nav[contains(@class,'sidenav---expanded')]")));
    	elementClick(
    			(driver.findElement(By.xpath("//*[contains(@name,'"+menuName+"') and contains(text(),'"+menuName+"')]")))
    					);
    }
    
    
 // select function
    protected void selectItemFromDropdown(String dropdownItem, String dropdownMenuItem) {
 		elementClick(driver.findElement(By.xpath("//*[contains(text(),'"+dropdownItem+"')]/..//div[contains(@class,'choices')]")));
 		try{
 			elementClick(driver.findElement(By.xpath("//*[text()='"+dropdownMenuItem+"']/..")));
 		}
 		catch (Exception e) {
 			elementClick(driver.findElement(By.xpath("//*[contains(text(),'"+dropdownItem+"')]/..//div[contains(@class,'choices')]")));
 			elementClick(driver.findElement(By.xpath("//*[text()='"+dropdownMenuItem+"']/..")));
 			
 		}
 		
 			
 		
 	}
 	
 	// input functions
    protected void addTextToTheInputField(String inputItem, String inputValue) {
 		
 		enterText(driver.findElement(By.xpath("//*[contains(text(),'"+inputItem+"')]/..//input[@type='text']")), inputValue);
 	}

}
