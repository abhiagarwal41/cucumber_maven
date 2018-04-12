package com.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.runner.SingletonClass;


public abstract class AbstractPage {

	protected static WebDriver driver;
	private static AbstractPage testPage;

	public AbstractPage(WebDriver driver) {
		driver = driver;
	}
	
	public static AbstractPage getTestPage(){
		return testPage;
	}
	
	public abstract WebElement getWebElement(String name);
	
	public static void setTestPage(String pageName){
	    driver = SingletonClass.getInstance().getDriver();
		switch(pageName.toLowerCase()){
		case "loginpage" :  testPage = new LoginPage(driver);
		PageFactory.initElements(SingletonClass.getInstance().getDriver(), LoginPage.class);break;
		case "homepage" :  testPage = new HomePage(driver); 
		PageFactory.initElements(SingletonClass.getInstance().getDriver(), HomePage.class);break;
		default:
			System.out.println("No such page exists: " +pageName );
		}
	}

}
