package com.controller;

import java.io.File;
import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.BaseTest;
import com.model.Admin;
import com.service.AdminService;
import com.util.HttpsFileUtil;
import com.util.HttpsUtil;

public class testLogin extends BaseTest{
	
	@Autowired
	private AdminService adminService;
	
	@Test
	public void testAdminLogin() {
		Admin admin=new Admin();
		admin.setUsername("konka");
		admin.setPassword("123");
		List<Admin> adminLogin = adminService.adminLogin(admin);
		System.out.println(adminLogin.get(0));
	}
	
	@Test
	public void testMD5() {
		String password = "123";
		String username = "konka";
		String md5 = new Md5Hash(password, username ,2).toString();
		System.out.println(username+"-----------------"+md5);
	}
	
	@Test
	public void testHttps() {
		String s=HttpsUtil.httpsRequest("https://ys.51asj.com/aihudong-duoping-web/aiviews/selectAllTeacher","GET",null);
		System.out.println(s);
	}
	
	@Test
	public void testHttpsFile() throws Exception {
		String url="https://ys.51asj.com/aihudong-duoping-web/aiviews/testFile";
		File file = new File("D:\\jd-gui-1.4.0.jar");
		HttpsFileUtil.sendPostWithFile(url,file);
		HttpsFileUtil.testFile(url, file);
	}

}
