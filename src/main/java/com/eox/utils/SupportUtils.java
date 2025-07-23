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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.fasterxml.jackson.databind.node.ArrayNode;

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
	        	System.out.println("this is calling");
	        	String filePath = Paths.get(System.getProperty("user.dir"), "test-output", "Functional", "Functional_Report.html").toAbsolutePath().toString();

	           String reportPath = filePath;
	           System.out.println("this is the path:"+reportPath); 
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

	    public static void waitFor(int millis) {
	        try {
	            Thread.sleep(millis);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	    }
	    
	    /**
	     * A utility method to read values from a JSON file that contains a root-level array.
	     * It features an internal cache to avoid reading the same file multiple times.
	     */
	    private static final Map<String, ArrayNode> cachedJsonData = new HashMap<>();
	    private static final ObjectMapper objectMapper = new ObjectMapper();
	    
	    /**
	     * Retrieves a string value from a specific object within a JSON file.
	     * The file is expected to contain a JSON array at its root. It will be loaded
	     * and cached on the first call for a given file path.
	     *
	     * This optimized version uses Jackson's built-in JSON Pointer support for robust
	     * and efficient path traversal.
	     *
	     * Example Usage:
	     * String city = JsonUtil.getValue("src/test/resources/users.json", 0, "address.city");
	     * String itemName = JsonUtil.getValue("src/test/resources/orders.json", 1, "items[0].name");
	     *
	     * @param filePath The absolute or relative path to the JSON array file.
	     * @param index    The 0-based index of the object within the root array.
	     * @param keyPath  The dot-separated path to the desired value (e.g., "user.address.city").
	     * Also supports array indexing (e.g., "items[0].name").
	     * @return The extracted value as a String. Returns an empty string if the value is null or the path is not found.
	     * @throws RuntimeException If the file cannot be loaded, is not a valid JSON array,
	     * or if the index is out of bounds.
	     */
	    
	    public static String getValue(String filePath, int index, String keyPath) {
	        ArrayNode jsonArray = loadAndCacheJsonArray(filePath);

	        if (index < 0 || index >= jsonArray.size()) {
	            throw new IndexOutOfBoundsException(
	                "Index " + index + " is out of bounds for file '" + filePath
	                + "'. Array size is " + jsonArray.size() + "."
	            );
	        }

	        JsonNode targetObject = jsonArray.get(index);

	        // Convert the custom keyPath into a standard JSON Pointer string.
	        // For example, "user.items[0].name" becomes "/user/items/0/name".
	        // This allows us to use Jackson's efficient `at()` method.
	        String jsonPointerPath = "/" + keyPath.replace('.', '/').replaceAll("\\[(\\d+)\\]", "/$1");

	        // Use `at()` to navigate the path. It safely handles missing paths
	        // by returning a special MissingNode instead of throwing an exception.
	        JsonNode resultNode = targetObject.at(jsonPointerPath);

	        // If the path does not exist (isMissingNode) or the value is explicitly null, return an empty string.
	        if (resultNode.isMissingNode() || resultNode.isNull()) {
	            return "";
	        }

	        return resultNode.asText();
	    }
	    
	    
	    /**
	     * Loads a JSON file from a given path, expecting a root-level array.
	     * Caches the result to avoid redundant file I/O. This method is synchronized
	     * to ensure it's thread-safe.
	     *
	     * @param filePath The path to the JSON file.
	     * @return The parsed ArrayNode from the file.
	     */
	    private static synchronized ArrayNode loadAndCacheJsonArray(String filePath) {
	        // Return from cache if already loaded
	        if (cachedJsonData.containsKey(filePath)) {
	            return cachedJsonData.get(filePath);
	        }

	        try {
	            File file = new File(filePath);
	            JsonNode rootNode = objectMapper.readTree(file);

	            if (rootNode.isArray()) {
	                ArrayNode arrayNode = (ArrayNode) rootNode;
	                cachedJsonData.put(filePath, arrayNode); // Cache the result
	                return arrayNode;
	            } else {
	                throw new IllegalArgumentException(
	                    "JSON file '" + filePath + "' does not contain a root-level array."
	                );
	            }
	        } catch (IOException e) {
	            throw new RuntimeException("Error loading or parsing JSON file at '" + filePath + "'.", e);
	        }
	    }
	    // validate 2 arraylist 
	    public static boolean validateListByPosition(ArrayList<String> expected, ArrayList<String> actual) {
	        if (expected == null || actual == null) {
	            System.out.println("One of the lists is null.");
	            return false;
	        }

	        if (expected.size() != actual.size()) {
	            System.out.println("List sizes are different. Expected: " + expected.size() + ", Actual: " + actual.size());
	            return false;
	        }

	        boolean allMatch = true;

	        for (int i = 0; i < expected.size(); i++) {
	            String expectedItem = expected.get(i).trim();
	            String actualItem = actual.get(i).trim();

	            if (!expectedItem.equals(actualItem)) {
	                System.out.println("Mismatch at position " + i + ": Expected '" + expectedItem + "', but got '" + actualItem + "'");
	                allMatch = false;
	            }
	        }

	        return allMatch;
	    }

}
