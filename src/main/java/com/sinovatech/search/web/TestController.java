package com.sinovatech.search.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.sinovatech.search.utils.encode.JsonBinder;
import com.sinovatech.search.utils.web.AjaxResponse;
import com.sinovatech.search.utils.web.DateBindController;

/**
 * 用户Controller
 * 
 * author elvman
 */
@Controller
@RequestMapping("test")
public class TestController extends DateBindController {

	/**
	 * 页面跳转
	 * 
	 * @param model
	 * @return
	 *
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return "user/testadd";
	}

	// 查询所有用户
	@RequestMapping(value = "/list")
	public String list(ModelMap model, HttpServletRequest request) {

	 
		return "user/testList";
	}

	// 新建用户 (注意，此方法演示redirect跳转)
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView createForm(  ModelMap modelMap) {
		 
		return new ModelAndView(new RedirectView("list"), modelMap);
	}

	/**
	 * 请求参数充当url 并且返回任意指定对象的ajax请求。
	 * 
	 * @param aaa
	 * @param bbb
	 * @return
	 * test/testlist/liuzhenquan/123457/
	 */
	@RequestMapping(value = "/testlist/{aaa}/{bbb}", method = RequestMethod.GET)
	public @ResponseBody
	AjaxResponse testList(@PathVariable("aaa") String aaa,
			@PathVariable("bbb") String bbb) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("aaa", aaa);
		map.put("bbb", bbb);
		AjaxResponse ar = new AjaxResponse(true, map);
		return ar;
	}

	/**
	 * 请求参数充当url 并且返回任意指定对象的ajax请求。
	 * 
	 * @param aaa
	 * @param bbb
	 * @return   jQuery  js  reponseText  eval()
	 */
	@RequestMapping(value = "/testlist", method = RequestMethod.GET)
	public @ResponseBody
	AjaxResponse testList_t() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("aaa", "test");
		map.put("bbb", "test1");
		String json = JsonBinder.buildNonDefaultBinder().toJson(map); 
		AjaxResponse ar = new AjaxResponse(true, json);
		return ar;
	}
}
