package stepDefinition;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en_lol.WEN;
import junit.framework.Assert;

public class FacebookTest extends AbstractStepDefinition{
	
	WebDriver driver=getDriver();
	
	@Before
	public void testSetup(){
		//System.setProperty("webdriver.chrome.driver", "./libs/chromedriver.exe");
		//driver=new ChromeDriver();
		System.out.println("new scenario");
	}
	
	@After
	public void testShutdown(){
		//driver.quit();
	}
	
	@Before("@web")
	public void testSetupWeb(){
	 System.out.println("in before testSetupWeb ");
	}
	
	@After("@web")
	public void testShutdownWeb(){
		System.out.println("in after testShutdownWeb ");
	}
	
	@Given("^Start Application$")
	public void Start_Application() throws Throwable {
		
		driver.get("http://www.facebook.com");
	   
	}

	@When("^I enter invalid username and password$")
	public void i_enter_invalid_username_and_password(DataTable table) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    // For automatic transformation, change DataTable to one of
	    // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
	    // E,K,V must be a scalar (String, Integer, Date, enum etc)
		List<List<String>> data = table.raw();
		driver.findElement(By.id("email")).sendKeys(data.get(1).get(1));
		driver.findElement(By.id("pass")).sendKeys(data.get(2).get(1));
	    
	}

	@When("^Click on LogIn button$")
	public void click_on_LogIn_button() throws Throwable {
		driver.findElement(By.id("u_0_q")).click();   
	}

	@Then("^User should not be able to log in$")
	public void user_should_not_be_able_to_log_in() throws Throwable {
		Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Recover Your Account')]")).isDisplayed());
	   
	}
	
	@When("^I click on ([^\"]*)$")
	public void i_click_on_link(String link) throws Throwable {
		String xpath = "//a[contains(text(),'"+link+"')]";
		driver.findElement(By.xpath(xpath)).click();
	}
	
	@Then("^I check I am on ([^\"]*)$")
	public void i_check_I_am_on_title(String title) throws Throwable {
	   Assert.assertTrue(driver.getTitle().contains(title));
	}

	@Then("^I close the browser$")
	public void i_close_the_browser() throws Throwable {
	  driver.quit();
	}

}
