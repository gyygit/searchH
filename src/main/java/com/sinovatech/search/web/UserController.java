package com.sinovatech.search.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.common.util.StringUtils;
import com.sinovatech.common.util.Validate;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.UserDTO;
import com.sinovatech.search.services.UserService;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.AjaxResponse;
import com.sinovatech.search.utils.web.BaseController;
/**
 * 用户User表 Controller类
 * @author Ma Tengfei
 *
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController{
	
	@Autowired
	private UserService userService;
	
	private static final Log log = LogFactory.getLog(UserController.class);
	
	/**
	 * 查询用户信息
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryUser")
	public String queryUser(UserDTO dto,HttpServletRequest request,HttpServletResponse response) throws Exception{
		// 列表控件的TableId值
//		String tableId = "UserExList";
//		// 获取分页信息
//		ILimitUtil limitUtil = new ExLimitUtil();
//		LimitInfo limit = limitUtil.getLimitInfo(request, tableId,
//				Consts.ROWS_PER_PAGE);
		LimitInfo limit = new LimitInfo();
		limit.setFirstEnter(true);
		limit.setRowDisplayed(Consts.ROWS_PER_PAGE);
		getRequestPageNo(request, limit);// 翻页代码
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		//获取当前登录用户关联的appcode
		String appCode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_APPCOCE);
		// 查询
		List list = userService.list(limit, dto,usercode,appCode);

		// 设置分页信息
//		limitUtil.setLimitInfo(request, limit);
		// 保存数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		dataMap.put("limit", limit);
		dataMap.put("list", list);
		dataMap.put("username", username);
		request.setAttribute("json", JsonUtil.getJSONString(dataMap));
		return "response";
	}
	
	/**
	 * 添加用户之前查询
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/beforeAddUser")
	@ResponseBody
	public AjaxResponse beforeAddUser(HttpServletRequest request)
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
	
	/**
	 * 保存添加的用户信息
	 * @param userDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addUser")
	public String addUser(UserDTO userDTO,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String appcode = request.getParameter("appcode");
		Map<String, Object> map = new HashMap<String, Object>();
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		userDTO.setOperator(username);
		userDTO.setOperatorCode(usercode);
		String flag = this.userService.save(userDTO,appcode);
		log.info("Save user is "+flag);
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/**
	 * 编辑用户查询
	 * @param userDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/beforeEditUser")
	public String beforeEditUser(UserDTO userDTO,HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		String id = request.getParameter("id");
		UserDTO m = null;
		if(id == null || id.equals("") || id.equals("undefined")){
			m = new UserDTO();
		}else{
			m = this.userService.get(id);
		}
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		//获取当前登录用户关联的appcode
		String appCode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_APPCOCE);
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<SearchAppDTO> lists = new ArrayList<SearchAppDTO>();
		if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
			if("admin".equals(usercode)){
				lists = userService.getSearchAppInfo();
			}else{
				lists = userService.getSearchAppInfoByCurrentUser(appCode);
			}
		}
		map.put("m", m);
		map.put("list",lists);
		String appcode = m.getAppcode();
		String[] app = null;
		if(StringUtils.isNotBlank(appcode) && !"null".equals(appcode)){
			app = appcode.split(",");
			System.out.println(app);
		}
		map.put("apps", app);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/**
	 * 更新用户信息
	 * @param userDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editUser")
	public String editUser(UserDTO userDTO,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		userDTO.setOperator(username);
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		String flag = this.userService.update(userDTO,usercode);
		log.info("Update user is "+flag);
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/**
	 * 删除用户信息
	 * @param userDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteUser")
	public String deleteUser(UserDTO userDTO,HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		String ids = request.getParameter("ids");
		Map<String, Object> map = new HashMap<String, Object>();
		Validate.notBlank(ids, "common", "errorparameter");
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		try {
			String message = this.userService.deleteTX(ids,usercode);
			log.info("Delete user is "+message);
			map.put("flag", message);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Delete user is error");
			map.put("flag", "false");
		}
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/**
	 * 用户状态变为启用
	 * @param userDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeStart")
	public String changeStart(UserDTO userDTO,HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		String id = request.getParameter("id");
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		userDTO = this.userService.get(id);
		userDTO.setOperator(username);
		userDTO.setStatus(RedisKeyConst.Search.USERING);
		userDTO.setUpdateTime(new Date());
		userDTO.setOperatorCode(usercode);
		userService.updateTX(userDTO);
		log.info("Update user status is ok");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", "OK");
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/**
	 * 用户状态变为停用
	 * @param userDTO
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeStop")
	public String changeStop(UserDTO userDTO,HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		String id = request.getParameter("id");
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		userDTO = this.userService.get(id);
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
			if(usercode.equals(userDTO.getCode())){
				map.put("flag", "对不起，你没有权限停用该用户！");
			}else{
				userDTO.setOperator(username);
				userDTO.setStatus(RedisKeyConst.Search.STOP);
				userDTO.setUpdateTime(new Date());
				userDTO.setOperatorCode(usercode);
				userService.updateTX(userDTO);
				map.put("flag", "OK");
			}
			log.info("Update user status is ok");
		}
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
	
	/**
	 * 用户登陆
	 * @param userDTO
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public AjaxResponse login(UserDTO userDTO,HttpServletRequest request){
		try {
			Map<String, String> map = new HashMap<String, String>();
			String message = userService.loginValidate(userDTO,request);
			map.put("message", message);
			return new AjaxResponse(true, map);
		} catch (Exception e) {
			return new AjaxResponse(false);
		}
	}
	
	/**
	 * 用户退出
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exit")
	@ResponseBody
	public AjaxResponse exit(HttpServletRequest request){
		HttpSession session = request.getSession(false);//防止创建Session  
		if(session == null){ 
			return new AjaxResponse(true);
		}
		session.removeAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		return new AjaxResponse(true);
	}
	
	@RequestMapping(value = "/checkOldPassword")
	@ResponseBody
	public AjaxResponse checkOldPassword(@RequestParam String userId,String oldPass){
		try {
			if(StringUtils.isNotBlank(userId) && !"null".equals(userId)){
				Map<String, String> map = new HashMap<String, String>();
				if(oldPass!=null && !"".equals(oldPass)){
					UserDTO userDTO = this.userService.get(userId);
					String message = userService.checkOldPassWord(userDTO, oldPass);
					map.put("message", message);
					return new AjaxResponse(true, map);
				}else{
					map.put("message", "原密码不可为空！");
					return new AjaxResponse(true, map);
				}
			}
		} catch (AppException e) {
			return new AjaxResponse(false);
		}
		return null;
	}
	
	@RequestMapping(value = "/updatePassWord")
	public String updatePassWord(UserDTO userDTO,HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		UserDTO oldUserDTO = this.userService.get(userDTO.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
		//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		oldUserDTO.setOperator(username);
		oldUserDTO.setPassword(userDTO.getPassword());
		String flag = this.userService.updatePassword(oldUserDTO,usercode);
		log.info("Update user password is ok");
		map.put("flag", flag);
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
	}
}
