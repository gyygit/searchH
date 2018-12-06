package com.sinovatech.search.ectable.limit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LimitInfo {
	private static Log log = LogFactory.getLog(LimitInfo.class);
    private Object storeEx;
    private Map filterMap;
    //需要排序的属性OR字段
    private String sortProperty;
    //排序方式：ASC、DESC
    private String sortType;
    //
    private int pageNum;
    //每页显示的行数
    private int rowDisplayed =0;
    //总行数
    private int totalNum;
    private int endLineNum; 
    private boolean firstEnter;
    private String action;
    private Map map;//参数的特殊处理

    public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public LimitInfo()
    {
        sortType = "asc";
        pageNum = 1;
    }

    /**
     * 得到总页数
     * @return
     */
    public int getTotalPage()
    {
        int i = totalNum % rowDisplayed;
        return i != 0 ? totalNum / rowDisplayed + 1 : totalNum / rowDisplayed;
    }

    /**
     * 增加分页过滤属性
     * @param property
     */
    public void addFilterProperty(HqlProperty property)
    {
        if(filterMap == null)
            filterMap = new HashMap();
        if(property == null)
        {
            return;
        } else
        {
            filterMap.put(property.getPropertyName(), property);
            return;
        }
    }

    public Object[] getWhereHQL(String as)
    {
        Object re[] = new Object[2];
        re[0] = "";
        if(filterMap == null || filterMap.size() == 0)
            return re;
        StringBuffer sb = new StringBuffer();
        Map paramMap = new HashMap();
        for(Iterator it = filterMap.keySet().iterator(); it.hasNext();)
        {
            String propertyName = (String)it.next();
            HqlProperty property = (HqlProperty)filterMap.get(propertyName);
            if(property != null)
            {
                String name = property.getPropertyName();
                String valueName = name.replaceAll("\\.", "");
                Object min = property.getMin();
                Object max = property.getMax();
                Object compareValue=property.getCompareValue();
                if(min != null || max != null || compareValue != null){
                    if(!property.isUseRefQuery())
                    {
                        if(!"".equals(min) || !"".equals(max))
                            switch(property.getChkType())
                            {
                            case 1: // '\001'
                                sb.append(" and " + as + "." + name + " = '" + min + "'");
                                break;

                            case 2: // '\002'
                                sb.append(" and " + as + "." + name + " like '" + min + "'");
                                break;
                            }
                    } else
                    {
                        switch(property.getChkType())
                        {
                        default:
                            break;

                        case 1: // '\001'
                            sb.append(" and " + as + "." + name + " = :" + valueName);
                            paramMap.put(valueName, min);
                            break;

                        case 2: // '\002'
                            sb.append(" and " + as + "." + name + " like :" + valueName);
                            paramMap.put(valueName, min);
                            break;

                        case 3: // '\003'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + as + "." + name + " > :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min == null && max != null)
                            {
                                sb.append(" and " + as + "." + name + " <:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + as + "." + name + " > :" + valueName + "Min and " + as + "." + name + " <:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 4: // '\004'
                            sb.append(" and " + as + "." + name + " in (:" + valueName + ")");
                            paramMap.put(valueName, property.getMin());
                            break;

                        case 5: // '\005'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + as + "." + name + " >= :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + as + "." + name + " >= :" + valueName + "Min and " + as + "." + name + " <:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 6: // '\006'
                            if(min == null && max != null)
                            {
                                sb.append(" and " + as + "." + name + " <=:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + as + "." + name + " > :" + valueName + "Min and " + as + "." + name + " <=:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 7: // '\007'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + as + "." + name + " >= :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min == null && max != null)
                            {
                                sb.append(" and " + as + "." + name + " <=:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + as + "." + name + " >= :" + valueName + "Min and " + as + "." + name + " <=:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 8: // '\b'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + as + "." + name + " < :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min == null && max != null)
                            {
                                sb.append(" and " + as + "." + name + " > :" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + as + "." + name + " < :" + valueName + "Min or " + as + "." + name + " >:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 9: // '\t'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + as + "." + name + " <= :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + as + "." + name + " <= :" + valueName + "Min or " + as + "." + name + " >:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 10: // '\n'
                            if(min == null && max != null)
                            {
                                sb.append(" and " + as + "." + name + " >=:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + as + "." + name + " < :" + valueName + "Min or " + as + "." + name + " >=:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 11: // '\013'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + as + "." + name + " <= :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min == null && max != null)
                            {
                                sb.append(" and " + as + "." + name + " >=:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + as + "." + name + " >= :" + valueName + "Min or " + as + "." + name + " >=:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;
                        case 12: 
                            sb.append(" and " + as + "." + name + " <> :" + valueName);
                            paramMap.put(valueName, min);
                            break;
                        case 13:
                            if (compareValue != null)
                            {
                              sb.append(" and " + as + "." + name + " <= :" + 
                                valueName);
                              paramMap.put(valueName, compareValue);
                            }
                            break;
                          case 14:
                            if (compareValue != null)
                            {
                              sb.append(" and " + as + "." + name + " >= :" + 
                                valueName);
                              paramMap.put(valueName, compareValue);
                            }
                            break;
                        }
                    }
                }
            }
        }

        re[0] = sb.toString();
        re[1] = paramMap;
        return re;
    }

    /**
     * @return  不自动添加别名的方法
     */
    public Object[] getWhereHQL()
    {
        Object re[] = new Object[2];
        re[0] = "";
        if(filterMap == null || filterMap.size() == 0)
            return re;
        StringBuffer sb = new StringBuffer();
        Map paramMap = new HashMap();
        for(Iterator it = filterMap.keySet().iterator(); it.hasNext();)
        {
            String propertyName = (String)it.next();
            HqlProperty property = (HqlProperty)filterMap.get(propertyName);
            if(property != null)
            {
                String name = property.getPropertyName();
                String valueName = name.replaceAll("\\.", "");
                Object min = property.getMin();
                Object max = property.getMax();
                Object compareValue=property.getCompareValue();
                if(min != null || max != null || compareValue != null){
                    if(!property.isUseRefQuery())
                    {
                        if(!"".equals(min) || !"".equals(max))
                            switch(property.getChkType())
                            {
                            case 1: // '\001'
                                sb.append(" and " + name + " = '" + min + "'");
                                break;

                            case 2: // '\002'
                                sb.append(" and " + name + " like '" + min + "'");
                                break;
                            }
                    } else
                    {
                        switch(property.getChkType())
                        {
                        default:
                            break;

                        case 1: // '\001'
                            sb.append(" and " + name + " = :" + valueName);
                            paramMap.put(valueName, min);
                            break;

                        case 2: // '\002'
                            sb.append(" and " + name + " like :" + valueName);
                            paramMap.put(valueName, min);
                            break;

                        case 3: // '\003'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + name + " > :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min == null && max != null)
                            {
                                sb.append(" and " + name + " <:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + name + " > :" + valueName + "Min and " + name + " <:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 4: // '\004'
                            sb.append(" and " + name + " in (:" + valueName + ")");
                            paramMap.put(valueName, property.getMin());
                            break;

                        case 5: // '\005'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + name + " >= :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + name + " >= :" + valueName + "Min and " + name + " <:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 6: // '\006'
                            if(min == null && max != null)
                            {
                                sb.append(" and " + name + " <=:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + name + " > :" + valueName + "Min and " + name + " <=:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 7: // '\007'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + name + " >= :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min == null && max != null)
                            {
                                sb.append(" and " + name + " <=:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + name + " >= :" + valueName + "Min and " + name + " <=:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 8: // '\b'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + name + " < :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min == null && max != null)
                            {
                                sb.append(" and " + name + " > :" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + name + " < :" + valueName + "Min or " + name + " >:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 9: // '\t'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + name + " <= :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + name + " <= :" + valueName + "Min or " + name + " >:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 10: // '\n'
                            if(min == null && max != null)
                            {
                                sb.append(" and " + name + " >=:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + name + " < :" + valueName + "Min or " + name + " >=:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;

                        case 11: // '\013'
                            if(min != null && max == null)
                            {
                                sb.append(" and " + name + " <= :" + valueName);
                                paramMap.put(valueName, min);
                                break;
                            }
                            if(min == null && max != null)
                            {
                                sb.append(" and " + name + " >=:" + valueName);
                                paramMap.put(valueName, max);
                                break;
                            }
                            if(min != null && max != null)
                            {
                                sb.append(" and (" + name + " >= :" + valueName + "Min or " + name + " >=:" + valueName + "Max)");
                                paramMap.put(valueName + "Min", min);
                                paramMap.put(valueName + "Max", max);
                            }
                            break;
                        case 12: 
                            sb.append(" and " + name + " <> :" + valueName);
                            paramMap.put(valueName, min);
                            break;
                        case 13:
                            if (compareValue != null)
                            {
                              sb.append(" and " + name + " <= :" + 
                                valueName);
                              paramMap.put(valueName, compareValue);
                            }
                            break;
                          case 14:
                            if (compareValue != null)
                            {
                              sb.append(" and " + name + " >= :" + 
                                valueName);
                              paramMap.put(valueName, compareValue);
                            }
                            break;
                        }
                    }
                }
            }
        }

        re[0] = sb.toString();
        re[1] = paramMap;
        return re;
    }
    
    public String getOrder(String as)
    {
        if(sortProperty != null)
        {
            if(sortType != null)
                return " order by " + as + "." + sortProperty + " " + sortType;
            else
                return " order by " + as + "." + sortProperty;
        } else
        {
            return "";
        }
    }

    public Object[] getWhereHQLWithSort(String as)
    {
        Object re[] = getWhereHQL(as);
        re[0] = re[0] + " " + getOrder(as);
        return re;
    }

    public int getStartLineNum()
    {
        return (pageNum - 1) * rowDisplayed+1;
    }

    public HqlProperty getFilterParam(String name)
    {
        if(filterMap != null)
            return (HqlProperty)filterMap.get(name);
        else
            return null;
    }

    public Map getFilterMap()
    {
        return filterMap;
    }

    public String getAction(String name, String value)
    {
        if(StringUtils.isBlank(action))
            return "";
        if(action.endsWith("?"))
            return action + name + "=" + value;
        else
            return action + "&" + name + "=" + value;
    }

    public void setFilterMap(Map filterMap)
    {
        if(this.filterMap != null)
            this.filterMap.putAll(filterMap);
        else
            this.filterMap = filterMap;
    }

    public String getSortProperty()
    {
        return sortProperty;
    }

    public void setSortProperty(String sortProperty)
    {
        this.sortProperty = sortProperty;
    }

    public String getSortType()
    {
        return sortType;
    }

    public void setSortType(String sortType)
    {
        this.sortType = sortType;
    }

    public int getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(int pageNum)
    {
        this.pageNum = pageNum;
    }

    public int getRowDisplayed()
    {
        return rowDisplayed;
    }

    public void setRowDisplayed(int rowDisplayed)
    {
        this.rowDisplayed = rowDisplayed;
    }

    public int getTotalNum()
    {
        return totalNum;
    }

    public void setTotalNum(int totalNum)
    {
        this.totalNum = totalNum;
    }

    public int getEndLineNum()
    {
       if(endLineNum == 0)
            endLineNum = pageNum * rowDisplayed;
        return endLineNum;
    }
    public int getEndLineNumOk()
    {
        
            endLineNum = pageNum * rowDisplayed;
            if(endLineNum>this.getTotalNum()){
            	endLineNum=this.getTotalNum();
            }
        return endLineNum;
    }

    public void setEndLineNum(int endLineNum)
    {
        this.endLineNum = endLineNum;
    }

    public boolean isFirstEnter()
    {
        return firstEnter;
    }

    public void setFirstEnter(boolean firstEnter)
    {
        this.firstEnter = firstEnter;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public Object getStoreEx()
    {
        return storeEx;
    }

    public void setStoreEx(Object storeEx)
    {
        this.storeEx = storeEx;
    }
}
