package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.AdminMapper;
import com.model.Admin;
import com.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminMapper adminMapper;

	@Override
	public List<Admin> adminLogin(Admin admin) {
		// TODO Auto-generated method stub
		return adminMapper.adminLogin(admin);
	}

}
