package com.sinovatech.search.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.sinovatech.common.config.GlobalConfig;

public class SingletonUrlCheck {

    private static SingletonUrlCheck uniqueInstance = null;
    private Map<String,String> urlMap = null;
    private SingletonUrlCheck() {
    	urlMap = new HashMap<String,String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(PublicUtil.getClasspath()+File.separator+GlobalConfig.getProperty("sys.core", "checkurl")));
			String data = br.readLine();
			while( data!=null){ 
			      urlMap.put(data, data);
			      data = br.readLine(); 
			} 
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
 
    public static SingletonUrlCheck getInstance() {
       if (uniqueInstance == null) {
           uniqueInstance = new SingletonUrlCheck();
       }
       return uniqueInstance;
    }

	public Map<String, String> getUrlMap() {
		return urlMap;
	}
    
    
}