package stepDefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SmokeTest {
	
	@Given("^Open firefox and start application$")
	public void open_firefox_and_start_application() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	  System.out.println("open_firefox_and_start_application");
	}

	@When("^I enter valid \"(.*?)\" and \"(.*?)\"$")
	public void i_enter_valid_and(String arg1, String arg2) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("i_enter_valid_and");
	}

	@Then("^user should login successfully$")
	public void user_should_login_successfully() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("user_should_login_successfully");
	}

}
