import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.pages.AbstractPage;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
//		plugin = { "cucumber.plugin.ExecutionSummaryReport:target/cucumber/summary-report" }, 
		strict = true, features = "src/test/resources/features/basic.feature",
		glue = "com.steps", 
		snippets = SnippetType.CAMELCASE
		)
public class CucumberTest {

	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
	}

	@AfterClass
	public static void afterClass() {
		SingletonClass.getInstance().stopApplication();
	}

}
