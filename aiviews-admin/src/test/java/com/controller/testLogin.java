package com.controller;

import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.BaseTest;
import com.model.Admin;
import com.service.AdminService;

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

}
