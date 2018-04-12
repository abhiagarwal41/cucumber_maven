package com.runner;

import static org.openqa.selenium.remote.CapabilityType.ACCEPT_SSL_CERTS;
import static org.openqa.selenium.remote.CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR;
import static org.openqa.selenium.remote.CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pages.AbstractPage;

public class SingletonClass {

	private static final SingletonClass INSTANCE = new SingletonClass();
	private boolean isRunning = false;

	protected WebDriver driver;
	private static WebDriverWait webDriverWait;

	public WebDriver getDriver() {
		return driver;
	}

	private SingletonClass() {
		// TODO Auto-generated constructor stub
	}

	public DesiredCapabilities getDesiredCapabilities() {
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(ENSURING_CLEAN_SESSION, true);
		capabilities.setCapability(UNEXPECTED_ALERT_BEHAVIOUR, "ACCEPT");
		capabilities.setCapability(ACCEPT_SSL_CERTS, true);
		capabilities.setCapability("ie.ensureCleanSession", true);
		capabilities.setCapability("requireWindowFocus", true);
		capabilities.setBrowserName("internet explorer");
		capabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL,
				"http://automationpractice.com/index.php");
		return capabilities;
	}

	public void startApplication() {

		/*
		 * final String USERNAME = "abhi_agarwal88"; final String ACCESS_KEY =
		 * "861a63a5-f8cf-4db7-b495-0c5a96ba6816"; final String URL = "https://"
		 * + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";
		 * DesiredCapabilities caps = DesiredCapabilities.chrome();
		 * caps.setCapability("platform", "Windows 7");
		 * caps.setCapability("version", "43.0");
		 * 
		 * 
		 * try { driver = new RemoteWebDriver(new URL(URL), caps); } catch
		 * (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * driver.get("http://automationpractice.com/index.php");
		 */

		DesiredCapabilities capabilities = getDesiredCapabilities();
		System.setProperty("webdriver.ie.driver", "./libs/IEDriverServer_x64_3.0.0/IEDriverServer.exe");
		driver = new InternetExplorerDriver(capabilities);
		driver.manage().window().maximize();
		webDriverWait = new WebDriverWait(driver, 15);

	}

	public static synchronized SingletonClass getInstance() {
		if (!INSTANCE.isRunning) {
			INSTANCE.startApplication();
			INSTANCE.isRunning = true;
		}

		return INSTANCE;
	}

	public void stopApplication() {

		driver.close();
		driver.quit();
		INSTANCE.isRunning = false;
	}

	public WebDriverWait getWebDriverWait() {
		if (driver == null) {
			throw new RuntimeException(
					"'webDriverWait' has not been initialized yet. Please initialize webDriverWait by calling 'open' method");
		}
		return webDriverWait;
	}

	public static void waitForLoad(WebDriver driver) {

		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		webDriverWait.until(pageLoadCondition);

	}

}
