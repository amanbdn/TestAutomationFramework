package Objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

import framework.FrameworkFunctions;


public class GmailLogin {
	WebDriver driver;
	WebDriverWait wait;
	public ExtentTest testReport;
	
	FrameworkFunctions func;
	
	public GmailLogin(WebDriver driver,ExtentTest testReport) 
	{           
		this.driver = driver; 
		this.testReport=testReport;
		func=new FrameworkFunctions(this.driver,this.testReport);
	    wait=new WebDriverWait(driver,10);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath="//input[@type='email']")
	private static WebElement GmailUserName;
	
	@FindBy(xpath="//input[@type='password']")
	private static WebElement GmailPassword;
	
	@FindBy(xpath="//div[normalize-space(.)='Wrong password. Try again or click Forgot password to reset it.']")
	private static WebElement GmailInvalidLoginMessage;
	
	@FindBy(xpath="//a[normalize-space(.)='Sign In']")
	private static WebElement GmailSignInLink;
	
	@FindBy(xpath="//span[normalize-space(.)='Next']")
	private static WebElement GmailNextButton;
	
	public void setGmailUserName(String UserName)
	{
		try
		{
			wait.until(ExpectedConditions.visibilityOf(GmailUserName));
			GmailUserName.sendKeys(UserName);
			func.logResult("Gmail UserName: "+UserName+" should be entered in Username box", "Gmail UserName: "+UserName+" is entered in UserName Box", "Pass","UserName Entered");
		}
		catch(Exception e)
		{
			func.logResult("Gmail UserName: "+UserName+" should be entered in Username box", "UserName Box is not visible", "Fail","UserName Entered");
			e.printStackTrace();
			Assert.fail();
		}
		
	}
	
	public void setGmailPassword(String Password)
	{
		try
		{
			wait.until(ExpectedConditions.visibilityOf(GmailPassword));
			GmailPassword.sendKeys(Password);
			func.logResult("Gmail Password: "+Password+" should be entered in Password box", "Gmail Password: "+Password+" is entered in Password Box", "Pass","Password Entered");
		}
		catch(Exception e)
		{
			func.logResult("Gmail Password: "+Password+" should be entered in Password box", "Password Box is not displayed", "Fail","Password Entered");
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	public void clickGmailNextButton()
	{
		try
		{
			wait.until(ExpectedConditions.visibilityOf(GmailNextButton));
			GmailNextButton.click();
			func.logResult("Next Button should be Clicked", "Next Button is Clicked", "Pass","Next Button");
		}
		catch(Exception e)
		{
			func.logResult("Next Button should be Clicked", "Next Button is not displayed", "Fail","Next Button");
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	public void clickGmailSignInLink()
	{
		try
		{
			wait.until(ExpectedConditions.visibilityOf(GmailSignInLink));
			GmailSignInLink.click();
			func.logResult("SignIn Link should be Clicked", "SignIn Link is Clicked", "Pass","SignIn Link");
		}
		catch(Exception e)
		{
			func.logResult("SignIn Link should be Clicked", "SignIn Link is not displayed", "Fail","SignIn Link");
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	public void validateErrorMessage()
	{
		
		try
		{
			if (GmailInvalidLoginMessage.isDisplayed())
			{
				func.logResult("Error Message: 'Wrong password. Try again or click Forgot password to reset it.' should be displayed", "Error Message is displayed as expected", "Pass","Error Message");
			}
		}
		catch(Exception e)
		{
			func.logResult("Error Message: 'Wrong password. Try again or click Forgot password to reset it.' should be displayed", "Error Message is not displayed.", "Fail","Error Message");
			e.printStackTrace();
			Assert.fail();
		}
	}
	
}
