package SeleniumVsPlayWright;

import java.io.File;
import java.time.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v131.network.Network;
import org.openqa.selenium.devtools.v131.performance.Performance;
import org.openqa.selenium.devtools.v131.performance.model.Metric;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumTesting {

	private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

	public static WebDriver getDriver(String browser) {
		if (driverThread.get() == null) {
			WebDriver driver = null;

			switch (browser.toLowerCase()) {
			case "chrome":
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				break;
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				break;
			case "edge":
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				break;
			default:
				throw new IllegalArgumentException("Unsupported browser: " + browser);
			}

			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driverThread.set(driver);
		}
		return driverThread.get();
	}

	public static void quitDriver() {
		if (driverThread.get() != null) {
			driverThread.get().quit();
			driverThread.remove();
		}
	}

	@SuppressWarnings("unused")
	private static void captureScreenshot(WebDriver driver, String screenshotName) {
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File srcFile = ts.getScreenshotAs(OutputType.FILE);
			File destFile = new File("./screenshots/" + screenshotName);
			FileUtils.copyFile(srcFile, destFile);
			Reporter.log("Screenshot saved to: " + destFile.getAbsolutePath(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loginToOrangeHRM(WebDriver driver, String username, String password) {
		driver.manage().window().maximize();
		driver.get("https://opensource-demo.orangehrmlive.com/");
		driver.findElement(By.cssSelector("input[name='username']")).sendKeys(username);
		driver.findElement(By.cssSelector("input[name='password']")).sendKeys(password);
		driver.findElement(By.cssSelector(".orangehrm-login-button")).click();
	}

	@SuppressWarnings("finally")
	public static long loginTest(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			loginToOrangeHRM(driver, "Admin", "admin123");
			WebElement dashboard = driver.findElement(By.xpath("//h6[text()='Dashboard']"));
			if (dashboard.isDisplayed()) {
				Reporter.log("Test passed successfully.", true);
			}
			
			captureScreenshot(driver, "test-success.png");
            
            
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: Login Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long addItemsToCartTest(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().window().maximize();
			driver.get("https://rahulshettyacademy.com/seleniumPractise/#/");
			String[] itemsNeeded = { "Brocolli", "Cucumber", "Beetroot" };
			List<WebElement> products = driver.findElements(By.cssSelector("h4.product-name"));

			for (int i = 0, j = 0; i < products.size(); i++) {
				String formattedName = products.get(i).getText().split("-")[0].trim();
				if (Arrays.asList(itemsNeeded).contains(formattedName)) {
					driver.findElements(By.xpath("//div[@class='product-action']/button")).get(i).click();
					if (++j == itemsNeeded.length)
						break;
				}
			}

			driver.findElement(By.xpath("//a[@class='cart-icon']/img")).click();
			driver.findElement(By.xpath("//button[contains(text(), 'PROCEED TO CHECKOUT')]")).click();
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);
            
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: Add to Cart Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long handleIFrameWithSelenium(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().window().maximize();
			driver.get("https://jqueryui.com/droppable/");
			driver.switchTo().frame(driver.findElement(By.cssSelector(".demo-frame")));
			new Actions(driver)
					.dragAndDrop(driver.findElement(By.id("draggable")), driver.findElement(By.id("droppable"))).build()
					.perform();
			driver.switchTo().defaultContent();
			
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);
            
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: iFrame Handling Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long navigatePagesWithSelenium(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10000));
			loginToOrangeHRM(driver, "Admin", "admin123");

			WebElement menu = driver.findElement(By.xpath("//span[text()='Admin']"));
			menu.click();
			WebElement subMenu = driver.findElement(By.xpath("//span[text()='User Management ']"));
			subMenu.click();

			WebElement head = driver.findElement(By.xpath("//h6[text()='User Management']"));
			if (head.isDisplayed()) {
				Reporter.log("Test passed successfully.", true);
			}
			
			captureScreenshot(driver, "test-success.png");
            
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: Navigation Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long alertHandleWithSelenium(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			Alert alert;
			String text = "Jagan";
			driver.manage().window().maximize();
			driver.get("https://rahulshettyacademy.com/AutomationPractice/");
			driver.findElement(By.id("name")).sendKeys(text);
			driver.findElement(By.id("alertbtn")).click();
			alert = driver.switchTo().alert();
			System.out.println(alert.getText());
			alert.accept();

			driver.findElement(By.id("name")).sendKeys(text);
			driver.findElement(By.id("confirmbtn")).click();
			alert = driver.switchTo().alert();
			System.out.println(alert.getText());
			alert.accept();
			
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);
            
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: Alert Handle Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long invalidUserNameTestWithSelenium(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().window().maximize();
			loginToOrangeHRM(driver, "AdminTest", "admin123");
			String error = driver.findElement(By.cssSelector(".oxd-alert-content-text")).getText();

			Assert.assertTrue(error.equalsIgnoreCase("Invalid Credentials"));
			
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);
            
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: InvalidUserName Test Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long invalidPasswordTestWithSelenium(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().window().maximize();
			loginToOrangeHRM(driver, "Admin", "admin1234");
			String error = driver.findElement(By.cssSelector(".oxd-alert-content-text")).getText();

			Assert.assertTrue(error.equalsIgnoreCase("Invalid Credentials"));
			
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: InvalidPassword Test Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long blankCredentialsTestWithSelenium(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().window().maximize();
			loginToOrangeHRM(driver, " ", " ");
			String error = driver.findElements(By.cssSelector(".oxd-input-field-error-message")).get(0).getText();

			Assert.assertTrue(error.equalsIgnoreCase("Required"));
			
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);
            
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: Blank Credentials Test Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long specialCharactersLoginTestWithSelenium(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().window().maximize();
			loginToOrangeHRM(driver, "#$%^&", "%^&*(");
			String error = driver.findElement(By.cssSelector(".oxd-alert-content-text")).getText();

			Assert.assertTrue(error.equalsIgnoreCase("Invalid Credentials"));
			
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: specialCharactersLogin Test Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long networkInterceptionTest(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			DevTools devTools = ((HasDevTools) driver).getDevTools();
			devTools.createSession();
			devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

			devTools.addListener(Network.requestWillBeSent(), request -> {
				System.out.println("Request URL: " + request.getRequest().getUrl());
			});

			// Navigate to a URL
			driver.get("https://rahulshettyacademy.com/seleniumPractise/#/");
			
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);

		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: networkInterception Test Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long shadowDOMHandelingTest(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			driver.get("https://books-pwakit.appspot.com/");
			System.out.println("Page Loaded: " + driver.getTitle());

			// Locate the shadow host element (book-app)
			WebElement shadowHost = driver.findElement(By.cssSelector("book-app[apptitle='BOOKS']"));
			System.out.println("Shadow Host Located: " + shadowHost);

			// Use JavascriptExecutor to get the shadow root
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement shadowRoot = (WebElement) js.executeScript("return arguments[0].shadowRoot", shadowHost);
			System.out.println("Shadow Root Retrieved: " + shadowRoot);

			// Wait until the input inside the shadow DOM is present
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement inputField = wait
					.until(ExpectedConditions.visibilityOf(shadowRoot.findElement(By.cssSelector("#input"))));
			System.out.println("Input Field Located inside Shadow DOM.");

			// Interact with the input element inside the shadow DOM
			inputField.sendKeys("Testing Books");

			// Optionally, access another element inside the shadow DOM
			WebElement description = shadowRoot.findElement(By.cssSelector(".books-desc"));
			System.out.println("Description: " + description.getText());

			System.out.println("Shadow DOM Test: Success");
			
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);

		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: shadowDOMHandeling Test Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long BrowserContextsTest(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			// Open the first tab
			driver.get("https://opensource-demo.orangehrmlive.com/");
			System.out.println("Tab 1 Title: " + driver.getTitle());

			// Open a new tab (or window)
			((ChromeDriver) driver)
					.executeScript("window.open('https://rahulshettyacademy.com/AutomationPractice/', '_blank');");

			// Switch to the second tab
			Set<String> handles = driver.getWindowHandles();
			String secondTab = handles.stream().skip(1).findFirst().get(); // Get the second handle
			driver.switchTo().window(secondTab);
			System.out.println("Tab 2 Title: " + driver.getTitle());

			// Perform actions in the second tab
			WebElement element = driver.findElement(By.tagName("h1"));
			System.out.println("Header in Tab 2: " + element.getText());

			// Switch back to the first tab
			String firstTab = handles.iterator().next();
			driver.switchTo().window(firstTab);
			System.out.println("Switched back to Tab 1");
			
			captureScreenshot(driver, "test-success.png");
            Reporter.log("Test passed successfully.", true);

		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: BrowserContextsTest Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long PerformanceTest(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			// Enable DevTools
			DevTools devTools = ((HasDevTools) driver).getDevTools();
			devTools.createSession();

			// Enable Performance Monitoring
			devTools.send(Performance.enable(java.util.Optional.empty()));

			// Navigate to a URL
			driver.get("https://example.com");

			// Fetch Performance Metrics
			List<Metric> metrics = devTools.send(Performance.getMetrics());
			System.out.println("Performance Metrics:");
			for (Metric metric : metrics) {
				System.out.println(metric.getName() + ": " + metric.getValue());
			}

			captureScreenshot(driver, "test-success.png");
			Reporter.log("Test passed successfully.", true);
			
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: PerformanceTest Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long ReportingComparision(String browser) {
		WebDriver driver = getDriver(browser);
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().window().maximize();
			driver.get("https://jqueryui.com/droppable/");

			// Interact with the page
			WebElement iframe = driver.findElement(By.cssSelector("iframe.demo-frame"));
			driver.switchTo().frame(iframe);

			WebElement draggable = driver.findElement(By.id("draggable"));
			WebElement droppable = driver.findElement(By.id("droppable"));

			// Perform drag and drop action
			Actions action = new Actions(driver);
			// new org.openqa.selenium.interactions.Actions(driver).dragAndDrop(draggable,
			// droppable).build().perform();
			action.dragAndDrop(draggable, droppable).build().perform();

			// Capture screenshot if needed
			captureScreenshot(driver, "test-success.png");
			Reporter.log("Test passed and drag-and-drop performed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println(e.getMessage());
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}

	}

	@SuppressWarnings("finally")
	public static long MobileEmulation(String browser) {
		long startTime = System.currentTimeMillis();
		WebDriver driver = null;

		try {
			switch (browser.toLowerCase()) {
			case "chrome":
				// Mobile emulation settings for Chrome
				Map<String, String> chromeEmulation = new HashMap<>();
				chromeEmulation.put("deviceName", "iPhone X");

				// ChromeOptions for mobile emulation
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.setExperimentalOption("mobileEmulation", chromeEmulation);

				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver(chromeOptions);
				break;

			case "firefox":
				// Note: Firefox does not have built-in mobile emulation.
				// You can set user agent and viewport size manually for Firefox
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver(); // Default desktop view
				System.out.println("Firefox does not support built-in mobile emulation. Proceeding in desktop mode.");
				break;

			case "edge":
				// Mobile emulation settings for Edge
				Map<String, String> edgeEmulation = new HashMap<>();
				edgeEmulation.put("deviceName", "iPhone X");

				// EdgeOptions for mobile emulation
				EdgeOptions edgeOptions = new EdgeOptions();
				edgeOptions.setExperimentalOption("mobileEmulation", edgeEmulation);

				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver(edgeOptions);
				break;

			default:
				System.out.println("Unsupported browser: " + browser);
			}

			// Navigate to a mobile-responsive website
			driver.get("https://opensource-demo.orangehrmlive.com/");

			// Print the page title to verify navigation
			System.out.println("Page Title: " + driver.getTitle());
			// Capture screenshot if needed
			captureScreenshot(driver, "test-success.png");
			Reporter.log("Test passed successfully.", true);

		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: MobileEmulation Test Failed");
		} finally {
			// Close the browser if initialized
			if (driver != null) {
				driver.quit();
			}
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long CookieLocalStorage(String browser) {
		long startTime = System.currentTimeMillis();
		WebDriver driver = getDriver(browser);

		try {
			// Navigate to the website
			driver.get("https://opensource-demo.orangehrmlive.com/");

			// Set a cookie
			Cookie cookie = new Cookie("testCookie", "cookieValue123");
			driver.manage().addCookie(cookie);
			System.out.println("Cookie added: " + cookie);

			// Retrieve all cookies
			for (Cookie c : driver.manage().getCookies()) {
				System.out.println("Cookie: " + c.getName() + " = " + c.getValue());
			}

			// Delete a specific cookie
			driver.manage().deleteCookieNamed("testCookie");
			System.out.println("Cookie 'testCookie' deleted");

			// Retrieve Local Storage using JavaScriptExecutor
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("localStorage.setItem('key', 'value');");
			String localStorageValue = (String) js.executeScript("return localStorage.getItem('key');");
			System.out.println("Local Storage Value: " + localStorageValue);

			// Remove Local Storage
			js.executeScript("localStorage.removeItem('key');");
			System.out.println(
					"Local Storage Value after deletion: " + js.executeScript("return localStorage.getItem('key');"));

			captureScreenshot(driver, "test-success.png");
			Reporter.log("Test passed successfully.", true);

		} catch (Exception e) {
			captureScreenshot(driver, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
			System.out.println("Selenium: PerformanceTest Failed");
		} finally {
			quitDriver();
			return System.currentTimeMillis() - startTime;
		}
	}

}
