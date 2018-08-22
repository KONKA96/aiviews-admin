package com.controller;

import java.util.List;

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

}
