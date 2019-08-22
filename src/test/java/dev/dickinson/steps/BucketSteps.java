package dev.dickinson.steps;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dev.dickinson.page.BucketHomePage;
import dev.dickinson.runners.BucketRunner;
import junit.framework.Assert;

public class BucketSteps {
	
	// Run the test on Junit4!~!~!~!
	public static BucketHomePage homepage = BucketRunner.homepage;
	public static WebDriver driver = BucketRunner.driver;

	//Change the localhost for new user.
	@Given("^The user is on the Sprint Reports home page$")
	public void the_user_is_on_the_Sprint_Reports_home_page() throws Throwable {
		driver.get("localhost:4200");
	}

	@When("^The user clicks on Upload Report$")
	public void the_user_clicks_on_Upload_Report() throws Throwable {
		homepage.uploadReport.click();
	}

	@Then("^The user should be on the Upload Report page$")
	public void the_user_should_be_on_the_Upload_Report_page() throws Throwable {
		boolean page = false;
		try{
			WebElement startdate = driver.findElement(By.name("UploadPage"));
			page = true;
		}catch (NoSuchElementException exception) {
			page = false;
		}

		Assert.assertEquals(true, page);

	}

	@When("^The user clicks on Edit Report$")
	public void the_user_clicks_on_Edit_Report() throws Throwable {
		homepage.editReport.click();
	}

	@Then("^The user should be on the Edit Report page$")
	public void the_user_should_be_on_the_Edit_Report_page() throws Throwable {
		boolean page = false;
		try{
			WebElement startdate = driver.findElement(By.name("editPage"));
			page = true;
		}catch (NoSuchElementException exception) {
			page = false;
		}

		Assert.assertEquals(true, page);

	}

	@When("^The user clicks on View Reports$")
	public void the_user_clicks_on_View_Reports() throws Throwable {
		homepage.viewReport.click();
	}

	@Then("^The user should be on the View Reports page$")
	public void the_user_should_be_on_the_View_Reports_page() throws Throwable {
		boolean page = false;
		try{
			WebElement startdate = driver.findElement(By.name("viewPage"));
			page = true;
		}catch (NoSuchElementException exception) {
			page = false;
		}

		Assert.assertEquals(true, page);

	}

}
