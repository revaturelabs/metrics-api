package com.revature.runners;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.revature.page.BucketHomePage;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(features= "src/test/resources",glue ="com.revature.steps")
public class BucketRunner {

	// Run the test on Junit4 ONLY!~!~!~!
	
	public static WebDriver driver;
	public static BucketHomePage homepage;

	static {
		File file = new File("src/main/resources/chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());        
		driver = new ChromeDriver();        
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		homepage = new BucketHomePage(driver);
	}

	@AfterClass
	public static void tearDown() {
		driver.quit();
	}
}
