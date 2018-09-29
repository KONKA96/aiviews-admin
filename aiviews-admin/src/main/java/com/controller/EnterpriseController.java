package com.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.model.Admin;
import com.model.Enterprise;
import com.model.Logger;
import com.model.Record;
import com.model.Screen;
import com.model.Student;
import com.model.Teacher;
import com.service.EnterpriseService;
import com.thread.EnterpriseThread;
import com.util.HttpsUtil;
import com.util.JsonUtils;
import com.util.PageUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/enterprise")
/**
 * 
 * @author KONKA
 *
 */
public class EnterpriseController {

	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private EnterpriseService enterpriseService;
	@Autowired
	private PageUtil pageUtil;

	/**
	 * 查询所有企业
	 * 
	 * @author KONKA
	 * @param enterprise
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/selectAllEnterprise")
	public String selectAllEnterprise(Enterprise enterprise, ModelMap modelMap,
			@RequestParam(required = false, defaultValue = "1") Integer index,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize, HttpServletRequest request) {

		PageHelper.startPage(index, pageSize);
		Page<Enterprise> enterpriseList = (Page<Enterprise>) enterpriseService.selectAllEnterprise(enterprise);
		pageUtil.setPageInfo(enterpriseList, index, pageSize, request);

		modelMap.put("enterpriseList", enterpriseList);
		return "list-enterprise";
	}

	/**
	 * 获取企业教师、学生、屏幕总量
	 * 
	 * @author KONKA
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEnterpriseData", produces = "text/json;charset=UTF-8")
	public String getEnterpriseData() {
		List<Enterprise> enterpriseList = enterpriseService.selectAllEnterprise(null);

		JSONObject jsonObject = new JSONObject();
		// 企业数量
		jsonObject.put("totalNum", enterpriseList.size());
		String url = "/aihudong-duoping-web/aiviews/getUserNum";

		int count = 0;
		int teacherNum = 0;
		int studentNum = 0;
		int screenNum = 0;

		// 创建一个线程池对象
		ExecutorService pool = Executors.newCachedThreadPool();

		for (Enterprise enterprise : enterpriseList) {
			url = enterprise.getRealmName() + url;
			EnterpriseThread et = new EnterpriseThread(url, null);
			// 开启线程
			Future<String> future = pool.submit(et);
			// 从 Future 对象 获取任务返回值
			while (true) {
				// 可以用isDone()方法来查询Future是否已经完成，任务完成后，可以调用get()方法来获取结果
				// 注意： 如果不加判断直接调用get方法，此时如果线程未完成，get将阻塞，直至结果准备就绪
				if (future.isDone()) {
					try {
						String httpsRequest = future.get().toString();
						JSONObject object = JSONObject.fromObject(httpsRequest);
						String teaNum = object.getString("teacherNum");

						teacherNum += Integer.parseInt(teaNum);

						String stuNum = object.getString("studentNum");

						studentNum += Integer.parseInt(stuNum);

						String srcNum = object.getString("screenNum");

						screenNum += Integer.parseInt(srcNum);
					} catch (Exception e) {
						logger.info(enterprise.getEnterpriseName() + "通信失败");
						count++;
						e.printStackTrace();
					}
					// 关闭线程池
					pool.shutdown();
					// 跳出循环
					break;
				}
			}
		}

		// 有企业通信失败
		if (count != 0) {
			jsonObject.put("errorMes", count + "家企业通信失败！");
		}
		// 传递教师、学生、屏幕数量
		jsonObject.put("teacherNum", teacherNum);
		jsonObject.put("studentNum", studentNum);
		jsonObject.put("screenNum", screenNum);

		return jsonObject.toString();
	}

	/**
	 * 获取各个企业使用的时长总和
	 * @author KONKA
	 * @param category
	 * @param interval
	 * @param time
	 * @param bingfa
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRecordEchartsData")
	public String getRecordEchartsData(@RequestParam(required = false, defaultValue = "1") Integer category,
			@RequestParam(required = false, defaultValue = "7") String interval,
			@RequestParam(required = false) String time, @RequestParam(required = false) String bingfa,
			HttpSession session) {
		List<Enterprise> enterpriseList = enterpriseService.selectAllEnterprise(null);

		JSONObject jsonObject = new JSONObject();
		String url = "/aihudong-duoping-web/aiviews/getEcharts";

		// 创建一个线程池对象
		ExecutorService pool = Executors.newCachedThreadPool();

		// 设置参数
		String params = "";
		if (category != null) {
			params += "category=" + category;
		}
		if (interval != null) {
			params += "&interval=" + interval;
		}
		if (time != null) {
			params += "&time=" + time;
		}
		if (bingfa != null) {
			params += "&bingfa=" + bingfa;
		}

		List<String> dateList = new ArrayList<>(6);
		String[] hourList = new String[] { "0", "0", "0", "0", "0", "0" };
		
		for (Enterprise enterprise : enterpriseList) {
			url = enterprise.getRealmName() + url;
			EnterpriseThread et = new EnterpriseThread(url, params);
			// 开启线程
			Future<String> future = pool.submit(et);
			while (true) {
				if (future.isDone()) {
					try {
						String httpsRequest = future.get().toString();
						JSONObject object = JSONObject.fromObject(httpsRequest);
						String xAxisData = object.getString("xAxisData");
						dateList = JsonUtils.jsonToList(xAxisData, String.class);
						String series = object.getString("seriesList");
						List<Object> list = JsonUtils.jsonToList(series, Object.class);
						for (int i = 0; i < hourList.length; i++) {
							String data = list.get(i).toString().substring(list.get(i).toString().indexOf("=") + 1,
									list.get(i).toString().length() - 1);
							Integer hour = Integer.parseInt(hourList[i]) + Integer.parseInt(data);
							hourList[i] = hour.toString();
						}
					} catch (Exception e) {
						logger.info(enterprise.getEnterpriseName() + "通信失败");
						e.printStackTrace();
					}
					// 关闭线程池
					pool.shutdown();
					// 跳出循环
					break;
				}
			}
		}
//      表格中字段的单位
        String type="";
		List< JSONObject> seriesList = new ArrayList< JSONObject>();
		
		jsonObject.put("xAxisData", dateList);
		for (int i = 0; i < hourList.length; i++) {
			JSONObject jsObject = new JSONObject();
			jsObject.put("hour", hourList[i]);
			seriesList.add(jsObject);
		}
		jsonObject.put("seriesList", seriesList);
		jsonObject.put("type", type);

		return jsonObject.toString();
	}

	/**
	 * 跳转到企业详情页
	 * 
	 * @param enterprise
	 * @param session
	 * @return
	 */
	@RequestMapping("/showEnterpriseDetail")
	public String showEnterpriseDetail(Enterprise enterprise, HttpSession session) {
		enterprise = enterpriseService.selectByPrimaryKey(enterprise);
		Subject subject = SecurityUtils.getSubject();
		String url = "/aihudong-duoping-web/aiviews/adminLogin";
		url = enterprise.getRealmName() + url;
		String params = "username=" + subject.getPrincipal();
		String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
		logger.info(subject.getPrincipal() + "登录" + enterprise.getRealmName() + "后台管理系统");
		session.setAttribute("enterprise", enterprise);
		return "index";
	}

	/**
	 * 跳转到修改、新增企业页面
	 * 
	 * @param enterprise
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toEditEnterprise")
	public String toEditEnterprise(Enterprise enterprise, ModelMap modelMap) {
		enterprise = enterpriseService.selectByPrimaryKey(enterprise);

		modelMap.addAttribute("enterprise", enterprise);
		return "/edit-enterprise";
	}

	/**
	 * 修改、新增企业
	 * 
	 * @param enterprise
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateEnterprise")
	public String updateEnterprise(Enterprise enterprise) {
		if (enterprise.getId() != null) {
			if (enterpriseService.updateByPrimaryKeySelective(enterprise) > 0) {
				return "success";
			}
		} else {
			if (enterpriseService.insertSelective(enterprise) > 0) {
				return "success";
			}
		}

		return "";
	}

	/**
	 * 删除企业
	 * 
	 * @param enterprise
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteEnterprise")
	public String deleteEnterprise(Enterprise enterprise) {
		if (enterpriseService.deleteByPrimaryKey(enterprise) > 0) {
			return "success";
		}
		return "";
	}

	/**
	 * 查询企业教师信息
	 * 
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectTeacher")
	public String selectTeacher(Teacher teacher, HttpSession session, ModelMap modelMap,
			@RequestParam(required = false, defaultValue = "1") Integer index,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize, HttpServletRequest request) {
		Enterprise enterprise = (Enterprise) session.getAttribute("enterprise");

		String url = "/aihudong-duoping-web/aiviews/selectAllTeacher";
		/*
		 * Map<String,String> params=new HashMap<>(); if(teacher.getUsername()!=null) {
		 * params.put("username", teacher.getUsername()); } params.put("index",
		 * index+""); params.put("pageSize", pageSize+"");
		 */
		String params = "index=" + index + "&pageSize=" + pageSize;
		if (teacher.getUsername() != null) {
			params = params + "&username=" + teacher.getUsername();
		}
		if (enterprise != null) {
			url = enterprise.getRealmName() + url;
			/* String sendPost = HttpUtil.sendPost(url, params); */
			String httpsRequest = null;
			try {
				httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);

			List<Teacher> teacherList = JsonUtils.jsonToList(jsonObject.get("teacherList").toString(), Teacher.class);
			modelMap.addAttribute("teacherList", teacherList);

			request.setAttribute("index", index);
			request.setAttribute("end", jsonObject.get("end"));
			request.setAttribute("start", jsonObject.get("start"));
			request.setAttribute("total", jsonObject.get("total"));
			request.setAttribute("totalCount", jsonObject.get("totalCount"));

			// pageUtil.setPageInfo(teacherList, index, pageSize,request);
		}

		return "/teacher/list-teacher";
	}

	/**
	 * 查询企业学生信息
	 * 
	 * @param student
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectStudent")
	public String selectStudent(Student student, HttpSession session, ModelMap modelMap,
			@RequestParam(required = false, defaultValue = "1") Integer index,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize, HttpServletRequest request) {
		Enterprise enterprise = (Enterprise) session.getAttribute("enterprise");

		String url = "/aihudong-duoping-web/aiviews/selectAllStudent";

		String params = "index=" + index + "&pageSize=" + pageSize;
		if (student.getUsername() != null) {
			params = params + "&username=" + student.getUsername();
		}

		if (enterprise != null) {
			url = enterprise.getRealmName() + url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);

			List<Student> studentList = JsonUtils.jsonToList(jsonObject.get("studentList").toString(), Student.class);
			modelMap.addAttribute("studentList", studentList);

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
	 * 
	 * @param admin
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectAdmin")
	public String selectAdmin(Admin admin, HttpSession session, ModelMap modelMap,
			@RequestParam(required = false, defaultValue = "1") Integer index,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize, HttpServletRequest request) {
		Enterprise enterprise = (Enterprise) session.getAttribute("enterprise");

		String url = "/aihudong-duoping-web/aiviews/selectAllAdmin";
		String params = "index=" + index + "&pageSize=" + pageSize;
		if (admin.getUsername() != null) {
			params = params + "&username=" + admin.getUsername();
		}

		if (enterprise != null) {
			url = enterprise.getRealmName() + url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);

			List<Admin> adminList = JsonUtils.jsonToList(jsonObject.get("adminList").toString(), Admin.class);
			modelMap.addAttribute("adminList", adminList);

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
	 * 
	 * @param screen
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectScreen")
	public String selectScreen(Screen screen, HttpSession session, ModelMap modelMap,
			@RequestParam(required = false, defaultValue = "1") Integer index,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize, HttpServletRequest request) {
		Enterprise enterprise = (Enterprise) session.getAttribute("enterprise");

		String url = "/aihudong-duoping-web/aiviews/selectAllScreen";
		String params = "index=" + index + "&pageSize=" + pageSize;
		if (screen.getUsername() != null) {
			params = params + "&username=" + screen.getUsername();
		}

		if (enterprise != null) {
			url = enterprise.getRealmName() + url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);

			List<Screen> screenList = JsonUtils.jsonToList(jsonObject.get("screenList").toString(), Screen.class);
			modelMap.addAttribute("screenList", screenList);

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
	 * 
	 * @param record
	 * @param session
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectRecord")
	public String selectRecord(Record record, HttpSession session, ModelMap modelMap,
			@RequestParam(required = false, defaultValue = "1") Integer index,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize, HttpServletRequest request) {

		Enterprise enterprise = (Enterprise) session.getAttribute("enterprise");

		String url = "/aihudong-duoping-web/aiviews/selectAllRecord";
		String params = "index=" + index + "&pageSize=" + pageSize;
		if (record.getRole() != null) {
			params = params + "&role=" + record.getRole();
		} else {
			params = params + "&role=1";
		}
		if (record.getUsername() != null && record.getUsername() != "") {
			params = params + "&username=" + record.getUsername();
		}

		if (enterprise != null) {
			url = enterprise.getRealmName() + url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);

			List<Record> recordList = JsonUtils.jsonToList(jsonObject.get("recordList").toString(), Record.class);
			modelMap.addAttribute("recordList", recordList);

			request.setAttribute("index", index);
			request.setAttribute("end", jsonObject.get("end"));
			request.setAttribute("start", jsonObject.get("start"));
			request.setAttribute("total", jsonObject.get("total"));
			request.setAttribute("totalCount", jsonObject.get("totalCount"));

		}
		return "/record/list-record";
	}

	/**
	 * 查询日志文件列表
	 * 
	 * @author KONKA
	 * @param session
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/selectFile")
	public String selectFile(HttpSession session, ModelMap modelMap) {
		Enterprise enterprise = (Enterprise) session.getAttribute("enterprise");

		String url = "/aihudong-duoping-web/aiviews/getFileList";
		String params = null;

		if (enterprise != null) {
			url = enterprise.getRealmName() + url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);
			List<String> fileList = (List<String>) jsonObject.get("fileList");
			modelMap.addAttribute("fileList", fileList);
		}
		return "/logFileList/list-logFile";
	}

	/**
	 * 下载日志文件
	 * 
	 * @param session
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testHttpsFile")
	public String testHttpsFile(HttpSession session, String fileName) throws Exception {
		Enterprise enterprise = (Enterprise) session.getAttribute("enterprise");

		String url = "/aihudong-duoping-web/aiviews/testFile";
		String params = "&fileName=" + fileName;

		if (enterprise != null) {
			url = enterprise.getRealmName() + url;
			String httpsRequest = HttpsUtil.httpsRequest(url, "GET", params);
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest);
			String title = (String) jsonObject.get("title");
			List<String> content = (List<String>) jsonObject.get("content");

			File file = new File("D:\\FTCache/" + title);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			file.mkdirs();
			for (String string : content) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.close();
		}
		/*
		 * String sendPostWithFile = HttpsFileUtil.sendPostWithFile(url, file);
		 * System.out.println(sendPostWithFile);
		 */

		// HttpsFileUtil.testFile(url, file);

		/*
		 * try { FileTransferClient client = new FileTransferClient(); // 启动客户端连接
		 * client.sendFile(file); // 传输文件 } catch (Exception e) { e.printStackTrace(); }
		 */

		return "success";
	}
}
