package com.leaderm.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.utils.DateUtils;
import jsystem.utils.FileUtils;
import junit.framework.SystemTestCase4;

import org.jsystem.webdriver_so.WebDriverSystemObject;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.leaderm.infra.FileParser;
import com.leaderm.infra.FiveFieldsLandingPage;
import com.leaderm.infra.StatsReport;
import com.leaderm.infra.mail.MailClient;

public class LandingPageMonitor extends SystemTestCase4 {

	private WebDriverSystemObject webDriverSystemObject;
	private WebDriver driver;
	private FileParser parser;
	private MailClient mailClient;
	private boolean errors = false;

	@Before
	public void setUp() throws Exception {
		webDriverSystemObject = (WebDriverSystemObject) system
				.getSystemObject("webDriver");
		driver = webDriverSystemObject.getDriver();
		parser = (FileParser) system.getSystemObject("parser");
		mailClient = (MailClient) system.getSystemObject("mailClient");
	}

	@Test
	public void getStat() throws Exception {
		ArrayList<String> emailList = parser.getEmailList();
		StatsReport report = new StatsReport(driver);
		report.register();
		for (String email : emailList) {
			report.getStats(email);
		}
		String table = report.getHTMLTable();
		mailClient.sendMail("Leads Summary Report for " + getYesterdayDate(),
				table);
	}
	
	@Test
	public void getAffiliateReport() throws Exception {
		ArrayList<String> partnerList = parser.getPartnerList();
		System.out.println("dsfv");
		StatsReport report = new StatsReport(driver);
		report.register();
		for (String partnerName : partnerList) {
			report.getPartnerStats(partnerName);
		}
		String table = report.getHTMLTable();
		mailClient.sendMail("Affiliate Status Report for " + getTodayDate(),
				table);
	}

	private String getYesterdayDate() throws Exception {
		StringBuffer yesterday = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		yesterday.append(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.append("-");
		yesterday.append(calendar.get(Calendar.MONTH) + 1);
		yesterday.append("-");
		yesterday.append(calendar.get(Calendar.YEAR));
		return yesterday.toString();
	}
	
	private String getTodayDate() throws Exception {
		StringBuffer today = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		today.append(calendar.get(Calendar.DAY_OF_MONTH));
		today.append("-");
		today.append(calendar.get(Calendar.MONTH) + 1);
		today.append("-");
		today.append(calendar.get(Calendar.YEAR));
		return today.toString();
	}

	@Test
	public void runTestOnSite() throws Exception {
		ArrayList<String> urlList = parser.getUrlList();
		// create cookie on lead site
		driver.navigate().to(
				"http://leads.esteticlub.com/set_test_cookie.php?set=on");
		for (String url : urlList) {
			testFiveFieldsSite(url);
		}
		// remove cookie
		driver.navigate().to("http://leads.esteticlub.com/set_test_cookie.php");
		//send mail only if errors were found during the run
		if (errors){
			mailClient.sendMail("Monitor Result for " + DateUtils.getDate());
		}
	}

	private void testFiveFieldsSite(String url) throws Exception {
		try {
			driver.navigate().to(url + "");
			// navigate to site
			driver.navigate().to(url);
			// populate page object
			FiveFieldsLandingPage page = new FiveFieldsLandingPage(driver);
			page.fillDetails("Test", "test@test.com");
			String orderid = page.getOrderId();
			if (orderid != null) {
				report.report(url + " Page is online, " + orderid,
						ReportAttribute.BOLD);
				mailClient.report(url, true, orderid, null, null);
			} else {
				// set errors to true for mail
				errors = true;
				report.report(url + " Could not get Order ID", Reporter.FAIL);
				File captureFile = takeScreenshot(url);
				mailClient.report(url, false, "Error in Order ID",
						captureFile.getName(), captureFile.getAbsolutePath());
			}
		} catch (Exception e) {
			// set errors to true for mail 
			errors = true;
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
		scrFile.delete();
		return capture;
	}

	
}
