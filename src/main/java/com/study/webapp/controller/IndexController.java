package com.study.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

//import com.zhonghui.core.utils.JsonUtils;

/**
 * 
 * @author xwb
 *
 */
@Controller
@RequestMapping("")
public class IndexController extends BaseController {

	@RequestMapping(value = { "/info" })
	@ResponseBody
	public String info() {
		return "{'status':'UP'}";
	}

	@RequestMapping(value = { "/login" })
	public String login() {
		return "pages/login";
	}

	// 主页
	@RequestMapping(value = { "/", "/index" })
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModelAndView(request, response, model);
		return "pages/project/project_index";
	}

	// 项目主页
	@RequestMapping(value = { "/project" })
	public String project(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModelAndView(request, response, model);
		return "pages/project/project_index";
	}

	// 项目资料（文件管理）
	@RequestMapping(value = { "/project/{projectId}/files" })
	public String projectFiles(HttpServletRequest request, HttpServletResponse response, Model model,
			@PathVariable("projectId") long projectId) {
		this.setModelAndView(request, response, model);

		Map<String, Object> datas = new HashMap<>();
		// Object project = projectService.ProjectVoById(projectId);
		// if (project != null)
		// datas.put("projectInfo", project);
		model.addAttribute("pageData", JSON.toJSONString(datas));

		return "pages/project/project_files";
	}

	// 文件查询
	@RequestMapping(value = { "/files/query" })
	public String filesQuery(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModelAndView(request, response, model);

		Map<String, Object> datas = new HashMap<>();
		datas.put("searchText", request.getParameter("t"));
		model.addAttribute("pageData", JSON.toJSONString(datas));

		return "pages/files/files_query";
	}

	// 业绩库
	@RequestMapping(value = { "/achieve" })
	public String achieve(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModelAndView(request, response, model);
		return "pages/achieve/achieve_index";
	}

	// 设置
	@RequestMapping(value = { "/settings" })
	public String settingsIndex(HttpServletRequest request, HttpServletResponse response) {
		// User user = this.getCurrentUser(request, response);
		// if (user.getRoleType() == 1) {
		// return "redirect:/settings/account";
		// }
		return "redirect:/settings/repwd";
	}

	// 帐户管理
	@RequestMapping(value = { "/settings/account" })
	public String account(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModelAndView(request, response, model);
		return "pages/settings/settings_account";
	}

	// 重置密码
	@RequestMapping(value = { "/settings/repwd" })
	public String repwd(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModelAndView(request, response, model);
		return "pages/settings/settings_repwd";
	}

	// 上传测试
	@RequestMapping(value = { "/test/upload" }, method = RequestMethod.POST)
	@ResponseBody
	public Object testUpload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile file) {
		System.out.println(file);
		return null;
	}

}
