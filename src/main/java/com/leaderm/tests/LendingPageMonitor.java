package com.leaderm.tests;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import junit.framework.SystemTestCase4;

import org.jsystem.webdriver_so.WebDriverSystemObject;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Cookie;
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
			report.startLevel(url);
			testFiveFieldsSite(url);
			report.stopLevel();
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
			report.report(url + " Was Ok");
		} catch (Exception e) {
			File capture = takeScreenshot(url);
			String ex = e.getMessage().toString();
			report.report(ex, report.FAIL);
			report.addLink("Click Here for Screenshot",
					capture.getAbsolutePath());

		}
	}

	protected File takeScreenshot(String title) throws Exception {
		File capture = new File(report.getCurrentTestFolder()+"/"+driver.getTitle() + ".png");
		BufferedImage image = new Robot().createScreenCapture(new Rectangle(
				Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", capture);
		return capture;
	}
}
