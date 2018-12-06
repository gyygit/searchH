package com.sinovatech.search.web;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinovatech.common.util.StringUtils;
import com.sinovatech.common.util.Validate;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.RecommendDTO;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.services.RecommendService;
import com.sinovatech.search.services.UserService;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.AjaxResponse;
import com.sinovatech.search.utils.web.BaseController;
/**
 * Recommend推荐 Controller类
 * @author Ma Tengfei
 *
 */
@Controller
@RequestMapping("recommend")
public class RecommendController extends BaseController{

	@Autowired
	private RecommendService recommendService;
	
	@Autowired
	private UserService userService;
	
	private static final Log log = LogFactory.getLog(RecommendController.class);
	
	/**
	 * 查询推荐信息
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryRecommend")
	public String queryRecommend(RecommendDTO dto,HttpServletRequest request,HttpServletResponse response) throws Exception{
		LimitInfo limit = new LimitInfo();
		limit.setFirstEnter(true);
		limit.setRowDisplayed(Consts.ROWS_PER_PAGE);
		getRequestPageNo(request, limit);// 翻页代码
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		//获取当前登录用户关联的appcode
		String appCode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_APPCOCE);
		// 查询
		List list = recommendService.list(limit, dto,usercode,appCode);
		// 保存数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		dataMap.put("limit", limit);
		dataMap.put("list", list);
		dataMap.put("username", username);
		request.setAttribute("json", JsonUtil.getJSONString(dataMap));
		return "response";
	}
	
	/***
	 * 添加推荐之前查询
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAppBeforeAddRecommend")
	@ResponseBody
	public AjaxResponse getAppBeforeAddRecommend(HttpServletRequest request)
			throws Exception{
		try {
			//获取当前登录用户登录名
			String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
			//获取当前登录用户关联的appcode
			String appCode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_APPCOCE);
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
			List<SearchAppDTO> lists = new ArrayList<SearchAppDTO>();
			if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
				if("admin".equals(usercode)){
					lists = userService.getSearchAppInfo();
				}else{
					lists = userService.getSearchAppInfoByCurrentUser(appCode);
				}
			}
			dataMap.put("apps", lists);
			return new AjaxResponse(true, dataMap);
		} catch (Exception e) {
			return new AjaxResponse(false);
		}
	}
	
	/***
	 * 保存添加的推荐信息
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addRecommend")
	public String addRecommend(RecommendDTO dto,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String commandCode = request.getParameter("commandCode");
		dto.setCommandCode(commandCode);
		Map<String, Object> map = new HashMap<String, Object>();
		String flag = this.recommendService.save(dto);
		log.info("Save recommend is ok ");
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/***
	 * 编辑之前查询
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/beforeEditRecommend")
	public String beforeEditRecommend(RecommendDTO dto,HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		String id = request.getParameter("id");
		RecommendDTO m = null;
		if(id == null || id.equals("") || id.equals("undefined")){
			m = new RecommendDTO();
		}else{
			m = this.recommendService.get(id);
		}
		 //获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		//获取当前登录用户关联的appcode
		String appCode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_APPCOCE);
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<SearchAppDTO> appList = new ArrayList<SearchAppDTO>();
		if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
			if("admin".equals(usercode)){
				appList = userService.getSearchAppInfo();
			}else{
				appList = userService.getSearchAppInfoByCurrentUser(appCode);
			}
		}
		List<SearchCommandDTO> commandList = recommendService.getListCommendByAppCode(m.getAppCode());
		map.put("m", m);
		map.put("appList",appList);
		map.put("commandList",commandList);
		String commandCode = m.getCommandCode();
		map.put("command", commandCode);
		
//		String[] command = null;
//		if(StringUtils.isNotBlank(commandCode)){
//			command = commandCode.split(",");
//			map.put("commands", command);
//		}
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/***
	 * 更新编辑推荐信息
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editRecommend")
	public String editRecommend(RecommendDTO dto,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String commandCode = request.getParameter("commandCode");
		dto.setCommandCode(commandCode);
		Map<String, Object> map = new HashMap<String, Object>();
		String flag = this.recommendService.update(dto);
		log.info("Update recommend is ok ");
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/***
	 * 删除推荐信息
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteRecommend")
	public String deleteRecommend(RecommendDTO dto,HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		String ids = request.getParameter("ids");
		Map<String, Object> map = new HashMap<String, Object>();
		Validate.notBlank(ids, "common", "errorparameter");
		try {
			this.recommendService.deleteTX(ids);
			log.info("Delete recommend is ok ");
			map.put("flag", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Delete recommend is error ");
			map.put("flag", "false");
		}
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/***
	 * 更新推荐状态为可用
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeStart")
	public String changeStart(RecommendDTO dto,HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		String id = request.getParameter("id");
		dto = this.recommendService.get(id);
		dto.setStatus(RedisKeyConst.Search.USERING);
		recommendService.updateTX(dto);
		log.info("Change recommend  status is ok ");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", "OK");
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/***
	 * 更新推荐状态为不可用
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeStop")
	public String changeStop(RecommendDTO dto,HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		String id = request.getParameter("id");
		dto = this.recommendService.get(id);
		dto.setStatus(RedisKeyConst.Search.STOP);
		recommendService.updateTX(dto);
		log.info("Change recommend  status is ok ");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", "OK");
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/**
	 * 通过业务应用编码获取业务数据信息
	 * @param request
	 * @param appcode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCommandBeforeAddRecommend")
	@ResponseBody
	public AjaxResponse getCommandBeforeAddRecommend(HttpServletRequest request,@RequestParam String appcode)
			throws Exception{
		try {
			List<SearchCommandDTO> lists = recommendService.getListCommendByAppCode(appcode);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("commands", lists);
			return new AjaxResponse(true, dataMap);
		} catch (Exception e) {
			return new AjaxResponse(false);
		}
	}
	
}
