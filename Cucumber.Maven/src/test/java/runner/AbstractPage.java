package com.pages;

import static com.runner.SingletonClass.getInstance;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.runner.SingletonClass;


public abstract class AbstractPage {

	private static AbstractPage testPage;
	
	public static AbstractPage getTestPage(){
		return testPage;
	}
	
	public abstract WebElement getWebElement(String name);
	
	public static void setTestPage(String pageName){
	  
		switch(pageName.toLowerCase()){
		case "loginpage" :  testPage = new LoginPage();
		PageFactory.initElements(getInstance().getDriver(), LoginPage.class);break;
		case "homepage" :  testPage = new HomePage(); 
		PageFactory.initElements(getInstance().getDriver(), HomePage.class);break;
		default:
			System.out.println("No such page exists: " +pageName );
		}
	}

}
