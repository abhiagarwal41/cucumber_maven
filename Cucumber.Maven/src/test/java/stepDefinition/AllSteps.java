package com.steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import junit.framework.Assert;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pages.AbstractPage;
import com.runner.SingletonClass;

import cucumber.api.java.en.Given;

public class AllSteps extends AbstractStep{
	public static WebDriver driver;
	
	@Given("^I start application$")
	public void iStartApp() throws Throwable {
		driver = SingletonClass.getInstance().getDriver();
	}

	@When("^I click on \"(.*?)\"$")
	public void iClickOn(String elementName) throws Throwable {
		AbstractPage.getTestPage().getWebElement(elementName).click();
	}

	@SuppressWarnings("deprecation")
	@Then("^\"(.*?)\" should be visible$")
	public void shouldBeVisible(String elementName) throws Throwable {
		Assert.assertEquals(true,AbstractPage.getTestPage().getWebElement(elementName).isDisplayed());
	}
	
	@Given("^Wait until \"(.*?)\" is visible$")
	public void waitUntilIsVisible(String elementName) throws Throwable {
		(new WebDriverWait(SingletonClass.getInstance().getDriver(), 10))
		  .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(getCssSelector(elementName))));
	}
    
	@Then("Wait for \"(.*?)\" to load$")
	public void waitForPageLoad(String pageName) throws Throwable {
		SingletonClass.waitForLoad(driver);
		AbstractPage.setTestPage(pageName);
	}
	
	@Then("Provide \"(.*?)\" text input as \"(.*?)\"$")
	public void provideTextInput(String elementName, String value) throws Throwable {
		AbstractPage.getTestPage().getWebElement(elementName).sendKeys(value);
	}
	
	public String getCssSelector(String elementName) throws IOException{
		 File src=new File("./objectRepository.properties");
		  FileInputStream fis=new FileInputStream(src);
		  Properties pro=new Properties();
		  pro.load(fis);
		  return pro.getProperty(elementName);
	}

}
