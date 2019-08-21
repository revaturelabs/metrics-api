package dev.dickinson.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dev.dickinson.page.BucketHomePage;
import dev.dickinson.runners.BucketRunner;
import junit.framework.Assert;

public class ViewReportStep {
	// Run the test on Junit4!~!~!~!
	public static BucketHomePage homepage = BucketRunner.homepage;
	public static WebDriver driver = BucketRunner.driver;

	@When("^The user clicks on Select Project$")
	public void the_user_clicks_on_Select_Project() throws Throwable {
		homepage.viewProject.click();
		homepage.viewProjectSelector.click();
	}
	@When("^The user clicks on Select Iteration$")
	public void the_user_clicks_on_Select_Iteration() throws Throwable {
		homepage.viewIteration.click();
		homepage.viewIterationSelector.click();
	}


	@Then("^The user should be on the iterationView$")
	public void the_use_should_be_on_the_iterationView() throws Throwable {

		boolean page = false;
		try{
			driver.findElement(By.xpath("//*[@id=\"iterationView\"]/div[1]"));
			Thread.sleep(1000);
			page = true;
		}catch (NoSuchElementException exception) {
			page = false;
		}
		Assert.assertEquals(true, page);
	}

}
