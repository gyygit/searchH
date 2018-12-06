package com.sinovatech.search.utils;

import com.sinovatech.common.util.StringUtils;
import com.sinovatech.search.entity.abstractdto.UserAbstractDTO;
/***
 * 
* @ClassName: RedisBusinessHelper
* @Description: 
* @author Ma Tengfei
* @date 2017年3月14日 下午2:20:30
*
 */
public class RedisBusinessHelper {

	private static JedisHelper jedisHelper = SpringContextHolder
			.getBean(JedisHelper.class);
	
	// 通过access_token得到登录的user信息
	public static UserAbstractDTO getUserDto(String access_token){
		String userDtojson="";
		UserAbstractDTO twlmUser = null;
		if(org.springframework.util.StringUtils.hasLength(access_token)){
			userDtojson = jedisHelper.get(access_token);
			if (StringUtils.isNotBlank(userDtojson)) {
				 twlmUser = JsonUtil.getDTO(userDtojson, UserAbstractDTO.class);
			}
		}
		return twlmUser;
	}
}
