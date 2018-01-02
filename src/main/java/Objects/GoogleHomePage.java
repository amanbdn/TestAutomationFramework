package Objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.aventstack.extentreports.ExtentTest;


import framework.FrameworkFunctions;


public class GoogleHomePage {
	WebDriver driver;
	public ExtentTest testReport;
	
	FrameworkFunctions func;
	
	public GoogleHomePage(WebDriver driver,ExtentTest testReport) 
	{           
		this.driver = driver; 
		this.testReport=testReport;
		func=new FrameworkFunctions(this.driver,this.testReport);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath="//*[@id='lst-ib']")
	private static WebElement GoogleSearchBox;
	
	
	@FindBy(xpath="//input[@value='Google Search']")
	private static WebElement SearchButton;
	
	
	public void setGoogleSearch(String searchText)
	{
		GoogleSearchBox.sendKeys(searchText);
		func.logResult("Search Text:"+searchText+" should be entered in Search Box", "Search Text:"+searchText+" is entered in Search Box", "Pass","Google Search Text");
	}
	
	public void clickGoogleSearch()
	{
		SearchButton.click();
		func.logResult("Search Button should be Clicked", "Search Button is Clicked", "Pass","Google Search Button");
	}
	
}
