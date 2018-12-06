package com.sinovatech.search.utils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *  cookie  操作类
 * createTime 2013-12-17 @author qizhongfei
 * modifyTime 2013-12-17 @author qizhongfei
 *
 */
public class CookieUtil { 

  /**
   *   得到cookie 字符串
 * @param request  request 对象
 * @param name  cookie 名称
 * @return
 */
public static String getCookieValue(HttpServletRequest request, String name) {
	  Cookie cookies = getCookie(request,name);
	  if(cookies!=null)
		  return cookies.getValue();
	  return null;
  }
  
  /**
   * 
   * 得到cookie 对象
 * @param request  request 对象
 * @param name cookie 名称
 * @return
 */
public static Cookie getCookie(HttpServletRequest request, String name) {
	    Cookie cookies[] = request.getCookies();
	    if (cookies == null || name == null || name.length() == 0) {
	      return null;
	    }
	    for (int i = 0; i < cookies.length; i++) {
//	    	System.out.println("cookies[i].getName() = "+cookies[i].getName() +"cookies[i].getValue() = "+cookies[i].getValue());
	      if (name.equals(cookies[i].getName())) {
	        return cookies[i];
	      }
	    }
	    return null;
	  }

  /**
   *  删除cookie 域（.10010.com） 
 * @param request request 对象
 * @param response  response 对象
 * @param jut   jut 字符串
 */
public static void deleteCookie(HttpServletRequest request,
      HttpServletResponse response, String jut) {
	  	Cookie cookie =  getCookie(request, jut);
	    if (cookie != null) {
	      cookie.setMaxAge(0);
	      cookie.setValue("");
	      cookie.setPath("/");
	      cookie.setDomain(".10010.com");
	      response.addCookie(cookie);
	    }
  }

  public static void setCookie(HttpServletRequest request,
      HttpServletResponse response, String name, String value) {
    setCookie(request, response, name, value, 0x278d00);
  }

  public static void setCookie(HttpServletRequest request,
      HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value == null ? "" : value);
    cookie.setMaxAge(maxAge);
    cookie.setPath(getPath(request));
    response.addCookie(cookie);
  }

  private static String getPath(HttpServletRequest request) {
    String path = request.getContextPath();
    return (path == null || path.length()==0) ? "/" : path;
  }

	public static String getSecurityCookie(HttpServletRequest request,
			String uacVerifyKey) {
		String cookieStr = request.getHeader("Cookie"); // 通过Header获取Cookie字符串,形如a=b;c=d;d=a=
														// 【先用；分割，再用=分割】
//		if (CommonUtils.isEmptyString(cookieStr)) {
//			return "";
//		}
		for (String element : cookieStr.split(";")) {
			element = element.trim();
			if (element.startsWith(uacVerifyKey + "=")) {
				return element.substring(uacVerifyKey.length() + 1);
			}
		}
		return "";
	}
	//获取省份编码
	public static String getProCode(HttpServletRequest request) {
		String pcode = null;
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("wlmcode".equals(cookie.getName())) {
						pcode = cookie.getValue();
						break;
					}
				}
			}
			if (pcode == null) {
				pcode = "110000";// 默认为北京
			}
		} catch (Exception e) {
			pcode = "110000";
			e.printStackTrace();
		}
		return pcode;
	}
}