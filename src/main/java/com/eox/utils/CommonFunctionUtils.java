package com.eox.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonFunctionUtils {
	
	//protected WebDriver driver;
    private static WebDriverWait wait;
    private static WebDriver driver;
    public CommonFunctionUtils(WebDriver driver, int waitTime){
    	CommonFunctionUtils.driver = driver;
    	wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
    }
    
    // Login to the application
    public static void loginToApplication(String uname, String password) {
    	CommonFunctionUtils.enterText(driver.findElement(By.id("username")),uname);
		CommonFunctionUtils.enterText(driver.findElement(By.id("password")),password);
		try {
			CommonFunctionUtils.elementClick(driver.findElement(By.id("kc-login")));//this is for internal
		}
		catch (Exception e) {
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//button")));//this is for HDO
		}
		
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
    
    // Enter text method
    public static void enterKey(WebElement element, Keys text) {
    	try {
    		 wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    	}
       catch(Exception e) {
    	   wait.until(ExpectedConditions.visibilityOf(element));
    	   wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
       }    	
    }
    
    // Click operational button 
    public static void clickOperationalButtons(String iconName) {
    	if(iconName.equalsIgnoreCase("create")) {
    		CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//i[@class='fa fa-plus']//parent::button[@class='btn btn-primary']")));
    	}
    	if(iconName.equalsIgnoreCase("create user")) {
    		CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//i[@class='fa-solid fa-user-plus']//parent::button[@class='btn btn-primary']")));
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
    
    // Click Dashbaord operation button 
    public static void clickDashbaordOperationButtons(String iconName, String dashboardTitle) {
    	if(iconName.equalsIgnoreCase("create")) {
    		CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//*[contains(@class,'fa-plus')]/parent::a")));
    	}
		if(iconName.equalsIgnoreCase("exportexcel")) {
				CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//*[text()='"+dashboardTitle+"']/..//i[contains(@class,'fa fa-file-exce')]")));
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
 			elementClick(driver.findElement(By.xpath("//*[contains(text(),'"+dropdownItem+"')]/..//div[contains(@class,'choices')]")));
 			enterText(driver.findElement(By.xpath("//label[contains(text(),'"+dropdownItem+"')]/..//div[contains(@class,'choices')]//input")), dropdownMenuItem);
 			SupportUtils.waitFor(20);
 			enterKey(driver.findElement(By.xpath("//label[contains(text(),'"+dropdownItem+"')]/..//div[contains(@class,'choices')]//input")), Keys.ENTER);
 			
 	}
 	
 	// input functions
    public static void addTextToTheInputField(String inputItem, String inputValue) {
 		
 		enterText(driver.findElement(By.xpath("//*[contains(text(),'"+inputItem+"')]/..//input[@type='text']")), inputValue);
 	}
    
    // input textarea fields 
    public static void addTextToTheInputArea(String inputTitle, String inputValue) {
    	
 		enterText(driver.findElement(By.xpath("//*[contains(text(),'"+inputTitle+"')]/..//div[contains(@class,'ck-content')]")), inputValue);
 	}
    // App level special functions - like observer , esign etc 
    
    // Select multiple observers 
    public static void selectUsers(String userRole, String userName) {
    	CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//*[contains(text(),'"+userRole+"')]/..//div[contains(@class,'choices')]")));
		CommonFunctionUtils.enterText(driver.findElement(By.xpath("//label[contains(text(),'"+userRole+"')]/..//div[contains(@class,'choices')]//input")), userName);
		CommonFunctionUtils.waitForVisibility(driver.findElement(By.xpath("//label[contains(text(),'"+userRole+"')]/..//div[contains(@id,'choices')]//*[text()='"+userName+"']")));
		SupportUtils.waitFor(1000);
		CommonFunctionUtils.enterKey(driver.findElement(By.xpath("//label[contains(text(),'"+userRole+"')]/..//div[contains(@class,'choices')]//input")), Keys.ENTER);
		
    	
    }
    // Draw signature on signature pane
    public static void drawSignature() {
        WebElement canvas = driver.findElement(By.xpath("//canvas[@class='signature-pad-canvas']"));
        Actions drawAction = new Actions(driver);
        int startX = 50;  // Starting X-coordinate (relative to canvas left edge)
        int startY = 50;  // Starting Y-coordinate (relative to canvas top edge)

        // Define points to simulate drawing a letter 'S' or a squiggly line
        int[][] path = {
            {startX, startY},                 // Start point
            {startX + 80, startY - 20},       // Move up-right
            {startX + 60, startY -50},       // Move down-right
            {startX + 40, startY + 40},       // Move down-left
            {startX + 70, startY + 20},       // Move up-right again
            {startX + 100, startY + 50}       // Finish point
        };

        // Move to the starting point and click and hold
        drawAction.moveToElement(canvas, path[0][0], path[0][1])
                  .clickAndHold()
                  .pause(100); // Small pause after holding, crucial for some canvases

        // Iterate through the path points to simulate drawing a line
        for (int i = 1; i < path.length; i++) {
            // moveByOffset is relative to the *current* mouse position
            // So, we need to calculate the offset from the previous point
            int currentX = path[i][0];
            int currentY = path[i][1];
            int prevX = path[i-1][0];
            int prevY = path[i-1][1];

            int offsetX = currentX - prevX;
            int offsetY = currentY - prevY;

            drawAction.moveByOffset(offsetX, offsetY)
                      .pause(50); // Pause between each segment to simulate drawing speed
        }

        // Release the mouse button to finish drawing
        drawAction.release()
                  .perform();

        System.out.println("Signature drawing attempt completed.");
    }


}
