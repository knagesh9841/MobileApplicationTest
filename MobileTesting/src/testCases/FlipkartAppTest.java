package testCases;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.opencsv.CSVReader;

import utility.*;




public class FlipkartAppTest {
	
	public AppiumDriver<WebElement> driver;
	public WebDriverWait wait;
	
	FileReader reader = null;
	Properties p = null;
	AppiumServerJava appiumServer = new AppiumServerJava();

	SoftAssert softAssertion= new SoftAssert();

	String userName,password,screenshotPath,version,deviceName,platform,uuid;
	
	JavascriptExecutor je = null;

	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	ExtentTest logger;
	
	int port = 4723;




	@BeforeTest
	public void startReport(){
		
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/test-output/FlipkartMobileApplicationTest.html");
		extent = new ExtentReports ();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name","XPANXIONQAEssentials");
		extent.setSystemInfo("Environment", "Automation Testing");
		extent.setSystemInfo("User Name", "Nagesh Mahadev Kadam");
		

		htmlReporter.config().setDocumentTitle("Title of the Report Comes here");
		htmlReporter.config().setReportName("Name of the Report Comes here");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);

	}
	
	
	@AfterTest
	public void endReport(){
		extent.flush();
	}
	
	@AfterMethod
	public void getResult(ITestResult result) throws Exception{
		if(result.getStatus() == ITestResult.FAILURE){
			logger.log(Status.FAIL, "Test Case Failed is "+result.getName());
			logger.log(Status.FAIL, "Test Case Failed is "+result.getThrowable());
			screenshotPath = ScreenshotCapture.getScreenshot(driver, result.getName());
			logger.addScreenCaptureFromPath(screenshotPath);
			
			
		}else if(result.getStatus() == ITestResult.SKIP){
			logger.log(Status.SKIP, "Test Case Skipped is "+result.getName());
		}else if(result.getStatus() == 1)
		{

			logger.log(Status.PASS, MarkupHelper.createLabel("Test Case Passed is "+result.getName()+"", ExtentColor.GREEN));
		
		}
		
	}

	@BeforeClass
	public void setUp() throws MalformedURLException{

		
		if(!appiumServer.checkIfServerIsRunnning(port)) {
			appiumServer.startServer();
		}else 
		{
			Log.info("Appium Server already running on Port - " + port);
		}
		
		DOMConfigurator.configure("log4j.xml");
		
		Log.info("Appium server is Started Successfully");

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("BROWSER_NAME", "Android");
		
		Log.info("Setting Capability");

		capabilities.setCapability("VERSION", "6.0.1"); 
		capabilities.setCapability("deviceName", "Moto G Turbo Edition");
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("udid","ZY22372D29");

		capabilities.setCapability("appPackage", "com.flipkart.android");

		capabilities.setCapability("appActivity","com.flipkart.android.SplashActivity");

		Log.info("Capability set successfully");

		driver = new AndroidDriver<WebElement>(new URL("http://"+AppiumServerJava.ipAddress+":4723/wd/hub"), capabilities);
		

	}

	@Test (priority=1)
	public void loginTestCase() throws Exception {

		Log.startTestCase("Login Testcase Started");
		
		logger = extent.createTest("Login Testcase");
	

		wait = new WebDriverWait(driver, 50);
		WebElement signin = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_SignIn));

		signin.click();

		WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(ObjectRepository.element_Name));

		name.click();
		WebElement noneoftheabove = wait.until(ExpectedConditions.visibilityOfElementLocated(ObjectRepository.element_NoneoftheAbove));

		noneoftheabove.click();

		Log.info("Reading data from File");

		CSVReader reader = new CSVReader(new FileReader(System.getProperty("user.dir")+"\\LoginCredentials.csv"));

		List<String[]> listElement=reader.readAll();

		Iterator<String[]> iterator = listElement.iterator();

		String[] str=iterator.next();

		userName = str[0].trim();
		password = str[1].trim();

		Log.info("Successfully read data from file");

		name.sendKeys(userName);

		WebElement pwd=driver.findElement(ObjectRepository.element_Password);

		pwd.sendKeys(password);

		signin.click();

		Log.info("Clicked on Login Button");

		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(ObjectRepository.element_searchBox));

		Assert.assertTrue(searchBox.isDisplayed());
		
		logger.log(Status.INFO, "Login is Successfull");
		
		screenshotPath = ScreenshotCapture.getScreenshot(driver, "loginTestCase");
		logger.addScreenCaptureFromPath(screenshotPath);
		
		Log.endTestCase("Login Testcase Ended");


	}

	@Test (priority=2)
	public void notificationTabVerification() throws Exception
	{
		Log.startTestCase("Notification Tab Verification Testcase Started");
		
		logger = extent.createTest("Notification Tab Verification Testcase");

		wait = new WebDriverWait(driver, 50);
		WebElement notification = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_Notification));
		notification.click();

		WebElement notify = wait.until(ExpectedConditions.visibilityOfElementLocated(ObjectRepository.element_Notify));

		Assert.assertEquals(notify.getText(), "Notifications");	
		
		logger.log(Status.INFO, "Notification Tab is opened Successfully");
		
		screenshotPath = ScreenshotCapture.getScreenshot(driver, "notificationTabVerification");
		logger.addScreenCaptureFromPath(screenshotPath);

		Log.endTestCase("Notification Tab Verification Testcase Ended");

		WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		
		menu.click();
		wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		menu.click();
		

	}

	@Test (priority=3)
	public void cartTabVerification() throws Exception
	{
		Log.startTestCase("Cart Tab Verification Testcase Started");
		
		logger = extent.createTest("Cart Tab Verification Testcase");

		wait = new WebDriverWait(driver, 50);
		WebElement cartverify = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_CartVerify));
		cartverify.click();
		
		WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(ObjectRepository.element_Cart));

		Assert.assertEquals(cart.getText(), "My Cart");
		
		logger.log(Status.INFO, "Cart Tab is opened Successfully");
		
		screenshotPath = ScreenshotCapture.getScreenshot(driver, "cartTabVerification");
		logger.addScreenCaptureFromPath(screenshotPath);

		Log.endTestCase("Cart Tab Verification Testcase Ended");

		WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		
		menu.click();

	}

	@Test(priority=4)
	public void myFashion() throws Exception
	{
		Log.startTestCase("Fashion Tab Verification Testcase Started");
		
		logger = extent.createTest("Fashion Tab Verification Testcase");

		wait = new WebDriverWait(driver, 30);
		boolean res=false;
		WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		menu.click();
		

		wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_myAcct));
		List<WebElement> myAcct = driver.findElements(ObjectRepository.element_myAcct);
		for(int i=0;i<myAcct.size();i++)
		{
			myAcct.get(i).click();
			

			WebElement myOrder;
			String sValue = "";
			try {
				myOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(ObjectRepository.element_myOrder));
				sValue = myOrder.getText();
			} catch (Exception e) {

			}


			if(sValue.equals("Fashion"))
			{
				res=true;
				break;
			}
			
			menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
			
			menu.click();
			
		}

		
		Assert.assertTrue(res);
		
		screenshotPath = ScreenshotCapture.getScreenshot(driver, "myFashion");
		logger.addScreenCaptureFromPath(screenshotPath);
		
		logger.log(Status.INFO, "Fashion Tab is opened Successfully");
		
		menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		
		menu.click();
		
		menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		
		menu.click();

		Log.endTestCase("Fashion Tab Verification Testcase Ended");

	}

	@Test(priority=5)
	public void verifyElectrMenu () throws Exception
	{

		Log.startTestCase("Electronics Tab Verification Testcase Started");
		
		logger = extent.createTest("Electronics Tab Verification Testcase");

		wait = new WebDriverWait(driver,30);
		boolean res=false;
		WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		menu.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_myAcct));
		List<WebElement> myAcct = driver.findElements(ObjectRepository.element_myAcct);
		for(int i=0;i<myAcct.size();i++)
		{
			myAcct.get(i).click();
			
			WebElement myOrder;
			String sValue = "";
			try {
				myOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(ObjectRepository.element_myOrder));
				sValue = myOrder.getText();
			} catch (Exception e) {

			}


			if(sValue.equals("Electronics"))
			{
				res=true;
				break;
			}
			menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
			
			menu.click();
			
		}

		Assert.assertTrue(res);	
		logger.log(Status.INFO, "Electronics Tab is opened Successfully");


		screenshotPath = ScreenshotCapture.getScreenshot(driver, "verifyElectrMenu");
		logger.addScreenCaptureFromPath(screenshotPath);

		List<WebElement> subMenu = driver.findElements(ObjectRepository.element_subMenu);

		logger.log(Status.INFO, "Following are Submenu under Electronics");

		for(int i=0;i<subMenu.size();i++)
		{
			logger.log(Status.INFO, "SubMenu:-"+subMenu.get(i).getText());
		}
		
		
		menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		
		menu.click();
		
		menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		
		menu.click();
		
		

		Log.endTestCase("Electronics Tab Verification Testcase Ended");

	}

	@Test(priority=6)
	public void verifyHomeandFurniture() throws Exception
	{
		Log.startTestCase("Home and Furniture Tab Verification Testcase Started");
		
		logger = extent.createTest("Home and Furniture Tab Verification Testcase");

		wait = new WebDriverWait(driver, 30);
		boolean res=false;
		WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		menu.click();

		wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		List<WebElement> myAcct = driver.findElements(ObjectRepository.element_myAcct);
		for(int i=0;i<myAcct.size();i++)
		{
			myAcct.get(i).click();
			
			WebElement myOrder;
			String sValue = "";
			try {
				myOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(ObjectRepository.element_myOrder));
				sValue = myOrder.getText();
			} catch (Exception e) {

			}


			if(sValue.equals("Home and Furniture"))
			{
				res=true;
				break;
			}
			menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
			menu.click();
			
		}

		Assert.assertTrue(res);
		
		logger.log(Status.INFO, "Home and Furniture Tab is opened Successfully");
		
		screenshotPath = ScreenshotCapture.getScreenshot(driver, "verifyHomeandFurniture");
		logger.addScreenCaptureFromPath(screenshotPath);
		
		menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		
		menu.click();
		
		menu = wait.until(ExpectedConditions.elementToBeClickable(ObjectRepository.element_menu));
		
		menu.click();
		
		Log.endTestCase("Home and Furniture Tab Verification Testcase Ended");

	}

	@AfterClass
	public void teardown(){

		ClearMemoryOfAppllication.clearAppMemory("com.flipkart.android");
		driver.quit();
		appiumServer.stopServer();
		Log.info("Appium server is Stopped Successfully");
		Log.info("Application is Closed Successfully");
	}
}

