package com.sinovatech.search.utils;

public  class JisuanDto{
	private String appcode;
	private String commandcode;
	private String ku;
	private String date;
	private int cpage;
	private int tpage;
	private int jindu;
	private double haoshi;
	private String old;
	
	private String begindate;
	private String nextdate;
	private String flag;
	private String errorinfo;
	
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getNextdate() {
		return nextdate;
	}
	public void setNextdate(String nextdate) {
		this.nextdate = nextdate;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
	public double getHaoshi() {
		return haoshi;
	}
	public void setHaoshi(double haoshi) {
		this.haoshi = haoshi;
	}
	public String getOld() {
		return old;
	}
	public void setOld(String old) {
		this.old = old;
	}
	public String getKu() {
		return ku;
	}
	public void setKu(String ku) {
		this.ku = ku;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public String getCommandcode() {
		return commandcode;
	}
	public void setCommandcode(String commandcode) {
		this.commandcode = commandcode;
	}
	public int getCpage() {
		return cpage;
	}
	public void setCpage(int cpage) {
		this.cpage = cpage;
	}
	public int getTpage() {
		return tpage;
	}
	public void setTpage(int tpage) {
		this.tpage = tpage;
	}
	public int getJindu() {
		return jindu;
	}
	public void setJindu(int jindu) {
		this.jindu = jindu;
	}
}