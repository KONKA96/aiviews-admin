package com.thread;

import java.util.concurrent.Callable;

import com.util.HttpsUtil;

public class EnterpriseThread implements Callable<String> {

	private String url;
	
	private String params;
	
	public EnterpriseThread(String url,String params) {
		super();
		this.url = url;
		this.params = params;
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
		
		return httpsRequest;
	}

}
