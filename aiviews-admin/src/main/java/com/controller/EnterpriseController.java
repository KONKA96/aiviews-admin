package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.model.Enterprise;
import com.model.Logger;
import com.service.EnterpriseService;
import com.util.HttpUtil;

@Controller
@RequestMapping("/enterprise")
public class EnterpriseController {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private EnterpriseService enterpriseService;
	
	/**
	 * 查询所有企业
	 * @param enterprise
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/selectAllEnterprise")
	public String selectAllEnterprise(Enterprise enterprise,ModelMap modelMap) {
		
		List<Enterprise> enterpriseList = enterpriseService.selectAllEnterprise(enterprise);
		modelMap.put("enterpriseList", enterpriseList);
		return "list-enterprise";
	}
	
	/**
	 * 跳转到企业详情页
	 * @param enterprise
	 * @param session
	 * @return
	 */
	@RequestMapping("/showEnterpriseDetail")
	public String showEnterpriseDetail(Enterprise enterprise,HttpSession session) {
		enterprise=enterpriseService.selectByPrimaryKey(enterprise);
		
		session.setAttribute("enterprise", enterprise);
		return "index";
	}
	
	@RequestMapping("/selectTeacher")
	public String selectTeacher(HttpSession session) {
		Enterprise enterprise=(Enterprise) session.getAttribute("enterprise");
		
		String url="/aihudong-duoping-web/aiviews/selectAllTeacher";
		Map<String,String> params=new HashMap<>();
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			String sendPost = HttpUtil.sendPost(url, params);
			System.out.println(sendPost);
		}
		return "";
	}
}
