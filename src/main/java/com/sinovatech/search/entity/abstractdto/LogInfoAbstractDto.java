package com.sinovatech.search.entity.abstractdto;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sinovatech.search.constants.LoggerConstants;
import com.sinovatech.search.entity.common.DtoSupport;
import com.sinovatech.search.utils.RequestUtils;
import com.sinovatech.search.utils.StringUtil;

/**
 * <pre>
 * 抽象搜索信息日志信息
 * 变量设置成 protected 是为了继承类直接使用
 * </pre>
 * 
 * @author herry
 * 
 */
@SuppressWarnings("serial")
public abstract class LogInfoAbstractDto extends DtoSupport {

	private static Logger logger = LoggerFactory.getLogger(LogInfoAbstractDto.class);

	/**
	 * 服务器本地IP
	 */
	@JsonIgnore
	protected static String localIP;

	static {
		if (localIP == null) {
			try {
				StringBuffer sb = new StringBuffer();
				Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
				NetworkInterface net = null;
				Enumeration<InetAddress> inetAddresses = null;
				InetAddress inetAddress = null;
				while (networkInterfaces.hasMoreElements()) {
					net = networkInterfaces.nextElement();
					inetAddresses = net.getInetAddresses();
					while (inetAddresses.hasMoreElements()) {
						inetAddress = inetAddresses.nextElement();
						if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().indexOf(":") == -1) {
							sb.append(inetAddress.getHostAddress());
							sb.append(LoggerConstants.COMMA);
						}
					}
				}
				if (sb.length() > LoggerConstants.MAXLENGTH) {
					localIP = sb.substring(0, LoggerConstants.MAXLENGTH);
				} else {
					localIP = sb.toString();
				}
			} catch (Exception e) {
				logger.error(LoggerConstants.LOGSPACESTR + " init localIP error " + LoggerConstants.LOGSPACESTR, e);
			}
			if (StringUtils.isBlank(localIP)) {
				localIP = "127.0.0.1";
			}
//			logger.info(LoggerConstants.LOGSPACESTR + " local ip is " + localIP + LoggerConstants.LOGSPACESTR);
		}
	}

    //唯一ID 32位
	protected String id;
	//访问日期
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	protected Date visitDate;
	//访问域名
    protected String visitDomain;
    //访问者ID cookie中的 VU_ID值
    protected String visitId;
    //cookie中的U_C_VS值，第三方记录
    protected String cookiesValue;
    //访问者来源IP
    protected String visitIP;
    //访问者浏览器userAgent
    protected String visitUA;
	//访问连接
    protected String visitUrl;
    //请求来源页面url
    protected String refererUrl;
    //请求参数
    protected String queryParams;
    //请求参数解析后形成的map
    protected Map<String, Object> queryParamsMap;
    //浏览器类型
    protected String browserType;
    //浏览器版本
    protected String browserVersion;
    
    //end字符串， 防止在最后出现空值，导致字符个数异常
    @JsonIgnore
    protected String endStr = "end";

	/**
	 * 解析 request
	 * 
	 * @param request
	 * @param response
	 */
	public void init(HttpServletRequest request, HttpServletResponse response) {
		initValue(request, response);
	}
	
	/**
	 * 
	 * <pre>
	 * 从request拿数据
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * 
	 * @author zouxiaomeng
	 */
	protected void initValue(HttpServletRequest request, HttpServletResponse response) {

	    
	    this.id = getUUID();
	    this.visitDate =  new Date();
	    this.visitUrl = decode(StringUtil.processValue(new String(request.getRequestURL())), LoggerConstants.UTF8);
	    this.refererUrl = decode(StringUtil.processValue(request.getHeader("Referer")), LoggerConstants.UTF8);
	    this.visitDomain = this.domain(this.visitUrl);
	    this.visitIP = RequestUtils.getIpByRequest(request);
	    this.visitId = RequestUtils.getCookie(request, LoggerConstants.COOKIES_VISITID);
        this.cookiesValue = RequestUtils.getCookie(request, LoggerConstants.COOKIES_VALUE);
        this.visitUA = RequestUtils.getHeader(request, "User-Agent", LoggerConstants.EMPTYSTR);
	    //this.sessionId = request.getRequestedSessionId();
        this.queryParams = request.getQueryString();
        this.queryParamsMap = RequestUtils.processQueryString(request);
        this.browserType = RequestUtils.getBrowserTypeAndVersionFromRequest(request)[0];
        this.browserVersion = RequestUtils.getBrowserTypeAndVersionFromRequest(request)[1];
	}

	/**
	 * 
	 * <pre>
	 * 根据URL字符串信息解析出域名
	 * </pre>
	 * 
	 * @param urlString
	 * @return
	 * 
	 * @author zouxiaomeng
	 */
	public String domain(String urlString) {
		if (StringUtils.isBlank(urlString)) {
			return LoggerConstants.EMPTYSTR;
		} else {
			try {
				URL url = new URL(urlString);
				return url.getHost();
			} catch (Exception e) {
				logError(e);
			}
			return LoggerConstants.EMPTYSTR;
		}
	}

	private void logError(Exception e) {
		logger.error(LoggerConstants.LOGSPACESTR + this.getClass() + " error " + LoggerConstants.LOGSPACESTR, e);
	}

	private String decode(String str, String charSet) {
		if (StringUtils.isBlank(str)) {
			return LoggerConstants.EMPTYSTR;
		} else {
			try {
				return URLDecoder.decode(str, charSet);
			} catch (Exception e) {
				return str;
			}
		}
	}

	private String getUUID() {
		return StringUtils.replace(UUID.randomUUID().toString().trim(), "-", LoggerConstants.EMPTYSTR);
	}
	
	public String toJsonString() throws Exception{
        return null;
    }

	/**
	 * 唯一ID
	 */
	public String getId() {
		return this.id;
	}
	
	/**
     * 设置id
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

	/**
	 * 访问日期
	 * 
	 * @return
	 */
	public Date getVisitDate() {
		return visitDate;
	}

    /**  
     * 获取localIP  
     * @return localIP localIP  
     */
    public static String getLocalIP() {
        return localIP;
    }

    /**
     * 设置localIP
     * @param localIP the localIP to set
     */
    public static void setLocalIP(String localIP) {
        LogInfoAbstractDto.localIP = localIP;
    }

    /**  
     * 获取visitDomain  
     * @return visitDomain visitDomain  
     */
    public String getVisitDomain() {
        return visitDomain;
    }

    /**
     * 设置visitDomain
     * @param visitDomain the visitDomain to set
     */
    public void setVisitDomain(String visitDomain) {
        this.visitDomain = visitDomain;
    }

    /**  
     * 获取visitId  
     * @return visitId visitId  
     */
    public String getVisitId() {
        return visitId;
    }

    /**
     * 设置visitId
     * @param visitId the visitId to set
     */
    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    /**  
     * 获取cookiesValue  
     * @return cookiesValue cookiesValue  
     */
    public String getCookiesValue() {
        return cookiesValue;
    }

    /**
     * 设置cookiesValue
     * @param cookiesValue the cookiesValue to set
     */
    public void setCookiesValue(String cookiesValue) {
        this.cookiesValue = cookiesValue;
    }

    /**  
     * 获取visitIP  
     * @return visitIP visitIP  
     */
    public String getVisitIP() {
        return visitIP;
    }

    /**
     * 设置visitIP
     * @param visitIP the visitIP to set
     */
    public void setVisitIP(String visitIP) {
        this.visitIP = visitIP;
    }

    /**  
     * 获取visitUA  
     * @return visitUA visitUA  
     */
    public String getVisitUA() {
        return visitUA;
    }

    /**
     * 设置visitUA
     * @param visitUA the visitUA to set
     */
    public void setVisitUA(String visitUA) {
        this.visitUA = visitUA;
    }

    /**  
     * 获取visitUrl  
     * @return visitUrl visitUrl  
     */
    public String getVisitUrl() {
        return visitUrl;
    }

    /**
     * 设置visitUrl
     * @param visitUrl the visitUrl to set
     */
    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }

    /**  
     * 获取refererUrl  
     * @return refererUrl refererUrl  
     */
    public String getRefererUrl() {
        return refererUrl;
    }

    /**
     * 设置refererUrl
     * @param refererUrl the refererUrl to set
     */
    public void setRefererUrl(String refererUrl) {
        this.refererUrl = refererUrl;
    }

    /**  
     * 获取queryParams  
     * @return queryParams queryParams  
     */
    public String getQueryParams() {
        return queryParams;
    }

    /**
     * 设置queryParams
     * @param queryParams the queryParams to set
     */
    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    /**
     * 设置visitDate
     * @param visitDate the visitDate to set
     */
    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }
    
    public String getPKfiledName() {
        return "id";
    }
	
	
}
