package com.sinovatech.search.entity.abstractdto;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，注册应用业务表， 对应表:SEARCH_APP
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:2014-11-14 13:25:04<br />
 * @author liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */
import java.util.ArrayList;
import java.util.List;

import com.sinovatech.search.entity.common.DtoSupport;

public abstract class LuceneSearchAbstractDTO extends DtoSupport {

	public LuceneSearchAbstractDTO() {
	}

	public LuceneSearchAbstractDTO(java.lang.String id) {
		this.setId(id);
	}

	private String id;

	// 查询字段
	private List fileds;
	// 查询条件
	private List conditions;
	// 查询值
	private List querys;
	// 关系
	private List logic;
	
	// 字段名
	private List names;
	// 字段值
	private List values;
	// 当前页
	private String currPage;
	// 索引路径
	private String indexAddr;

	private String appCode;
	
	private String commandCode;
	
	public String getIndexAddr() {
		return indexAddr;
	}

	public void setIndexAddr(String indexAddr) {
		this.indexAddr = indexAddr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getNames() {
		if(names == null)
			names = new ArrayList<String>();
		return names;
	}

	public void setNames(List names) {
		this.names = names;
	}

	public List<String> getValues() {
		if(values == null)
			values = new ArrayList<String>();
		return values;
	}

	public void setValues(List values) {
		this.values = values;
	}

	public String getPKfiledName() {
		return "id";
	}

	public List<String> getFileds() {
		if(fileds == null)
			fileds =  new ArrayList<String>();
		return fileds;
	}

	public void setFileds(List fileds) {
		this.fileds = fileds;
	}

	public List<String> getConditions() {
		if(conditions ==null)
			conditions = new ArrayList<String>();
		return conditions;
	}

	public void setConditions(List conditions) {
		this.conditions = conditions;
	}

	public List<String> getQuerys() {
		return querys;
	}

	public void setQuerys(List querys) {
		this.querys = querys;
	}

	public List<String> getLogic() {
		if(logic ==null)
			logic = new ArrayList<String>();
		return logic;
	}

	public void setLogic(List logic) {
		this.logic = logic;
	}

	public String getCurrPage() {
		return currPage;
	}

	public void setCurrPage(String currPage) {
		this.currPage = currPage;
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
	
}
