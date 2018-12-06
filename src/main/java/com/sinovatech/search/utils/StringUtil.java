package com.sinovatech.search.utils;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author ChenZhuo
 * @date 2016年12月13日 下午4:07:29
 */
public class StringUtil {
    
    /**
     * 处理字符串值
     *
     * @author ChenZhuo
     * @date 2016年12月13日 下午4:20:02
     * @param str
     * @return
     */
    public static String processValue(String str) {
        //str = str.trim();
        return StringUtils.isEmpty(str) ? "" : str;
    }

}
