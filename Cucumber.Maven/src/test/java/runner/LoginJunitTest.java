package com.runner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.pages.HomePage;
import com.pages.LoginPage;

public class LoginTeestCases {
	
	public static WebDriver driver;
	static HomePage homePage;
	static LoginPage loginPage;
	
	@BeforeClass
	public static void initialize(){
		driver = SingletonClass.getInstance().getDriver();
		homePage= PageFactory.initElements(driver, HomePage.class);
		loginPage= PageFactory.initElements(driver, LoginPage.class);
	}
	
	@Test
	public void logIn(){
		homePage.signin_button.click();
		SingletonClass.waitForLoad(driver);
		loginPage.login_wordpress("guest", "12345");
		SingletonClass.waitForLoad(driver);
		Assert.assertEquals("Invalid email address.", loginPage.getErrorMessage());
	}
	
	@AfterClass
	public static void tearDown(){
		SingletonClass.getInstance().stopApplication();
	}

}
