package com.sinovatech.search.entity.abstractdto;

import java.util.Date;

import com.sinovatech.search.entity.common.DtoSupport;

public abstract class UserAbstractDTO extends DtoSupport{
	//主键ID
	private String id;
	//用户名code
	private String code;
	//用户名昵称
	private String name;
	//密码
	private String password;
	//应用appcode
	private String appcode;
	//创建时间
	private Date createTime;
	// 修改时间
    private Date updateTime;
    // 操作人
    private String operator;
    // 操作人code
    private String operatorCode;
    
	//状态1可用2不可用
    private String  status;
    
//   private List<SearchAppDTO> searchApp;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPKfiledName() 
  	{
  		return "id";
  	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public UserAbstractDTO(){
		
	}
	
	public UserAbstractDTO(java.lang.String id){
		this.setId(id);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
//	public List<SearchAppDTO> getSearchApp() {
//		return searchApp;
//	}
//	public void setSearchApp(List<SearchAppDTO> searchApp) {
//		this.searchApp = searchApp;
//	}
	
}
