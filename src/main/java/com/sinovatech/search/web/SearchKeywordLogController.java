package com.sinovatech.search.web;

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
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.BaseController;
import com.sinovatech.search.entity.SearchKeywordLogDTO;
import com.sinovatech.search.services.SearchKeywordLogService;

/**
 * SearchKeywordLog搜索日志表 Controller类
 * 
 * 创建: 2014-11-14 13:25:08<br />
 * 
 * @author 作者liuzhenquan
 */
@Controller
@RequestMapping("searchKeywordLog")
public class SearchKeywordLogController extends BaseController {

	@Autowired
	private SearchKeywordLogService searchKeywordLogService;

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>查询前初始化</li>
	 * <li>目的：初始化方法</li>
	 * </ul>
	 * </p>
	 * 
	 * @param searchKeywordLogDTO
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
	@RequestMapping(value = "/beforeQuerySearchKeywordLog")
	public String beforeQuerySearchKeywordLog(
			SearchKeywordLogDTO searchKeywordLogDTO,
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
	@RequestMapping(value = "/querySearchKeywordLog")
	public String querySearchKeywordLog(
			SearchKeywordLogDTO dto,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 列表控件的TableId值
		String tableId = "SearchKeywordLogExList";
		// 获取分页信息
		ILimitUtil limitUtil = new ExLimitUtil();
		LimitInfo limit = limitUtil.getLimitInfo(request, tableId, Consts.ROWS_PER_PAGE);
		
		getRequestPageNo(request, limit);// 翻页代码
		// 如果不进行分页， 只做条件查询时，limit使用如下方式获取即可:
		// LimitInfo limit = new LimitInfo();
		// 自定义条件
		// limit.addFilterProperty(HqlProperty.getEq("属性名称", 值【必须与属性类型匹配】));
		// 状态保留
		// limit = this.limitStateStore(request, limit, mapping);
		// 查询
		List list = searchKeywordLogService.list(limit, dto);
		//获取登录用户信息
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		
		// 设置分页信息
		limitUtil.setLimitInfo(request, limit);
		// 保存数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("limit", limit);
		dataMap.put("list", list);
		dataMap.put("username", username);
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
	 *            :beforeAddSearchKeywordLog
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/beforeAddSearchKeywordLog")
	public String beforeAddSearchKeywordLog(
			SearchKeywordLogDTO searchKeywordLogDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "searchKeywordLog/SearchKeywordLogAdd";
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
	@RequestMapping(value = "/addSearchKeywordLog")
	public void addSearchKeywordLog(SearchKeywordLogDTO searchKeywordLogDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.searchKeywordLogService.saveTX(searchKeywordLogDTO);
//		return "searchKeywordLog/SearchKeywordLogList";
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
	@RequestMapping(value = "/beforeEditSearchKeywordLog")
	public String beforeEditSearchKeywordLog(
			SearchKeywordLogDTO searchKeywordLogDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(id, "common", "errorparameter");
		SearchKeywordLogDTO m = this.searchKeywordLogService.get(id);
		request.setAttribute("m", m);
		return "searchKeywordLog/SearchKeywordLogEdit";
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
	@RequestMapping(value = "/editSearchKeywordLog")
	public String editSearchKeywordLog(SearchKeywordLogDTO searchKeywordLogDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.searchKeywordLogService.updateTX(searchKeywordLogDTO);
		return "searchKeywordLog/SearchKeywordLogList";
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
	@RequestMapping(value = "/detailSearchKeywordLog")
	public String detailSearchKeywordLog(
			SearchKeywordLogDTO searchKeywordLogDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(id, "common", "errorparameter");
		request.setAttribute("m", this.searchKeywordLogService.get(id));
		return "searchKeywordLog/SearchKeywordLogView";
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
	@RequestMapping(value = "/deleteSearchKeywordLog")
	public String deleteSearchKeywordLog(
			SearchKeywordLogDTO searchKeywordLogDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String ids = request.getParameter("ids");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(ids, "common", "errorparameter");
		this.searchKeywordLogService.deleteTX(ids);
		return "searchKeywordLog/SearchKeywordLogList";
	}

 
}
