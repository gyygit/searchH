package com.sinovatech.search.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinovatech.search.entity.JustBean;
import com.sinovatech.search.utils.encode.JsonBinder;
import com.sinovatech.search.utils.web.AjaxResponse;

@Controller
@RequestMapping("/request")
public class RequestController {

	private static final Logger log = LoggerFactory
			.getLogger("RequestController");
	//@Autowired
	//private JustService justService;

	@RequestMapping("main")
	public String main(HttpServletRequest request) {
		return "url/main";
	}

	@RequestMapping(value = "/get/{code}/{url}", method = RequestMethod.GET)
	public @ResponseBody
	AjaxResponse requestGet(HttpServletRequest request,
			@PathVariable("code") String code, @PathVariable("url") String url) {
		log.info("begin");
		List<JustBean> list = null;//justService.doSomething(code, url);
		return null;
	}

	@RequestMapping(value = "/post/{code}/{url}", method = RequestMethod.POST)
	public @ResponseBody
	AjaxResponse requestPost(HttpServletRequest request,
			@PathVariable("code") String code, @PathVariable("url") String url) {
		List<JustBean> list =null;//nu justService.doSomething(code, url);
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/convert", method = RequestMethod.POST)
	public @ResponseBody
	AjaxResponse convertJsonByMap(HttpServletRequest request) {

		try {
			Enumeration<String> _enum = request.getParameterNames();
			Map<String, String> map = new HashMap<String, String>();
			String parameterName = null;
			while (_enum.hasMoreElements()) {
				parameterName = (String) _enum.nextElement();

				String value = request.getParameter(parameterName);

				System.out.println(parameterName + "|" + value);
				map.put(parameterName, value);
			}

			String respnseContent = JsonBinder.buildNonDefaultBinder().toJson(
					map);

			return new AjaxResponse(true, respnseContent); 
		} catch (Exception ex) {
			ex.printStackTrace();
			return new AjaxResponse(false);
		}

	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request) {
		return "url/list";
	}
}
