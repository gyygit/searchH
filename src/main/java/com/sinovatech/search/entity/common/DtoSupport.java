package com.sinovatech.search.entity.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sinovatech.search.constants.LoggerConstants;
import com.sinovatech.search.utils.DateUtil;

public class DtoSupport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7018075941674873560L;

    private Map<String, Object[]> filedValueMap;

    /**
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * 
     */
    @JsonIgnore
    public Map<String, Object[]> getFiledValue() {
        if (filedValueMap == null) {
            filedValueMap = makeFiledValue();
        }

        return filedValueMap;
    }

    @JsonIgnore
    public Map<String, String> getFiledValueForString() {
        Map<String, String> rmap = new HashMap<String, String>();
        if (filedValueMap == null) {
            filedValueMap = makeFiledValue();
        }

        for (String f : filedValueMap.keySet()) {
            Object[] os = filedValueMap.get(f);
            String v = null;
            if (os[0].equals("Date")) {
                if ((java.util.Date) os[2] != null) {
                    v = DateUtil.format((java.util.Date) os[2], DateUtil.yyyyMMddHHmmssSpt);
                }
            } else {
                v = String.valueOf(os[2]);
            }
            rmap.put(f, v);
        }
        return rmap;
    }

    public String getPKfiledName() {
        return null;
    }

    private Map<String, Object[]> makeFiledValue() {
        List<String> lst = new ArrayList<String>();
        Map<String, Object[]> testfiledValueMap = new HashMap<String, Object[]>();
        Class<?> c = this.getClass();
        Field[] ff = null;
        for (Class<?> c2 = c; c2 != Object.class; c2 = c2.getSuperclass()) {
            ff = c2.getDeclaredFields();
            for (Field f : ff) {
                lst.add(f.getName());
                try {
                    Method method = c2.getMethod("get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1), null);
                    Object ov = method.invoke(this, null);
                    Object[] o = { f.getType().getSimpleName(), f.getType(), ov };
                    testfiledValueMap.put(f.getName(), o);
                } catch (Exception e) {
                    continue;
                }

            }
        }
        return testfiledValueMap;
    }
    
    /**
     * 格式化日志信息
     *
     * @author ChenZhuo
     * @date 2016年12月12日 上午10:51:19
     * @param strs
     * @return
     */
    public String toString(String... strs) {
        return toString(LoggerConstants.SEPARATE_SIGN, strs);
    }
    /**
     * 格式化日志信息,以“#”分隔
     *
     * @author ChenZhuo
     * @date 2016年12月12日 上午10:52:29
     * @param separatorChar
     * @param strs
     * @return
     */
    public String toString(String separatorChar, String[] strs) {
        if (strs == null)
            return null;
        StringBuilder builder = new StringBuilder();
        for (String s : strs) {
            if (s == null) {
                s = LoggerConstants.EMPTYSTR;
            } else if (s.length() == 0) {
                s = LoggerConstants.EMPTYSTR;
            } else if (s.indexOf(LoggerConstants.SEPARATE_SIGN) >= 0) {
                s = s.replace(LoggerConstants.SEPARATE_SIGN, LoggerConstants.NULL_PLACEHOLDER);
            }
            builder.append(s);
            builder.append(separatorChar);
        }
        return builder.toString();
    }
    
    public String[] splitField(String line) {
        return line.split(LoggerConstants.SEPARATE_SIGN);
    }

    /**
     * 字符串转成int
     * @param filed
     * @param values
     * @return
     * @throws Exception
     */
    protected static int parseInt(String filed, String values) throws Exception {
        try {
            return Integer.parseInt(values);
        } catch (Exception e) {
            throw new Exception("filed =[" + filed + "] values =[" + values + "] parseInt error!");
        }
    }

   

    /**
     * 字符串转成boolean
     *
     * @param filed
     * @param values
     * @return
     * @throws Exception
     */
    protected static boolean parseBoolean(String filed, String values) throws Exception {
        try {
            return Boolean.parseBoolean(values);
        } catch (Exception e) {
            throw new Exception("filed =[" + filed + "] values =[" + values + "] parseDate error!");
        }
    }
}
