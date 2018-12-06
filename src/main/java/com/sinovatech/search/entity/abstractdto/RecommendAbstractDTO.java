package com.sinovatech.search.entity.abstractdto;

import com.sinovatech.search.entity.common.DtoSupport;

public abstract class RecommendAbstractDTO extends DtoSupport {
	private static final long serialVersionUID = -8654933723266414658L;
	// 唯一标识
	private String  id;
	// 业务应用编码
	private String appCode;
	// 业务数据分类编码
	private String commandCode;
	//搜索词
	private String searchKeyword;
	//标题
	private String title;
	//url
	private String url;
	//状态1可用2不可用
	private String status;
	//备注1
	private String remark1;
	//备注2
	private String remark2;
	//备注3
	private String remark3;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String getCommandCode() {
		return commandCode;
	}
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	public RecommendAbstractDTO() {

	}
	public RecommendAbstractDTO(java.lang.String id) {
		this.setId(id);
	}
	public String getPKfiledName() 
  	{
  		return "id";
  	}
}
