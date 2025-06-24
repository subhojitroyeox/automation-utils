package com.eox.utils;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonFunctionUtils {
	
	//protected WebDriver driver;
    private static WebDriverWait wait;
    private static WebDriver driver;
    public CommonFunctionUtils(WebDriver driver){
    	CommonFunctionUtils.driver = driver;
    	wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Click method with explicit wait
    public static void elementClick(WebElement element) {
    	wait.until(ExpectedConditions.elementToBeClickable(element));
        SupportUtils.safeClick(element,driver);
    }
    
    // Click any button - enabled --> updated on 30-05-2025
    public static void activeButtonClick(String ButtonName) {
    	wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//button[contains(text(),'"+ButtonName+"')]"))));
    	elementClick(driver.findElement(By.xpath("//button[contains(text(),'"+ButtonName+"')]")));
    }
    
    // checkbox select - checked and unchecked  --> updated on 30-05-2025
    public static void checkBoxClick(String contains) {
    	WebElement ele = driver.findElement(By.xpath("//*[contains(text(),'"+contains+"')]/..//input[@type='checkbox']"));
    	if(!ele.isSelected() ) {
    		elementClick(ele);
    	}  	
    }
    public static void checkBoxUnchecked(String contains) {
    	WebElement ele = driver.findElement(By.xpath("//*[contains(text(),'"+contains+"')]/..//input[@type='checkbox']"));
    	if(ele.isSelected() ) {
    		elementClick(ele);
    	}    	
    }
    
    //radio button select --> added on 30-05-2025
    public static void radioButtonSelect(String type, String headerLavel) {
    	elementClick(driver.findElement(By.xpath("//*[contains(text(),'"+headerLavel+"')]/following::input[contains(@id,'"+type+"')and @type='radio'] ")));
    }
    
    // Enter text method
    public static void enterText(WebElement element, String text) {
    	try {
    		 wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    	}
       catch(Exception e) {
    	   wait.until(ExpectedConditions.visibilityOf(element));
    	   SupportUtils.safeInsert(element,text,driver);
       }

    	
    }
    
    // Click operational button 
    public static void clickOperationalButtons(String iconName) {
		if(iconName.equalsIgnoreCase("create")) {
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//i[@class='fa fa-plus']//parent::button[@class='btn btn-primary']")));
		}
		if(iconName.equalsIgnoreCase("refresh")) {
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//i[@class='fa-solid fa-arrows-rotate']//parent::button[@class='btn btn-primary']")));
		}
		if(iconName.equalsIgnoreCase("exportpdf")) {
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//i[@class='fa fa-file-pdf']//parent::button[@class='btn btn-primary']")));
		}
		if(iconName.equalsIgnoreCase("exportexcel")) {
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//i[@class='fa fa-file-excel']//parent::button[@class='btn btn-primary']")));
		}
    }
    // Get text from element
    public static String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText();
    }

    // Wait for an element to be visible
    public static void waitForVisibility(WebElement element) {
    	wait.until(ExpectedConditions.visibilityOf(element));
    }
    // Wait for an element to be visible
    public static void waitForClickableElement(WebElement element) {
    	wait.until(ExpectedConditions.visibilityOf(element));
    	wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    // Wait for an element to be visible
    public static void waitForSpinnerGoesOff() {
    	try {
    		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//div[@class='spinner']"))));
    	}
        catch(Exception e) {        	
        	wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//div[@class='spinner']"))));
        	if(driver.findElement(By.xpath("div[@class='osjs-boot-splash_message']")).isDisplayed()) {
        		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("div[@class='osjs-boot-splash_message']"))));
        		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("div[@class='osjs-boot-splash_message']"))));
        	}
        	
        }
    	
    }
    
    // open an app from side panel 
    
    public static String launchAnApp(String appName, String tabName) {
    	elementClick(driver.findElement(By.xpath("//div[@class='logo-here']/img"))); // open side bar
    	driver.findElement(By.xpath("//input[@id='appsearch']")).sendKeys(appName); //Search an app 
    	elementClick(driver.findElement(By.xpath("//div[text()='"+appName+"']/ancestor::div[@class='app app-item']")));
    	return wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@id='NavigationHeaderContainer_tempId']//span[contains(text(),'"+tabName+"')]"))))
    			.getText();
    	
    }
    
    // Open left nav bar for any app
    public static void openSideNavBar(String menuName) {
    	elementClick(
    			(driver.findElement
    					(By.xpath("//div[contains(@id,'left-navigation')]//button[contains(@class,'sidenav-toggle')]"))));
    	
    	waitForVisibility(driver.findElement(By.xpath("//nav[contains(@class,'sidenav---expanded')]")));
    	elementClick(
    			(driver.findElement(By.xpath("//*[contains(@name,'"+menuName+"') and contains(text(),'"+menuName+"')]")))
    					);
    }
    
    
 // select function
    
    public static void selectItemFromDropdown(String dropdownItem, String dropdownMenuItem) {
    	SupportUtils.waitFor(200);
 		elementClick(driver.findElement(By.xpath("//*[contains(text(),'"+dropdownItem+"')]/..//div[contains(@class,'choices')]")));
 		try {
 			elementClick(driver.findElement(By.xpath("//*[contains(text(),'"+dropdownItem+"')]/..//*[text()='"+dropdownMenuItem+"']/..")));
 		}
 		catch(Exception e) {
 			SupportUtils.waitFor(200);
 			elementClick(driver.findElement(By.xpath("//*[contains(text(),'"+dropdownItem+"')]/..//*[text()='"+dropdownMenuItem+"']/..")));
 		}
 	}
    
    // Multiple Selection from dropdown
    public static void multiSelectFromDropdown(String dropdownItem, String[] optionList) {
    	List<String> optionsList = Arrays.stream(optionList).collect(Collectors.toList());
    	for(String optionText : optionsList) {
    		selectItemFromDropdown(dropdownItem, optionText);
    	}
    }
 	
 	// input functions
    public static void addTextToTheInputField(String inputItem, String inputValue) {
 		
 		enterText(driver.findElement(By.xpath("//*[contains(text(),'"+inputItem+"')]/..//input[@type='text']")), inputValue);
 	}
    
    // input textarea fields 
    public static void addTextToTheInputArea(String inputTitle, String inputValue) {
    	
 		enterText(driver.findElement(By.xpath("//*[contains(text(),'"+inputTitle+"')]/..//div[contains(@class,'ck-content')]")), inputValue);
 	}
    
    // Draw signature on signature pane
    public void drawSignature() {
        WebElement canvas = driver.findElement(By.xpath("//canvas[@class='signature-pad-canvas']"));
        Actions drawAction = new Actions(driver);
        int offsetX = 10; 
        int offsetY = 50;
        int endX = 150;
        int endY = 10;
        drawAction.moveToElement(canvas, offsetX, offsetY)
                  .clickAndHold()
                  .moveByOffset(endX, endY) 
                  .release()
                  .perform();
    }


}
