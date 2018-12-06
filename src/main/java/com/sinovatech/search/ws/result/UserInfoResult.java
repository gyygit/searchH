package com.sinovatech.search.ws.result;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import com.sinovatech.search.ws.dto.UserInfoDTO;

/**
 * GetItemLogResult
 * 
 * @author 陈杰
 */
@XmlType(name = "GetItemLogResult")
public class UserInfoResult extends WSResult {

	/**
	 * Dto对象传递集合
	 */
	private List<UserInfoDTO> userInfoList;
	
	@XmlElementWrapper(name = "userInfoList")
	@XmlElement(name = "UserInfo")
	public List<UserInfoDTO> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<UserInfoDTO> userInfoList) {
		this.userInfoList = userInfoList;
	}

}
