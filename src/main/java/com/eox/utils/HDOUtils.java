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

// Click on 
}
