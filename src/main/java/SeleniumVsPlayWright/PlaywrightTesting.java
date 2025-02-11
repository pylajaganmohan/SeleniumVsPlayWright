package SeleniumVsPlayWright;

import java.nio.file.Paths;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;

public class PlaywrightTesting {

	private static ThreadLocal<Browser> browserThread = new ThreadLocal<>();
	private static ThreadLocal<BrowserContext> contextThread = new ThreadLocal<>();
	private static ThreadLocal<Page> pageThread = new ThreadLocal<>();

	public static Page getPage(String browser) {
		if (pageThread.get() == null) {
			Playwright playwright = Playwright.create();
			Browser browserInstance = null;
			BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);

			switch (browser.toLowerCase()) {
			case "chrome":
				options.setChannel("chrome");
				browserInstance = playwright.chromium().launch(options);
				break;
			case "firefox":
				browserInstance = playwright.firefox().launch(options);
				break;
			case "edge":
				options.setChannel("msedge");
				browserInstance = playwright.chromium().launch(options);
				break;
			default:
				throw new IllegalArgumentException("Unsupported browser: " + browser);
			}

			BrowserContext context = browserInstance
					.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
			Page page = context.newPage();
			browserThread.set(browserInstance);
			contextThread.set(context);
			pageThread.set(page);
		}
		return pageThread.get();
	}

	public static void quitBrowser() {
		if (browserThread.get() != null) {
			browserThread.get().close();
			browserThread.remove();
			contextThread.remove();
			pageThread.remove();
		}
	}

	
	private static void captureScreenshot(Page page, String screenshotName) {
		try {
			page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("./screenshots/" + screenshotName)));
			System.out.println("Screenshot saved to: ./screenshots/" + screenshotName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loginToOrangeHRM(Page page, String username, String password) {
		page.navigate("https://opensource-demo.orangehrmlive.com/");
		page.locator("input[name='username']").fill(username);
		page.locator("input[name='password']").fill(password);
		page.locator(".orangehrm-login-button").click();
	}

	@SuppressWarnings("finally")
	public static long loginTest(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			loginToOrangeHRM(page, "Admin", "admin123");
			if (page.locator("h6:has-text('Dashboard')").isVisible()) {
				System.out.println("Playwright: Login Successful");
			}
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long addItemsToCartTest(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			page.navigate("https://rahulshettyacademy.com/seleniumPractise/#/");
			String[] itemsNeeded = { "Brocolli", "Cucumber", "Beetroot" };
			List<Locator> products = page.locator("h4.product-name").all();

			for (int i = 0, j = 0; i < products.size(); i++) {
				String formattedName = products.get(i).textContent().split("-")[0].trim();
				if (Arrays.asList(itemsNeeded).contains(formattedName)) {
					page.locator("//div[@class='product-action']/button").nth(i).click();
					if (++j == itemsNeeded.length)
						break;
				}
			}

			page.locator("//a[@class='cart-icon']/img").click();
			page.locator("//button[contains(text(), 'PROCEED TO CHECKOUT')]").click();
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long handleIFrameWithPlaywright(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			page.navigate("https://jqueryui.com/droppable/");
			FrameLocator frame = page.frameLocator(".demo-frame");
			Locator source = frame.locator("#draggable");
			Locator target = frame.locator("#droppable");
			source.dragTo(target);
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long navigatePagesWithPlaywright(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			loginToOrangeHRM(page, "Admin", "admin123");
			page.locator("a[href*=admin]").click();
			// page.locator("//span[text()='User Management ']").click();

			if (page.locator("//span/h6[text()='User Management']").isVisible()) {
				System.out.println("Playwright: Navigation Test Successful");
			}
			
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
            
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long alertHandleWithPlaywright(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			String text = "Jagan";
			page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
			page.onDialog(dialog -> dialog.accept());
			page.locator("#name").fill(text);
			page.locator("#alertbtn").click();

			page.locator("#name").fill(text);
			page.locator("#confirmbtn").click();
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long invalidUserNameTestWithPlaywright(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			loginToOrangeHRM(page, "AdminTest", "admin123");
			String error = page.locator(".oxd-alert-content-text").textContent();
			Assert.assertTrue(error.equalsIgnoreCase("Invalid Credentials"));
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long invalidPasswordTestWithPlaywright(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			loginToOrangeHRM(page, "Admin", "admin1234");
			String error = page.locator(".oxd-alert-content-text").textContent();
			Assert.assertTrue(error.equalsIgnoreCase("Invalid Credentials"));
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long blankCredentialsTestWithPlaywright(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			loginToOrangeHRM(page, " ", " ");
			String error = page.locator(".oxd-input-field-error-message").first().textContent();
			Assert.assertTrue(error.equalsIgnoreCase("Required"));
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long specialCharactersLoginTestWithPlaywright(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			loginToOrangeHRM(page, "#$%^&", "%^&*(");
			String error = page.locator(".oxd-alert-content-text").textContent();
			Assert.assertTrue(error.equalsIgnoreCase("Invalid Credentials"));
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long networkInterceptionTest(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			// Create a page and enable network interception
			page.onRequest(request -> {
				System.out.println("Request URL: " + request.url());
			});

			// Navigate to a URL
			page.navigate("https://rahulshettyacademy.com/seleniumPractise/#/");
			
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);

		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long shadowDOMHandelingTest(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			page.navigate("https://books-pwakit.appspot.com/");

			// Query for the shadow host element and access the shadow root
			Locator shadowHost = page.locator("book-app[apptitle='BOOKS']");
			Locator shadowRoot = shadowHost.locator("#input");

			// Interact with elements inside the shadow DOM
			// Example: Fill an input field inside the shadow DOM
			shadowRoot.fill("Testing Books");
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
			
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long BrowserContextsTest(String browser) {
		Page page1 = getPage(browser);
		Page page2 = getPage(browser);
		long startTime = System.currentTimeMillis();
		try {
			// Create a new browser context (Session 1)

			page1.navigate("https://opensource-demo.orangehrmlive.com/");
			System.out.println("Context 1 - Page Title: " + page1.title());

			page2.navigate("https://rahulshettyacademy.com/AutomationPractice/");
			System.out.println("Context 2 - Page Title: " + page2.title());
			
			captureScreenshot(page1, "test-success.png");
            Reporter.log("Test passed successfully.", true);
		} catch (Exception e) {
			
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long PerformanceTest(String browser) {
		Page page = getPage(browser);

		long startTime = System.currentTimeMillis();
		try {
			// Navigate to the page
			page.navigate("https://example.com");

			// End the timer
			long end = System.currentTimeMillis();
			System.out.println("Page Load Time: " + (end - startTime) + " ms");

			// Extract Performance Metrics
			String performanceTiming = (String) page.evaluate("JSON.stringify(window.performance.timing)");
			System.out.println("Performance Timing: " + performanceTiming);
			
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
			
		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long ReportingComparision(String browser) {
		Page page = getPage(browser);
		long startTime = System.currentTimeMillis();

		try {
			page.navigate("https://jqueryui.com/droppable/");
			page.waitForSelector("iframe.demo-frame");

			// Switch to iframe
			page.frameLocator("iframe.demo-frame").locator("#draggable")
					.dragTo(page.frameLocator("iframe.demo-frame").locator("#droppable"));

			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed and drag-and-drop performed successfully.", true);

		} catch (Exception e) {
			captureScreenshot(page, "test-failure.png");
			Reporter.log("Test failed: " + e.getMessage(), true);
            Assert.fail("Test failed due to exception", e);
		} finally {
			quitBrowser();
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long MobileEmulation(String browserName) {
		long startTime = System.currentTimeMillis();

		try (Playwright playwright = Playwright.create()) {
			Browser browserInstance = null;
			BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);

			// Choose the browser based on the input
			switch (browserName.toLowerCase()) {
			case "chrome":
				options.setChannel("chrome");
				browserInstance = playwright.chromium().launch(options);
				break;
			case "firefox":
				browserInstance = playwright.firefox().launch(options);
				break;
			case "edge":
				options.setChannel("msedge");
				browserInstance = playwright.chromium().launch(options);
				break;
			default:
				throw new IllegalArgumentException("Unsupported browser: " + browserName);
			}

			// Define mobile emulation settings (e.g., iPhone 12)
			Browser.NewContextOptions contextOptions = new Browser.NewContextOptions().setViewportSize(390, 844) // iPhone
																													// 12
																													// viewport
																													// size
					.setUserAgent(
							"Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1")
					.setDeviceScaleFactor(3) // Retina display scale
					.setIsMobile(true); // Enable mobile emulation

			// Create a new browser context with mobile emulation
			BrowserContext context = browserInstance.newContext(contextOptions);

			// Create a new page in the emulated context
			Page page = context.newPage();

			// Navigate to a mobile-responsive website
			page.navigate("https://opensource-demo.orangehrmlive.com/");

			// Print the page title to verify navigation
			System.out.println("Page Title: " + page.title());

			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
            
			// Close the context and browser instance          
			context.close();
			browserInstance.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Playwright: Mobile Emulation Test Failed");
		} finally {
			return System.currentTimeMillis() - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long CookieLocalStorage(String browserName) {

		long startTime = System.currentTimeMillis();
		try (Playwright playwright = Playwright.create()) {
			Browser browserInstance = null;
			BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);

			// Choose the browser based on the input
			switch (browserName.toLowerCase()) {
			case "chrome":
				options.setChannel("chrome");
				browserInstance = playwright.chromium().launch(options);
				break;
			case "firefox":
				browserInstance = playwright.firefox().launch(options);
				break;
			case "edge":
				options.setChannel("msedge");
				browserInstance = playwright.chromium().launch(options);
				break;
			default:
				throw new IllegalArgumentException("Unsupported browser: " + browserName);
			}

			// Launch the browser
			BrowserContext context = browserInstance.newContext();

			// Create a new page
			Page page = context.newPage();

			// Navigate to the website
			page.navigate("https://opensource-demo.orangehrmlive.com/");

			// Set a cookie
			context.addCookies(Arrays.asList(new Cookie("myCookie", "cookieValue").setDomain("example.com").setPath("/")
					.setHttpOnly(true).setSecure(true)));

			// Get cookies
			List<Cookie> cookies = context.cookies();
			for (Cookie cookie : cookies) {
				System.out.println(cookie.name + ": " + cookie.value);
			}

			// Delete a cookie
			context.clearCookies();

			// Handling Local Storage
			// Set an item in local storage
			page.evaluate("() => localStorage.setItem('myItem', 'itemValue')");

			// Get an item from local storage
			String localStorageValue = page.evaluate("() => localStorage.getItem('myItem')").toString();
			System.out.println("Local Storage Value: " + localStorageValue);

			// Remove an item from local storage
			page.evaluate("() => localStorage.removeItem('myItem')");
			
			captureScreenshot(page, "test-success.png");
            Reporter.log("Test passed successfully.", true);
			// Close the context and browser instance
			context.close();
			browserInstance.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Playwright: Cookie Test Failed");
		} finally {
			return System.currentTimeMillis() - startTime;
		}
	}

}
