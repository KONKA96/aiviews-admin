package com.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Admin;
import com.model.Enterprise;
import com.model.Logger;
import com.service.AdminService;

@Controller
@RequestMapping("/login")
/**
 * 
 * @author KONKA
 *
 */
public class LoginController {
	
	@Autowired
	private AdminService adminService;
	
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 跳转登录页
	 * @return
	 */
	@RequestMapping("/toLogin")
	public String toLogin() {
		return "login";
	}
	
	
	/**
	 * 管理员登录
	 * @param admin
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/adminLogin")
	public String adminLogin(Admin admin) {
		UsernamePasswordToken token = new UsernamePasswordToken(admin.getUsername(),
				new Md5Hash(admin.getPassword(), admin.getUsername() ,2).toString());
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			
			logger.info(admin.getUsername()+"登录系统");
			return "success";
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 跳转首页
	 * @return
	 */
	@RequestMapping("/toIndex")
	public String toIndex() {
		return "index";
	}
	
	@RequestMapping("/getEchartData")
	public String getEchartData() {
		return "echarts-data";
	}
	
	/**
	 * 清除企业session
	 * @param session
	 * @return
	 */
	@RequestMapping("/enterpriseLogout")
	public String enterpriseLogout(HttpSession session) {
		session.removeAttribute("enterprise");
		return "redirect:/enterprise/selectAllEnterprise";
	}

}
