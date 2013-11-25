package com.leaderm.tests;

import java.io.File;
import java.util.ArrayList;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.utils.FileUtils;
import junit.framework.SystemTestCase4;

import org.jsystem.webdriver_so.WebDriverSystemObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.leaderm.infra.FileParser;
import com.leaderm.infra.FiveFieldsLendingPage;
import com.leaderm.infra.mail.MailClient;

public class LendingPageMonitor extends SystemTestCase4 {

	WebDriverSystemObject webDriverSystemObject;
	WebDriver driver;
	FileParser parser;
	MailClient mailClient;

	@Before
	public void setUp() throws Exception {
		webDriverSystemObject = (WebDriverSystemObject) system
				.getSystemObject("webDriver");
		driver = webDriverSystemObject.getDriver();
		parser = (FileParser) system.getSystemObject("parser");
		mailClient = (MailClient) system.getSystemObject("mailClient");
	}

	@Test
	public void runTestOnSite() throws Exception {
		ArrayList<String> urlList = parser.getUrlList();
		//create cookie on lead site
		driver.navigate().to("http://leads.esteticlub.com/set_test_cookie.php?set=on");
		for (String url : urlList) {
			testFiveFieldsSite(url);
		}
		//remove cookie
		driver.navigate().to("http://leads.esteticlub.com/set_test_cookie.php");

	}

	private void testFiveFieldsSite(String url) throws Exception {
		try {
			driver.navigate().to(url+"");
			//navigate to site 
			driver.navigate().to(url);	
			//populate page object
			FiveFieldsLendingPage page = new FiveFieldsLendingPage(driver);
			page.fillDetails("Test", "test@test.com");
			String orderid = page.getOrderId();
			if (orderid != null) {
				report.report(url + " Page is online, " + orderid,
						ReportAttribute.BOLD);
				mailClient.report(url, true, orderid, null, null);
			} else {
				report.report(url + " Could not get Order ID", Reporter.FAIL);
				File captureFile = takeScreenshot(url);
				mailClient.report(url, false, "Error in Order ID", captureFile.getName(), captureFile.getAbsolutePath());
			}
		} catch (Exception e) {
			File captureFile = takeScreenshot(url);
			report.report("Error in " + url, Reporter.FAIL);
			report.addLink("Click Here for Screenshot", captureFile.getName());
			mailClient.report(url, false, "0", captureFile.getName(),
					captureFile.getAbsolutePath());

		}
	}

	protected File takeScreenshot(String title) throws Exception {
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy
		// somewhere
		File capture = new File(report.getCurrentTestFolder() + "/"
				+ driver.getTitle().replace("!", "").replace("|", "") + ".jpg");
		FileUtils.copyFile(scrFile, capture);
		//ImageResizer.resizeImg(scrFile, capture);
		scrFile.delete();
		// FileUtils.copyFile(scrFile, resizedCapture);
		// BufferedImage image = new Robot().createScreenCapture(new Rectangle(
		// Toolkit.getDefaultToolkit().getScreenSize()));
		// ImageIO.write(image, "png", capture);
		return capture;
	}

	@After
	public void sendMail() throws Exception {
		mailClient.sendMail();
	}
}
