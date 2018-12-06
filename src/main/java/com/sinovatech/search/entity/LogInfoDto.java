package com.sinovatech.search.entity;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.sinovatech.search.entity.abstractdto.LogInfoAbstractDto;
import com.sinovatech.search.utils.DateUtil;

@SuppressWarnings("serial")
public class LogInfoDto extends LogInfoAbstractDto {
	
    /**
	 * <pre>
	 * 返回字符串 已#分割
	 * 	1	 ID	唯一ID 32位	baseID,
	 * 	2	 VISIT_DATE	访问时间	visitDate,
	 * 	3	 VISIT_URL	访问URL	visitorUrl,
	 * 	4	 VISIT_DOMAIN	访问域名	visitDomain,
	 * 	5	 VISIT_IP	访问者来源IP	visitIP,
	 * 	6	 SESSION_ID	会话ID	sessionId,
	 * 	7	 VISIT_ID	cookie用户ID	visitId,
	 * 	8	 REFER_URL	访问来源URL地址	referUrl,
	 * 	9	 REFER_DOMAIN	访问来源域名	referDomain,
	 * 	10	 PAGE_TITLE	访问页面标题	pageTitle,
	 * 	11	 VISIT_UA	访问者浏览器userAgent	visitUA,
	 * 	12	 FLASH_VERSION	访问者浏览器flash版本	flashVersion,
	 * 	13	 SYSTEM_ID	系统ID	systemId,
	 * 	14	 SRC_COLOR	访问者屏幕color	srcColor,
	 * 	15	 SRC_SIZE	访问者屏幕宽度width*high	scrSize,
	 * 	16	 SCR_PIXEL	访问者屏幕pixelDepth	scrPixel,
	 * 	17	 COOKIES_VALUE	cookiesValue值	cookiesValue,	
	 * 	18	 LOCAL_IP	服务器本地IP	localIP,
	 * 	19	 DISTRICT	地域 -- 省份	visitPro,
	 * 	20	 APPNAME	浏览器名称	naviAppName,
	 * 	21	 BROWSERLANGUAGE	浏览器语言	naviBroLanguage,
	 * 	22	 COOKIEENABLED	是否启用cookie	naviCookiee,
	 * 	23	 CPUCLASS	浏览器系统CPU等级	naviCpuClass,
	 * 	24	 PLATFORM	操作系统平台	naviPlatForm,
	 * 	25	 SYSTEMLANGUAGE	OS默认语言	naviOsLanguage,
	 * 	26 	 USERLANGUAGE	OS 自然语言设置	naviUserLanguage,
	 * 	27	 CHANNEL	渠道类型	channel,
	 * 	28 	 SEARCH	搜索引擎	search,
	 * 	29 	 SEARCH_KEY	搜索词	searchKeyWord,
	 * 	30 	 BROWSER	浏览器	browser,
	 * 	31 	 OS	操作系统	os,
	 * 	32	 COOKIEPATH	访问路径PATH	cookiePath
	 * 	33	 DIME_ID	维度ID	dimeId;
	 * </pre>
	 */
    public String toString() {
        return toString(id, DateUtil.format(visitDate, "yyyy-MM-dd HH:mm:ss"), visitDomain, visitIP, visitId, visitUA,
                cookiesValue, localIP);
    }
    
    /**
     *  //唯一ID 32位
    protected String baseID;
    //访问日期
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
     *
     * @author ChenZhuo
     * @date 2016年12月14日 下午2:15:17
     * @return
     * @throws Exception
     */
    public String toJsonString() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("visitDate", DateUtil.format(visitDate, "yyyy-MM-dd HH:mm:ss"));
        map.put("visitDomain", visitDomain);
        map.put("visitId", visitId);
        map.put("cookiesValue", cookiesValue);
        map.put("visitIP", visitIP);
        map.put("visitUA", visitUA);
        map.put("visitUrl", visitUrl);
        map.put("refererUrl", refererUrl);
        map.put("queryParams", queryParams);
        map.put("browserType", browserType);
        map.put("browserVersion", browserVersion);
        map.put("queryParamsMap", queryParamsMap);
        return JSONObject.fromObject(map).toString();
    }
    
    
}