package com.sinovatech.search.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 通用工具类
 * 
 * 
 */
public class GeneralCommonUtil {
	public static final String PRIVILEGE_ORGIDLIST_SEPARTOR = ",";

	/**
	 * 判断int是否为大于0的整数
	 * 
	 * @param id
	 * @return >0 返回 true;否则返回false
	 */
	public static boolean isLargerThanZero(int id) {
		if (id > 0)
			return true;
		return false;
	}

	/**
	 * 判断集合是否为空,或者长度为0
	 * 
	 * @param coll
	 * @return 集合为Null，或者长度为0,返回 true;否则返回false
	 */
	public static boolean isNullOrSizeZero(Collection coll) {
		if (coll == null || coll.size() == 0)
			return true;
		return false;
	}

	/**
	 * 判断数组是否为空,或者长度为0
	 * 
	 * @param objs
	 * @return 数组为Null，或者长度为0,返回 true;否则返回false
	 */
	public static boolean isNullOrSizeZero(Object[] objs) {
		if (objs == null || objs.length < 1)
			return true;
		return false;
	}

	// add by wujun
	/**
	 * 判断long数组是否为空,或者长度为0
	 * 
	 * @param objs
	 * @return 数组为Null，或者长度为0,返回 true;否则返回false
	 */
	public static boolean isNullOrSizeZero(long[] objs) {
		if (objs == null || objs.length < 1)
			return true;
		return false;
	}

	/**
	 * 判断int数组是否为空,或者长度为0
	 * 
	 * @param objs
	 * @return 数组为Null，或者长度为0,返回 true;否则返回false
	 */
	public static boolean isNullOrSizeZero(int[] objs) {
		if (objs == null || objs.length < 1)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean isNullOrSizeZero(Map map) {
		if (map == null || map.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断一个字符串是否为null,或者长度为0
	 * 
	 * @param str
	 * @return str为null,或者长度为0 返回true,否则返回false
	 */
	public static boolean isNullOrSizeZero(String str) {
		if (str == null || "".equals(str))
			return true;
		return false;
	}

	/**
	 * 判断一个字符串是否为null,或者长度为0
	 * 
	 * @param str
	 * @return str为null,或者长度为0 返回true,否则返回false
	 */
	public static boolean isNullOrSizeZero_trim(String str) {
		if (str == null || "".equals(str.trim()))
			return true;
		return false;
	}

	/**
	 * 判断一个char是否为null,或者长度为0
	 * 
	 * @param cha
	 * @return cha为null 返回true,否则返回false
	 */
	public static boolean isNullChar(char cha) {
		String str = String.valueOf(cha);
		if (str == null || "".equals(str))
			return true;
		return false;
	}

	/**
	 * 判断一个Object 是否为null
	 * 
	 * @param obj
	 * @return obj为null 返回true,否则返回false
	 */
	public static boolean isNull(Object obj) {
		if (obj == null)
			return true;
		return false;
	}

	/**
	 * 判断字符串是否为null或者长度为0
	 * 
	 * @param obj
	 *            字符串的Object类型
	 * @return
	 */
	public static boolean isNullOrZero(Object obj) {
		return (isNull(obj) || isNullOrSizeZero_trim(obj.toString()));
	}

	/**
	 * 比较两个字符串相同与否
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compareString(String str1, String str2) {
		if (GeneralCommonUtil.isNullOrSizeZero(str1)
				|| GeneralCommonUtil.isNullOrSizeZero(str2)) {
			return false;
		}
		if (str1.trim().equals(str2.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 验证格式是yyyy的日期类型是否合法
	 */
	public static boolean checkDateYYYY(String str) {
		if (isNullOrSizeZero(str))
			return true;
		boolean flag = false;
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy");// 这里可以定义你想要监查的格式
		try {
			simpleDate.parse(str);
			flag = true;
		} catch (ParseException e) {
			return flag;
		}
		return flag;
	}

	/*
	 * 验证格式是yyyy-MM的日期类型是否合法
	 */
	public static boolean checkDateYYYYMM(String str) {
		if (isNullOrSizeZero(str))
			return true;
		boolean flag = false;
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM");// 这里可以定义你想要监查的格式
		try {
			simpleDate.parse(str);
			flag = true;
		} catch (ParseException e) {
			return flag;
		}
		return flag;
	}

	/*
	 * 验证格式是yyyy-MM-dd的日期类型是否合法
	 */
	public static boolean checkDateYYYYMMDD(String str) {
		if (isNullOrSizeZero(str))
			return true;
		boolean flag = false;
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");// 这里可以定义你想要监查的格式
		try {
			simpleDate.parse(str);
			flag = true;
		} catch (ParseException e) {
			return flag;
		}
		return flag;
	}

	/*
	 * 验证格式是yyyy-MM-dd kk-mm-ss的日期类型是否合法
	 */
	public static boolean checkDateYYYYMMDDHHMMSS(String str) {
		if (isNullOrSizeZero(str))
			return true;
		boolean flag = false;
		SimpleDateFormat simpleDate = new SimpleDateFormat(
				"yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);// 这里可以定义你想要监查的格式
		try {
			simpleDate.parse(str);
			flag = true;
		} catch (ParseException e) {
			return flag;
		}
		return flag;
	}

	/*
	 * 将Date型的日期按格式"yyyyMMdd"返回
	 */
	public static String converDateToString(Date date) {
		if (isNull(date))
			return "";
		String result = "";
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
		result = simpleDate.format(date);
		return result;
	}

	/*
	 * 将Date型的日期按指定格式返回
	 */
	public static String converDateToString(Date date, String formate) {
		if (isNull(date))
			return "";
		String result = "";
		SimpleDateFormat simpleDate = new SimpleDateFormat(formate);
		result = simpleDate.format(date);
		return result;
	}

	/*
	 * 将字符串表示的日期转换成指定格式的日期返回
	 */
	public static Timestamp converstringToTime(String dateString, String formate)
			throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(formate, Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date timeDate = dateFormat.parse(dateString);
		Timestamp dateTime = new Timestamp(timeDate.getTime());
		return dateTime;
	}

	/**
	 * 把 value 转成长度为 toLength 长度的字符串
	 * 
	 * @param value
	 *            要转成 String 的 long 型值
	 * @param toLength
	 *            转成 String 后的字符串的长度
	 * @return
	 */
	public static String longToStringLength(final long value, final int toLength) {
		// toLength长度下最大可以表示的long型值,返回的字符串对应的long值最大应该是maxLong
		long maxLong = 0l;
		StringBuffer maxLongValue = new StringBuffer();
		for (int i = 0; i < toLength; i++) {
			maxLongValue.append("9");
		}
		maxLong = Long.parseLong(maxLongValue.toString());
		if (value < 1l) {
			StringBuffer minValue = new StringBuffer();
			for (int i = 0; i < toLength - 1; i++) {
				minValue.append("0");
			}
			return minValue.append("1").toString();
		}
		if (value > maxLong) {
			return maxLongValue.toString();
		}
		String temp = String.valueOf(value);
		int strLength = temp.length();
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < toLength - strLength; i++) {
			ret.append("0");
		}
		ret.append(temp);
		return ret.toString();
	}

	/**
	 * 根据String列表适配成(?,?,?)的格式<br>
	 * 1.调用该方法之前保证 status 不是空
	 * 
	 * @param strList
	 * @return String
	 */
	public static String stringListToArraysSeperatedByComma(List<String> strList) {
		if (GeneralCommonUtil.isNullOrSizeZero(strList))
			return null;
		StringBuffer sb = new StringBuffer("(");
		int size = strList.size();
		String strs = "";
		for (int i = 0; i < size; i++) {
			sb.append("?,");
		}
		strs = sb.toString().substring(0, sb.toString().lastIndexOf(","));
		strs = strs + ")";
		return strs;
	}

	/**
	 * 根据指定的URL取出HTML代码
	 * 
	 * @param URLStr
	 * @return
	 */
	public static String getHTMLContent(String URLStr) {
		StringBuffer sb = new StringBuffer();
		String sourceContent = "";
		try {
			URL newURL = new URL(URLStr);
			HttpURLConnection httpConn = (HttpURLConnection) newURL
					.openConnection();
			httpConn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; CIBA)");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
				sb.append("\n");
			}
			sourceContent = sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sourceContent;
	}

	/**
	 * 去掉左右空格后字符串是否为空
	 * 
	 * @param astr
	 *            String
	 * @return boolean
	 */
	public static boolean isTrimEmpty(String astr) {
		if ((null == astr) || (astr.length() == 0)) {
			return true;
		}
		if (isNull(astr.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 正则表达式过滤html标签，只返回内容
	 * 
	 * @param htmlContent
	 * @return
	 */
	public static String getContentOfHtml(String htmlContent) {
		htmlContent = htmlContent.replaceAll("<[^>]*>", "").replaceAll(
				"&nbsp;", " ");
		return htmlContent;
	}

	public static String getBigNumber(String number) {
		if (number == null)
			return "";
		if (number.equals("1"))
			return "一";
		else if (number.equals("2"))
			return "二";
		else if (number.equals("3"))
			return "三";
		else if (number.equals("4"))
			return "四";
		else if (number.equals("5"))
			return "五";
		else if (number.equals("6"))
			return "六";
		else if (number.equals("7"))
			return "七";
		else if (number.equals("8"))
			return "八";
		else if (number.equals("9"))
			return "九";
		else if (number.equals("10"))
			return "十";
		else
			return number;
	}

	public static Timestamp string2Time2(String dateString)
			throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date timeDate = dateFormat.parse(dateString);
		Timestamp dateTime = new Timestamp(timeDate.getTime());
		return dateTime;
	}

	// add by wujun
	/**
	 * 将分隔符(逗号)隔开的字符串，转换为字符串的集合
	 * 
	 * @param str
	 *            输入的字符串
	 * @return
	 */
	public static Set<String> changeStringToSet(String str) {

		Set<String> strSet = new HashSet<String>();

		if (str != null) {
			String[] strs = str.split(PRIVILEGE_ORGIDLIST_SEPARTOR);
			for (String s : strs) {
				strSet.add(s);
			}
		}

		return strSet;
	}

	/**
	 * 将字符串的集合，转换为分隔符(逗号)隔开的字符串
	 * 
	 * @param strSet
	 *            字符串的集合
	 * @return
	 */
	public static String changeSetToString(Set<String> strSet) {

		StringBuilder sb = new StringBuilder();

		if (strSet != null) {
			for (String s : strSet) {
				sb.append(s);
				sb.append(PRIVILEGE_ORGIDLIST_SEPARTOR);
			}
		}

		String str = sb.toString();

		// 如果不为""，去掉结尾处的分隔符
		if (!str.equals(""))
			str = str.substring(0,
					str.length() - PRIVILEGE_ORGIDLIST_SEPARTOR.length());

		return str;
	}

	/**
	 * 通过用户账号（邮件地址形式）获取域名
	 * 
	 * @param userId
	 *            用户账号
	 * @return
	 */
	public static String getDomainNameByUserId(String userId) {
		// 特殊情况下，域名也可作为用户账号
		return userId.indexOf("@") == -1 ? userId : userId.substring(userId
				.indexOf("@") + 1);
	}
}
