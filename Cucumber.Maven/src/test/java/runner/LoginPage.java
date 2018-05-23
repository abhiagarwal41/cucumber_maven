package com.pages;

import static org.openqa.selenium.By.cssSelector;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.runner.SingletonClass;

public class LoginPage extends AbstractPage {

	
	@FindBy(id = "email")
	@CacheLookup
	public static WebElement email;

	@FindBy(id = "passwd")
	@CacheLookup
	public static WebElement password;

	@FindBy(how = How.XPATH, using = ".//*[@id='SubmitLogin']")
	@CacheLookup
	public static WebElement submit_button;

	@FindBy(how = How.XPATH, using = ".//*[@id='center_column']/div[1]/ol/li")
	@CacheLookup
	public static WebElement error_message;

	public WebElement getWebElement(String name) {
		/*
		 * String cssselector=""; switch(name.toUpperCase()){ case "EMAIL" :
		 * cssselector= "#email"; break; case "PASSWORD" : cssselector=
		 * "#passwd"; break; case "SUBMIT BUTTON" : cssselector= "#SubmitLogin";
		 * break; case "ERROR MESSAGE" : cssselector=
		 * "#center_column div.alert ol li"; break; default: throw new
		 * NoSuchElementException(name); } return
		 * SingletonClass.getInstance().getDriver().findElement(By.cssSelector(
		 * cssselector));
		 */

		switch (name.toUpperCase()) {
		case "EMAIL": 
			return email;
		case "PASSWORD":
			return password;
		case "SUBMIT BUTTON":
			return submit_button;
		case "ERROR MESSAGE":
			return error_message;
		default:
			throw new NoSuchElementException(name);
		}
	}

	public void login_wordpress(String uid, String pass) {
		email.sendKeys(uid);
		password.sendKeys(pass);
		submit_button.click();
	}

	public String getErrorMessage() {
		return error_message.getText();
	}

}
