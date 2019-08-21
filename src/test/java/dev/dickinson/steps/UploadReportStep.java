package dev.dickinson.steps;

import java.io.File;

import org.openqa.selenium.WebDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dev.dickinson.page.BucketHomePage;
import dev.dickinson.runners.BucketRunner;

public class UploadReportStep {
	// Run the test on Junit4!~!~!~!
	public static BucketHomePage homepage = BucketRunner.homepage;
	public static WebDriver driver = BucketRunner.driver;

	@When("^The user clicks on the start date$")
	public void the_user_clicks_on_the_start_date() throws Throwable {
		homepage.startDate.sendKeys("10032019");
	}

	@When("^The user clicks on the end date$")
	public void the_user_clicks_on_the_end_date() throws Throwable {
		homepage.endDate.sendKeys("10262019");
	}


	@When("^The user clicks on the completed SP's$")
	public void the_user_clicks_on_the_completed_SP_s() throws Throwable {
		homepage.completedSPS.sendKeys("2");
	}

	@When("^The user clicks on Assigned SP's$")
	public void the_user_clicks_on_Assigned_SP_s() throws Throwable {
		homepage.assignedSP.sendKeys("3");
	}

	@When("^The user clicks on Trainer$")
	public void the_user_clicks_on_Trainer() throws Throwable {
		homepage.trainer.sendKeys("Test Trainer");
	}

	@When("^The user clicks on Observer$")
	public void the_user_clicks_on_Observer() throws Throwable {
		homepage.observer.sendKeys("Test Observer");
	}

	@When("^The user clicks on Project$")
	public void the_user_clicks_on_Project() throws Throwable {
		homepage.project.click();
		Thread.sleep(200);
		homepage.uploadprojectSelector.click();
		homepage.iteration.sendKeys("45");
	}

	@When("^The user clicks on Upload Files$")
	public void the_user_clicks_on_Upload_Files() throws Throwable {
		File file = new File("derserserterstferl.txt");
		homepage.uploadId.sendKeys(file.getAbsoluteFile().toString()); 	
	}

	@Then("^The user clicks on Submit$")
	public void the_user_clicks_on_Submit() throws Throwable {
		Thread.sleep(1000);
		homepage.submit.click();
		Thread.sleep(1000);
	}
}
