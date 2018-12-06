package com.sinovatech.search.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.luceneindex.task.IndexWriteTask;
import com.sinovatech.search.services.CallwsService;

@Controller
@RequestMapping("callws")
public class CallwsController {

	@Autowired
	private CallwsService callwsService;
	
	@RequestMapping(value = "/callsw")
	public String senddataxml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(callwsService.getXml());
		
		return null;
	}
	
	@RequestMapping(value = "/callswBypageNo")
    public String getDateXml(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        Integer pageNo = Integer.parseInt(request.getParameter("pageno"));
        response.getWriter().println(callwsService.getXml(pageNo));
        return null;
    }
	
}
