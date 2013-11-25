package com.leaderm.infra.mail;

import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.OMGVMCID;

import jsystem.framework.system.SystemObjectImpl;
import jsystem.utils.DateUtils;

public class MailClient extends SystemObjectImpl {

	private String host;
	private String port;
	private String mailFrom;
	private String password;
	private String mailTo;
	private StringBuilder msg;
	private Map<String, String> inlineImages;

	@Override
	public void init() throws Exception {
		super.init();
		inlineImages = new HashMap<String, String>();
		msg = new StringBuilder();
		msg.append("<html>");
		msg.append("<table border=\"1\"><tr><th>Site URL</th><th>Status</th><th>OrderId</th><th>Screenshot</th></tr>");
	}

	public void report(String url, boolean status, String orderId,
			String imageName, String imagePath) {
		if (status) {
			msg.append("<tr bgcolor=\"#00FF00\"><td>" + url
					+ "</td><td>Pass</td><td>" + orderId
					+ "</td><td>None</td></tr>");
		} else {
			if (imageName != null) {
				inlineImages.put(imageName.replace(" ", ""), imagePath);
			}
			msg.append("<tr bgcolor=\"red\"><td>" + url
					+ "</td><td>Fail</td><td>None</td><td><a href=\"cid:"
					+ imageName.replace(" ", "")
					+ "\">click here</a></td></tr>");

		}

	}

	public void sendMail(String title) {
		msg.append("</table></html>");
		String subject = title;

		try {
			EmbeddedImageEmailUtil.send(host, port, mailFrom, password, mailTo,
					subject, msg.toString(), inlineImages);
			report.report("Email sent.");
		} catch (Exception ex) {
			report.report("Could not send email.");
			ex.printStackTrace();
		}
	}

	public void sendMail(String title, String body) {
		String subject = title;
		try {
			EmbeddedImageEmailUtil.send(host, port, mailFrom, password, mailTo,
					subject, body, inlineImages);
			report.report("Email sent.");
		} catch (Exception ex) {
			report.report("Could not send email.");
			ex.printStackTrace();
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
}