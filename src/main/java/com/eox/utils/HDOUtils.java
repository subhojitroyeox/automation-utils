package com.eox.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HDOUtils {
	//protected WebDriver driver;
    private static WebDriverWait wait;
    private static WebDriver driver;
    public HDOUtils(WebDriver driver){
    	HDOUtils.driver = driver;
    	wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    
 // Click method with explicit wait
    public static void fetchAllTabTexts(List<WebElement> element) {
    	for (WebElement ele:driver.findElements(By.xpath("//li[contains(@class,'page-item')]//button"))) {
    		System.out.println(ele.getText());
    	}    	
    }
    // Login to the HDO application
    public static void loginToApplication(String uname, String password) {
    	CommonFunctionUtils.enterText(driver.findElement(By.id("username")),uname);
		CommonFunctionUtils.enterText(driver.findElement(By.id("password")),password);
		CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//button")));
    }
    
 // input functions
	public static void openTiles(String tilesTitle, String tilesButtonName) { 		
    	CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//a[contains(@title,'"+tilesTitle+"') and contains(normalize-space(), '"+tilesButtonName+"')]")));
    	CommonFunctionUtils.waitForVisibility(driver.findElement(By.xpath("//span[text()='"+tilesTitle+"' and contains(@class,'k-breadcrumb-item-text')]")));
}
	
	// HDO Operational button
	public static void clickDashbaordOperationButtons(String iconName) {
		if(iconName.equals("refresh")) {
    		CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//i[contains(@class,'fa fa-refresh')]//parent::button[@class='btn btn-primary']")));
    		
		}
		else if (iconName.equals("add user")) {
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//button[@title = 'Add User']")));
			CommonFunctionUtils.waitForVisibility(driver.findElement(By.xpath("//span[text()='Add User' and contains(@class,'k-breadcrumb-item-text')]")));
		
		}
	}
		
		// HDO action button activity
		public static void hdoClickActionsButtons(String email,String buttonName) {
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//td[text()='"+email+"']/..//*[text()='"+buttonName+"']/parent::button")));
		}
	}
	
	

