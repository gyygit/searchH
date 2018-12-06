package com.sinovatech.search.entity.abstractdto;

import com.sinovatech.search.entity.common.DtoSupport;

public abstract class CreateIndexProcessLogAbstractDTO extends DtoSupport {
	
	
	private String id;
	private String cdate;
	private String appcode;
	private String commandcode;
	private String cpage;
	private String totalpage;
	private String ku;
	private String old;
	private String errinfo;//错误信息
	private String flag;//2=错误1=正确
	private String startTime;
	private String endTime;
	private long dobegintime;
	private long doendtime;
	
	public String getErrinfo() {
		return errinfo;
	}
	public void setErrinfo(String errinfo) {
		this.errinfo = errinfo;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public long getDoendtime() {
		return doendtime;
	}
	public void setDoendtime(long doendtime) {
		this.doendtime = doendtime;
	}
	public long getDobegintime() {
		return dobegintime;
	}
	public void setDobegintime(long dobegintime) {
		this.dobegintime = dobegintime;
	}
	public String getOld() {
		return old;
	}
	public void setOld(String old) { 
		this.old = old;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public  CreateIndexProcessLogAbstractDTO(java.lang.String id)
    {
           this.setId(id);
    }
	
	public  CreateIndexProcessLogAbstractDTO()
    {
    }
	public String getCdate() {
		return cdate;
	}
	public void setCdate(String cdate) {
		this.cdate = cdate;
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
	public String getCpage() {
		return cpage;
	}
	public void setCpage(String cpage) {
		this.cpage = cpage;
	}
	public String getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(String totalpage) {
		this.totalpage = totalpage;
	}
	public String getKu() {
		return ku;
	}
	public void setKu(String ku) {
		this.ku = ku;
	}
	

	public String getPKfiledName() {
        return "id";
    }
	
	

}
