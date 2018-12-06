package com.sinovatech.search.luceneindex.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class Page {
	 public static enum ORDER {
		 ASC     { @Override public String toString() { return "ASC"; } },//锟斤拷锟斤拷

		 DESC   { @Override public String toString() { return  "DESC";  } },//锟斤拷锟斤拷 
		 
		 INT_DESC   { @Override public String toString() { return  "INT_DESC";  } },//锟斤拷锟斤拷 
		 
		 INT_ASC   { @Override public String toString() { return  "INT_ASC";  } },//锟斤拷锟斤拷 
	 }
	// 高亮显示
	private String highLighterWord;
	
	private   Map<String,String> highlighterMap = new HashMap<String,String> ();
	public void setHighlighterMap(Map<String, String> highlighterMap) {
		this.highlighterMap = highlighterMap;
	}
	public void setSortList(List<String[]> sortList) {
		this.sortList = sortList;
	}
	//子业务
 
	private String[] commandCode;
	
	private String writeHotword;//要记录的热词
	//注册应用code
	private String appCode;
	//搜索条件 
	private String queryStr;
	//排序字段
	private List<String[]> sortList = new ArrayList<String[]> ();
	//当前页
	private int pageNum=1;
	// 每页显示的行数
	private int rowDisplayed=10;
	// 总行数
	private int totalNum;
	// 读取最后数
	private int endLineNum;
	//存放数据
	private List<Map<String,String>> listData;
    //错误信息
	private String errorMsg;
	//关键词名称
	private String searchKeyName;
	//关键词
	private String searchKeyWord;
	
	//分组字段名
    private String group_groupField;
    //每组获取记录的数量
	 private int group_zuSize;
	 
	//存放分组数据 String 是分组标题 List<Map<String,String>>是组内记录
	private Map<String,List<Map<String,String>>> listGroupData;
    
	public Map<String, List<Map<String, String>>> getListGroupData() {
		return listGroupData;
	}
	public void setListGroupData(
			Map<String, List<Map<String, String>>> listGroupData) {
		this.listGroupData = listGroupData;
	}
	public String getGroup_groupField() {
		return group_groupField;
	}
	public void setGroup_groupField(String group_groupField) {
		this.group_groupField = group_groupField;
	}
	public int getGroup_zuSize() {
		return group_zuSize;
	}
	public void setGroup_zuSize(int group_zuSize) {
		this.group_zuSize = group_zuSize;
	}
	public String getSearchKeyName() {
		return searchKeyName;
	}
	public void setSearchKeyName(String searchKeyName) {
		this.searchKeyName = searchKeyName;
	}
	public String getSearchKeyWord() {
		return searchKeyWord;
	}
	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}
	public String getWriteHotword() {
		return writeHotword;
	}
	public void setWriteHotword(String writeHotword) {
		this.writeHotword = writeHotword;
	}
	public  Page(){}
 
	public  Page(String appCode,String[] commandCode){ 
		this.appCode= appCode;
		this.commandCode=commandCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

 
	public String[] getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String[] commandCode) {
		this.commandCode = commandCode;
	}
 
	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	
	 
	public List<Map<String, String>> getListData() {
		return listData;
	}
	public void setListData(List<Map<String, String>> listData) {
		this.listData = listData;
	}
	
	public List<String[]> getSortList() {
		return sortList;
	}
 
	public void  addSortList(String field, Page.ORDER order ) {
		String[]  str = new String[]  {field,order.toString()};
		sortList.add(str);
	}
 
	public Map<String, String> getHighlighterMap() {
		return highlighterMap;
	}
	public void addHighlighterMap(String hfiled) {
		highlighterMap.put(hfiled, hfiled);
	}

	public String getHighLighterWord() {
		return highLighterWord;
	}

	public void setHighLighterWord(String highLighterWord) {
		this.highLighterWord = highLighterWord;
	}


	/**
	 * 锟矫碉拷锟斤拷页锟斤拷
	 * 
	 * @return
	 */
	public int getTotalPage() {
		int i = totalNum % rowDisplayed;
		return i != 0 ? totalNum / rowDisplayed + 1 : totalNum / rowDisplayed;
	}

	public int getStartLineNum() {
		return (pageNum - 1) * rowDisplayed;
	}
 

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getRowDisplayed() {
		return rowDisplayed;
	}

	public void setRowDisplayed(int rowDisplayed) {
		this.rowDisplayed = rowDisplayed;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getEndLineNum() {
		if (endLineNum == 0)
			endLineNum = pageNum * rowDisplayed;
		return endLineNum;
	}

	public void setEndLineNum(int endLineNum) {
		this.endLineNum = endLineNum;
	}
	
	 
}

