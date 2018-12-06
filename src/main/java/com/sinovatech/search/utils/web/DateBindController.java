package com.sinovatech.search.utils.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public class DateBindController {
	
	 
	/**
	 * 绑定日期\长整形类型
	 * 
	 * @param binder
	 * @param webRequest
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateEditor());
		binder.registerCustomEditor(Long.class, new LongEditor());
	}
	
	public static void main(String[] args) {
		String str="10.96.0.0";
		
		String st1="10.122.0.0";
		
		long ipOne = Long.parseLong(str.substring(0,str.indexOf(".")));
	}
}
