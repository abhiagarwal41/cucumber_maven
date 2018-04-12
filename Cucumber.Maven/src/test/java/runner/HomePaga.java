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

public class HomePage extends AbstractPage {
	

	public HomePage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(how=How.XPATH,using="//a[@title='Women']") 
	@CacheLookup
	public static WebElement women_tab; 

	@FindBy(how=How.XPATH,using="//a[@title='Dresses']")
	@CacheLookup
	public static WebElement dresses_tab;

	@FindBy(how=How.CLASS_NAME,using="login")
	@CacheLookup
	public static WebElement signin_button;
    
	public WebElement getWebElement(String name){
		/*String cssselector="";
		switch(name.toUpperCase()){
		case "SIGN IN BUTTON" : cssselector= "a[class='login']"; break;
		case "WOMEN TAB" : cssselector= "a[title='Women']"; break;
		case "DRESSES TAB" : cssselector= "a[title='Dresses']"; break;
		default:
			throw new NoSuchElementException(name);
		}
		return SingletonClass.getInstance().getDriver().findElement(By.cssSelector(cssselector));*/

		switch(name.toUpperCase()){
		case "SIGN IN BUTTON" : return signin_button;
		case "WOMEN TAB" : return women_tab;
		case "DRESSES TAB" : return dresses_tab;
		default:
			throw new NoSuchElementException(name);
		}
	}
	
	// getter for elments in case we do not want them as public
	public WebElement getWomenTab(){
		return women_tab;
	}
}
