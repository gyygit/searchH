package com.sinovatech.search.utils.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.sinovatech.search.entity.abstractdto.UserAbstractDTO;
import com.sinovatech.search.utils.RedisBusinessHelper;
import com.sinovatech.search.utils.web.BaseController;
/***
 * 
* @ClassName: BaseClientController
* @Description: 
* @author Ma Tengfei
* @date 2017年3月14日 下午2:26:02
*
 */
public class BaseClientController extends BaseController{
	private static final Log log = LogFactory.getLog(BaseClientController.class);
	
	/**
	 * @param request  
	 * @return   去除token
	 */
	public String getRedisToken(HttpServletRequest request){
		String ua = request.getHeader("user-agent");
		String result = null;
		if(ua.indexOf(";#")!=-1){
			  result = ua.substring(ua.lastIndexOf(";#")+2, ua.length());
		}
		return result;
	}
	
	//得到用户是否登录
	public String getLoginState(HttpServletRequest request){
		UserAbstractDTO user = getUserByRedis(request);
		if(user==null){
			return "n";
		}else{
			return "y";
		}
	}
	
	// 通过access_token得到用户的登录信息
	public UserAbstractDTO getUserByRedis(HttpServletRequest request) {
		UserAbstractDTO userDTO = null;
		try {
			String token = request.getParameter("access_token");
			if (!StringUtils.hasLength(token)) {
				return userDTO;
			}
			log.info("获取到的access_token为：" + token);
			userDTO = RedisBusinessHelper.getUserDto(token);
		} catch (Exception e) {
			log.error("登录出错,错误原因为："+e);
		}
		return userDTO;
	}

}
