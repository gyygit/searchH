package com.sinovatech.search.utils;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;




/**
 * <p>
 * 处理对于Request的一些得到客户提交参数和设置数据的方法
 * </p>
 * @author 王文广 2010-6-7
 *
 */
public class RequestUtils {
    /** the logger */
    private static Log logger = LogFactory.getLog(RequestUtils.class);
    private static String JUTstr = "JUT";
    /**
     * 得到request中对应parameter的参数的值，如果没有，返回null
     *
     * @param request http request
     * @param name param 参数名
     *
     * @return request中对应parameter的值
     */
    public static String getStringParameter(HttpServletRequest request, String name) {
        if (name == null) {
            return null;
        } else {
            return request.getParameter(name.trim());
        }
    }

	

    /**
     * 得到request中对应parameter的参数的值，如果没有，返回指定的缺省值
     *
     * @param request http request
     * @param name param 参数名
     * @param def 指定的缺省值
     *
     * @return request中对应parameter的值
     */
    public static String getStringParameter(HttpServletRequest request, String name, String def) {
        String value = getStringParameter(request, name);
        if (value == null) {
            return def;
        } else {
            return value;
        }
    }

    /**
     * 得到request中对应parameter的参数的值，如果没有，抛错
     *
     * @param request http request
     * @param name param 参数名
     *
     * @return request中对应parameter的值
     */
    public static int getIntegerParameter(HttpServletRequest request, String name) {
        return Integer.parseInt(request.getParameter(name));
    }

    /**
     * 得到request中对应parameter的参数的值，如果没有或出错，返回指定的缺省值
     *
     * @param request http request
     * @param name param 参数名
     * @param def 指定的缺省值
     *
     * @return request中对应parameter的值
     */
    public static int getIntegerParameter(HttpServletRequest request, String name, int def) {
        String value = request.getParameter(name);
        try {
            if (value != null) {
                return Integer.parseInt(value);
            }
        } catch (Exception ex) {
        }
        return def;
    }

    /**
     * 对应字符串的简短显示格式，方便页面显示
     * @param strview 要显示的字符串
     * @param len 短显示格式字符串的长度
     *
     * @return 指定字符串的短显示格式
     */
    public static String shortView(String strview, int len) {
        int strlen = strview.length();
        String strshortview = "";

        if (strlen > len) {
            strshortview = strview.substring(0, len) + "...";
        } else {
            strshortview = strview;
        }

        return strshortview;
    }

    /**
     * 得到request中对应parameter的参数的值，如果没有，抛错
     *
     * @param request http request
     * @param name param 参数名
     *
     * @return request中对应parameter的值
     */
    public static long getLongParameter(HttpServletRequest request, String name) {
        return Long.parseLong(request.getParameter(name));
    }

    /**
     * 得到request中对应parameter的参数的值，如果没有或出错，返回指定的缺省值
     *
     * @param request http request
     * @param name param 参数名
     * @param def 指定的缺省值
     *
     * @return request中对应parameter的值
     */
    public static long getLongParameter(HttpServletRequest request, String name, long def) {
        try {
            return getLongParameter(request, name);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 得到request中对应parameter的参数的值，如果没有，抛错
     *
     * @param request http request
     * @param name param 参数名
     *
     * @return request中对应parameter的值
     */
    public static float getFloatParameter(HttpServletRequest request, String name) {
        return Float.parseFloat(request.getParameter(name));
    }

    /**
     * 得到request中对应parameter的参数的值，如果没有或出错，返回指定的缺省值
     *
     * @param request http request
     * @param name param 参数名
     * @param def 指定的缺省值
     *
     * @return request中对应parameter的值
     */
    public static float getFloatParameter(HttpServletRequest request, String name, float def) {
        try {
            return getFloatParameter(request, name);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 得到request中对应parameter的参数的值，如果没有，抛错
     *
     * @param request http request
     * @param name param 参数名
     *
     * @return request中对应parameter的值
     */
    public static double getDoubleParameter(HttpServletRequest request, String name) {
        return Double.parseDouble(request.getParameter(name));
    }

    /**
     * 得到request中对应parameter的参数的值，如果没有或出错，返回指定的缺省值
     *
     * @param request http request
     * @param name param 参数名
     * @param def 指定的缺省值
     *
     * @return request中对应parameter的值
     */
    public static double getDoubleParameter(HttpServletRequest request, String name, double def) {
        try {
            return getDoubleParameter(request, name);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 得到request中对应Attribute的参数的值，如果没有，返回null
     *
     * @param request http request
     * @param name param 参数名
     *
     * @return request中对应Attribute的值
     */
    public static String getStringAttribute(HttpServletRequest request, String name) {
        if (name == null) {
            return null;
        } else {
            return (String) request.getAttribute(name.trim());
        }
    }

    /**
     * 得到request中对应Attribute的参数的值，如果没有，返回指定的缺省值
     *
     * @param request http request
     * @param name param 参数名
     * @param def 指定的缺省值
     *
     * @return request中对应Attribute的值
     */
    public static String getStringAttribute(HttpServletRequest request, String name, String def) {
        String value = getStringAttribute(request, name);

        if (value == null) {
            return def;
        } else {
            return value;
        }
    }

    /**
     * 得到request中对应Attribute的参数的值，如果没有，返回null
     *
     * @param request http request
     * @param name param 参数名
     *
     * @return request中对应Attribute的值
     */
    public static Object getAttribute(HttpServletRequest request, String name) {
        if (name == null) {
            return null;
        } else {
            return request.getAttribute(name.trim());
        }
    }

    /**
     * 得到request中对应Attribute的参数的值，如果没有，返回指定的缺省值
     *
     * @param request http request
     * @param name param 参数名
     * @param def 指定的缺省值
     *
     * @return request中对应Attribute的值
     */
    public static Object getAttribute(HttpServletRequest request, String name, Object def) {
        Object value = getAttribute(request, name);

        if (value == null) {
            return def;
        } else {
            return value;
        }
    }

    /**
     * 得到Session中的指定对象。
     *
     * @param req   reqeust对象
     * @param id    Session中对象名称
     *
     * @return Session中存放的对象
     */
    public static Object getSessionValue(HttpServletRequest req, String id) {
        HttpSession session = req.getSession(true);
        return session.getAttribute(id);
    }

    /**
     * 得到Session中的指定对象,如果为null，则返回缺省对象。
     *
     * @param req   reqeust对象
     * @param id    Session中对象名称
     * @param defaultObj    缺省对象
     *
     * @return Session中存放的对象
     */
    public static Object getSessionValue(HttpServletRequest req, String id, Object defaultObj) {
        if (getSessionValue(req, id) != null) {
            defaultObj = getSessionValue(req, id);
        }

        return defaultObj;
    }

    /**
     * 向session中存放对象。
     *
     * @param req   request对象
     * @param id    对象对应键值
     * @param value 要放到Session中的对象
     */
    public static void setSessionValue(HttpServletRequest req, String id, Object value) {
        HttpSession session = req.getSession(true);
        session.setAttribute(id, value);
    }

    /**
     * 移除Session中指定的对象。
     *
     * @param req   request对象
     * @param id    对象键值
     */
    public static void removeSessionValue(HttpServletRequest req, String id) {
        HttpSession session = req.getSession(true);
        Object value = session.getAttribute(id);

        if (value != null) {
            session.removeAttribute(id);
        }
    }

    /**
     * 得到request中对应parameter的参数的值,这个值是经过解码的值
     *
     * @param request http request
     * @param name param 参数名
     *
     * @return request中对应parameter的解码的值
     */
    public static String getUnEscapeParameter(HttpServletRequest request, String name) {
        String value = getStringParameter(request, name);

        if (value == null) {
            return "";
        } else {
            return unEscapeStr(value);
        }
    }

    /**
     * 对应解开客户端进行简单加密的字符串，进一步提高系统的安全性
     * 原理：对应客户端加密的字符串进行拆解，转为Unicode对应的数字，对每一个数字进行恢复的反向调整。
     * @param src   原加密字符串
     * @return String   解密后的字符串
     */
    public static String unEscapeStr(String src) {
        String ret = "";

        if (src == null) {
            return ret;
        }

        for (int i = src.length() - 1; i >= 0; i--) {
            int iCh = src.substring(i, i + 1).hashCode();

            if (iCh == 15) {
                iCh = 10;
            } else if (iCh == 16) {
                iCh = 13;
            } else if (iCh == 17) {
                iCh = 32;
            } else if (iCh == 18) {
                iCh = 9;
            } else {
                iCh = iCh - 5;
            }

            ret += (char) iCh;
        }

        //        logger.debug("unEscape: input=" + src + "   output=" + ret);
        return ret;
    }

    /**
     * 加密字符串，进一步提高系统的安全性
     * @param src   未加密字符串
     * @return String   加密后的字符串
     */
    public static String escapeStr(String src) {
        String ret = "";

        if (src == null) {
            return ret;
        }

        for (int i = src.length() - 1; i >= 0; i--) {
            int iCh = src.substring(i, i + 1).hashCode();

            if (iCh == 10) {
                iCh = 15;
            } else if (iCh == 13) {
                iCh = 16;
            } else if (iCh == 32) {
                iCh = 17;
            } else if (iCh == 9) {
                iCh = 18;
            } else {
                iCh = iCh + 5;
            }

            ret += (char) iCh;
        }

        //        logger.debug("unEscape: input=" + src + "   output=" + ret);
        return ret;
    }

    /**
     * 将request的头信息和参数信息输出到log中
     * @param request   http Request
     */
    public static void logRequest(HttpServletRequest request) {
        logger.debug("contentLength=" + request.getContentLength());
        logger.debug("ContentType=" + request.getContentType());
        Enumeration headers = request.getHeaderNames();
        String reqName;
        String reqValue;

        while (headers.hasMoreElements()) {
            reqName = (String) headers.nextElement();
            reqValue = request.getParameter(reqName);
            logger.debug("!!!!!!Header!!!!!!!!!!" + reqName + "=" + reqValue);
        }

        Enumeration names = request.getParameterNames();

        while (names.hasMoreElements()) {
            reqName = (String) names.nextElement();
            reqValue = request.getParameter(reqName);
            logger.debug("!!!!!!Request!!!!!!!!!!" + reqName + "=" + reqValue);
        }
    }

    /**
     * 将request的Parameter信息放置到Attribute中。
     * @param request   http Request
     */
    public static void paramToAttribute(HttpServletRequest request) {
        String reqName;
        String reqValue;
        Enumeration names = request.getParameterNames();

        while (names.hasMoreElements()) {
            reqName = (String) names.nextElement();
            reqValue = request.getParameter(reqName);
            request.setAttribute(reqName, reqValue);
        }
    }

    /**
     * 根据request中的信息判断是否是文件上传模式，因为此模式取不到任何的Parameter信息，
     * 所以才如此进行是否上传的判断。
     * @param request   request 对象
     * @return  true 是文件上传， false 非文件上传
     */
    public static boolean isFileUpload(HttpServletRequest request) {
        boolean isUpload;

        if (
            (request.getContentType() != null)
                && (request.getContentType().indexOf("multipart/form-data;") >= 0)) {
            isUpload = true;
        } else {
            isUpload = false;
        }

        return isUpload;
    }

    /**
     * 将html标记转化为规定标示符
     *
     * @param input 要转换的HTML
     * @return   HTML对应的文本
     */
    public static String escapeHTMLTags(String input) {
        if ((input == null) || (input.length() == 0)) {
            return input;
        }

        StringBuffer stringbuffer = new StringBuffer(input.length() + 6);

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            switch (c) {
                case 60: // '<'
                    stringbuffer.append("&lt;");
                    break;
                case 62: // '>'
                    stringbuffer.append("&gt;");
                    break;
                case 10: // '\n'
                    stringbuffer.append("<br>");
                    break;
                case 13: // '\r'
                    stringbuffer.append("<br>");
                    i++;
                    break;
                case 32: // ' '
                    stringbuffer.append("&nbsp;");
                    break;
                case 39: // '\''
                    stringbuffer.append("&acute;");
                    break;
                case 34: // '"'
                    stringbuffer.append("&quot;");
                    break;
                /*case 63: // '?'
                   stringbuffer.append("~`~");
                    break;*/
                default:
                    stringbuffer.append(c);
                    break;
            }
        }
//        logger.debug("source=[" + input + "] result=[" + stringbuffer + "]");
        return stringbuffer.toString();
    }

    /**
     * 将文档内容中不符合Js变量要求的字符替换处理，使可以被Js使用
     *
     * @param input    要转换的doc文档内容
     * @return         Js可以使用的文本
     */
    public static String escapeDocToJs(String input) {
        if ((input == null) || (input.length() == 0)) {
            return input;
        }

        //得到回车的字符串
        byte[] btEnter = new byte[2];
        btEnter[0] = 13;
        btEnter[1] = 10;
        String strEnter = new String(btEnter);

        //首先成对替换掉"
        int iCount = 0;

        while (input.indexOf("\"") >= 0) {
            if ((iCount % 2) == 0) { //引号起始
                input = input.replaceFirst("\"", "“");
            } else { //引号结束
                input = input.replaceFirst("\"", "”");
            }

            iCount++;
        }

        //logger.debug(input);
        //然后替换掉换行符
        input = input.replaceAll(strEnter, "\" + String.fromCharCode(11) + \""); //JS可以解析的回车符
        return input;
    }



    /**
     * 解码方法
     * @param str       解码对象
     * @param encoding  解码采用字符集
     * @return  解密后的对象
     */
    public static String decodeStr(String str, String encoding) {
        try {
            return URLDecoder.decode(str, encoding);
        } catch (Exception e) {
            logger.debug("解码错误：" + str, e);
            return str;
        }
    }

    /**判断一字符串是否在某数组中
     * @param arr   数组对象
     * @param str   字符串对象
     * @return  是否包含
     */
    public static boolean isContain(String[] arr, String str) {
        boolean flag = false;

        for (int j = 0; j < arr.length; j++) {
            if (arr[j].equals(str)) {
                flag = true;
                break;
            } else {
                flag = false;
            }
        }

        return flag;
    }


    /**
     * 将list放到集合中
     * @param list ArrayList
     * @return set
     *
     */
    public static final Set addArrayList(ArrayList list) {
        Set set = new HashSet();
        Object[] obj = list.toArray();

        for (int i = 0; i < obj.length; i++) {
            set.add(obj[i]);
        }

        return set;
    }

    /**
     * 通过Dispatcher方式向JSP或Servlet跳转.
     *
     * @param req   reqeust对象
     * @param res   response对象
     * @param url   目标的URL相对路径
     * @throws  HDException 当跳转到JSP页面出错时
     */
    public static void sendDisp(HttpServletRequest req, HttpServletResponse res, String url)
         {
        try {
            RequestDispatcher rd = null;

            if (!url.substring(0, 1).equals("/")) {
                url = "/" + url;
            }

            rd = req.getRequestDispatcher(url);
            rd.forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new HDException(e.getMessage(), e);
        }
    }

    /**
     * 通过Direct方式向JSP或Servlet跳转.
     *
     * @param res   response对象
     * @param url   目标的URL相对路径
     */
    public static void sendDir(HttpServletResponse res, String url) {
        try {
            res.sendRedirect(url);
        } catch (java.io.IOException e) {
            logger.error("Unable to redirect to /" + url, e);
        }
    }

    /**
     * 通过JavaScript方式跳转页面.
     *
     * @param res   request对象
     * @param url   目标的URL绝对路径
     * @param holdhistory 是否在浏览器中保留历史纪录，true保留，false不保留
     */
    public static void sendReplace(HttpServletResponse res, String url , boolean holdhistory) {
        PrintWriter out = null;
        try {
            out = res.getWriter();
            out.println();
            out.println("<script language=\"javascript\">");
            if (holdhistory) {
                out.println("    window.location.href=\"" + url + "\";");
            } else {
                out.println("    window.location.replace(\"" + url + "\");");
            }

            out.println("</script>");
        } catch (IOException ioe) {
            logger.error("Unable to replace to /" + url, ioe);
        }
    }


    /**
     * 格式化数据，调整小数位数为指定的长度
     * @param inputNumber       需要被格式化的数字
     * @param pattern           要格式化的数字格式，支持#号和%号
     *      例如输入        10000
     *      #0.00           10000.00
     *      #               10000
     *      #,##0.00        10,000.00
     * @see java.text.DecimalFormat
     * @return                  格式化后的结果
     */
    public static String formatNumber(String inputNumber, String pattern) {
        try {
            DecimalFormat nf = new DecimalFormat(pattern);
            return nf.format(Double.parseDouble(inputNumber));
        } catch (Exception e) {
            logger.error("formatNumber error:" + inputNumber + " pattern:" + pattern, e);
            return inputNumber;
        }
    }

    /**
     * 根据指定小数位数格式化数据
     *
     * @param inputNumber  需要被格式化的数字
     * @param dec 要保留的小数位数
     *
     * @return 格式化后的结果
     */
    public static String formatNumber(String inputNumber, int dec) {
        StringBuffer pattern = new StringBuffer();
        pattern.append("0.");

        for (int i = 0; i < dec; i++) {
            pattern.append("0");
        }

        inputNumber = formatNumber(inputNumber, pattern.toString());

        if (dec == 0) {
            return inputNumber.substring(0, inputNumber.length() - 1);
        } else {
            return inputNumber;
        }
    }

    /**
     * 将Unicode转化为中文
     * @param s 需要转化的字符串
     * @param encoding  要转化的字符集
     * @return 首页URL前缀
     */
    public static String unicodeToCharset(String  s, String encoding) {
      try {
          if (s == null || s.equals("")) {
              return  "";
          }
          String newstring = null;
          newstring = new  String(s.getBytes("ISO8859_1"), encoding);
          return  newstring;
       } catch (Exception  e)  {
           return  s;
       }
    }

    /**
     * 将Unicode转化为中文
     * @param s 需要转化的字符串
     * @return 首页URL前缀
     */
    public static String unicodeToChinese(String  s) {
      try {
          if (s == null || s.equals("")) {
              return  "";
          }
          String newstring = null;
          newstring = new  String(s.getBytes("ISO8859_1"), "gb2312");
          return  newstring;
       } catch (Exception  e)  {
           return  s;
       }
    }
    /**
     * 获取strUrl响应结果文本
     * @param strUrl strUrl
     * @return  strUrl响应结果文本
     * @throws HDException 例外
     */
    public static String getResponseText(String strUrl) {
        try {
            URL url = new URL(strUrl);
            URLConnection httpConn = url.openConnection();
            httpConn.setConnectTimeout(90000);
            httpConn.setReadTimeout(90000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(str);
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
          e.printStackTrace();
        }
		return strUrl;
    }

    /**
     * 根据reuqest对象得到对应的URL的全名称信息，比如：http://www.sina.com.cn:81
     * @param request   request对象
     * @return          URL全名称字符串
     */
    public static String getHttpURL(HttpServletRequest request) {
        StringBuffer sbURL = new StringBuffer(request.getProtocol());
        String[] protocol = request.getProtocol().split("/"); //取得协议前面的字母，如HTTP/1.1,变为"HTTP","1.1"
        sbURL.append(protocol[0]).append("://").append(request.getServerName());
        int port = request.getServerPort(); //取得端口值
        if (port != 80) { //查看端口是否为80，如果不是还需要在联接上加上端口
            sbURL.append(":").append(port);
        }
        return sbURL.toString();
    }

    /**
     * 是否ajax请求
     * @param request request
     * @return 是否ajax请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return RequestUtils.getStringParameter(request, "isAjaxRequest", "false").equals("true") ||
            RequestUtils.getStringParameter(request, "act", "").equals("ajax");
    }
    /**
     * 获取系统路径,自动修正末尾分割符
     * @param request request
     * @return realPath
     */
    public static String getRealPath(HttpServletRequest request) {
        String realPath = request.getRealPath("/");
        if (!realPath.endsWith("/") && !realPath.endsWith("\\")) {
            realPath += "/";
        }
        return realPath;
    }


 
    /**
     * @param request   验证 cookies是空   但session不为空 
     * @return
     */
    public static boolean cookieSessionCheck(HttpServletRequest request) {
    	String cookiesJut = CookieUtil.getCookieValue(request, JUTstr);
		if(!StringUtils.hasLength(cookiesJut)){
			if (request.getSession().getAttribute(JUTstr)!=null) {
				return true;
			}
		}
        return false;
    }

	public static boolean checkIsMustLogin(String uri) {
		String u = uri.substring(uri.indexOf("/", 2),uri.length());
		String flag = SingletonUrlCheck.getInstance().getUrlMap().get(u);
		if(StringUtils.hasLength(flag)){
			return true;
		}
		return false;
	}
	
	/**
	 * @功能:根据request获取发起请求用户的id地址
	 * @param request
	 * @return
	 */
	public static String getIpByRequest(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.indexOf(",") != -1) {
            String[] ips = ip.split(",");
            if (ips.length > 0 && !ips[0].equalsIgnoreCase("unknown")) {
                ip = ips[0];
            }
        }
        return "0:0:0:0:0:0:0:1".equals(ip)?"127.0.0.1":ip;
	}
	
	public static String getCookie(HttpServletRequest request, String key) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key))
                    return cookie.getValue();
            }
        }
        return null;
    }
	
	public static void setCookie(HttpServletResponse response,
            String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        // 有效期十年
        cookie.setMaxAge(315360000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void setCookieSession(HttpServletResponse response,
            String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String key,
            String defaultValue) {
        String value = getCookie(request, key);
        return value != null ? value : defaultValue;
    }

    public static String getHeader(HttpServletRequest request, String key,
            String defaultValue) {
        String value = request.getHeader(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 解析出url请求的路径，包括页面
     * 
     * @param strURL
     *            url地址
     * @return url路径
     */
    public static String getUrlPage(String strURL) {
        String strPage = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 0) {
            if (arrSplit.length > 1) {
                if (arrSplit[0] != null) {
                    strPage = arrSplit[0];
                }
            }
        }

        return strPage;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     * 
     * @param strURL
     *            url地址
     * @return url请求参数部分
     */
    private static String getTruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * 
     * @param URL
     *            url地址
     * @return url请求参数部分
     */
    public static Map<String, String> getURLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = getTruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        // 每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            // 解析出键值
            if (arrSplitEqual.length > 1) {
                // 正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    // 只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 把request中URL连接后所带参数，处理成map
     *
     * @author ChenZhuo
     * @date 2016年12月13日 下午5:28:45
     * @param request
     * @return
     */
    public static Map<String, Object> processQueryString(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String queryParams = request.getQueryString();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(queryParams)) {
            String[] params = queryParams.split("&");
            if (params.length > 0) {
                for (String param : params) {
                    String[] kv = param.split("=");
                    map.put(kv[0], kv[1]);
                }
            }
        }
        return map;
    }
    
    /**
     * 从request中获取浏览器类型及版本
     * IE浏览器类型及版本参考：https://msdn.microsoft.com/en-us/library/ms537503(v=vs.85).aspx
     *
     * @author ChenZhuo
     * @date 2016年12月14日 上午10:17:55
     * @param request
     * @return
     */
    public static String[] getBrowserTypeAndVersionFromRequest(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        String browserType = "", browserVersion = "";
        if(!org.apache.commons.lang.StringUtils.isEmpty(userAgent)){
            if(userAgent.contains("Chrome")){
                //Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36
                browserType = "Chrome";
                String temp = userAgent.substring(userAgent.indexOf("Chrome/") + 7);
                temp = temp.trim();
                if(temp.indexOf(" ") < 0){
                    browserVersion = temp;
                }else{
                    browserVersion = temp.substring(0, temp.indexOf(" "));
                }
            }else if(userAgent.contains("Firefox")){
                //Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0
                browserType = "Firefox";
                String temp = userAgent.substring(userAgent.indexOf("Firefox/") + 8);
                temp = temp.trim();
                if(temp.indexOf(" ") < 0){
                    browserVersion = temp;
                }else{
                    browserVersion = temp.substring(0, temp.indexOf(" "));
                }
            }else if(userAgent.contains("MSIE")){
                if (userAgent.contains("MSIE 10.0")) {//Internet Explorer 10 
                    browserType = "IE";
                    browserVersion = "10";
                } else if (userAgent.contains("MSIE 9.0")) {//Internet Explorer 9  
                    browserType = "IE";
                    browserVersion = "9";  
                } else if (userAgent.contains("MSIE 8.0")) {//Internet Explorer 8  
                    browserType = "IE";
                    browserVersion = "8";  
                } else if (userAgent.contains("MSIE 7.0")) {//Internet Explorer 7  
                    browserType = "IE";
                    browserVersion = "7";  
                } else if (userAgent.contains("MSIE 6.0")) {//Internet Explorer 6  
                    browserType = "IE";
                    browserVersion = "6";  
                }  
            }else if(userAgent.contains("Trident/7.0")){
                browserType = "IE";
                browserVersion = "11";
            }else{
                //暂时收集以上三个主流.其它浏览器,待续...
                browserType = "";
                browserVersion = "";
            }
        }
        String tv = browserType + "#" + browserVersion;
        return tv.split("#");
    }
	
}
