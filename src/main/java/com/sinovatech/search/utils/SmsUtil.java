package com.sinovatech.search.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.sinovatech.common.config.GlobalConfig;

public class SmsUtil {

	private static final Log log = LogFactory.getLog(SmsUtil.class);

	/**
	 * 发送短信
	 * @return
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static int sendSms(String tel,String msg){
		//NtfplatService sendsingle = new NtfplatService();
		int flag = 1;
		try{
			//flag = sendsingle.sendSingle("11007",  "其他短信类下发", "验证码", msg, tel);
		} catch (Exception e) {
			flag = 9999999;
			log.error("短信发送异常："+e);
			e.printStackTrace();
		}finally{
			return flag;
		}
	}
	public static int sendSms2(String tel,String msg){
		//NtfplatService sendsingle = new NtfplatService();
		int flag = 0;
		try{
			String msgs = GlobalConfig.getProperty("sms", "codeMsg2").replace("XXXXXX", msg);
			//flag = sendsingle.sendSingle("11007",  "其他短信类下发", "验证码", msgs, tel);
		} catch (Exception e) {
			flag = 9999999;
			log.error("短信发送异常："+e);
			e.printStackTrace();
		}finally{
			return flag;
		}
	}
	/**
	 * 获取随机六位验证码
	 * @return
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static String getCode(){
		String str = "0123456789"; 
		Random random = new Random(); 
		StringBuffer num = new StringBuffer(); 
		List list =new ArrayList(); 
		while ( num.length() < 6) { 
			int number = random.nextInt(10); 
			if(num.length()>0&&!"0".equals(num.substring(0,1))){ 
				int n=0; 
				for(int i=0;i<list.size();i++){ 
					if(!((Object) number).equals(list.get(i))){ 
						n++; 
					} 
				} 
				if(n==num.length()){ 
					num.append(str.charAt(number)); 
					list.add(number); 
				} 
			}else{ 
				if(!"0".equals(String.valueOf(str.charAt(number))) && num.length()==0){ 
					num.append(str.charAt(number)); 
					list.add(number); 
				} 
			} 
		}
		return num.toString();
	}

}
