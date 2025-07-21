package com.eox.utils;
import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HDOUtils {
	//protected WebDriver driver;
    private static WebDriverWait wait;
    private static WebDriver driver;
    public HDOUtils(WebDriver driver){
    	HDOUtils.driver = driver;
    	wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    
 // Click method with explicit wait
    public static boolean readTilesRolesBasis(String roleName, String jsonFilePath) {
    	ArrayList<String> allTabTitle= new ArrayList<>();
    	for (WebElement ele:driver.findElements(By.xpath("//*[@class='card-title']"))) {
    		allTabTitle.add(ele.getText());
    	}
    	getMenuItemsByRole(roleName , jsonFilePath);
    	return SupportUtils.validateListByPosition(getMenuItemsByRole(roleName , jsonFilePath), allTabTitle);
    	
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
		
		
		// Validate Roles wise tiles
	    private static ArrayList<String> getMenuItemsByRole(String roleKey, String jsonFilePath) {
	        ArrayList<String> result = new ArrayList<>();

	        try {
	        	ClassLoader classLoader = HDOUtils.class.getClassLoader();
	        	InputStream inputStream = classLoader.getResourceAsStream("testdata/"+ jsonFilePath+"/");
	        	if (inputStream == null) {
	                throw new IllegalArgumentException("roles.json not found in testdata folder");
	            }

	        	ObjectMapper mapper = new ObjectMapper();
	            Map<String, Map<String, ArrayList<String>>> jsonData = mapper.readValue(
	                    inputStream,
	                    new TypeReference<Map<String, Map<String, ArrayList<String>>>>() {}
	            );

	            // Extract roles map
	            Map<String, ArrayList<String>> rolesMap = jsonData.get("roles");
	            if (rolesMap != null && rolesMap.containsKey(roleKey)) {
	                result = rolesMap.get(roleKey);
	            }

	        } catch (Exception e) {
	            System.err.println("Error parsing JSON: " + e.getMessage());
	        }
	        
	        return result;	        
	    }				
	}
	
	

