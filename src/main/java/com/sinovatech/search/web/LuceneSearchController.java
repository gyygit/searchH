package com.sinovatech.search.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sinovatech.search.ectable.limit.ExLimitUtil;
import com.sinovatech.search.ectable.limit.ILimitUtil;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.LuceneSearchDTO;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.services.LuceneSearchService;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.services.SearchCommandService;
import com.sinovatech.search.services.SearchRuleDateService;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.BaseController;

@Controller
@RequestMapping("luceneSearch")
public class LuceneSearchController extends BaseController{
	
	@Autowired
	private LuceneSearchService luceneSearchService;
	@Autowired
	private SearchCommandService searchCommandService;
	@Autowired
	private SearchRuleDateService searchRuleDateService;
	@Autowired
	private SearchAppService searchAppService;
	
	@RequestMapping(value = "/search")
	public String search(LuceneSearchDTO dto, HttpServletRequest request, HttpServletResponse respons) throws Exception{
		String tableId = "LuceneSearchExList";
		// 获取分页信息
		ILimitUtil limitUtil = new ExLimitUtil();
		LimitInfo limit = limitUtil.getLimitInfo(request, tableId,
				Consts.ROWS_PER_PAGE);
		String commandId = request.getParameter("commandId");
        String app_id = request.getParameter("app_id");
		getRequestPageNo(request, limit);// 翻页代码
		SearchCommandDTO commandDto = searchCommandService.get(commandId);
		SearchAppDTO appDTO = searchAppService.get(commandDto.getAppId());
		dto.setAppCode(appDTO.getAppCode());
		dto.setCommandCode(commandDto.getCommandCode());
		Map<String, Object> map = new HashMap<String, Object>();
		if(dto.getNames().size() == 0){
			LimitInfo limitInfo =  new LimitInfo();
			limitInfo.setRowDisplayed(100);
			String sql = " commandCode:" + commandDto.getCommandCode()+"~0 AND appId:"+commandDto.getAppId()+"~0";
			List<SearchRuleDateDTO> list = searchRuleDateService.list(limitInfo, sql);
			for(SearchRuleDateDTO ruleDate : list){
				dto.getNames().add(ruleDate.getFieldName());
			}
		}
		dto = luceneSearchService.search(dto, limit);
		map.put("limit", limit);
		map.put("m", dto);
        System.out.println(app_id);
        map.put("appid",app_id);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return  "response";
	}
	
	@RequestMapping(value = "/searchhot")
	public String searchHot( HttpServletRequest request, HttpServletResponse respons) throws Exception{
		String tableId = "LuceneSearchExList";
		// 获取分页信息
		ILimitUtil limitUtil = new ExLimitUtil();
		LimitInfo limit = limitUtil.getLimitInfo(request, tableId,
				Consts.ROWS_PER_PAGE);
		String commandId = request.getParameter("commandId");
        String app_id = request.getParameter("app_id");
		getRequestPageNo(request, limit);// 翻页代码
		SearchCommandDTO commandDto = searchCommandService.get(commandId);
		SearchAppDTO appDTO = searchAppService.get(commandDto.getAppId());
		Map<String, Object> map = new HashMap<String, Object>();
		List lst = luceneSearchService.searchHot(appDTO.getAppCode(), commandDto.getCommandCode(),limit);
		map.put("limit", limit);
        map.put("appid",app_id);
        map.put("commandId",commandId);
        map.put("list",lst);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return  "response";
	}
}
