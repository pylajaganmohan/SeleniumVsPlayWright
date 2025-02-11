package SeleniumVsPlayWright;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.microsoft.playwright.*;

public class PlaywrightTest {

	@SuppressWarnings("finally")
	public static long loginToOrangeHRM() {
		Playwright playwright = Playwright.create();
		Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		BrowserContext context = browser.newContext();
		Page page = context.newPage();
		long startTime = System.currentTimeMillis();
		try {
			page.navigate("https://opensource-demo.orangehrmlive.com/");
			page.locator("input[name='username']").fill("Admin");
			page.locator("input[name='password']").fill("admin123");
			page.locator(".orangehrm-login-button").click();
			Locator dashboard = page.locator("h6:has-text('Dashboard')");
			if (dashboard.isVisible()) {
				System.out.println("Playwright: Login Successful");
			}
		} catch (Exception e) {
			System.out.println("Playwright: Login Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			browser.close();
			playwright.close();
			return endTime - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long addItemsToCart() {
		Playwright playwright = Playwright.create();
		Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		BrowserContext context = browser.newContext();
		Page page = context.newPage();
		long startTime = System.currentTimeMillis(); // Start time measurement

		try {
			page.navigate("https://rahulshettyacademy.com/seleniumPractise/#/");

			// Wait for the page to load and the products to be visible
			page.waitForSelector("h4.product-name", new Page.WaitForSelectorOptions().setTimeout(5000));

			String[] itemsNeeded = { "Brocolli", "Cucumber", "Beetroot" };
			int j = 0;

			List<Locator> products = page.locator("h4.product-name").all();

			for (int i = 0; i < products.size(); i++) {
				String productName = products.get(i).textContent().split("-")[0].trim();
				List<String> itemNeededList = Arrays.asList(itemsNeeded);

				if (itemNeededList.contains(productName)) {
					j++;
					page.locator("//div[@class='product-action']/button").nth(i).click();
					if (j == itemsNeeded.length) {
						break;
					}
				}
			}

			// Wait for the cart icon to be visible and proceed to checkout
			page.locator("//a[@class='cart-icon']/img").click();
			page.waitForSelector("//button[contains(text(), 'PROCEED TO CHECKOUT')]",
					new Page.WaitForSelectorOptions().setTimeout(5000));
			page.locator("//button[contains(text(), 'PROCEED TO CHECKOUT')]").click();

			System.out.println("Playwright: Add to Cart Success");
			// Uncomment if needed for the next step (Place Order)
			// page.locator("//button[contains(text(), 'Place Order')]").click();

		} catch (Exception e) {
			System.out.println("Playwright: Add to Cart Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			browser.close();
			playwright.close();
			return endTime - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long handleIFrameWithPlaywright() {
		Playwright playwright = Playwright.create();
		Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		BrowserContext context = browser.newContext();
		Page page = context.newPage();
		long startTime = System.currentTimeMillis();
		try {
			page.navigate("https://jqueryui.com/droppable/");

			// Switch to the iframe using the frame locator
			FrameLocator iframe = page.frameLocator("iframe.demo-frame");

			// Find source and target elements inside the iframe
			Locator source = iframe.locator("#draggable");
			Locator target = iframe.locator("#droppable");

			// Perform drag-and-drop action inside the iframe
			source.dragTo(target);

		} catch (Exception e) {
			System.out.println("Playwright: iFrame Handling Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			browser.close();
			playwright.close();
			return endTime - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long navigatePagesWithPlaywright() {
		Playwright playwright = Playwright.create();
		Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		BrowserContext context = browser.newContext();
		Page page = context.newPage();
		long startTime = System.currentTimeMillis();
		try {
			page.navigate("https://opensource-demo.orangehrmlive.com/");
			page.locator("input[name='username']").fill("Admin");
			page.locator("input[name='password']").fill("admin123");
			page.locator(".orangehrm-login-button").click();
			page.locator("span:has-text('Admin')").click();
			page.locator("span:has-text('User Management ')").click();
			
			Locator head = page.locator("h6:has-text('User Management')");
			if (head.isVisible()) {
				System.out.println("Playwright: Navigation Successful");
			}
		} catch (Exception e) {
			System.out.println("Playwright: Navigation Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			browser.close();
			playwright.close();
			return endTime - startTime;
		}
	}
	
	@SuppressWarnings("finally")
	public static long alertWithPlaywright() {
		Playwright playwright = Playwright.create();
		Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		BrowserContext context = browser.newContext();
		Page page = context.newPage();
		long startTime = System.currentTimeMillis();
		try {
			page.navigate("https://opensource-demo.orangehrmlive.com/");
			page.locator("input[name='username']").fill("Admin");
			page.locator("input[name='password']").fill("admin123");
			page.locator(".orangehrm-login-button").click();
			page.locator("span:has-text('Admin')").click();
			page.locator("span:has-text('User Management ')").click();
			
			Locator head = page.locator("h6:has-text('User Management')");
			if (head.isVisible()) {
				System.out.println("Playwright: Navigation Successful");
			}
		} catch (Exception e) {
			System.out.println("Playwright: Navigation Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			browser.close();
			playwright.close();
			return endTime - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long fillFormWithPlaywright() {
		Playwright playwright = Playwright.create();
		Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		BrowserContext context = browser.newContext();
		Page page = context.newPage();
		long startTime = System.currentTimeMillis();
		try {
			page.navigate("https://opensource-demo.orangehrmlive.com/");
			page.locator("input[name='username']").fill("Admin");
			page.locator("input[name='password']").fill("admin123");
			page.locator(".orangehrm-login-button").click();

			// Navigate to form page (after login)
			page.locator("a:has-text('Admin')").click();
			page.locator("#btnAdd").click();

			page.locator("#systemUser_userType").fill("Admin");
			page.locator("#systemUser_employeeName_empName").fill("Test User");
		} catch (Exception e) {
			System.out.println("Playwright: Form Filling Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			browser.close();
			playwright.close();
			return endTime - startTime;
		}
	}
}
