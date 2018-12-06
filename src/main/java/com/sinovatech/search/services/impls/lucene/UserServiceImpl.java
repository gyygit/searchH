package com.sinovatech.search.services.impls.lucene;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.common.util.StringUtils;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.UserDTO;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.SearchAppDAO;
import com.sinovatech.search.luceneindex.db.dao.UserDAO;
import com.sinovatech.search.services.UserService;
import com.sinovatech.search.utils.Des1;
/**
 * User用户表 Service实现类
 * @author Ma Tengfei
 *
 */
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDAO userDAOL;
	
	@Autowired
	private SearchAppDAO searchAppDAOL;
	
	/**
	 * 前台sql 不需要  “ where 1=1 ”
	 * @param sqlwhere  直接是条件
	 * @param limit
	 * @param sqlwhere
	 * @return
	 */
	public List list(LimitInfo limit, String sqlwhere) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setPageNum(limit.getPageNum());
		page.setQueryStr(sqlwhere);
		page.addSortList("createTime", Page.ORDER.DESC);
		List<UserDTO> lst = userDAOL.listForT(page);
		limit.setTotalNum(page.getTotalNum());
		return lst;
	}
	
	@Override
	public List list(LimitInfo limit, UserDTO dto,String usercode,String appcode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" (*:*) ");
		if (!"".equals(dto.getCode()) && (null != dto.getCode())) {
			sb.append(" AND (code:").append(dto.getCode()).append("*) ");
		}
		if (!"".equals(dto.getName()) && (null != dto.getName())) {
			sb.append(" AND (name:").append(dto.getName()).append("*) ");
		}
		if (!"".equals(dto.getStatus()) && (null!=dto.getStatus()) ) {
			sb.append(" AND (status:").append(dto.getStatus())
					.append("~0) ");
		}
		if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
			if(!"admin".equals(usercode)){
				sb.append(" AND ((code:").append(usercode).append("*) ");
				sb.append(" OR (operatorCode:").append(usercode).append("*))");
//				if(appcode.indexOf(",")!=-1){
//					String[] app = appcode.split(",");
//					sb.append(" AND (");
//					for(int i=0;i<app.length;i++){
//						sb.append("(appcode:").append(app[i]).append("*) OR ");
//					}
//					String cssb = sb.substring(0,sb.length()-3)+")";
//					sb = new StringBuffer();
//					sb.append(cssb);
//				}else{
//					sb.append(" AND (appcode:").append(appcode).append("*) ");
//				}
			}
		}
		return this.list(limit, sb.toString());
	}

	@Override
	public String save(UserDTO userDTO,String appcode) throws Exception {
//		List<SearchAppDTO> searchApp = new ArrayList<SearchAppDTO>(); 
//		String[] str = appcode.split(" ");
//		for(String obj : str){
//			SearchAppDTO search = getListSearchApp(obj);
//			searchApp.add(search);
//		}
//		userDTO.setSearchApp(searchApp);
		
		userDTO.setAppcode(appcode);
		String flag = "OK";
		flag = this.checkFieldISValid(userDTO);
		if (flag != "OK"){
			return flag;
		}
		userDTO.setCreateTime(new Date());
		this.saveTX(userDTO);
		return flag;
	}
	
	@Override
	public void saveTX(UserDTO userDTO) throws AppException {
		userDAOL.add(userDTO);
	}

	@Override
	public UserDTO get(String id) throws AppException {
		return userDAOL.getById(id);
	}

	@Override
	public String update(UserDTO userDTO,String usercode) throws Exception {
		String flag = "OK";
		UserDTO oldDTO = this.get(userDTO.getId());
		userDTO.setCreateTime(oldDTO.getCreateTime());
		userDTO.setUpdateTime(new Date());
		userDTO.setPassword(oldDTO.getPassword());
		userDTO.setOperatorCode(usercode);
		if(usercode.equals(userDTO.getCode())){
			if(!oldDTO.getAppcode().equals(userDTO.getAppcode())){
				flag = "ReLogin";
			}
		}
//		flag = this.checkFieldISValid(userDTO);
//		if (flag != "OK"){
//			return flag;
//		}
		this.updateTX(userDTO);
		return flag;
	}

	@Override
	public void updateTX(UserDTO userDTO) throws AppException {
		userDAOL.update(userDTO);
	}

	@Override
	public String deleteTX(String ids,String usercode) throws AppException {
		if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
			List<UserDTO> list = listByIds(ids);
			for(UserDTO dto : list){
				if(usercode.equals(dto.getCode())){
					return "对不起，你没有权限删除登录名为 "+usercode+" 的用户！";
				}else{
					userDAOL.del(dto);
				}
			}
			return "OK";
		}
		return null;
	}
	
	public List listByIds(String ids) throws AppException {
	    ids = "'" + ids.replaceAll(",", "','")+ "'";
	    return userDAOL.listByIds(ids);
	}
	
	private String checkFieldISValid(UserDTO userDTO){
		String flag = "OK";
		LimitInfo limit = new LimitInfo();
		List list1 = this.list(limit, " (*:*) AND  (code:"	+ userDTO.getCode() + "~0) ");
		if (list1 != null && list1.size() > 0) {
			return flag = "登录名不能重复，请重新录入!";
		}
		return flag;
	}
	
	@Override
	public String loginValidate(UserDTO userDTO,HttpServletRequest request){
		String code = userDTO.getCode();
		String pass = userDTO.getPassword();
		//密码解密
		pass = Des1.strDec(pass, "YHXWWLKJYXGS", "ZFCHHYXFL10C", "DES");
		
		if("admin".equals(code)){
			if("a123456".equals(pass)){
				request.getSession().setAttribute(RedisKeyConst.Search.CALL_WS_USERNAME, "超级管理员");
				request.getSession().setAttribute(RedisKeyConst.Search.SESSION_USERCODE, "admin");
				return "OK";
			}else{
				return "密码输入错误！";
			}
		}else{
			List<UserDTO> lists = this.getUserDTOByCode(code);
			if(!lists.isEmpty() && lists.size()>0){
				//判断 登录用户是否可用 ：1可用 2停用
				if(RedisKeyConst.Search.USERING.equals(lists.get(0).getStatus())){
					String  password = lists.get(0).getPassword();
					if(pass.equals(password)){
						//登录成功，将用户数据放入到Session中  
						request.getSession().setAttribute(RedisKeyConst.Search.CALL_WS_USERNAME, lists.get(0).getName());
						request.getSession().setAttribute(RedisKeyConst.Search.SESSION_USERCODE, lists.get(0).getCode());
						request.getSession().setAttribute(RedisKeyConst.Search.SESSION_APPCOCE, lists.get(0).getAppcode());
						return "OK";
					}else{
						return "密码输入错误！";
					}
				}else{
					return "该登录名已停用！";
				}
			}else{
				return "该登录名不存在！";
			}
		}
	}
	
	public List<UserDTO> getUserDTOByCode(String code){
		Page page = new Page();
		page.setQueryStr("(code:"+code+"~0)");
		page.setRowDisplayed(Integer.MAX_VALUE);
		List<UserDTO> lists = userDAOL.listForT(page);
		return lists;
	}
	
	@Override
	public List<SearchAppDTO> getSearchAppInfo(){
		Page page = new Page();
		StringBuffer sb = new StringBuffer();
//		sb.append(" (*:*)  AND (state:").append(RedisKeyConst.Search.USERING).append("~0) ");//只查询业务应用状态为 启用的
		sb.append(" (*:*) "); //查询所有业务应用(不管状态是启用或者是停用)
		page.setQueryStr(sb.toString());
		page.setRowDisplayed(Integer.MAX_VALUE);
		List<SearchAppDTO> lists = searchAppDAOL.listForT(page);
		return lists;
	}
	
	private SearchAppDTO getListSearchApp(String code){
		Page page = new Page();
		StringBuffer sb = new StringBuffer();
		sb.append(" (*:*)  AND (state:").append(RedisKeyConst.Search.USERING).append("~0) ");
		sb.append(" AND (appCode:").append(code).append("~0) ");
		page.setQueryStr(sb.toString());
		page.setRowDisplayed(Integer.MAX_VALUE);
		List<SearchAppDTO> lists = searchAppDAOL.listForT(page);
		return lists.get(0);
	}
	
	@Override
	public String checkOldPassWord(UserDTO userDTO,String oldPass){
		String pass = userDTO.getPassword();
		//密码解密
//		pass = Des1.strDec(pass, "YHXWWLKJYXGS", "ZFCHHYXFL10C", "DES");
		if(pass.equals(oldPass)){
			return "OK";
		}else{
			return "原密码输入错误！";
		}
	}
	
	@Override
	public String updatePassword(UserDTO userDTO,String usercode) throws Exception {
		String flag = "OK";
		userDTO.setUpdateTime(new Date());
		userDTO.setOperatorCode(usercode);
		if(usercode.equals(userDTO.getCode())){
			flag = "你修改了密码，请重新登录！";
		}
		this.updateTX(userDTO);
		return flag;
	}
	
	@Override
	public List<SearchAppDTO> getSearchAppInfoByCurrentUser(String appcode){
		Page page = new Page();
		StringBuffer sb = new StringBuffer();
//		sb.append(" (*:*)  AND (state:").append(RedisKeyConst.Search.USERING).append("~0) ");//只查询业务应用状态为 启用的
		sb.append(" (*:*) "); //查询所有业务应用(不管状态是启用或者是停用)
		if(appcode.indexOf(",")!=-1){
			String[] app = appcode.split(",");
			sb.append(" AND (");
			for(int i=0;i<app.length;i++){
				sb.append("(appCode:").append(app[i]).append("*) OR ");
			}
			String cssb = sb.substring(0,sb.length()-3)+")";
			sb = new StringBuffer();
			sb.append(cssb);
		}else{
			sb.append(" AND (appCode:").append(appcode).append("*) ");
		}
		page.setQueryStr(sb.toString());
		page.setRowDisplayed(Integer.MAX_VALUE);
		List<SearchAppDTO> lists = searchAppDAOL.listForT(page);
		return lists;
	}
}
