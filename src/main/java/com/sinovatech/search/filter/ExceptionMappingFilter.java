package com.sinovatech.search.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinovatech.search.exceptions.AppException;
import com.sinovatech.search.utils.GeneralCommonUtil; 

public class ExceptionMappingFilter implements Filter  {
	
	private static final Log log = LogFactory.getLog(ExceptionMappingFilter.class);
	
	private String[] noturi;

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		if(check(req)){
			chain.doFilter(request, response);
		}else{
			String backdata = "data";
			String callback = "QueryMain.exceptionBack";
			try {
				backdata = req.getParameter("backdata");
				callback = req.getParameter("callback");
				 if(GeneralCommonUtil.isNullOrSizeZero(backdata) || GeneralCommonUtil.isNullOrSizeZero(callback)){
					 backdata = "data";
					 callback = "QueryMain.exceptionBack";
				 }// 不做强制验证，为了非ajax请求访问
				request.setAttribute("backdata", backdata);
				request.setAttribute("callback", callback);
				chain.doFilter(request, response);
			}catch (Exception e) {
				e.printStackTrace();
				log.error("请求发生异常："+e.getMessage(),e);
				String json =MessageForException.exceptionMessage("999999");
				Throwable t = e.getCause();
				if(t instanceof AppException){
					AppException ae = (AppException)t;
					json = MessageForException.exceptionMessage(ae.getErrorCode(), ae.getArgs());
				}
				request.setAttribute("backdata", "data");
				request.setAttribute("callback", "QueryMain.exceptionBack");
				request.setAttribute("json", json);
				request.getRequestDispatcher("/response.jsp").forward(request, response);
				
			}
		}
	}
	
	private boolean check(HttpServletRequest request){
		String uri = request.getRequestURI();
		for (int i = 0; i < noturi.length; i++) {
			if(uri.lastIndexOf(noturi[i]) != -1){
				return true;
			}
		}
		return false;
	}

	public void init(FilterConfig config) throws ServletException {
		noturi = config.getInitParameter("noturi").split(",");
	}

}
