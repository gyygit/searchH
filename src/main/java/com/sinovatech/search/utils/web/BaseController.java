package com.sinovatech.search.utils.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.ExLimitUtil;
import com.sinovatech.search.ectable.limit.ILimitUtil;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;

public class BaseController {
	private String SERVER_PROTOCOL="http://";
	/**
	 *  获取应用名 
	 * @param request
	 * @return 
	 */
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}
	
	/**
	 * 获取域名及端口
	 * @param request
	 * @return 
	 */
	public String getServerNamePort(HttpServletRequest request) {
		int port=request.getServerPort();
		String serverport="";
		if(port!=80){
			serverport+=":"+ String.valueOf(request.getServerPort());
		}
		return (SERVER_PROTOCOL + request.getServerName()+serverport);
	}
	
	/**  获取域名+端口+应用名
	 * @param request
	 * @return
	 */
	public String getServerPath(HttpServletRequest request) {
		return (getServerNamePort(request) +getContextPath(request));
	}
	
	public void getRequestPageNo(HttpServletRequest request, LimitInfo limit) {
		String pageNo = request.getParameter("pageNo");
		int pageNoInt = 1;
		if (pageNo == null) {
		} else {
			try {
				pageNoInt = Integer.parseInt(pageNo);
			} catch (Exception e) {
				pageNoInt = 1;
			}
		}
		
		limit.setPageNum(pageNoInt);
	}

	public <T> void getRequestPageNo(HttpServletRequest request, Page<T> page,
			int pageSize) {
		String pageNo = request.getParameter("pageNo");
		int pageNoInt = 1;
		int pageSize1 = 10;
		if (pageNo == null) {
		} else {
			try {
				pageNoInt = Integer.parseInt(pageNo);
				if (pageSize != 0) {
					pageSize1 = pageSize;
				}
			} catch (Exception e) {
				pageNoInt = 1;
				pageSize1 = 10;
			}
		}

		page.setPageNo(pageNoInt);
		page.setPageSize(pageSize1);
		page.setAutoCount(true); // 统计总数
	}

	/*public int checkLogin(HttpServletRequest request,
			HttpServletResponse response) {
		int yes = 0;
		// 删除原有的cookie
		if (CookieUtil.getCookieValue(request, Consts.JUT) == null
				|| JUTUtils.parseLoginCookie(CookieUtil.getCookieValue(request,
						Consts.JUT)) == null) {
			log.info("如果不存在cookie信息，则删除....");
			removeSession(request, response);
			CookieUtil.deleteCookie(request, response, Consts.JUT); // JUT
			yes = 1;
		}

		// 如果当前登录用户与原有用户不一致，则从session中清除原有数据
		if (request.getSession().getAttribute(Consts.SESSION_USER) != null) {
			boolean flag = RequestUtils.cookieCheck(request);
			log.info("flag=" + flag);
			if (flag) {
				request.getSession().removeAttribute(Consts.SESSION_USER);
				request.getSession().removeAttribute(Consts.JUT);
				yes = 1;
			}
		}
		boolean flag = RequestUtils.cookieSessionCheck(request);
		log.info("cookieSessionCheck..flag=" + flag);
		if (flag) {
			// 如果session中有值，但是cookie为空，移除session信息
			removeSession(request, response);
			yes = 1;
		}
		// 1未登录；0已登录
		return yes;
	}*/

	public String getUserInfoFromSession(HttpServletRequest request) {
		String username = (String) request.getSession()
				.getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		return username;
	}

	public void returnOutput(String string, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (out != null) {
			out.write(string);
			out.flush();
			out.close();
		}

	}
	
	/**
    *
    * @author ChenZhuo
    * @date 2016年12月9日 下午4:21:57
    * @param request
    * @param response
    * @param dataString
    * @throws UnsupportedEncodingException
    * @throws IOException
    */
   public void out(HttpServletRequest request, HttpServletResponse response, String dataString)
           throws UnsupportedEncodingException, IOException {
       request.setCharacterEncoding("utf-8");
       response.setContentType("text/html;charset=UTF-8");
       PrintWriter out = null;
       try {
           out = response.getWriter();
       } catch (IOException e) {
           e.printStackTrace();
       }
       if (out != null) {
           out.write(dataString);
           out.flush();
           out.close();
       }
   }
	
	/**
	 * 得到LimitInfo分页
	 * @param request
	 * @param tableId 列表控件的TableId值
	 * @param paramInt 一页显示多少条
	 * @return
	 */
	public LimitInfo getLimitInfo(HttpServletRequest request,String tableId,int paramInt ){
		int n = Integer.valueOf(request.getParameter("pageNo") == null ? "1"
				: request.getParameter("pageNo"));
		// 获取分页信息
		ILimitUtil limitUtil = new ExLimitUtil();
		LimitInfo limit = limitUtil.getLimitInfo(request, tableId, paramInt);
		getRequestPageNo(request, limit);// 翻页代码
		limit.setPageNum(n);
		//分页结束
		return limit;

	}
	
	public boolean isThirdLogin(String type){
		if("99".equals(type)||"98".equals(type)||"96".equals(type)||"93".equals(type)||"94".equals(type)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 客户端使用方法
	 * */
	//得到user-agent
	protected String getUserAgent(HttpServletRequest request){
		return request.getHeader("user-agent");
	}
	/**
	 * @param request
	 * @return 是否是客户端访问
	 */
	public boolean getIsClient(HttpServletRequest request) {
		String ua = getUserAgent(request);
		if (ua.indexOf("#!") != -1) {
			return true;
		} else {
			return false;
		}
	}
 
}
