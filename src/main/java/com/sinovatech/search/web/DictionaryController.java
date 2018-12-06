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
import com.sinovatech.search.ectable.limit.ExLimitUtil;
import com.sinovatech.search.ectable.limit.ILimitUtil;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.BaseController;
import com.sinovatech.search.entity.DictionaryDTO;
import com.sinovatech.search.entity.DictionaryindexDTO;
import com.sinovatech.search.services.DictionaryService;
import com.sinovatech.search.services.DictionaryindexService;

/**
 * Dictionary数据字典子表 Controller类
 * 
 * 创建: 2014-11-14 13:24:57<br />
 * 
 * @author 作者liuzhenquan
 */
@Controller
@RequestMapping("dictionary")
public class DictionaryController extends BaseController {

	@Autowired
	private DictionaryService dictionaryService;
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
	 * @param dictionaryDTO
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
	@RequestMapping(value = "/beforeQueryDictionary")
	public String beforeQueryDictionary(DictionaryDTO dictionaryDTO,
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
	@RequestMapping(value = "/queryDictionary")
	public String queryDictionary(DictionaryDTO dto,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 列表控件的TableId值
		String tableId = "DictionaryExList";
		// 获取分页信息
		ILimitUtil limitUtil = new ExLimitUtil();
		LimitInfo limit = limitUtil.getLimitInfo(request, tableId, 10);
		String indexId = request.getParameter("index_id");
		dto.setIndexId(indexId);
		getRequestPageNo(request, limit);// 翻页代码
		// 如果不进行分页， 只做条件查询时，limit使用如下方式获取即可:
		// LimitInfo limit = new LimitInfo();
		// 自定义条件
		// limit.addFilterProperty(HqlProperty.getEq("属性名称", 值【必须与属性类型匹配】));
		// 状态保留
		// limit = this.limitStateStore(request, limit, mapping);
		// 查询
		List list = dictionaryService.list(limit, dto);
		

		// 设置分页信息
		limitUtil.setLimitInfo(request, limit);
		// 保存数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("limit", limit);
		dataMap.put("list", list);
		dataMap.put("index_id", indexId);
		request.setAttribute("json", JsonUtil.getJSONString(dataMap));
		dataMap = null;
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
	 *            :beforeAddDictionary
	 * @param request
	 *            : HttpServletRequest
	 * @return ActionForward
	 * @exception Exception
	 */
	@RequestMapping(value = "/beforeAddDictionary")
	public String beforeAddDictionary(DictionaryDTO dictionaryDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "dictionary/DictionaryAdd";
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
	@RequestMapping(value = "/addDictionary")
	public String addDictionary(DictionaryDTO dictionaryDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String flag = "OK";
		Map<String, Object> map = new HashMap<String, Object>();
		flag =this.dictionaryService.save(dictionaryDTO);
		map.put("flag", flag);
		map.put("indexId", dictionaryDTO.getIndexId());
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
	@RequestMapping(value = "/beforeEditDictionary")
	public String beforeEditDictionary(DictionaryDTO dictionaryDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		String indexId = request.getParameter("indexId");
		Map<String, Object> map = new HashMap<String, Object>();
		// 验证方法， 如果为null或者为空则直接返回异常
		// Validate.notBlank(id, "common", "errorparameter");
		DictionaryDTO m = null;
		if (id != null && !id.equals("") && !id.equals("undefined")) {
			m = this.dictionaryService.get(id);
		} else {
			m = new DictionaryDTO();
		}
		if (indexId != null && !indexId.equals("") && !indexId.equals("undefined")) {
			DictionaryindexDTO dto = dictionaryindexService.get(indexId);
			m.setIndexcode(dto.getIndexcode());
		}
		map.put("m", m);
		map.put("indexId", indexId);
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
	@RequestMapping(value = "/editDictionary")
	public String editDictionary(DictionaryDTO dictionaryDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String flag = "OK";
		Map<String, Object> map = new HashMap<String, Object>();
		flag = this.dictionaryService.update(dictionaryDTO);
		map.put("flag", flag);
		map.put("indexId", dictionaryDTO.getIndexId());
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
	@RequestMapping(value = "/detailDictionary")
	public String detailDictionary(DictionaryDTO dictionaryDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(id, "common", "errorparameter");
		request.setAttribute("m", this.dictionaryService.get(id));
		return "dictionary/DictionaryView";
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
	@RequestMapping(value = "/deleteDictionary")
	public String deleteDictionary(DictionaryDTO dictionaryDTO,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String ids = request.getParameter("ids");
	
		
		// 验证方法， 如果为null或者为空则直接返回异常
		Validate.notBlank(ids, "common", "errorparameter");
		String flag = "OK";
		Map<String, Object> map = new HashMap<String, Object>();
		this.dictionaryService.deleteTX(ids);
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}

	 

}
