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
import com.sinovatech.search.entity.DictionaryindexDTO;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.services.DictionaryindexService;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.BaseController;

/**
 * Dictionaryindex数据字典主表 Controller类
 * 
 * 创建: 2014-11-14 13:25:00<br />
 * 
 * @author 作者liuzhenquan
 */
@Controller
@RequestMapping("dictionaryindex")
public class DictionaryindexController extends BaseController {

	@Autowired
	private DictionaryindexService dictionaryindexService;

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>查询前初始化</li>
	 * <li>目的：初始化方法</li>
	 * </ul>
	 * </p>
	 * 
	 * @param dictionaryindexDTO
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
	@RequestMapping(value = "/beforeQueryDictionaryindex")
	public String beforeQueryDictionaryindex(
			DictionaryindexDTO dictionaryindexDTO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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
	@RequestMapping(value = "/queryDictionaryindex")
	public String queryDictionaryindex(DictionaryindexDTO dto,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 列表控件的TableId值
		String tableId = "DictionaryindexExList";
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
		// 查询
		List list = dictionaryindexService.list(limit, dto);
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
		// dataMap = null;
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
	 *            :beforeAddDictionaryindex
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/beforeAddDictionaryindex")
	public String beforeAddDictionaryindex(
			DictionaryindexDTO dictionaryindexDTO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return "dictionaryindex/DictionaryindexAdd";
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
	@RequestMapping(value = "/addDictionaryindex")
	public String addDictionaryindex(DictionaryindexDTO dictionaryindexDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String flag = "OK";
		Map<String, Object> map = new HashMap<String, Object>();
		flag = this.dictionaryindexService.save(dictionaryindexDTO);
		
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
	@RequestMapping(value = "/beforeEditDictionaryindex")
	public String beforeEditDictionaryindex(
			DictionaryindexDTO dictionaryindexDTO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");

		// 验证方法， 如果为null或者为空则直接返回异常
		// Validate.notBlank(id, "common", "errorparameter");

		DictionaryindexDTO m = null;
		if (id.equals(""))
			m = new DictionaryindexDTO();
		else
			m = this.dictionaryindexService.get(id);

		request.setAttribute("json", JsonUtil.getJSONString(m));
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
	@RequestMapping(value = "/editDictionaryindex")
	public String editDictionaryindex(DictionaryindexDTO dictionaryindexDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String flag = "OK";
		Map<String, Object> map = new HashMap<String, Object>();
		flag =this.dictionaryindexService.update(dictionaryindexDTO);
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
	@RequestMapping(value = "/detailDictionaryindex")
	public String detailDictionaryindex(DictionaryindexDTO dictionaryindexDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(id, "common", "errorparameter");
		request.setAttribute("m", this.dictionaryindexService.get(id));
		return "dictionaryindex/DictionaryindexView";
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
	@RequestMapping(value = "/deleteDictionaryindex")
	public String deleteDictionaryindex(DictionaryindexDTO dictionaryindexDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String ids = request.getParameter("ids");
		// 验证方法， 如果为null或者为空则直接返回异常
		Map<String, Object> map = new HashMap<String, Object>();
		Validate.notBlank(ids, "common", "errorparameter");
		try {
			this.dictionaryindexService.deleteTX(ids);
			map.put("flag", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
		}

		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
 

}
