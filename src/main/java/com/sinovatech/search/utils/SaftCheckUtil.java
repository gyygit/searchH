package com.sinovatech.search.utils;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

public class SaftCheckUtil {
	
	 private final static String[] hexDigits = {"0", "1", "2", "3", "4",    
         "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"}; 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String appCode = "sc001";
		System.out.println(encrypt(appCode, random()));
	}
		
	public static int random(){
		Random random = new Random();
		return random.nextInt(10);
	}
	
	/**
	 * 加密
	 * @param appCode
	 * @param encryptStr
	 * @return
	 */
	public static String encrypt(String appCode, int num){
		
		String encryptStr = (appCode + DateUtil.format(new Date(), DateUtil.yyyyMMddSpt));
        encryptStr = num + encodeByMD5(num + encodeByMD5(encryptStr));
        
		return encryptStr;
	}
	/**  对字符串进行MD5加密     */
    private static String encodeByMD5(String originString){
        if (originString != null){
            try{
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                String pass = resultString.toUpperCase();
                return pass;
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }
    private static String byteArrayToHexString(byte[] b){    
        StringBuffer resultSb = new StringBuffer();    
        for (int i = 0; i < b.length; i++){    
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    /** 将一个字节转化成十六进制形式的字符串     */    
    private static String byteToHexString(byte b){
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
