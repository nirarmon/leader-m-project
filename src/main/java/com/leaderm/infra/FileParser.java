package com.leaderm.infra;

import java.util.ArrayList;

import org.apache.tools.ant.types.FileList.FileName;

import jsystem.framework.IgnoreMethod;
import jsystem.framework.system.SystemObjectImpl;
import jsystem.utils.FileUtils;

public class FileParser extends SystemObjectImpl{
	
	private String fileName;

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
	}
	public ArrayList<String> getUrlList() throws Exception {
		String[] urls = FileUtils.read(fileName).split("\n");
		ArrayList<String> urlList = new ArrayList<String>();
		for (String url : urls) {
			urlList.add(url);
		}
		return urlList;

	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
