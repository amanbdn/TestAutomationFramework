package Scripts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import Objects.GmailLogin;
import Objects.GoogleHomePage;
import framework.FrameworkFunctions;

public class Script2 {
		
	HashMap<String,String> data=null;
	WebDriver driver;
	
	ExtentReports extent;
	ExtentHtmlReporter htmlReporter;
	ExtentTest testReport;
	
	
	//Objects Declaration
	FrameworkFunctions func;
	GoogleHomePage HomePage;
	GmailLogin Login;
	
	// Variables Declaration
	public static String ExcelFileName="TestData.xlsx";
	public static String SheetName="Google";
	public static String browser="Chrome";
	public static String execType="SingleNode";
	public static String ResultsFile="Google";
	
	@BeforeSuite
	public void fnBeforeSuite()
	{
		func=new FrameworkFunctions();
		extent=func.InitializeReport(ResultsFile);
	}
	
	@BeforeMethod
	public void fnBeforeMethod(Method m) throws FileNotFoundException, IOException
	{
		Test test=m.getAnnotation(Test.class);
		testReport = extent.createTest(test.testName());
		FrameworkFunctions reader=new FrameworkFunctions(test.testName(),ExcelFileName,SheetName);
		//Read Data from Excel and Store the Data in HashMap
		data=reader.fnGetCurrentRowData();
		//Initialize the Driver Instance
		this.driver=func.InitializeDriverInstance(browser, execType,testReport);
		//Initialize Google HomePage Object 
		HomePage=new GoogleHomePage(driver,testReport);
		Login=new GmailLogin(driver,testReport);
	}
	
	
	@Test(enabled=true,testName="LaunchGoogle")
	public void fnTestN1()
	{
		String URL=data.get("URL");
		String SearchText=data.get("SearchText");
		func.gotoURL(URL);
		HomePage.setGoogleSearch(SearchText);
		HomePage.clickGoogleSearch();
	}
	
	@AfterMethod
	public void fnAfterMethod()
	{
		HomePage=null;
		driver.quit();
	}

	@AfterSuite
	public void fnAfterSuite()
	{
		extent.flush();
		func=null;
	}
	
}
