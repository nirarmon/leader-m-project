package com.leaderm.infra;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsystem.utils.RandomUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.google.common.io.PatternFilenameFilter;

public class FiveFieldsLendingPage extends AbstractPage {

	@FindBy(id = "fname")
	WebElement nameField;

	@FindBy(id = "age")
	WebElement ageSelect;

	@FindBy(id = "phone")
	WebElement phoneField;

	@FindBy(id = "email")
	WebElement emailField;

	@FindBy(id = "townselect")
	WebElement townSelect;

	// *[@id="form-wrap"]/form/div[6]/input
	@FindBy(xpath = "//input[@type=\"submit\"]")
	WebElement submitBtn;

	public FiveFieldsLendingPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);

	}

	/**
	 * This function will fill the user details without age and town (both will
	 * be left as default values)
	 * 
	 * @param name
	 * @param phone
	 * @param email
	 * @throws Exception
	 */
	public boolean fillDetails(String name, String email) throws Exception {
		nameField.sendKeys(name);
		phoneField.sendKeys(createRandomPhoneNumber());
		emailField.sendKeys(email);
		// select the first town by default
		Select select = new Select(townSelect);
		select.selectByIndex(3);
		// Select the first age
		select = new Select(ageSelect);
		select.selectByIndex(1);
		// submit form
		submitBtn.submit();
		try {
			driver.findElement(By.id("thankyou"));
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public String getOrderId() throws Exception {
		String currentURL = driver.getCurrentUrl();
		Pattern p = Pattern.compile(".*(orderid=\\d*).*");
		Matcher m = p.matcher(currentURL);
		if (m.find()) {
			return m.group(1);
		} else {
			return null;
		}

	}

	/**
	 * this function creates a random number with 10 digits
	 * 
	 * @return
	 */
	private String createRandomPhoneNumber() {
		int[] array = RandomUtils.getSeveralRandomInts(0, 9, 10, new Random());
		StringBuffer buffer = new StringBuffer();
		for (int i : array) {
			buffer.append("" + i);
		}
		return buffer.toString();

	}

}
