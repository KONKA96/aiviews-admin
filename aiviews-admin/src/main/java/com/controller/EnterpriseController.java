package com.controller;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.httpsFile.FileTransferClient;
import com.model.Admin;
import com.model.Enterprise;
import com.model.Logger;
import com.model.Record;
import com.model.Screen;
import com.model.Student;
import com.model.Teacher;
import com.service.EnterpriseService;
import com.util.HttpsUtil;
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
	public String selectAllEnterprise(Enterprise enterprise,ModelMap modelMap,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request) {
		
		PageHelper.startPage(index, pageSize);
		Page<Enterprise> enterpriseList = (Page<Enterprise>) enterpriseService.selectAllEnterprise(enterprise);
		pageUtil.setPageInfo(enterpriseList, index, pageSize,request);
		
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
	 * 跳转到修改、新增企业页面
	 * @param enterprise
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toEditEnterprise")
	public String toEditEnterprise(Enterprise enterprise,ModelMap modelMap) {
		enterprise=enterpriseService.selectByPrimaryKey(enterprise);
		
		modelMap.addAttribute("enterprise", enterprise);
		return "/edit-enterprise";
	}
	
	/**
	 * 修改、新增企业
	 * @param enterprise
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateEnterprise")
	public String updateEnterprise(Enterprise enterprise) {
		if(enterprise.getId()!=null) {
			if(enterpriseService.updateByPrimaryKeySelective(enterprise)>0) {
				return "success";
			}
		}else {
			if(enterpriseService.insertSelective(enterprise)>0) {
				return "success";
			}
		}
		
		return "";
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
		/*Map<String,String> params=new HashMap<>();
		if(teacher.getUsername()!=null) {
			params.put("username", teacher.getUsername());
		}
		params.put("index", index+"");
		params.put("pageSize", pageSize+"");*/
		String params="index="+index+"&pageSize="+pageSize;
		if(teacher.getUsername()!=null) {
			params=params+"&username="+teacher.getUsername();
		}
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			/*String sendPost = HttpUtil.sendPost(url, params);*/
			String httpsRequest = null;
			try {
				httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);
			
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
		
		String params="index="+index+"&pageSize="+pageSize;
		if(student.getUsername()!=null) {
			params=params+"&username="+student.getUsername();
		}
		
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);
			
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
		String params="index="+index+"&pageSize="+pageSize;
		if(admin.getUsername()!=null) {
			params=params+"&username="+admin.getUsername();
		}
		
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);
			
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
		String params="index="+index+"&pageSize="+pageSize;
		if(screen.getUsername()!=null) {
			params=params+"&username="+screen.getUsername();
		}
		
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);
			
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
	
	/**
	 * 查询企业使用记录信息
	 * @param record
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectRecord")
	public String selectRecord(Record record,HttpSession session,ModelMap modelMap,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request) {
		
		Enterprise enterprise=(Enterprise) session.getAttribute("enterprise");
		
		String url="/aihudong-duoping-web/aiviews/selectAllRecord";
		String params="index="+index+"&pageSize="+pageSize;
		if(record.getRole()!=null) {
			params = params + "&role="+record.getRole();
		}else {
			params = params + "&role=1";
		}
		if(record.getUsername()!=null && record.getUsername()!="") {
			params = params + "&username="+record.getUsername();
		}
		
		if(enterprise!=null) {
			url=enterprise.getRealmName()+url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);
			
			List<Record> recordList = JsonUtils.jsonToList(jsonObject.get("recordList").toString(), Record.class);
			modelMap.addAttribute("recordList",recordList);
			
			request.setAttribute("index", index);
			request.setAttribute("end", jsonObject.get("end"));
			request.setAttribute("start", jsonObject.get("start"));
			request.setAttribute("total", jsonObject.get("total"));
			request.setAttribute("totalCount", jsonObject.get("totalCount"));
			
		}
		return "/record/list-record";
	}
	
	@RequestMapping("/testHttpsFile")
	public String testHttpsFile(HttpServletRequest request) throws Exception {
		ServletContext servletContext = request.getServletContext();
		String realPath = servletContext.getRealPath("/");
		realPath = realPath.substring(0, realPath.lastIndexOf("\\"));
		realPath = realPath.substring(0, realPath.lastIndexOf("\\"));
		realPath = realPath.substring(0, realPath.lastIndexOf("\\"));
		File file = new File(realPath+"\\"+"logs"+"\\"+"log.log");
		String url="https://ys.51asj.com/aihudong-duoping-web/aiviews/testFile";
		/*String sendPostWithFile = HttpsFileUtil.sendPostWithFile(url, file);
		System.out.println(sendPostWithFile);*/
		
		//HttpsFileUtil.testFile(url, file);
		
		try {  
            FileTransferClient client = new FileTransferClient(); // 启动客户端连接  
            client.sendFile(file); // 传输文件  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
		
		return "";
	}
}
