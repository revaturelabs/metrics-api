package dev.dickinson.runners;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import dev.dickinson.page.BucketHomePage;


@RunWith(Cucumber.class)
@CucumberOptions(features= "src/test/resources",glue ="dev.dickinson.steps")
public class BucketRunner {

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
