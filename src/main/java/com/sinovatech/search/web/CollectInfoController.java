package com.sinovatech.search.web;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.entity.ClickVisitLogInfoDTO;
import com.sinovatech.search.services.CollectInfoService;
import com.sinovatech.search.utils.StringUtil;
import com.sinovatech.search.utils.web.BaseController;

/**
 *
 * @author ChenZhuo
 * @date 2016年12月9日 下午3---------50---------51
 */
@Controller
@RequestMapping("collectinfo")
public class CollectInfoController extends BaseController {

    Logger logger = LoggerFactory.getLogger(CollectInfoController.class);
    
    @Autowired
    private CollectInfoService collectInfoService;
    
    @RequestMapping("receiveLogInfoToDocument")
    public void receiveLogInfoToDocument(HttpServletRequest request, HttpServletResponse response) throws AppException{
        //TODO  记录日志时间需要时分秒。但是在后面入库时，所有的时间格式都是“yyyy-MM-dd”
        collectInfoService.recordLogInfoToDocument(request, response);
    }
    
    @RequestMapping("receiveClickVisitLogInfo")
    public void receiveClickVisitLogInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String targetUrl = request.getParameter("url");
        String searchKeyWord = request.getParameter("kw");
        String beforeSearchKW = request.getParameter("bkw");
        String recordNumber = request.getParameter("rn");
        String inputEncoding = request.getParameter("ie");
        
        ClickVisitLogInfoDTO clickDTO = new ClickVisitLogInfoDTO();
        clickDTO.setTargetUrl(StringUtil.processValue(targetUrl));
        clickDTO.setSearchKeyWord(StringUtil.processValue(searchKeyWord));
        clickDTO.setBeforeSearchKW(StringUtil.processValue(beforeSearchKW));
        clickDTO.setRecordNumber(StringUtil.processValue(recordNumber));
        clickDTO.setInputEncoding(StringUtil.processValue(inputEncoding));
        
        clickDTO.processRequest(request);
        
        collectInfoService.intoClickVisitLogInfo(clickDTO);
        
        if(StringUtils.isEmpty(clickDTO.getTargetUrl())){
            response.sendRedirect(clickDTO.getRefererUrl());
        }else{
            response.sendRedirect(clickDTO.getTargetUrl());
        }
        
    }
    
    @RequestMapping("receiveInfo")
    public void receiveInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            logger.trace("======trace");  
            logger.debug("======debug");  
            logger.info("======info");
            logger.warn("======warn");
            logger.error("======error");
        }
        long endTime = System.currentTimeMillis();
        long time = endTime - beginTime;
        logger.info("总用时--------------{}", time);
        
        collectInfoService.recordLogInfo(request, response);
    }
    
    @RequestMapping("analyseRequest")
    public void analyseRequest(HttpServletRequest request, HttpServletResponse response){
        System.out.println("request.getContextPath()---------" + request.getContextPath());
        System.out.println("request.getLocalAddr()---------" + request.getLocalAddr());
        System.out.println("request.getLocalName()---------" + request.getLocalName());
        System.out.println("request.getLocalPort()---------" + request.getLocalPort());
        System.out.println("request.getRemotePort()---------" + request.getRemotePort());
        System.out.println("request.getRequestURL()---------" + request.getRequestURL());
        System.out.println("request.getSession()---------" + request.getSession());
        System.out.println("request.getAttributeNames()---------" + request.getAttributeNames());
        System.out.println("request.getCookies()---------" + request.getCookies());
        System.out.println("request.getLocale()---------" + request.getLocale());
        System.out.println("request.getLocales()---------" + request.getLocales());
        System.out.println("request.getParameterMap()---------" + request.getParameterMap());
        System.out.println("request.getParameterNames()---------" + request.getParameterNames());
        
        String agent = request.getHeader("user-agent");
        System.err.println(agent); 
        StringTokenizer st = new StringTokenizer(agent,";"); 
        st.nextToken(); 
        String userbrowser = st.nextToken(); 
        System.err.println(userbrowser); 
        String useros = st.nextToken(); 
        System.err.println(useros); 
        System.err.println("request.getHeader('user-agent')---------" + request.getHeader("user-agent")); //返回客户端浏览器的版本号、类型 
        System.err.println("request.getHeaderNames()---------" + request.getHeaderNames()); //：返回所有request header的名字，结果集是一个enumeration（枚举）类的实例 
        System.err.println("Scheme--------- " + request.getScheme()); 
        System.err.println("Server Name--------- " + request.getServerName());//获得服务器的名字 
        System.err.println("Server Port--------- " + request.getServerPort()); //获得服务器的端口号
        System.err.println("Protocol--------- " + request.getProtocol()); 
        System.err.println("Server Info--------- " + request.getSession().getServletContext().getServerInfo()); 
        System.err.println("Remote Addr--------- " + request.getRemoteAddr()); //获得客户端的ip地址
        System.err.println("Remote Host--------- " + request.getRemoteHost()); //获得客户端电脑的名字，若失败，则返回客户端电脑的ip地址
        System.err.println("Character Encoding--------- " + request.getCharacterEncoding()); 
        System.err.println("Content Length--------- " + request.getContentLength()); 
        System.err.println("Content Type--------- "+ request.getContentType()); 
        System.err.println("Auth Type--------- " + request.getAuthType()); 
        System.err.println("HTTP Method--------- " + request.getMethod()); //获得客户端向服务器端传送数据的方法有get、post、put等类型
        System.err.println("Path Info--------- " + request.getPathInfo()); 
        System.err.println("Path Trans--------- " + request.getPathTranslated()); 
        System.err.println("Query String--------- " + request.getQueryString()); 
        System.err.println("Remote User--------- " + request.getRemoteUser()); 
        System.err.println("Session Id--------- " + request.getRequestedSessionId());
        System.out.println(request.getSession().getId());
        System.err.println("Request URI--------- " + request.getRequestURI()); //获得发出请求字符串的客户端地址 
        System.err.println("Servlet Path--------- " + request.getServletPath()); //获得客户端所请求的脚本文件的文件路径
        
        System.err.println("Accept--------- " + request.getHeader("Accept")); 
        System.err.println("Accept-Language --------- " + request.getHeader("Accept-Language")); 
        System.err.println("Accept-Encoding --------- " + request.getHeader("Accept-Encoding")); 
        System.err.println("Host--------- " + request.getHeader("Host")); 
        System.err.println("Referer --------- " + request.getHeader("Referer")); 
        System.err.println("User-Agent --------- " + request.getHeader("User-Agent")); 
        System.err.println("Connection --------- " + request.getHeader("Connection")); 
        System.err.println("Cookie --------- " + request.getHeader("Cookie"));
    }

    
    
}
