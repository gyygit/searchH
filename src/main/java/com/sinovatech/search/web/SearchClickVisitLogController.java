package com.sinovatech.search.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sinovatech.common.util.Validate;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.ClickVisitLogInfoDTO;
import com.sinovatech.search.entity.GroupBean;
import com.sinovatech.search.services.SearchClickVisitLogService;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.BaseController;

/**
 *
 * @author Ma Tengfei
 * @date 2016年12月22日 下午4:24:27
 */
@Controller
@RequestMapping("searchClickVisitLog")
public class SearchClickVisitLogController extends BaseController {
    
    @Autowired
    private SearchClickVisitLogService searchClickVisitLogService;
    
    private static final Log log = LogFactory.getLog(SearchClickVisitLogController.class);
    
    @RequestMapping("queryClickVisitLog")
    public String queryClickVisitLog(ClickVisitLogInfoDTO clickDto, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LimitInfo limit = new LimitInfo();
        limit.setFirstEnter(true);
        limit.setRowDisplayed(Consts.ROWS_PER_PAGE);
        
        getRequestPageNo(request, limit);
        
        List<ClickVisitLogInfoDTO> clickLogList = searchClickVisitLogService.getClickVisitLogList(limit, clickDto);
        String groupField1 = "targetUrl";//跳转目标地址字段
        String groupField2 = "searchKeyWord";//搜索关键词字段
        List<GroupBean> groupList1 = searchClickVisitLogService.group(groupField1);
        List<GroupBean> groupList2 = searchClickVisitLogService.group(groupField2);
        JSONArray json1 = JSONArray.fromObject(groupList1);
        JSONArray json2 = JSONArray.fromObject(groupList2);
        Map<String, Object> map = new HashMap<String, Object>();
        //获取登录用户信息
  		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
        map.put("limit", limit);
        map.put("list", clickLogList);
        map.put("groupJson1", json1.toString());
        map.put("groupJson2", json2.toString());
        map.put("username", username);
        request.setAttribute("json", JsonUtil.getJSONString(map));
        return "response";
    }
    
    @RequestMapping("/deleteClickLog")
    public String deleteClickLog(HttpServletRequest request,HttpServletResponse response)
    		throws Exception{
    	String ids = request.getParameter("ids");
    	Map<String, Object> map = new HashMap<String, Object>();
		Validate.notBlank(ids, "common", "errorparameter");
		try {
			this.searchClickVisitLogService.deleteTX(ids);
			log.info("Delete clickLog is ok ");
			map.put("flag", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Delete clickLog is error ");
			map.put("flag", "false");
		}
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
    }
}
