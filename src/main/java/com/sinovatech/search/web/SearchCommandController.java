package com.sinovatech.search.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sinovatech.common.util.Validate;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.ExLimitUtil;
import com.sinovatech.search.ectable.limit.ILimitUtil;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.services.SearchCommandService;
import com.sinovatech.search.services.SearchRuleDateService;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.BaseController;

/**
 * SearchCommand业务数据类型表 Controller类
 * 
 * 创建: 2014-11-14 13:25:06<br />
 * 
 * @author 作者liuzhenquan
 */
@Controller
@RequestMapping("searchCommand")
public class SearchCommandController extends BaseController {

	@Autowired
	private SearchCommandService searchCommandService;

	@Autowired
	private SearchAppService searchAppService;
	
	@Autowired
	private SearchRuleDateService searchRuleDateService;
	
	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>查询前初始化</li>
	 * <li>目的：初始化方法</li>
	 * </ul>
	 * </p>
	 * 
	 * @param searchCommandDTO
	 *            :值对象
	 * @param request
	 *            :request
	 * @param response
	 *            :response
	 * @param response
	 *            :HttpServletResponse
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/beforeQuerySearchCommand")
	public String beforeQuerySearchCommand(SearchCommandDTO searchCommandDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO 自定义实现
		return null;
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：初始化方法</li>
	 * <li>适用的前提条件</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @param mapping
	 *            :ActionMapping
	 * @param form
	 *            :ActionForm
	 * @param request
	 *            :HttpServletRequest
	 * @param response
	 *            :HttpServletResponse
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/querySearchCommand")
	public String querySearchCommand(SearchCommandDTO dto,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 列表控件的TableId值
		String tableId = "SearchCommandExList";
		// 获取分页信息
		ILimitUtil limitUtil = new ExLimitUtil();
		LimitInfo limit = limitUtil.getLimitInfo(request, tableId, Consts.ROWS_PER_PAGE);
		String appId = request.getParameter("app_id");
		getRequestPageNo(request, limit);// 翻页代码
		// 查询
		dto.setAppId(appId);
		List<SearchCommandDTO> list = searchCommandService.list(limit, dto);
		SearchAppDTO searchAppDTO = searchAppService.get(appId);
		// 设置分页信息
		limitUtil.setLimitInfo(request, limit);
		// 保存数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("limit", limit);
		dataMap.put("list", list);
		dataMap.put("app_id", appId);
		dataMap.put("indexType", searchAppDTO.getIndexType());
		dataMap.put("jisuanappcode", searchAppDTO.getAppCode());
		dataMap.put("state", searchAppDTO.getState());
		request.setAttribute("json", JsonUtil.getJSONString(dataMap));
		return "response";
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：初始化方法</li>
	 * <li>适用的前提条件</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @param mapping
	 *            :beforeAddSearchCommand
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/beforeAddSearchCommand")
	public String beforeAddSearchCommand(SearchCommandDTO searchCommandDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "searchCommand/SearchCommandAdd";
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：初始化方法</li>
	 * <li>适用的前提条件</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @param mapping
	 *            :ActionMapping
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/addSearchCommand")
	public String addSearchCommand(SearchCommandDTO searchCommandDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		searchCommandDTO.setOperator(username);
	
		String flag = this.searchCommandService.save(searchCommandDTO);
		
		if("OK".equals(flag) && "2".equals(searchCommandDTO.getExtend()))//map service
		{
			SearchRuleDateDTO searchRuleDateDTO =new SearchRuleDateDTO();
			searchRuleDateDTO.setId("");
			searchRuleDateDTO.setAppCode(searchCommandDTO.getAppCode());
			searchRuleDateDTO.setAppId(searchCommandDTO.getAppId());
			searchRuleDateDTO.setCommandCode(searchCommandDTO.getCommandCode());
			searchRuleDateDTO.setCommandName(searchCommandDTO.getCommandName());
			searchRuleDateDTO.setCreateTime(new Date());
			searchRuleDateDTO.setFieldIndexType(RedisKeyConst.Search.RULE_INDEX_TYPE_NOT_TOKENIZED);
			searchRuleDateDTO.setFieldName("LONGITUDE");
			searchRuleDateDTO.setFieldStoreType(RedisKeyConst.Search.RULE_STORE_TYPE_YES);
			searchRuleDateDTO.setFileldDateType(RedisKeyConst.Search.RULE_DATE_TYPE_STRING);
			searchRuleDateDTO.setIsIntelliSense(RedisKeyConst.Search.IS_INTELLISENSE_false);
			searchRuleDateDTO.setOperator(username);
			
			
			SearchRuleDateDTO searchRuleDateDTO1 =new SearchRuleDateDTO();
			searchRuleDateDTO1.setId("");
			searchRuleDateDTO1.setAppCode(searchCommandDTO.getAppCode());
			searchRuleDateDTO1.setAppId(searchCommandDTO.getAppId());
			searchRuleDateDTO1.setCommandCode(searchCommandDTO.getCommandCode());
			searchRuleDateDTO1.setCommandName(searchCommandDTO.getCommandName());
			searchRuleDateDTO1.setCreateTime(new Date());
			searchRuleDateDTO1.setFieldIndexType(RedisKeyConst.Search.RULE_INDEX_TYPE_NOT_TOKENIZED);
			searchRuleDateDTO1.setFieldName("LATITUDE");
			searchRuleDateDTO1.setFieldStoreType(RedisKeyConst.Search.RULE_STORE_TYPE_YES);
			searchRuleDateDTO1.setFileldDateType(RedisKeyConst.Search.RULE_DATE_TYPE_STRING);
			searchRuleDateDTO1.setIsIntelliSense(RedisKeyConst.Search.IS_INTELLISENSE_false);
			searchRuleDateDTO1.setOperator(username);

			this.searchRuleDateService.save(searchRuleDateDTO);
			this.searchRuleDateService.save(searchRuleDateDTO1);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		map.put("appId", searchCommandDTO.getAppId());
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}

	

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：初始化方法</li>
	 * <li>适用的前提条件</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @param mapping
	 *            :ActionMapping
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/beforeEditSearchCommand")
	public String beforeEditSearchCommand(SearchCommandDTO searchCommandDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		String appId = request.getParameter("appId");
		Map<String, Object> map = new HashMap<String, Object>();
		SearchCommandDTO m = null;
		if(id != null && !id.equals("") && !id.equals("undefined")){
			m = this.searchCommandService.get(id);
		}else{
			m = new SearchCommandDTO();
		}
		m.setAppId(appId);
		SearchAppDTO appDTO = searchAppService.get(appId);
		map.put("m", m);
		map.put("appDTO", appDTO);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：初始化方法</li>
	 * <li>适用的前提条件</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @param mapping
	 *            :ActionMapping
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/editSearchCommand")
	public String editSearchCommand(SearchCommandDTO searchCommandDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		//根据id获得searchapp，填充数据
		SearchCommandDTO m = this.searchCommandService.get(searchCommandDTO.getId());
		searchCommandDTO.setCreateTime(m.getCreateTime());
		searchCommandDTO.setUpdateTime(new Date());
//		searchCommandDTO.setOperator("1");
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		searchCommandDTO.setOperator(username);
		String flag = this.searchCommandService.update(searchCommandDTO);
		map.put("flag", flag);
		map.put("appId", m.getAppId());
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}


	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：初始化方法</li>
	 * <li>适用的前提条件</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @param mapping
	 *            :ActionMapping
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/detailSearchCommand")
	public String detailSearchCommand(SearchCommandDTO searchCommandDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(id, "common", "errorparameter");
		request.setAttribute("m", this.searchCommandService.get(id));
		return "searchCommand/SearchCommandView";
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：初始化方法</li>
	 * <li>适用的前提条件</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @param mapping
	 *            :ActionMapping
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/deleteSearchCommand")
	public String deleteSearchCommand(SearchCommandDTO searchCommandDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String ids = request.getParameter("ids");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(ids, "common", "errorparameter");
		String flag= "OK";
		Map<String, Object> map = new HashMap<String, Object>();
		flag = this.searchCommandService.deleteTX(ids);
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}

	@RequestMapping(value = "/queryForPageSearchCommand")
	public String queryForPageSearchCommand(SearchCommandDTO searchCommandDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Page<SearchCommandDTO> page = new Page<SearchCommandDTO>();
		getRequestPageNo(request, page, 10);
		// hql 如果为空 或 "" 则默认查该表的分页
//		String hql = "";
//		Object[] objWhere = null;
//		page = this.searchCommandService.page(page, hql, objWhere);
//		request.setAttribute("page", page);
		return "searchCommand/SearchCommandList";
	}

}
