package dev.dickinson.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dev.dickinson.page.BucketHomePage;
import dev.dickinson.runners.BucketRunner;

public class EditReportStep {

	// Run the test on Junit4 ONLY!~!~!~!
	public static BucketHomePage homepage = BucketRunner.homepage;
	public static WebDriver driver = BucketRunner.driver;

	@When("^The user selects a project$")
	public void the_user_selects_a_project() throws Throwable {
		Thread.sleep(1500);
		homepage.editProject.click();
		Thread.sleep(1500);
		homepage.editProjectSelector.click();
	}

	@When("^The user selects an iteration$")
	public void the_user_selects_an_iteration() throws Throwable {
		Thread.sleep(1500);
		homepage.editIteration.click();
		Thread.sleep(1500);
		homepage.editIterationSelector.click();
	}

	@When("^The user removes a file$")
	public void the_user_removes_a_file() throws Throwable {
		Thread.sleep(1500);
		homepage.deleteFile.click();
	}


	@Then("^The user clicks on update file$")
	public void the_user_clicks_on_update_file() throws Throwable {
		Thread.sleep(1500);
		homepage.editUpdate.click();
	}

	@Then("^The user clicks on delete Iteration$")
	public void the_user_clicks_on_delete_Iteration() throws Throwable {
		Thread.sleep(1500);
		homepage.remove.click();
		Thread.sleep(1500);
		homepage.yes.click();
	}
}
