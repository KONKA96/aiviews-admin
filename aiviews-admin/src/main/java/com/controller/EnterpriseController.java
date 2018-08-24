package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model.Admin;
import com.model.Enterprise;
import com.model.Logger;
import com.model.Screen;
import com.model.Student;
import com.model.Teacher;
import com.service.EnterpriseService;
import com.util.HttpUtil;
import com.util.JsonUtils;
import com.util.PageUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/enterprise")
public class EnterpriseController {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private EnterpriseService enterpriseService;
	@Autowired
	private PageUtil pageUtil;
	
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
	
	/**
	 * 查询企业教师信息
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectTeacher")
	public String selectTeacher(Teacher teacher,HttpSession session,ModelMap modelMap,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request) {
		Enterprise enterprise=(Enterprise) session.getAttribute("enterprise");
		
		String url="/aihudong-duoping-web/aiviews/selectAllTeacher";
		Map<String,String> params=new HashMap<>();
		if(teacher.getUsername()!=null) {
			params.put("username", teacher.getUsername());
		}
		params.put("index", index+"");
		params.put("pageSize", pageSize+"");
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			String sendPost = HttpUtil.sendPost(url, params);
			JSONObject jsonObject = JSONObject.fromObject(sendPost);
			
			List<Teacher> teacherList = JsonUtils.jsonToList(jsonObject.get("teacherList").toString(), Teacher.class);
			modelMap.addAttribute("teacherList",teacherList);
			
			request.setAttribute("index", index);
			request.setAttribute("end", jsonObject.get("end"));
			request.setAttribute("start", jsonObject.get("start"));
			request.setAttribute("total", jsonObject.get("total"));
			request.setAttribute("totalCount", jsonObject.get("totalCount"));
			
			//pageUtil.setPageInfo(teacherList, index, pageSize,request);
		}
		
		return "/teacher/list-teacher";
	}
	
	/**
	 * 查询企业学生信息
	 * @param student
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectStudent")
	public String selectStudent(Student student,HttpSession session,ModelMap modelMap,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request) {
		Enterprise enterprise=(Enterprise) session.getAttribute("enterprise");
		
		String url="/aihudong-duoping-web/aiviews/selectAllStudent";
		Map<String,String> params=new HashMap<>();
		if(student.getUsername()!=null) {
			params.put("username", student.getUsername());
		}
		params.put("index", index+"");
		params.put("pageSize", pageSize+"");
		
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			String sendPost = HttpUtil.sendPost(url, params);
			JSONObject jsonObject = JSONObject.fromObject(sendPost);
			
			List<Student> studentList = JsonUtils.jsonToList(jsonObject.get("studentList").toString(), Student.class);
			modelMap.addAttribute("studentList",studentList);
			
			request.setAttribute("index", index);
			request.setAttribute("end", jsonObject.get("end"));
			request.setAttribute("start", jsonObject.get("start"));
			request.setAttribute("total", jsonObject.get("total"));
			request.setAttribute("totalCount", jsonObject.get("totalCount"));
			
		}
		return "/student/list-student";
	}
	
	/**
	 * 查询企业管理员信息
	 * @param admin
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectAdmin")
	public String selectAdmin(Admin admin,HttpSession session,ModelMap modelMap,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request) {
		Enterprise enterprise=(Enterprise) session.getAttribute("enterprise");
		
		String url="/aihudong-duoping-web/aiviews/selectAllAdmin";
		Map<String,String> params=new HashMap<>();
		if(admin.getUsername()!=null) {
			params.put("username", admin.getUsername());
		}
		params.put("index", index+"");
		params.put("pageSize", pageSize+"");
		
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			String sendPost = HttpUtil.sendPost(url, params);
			JSONObject jsonObject = JSONObject.fromObject(sendPost);
			
			List<Admin> adminList = JsonUtils.jsonToList(jsonObject.get("adminList").toString(), Admin.class);
			modelMap.addAttribute("adminList",adminList);
			
			request.setAttribute("index", index);
			request.setAttribute("end", jsonObject.get("end"));
			request.setAttribute("start", jsonObject.get("start"));
			request.setAttribute("total", jsonObject.get("total"));
			request.setAttribute("totalCount", jsonObject.get("totalCount"));
			
		}

		return "/admin/list-admin";
	}
	
	/**
	 * 查询企业屏幕信息
	 * @param screen
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectScreen")
	public String selectScreen(Screen screen,HttpSession session,ModelMap modelMap,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request) {
		Enterprise enterprise=(Enterprise) session.getAttribute("enterprise");
		
		String url="/aihudong-duoping-web/aiviews/selectAllScreen";
		Map<String,String> params=new HashMap<>();
		if(screen.getUsername()!=null) {
			params.put("username", screen.getUsername());
		}
		params.put("index", index+"");
		params.put("pageSize", pageSize+"");
		
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			String sendPost = HttpUtil.sendPost(url, params);
			JSONObject jsonObject = JSONObject.fromObject(sendPost);
			
			List<Screen> screenList = JsonUtils.jsonToList(jsonObject.get("screenList").toString(), Screen.class);
			modelMap.addAttribute("screenList",screenList);
			
			request.setAttribute("index", index);
			request.setAttribute("end", jsonObject.get("end"));
			request.setAttribute("start", jsonObject.get("start"));
			request.setAttribute("total", jsonObject.get("total"));
			request.setAttribute("totalCount", jsonObject.get("totalCount"));
			
		}
		return "/screen/list-screen";
	}
}
