package framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


public class FrameworkFunctions {
	
	WebDriver driver;
	HashMap<String,String> objectsMap;
	
	public static String TestName;
	public static FileInputStream inputStream;
	public Workbook wb;
	public Sheet sh;
	public Row nextRow;
	public Cell nextCellTestName;
	public Cell nextCellTestRun;
	
	public static ExtentReports extent;
	public static ExtentHtmlReporter htmlReporter;
	public ExtentTest testReport;
	
	String FolderName;
	
	public FrameworkFunctions(String TestName,String ExcelFileName,String SheetName) throws IOException
	{
			FrameworkFunctions.TestName=TestName;
			String ExcelFilePath=System.getProperty("user.dir")+File.separator+"src"+File.separator
				   	  +"main"+File.separator+"resources"+File.separator+"TestData"+
				   	  File.separator+ExcelFileName;	
			inputStream=new FileInputStream(new File(ExcelFilePath));
			wb=new XSSFWorkbook(inputStream);
			sh=wb.getSheet(SheetName);
			FrameworkFunctions.fnCreateFolder(FrameworkFunctions.TestName);
			
	}
	
	public FrameworkFunctions()
	{
		
	}
	
	public FrameworkFunctions(WebDriver driver,ExtentTest testReport)
	{
		this.driver=driver;
		this.testReport=testReport;
	}
	
	public ExtentReports InitializeReport(String ResultsFile)
	{	
		String filePath = System.getProperty("user.dir")+File.separator+"src"+File.separator
			   	  +"main"+File.separator+"resources"+File.separator+"Results"+
			   	  File.separator+ResultsFile+".html";
		extent = new ExtentReports();
		 // Provide the Report Path
		htmlReporter = new ExtentHtmlReporter(filePath);
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name", "Aman Gupta");
		extent.setSystemInfo("Env", "POC");
		extent.setSystemInfo("User", "Aman Gupta");
		
		return FrameworkFunctions.extent;	
	}
	
	public void logResult(String ExpectedResult,String ActualResult,String TCStatus,String ScreenshotName)
	{
		TCStatus=TCStatus.toUpperCase();
		
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		Date date = new Date();
		ScreenshotName=ScreenshotName+"_"+dateFormat.format(date);
	
		//Screenshot Path
		String extentReportImage =System.getProperty("user.dir")+File.separator+"src" +File.separator+"main"+File.separator+"resources"+File.separator+"Screenshots"+File.separator+FrameworkFunctions.TestName+File.separator+ScreenshotName+".png";
		
		// Take screenshot and store as a file format
		File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try 
		{
			// now copy the screenshot to desired location using copyFile method
			FileUtils.copyFile(src, new File(extentReportImage));
			switch(TCStatus)
			{
				case "PASS":
					testReport.log(Status.PASS,"Expected Result: '"+ExpectedResult+"' ActualResult: '"+ActualResult+"'",MediaEntityBuilder.createScreenCaptureFromPath(extentReportImage).build());
					break;
				case "FAIL":
					testReport.log(Status.FAIL,"Expected Result: '"+ExpectedResult+"' ActualResult: '"+ActualResult+"'",MediaEntityBuilder.createScreenCaptureFromPath(extentReportImage).build());
					break;
				case "INFO":
					testReport.log(Status.INFO,"Expected Result: '"+ExpectedResult+"' ActualResult: '"+ActualResult+"'");
					break;
				case "WARNING":
					testReport.log(Status.WARNING,"Expected Result: '"+ExpectedResult+"' ActualResult: '"+ActualResult+"'");
					break;
				case "ERROR":
					testReport.log(Status.ERROR,"Expected Result: '"+ExpectedResult+"' ActualResult: '"+ActualResult+"'");
					break;
				case "SKIP":
					testReport.log(Status.SKIP,"Expected Result: '"+ExpectedResult+"' ActualResult: '"+ActualResult+"'");
					break;
				case "FATAL":	
					testReport.log(Status.FATAL,"Expected Result: '"+ExpectedResult+"' ActualResult: '"+ActualResult+"'");
					break;
				default:
					break;
			}
		} 
		catch (IOException e)
		{
			System.out.println("Error in Reporting Method" + e.getMessage());
		}
	}
	public int fnReturnMatchingRowNumber() throws FileNotFoundException,IOException
	{
		int rowNum=-1;
		Iterator<Row> row=sh.iterator();
		while(row.hasNext())
		{
			 nextRow = row.next();
			 Iterator<Cell> cell=nextRow.cellIterator();
			 nextCellTestName=cell.next();
			 nextCellTestRun=cell.next();
			 if (nextCellTestName.getStringCellValue().equalsIgnoreCase(TestName))
			 {
				 rowNum= nextRow.getRowNum();
				 break;
			 } 
		}
		return rowNum;
	}
	
	public HashMap<String,String> fnGetCurrentRowData() throws FileNotFoundException,IOException
	{
		HashMap<String,String> map=new HashMap<String,String>();
		int RowNum=fnReturnMatchingRowNumber();
		try
		{
			int ColumnCount=sh.getRow(0).getPhysicalNumberOfCells();
			for (int i=0;i<ColumnCount;i++)
			{
				map.put(sh.getRow(0).getCell(i).toString(),sh.getRow(RowNum).getCell(i).toString());
			}	
		}catch(Exception e)
		{
			System.out.println("Test Name Doesn't Exist OR Test Run is Disabled");
		}
		return map;
	}

	public WebDriver InitializeDriverInstance(String browser,String execType,ExtentTest testReport)
	{
		this.testReport=testReport;
		String browserType=execType+"_"+browser;
		browserType=browserType.toUpperCase();
		
		switch(browserType)
		{
				case "SINGLENODE_INTERNETEXPLORER":
					
						System.setProperty("webdriver.ie.driver",System.getProperty("user.dir")+File.separator+"src"+File.separator
							   	  +"main"+File.separator+"resources"+File.separator+"Drivers"+
							   	  File.separator+"IEDriverServer.exe");
						try
						{
							driver=new InternetExplorerDriver();
							break;
						}
						catch(Exception e)
						{
							e.printStackTrace();
							driver.quit();
						}
						
				case "SINGLENODE_CHROME":
						System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+File.separator+"src"+File.separator
							   	  +"main"+File.separator+"resources"+File.separator+"Drivers"+
							   	  File.separator+"chromedriver.exe");
						try
						{
							driver=new ChromeDriver();
							break;
						}
						catch(Exception e)
						{
							e.printStackTrace();
							driver.quit();
						}
				case "SINGLENODE_FIREFOX":
						System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+File.separator+"src"+File.separator
							   	  +"main"+File.separator+"resources"+File.separator+"Drivers"+
							   	  File.separator+"geckodriver.exe");
						try
						{
							driver=new FirefoxDriver();
							break;
						}
						catch(Exception e)
						{
							e.printStackTrace();
							driver.quit();
						}
				case "SINGLENODE_MICROSOFTEDGE":
						System.setProperty("webdriver.edge.driver",System.getProperty("user.dir")+File.separator+"src"+File.separator
							   	  +"main"+File.separator+"resources"+File.separator+"Drivers"+
							   	  File.separator+"MicrosoftWebDriver.exe");
						try
						{
							driver=new EdgeDriver();
							break;
						}
						catch(Exception e)
						{
							e.printStackTrace();
							driver.quit();
						}
				case "MULTIPLENODES_INTERNETEXPLORER":
						break;
				case "MULTIPLENODES_CHROME":
						break;
				case "MULTIPLENODES_FIREFOX":
						break;
				case "MULTIPLENODES_MICROSOFTEDGE":
						break;
				default:
						
		}
		logResult(browser+" Driver Should be Initialized",browser+" is Initialized as expected","Pass","DriverInitialization");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}
	
	public void gotoURL(String URL)
	{
		try
		{
			driver.manage().window().maximize();
			driver.get(URL);
			logResult("URL:"+URL+" Should be Launched","URL:"+URL+" is Launched","Pass","Launch URL");
		}
		catch(Exception e)
		{
			logResult("URL:"+URL+" Should be Launched","URL:"+URL+" is not Launched","Fail","Launch URL");
			e.printStackTrace();
			driver.quit();
		}
	}
	
	public static void fnCreateFolder(String FolderName)
	{
		File file = new File(System.getProperty("user.dir")+File.separator+"src" +File.separator+"main"+File.separator+"resources"+File.separator+"Screenshots"+File.separator+FolderName);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }		
	}
}
