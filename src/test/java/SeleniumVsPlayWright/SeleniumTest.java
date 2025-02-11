package SeleniumVsPlayWright;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.edge.EdgeDriver;

public class SeleniumTest {
	@SuppressWarnings("finally")
	public static long loginToOrangeHRM() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10000));
			driver.get("https://opensource-demo.orangehrmlive.com/");
			WebElement username = driver.findElement(By.cssSelector("input[name='username']"));
			WebElement password = driver.findElement(By.cssSelector("input[name='password']"));
			WebElement loginButton = driver.findElement(By.cssSelector(".orangehrm-login-button"));
			username.sendKeys("Admin");
			password.sendKeys("admin123");
			loginButton.click();
			// Verify login by checking for the dashboard element
			WebElement dashboard = driver.findElement(By.xpath("//h6[text()='Dashboard']"));
			if (dashboard.isDisplayed()) {
				System.out.println("Selenium: Login Successful");
			}
		} catch (Exception e) {
			System.out.println("Selenium: Login Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			driver.quit();
			return endTime - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long addItemsToCart() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		long startTime = System.currentTimeMillis(); // Start time measurement

		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			driver.get("https://rahulshettyacademy.com/seleniumPractise/#/");

			String[] itemsNeeded = { "Brocolli", "Cucumber", "Beetroot" };
			int j = 0;
			List<WebElement> products = driver.findElements(By.cssSelector("h4.product-name"));

			for (int i = 0; i < products.size(); i++) {
				// Format the product name
				String[] name = products.get(i).getText().split("-");
				String formattedName = name[0].trim();

				// Convert the itemsNeeded array into a list for easy lookup
				List<String> itemNeededList = Arrays.asList(itemsNeeded);

				// Check if the current product is in the itemsNeeded list
				if (itemNeededList.contains(formattedName)) {
					j++;
					// Click the "Add to Cart" button for the item
					driver.findElements(By.xpath("//div[@class='product-action']/button")).get(i).click();
					if (j == itemsNeeded.length) {
						break;
					}
				}
			}

			// Proceed to checkout
			driver.findElement(By.xpath("//a[@class='cart-icon']/img")).click();
			driver.findElement(By.xpath("//button[contains(text(), 'PROCEED TO CHECKOUT')]")).click();

			// driver.findElement(By.linkText("Place Order")).click();

		} catch (Exception e) {
			System.out.println("Selenium: Add to Cart Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			driver.quit();
			return endTime - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long handleIFrameWithSelenium() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		long startTime = System.currentTimeMillis();
		try {
			driver.get("https://jqueryui.com/droppable/");
			driver.switchTo().frame(driver.findElement(By.cssSelector(".demo-frame")));
			WebElement source = driver.findElement(By.id("draggable"));
			WebElement target = driver.findElement(By.id("droppable"));
			Actions a = new Actions(driver);
			a.dragAndDrop(source, target).build().perform();

			driver.switchTo().defaultContent();
		} catch (Exception e) {
			System.out.println("Selenium: iFrame Handling Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			driver.quit();
			return endTime - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long navigatePagesWithSelenium() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		long startTime = System.currentTimeMillis();
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10000));
			driver.get("https://opensource-demo.orangehrmlive.com/");
			WebElement username = driver.findElement(By.cssSelector("input[name='username']"));
			WebElement password = driver.findElement(By.cssSelector("input[name='password']"));
			WebElement loginButton = driver.findElement(By.cssSelector(".orangehrm-login-button"));
			username.sendKeys("Admin");
			password.sendKeys("admin123");
			loginButton.click();

			WebElement menu = driver.findElement(By.xpath("//span[text()='Admin']"));
			menu.click();
			WebElement subMenu = driver.findElement(By.xpath("//span[text()='User Management ']"));
			subMenu.click();

			WebElement head = driver.findElement(By.xpath("//h6[text()='User Management']"));
			if (head.isDisplayed()) {
				System.out.println("Selenium: Navigation Successful");
			}
		} catch (Exception e) {
			System.out.println("Selenium: Navigation Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			driver.quit();
			return endTime - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long alertWithSelenium() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		long startTime = System.currentTimeMillis();
		try {
			Alert alert;
			String text = "Jagan";
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
			Thread.sleep(3000);
			// alert.accept();
			alert.dismiss();
		} catch (Exception e) {
			System.out.println("Selenium: Form Filling Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			driver.quit();
			return endTime - startTime;
		}
	}

	@SuppressWarnings("finally")
	public static long fillFormWithSelenium() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		long startTime = System.currentTimeMillis();
		try {
			driver.get("https://opensource-demo.orangehrmlive.com/");
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5000));
			WebElement username = driver.findElement(By.cssSelector("input[name='username']"));
			WebElement password = driver.findElement(By.cssSelector("input[name='password']"));
			WebElement loginButton = driver.findElement(By.cssSelector(".orangehrm-login-button"));
			username.sendKeys("Admin");
			password.sendKeys("admin123");
			loginButton.click();

			// Navigate to the form (example form after login)
			WebElement menu = driver.findElement(By.xpath("//a[text()='Admin']"));
			menu.click();
			WebElement addUserButton = driver.findElement(By.id("btnAdd"));
			addUserButton.click();

			WebElement userRoleDropdown = driver.findElement(By.id("systemUser_userType"));
			userRoleDropdown.sendKeys("Admin");
			WebElement employeeName = driver.findElement(By.id("systemUser_employeeName_empName"));
			employeeName.sendKeys("Test User");
		} catch (Exception e) {
			System.out.println("Selenium: Form Filling Failed");
		} finally {
			long endTime = System.currentTimeMillis();
			driver.quit();
			return endTime - startTime;
		}
	}
}
