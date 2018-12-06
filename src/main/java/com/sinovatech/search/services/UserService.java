package com.sinovatech.search.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.UserDTO;
/**
 * User用户表 Service接口
 * @author Ma Tengfei
 *
 */
public interface UserService {

	/**
	 * 前台sql 不需要  “ where 1=1 ”
	 * @param sqlwhere  直接是条件
	 * @param limit
	 * @param sqlwhere
	 * @return
	 */
	public List list(LimitInfo limit,String sqlwhere);
	
	public List list(LimitInfo limit, UserDTO dto,String usercode,String appcode);
	
	public String save(UserDTO userDTO,String appcode) throws Exception;
	
	public void saveTX(UserDTO userDTO) throws AppException;
	
	public UserDTO get(String id) throws AppException;
	
	public String update(UserDTO userDTO,String usercode) throws Exception;
	
	public void updateTX(UserDTO userDTO) throws AppException;
	
	public String deleteTX(String ids,String usercode) throws AppException;
	
	public String loginValidate(UserDTO userDTO,HttpServletRequest request);
	
	public List<SearchAppDTO> getSearchAppInfo();
	
	public String checkOldPassWord(UserDTO userDTO,String oldPass);
	public String updatePassword(UserDTO userDTO,String usercode) throws Exception;
	public List<SearchAppDTO> getSearchAppInfoByCurrentUser(String appcode);
}
