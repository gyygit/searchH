 


/**
*
*/
package com.sinovatech.search.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.hql.ast.InvalidPathException;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.web.BaseController;

/**
* @author Administrator
* 
*/
public class SessionFilter  extends BaseController implements Filter {
	private static Log log = LogFactory.getLog(SessionFilter.class);

	private String[] noturi;
	private String[] staticNoturi = {".css",".js",".png",".jpg",".gif"};

	public void destroy() {

	}

	/**
	 * @param req
	 *  条件不符合 把 session 和 cookies 清除掉。
	 */
	private void removeSession(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute(Consts.SESSION_USER);
		//request.getSession().removeAttribute(Consts.JUT);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		doFilterMain(req, res, chain);
	}

	/**
	 * 
	 * 主函数
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doFilterMain(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		if (passStatic(request)) {//放行
			chain.doFilter(request, response);
		} else{
		// 判断是否有登录过滤限制
		if (check(request)) {//放行
			chain.doFilter(request, response);
			
		} else {
			// 如果未登录，则跳转值登录页
	       if (request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME)==null) {
	    	   cookiesErrorProc(request,response);
			     return;
			 } else {
				 chain.doFilter(request, response);			 
							
			 }
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
	private boolean passStatic(HttpServletRequest request){
		String uri = request.getRequestURI();
		for (int i = 0; i < staticNoturi.length; i++) {
			if(uri.lastIndexOf(staticNoturi[i]) != -1){
				return true;
			}
		}
		return false;
	}

	public void init(FilterConfig config) throws ServletException { 
		noturi = config.getInitParameter("noturi").split(",");
	}

	private void cookiesErrorProc(HttpServletRequest req,
			HttpServletResponse resp) { 
		// 跳转登录页
		String jumpUrl ="/searchH/phtml/login.html";//登陆页面
		log.info("jumpUrl======"+jumpUrl);
		try {
				if (req.getHeader("x-requested-with") != null&& req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){   
					//在响应头设置session状态    
					resp.setHeader("sessionstatus", "sessionOut"); 
				    resp.setHeader("loginUrl", jumpUrl);
					resp.getWriter().print("sessionOut");   
				}else{
				    //req.getRequestDispatcher(jumpUrl).forward(req, resp);
				    resp.sendRedirect(jumpUrl);
				}
			 log.info("jumpUrl ====== end");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
}
