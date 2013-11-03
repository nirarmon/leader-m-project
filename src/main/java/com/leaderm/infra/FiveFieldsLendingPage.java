package com.leaderm.infra;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

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

	//*[@id="form-wrap"]/form/div[6]/input
	@FindBy( xpath= "//input[@type=\"submit\"]")
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
	 */
	public boolean fillDetails(String name, String phone, String email) {
		nameField.sendKeys(name);
		phoneField.sendKeys(phone);
		emailField.sendKeys(email);
		//select the first town by default
		Select select = new Select(townSelect);
		select.selectByIndex(1);
		//Select the first age
		select = new Select(ageSelect);
		select.selectByIndex(1);
		// submit form
		submitBtn.submit();
		try {
			driver.findElement(By.id("thankyou"));
			return true;
		} catch (ElementNotFoundException e) {
			throw e;
		}
	}

}
