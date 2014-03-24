package com.leaderm.infra;

import java.util.ArrayList;

import jsystem.framework.system.SystemObjectImpl;
import jsystem.utils.FileUtils;

public class FileParser extends SystemObjectImpl {

	private String urlListFileName;
	private String emailListFileName;
	private String partnerListFileName;

	

	public ArrayList<String> getList(String fileName) throws Exception {
		String[] urls = FileUtils.read(fileName).split(System.getProperty("line.separator"));
		ArrayList<String> list = new ArrayList<String>();
		for (String url : urls) {
			list.add(url.trim());
		}
		return list;

	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
	}

	public String getUrlListFileName() {
		return urlListFileName;
	}

	public void setUrlListFileName(String urlListFileName) {
		this.urlListFileName = urlListFileName;
	}

	public String getEmailListFileName() {
		return emailListFileName;
	}

	public void setEmailListFileName(String emailListFileName) {
		this.emailListFileName = emailListFileName;
	}

	public ArrayList<String> getPartnerList() throws Exception {
		return  getList(partnerListFileName);
	}

	public void setPartnerListFileName(String partnerListFileName) {
		this.partnerListFileName = partnerListFileName;
	}

	public ArrayList<String> getUrlList() throws Exception {
		return getList(urlListFileName);
	}

	public ArrayList<String> getEmailList() throws Exception {
		return getList(emailListFileName);
	}

	public String getPartnerListFileName() {
		return partnerListFileName;
	}
	
}
