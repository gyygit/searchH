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

import com.sinovatech.bms.client.bean.User;
import com.sinovatech.bms.client.loader.AbstractComponentLoader;
import com.sinovatech.bms.client.loader.ComponentLoaderFactory;
import com.sinovatech.bms.client.sso.SSOLoginAction;
import com.sinovatech.common.util.StringUtils;
import com.sinovatech.search.constants.RedisKeyConst;
/***
 * 
* @ClassName: BmsIntegrationSupermanFilter
* @Description: 
* @author Ma Tengfei
* @date 2017年6月1日 下午1:49:53
*
 */
public class BmsIntegrationSupermanFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		//接bms 获取登录用户信息
		AbstractComponentLoader loader = ComponentLoaderFactory.getCurrentComponentLoader();
		if(loader!=null){
			SSOLoginAction ssoLogin = loader.getSSOLoginAction();
			User bmsuser = ssoLogin.getCurrentUser(req);
			if(bmsuser!=null){
				String loginname = bmsuser.getUserLoginName();
				String realname = bmsuser.getUserRealName();
				if("admin".equals(loginname)){
					req.getSession().setAttribute(RedisKeyConst.Search.CALL_WS_USERNAME, realname);
					req.getSession().setAttribute(RedisKeyConst.Search.SESSION_USERCODE, loginname);
				}else{
					req.getSession().setAttribute(RedisKeyConst.Search.CALL_WS_USERNAME, "超级管理员");
					req.getSession().setAttribute(RedisKeyConst.Search.SESSION_USERCODE, "admin");
				}
			}
		}
		
		//从session 获取用户信息
		String usercode = (String) req.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		String username = (String) req.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		
		if(StringUtils.isBlank(usercode) && StringUtils.isBlank(username)){
			//如果从session中获取不到用户信息，就直接赋值admin
			req.getSession().setAttribute(RedisKeyConst.Search.CALL_WS_USERNAME, "超级管理员");
			req.getSession().setAttribute(RedisKeyConst.Search.SESSION_USERCODE, "admin");
		}
		
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
	}

}
