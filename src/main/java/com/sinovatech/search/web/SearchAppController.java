package com.sinovatech.search.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sinovatech.common.util.Validate;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.ExLimitUtil;
import com.sinovatech.search.ectable.limit.ILimitUtil;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.DictionaryDTO;
import com.sinovatech.search.entity.DictionaryindexDTO;
import com.sinovatech.search.entity.RecommendDTO;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchKeywordLogDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.entity.SynonymWordDTO;
import com.sinovatech.search.luceneindex.AppCacheDTO;
import com.sinovatech.search.luceneindex.LuceneJsonUtil;
import com.sinovatech.search.luceneindex.LuceneManager;
import com.sinovatech.search.services.DictionaryService;
import com.sinovatech.search.services.DictionaryindexService;
import com.sinovatech.search.services.RecommendService;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.services.SearchCommandService;
import com.sinovatech.search.services.SearchKeywordLogService;
import com.sinovatech.search.services.SearchRuleDateService;
import com.sinovatech.search.services.SynonymWordService;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.BaseController;

/**
 * SearchApp注册应用业务表 Controller类
 * 
 * 创建: 2014-11-14 13:25:04<br />
 * 
 * @author 作者liuzhenquan
 */
@Controller
@RequestMapping("searchApp")
public class SearchAppController extends BaseController {

	@Autowired
	private SearchAppService searchAppService;
	@Autowired
	private SearchCommandService searchCommandService;
	@Autowired
	private SearchRuleDateService searchRuleDateService;
	@Autowired
	private SearchKeywordLogService searchKeywordLogService;
	
	@Autowired
	private DictionaryindexService dictionaryindexService;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private SynonymWordService synonymWordService;
	
	@Autowired
	private RecommendService recommendService;
	
	private static final Log log = LogFactory.getLog(SearchAppController.class);
	// @Autowired
	// private SearchRuleDateDTO searchRuleDateDTO;
	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>查询前初始化</li>
	 * <li>目的：初始化方法</li>
	 * </ul>
	 * </p>
	 * 
	 * @param searchAppDTO
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
	@RequestMapping(value = "/beforeQuerySearchApp")
	public String beforeQuerySearchApp(SearchAppDTO searchAppDTO,
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
	@RequestMapping(value = "/querySearchApp")
	public String querySearchApp(SearchAppDTO dto, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 列表控件的TableId值
		String tableId = "SearchAppExList";
		// 获取分页信息
		ILimitUtil limitUtil = new ExLimitUtil();
		LimitInfo limit = limitUtil.getLimitInfo(request, tableId,
				Consts.ROWS_PER_PAGE);
		getRequestPageNo(request, limit);// 翻页代码
		// 如果不进行分页， 只做条件查询时，limit使用如下方式获取即可:
		// LimitInfo limit = new LimitInfo();
		// 自定义条件
		// limit.addFilterProperty(HqlProperty.getEq("属性名称", 值【必须与属性类型匹配】));
		// 状态保留
		// limit = this.limitStateStore(request, limit, mapping);
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		//获取当前登录用户关联的appcode
		String appCode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_APPCOCE);
		// 查询
		List<SearchAppDTO> list = searchAppService.list(limit, dto,usercode,appCode);
		String jisuanappcode = "";
		for(SearchAppDTO searchAppDTO:list)
		{
			if(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI.equals(searchAppDTO.getIndexType()) ||RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE.equals(searchAppDTO.getIndexType())){
				jisuanappcode+=searchAppDTO.getAppCode()+",";
			}
		}
		if(!jisuanappcode.equals("")){
			jisuanappcode=jisuanappcode.substring(0,jisuanappcode.length()-1);
		}
		 
		//获取登录用户信息
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		// 设置分页信息
		limitUtil.setLimitInfo(request, limit);
		// 保存数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("limit", limit);
		dataMap.put("jisuanappcode", jisuanappcode);
		dataMap.put("list", list);
		dataMap.put("username", username);
		request.setAttribute("json", JsonUtil.getJSONString(dataMap));
		return "response";
	}
	
	
	@RequestMapping(value = "/queryuser")
	public String queryuser(SearchAppDTO dto, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 列表控件的TableId值
	 
		 
		//获取登录用户信息
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
	 
		// 保存数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("username", username);
		request.setAttribute("json", JsonUtil.getJSONString(dataMap));
		return "response";
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/querySearchDic")
	public String querySearchDic(HttpServletRequest request, HttpServletResponse response)	throws Exception {
		LimitInfo limit = new LimitInfo();
		limit.setRowDisplayed(Integer.MAX_VALUE);
		DictionaryindexDTO dto = new DictionaryindexDTO();
		dto.setStatus(RedisKeyConst.Search.USERING);
		List<DictionaryindexDTO> list=this.dictionaryindexService.list(limit,dto);
		DictionaryDTO dictionaryDTO = new DictionaryDTO();
		Map<String,List<DictionaryDTO>> rmap =new HashMap<String,List<DictionaryDTO>>();//主表key
		String indexId="";
		for(DictionaryindexDTO dictionaryindexDTO:list)
		{
			indexId =dictionaryindexDTO.getIndexcode();
			dictionaryDTO.setIndexId(dictionaryindexDTO.getId());
			dictionaryDTO.setStatus(RedisKeyConst.Search.USERING);
			rmap.put(dictionaryindexDTO.getIndexcode(),  this.dictionaryService.list(limit, dictionaryDTO));
		}
		request.setAttribute("jsonstr", LuceneJsonUtil.getJSONString(rmap));
		return "json";
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryTongyici")
	public String queryTongyici(HttpServletRequest request, HttpServletResponse response)	throws Exception {
		LimitInfo limit = new LimitInfo();
		limit.setFirstEnter(true);
		limit.setRowDisplayed(Integer.MAX_VALUE);
		SynonymWordDTO dto = new SynonymWordDTO();
		dto.setState("1");
		List<SynonymWordDTO> list=this.synonymWordService.list(limit,dto);
		request.setAttribute("jsonstr", LuceneJsonUtil.getJSONString(list));
		return "json";
		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryTuijian")
	public String queryTuijian(HttpServletRequest request, HttpServletResponse response)	throws Exception {
		LimitInfo limit = new LimitInfo();
		limit.setFirstEnter(true);
		limit.setRowDisplayed(Integer.MAX_VALUE);
		RecommendDTO dto = new RecommendDTO();
		dto.setStatus("1");
		List<RecommendDTO> list=this.recommendService.list(limit,dto); 
		request.setAttribute("jsonstr", LuceneJsonUtil.getJSONString(list));
		return "json";
	}
	
	@RequestMapping(value = "/querySearchAppTest")
	public String querySearchAppTest(SearchAppDTO dto,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LimitInfo limit = new LimitInfo();
		limit.setRowDisplayed(10);

		//List list = searchAppService.listByLimitForSqlRmap(limit, "select  * from search_app ", "", "");

		// 保存数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("limit", limit);
		//dataMap.put("list", list);

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
	 *            :beforeAddSearchApp
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/beforeAddSearchApp")
	public String beforeAddSearchApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "searchApp/SearchAppAdd";
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
	@RequestMapping(value = "/addSearchApp")
	public String addSearchApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		searchAppDTO.setOperator(username);
		String flag = this.searchAppService.save(searchAppDTO);
		map.put("flag", flag);
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
	@RequestMapping(value = "/beforeEditSearchApp")
	public String beforeEditSearchApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		SearchAppDTO m = null;
		if(id == null || id.equals("") || id.equals("undefined"))
			m = new SearchAppDTO();
		else
			m = this.searchAppService.get(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("m", m);
		map.put("method", request.getParameter("method"));
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
	@RequestMapping(value = "/editSearchApp")
	public String editSearchApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		searchAppDTO.setOperator(username);
		String flag = this.searchAppService.update(searchAppDTO);
		map.put("flag", flag);
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
	@RequestMapping(value = "/detailSearchApp")
	public String detailSearchApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(id, "common", "errorparameter");
		request.setAttribute("m", this.searchAppService.get(id));
		return "searchApp/SearchAppView";
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
	@RequestMapping(value = "/deleteSearchApp")
	public String deleteSearchApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String ids = request.getParameter("ids");
		// 验证方法， 如果为null或者为空则直接返回异常
		Map<String, Object> map = new HashMap<String, Object>();
		Validate.notBlank(ids, "common", "errorparameter");
		try {
			this.searchAppService.deleteTX(ids);
			map.put("flag", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
		}
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}

	@RequestMapping(value = "/queryForPageSearchApp")
	public String queryForPageSearchApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		Page page = new Page();
//		getRuquestPageNo(request, page, 10);
//		// hql 如果为空 或 "" 则默认查该表的分页
//		String hql = "";
//		Object[] objWhere = null;
//		page = this.searchAppService.page(page, hql, objWhere);
//		request.setAttribute("page", page);
 	return "searchApp/SearchAppList";
	}

	/**
	 * 启动APP
	 * 
	 * @param searchAppDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/startApp")
	public String startApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		searchAppDTO = this.searchAppService.get(id);
		searchAppDTO.setIsReset(RedisKeyConst.Search.RESET_NO);
		searchAppDTO.setUpdateTime(new Date());
		searchAppDTO.setState(RedisKeyConst.Search.USERING);

		List<SearchCommandDTO> commandList = searchCommandService
				.searchCommand(id);// 读取command
		List<SearchRuleDateDTO> ruleDateList = searchRuleDateService
				.searchRuleDate(id);// 读取RuleDate
		String flag = "";
		Map<String, Object> map = new HashMap<String, Object>();
		flag =this.searchAppService.validStart(commandList, ruleDateList, flag);
		if (flag.equals("OK")) {
			// 修改缓存(重新读取相关command和ruleDate)
			this.searchAppService.editCache(searchAppDTO, commandList, ruleDateList);
			// 调用启用接口
			LuceneManager.optIndexWrite(searchAppDTO.getAppCode(),
					RedisKeyConst.Search.SEARCH_SYS_START);
		}
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	/**
	 * 停止APP
	 * 
	 * @param searchAppDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stopApp")
	public String stopApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		searchAppDTO = this.searchAppService.get(id);
		searchAppDTO.setIsReset(RedisKeyConst.Search.RESET_NO);
		searchAppDTO.setState(RedisKeyConst.Search.STOP);
		searchAppDTO.setUpdateTime(new Date());
		searchAppService.updateTX(searchAppDTO);
		// 删除缓存
		//AppCacheDTO cacheDTO = AppContext.getAppCacheDTOMap().get(searchAppDTO.getAppCode());
		//AppContext.getAppCacheDTOMap().remove(cacheDTO);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", "OK");
		request.setAttribute("json", JsonUtil.getJSONString(map));
		// 调用停用接口
		LuceneManager.optIndexWrite(searchAppDTO.getAppCode(),
				RedisKeyConst.Search.SEARCH_SYS_STOP);
		return "response";
	}

	/**
	 * 重启启动APP
	 * 
	 * @param searchAppDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resetApp")
	public String resetApp(SearchAppDTO searchAppDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");

		List<SearchCommandDTO> commandList = searchCommandService
				.searchCommand(id);// 读取command
		List<SearchRuleDateDTO> ruleDateList = searchRuleDateService
				.searchRuleDate(id);// 读取RuleDate
		String flag = "OK";
		flag =this.searchAppService.validStart(commandList, ruleDateList, flag);
		Map<String, Object> map = new HashMap<String, Object>();
		if (flag.equals("OK")) {
			
			searchAppDTO = this.searchAppService.get(id);
			searchAppDTO.setIsReset(RedisKeyConst.Search.RESET_NO);
			searchAppDTO.setState(RedisKeyConst.Search.USERING);
			searchAppDTO.setUpdateTime(new Date());
			searchAppService.updateTX(searchAppDTO);
			// 修改缓存
			this.searchAppService.editCache(searchAppDTO, commandList, ruleDateList);
			//-------》》》》
 			LuceneManager.optIndexWrite(searchAppDTO.getAppCode(),
 					RedisKeyConst.Search.SEARCH_SYS_STOP);
 			LuceneManager.optIndexWrite(searchAppDTO.getAppCode(),
 					RedisKeyConst.Search.SEARCH_SYS_START);
		}
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}

	/**
	 * 供前台获取配置缓存AppCacheDTO
	 * 
	 * @param searchAppDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAppCache")
	public String getAppCache(HttpServletRequest request, HttpServletResponse response)	throws Exception {
		String appCode = request.getParameter("appcode");
		Map<String, AppCacheDTO> rmap  =this.searchAppService.getReopen(appCode);
		request.setAttribute("jsonstr", LuceneJsonUtil.getJSONString(rmap));
		return "json";
	}
	/**
	 * 接收前台推送过来的信息写入searchMessage的lucene表，供后台定时器去追加创建内容索引
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tuiMsg")
	public String tuiMsg(HttpServletRequest request, HttpServletResponse response)	throws Exception {
		String dataxml = request.getParameter("dataxml");
		String[]  str = null;
		try {
			 str = searchAppService.readXmlCommandAndAppCode(dataxml);
			 searchAppService.saveSearchMessageDTOTX(dataxml);
			 str[2]="yes";
			log.info("Save TuiThreadPoolTask is ok TuiThreadPoolTask:[]");
		} catch (Exception e) {
			log.error("Save searchKeywordLogDTO is error",e);
			if(str==null)
			{
				str=new String[]{"null","null","解析错误"};
			}else{
				 str[2]=e.getMessage();
			}
			e.printStackTrace();
		} 
		String rstr = LuceneJsonUtil.getJSONString(str);
		response.getWriter().print(rstr);
		return null;
	}
	/**
	 * 接到前台推送过来的热词，来写入热词lucene临时表，供后台搜索引擎来定时创建热词索引库	
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tuiHotWord")
	public String tuiHotWord(HttpServletRequest request, HttpServletResponse response)	throws Exception {
		String datajosn = request.getParameter("datajosn");
		SearchKeywordLogDTO searchKeywordLogDTO = LuceneJsonUtil.getDTOforme(datajosn, SearchKeywordLogDTO.class);
		String[]  str = null;
		try {
			 searchKeywordLogService.saveTX(searchKeywordLogDTO);
			log.info("Save TuiThreadPoolTask is ok TuiThreadPoolTask:[]");
			str=new String[]{searchKeywordLogDTO.getAppCode(),searchKeywordLogDTO.getSearchKeyword(),"yes"};
		} catch (Exception e) {
			log.error("Save searchKeywordLogDTO is error",e);
			str=new String[]{searchKeywordLogDTO.getAppCode(),searchKeywordLogDTO.getSearchKeyword(),e.getMessage()};
			e.printStackTrace();
		} 
		String rstr = LuceneJsonUtil.getJSONString(str);
		response.getWriter().print(rstr);
		return null;
	}
	
	
}
