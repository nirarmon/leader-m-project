package com.leaderm.tests;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.utils.FileUtils;
import junit.framework.SystemTestCase4;

import org.jsystem.webdriver_so.WebDriverSystemObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Cookie;
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

		// TODO here should be a loop for all urls from file
		ArrayList<String> urlList = parser.getUrlList();
		for (String url : urlList) {
			// report.startLevel(url);
			testFiveFieldsSite(url);
			// report.stopLevel();
		}
	}

	private void testFiveFieldsSite(String url) throws Exception {
		try {
			URL urladdr = new URL(url);

			driver.navigate().to(url);
			driver.manage().addCookie(
					new Cookie("Imtest", "Imtest", urladdr.getHost(), null,
							null));
			FiveFieldsLendingPage page = new FiveFieldsLendingPage(driver);
			page.fillDetails("Test", "test@test.com");
			String orderid = page.getOrderId();
			if (orderid != null) {
				report.report(url + " Page is online, " + orderid,
						ReportAttribute.BOLD);
				mailClient.report(url, true, orderid, null, null);
			} else {
				report.report(url + " Could not get Order ID", report.FAIL);
				mailClient.report(url, false, "0", null, null);
			}
		} catch (Exception e) {
			File captureFile = takeScreenshot(url);
			// String ex = e.getMessage().toString();
			// report.report(ex, report.FAIL);
			report.report("Error in " + url, report.FAIL);
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
