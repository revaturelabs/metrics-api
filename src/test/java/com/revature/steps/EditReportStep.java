package com.revature.steps;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.revature.page.BucketHomePage;
import com.revature.runners.BucketRunner;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EditReportStep {

	// Run the test on Junit4 ONLY!~!~!~!
	public static BucketHomePage homepage = BucketRunner.homepage;
	public static WebDriver driver = BucketRunner.driver;

	@When("^The user selects a project$")
	public void the_user_selects_a_project() throws Throwable {
		Thread.sleep(500);
		homepage.editProject.click();
		Thread.sleep(500);
		homepage.editProjectSelector.click();
	}

	@When("^The user selects an iteration$")
	public void the_user_selects_an_iteration() throws Throwable {
		Thread.sleep(500);
		homepage.editIteration.click();
		Thread.sleep(500);
		homepage.editIterationSelector.click();
	}

	@When("^The user removes a file$")
	public void the_user_removes_a_file() throws Throwable {
		Thread.sleep(500);
		homepage.deleteFile.click();
		Thread.sleep(500);
	}

	@When("^The user adds a file$")
	public void the_user_adds_a_file() throws Throwable {
	   Thread.sleep(500);
	   File file = new File("test.txt");
	   homepage.editAddFile.sendKeys(file.getAbsolutePath().toString());
	   Thread.sleep(500);
	}

	@Then("^The user clicks on update file$")
	public void the_user_clicks_on_update_file() throws Throwable {
		Thread.sleep(500);
		homepage.editUpdate.click();
	}

	@Then("^The user clicks on delete Iteration$")
	public void the_user_clicks_on_delete_Iteration() throws Throwable {
		Thread.sleep(2500);
		homepage.remove.click();
		Thread.sleep(2500);
		homepage.yes.click();
		Thread.sleep(2500);
	}
}
