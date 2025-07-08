package com.eox.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SupportUtils {
	//Read Property file and return the value
		private static Properties properties;
		 private static final String ROOT_DIR = System.getProperty("user.dir").concat("/src/test/resources/testData/");
		public static String getProperty(String key) {
			properties = new Properties();
	        FileInputStream file;
			try {
				file = new FileInputStream("src/test/resources/config.properties");
				properties.load(file);
			} catch (FileNotFoundException e) {			
				e.printStackTrace();
			} catch (IOException e) {			
				e.printStackTrace();
			}
	        
	        return properties.getProperty(key);
	    }
		
		//--- this is for extracting and getting test data as json format
		
		public static String getValueFromJson(String filePath, String key) throws IOException, IllegalArgumentException {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode rootNode;

	        File jsonFile = new File(ROOT_DIR+"/"+filePath);
	        if (!jsonFile.exists()) {
	            throw new IOException("JSON file not found at: " + filePath);
	        }

	        try {
	            rootNode = objectMapper.readTree(jsonFile);
	        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
	            throw new com.fasterxml.jackson.core.JsonProcessingException(
	                "Error decoding JSON from '" + filePath + "': " + e.getMessage()) {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;};
	        }

	        String[] keys = key.split("\\.");
	        JsonNode currentNode = rootNode;

	        for (int i = 0; i < keys.length; i++) {
	            String currentKeyPart = keys[i];

	            if (currentNode.isObject()) {
	                currentNode = currentNode.get(currentKeyPart);
	                if (currentNode == null) {
	                    throw new IllegalArgumentException(
	                        "Key '" + currentKeyPart + "' not found in JSON path: '" + key + "'"
	                    );
	                }
	            } else {
	                String traversedPath = String.join(".", java.util.Arrays.copyOfRange(keys, 0, i));
	                throw new IllegalArgumentException(
	                    "Cannot access key '" + currentKeyPart + "' on a non-object value at path part: '" + traversedPath + "'"
	                );
	            }
	        }
	        return currentNode.asText();
	    }
		
	    //-- upload any file to the application
		public static void uploadFile(String labelName,String userFilePath, WebDriver driver) {
	    	
	    	WebElement attachment = driver.findElement(By.xpath("//label[normalize-space(text())="+labelName+"]/following::a[contains(@class, 'browse')][1]"));
	    	attachment.click();

	    	String filePath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "testData", userFilePath).toAbsolutePath().toString();

	        File file = new File(filePath);
	    	StringSelection filePathSelection = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filePathSelection, null);
			try {
				copyPasteAction();
			} catch (AWTException e) {
				System.err.println("Error during copy-paste action for file upload: " + e.getMessage());
			}
			
		}
		
		public static void copyPasteAction() throws AWTException {

			Robot robot = new Robot();
			robot.delay(1000);
			
	        robot.keyPress(KeyEvent.VK_CONTROL);
	        robot.keyPress(KeyEvent.VK_V);
	        robot.keyRelease(KeyEvent.VK_V);
	        robot.keyRelease(KeyEvent.VK_CONTROL);

	        robot.delay(500);

	        robot.keyPress(KeyEvent.VK_ENTER);
	        robot.keyRelease(KeyEvent.VK_ENTER);
		}
	    // Generate Extend report 
	    private static ExtentReports extent;

	    public static ExtentReports getInstance() {
	        if (extent == null) {
	           String reportPath = System.getProperty("user.dir") + "/test-output/Functional/Functional_Report.html";
	            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath)
	                    .viewConfigurer()
	                    .viewOrder()
	                    .as(new ViewName[]{ViewName.DASHBOARD, ViewName.TEST})
	                    .apply();
	            spark.config().setDocumentTitle("Automation Test Report");
	            spark.config().setReportName("Selenium Test Execution Report");
	            spark.config().setTheme(Theme.DARK);
	            spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
	            spark.config().setJs(
	            	    "document.addEventListener('DOMContentLoaded', function() {" +
	            	    "document.querySelector('.logo').style.backgroundImage = \"url('LOGO-EOX.png')\";" +
	            	    "});"
	            	);
	            
	            extent = new ExtentReports();
	            extent.attachReporter(spark);
	            extent.setSystemInfo("OS", System.getProperty("os.name"));
	            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
	            extent.setSystemInfo("Tester", "QA Engineer");
	        }
	        return extent;
	    }
	    // Takes screenshot 
	    public static String takeScreenshot(String testName, WebDriver driver) {
	        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	        String screenshotPath = System.getProperty("user.dir") + "/test-output/screenshots/" + testName + "_" + timestamp + ".png";

	        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        try {
	            FileUtils.copyFile(src, new File(screenshotPath));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return screenshotPath;
	    }
	    
	    //Retry for unwanted methods 
	    public static void safeClick(WebElement element, WebDriver driver) {
	    	int attempts = 0;
	    	while (attempts < 10) {
	    		try {
	    			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element); // Try clicking normally
	    			return; // If successful, exit method
	    		} catch (Exception e) {
	    			attempts++;
	    			waitFor(50); // Wait before retrying
	    		}
	    	}
	    	// If all retries fail, click using JavaScript
	    	System.out.println("Click intercepted, using JavaScript executor...");
	    	
	    }
	    //Retry for unwanted methods -for safely insert
	    public static void safeInsert(WebElement targer, String input, WebDriver driver) {
	        int attempts = 0;
	        waitFor(10);
	        while (attempts < 10) {
	            try {
	            	targer.sendKeys(input); // Try clicking normally
	                return; // If successful, exit method
	            } catch (Exception e) {
	                attempts++;
	                waitFor(50); // Wait before retrying
	            }
	        }
	    }

	    static void waitFor(int millis) {
	        try {
	            Thread.sleep(millis);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	    }
	    
	    //upload any file to the application 
//	    public static void uploadFile(String labelName,String userFilePath, WebDriver driver) {
//	    	
//	    	WebElement attachment = driver.findElement(By.xpath("//label[normalize-space(text())='"+labelName+"']/following::a[contains(@class, 'browse')][1]"));
//	    	
//	        String filePath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "testData", userFilePath).toAbsolutePath().toString();
//
//	        File file = new File(filePath);
//
//	        if (!file.exists()) {
//	            throw new IllegalArgumentException("File not found at the specified path: " + filePath +
//	                                               ". Please ensure the file exists in 'src/test/resources/testData/'");
//	        }
//
//	        try {	
//	            //WebElement fileInputElement = driver.findElement(fileInputLocator);
//	        	attachment.sendKeys(filePath);
//	            System.out.println("Successfully attempted to upload file: " + userFilePath + " from path: " + filePath);
//	        } catch (Exception e) {
//	            System.err.println("Error during file upload for '" + userFilePath + "': " + e.getMessage());
//	            throw e;
//	        }
//	    }
}
