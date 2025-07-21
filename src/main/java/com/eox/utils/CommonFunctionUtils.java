package com.eox.utils;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonFunctionUtils {

	// protected WebDriver driver;
	private static WebDriverWait wait;
	private static WebDriver driver;
	public CommonFunctionUtils(WebDriver driver, int waitTime) {
		CommonFunctionUtils.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
		
	}
	// Click method with explicit wait
	public static void elementClick(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
		SupportUtils.safeClick(element, driver);
	}

	// Click any button - enabled --> updated on 30-05-2025
	public static void activeButtonClick(String ButtonName) {
		wait.until(ExpectedConditions
				.elementToBeClickable(driver.findElement(By.xpath("//button[contains(text(),'" + ButtonName + "')]"))));
		elementClick(driver.findElement(By.xpath("//button[contains(text(),'" + ButtonName + "')]")));
	}

	// checkbox select - checked and unchecked --> updated on 30-05-2025
	public static void checkBoxClick(String contains) {
		WebElement ele = driver
				.findElement(By.xpath("//*[contains(text(),'" + contains + "')]/..//input[@type='checkbox']"));
		if (!ele.isSelected()) {
			elementClick(ele);
		}
	}

	public static void checkBoxUnchecked(String contains) {
		WebElement ele = driver
				.findElement(By.xpath("//*[contains(text(),'" + contains + "')]/..//input[@type='checkbox']"));
		if (ele.isSelected()) {
			elementClick(ele);
		}
	}

	// radio button select --> added on 30-05-2025
	public static void radioButtonSelect(String type, String headerLavel) {

		elementClick(driver.findElement(By.xpath("//*[contains(text(),'" + headerLavel
				+ "')]/following::input[contains(@id,'" + type + "')and @type='radio'] ")));

	}

	// Enter text method
	public static void enterText(WebElement element, String text) {
		try {
			wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
		} catch (Exception e) {
			wait.until(ExpectedConditions.visibilityOf(element));
			SupportUtils.safeInsert(element, text, driver);
		}
	}

	// Enter text method
	public static void enterKey(WebElement element, Keys text) {
		try {
			wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
		} catch (Exception e) {
			wait.until(ExpectedConditions.visibilityOf(element));
			wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
		}
	}

	// Click operational button
	public static void clickOperationalButtons(String iconName) {
		if (iconName.equalsIgnoreCase("create")) {
			CommonFunctionUtils.elementClick(
					driver.findElement(By.xpath("//i[@class='fa fa-plus']//parent::button[@class='btn btn-primary']")));
		}
		if (iconName.equalsIgnoreCase("create user")) {
			CommonFunctionUtils.elementClick(driver.findElement(
					By.xpath("//i[@class='fa-solid fa-user-plus']//parent::button[@class='btn btn-primary']")));
		}
		if (iconName.equalsIgnoreCase("refresh")) {
			CommonFunctionUtils.elementClick(driver.findElement(
					By.xpath("//i[@class='fa-solid fa-arrows-rotate']//parent::button[@class='btn btn-primary']")));
		}
		if (iconName.equalsIgnoreCase("exportpdf")) {
			CommonFunctionUtils.elementClick(driver
					.findElement(By.xpath("//i[@class='fa fa-file-pdf']//parent::button[@class='btn btn-primary']")));
		}
		if (iconName.equalsIgnoreCase("exportexcel")) {
			CommonFunctionUtils.elementClick(driver
					.findElement(By.xpath("//i[@class='fa fa-file-excel']//parent::button[@class='btn btn-primary']")));
		}
	}

	// Click Dashbaord operation button
	public static void clickDashbaordOperationButtons(String iconName, String dashboardTitle) {
		if (iconName.equalsIgnoreCase("create")) {
			CommonFunctionUtils.elementClick(driver.findElement(By.xpath("//*[contains(@class,'fa-plus')]/parent::a")));
		}
		if (iconName.equalsIgnoreCase("exportexcel")) {
			CommonFunctionUtils.elementClick(driver.findElement(
					By.xpath("//*[text()='" + dashboardTitle + "']/..//i[contains(@class,'fa fa-file-exce')]")));
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

	// wait for invisibility of an element
	public static void waitForInvisibility(WebElement element) {
		wait.until(ExpectedConditions.invisibilityOf(element));
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
		} catch (Exception e) {
			wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//div[@class='spinner']"))));
			if (driver.findElement(By.xpath("div[@class='osjs-boot-splash_message']")).isDisplayed()) {
				wait.until(ExpectedConditions
						.invisibilityOf(driver.findElement(By.xpath("div[@class='osjs-boot-splash_message']"))));
				wait.until(ExpectedConditions
						.invisibilityOf(driver.findElement(By.xpath("div[@class='osjs-boot-splash_message']"))));
			}

		}

	}

	// open an app from side panel

	public static String launchAnApp(String appName, String tabName) {
		elementClick(driver.findElement(By.xpath("//div[contains(@class,'osjs-panel-my-home')]//i"))); // open side bar
		elementClick(driver.findElement(By.xpath("//div[@class='logo-here']/img"))); // open side bar
		driver.findElement(By.xpath("//input[@id='appsearch']")).sendKeys(appName); // Search an app
		elementClick(driver.findElement(By.xpath("//div[contains(@class,'osjs-panel-my-home')]//i")));
		elementClick(
				driver.findElement(By.xpath("//div[text()='" + appName + "']/ancestor::div[@class='app app-item']")));
		return wait
				.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(
						"//div[@id='NavigationHeaderContainer_tempId']//span[contains(text(),'" + tabName + "')]"))))
				.getText();

	}

	// Open left nav bar for any app
	public static void openSideNavBar(String menuName) {
		elementClick((driver.findElement(
				By.xpath("//div[contains(@id,'left-navigation')]//button[contains(@class,'sidenav-toggle')]"))));

		waitForVisibility(driver.findElement(By.xpath("//nav[contains(@class,'sidenav---expanded')]")));
		elementClick((driver.findElement(
				By.xpath("//*[contains(@name,'" + menuName + "') and contains(text(),'" + menuName + "')]"))));
	}

	// select function

	public static void selectItemFromDropdown(String dropdownItem, String dropdownMenuItem) {
		SupportUtils.waitFor(1500);
		elementClick(driver.findElement(By.xpath("//*[contains(text(),'" + dropdownItem
				+ "')]/..//div[contains(@class,'form-control ui fluid selection dropdown')]")));
		String temp = "";
		for (char c : dropdownMenuItem.toCharArray()) {
			if (c == ' ') {
				SupportUtils.waitFor(600);
			}
			temp += c;
			SupportUtils.waitFor(200);
			enterText(driver.findElement(By.xpath(
					"//label[contains(text(),'" + dropdownItem + "')]/..//div[contains(@class,'choices')]//input")),
					String.valueOf(c));

		}
		enterKey(
				driver.findElement(By.xpath(
						"//label[contains(text(),'" + dropdownItem + "')]/..//div[contains(@class,'choices')]//input")),
				Keys.ENTER);

	}

	// input functions
	public static void addTextToTheInputField(String inputItem, String inputValue) {

		enterText(driver.findElement(By.xpath("//*[contains(text(),'" + inputItem + "')]/..//input[@type='text']")),
				inputValue);
	}

	public static void addEmailToTheInputField(String inputItem, String inputValue) {

		enterText(driver.findElement(By.xpath("//*[contains(text(),'" + inputItem + "')]/..//input[@type='email']")),
				inputValue);
	}

	// input textarea fields -- updated on 01-07-2025
	public static void addTextToTheInputArea(String inputTitle, String inputValue) {

		try {
			// 1. Find the label by text
			WebElement label = driver.findElement(By.xpath("//label[normalize-space(text())='" + inputTitle + "']"));

			// 2. Traverse to the parent container, then find iframe inside it
			WebElement container = label.findElement(By.xpath("./ancestor::div[contains(@class,'form-group')]"));
			WebElement iframe = container.findElement(By.xpath(".//iframe"));

			// 3. Switch to iframe
			driver.switchTo().frame(iframe);

			// 4. Set content inside the iframe's body using JavaScript
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String script = "var p = document.body.querySelector('p');"
					+ "if (!p) { p = document.createElement('p'); document.body.appendChild(p); }"
					+ "p.textContent = arguments[0];";
			js.executeScript(script, inputValue);

			// 5. Switch back to main content
			driver.switchTo().defaultContent();

		} catch (NoSuchElementException e) {
			enterText(
					driver.findElement(By.xpath(
							"//*[contains(text(),'" + inputTitle + "')]/..//div[contains(@class,'ck-content')]")),
					inputValue);
		} catch (Exception e) {
			System.err.println("Unexpected error setting iframe content.");
			e.printStackTrace();
		}
	}

	// input textarea fields helpdesk -- updated on 01-07-2025
	public static void addTextToTheInputArea(String inputTitle, String inputValue, Boolean isCollapsible) {
		if (isCollapsible) {

		}

		try {
			// 1. Find the label by text
			WebElement iframe = driver
					.findElement(By.xpath("//span[normalize-space() = '" + inputTitle + "']/../..//iframe"));

			// 3. Switch to iframe
			driver.switchTo().frame(iframe);

			// 4. Set content inside the iframe's body using JavaScript
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String script = "var p = document.body.querySelector('p');"
					+ "if (!p) { p = document.createElement('p'); document.body.appendChild(p); }"
					+ "p.textContent = arguments[0];";
			js.executeScript(script, inputValue);

			// 5. Switch back to main content
			driver.switchTo().defaultContent();

		} catch (NoSuchElementException e) {
			enterText(
					driver.findElement(By.xpath(
							"//*[contains(text(),'" + inputTitle + "')]/..//div[contains(@class,'ck-content')]")),
					inputValue);
		} catch (Exception e) {
			System.err.println("Unexpected error setting iframe content.");
			e.printStackTrace();
		}
	}

	// Double click method with explicit wait
	public static void elementDoubleClick(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
		Actions action = new Actions(driver);
		action.moveToElement(element).doubleClick().perform();
	}
	// App level special functions - like observer , esign etc

	// Select multiple observers
	public static void selectUsers(String userRole, String userName) {
		CommonFunctionUtils.elementClick(driver
				.findElement(By.xpath("//*[contains(text(),'" + userRole + "')]/..//div[contains(@class,'choices')]")));
		CommonFunctionUtils.enterText(
				driver.findElement(By.xpath(
						"//label[contains(text(),'" + userRole + "')]/..//div[contains(@class,'choices')]//input")),
				userName);
		CommonFunctionUtils.waitForVisibility(driver.findElement(By.xpath("//label[contains(text(),'" + userRole
				+ "')]/..//div[contains(@id,'choices')]//*[text()='" + userName + "']")));
		SupportUtils.waitFor(1000);
		CommonFunctionUtils.enterKey(
				driver.findElement(By.xpath(
						"//label[contains(text(),'" + userRole + "')]/..//div[contains(@class,'choices')]//input")),
				Keys.ENTER);

	}

	// Draw signature on signature pane
	public static void drawSignature() {
		WebElement canvas = driver.findElement(By.xpath("//canvas[@class='signature-pad-canvas']"));
		Actions drawAction = new Actions(driver);
		int startX = 50; // Starting X-coordinate (relative to canvas left edge)
		int startY = 50; // Starting Y-coordinate (relative to canvas top edge)

		// Define points to simulate drawing a letter 'S' or a squiggly line
		int[][] path = { { startX, startY }, // Start point
				{ startX + 80, startY - 20 }, // Move up-right
				{ startX + 60, startY - 50 }, // Move down-right
				{ startX + 40, startY + 40 }, // Move down-left
				{ startX + 70, startY + 20 }, // Move up-right again
				{ startX + 100, startY + 50 } // Finish point
		};

		// Move to the starting point and click and hold
		drawAction.moveToElement(canvas, path[0][0], path[0][1]).clickAndHold().pause(100); // Small pause after
																							// holding, crucial for some
																							// canvases

		// Iterate through the path points to simulate drawing a line
		for (int i = 1; i < path.length; i++) {
			// moveByOffset is relative to the *current* mouse position
			// So, we need to calculate the offset from the previous point
			int currentX = path[i][0];
			int currentY = path[i][1];
			int prevX = path[i - 1][0];
			int prevY = path[i - 1][1];

			int offsetX = currentX - prevX;
			int offsetY = currentY - prevY;

			drawAction.moveByOffset(offsetX, offsetY).pause(50); // Pause between each segment to simulate drawing speed
		}

		// Release the mouse button to finish drawing
		drawAction.release().perform();

		System.out.println("Signature drawing attempt completed.");
	}

	// Upload any file to any application - 28-06-2025
	public static void excelUpload(String labelName, String userFilePath) {

		SupportUtils.uploadFile(escapeForXPathLiteral(labelName), userFilePath, driver);
	}

	// Internal purpose for string

	private static String escapeForXPathLiteral(String input) {
		if (input == null) {
			return "''"; // XPath empty string
		}

		boolean hasSingleQuote = input.contains("'");
		boolean hasDoubleQuote = input.contains("\"");

		if (hasSingleQuote && hasDoubleQuote) {
			// Use XPath concat() if both quotes are present
			StringBuilder result = new StringBuilder("concat(");
			String[] parts = input.split("\"", -1); // split at each double quote

			for (int i = 0; i < parts.length; i++) {
				if (i > 0)
					result.append(", '\"', ");
				result.append("'").append(parts[i].replace("'", "''")).append("'");
			}
			result.append(")");
			return result.toString();
		} else if (hasSingleQuote) {
			return "\"" + input + "\"";
		} else {
			return "'" + input + "'";
		}
	}

	// This is for listview purpose - 08-07-2025


/**
     * Searches for a record in a list view and performs an action on it. This
     * method locates the list view by its ID, searches for the record by its name,
     * and performs the specified action (click or double-click) on the record.
     * 
     * @param recordName (the name of the record to search for)
     * @param actionType ("edit", "delete", "doubleClick")
     * @throws NoSuchElementException if the record is not found.
     * @author harshakr
     */
    public static void searchAndSelectListViewRecord(String recordName, String action, 
            String actionType) {
        try {
            WebElement listViewEle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".oxGrid")));
            WebElement searchInput = listViewEle.findElement(By.cssSelector(".grid-serach-input"));

            searchInput.clear();
            searchInput.sendKeys(recordName);

            // Wait for the record to appear
            By recordLocator = By.xpath("//tr[@data-grid-row-index='0']//td[contains(text(),'" + recordName + "')]");
            WebElement record = wait.until(ExpectedConditions.visibilityOfElementLocated(recordLocator));

            Actions actions = new Actions(driver);
            actions.moveToElement(record).perform(); // Hover over the record

            switch (actionType) {
                case "click":
                    clickListViewActionButton(action); // Custom method to click the action button
                    break;

                case "doubleClick":
                    elementDoubleClick(record); // Double click the record
                    break;

                default:
                    System.out.println("Invalid action type specified: " + actionType);
            }

        } catch (NoSuchElementException e) {
            System.out.println("No record found with name: " + recordName);
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for record: " + recordName);
        } catch (StaleElementReferenceException e) {
            System.out.println("Stale element reference while searching for record: " + recordName);
        } catch (ElementNotInteractableException e) {
            System.out.println("Element not interactable while searching for record: " + recordName);
        } catch (UnsupportedOperationException e) {
            System.out.println("Unsupported operation while searching for record: " + recordName);
        } catch (Exception e) {
            System.out.println("Error while searching for record: " + e.getMessage());
        }
    }

    /**
     * Clicks an action button in the list view. This method first checks if the
     * "Show more options" button is displayed, clicks it if available, and then
     * clicks the specified action button. If the "Show more options" button is not
     * displayed, it directly attempts to click the specified action button.
     * 
     * @param actionButtonName (the name of the action button to click like "Edit",
     *                         "Delete", etc.)
     * @throws NoSuchElementException          if the action button is not found.
     * @throws TimeoutException                if the action button is not clickable
     *                                         within the wait time.
     * @throws StaleElementReferenceException  if the action button reference
     *                                         becomes stale.
     * @throws ElementNotInteractableException if the action button is not
     *                                         interactable.
     * @throws UnsupportedOperationException   if the operation is not supported.
     * @throws Exception                       for any other unexpected errors.
     * 
     * @author harshakr
     */
    public static void clickListViewActionButton(String actionButtonName) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".actionButtons")));
            List<WebElement> actionButtons = driver.findElements(By.cssSelector(".actionButtons"));

            for (WebElement button : actionButtons) {
                if (button.getDomAttribute("title").contains("Show more options")) {
                    System.out.println("Clicking on 'Show more options' button.");
                    elementClick(button);
                    return;
                }
            }
            System.out.println("No 'Show more options' button found, proceeding to click the action button directly.");

            // Find and click the desired action button
            elementClick(driver.findElement(By.xpath("//button[@title='" + actionButtonName + "' and contains(@class, 'actionButtons')]")));

        } catch (NoSuchElementException e) {
            System.out.println("No action button found in the list view.");
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for record: " + actionButtonName);
        } catch (StaleElementReferenceException e) {
            System.out.println("Stale element reference while getting action button in the list view.");
        } catch (ElementNotInteractableException e) {
            System.out.println("Element not interactable while getting action button in the list view.");
        } catch (UnsupportedOperationException e) {
            System.out.println("Unsupported operation while getting action button in the list view.");
        } catch (Exception e) {
            System.out.println("Error while getting action button in the list view: " + e.getMessage());
        }
    }

	public void navigateViaBreadcrumbTo(String breadcrumbText) {
		try {
			WebElement breadcrumb = driver.findElement(By.xpath(
					"//*[@class='k-breadcrumb-item-text' and normalize-space(text()) = '" + breadcrumbText + "']"));
			wait.until(ExpectedConditions.elementToBeClickable(breadcrumb));
			elementClick(breadcrumb);
		} catch (Exception e) {
			System.out.println("Breadcrumb with text '" + breadcrumbText + "' not found.");
		}
	}
	
	/**
	 * This Feature is for those list views that have a search box for each column.
	 * 
	 * Verifies if a record exists in the list view based on the column name and
	 * record name. This method searches for the record in the first row of the list
	 * view.
	 * 
	 * @param driver     The WebDriver instance used to interact with the web page.
	 * @param columnName The name of the column to search in.
	 * @param recordName The name of the record to search for. If the record is
	 *                   found, it returns true; otherwise, it returns false.
	 * @param actionButtonName Optional name of the action button to perform
	 *  				 on the action button displayed at the end of the record.                  
	 * @return true if the record is found, false otherwise.
	 */
	public static boolean verifyListViewRecordAndAction(WebDriver driver, String columnName, String recordName,
			Optional<String> actionButtonName) {

		try {
			WebElement record = driver.findElement(By.xpath(
					"//th/ancestor::table//tr/td[count(//th[.='" + columnName + "']/preceding-sibling::th)+1]//input"));
			record.sendKeys(recordName);
			CommonFunctionUtils.waitForVisibility(driver.findElement(By.xpath("//tr[@data-grid-row-index='0']")));
			WebElement firstRow = driver
					.findElement(By.xpath("//tr[@data-grid-row-index='0']//td[contains(text(),'" + recordName + "')]"));
			if (firstRow.isDisplayed() && firstRow.getText().contains(recordName)) {
				System.out.println("Record found in the list view: " + recordName);
				if (actionButtonName.isPresent() || actionButtonName.get() != null) {
					WebElement actionButton = firstRow
							.findElement(By.xpath("//tr[@data-grid-row-index='0']//td[contains(text(),'" + recordName
									+ "')]/following-sibling::td/abbr/button/span[.='"+actionButtonName.get()+"']"));
					if (actionButton.getText().contains(actionButtonName.get())) {
						elementClick(actionButton);
						SupportUtils.waitFor(1000); // Wait for any action to complete
					} else {
						System.out.println("Action button '" + actionButtonName.get() + "' not found for the record: "
								+ recordName);
					}
					System.out
							.println("Action '" + actionButtonName.get() + "' performed on the record: " + recordName);
				} else {
					System.out.println("No action specified for the record: " + recordName);
				}
				return true;

			} else {
				System.out.println("Record not found in the list view: " + recordName);
				return false;
			}
		} catch (TimeoutException e) {
			System.out.println("No records found in the list view.");
			return false;
		} catch (NoSuchElementException e) {
			System.out.println("List view is not available or no records found.");
			return false;
		}
	}
	
	

    public static void popupAccept() {
        try {
            WebElement popup = driver.findElement(By.cssSelector(".swal2-show button[style*=\"display: inline-block\"][class*=\"swal2-confirm\"]"));
            if (popup.isDisplayed()) {
                elementClick(popup);
            }
        } catch (NoSuchElementException e) {
            System.out.println("No popup found to dismiss.");
        } catch (Exception e) {
            System.out.println("Error while dismissing popup: " + e.getMessage());
        }
    }
    
    public static void popupDismiss() {
        try {
            WebElement popup = driver.findElement(By.cssSelector(".swal2-show button[style*=\"display: inline-block\"][class*=\"swal2-cancel\"]"));
            if (popup.isDisplayed()) {
                elementClick(popup);
            }
        } catch (NoSuchElementException e) {
            System.out.println("No popup found to dismiss.");
        } catch (Exception e) {
            System.out.println("Error while dismissing popup: " + e.getMessage());
        }
    }
    
    

}
