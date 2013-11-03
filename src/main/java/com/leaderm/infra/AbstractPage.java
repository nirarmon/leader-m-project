package com.leaderm.infra;

import org.openqa.selenium.WebDriver;

public class AbstractPage {

	WebDriver driver;

	public AbstractPage(WebDriver driver) {
		this.driver = driver;
	}
}
