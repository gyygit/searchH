package com.sinovatech.search.entity;
/**
 * 保存分组数据javaBean
 * @author Ma Tengfei
 *
 */
public class GroupBean {
	private String groupKey;
	private int groupNum;
//	private String subGroupKey;
	
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	public int getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}
//	public String getSubGroupKey() {
//		return subGroupKey;
//	}
//	public void setSubGroupKey(String subGroupKey) {
//		this.subGroupKey = subGroupKey;
//	}
}
