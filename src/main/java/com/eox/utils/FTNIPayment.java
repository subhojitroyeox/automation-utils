package com.eox.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FTNIPayment {
	private static WebDriver driver;
	public FTNIPayment(WebDriver driver) {
		FTNIPayment.driver = driver;
	}
	public static Boolean ftniPaymentCompletionForUS() {
		final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Wait until there are 2 iframes
		wait.until(new ExpectedCondition<Boolean>() {
		    public Boolean apply(WebDriver driver) {
		        return (driver.findElements(By.tagName("iframe")).size() >= 2);
		    }
		});

		WebElement targetIframe;

		try {
		    List<WebElement> iframes = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("iframe")));
		    targetIframe = iframes.get(1);
		} catch (Exception e) {
		    System.err.println("Error locating iframe element on the parent page: " + e.getMessage());
		    throw e;
		}

		driver.switchTo().defaultContent();

		try {
		    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(targetIframe));
		} catch (NoSuchFrameException e) {
		    System.err.println("NoSuchFrameException when switching to the second iframe. Error: " + e.getMessage());
		    throw e;
		} 

		JavascriptExecutor js = (JavascriptExecutor) driver;

		wait.until(webDriver -> {
		    String readyState = js.executeScript("return document.readyState").toString();
		    return "complete".equals(readyState) || "interactive".equals(readyState);
		});

		try {
			js.executeScript("document.getElementById('BankName').value='FTNI Test';");
			js.executeScript("document.getElementById('RoutingNumber').value='123456780';");
			js.executeScript("document.getElementById('AccountNumber').value='18616161';");
		    js.executeScript("document.getElementById('f_TermsAndConditions').click();");
		} catch (Exception e) { 
		    System.err.println("Error entering First Name via JS: " + e.getMessage()); 
		}
		return true;
	}
	

}
