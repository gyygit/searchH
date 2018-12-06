package com.sinovatech.search.entity;

import com.sinovatech.search.entity.common.DtoSupport;

public class BankInfoBean extends DtoSupport{

	/**
	 * 银行信息
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String pic;
	private String bankName;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
}
