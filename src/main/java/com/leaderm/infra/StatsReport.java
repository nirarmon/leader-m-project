package com.leaderm.infra;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class StatsReport extends AbstractPage {

	HashMap<String, HashMap<String, String>> statsFromEmails;
	public String fromDate; // yesterday
	public String toDate; // today

	public StatsReport(WebDriver driver) throws Exception {
		super(driver);
		statsFromEmails = new HashMap<String, HashMap<String, String>>();
		getDates();
	}

	public void register() throws Exception {
		driver.navigate().to("http://leads.leader-m.com/");
		WebElement loginBtn = driver.findElement(By
				.xpath("//*[@id=\"loginLink\"]/a"));
		loginBtn.click();
		WebElement username = driver.findElement(By.name("username"));
		WebElement password = driver.findElement(By.name("password"));
		WebElement ok = driver.findElement(By.xpath("//button[1]"));
		username.sendKeys("lm");
		password.sendKeys("V^d5Xv^6ct%!");
		ok.click();

	}
	
	public void getStats(String email) throws Exception{
		getStats(email, "http://leads.leader-m.com/en/stats.php?leadDateField=leadDate&leadDate_from=!!!!!!!!!&leadDate_till=!!!!!!!!!!&fields%5B%5D=rcptMail&filters%5BrcptMail%5D%5B%5D=!!!!!!!&fields%5B%5D=campId&filters%5BcampId%5D=&fields%5B%5D=&username=lm&password=EwmswjLgHNv72Tu&cc="
				.replace("!!!!!!!!!!", toDate).replace("!!!!!!!!!", fromDate)
				.replace("!!!!!!!", URLEncoder.encode(email)));
	}
	
	public void getPartnerStats(String email) throws Exception{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd+MMMM+yyyy",Locale.ENGLISH);
		//get todays date
		Date date = new Date();
		String today = dateFormat.format(date);
		// get the next day date
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		String tomorrow = dateFormat.format(calendar.getTime());
		getStats(email,String.format("http://leads.leader-m.com/en/stats.php?leadDate_from=%s&leadDate_till=%s&fields[]=__referrer&filters[__referrer][]=%s&fields[]=campId&filters[campId]=&fields[]=&fields[]=&fields[]=",today, tomorrow, URLEncoder.encode(email,"UTF-8")));
	}

	public void getStats(String email,String url) throws Exception {
		HashMap<String, String> tmp = new HashMap<String, String>();
		
		driver.navigate().to(url);
		try {
			List<WebElement> count = driver.findElements(By
					.className("columnleadCount"));
			List<WebElement> compiegne = driver.findElements(By
					.className("columncampId"));

			WebElement total = driver.findElement(By
					.xpath("//span[@class=\"badge\"]"));
			for (int i = 0; i < compiegne.size(); i++) {
				tmp.put(compiegne.get(i).getText(), count.get(i).getText());
			}
			tmp.put("total", total.getText());
		} catch (Exception e) {
			tmp.put("total", "0");
		}

		statsFromEmails.put(email, tmp);

	}

	private void getDates() throws Exception {
		StringBuffer today = new StringBuffer();
		StringBuffer yesterday = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		today.append(calendar.get(Calendar.MONTH)+1);
		yesterday.append(calendar.get(Calendar.MONTH)+1);
		today.append("%2F");
		yesterday.append("%2F");
		today.append(calendar.get(Calendar.DAY_OF_MONTH));
		yesterday.append(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		today.append("%2F");
		yesterday.append("%2F");
		today.append(calendar.get(Calendar.YEAR));
		yesterday.append(calendar.get(Calendar.YEAR));

		toDate = today.toString();
		fromDate = yesterday.toString();

	}

	public String getHTMLTable() throws Exception {
		int totalCount = 0;
		StringBuffer htmlTable = new StringBuffer();
		htmlTable
				.append("<html><Meta HTTP-EQUIV=\"Content-Type\"Content=\"text/html; charset=koi8-r\">");
		htmlTable
				.append("<table border=\"1\" cellpadding=\"3\" cellspacing=\"2\">");
		for (String key : statsFromEmails.keySet()) {
			String value = "";
			htmlTable.append(String.format(
					"<tr><th colspan=\"2\" bgcolor=\"#C0C0C0\">%s</th></tr>",
					key));
			for (String category : statsFromEmails.get(key).keySet()) {
				// we will print the total later
				if (category.equals("total")){
					continue;
				}
				value = statsFromEmails.get(key).get(category);
				htmlTable.append(String.format("<tr><td>%s</td><td>%s</td></tr>",
						category, value));
			}
			String total = statsFromEmails.get(key).get("total");	
			htmlTable.append(String.format(
					"<tr><th>total</th><th>%s</th></tr>", total));
			totalCount +=Integer.parseInt(total);
			}
		htmlTable.append(String.format(
				"<tr><th colspan=\"2\" bgcolor=\"#C0C0C0\">Total Count %s</th></tr>",
				totalCount));
		htmlTable.append("</table></html>");
		return htmlTable.toString();

	}

}
