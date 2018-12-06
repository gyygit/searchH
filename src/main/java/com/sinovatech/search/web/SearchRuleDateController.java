package com.sinovatech.search.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.common.util.Validate;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.ExLimitUtil;
import com.sinovatech.search.ectable.limit.ILimitUtil;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.AjaxResponse;
import com.sinovatech.search.utils.web.BaseController;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchMessageDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.services.SearchCommandService;
import com.sinovatech.search.services.SearchMessageService;
import com.sinovatech.search.services.SearchRuleDateService;

/**
 * SearchRuleDate索引数据配置表 Controller类
 * 
 * 创建: 2014-11-14 13:25:11<br />
 * 
 * @author 作者liuzhenquan
 */
@Controller
@RequestMapping("searchRuleDate")
public class SearchRuleDateController extends BaseController {
	private static final Log log = LogFactory.getLog(SearchRuleDateController.class);

	@Autowired
	private SearchRuleDateService searchRuleDateService;
	
	@Autowired
	private SearchCommandService searchCommandService;

	@Autowired
	private SearchMessageService searchMessageService;
	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>查询前初始化</li>
	 * <li>目的：初始化方法</li>
	 * </ul>
	 * </p>
	 * 
	 * @param searchRuleDateDTO
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
	@RequestMapping(value = "/beforeQuerySearchRuleDate")
	public String beforeQuerySearchRuleDate(
			SearchRuleDateDTO searchRuleDateDTO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//在搜索ruledate表前，看相关的command是否存在，不存在则提示：请先创建command
		String appId = request.getParameter("app_id");
		String flag = "OK";
		List<SearchCommandDTO> list = searchCommandService.searchCommand(appId);
		if(list == null || list.size() <= 0){
			flag = "该应用下，不存在业务数据类型，请先创建业务数据！";
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("flag", flag);
		dataMap.put("app_id", appId);
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
	@RequestMapping(value = "/querySearchRuleDate")
	public String querySearchRuleDate(SearchRuleDateDTO dto,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 列表控件的TableId值
		String tableId = "SearchRuleDateExList";
		// 获取分页信息
		ILimitUtil limitUtil = new ExLimitUtil();
		LimitInfo limit = limitUtil.getLimitInfo(request, tableId,
				Consts.ROWS_PER_PAGE);
		dto.setAppId(request.getParameter("app_id"));
		getRequestPageNo(request, limit);// 翻页代码
		// 查询
		List<SearchRuleDateDTO> list = searchRuleDateService.list(limit, dto);
		// 设置分页信息
		limitUtil.setLimitInfo(request, limit);
		// 保存数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("limit", limit);
		dataMap.put("list", list);
		dataMap.put("app_id", dto.getAppId());
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
	 *            :beforeAddSearchRuleDate
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/beforeAddSearchRuleDate")
	public String beforeAddSearchRuleDate(SearchRuleDateDTO searchRuleDateDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "searchRuleDate/SearchRuleDateAdd";
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
	@RequestMapping(value = "/addSearchRuleDate")
	public String addSearchRuleDate(SearchRuleDateDTO searchRuleDateDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if(searchRuleDateDTO.getFileldDateType().equals(RedisKeyConst.Search.RULE_DATE_TYPE_PK)){
			searchRuleDateDTO.setFieldIndexType(RedisKeyConst.Search.RULE_INDEX_TYPE_NOT_TOKENIZED);
			searchRuleDateDTO.setFieldStoreType(RedisKeyConst.Search.RULE_STORE_TYPE_YES);
		}
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		searchRuleDateDTO.setOperator(username);
		String flag = this.searchRuleDateService.save(searchRuleDateDTO);
		map.put("flag", flag);
		map.put("appId", searchRuleDateDTO.getAppId());
		map.put("commandCode", searchRuleDateDTO.getCommandCode());
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
	@RequestMapping(value = "/beforeEditSearchRuleDate")
	public String beforeEditSearchRuleDate(SearchRuleDateDTO searchRuleDateDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		String appId = request.getParameter("appId");
		SearchRuleDateDTO m = null;
		if (id != null && !id.equals("") && !id.equals("undefined")) {
			m = this.searchRuleDateService.get(id);
		} else {
			m = new SearchRuleDateDTO();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<SearchCommandDTO> list = searchCommandService.searchCommand(appId);
		m.setAppId(appId);
		map.put("list", list);
		map.put("m", m);
		map.put("appId", searchRuleDateDTO.getAppId());
		map.put("commandCode", searchRuleDateDTO.getCommandCode());
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
	@RequestMapping(value = "/editSearchRuleDate")
	public String editSearchRuleDate(SearchRuleDateDTO searchRuleDateDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 根据id获得searchapp，填充数据
		SearchRuleDateDTO m = this.searchRuleDateService.get(searchRuleDateDTO.getId());
		searchRuleDateDTO.setCreateTime(m.getCreateTime());
		searchRuleDateDTO.setAppCode(m.getAppCode());
//		searchRuleDateDTO.setOperator("1");
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		searchRuleDateDTO.setOperator(username);
		if(searchRuleDateDTO.getFileldDateType().equals(RedisKeyConst.Search.RULE_DATE_TYPE_PK )){
			searchRuleDateDTO.setFieldIndexType(RedisKeyConst.Search.RULE_INDEX_TYPE_NOT_TOKENIZED);
			searchRuleDateDTO.setFieldStoreType(RedisKeyConst.Search.RULE_STORE_TYPE_YES);
		}
		String flag = this.searchRuleDateService.update(searchRuleDateDTO);
		map.put("flag", flag);
		map.put("appId", searchRuleDateDTO.getAppId());
		map.put("commandCode", searchRuleDateDTO.getCommandCode());
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
	@RequestMapping(value = "/detailSearchRuleDate")
	public String detailSearchRuleDate(SearchRuleDateDTO searchRuleDateDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(id, "common", "errorparameter");
		request.setAttribute("m", this.searchRuleDateService.get(id));
		return "searchRuleDate/SearchRuleDateView";
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
	@RequestMapping(value = "/deleteSearchRuleDate")
	public String deleteSearchRuleDate(SearchRuleDateDTO searchRuleDateDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String ids = request.getParameter("ids");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(ids, "common", "errorparameter");
		String flag = "OK";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.searchRuleDateService.deleteTX(ids);
		} catch (Exception e) {
			e.printStackTrace();
			flag = "删除失败!";
		}
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}

	@RequestMapping(value = "/queryForPageSearchRuleDate")
	public String queryForPageSearchRuleDate(
			SearchRuleDateDTO searchRuleDateDTO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Page<SearchRuleDateDTO> page = new Page<SearchRuleDateDTO>();
		getRequestPageNo(request, page, 10);
		// hql 如果为空 或 "" 则默认查该表的分页
//		String hql = "";
//		Object[] objWhere = null;
//		page = this.searchRuleDateService.page(page, hql, objWhere);
		request.setAttribute("page", page);
		return "searchRuleDate/SearchRuleDateList";
	}
	
	@RequestMapping(value = "/syncRuleDate")
	public String syncRuleDate(
			SearchRuleDateDTO searchRuleDateDTO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		String datexml = request.getParameter("datexml");
		//datexml = "<?xml version='1.0' encoding='UTF-8' standalone='no'?><infoDate><opt>1</opt><appCode>ceshiB</appCode><commandCode>0002</commandCode><list><listDate><fieldName>PNAME</fieldName><fileldDateType>1</fileldDateType><fieldIndexType>3</fieldIndexType><fieldStoreType>1</fieldStoreType></listDate><listDate><fieldName>CON</fieldName><fileldDateType>1</fileldDateType><fieldIndexType>3</fieldIndexType><fieldStoreType>1</fieldStoreType></listDate><listDate><fieldName>SALENUM</fieldName><fileldDateType>2</fileldDateType><fieldIndexType>2</fieldIndexType><fieldStoreType>1</fieldStoreType></listDate></list></infoDate>";
		log.info(datexml);
		List<SearchRuleDateDTO> list = readXml(datexml);
		//(Map<String, String> mapOpt ,Map<String, String> mapVale
		Map<String, String> mapOpt = new HashMap<String, String>();
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		String opt = "", appcode = "" , commandCode = "";
		StringBuffer sb = new StringBuffer();
		for(SearchRuleDateDTO dto : list){
			Map<String, String> mapVale = new HashMap<String, String>();
			mapOpt.put("opt", dto.getOpt());
			mapOpt.put("appcode", dto.getAppCode());
			mapOpt.put("commandcode", dto.getCommandCode());
			mapVale.put("fieldName", dto.getFieldName());
			mapVale.put("fileldDateType", dto.getFileldDateType());
			mapVale.put("fieldIndexType", dto.getFieldIndexType());
			mapVale.put("fieldStoreType", dto.getFieldStoreType());
			opt = dto.getOpt();
			appcode = dto.getAppCode();
			commandCode = dto.getCommandCode();
			sb.append("\"fieldName\":\"").append(dto.getFieldName()).append("\",")
			  .append("\"fileldDateType\":\"").append(dto.getFileldDateType()).append("\",")
			  .append("\"fieldIndexType\":\"").append(dto.getFieldIndexType()).append("\",")
			  .append("\"fieldStoreType\":\"").append(dto.getFieldStoreType()).append("\",");
			mapList.add(mapVale);
		}
		
		//把数据存入到数据库
		SearchMessageDTO message = new SearchMessageDTO();
		message.setAppcode(appcode);
		message.setCommandcode(commandCode);
		message.setFileinfo(sb.toString().substring(0, sb.toString().length()-1));
		searchMessageService.saveTX(message);
//		LuceneManager.optIndex(mapOpt, mapList);
		return "response";
	}
	/**
	 * 解析xml
	 * @param datexml
	 */
	private List<SearchRuleDateDTO> readXml(String datexml) {
		List<SearchRuleDateDTO> list = new ArrayList<SearchRuleDateDTO>();
		Document document = null;
		try {
			document = DocumentHelper.parseText(datexml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		String opt = "";
		String appCode = "";
		String commandCode = "";
		opt = root.element("opt").getText() == null ?  "" : root.element("opt").getText();
		appCode = root.element("appcode").getText() == null ?  "" : root.element("appcode").getText();
		commandCode = root.element("commandcode").getText() == null ?  "" : root.element("commandcode").getText();
		for (Iterator iter = root.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			for (Iterator iterInner = element.elementIterator(); iterInner.hasNext();) {
				Element element1 = (Element) iterInner.next();
				SearchRuleDateDTO dto = new SearchRuleDateDTO();
				dto.setAppCode(appCode);
				dto.setOpt(opt);
				dto.setCommandCode(commandCode);
				for (Iterator iterInner1 = element1.elementIterator(); iterInner1.hasNext();) {
					Element element2 = (Element) iterInner1.next();
					if(element2.getName().equals("fieldName")){
						dto.setFieldName(element2.getText()  != null ? element2.getText() :"");
					}
					else if(element2.getName().equals("fileldDateType")){
						dto.setFileldDateType(element2.getText()  != null ? element2.getText() :"");
					}
					else if(element2.getName().equals("fieldIndexType")){
						dto.setFieldIndexType(element2.getText()  != null ? element2.getText() :"");
					}
					else if(element2.getName().equals("fieldStoreType")){
						dto.setFieldStoreType(element2.getText()  != null ? element2.getText() :"");
					}
				}
				list.add(dto);
			}
		}
		return list;
	}
	
	@RequestMapping(value = "/updateSyncSearchRule")
	@ResponseBody
	public AjaxResponse updateSyncSearchRule(){
		try {
			Map<String, String> map = new HashMap<String, String>();
			String message = searchRuleDateService.updateSyncSearchRule();
			String path = GlobalConfig.getProperty("searchh", "lucene_db_path");
			map.put("message", message);
			map.put("path", path);
			return new AjaxResponse(true, map);
		} catch (Exception e) {
			return new AjaxResponse(false);
		}
	}
}
