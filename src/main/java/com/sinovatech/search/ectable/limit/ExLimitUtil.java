package com.sinovatech.search.ectable.limit;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.context.HttpServletRequestContext;
import org.extremecomponents.table.limit.Filter;
import org.extremecomponents.table.limit.Limit;
import org.extremecomponents.table.limit.LimitFactory;
import org.extremecomponents.table.limit.Sort;
import org.extremecomponents.table.limit.TableLimit;
import org.extremecomponents.table.limit.TableLimitFactory;

public class ExLimitUtil implements ILimitUtil {
    private Limit limit;

    public LimitInfo getLimitInfo(HttpServletRequest request, String tableId, int rowDisplayed) {
        LimitInfo limitInfo = new LimitInfo();
        if ((tableId == null) && (rowDisplayed < 1)) {
            return limitInfo;
        }

        Context context = new HttpServletRequestContext(request);
        LimitFactory limitFactory = new TableLimitFactory(context, tableId);
        this.limit = new TableLimit(limitFactory);

        String prefixTableId = tableId;
        if ((prefixTableId == null) || ("".endsWith(prefixTableId.trim()))) {
            prefixTableId = "ec";
        }

        String currentRowsDisplayed = context.getParameter(prefixTableId + "_" + "crd");

        if (StringUtils.isNotBlank(currentRowsDisplayed)) {
            limitInfo.setRowDisplayed(Integer.parseInt(currentRowsDisplayed));
        } else {
            limitInfo.setFirstEnter(true);
            limitInfo.setRowDisplayed(rowDisplayed);
            return limitInfo;
        }

        limitInfo.setPageNum(this.limit.getPage());

        if (this.limit.isFiltered()) {
            initFilterMap(limitInfo);
        }

        if (this.limit.isSorted()) {
            Sort sort = this.limit.getSort();
            limitInfo.setSortProperty(sort.getProperty());
            limitInfo.setSortType(sort.getSortOrder());
        }
        return limitInfo;
    }

    public void setLimitInfo(HttpServletRequest request, LimitInfo limitInfo) {
        request.setAttribute("totalRows", new Integer(limitInfo.getTotalNum()));

        this.limit.setRowAttributes(limitInfo.getTotalNum(), limitInfo.getRowDisplayed());
    }

    private void initFilterMap(LimitInfo limitInfo) {
        Map filterMap = new HashMap();
        Filter[] filters = this.limit.getFilterSet().getFilters();

        Filter filter = null;
        for (int i = 0; i < filters.length; i++) {
            filter = filters[i];
            String value = filter.getValue();
            if ((value == null) || ("".equals(value))) {
                continue;
            }

            HqlProperty property = null;
            if ((value.length() > 1) && (value.startsWith("="))) {
                value = value.substring(1);

                property = new HqlProperty(filter.getProperty(), null, value, HqlProperty.TYPE_EQ, false);
            } else {
                value = value.replaceAll("'", "");
                property = new HqlProperty(filter.getProperty(), null, "%" + value + "%", HqlProperty.TYPE_LIKE, false);
            }

            filterMap.put(filter.getProperty(), property);
        }

        limitInfo.setFilterMap(filterMap);
    }
}