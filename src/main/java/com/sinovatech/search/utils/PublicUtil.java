package com.sinovatech.search.utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class PublicUtil {
	
	/**
	 * 检查表单文件空
	 * @param file
	 * @return
	 */
/*	public static boolean isBlankOfFile(FormFile file){
		if(file==null||file.getFileName()==null||"".equals(file.getFileName().trim())){
			return true;
		}
		return false;
	}*/
	
	/**
	 * 格式化时间
	 * @param currentTime 返回时间格式为：yyyy-MM-dd
	 * @return
	 */
	public static boolean compareEq(Date one,Date other) {
	     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	     
	     if(one!=null&&other!=null){
	    	 
	    	 String dateString = formatter.format(one);
	    	 String dateString2 = formatter.format(other);
	    	 Date one_1 = null;
	    	 Date one_2 = null;
	    	 try {
	    		 one_1 = formatter.parse(dateString);
	    		 one_2 = formatter.parse(dateString2);
	    		 if(one_1.compareTo(one_2)==0||one_1.compareTo(one_2)==-1){
	    			 return true;
	    		 }
	    	 } catch (ParseException e) {
	    		 e.printStackTrace();
	    	 }
	     }
	     
	     return false;
	} 
	
	/**
	 * 格式化时间
	 * @param currentTime 返回时间格式为：yyyy-MM-dd
	 * @return
	 */
	public static Date getDateShort(Date currentTime) {
	     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	     
	     if(currentTime!=null){
	    	 
	    	 String dateString = formatter.format(currentTime);
	    	 Date currentTime_2 = null;
	    	 try {
	    		 currentTime_2 = formatter.parse(dateString);
	    	 } catch (ParseException e) {
	    		 e.printStackTrace();
	    	 }
	    	 return currentTime_2;
	     }
	     
	     return null;
	}

	/**
	 * 判断字符串非空
	 * @param val
	 * @return
	 */
    public static boolean isEmpty(String val)
    {
        if (val == null || "".equals(val))
            return true;
        return false;
    }
    
    /**
     * 判断集合非空
     * @param o
     * @return
     */
    public static boolean isCollectionEmpty (Collection o)
    {
        if(o == null || o.size() == 0)
            return true ;
        
        return false;
    }
    
    /**
     *  转义字符
     * @param likeStr
     * @return
     */
    public static String escapeSQLLike(String likeStr) {
        String str = StringUtils.replace(likeStr, "_", "/_");
        str = StringUtils.replace(str, "%",    "/%");
        str = StringUtils.replace(str, "?", "_");
        str = StringUtils.replace(str, "*", "%");
        return str;
    }
	
	/**
	 *  勿删,计算时间差，单位:秒
	 * @return 两个时间差的秒
	 */
	public static Long getTimeDistance(Date date1, Date date2)
    {
        SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh", "cn"));
        Long timeDistance = 0L;
        
        try
        {
            java.util.Date nowTime = m_format.parse(m_format.format(date1));
            java.util.Date endTime = m_format.parse(m_format.format(date2));
            timeDistance = (endTime.getTime() - nowTime.getTime()) / 1000;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return timeDistance;
    }
	
	/**
	 * 生成流水号
	 * @return
	 */
	public static String generateId() {
        String dateStr = new java.text.SimpleDateFormat(("yyyyMMddHHmmss")).format(new Date());
        //使随机数保持在三位
        int random;
        while(true){
        random =new Random().nextInt(1000);
        if(random>99&&random!=1000){
            break;
        }
        }
        return dateStr + random;
    }
	
	/**
     * 用于处理配送方式与配送费(例：1:30,2:40,3:50);
     * @param expressTypeStr
     * @return 
     */
    public static Map<String, String> dealWithExpressTypePrice(String expressTypeStr)
    {
        String[] expressCouple = expressTypeStr.split(",");
        Map<String, String> map = new HashMap<String, String>();
        for (String perSub : expressCouple)
        {
            map.put(perSub.substring(0, 1), perSub.substring(perSub.indexOf(":") + 1));
        }
        return map;
    }
    
    /**
     * 生成指定位数随机验证码
     * @param length
     * @return
     */
    public static String getCharacterAndNumber(int length){
        String val = "";

        Random random = new Random();
        for (int i = 0; i < length; i++){
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) // 字符串
            {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
                val += (char)(choice + random.nextInt(26));
            }
            else if ("num".equalsIgnoreCase(charOrNum)) // 数字
            {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
	 * 格式化时间
	 * @param currentTime 返回时间格式为：yyyy-MM-dd
	 * @return
	 */
	public static String getDateToString(Date currentTime,String patten) {
	    // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString="20080808";
	     SimpleDateFormat formatter = new SimpleDateFormat(patten);
	     if(currentTime!=null){
	    	 try {
	    	   dateString = formatter.format(currentTime);
	    	
	    	 } catch (Exception e) {
	    		 e.printStackTrace();
	    	 }
	     }
	     
	     return dateString;
	}
	

	/**
	 * @return 得到根路径对象
	 */
	public static File getClasspath(){
		/* 获取项目路径的方法*/
		return new File(PublicUtil.class.getResource("/").getPath());
	}
	
	public static void main(String[] args){
//		System.out.println("123456".substring(0,2));
//		System.out.println("123456".substring(0,4));
//		 
//		System.out.println(getDateToString(new Date(),"yyyyMMdd"));
		System.out.println(PublicUtil.getMsgDisChannel(" {myAllies} <a>ddddddddddd</a>", "web"));
	}
	
	/**
	 * @param content  消息内容
	 * @param channel  渠道             消息内容处理
	 * @return
	 */
	public static String getMsgDisChannel(String content,String channel){
		if("web".equals(channel)){
			return content.replace("{myFortune}", "<a onclick=url('/wolmweb/wealth/myWeath.html') >我的财富</a>")
					.replace("{myAllies}", "<a onclick=url('/wolmweb/ot/myFriends.html')>我的盟友</a>");
		}else{
			return content.replace("{myFortune}", "<a onclick=url('/wolm/wealth/wealth.html') >我的财富</a>")
					.replace("{myAllies}", "<a onclick=url('/wolm/ot/myFriend.html')>我的盟友</a>");
		}
	}
	private static Map<String,String> allProMap;
	 
}
