package com.sinovatech.search.entity.abstractdto;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinovatech.search.constants.LoggerConstants;
import com.sinovatech.search.entity.common.DtoSupport;
import com.sinovatech.search.utils.RequestUtils;
import com.sinovatech.search.utils.StringUtil;

/**
 *记录点击日志实体类
 * @author Ma Tengfei
 * @date 2016年12月21日 下午4:53:53
 */
public abstract class ClickVisitLogInfoAbstractDTO extends DtoSupport {
    
    private static Logger logger = LoggerFactory.getLogger(LogInfoAbstractDto.class);

    private static final long serialVersionUID = -2795326063172257582L;
    
    //主键id
    protected String id;
    // 业务应用编码
    private String appCode;
    // 业务数据分类编码
    private String commandCode;
    //需要跳转的url
    protected String targetUrl;
    //搜索关键词
    protected String searchKeyWord;
    //上一个搜索关键词
    protected String beforeSearchKW;
    //搜索结果显示条数，默认10条，范围建议10-100条
    protected String recordNumber;
    //查询输入文字的编码，默认为gb2312
    protected String inputEncoding;
    //访问日期时间
    protected Date visitDate;
    //访问连接
    protected String visitUrl;
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
    //请求来源页面url
    protected String refererUrl;
    //请求参数
    protected String queryParams;
    //请求参数解析后形成的map
    //protected Map<String, Object> queryParamsMap;
    //浏览器类型
    protected String browserType;
    //浏览器版本
    protected String browserVersion;
    //sessionID
    protected String sessionId;
    
    public void processRequest(HttpServletRequest request) {

        
        this.id = getUUID();
        this.visitDate =  new Date();
        this.visitUrl = decode(StringUtil.processValue(new String(request.getRequestURL())), LoggerConstants.UTF8);
        this.refererUrl = decode(StringUtil.processValue(request.getHeader("Referer")), LoggerConstants.UTF8);
        this.visitDomain = this.domain(this.visitUrl);
        this.visitIP = RequestUtils.getIpByRequest(request);
        this.visitId = RequestUtils.getCookie(request, LoggerConstants.COOKIES_VISITID);
        this.cookiesValue = RequestUtils.getCookie(request, LoggerConstants.COOKIES_VALUE);
        this.visitUA = RequestUtils.getHeader(request, "User-Agent", LoggerConstants.EMPTYSTR);
        this.sessionId = request.getRequestedSessionId();
        this.queryParams = request.getQueryString();
//        this.queryParamsMap = RequestUtils.processQueryString(request);
        this.browserType = RequestUtils.getBrowserTypeAndVersionFromRequest(request)[0];
        this.browserVersion = RequestUtils.getBrowserTypeAndVersionFromRequest(request)[1];
    }

    private String getUUID() {
        return StringUtils.replace(UUID.randomUUID().toString().trim(), "-", LoggerConstants.EMPTYSTR);
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
    
    /**  
     * 获取id  
     * @return id id  
     */
    public String getId() {
        return id;
    }
    /**
     * 设置id
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**  
     * 获取appCode  
     * @return appCode appCode  
     */
    public String getAppCode() {
        return appCode;
    }
    /**
     * 设置appCode
     * @param appCode the appCode to set
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
    /**  
     * 获取commandCode  
     * @return commandCode commandCode  
     */
    public String getCommandCode() {
        return commandCode;
    }
    /**
     * 设置commandCode
     * @param commandCode the commandCode to set
     */
    public void setCommandCode(String commandCode) {
        this.commandCode = commandCode;
    }
    /**  
     * 获取targetUrl  
     * @return targetUrl targetUrl  
     */
    public String getTargetUrl() {
        return targetUrl;
    }
    /**
     * 设置targetUrl
     * @param targetUrl the targetUrl to set
     */
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
    /**  
     * 获取searchKeyWord  
     * @return searchKeyWord searchKeyWord  
     */
    public String getSearchKeyWord() {
        return searchKeyWord;
    }
    /**
     * 设置searchKeyWord
     * @param searchKeyWord the searchKeyWord to set
     */
    public void setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
    }
    /**  
     * 获取beforeSearchKW  
     * @return beforeSearchKW beforeSearchKW  
     */
    public String getBeforeSearchKW() {
        return beforeSearchKW;
    }
    /**
     * 设置beforeSearchKW
     * @param beforeSearchKW the beforeSearchKW to set
     */
    public void setBeforeSearchKW(String beforeSearchKW) {
        this.beforeSearchKW = beforeSearchKW;
    }
    /**  
     * 获取recordNumber  
     * @return recordNumber recordNumber  
     */
    public String getRecordNumber() {
        return recordNumber;
    }
    /**
     * 设置recordNumber
     * @param recordNumber the recordNumber to set
     */
    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }
    /**  
     * 获取inputEncoding  
     * @return inputEncoding inputEncoding  
     */
    public String getInputEncoding() {
        return inputEncoding;
    }
    /**
     * 设置inputEncoding
     * @param inputEncoding the inputEncoding to set
     */
    public void setInputEncoding(String inputEncoding) {
        this.inputEncoding = inputEncoding;
    }
    /**  
     * 获取visitDate  
     * @return visitDate visitDate  
     */
    public Date getVisitDate() {
        return visitDate;
    }
    /**
     * 设置visitDate
     * @param visitDate the visitDate to set
     */
    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
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
     * 获取queryParamsMap  
     * @return queryParamsMap queryParamsMap  
     */
    /*public Map<String, Object> getQueryParamsMap() {
        return queryParamsMap;
    }*/
    /**
     * 设置queryParamsMap
     * @param queryParamsMap the queryParamsMap to set
     */
    /*public void setQueryParamsMap(Map<String, Object> queryParamsMap) {
        if(StringUtils.isNotEmpty(this.queryParams)){
            Map<String, Object> map = new HashMap<String, Object>();
            String[] params = this.queryParams.split("&");
            if (params.length > 0) {
                for (String param : params) {
                    String[] kv = param.split("=");
                    map.put(kv[0], kv[1]);
                }
            }
            this.queryParamsMap =  map;
        }else{
            this.queryParamsMap =  null;
        }
        
        //this.queryParamsMap = queryParamsMap;
    }*/
    /**  
     * 获取browserType  
     * @return browserType browserType  
     */
    public String getBrowserType() {
        return browserType;
    }
    /**
     * 设置browserType
     * @param browserType the browserType to set
     */
    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }
    /**  
     * 获取browserVersion  
     * @return browserVersion browserVersion  
     */
    public String getBrowserVersion() {
        return browserVersion;
    }
    /**
     * 设置browserVersion
     * @param browserVersion the browserVersion to set
     */
    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }
    
    public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getPKfiledName() {
        return "id";
    }

}
