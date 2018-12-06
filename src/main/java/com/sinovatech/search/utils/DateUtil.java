package com.sinovatech.search.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sinovatech.search.entity.common.TimeEntity;

public class DateUtil {

	private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

	private static final String MONTH_PATTERN = "MM.dd";

	private static final String DEFAULT_DELIM = "-";

	public static String yyyyMMddHHmmssSpt = "yyyy-MM-dd HH:mm:ss";
	public static String yyyyMMddHHmmssSSpt = "yyyy-MM-dd HH:mm:ss.S";
	public static String yyyyMMddHHmmSpt = "yyyy-MM-dd HH:mm";
	public static String yyyyMMddSpt = "yyyy-MM-dd";
	public static String yyyyMMSpt = "yyyy-MM";
	public static String HHmmssSpt = "HH:mm:ss";
	public static String HHmmSpt = "HH:mm";

	private static SimpleDateFormat sdfStr = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	/**
	 * 转换日期
	 * 
	 * @param fileName
	 * @param key
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date parseDateTime(String dataTime, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(dataTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date(dataTime);
		}
	}

	public static String format(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public final static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static String parseDateTime(Date dataTime, String pattern) {
		return new SimpleDateFormat(pattern).format(dataTime);
	}

	public static String parseDateTimeString2String(String dataTime,
			String pattern) {
		return parseDateTime(parseDateTime(dataTime, pattern), pattern);
	}

	/**
	 * 验证日期是否合法（yyyy-MM-dd）
	 * 
	 * @param sDate
	 * @return
	 */
	public static boolean isValidDate(String sDate) {
		if ("null".equalsIgnoreCase(sDate)
				|| "undefined".equalsIgnoreCase(sDate)
				|| "000000".equalsIgnoreCase(sDate)) {
			return true;
		}
		if (!"null".equalsIgnoreCase(sDate)
				&& !"undefined".equalsIgnoreCase(sDate)
				&& !"000000".equalsIgnoreCase(sDate)) {
			String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
			// String datePattern1 = fmt;
			String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
					+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
					+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
					+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
					+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
					+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
			if ((sDate != null)) {
				Pattern pattern = Pattern.compile(datePattern1);
				Matcher match = pattern.matcher(sDate);
				if (match.matches()) {
					pattern = Pattern.compile(datePattern2);
					match = pattern.matcher(sDate);
					return match.matches();
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Get the first day of month
	 * 
	 * @param pattern
	 *            e.g. MM.dd yyyy-MM-dd
	 * @return
	 */
	public static String getFirstDayOfMonth(String pattern) {
		if (pattern == null)
			pattern = MONTH_PATTERN;
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DATE, 1);// set the first day of month
		return new SimpleDateFormat(pattern).format(date.getTime());
	}

	/**
	 * Get the current day of month
	 * 
	 * @param pattern
	 *            e.g. MM.dd yyyy-MM-dd
	 * @return
	 */
	public static String getCurrentDayOfMonth(String pattern) {
		if (pattern == null)
			pattern = MONTH_PATTERN;
		Calendar date = Calendar.getInstance();
		return new SimpleDateFormat(pattern).format(date.getTime());
	}

	/**
	 * Get the begin date and end date for query request
	 * 
	 * @param pattern
	 * @param delim
	 * @return
	 */
	public static String getBeginEndDate(String pattern, String delim) {
		if (delim == null)
			delim = DEFAULT_DELIM;
		return getFirstDayOfMonth(pattern) + delim
				+ getCurrentDayOfMonth(pattern);
	}

	/**
	 * Get the begin date and end date for query request
	 * 
	 * @param pattern
	 * @param delim
	 * @return
	 */
	public static String getDefaultBeginEndDate() {
		return getFirstDayOfMonth(MONTH_PATTERN) + DEFAULT_DELIM
				+ getCurrentDayOfMonth(MONTH_PATTERN);
	}

	/**
	 * Get the begin date and end date for query request
	 * 
	 * @param pattern
	 * @param delim
	 * @return
	 */
	public static String getBeforeDayBeginEndDate() {
		return getFirstDayOfMonth(MONTH_PATTERN) + DEFAULT_DELIM
				+ getBeforeDayOfMonth(MONTH_PATTERN);
	}

	private static String getBeforeDayOfMonth(String pattern) {
		if (pattern == null)
			pattern = MONTH_PATTERN;
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, -1);
		return new SimpleDateFormat(pattern).format(date.getTime());
	}

	/**
	 * 
	 * @return
	 */
	public static String getDefaultQueryDate() {
		Calendar date = Calendar.getInstance();
		return new SimpleDateFormat(DEFAULT_PATTERN).format(date.getTime());
	}

	/**
	 * 获取上个月第一天的日期
	 * 
	 * @return
	 */
	public static String getFirstDayOfLastMonth(String pattern) {
		if (pattern == null)
			pattern = DEFAULT_PATTERN;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, -1);
		return new SimpleDateFormat(pattern).format(cal.getTime());
	}

	/**
	 * 获取上个月最后一天的日期
	 * 
	 * @return
	 */
	public static String getLastDayOfLastMonth(String pattern) {
		if (pattern == null)
			pattern = DEFAULT_PATTERN;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		int MaxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), MaxDay);
		return new SimpleDateFormat(pattern).format(cal.getTime());
	}

	public static String getBeginEndDateOfLastMonth(String delim) {
		if (delim == null)
			delim = DEFAULT_DELIM;
		return getFirstDayOfLastMonth(MONTH_PATTERN) + delim
				+ getLastDayOfLastMonth(MONTH_PATTERN);
	}

	/**
	 * 获取套餐余量的开始与结束时间 时间格式:2012-08-17至2012-08-17
	 * 
	 * @return
	 */
	public static String getPackageFlowDayMonth() {
		Calendar cal = Calendar.getInstance();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);

		String result = "";
		switch (month) {
		case 0:
			if (day == 1) {
				year = year - 1;
				cal.set(year, 11, 1);
				result += (year) + "-12-1至" + (year) + "-12-"
						+ cal.getActualMaximum(Calendar.DATE);
			} else {
				result += year + "-" + (month + 1) + "-1至" + year + "-"
						+ (month + 1) + "-" + (day - 1);
			}
			break;
		default:
			if (day == 1) {
				cal.set(year, month - 1, 1);
				result += year + "-" + month + "-1" + "至" + year + "-" + month
						+ "-" + cal.getActualMaximum(Calendar.DATE);
			} else {
				result += year + "-" + (month + 1) + "-1" + "至" + year + "-"
						+ (month + 1) + "-" + (day - 1);
			}
			break;
		}

		return result;
	}

	/**
	 * 请求发起交易流水(长度50位以下) YH+currentTimeMillis+当前时间(yyyyMMddHHmmss)+4位随机数
	 * Description:<br>
	 * 
	 * @return
	 */
	public static String getApptx() {
		String apptx = "YH";
		String time = String.valueOf(System.currentTimeMillis());
		String now = sdfStr.format(new Date());
		apptx += time + now + (Math.round(Math.random() * 100));
		return apptx;
	}

	public static TimeEntity dealDate(Date date) {
		TimeEntity time = new TimeEntity();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		time.setYear(calendar.get(Calendar.YEAR));
		time.setMonth((calendar.get(Calendar.MONTH) + 1));
		time.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		time.setHour(calendar.get(Calendar.HOUR_OF_DAY));
		time.setMinite(calendar.get(Calendar.MINUTE));
		time.setSecond(calendar.get(Calendar.SECOND));
		return time;
	}
public static void main(String[] args) {
	Calendar date = Calendar.getInstance();
	date.setTime(new Date());
	date.add(Calendar.SECOND, 100);
	System.out.print("aaaaaaaaaaaa"+date.getTime());} 
}
