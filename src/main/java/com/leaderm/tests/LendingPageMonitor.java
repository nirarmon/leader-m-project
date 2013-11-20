package com.leaderm.tests;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.utils.FileUtils;
import junit.framework.SystemTestCase4;

import org.jsystem.webdriver_so.WebDriverSystemObject;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.leaderm.infra.FileParser;
import com.leaderm.infra.FiveFieldsLendingPage;

public class LendingPageMonitor extends SystemTestCase4 {

	WebDriverSystemObject webDriverSystemObject;
	WebDriver driver;
	FileParser parser;

	@Before
	public void setUp() throws Exception {
		webDriverSystemObject = (WebDriverSystemObject) system
				.getSystemObject("webDriver");
		driver = webDriverSystemObject.getDriver();
		parser = (FileParser) system.getSystemObject("parser");
	}

	@Test
	public void runTestOnSite() throws Exception {

		// TODO here should be a loop for all urls from file
		ArrayList<String> urlList = parser.getUrlList();
		for (String url : urlList) {
			//report.startLevel(url);
			testFiveFieldsSite(url);
			//report.stopLevel();
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
				report.report(url + " Page is online, " + orderid,ReportAttribute.BOLD);
			} else {
				report.report(url + " Could not get Order ID", report.FAIL);
			}
		} catch (Exception e) {
			String captureFileName = takeScreenshot(url);
			//String ex = e.getMessage().toString();
			//report.report(ex, report.FAIL);
			report.report("Error in "+url,report.FAIL);
			report.addLink("Click Here for Screenshot", captureFileName);

		}
	}

	protected String takeScreenshot(String title) throws Exception {
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy
		// somewhere
		File capture = new File(report.getCurrentTestFolder() + "/"
				+ driver.getTitle().replace("!","").replace("|","") + ".png");
		FileUtils.copyFile(scrFile, capture);
		// BufferedImage image = new Robot().createScreenCapture(new Rectangle(
		// Toolkit.getDefaultToolkit().getScreenSize()));
		// ImageIO.write(image, "png", capture);
		return capture.getName();
	}
}
