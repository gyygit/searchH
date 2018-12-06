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
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.web.BaseController;

/**
 * @author Administrator
 * 
 */
public class AuthFilter  extends BaseController implements Filter {
	private static Log log = LogFactory.getLog(AuthFilter.class);

	private String[] noturi;

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

		String uri = request.getRequestURI();
		String  referer = request.getHeader("referer");
		request.getSession().setAttribute("referer", referer);
		log.info(request.getRemoteAddr() + "\tvisite\t\n" + uri);
		log.info(request.getRemoteAddr() + "\referer\t\n" + referer);
		// 判断是否有登录过滤限制
		if (check(request)) {//放行
			chain.doFilter(request, response);
			
		} else {
			// 如果未登录，则跳转值登录页
	       if (request.getSession().getAttribute(Consts.SESSION_USER)==null) {
	    	   cookiesErrorProc(request,response);
			     return;
			 } else {
				 this.doLocalFilter(request, response, chain);			 
							
			 }
		}
	}
	
	/**
	 *  
     *过滤是否完善昵称和电话
	 * */
	private void doLocalFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		try {
			int rint = improve(request); 
			if(rint!=0)
			{
				String userEit = GlobalConfig.getProperty("url", "userEit");//不过滤路径---完善客户信息
				if(userEit.indexOf(uri)!=-1)
				{
					chain.doFilter(request, response);
				}else{
					String nickEdit = GlobalConfig.getProperty("url", "nickEdit");//完善客户信息 
					if (request.getHeader("x-requested-with") != null&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){   
						//在响应头设置session状态    
						response.setHeader("sessionstatus", "sessionOut"); 
						response.setHeader("loginUrl", nickEdit);
						response.getWriter().print("sessionOut");   
						return ;
					}else{
//						request.getRequestDispatcher("/wlm/view/login.jsp").forward(request, response);
						request.getRequestDispatcher("/searchH/").forward(request,response);
					}
				}
			}else if(rint==0){
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			if (e instanceof InvalidPathException) {
				log.info("找不到指定页面" + uri + "跳转至异常页面");
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
				log.info("访问" + uri + "异常，跳转至异常页面");
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			}
		}
	}
	//判断是否完善个人昵称和安全手机号
	/**
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @return 0 都已经完善,1未完善昵称,2未完善电话号码
	 * @throws ServletException
	 * @throws IOException
	 */
    private int  improve(HttpServletRequest request)
			throws ServletException, IOException {
    	int rint=0;
    	 
    	 return rint;
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

	private void cookiesErrorProc(HttpServletRequest req,
			HttpServletResponse resp) { 
		// 跳转登录页
		String largeUrl = GlobalConfig.getProperty("searchh", "largeUrl");//登陆页面
//		String jumpUrl = GlobalConfig.getProperty("url", "jumpUrl");//回调页面
		try {
			if (req.getHeader("x-requested-with") != null&& req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){   
				//在响应头设置session状态    
				resp.setHeader("sessionstatus", "sessionOut"); 
				resp.setHeader("loginUrl", largeUrl);
//				resp.setHeader("loginUrl", largeUrl+"&redirect_uri="+jumpUrl);
				resp.getWriter().print("sessionOut");   
			}else{
				req.getRequestDispatcher("/searchH/").forward(req,resp);
//				req.getRequestDispatcher("/wlm/view/login.jsp").forward(req, resp);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (ServletException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
}
