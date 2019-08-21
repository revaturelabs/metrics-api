package dev.dickinson.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BucketHomePage {
	WebDriver driver;

	public BucketHomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(name="ViewReports")
	public WebElement viewReport;

	@FindBy(name="UploadReport")
	public WebElement uploadReport;

	@FindBy(name="UploadPage")
	public WebElement uploadPage;

	@FindBy(name="EditReport")
	public WebElement editReport;

	@FindBy(id="iterationView")
	public WebElement iterationView;

	@FindBy(id="ProjectSelector")
	public WebElement viewProject;

	@FindBy(xpath = "//*[@id=\"view-reports\"]/app-view-reports/div/div/div/div/div/div[4]/p")
	public WebElement viewProjectSelector;

	@FindBy(id="ChosenIteration")
	public WebElement viewIteration;

	@FindBy(xpath = "//*[@id=\"view-reports\"]/app-view-reports/div/div/div/div[2]/div/div/p")
	public WebElement viewIterationSelector;

	@FindBy(id = "start-date")
	public WebElement startDate;

	@FindBy(id="end-date")
	public WebElement endDate;


	@FindBy(xpath="//*[@id=\"completed\"]")
	public WebElement completedSPS;

	@FindBy(xpath="//*[@id=\"assigned\"]")
	public WebElement assignedSP;

	@FindBy(xpath="//*[@id=\"mat-chip-list-input-0\"]")
	public WebElement trainer;

	@FindBy(xpath="//*[@id=\"mat-chip-list-input-1\"]")
	public WebElement observer;
	
	@FindBy(id="projectbtn")
	public WebElement project;
	
	@FindBy(xpath="//*[@id=\"upload-reports\"]/app-upload-reports/div/div[3]/div/div/div[1]/div/div[4]/p")
	public WebElement uploadprojectSelector;

	@FindBy(name="UploadFiles")
	public WebElement uploadFile;

	@FindBy(id="upload")
	public WebElement uploadId;

	@FindBy(name = "IterationPlace")
	public WebElement iteration;

	@FindBy(id="submitbtn")
	public WebElement submit;

	@FindBy(id="project")
	public WebElement editProject;
	
	@FindBy(xpath="//*[@id=\"edit-reports\"]/app-edit-reports/div[1]/div/div/div[1]/div/div[4]/p")
	public WebElement editProjectSelector;

	@FindBy(id="iterations")
	public WebElement editIteration;
	
	@FindBy(xpath="//*[@id=\"edit-reports\"]/app-edit-reports/div[1]/div/div/div[2]/div/div[1]/p")
	public WebElement editIterationSelector;
	
	@FindBy(xpath="//*[@id=\"remove\"]")
	public WebElement deleteFile;

	@FindBy(id="update")
	public WebElement editUpdate;

	@FindBy(id="remove")
	public WebElement remove;

	@FindBy(xpath = "//*[@id=\"edit-reports\"]/app-edit-reports/div[3]/div/div/button[1]")
	public WebElement yes;
}
