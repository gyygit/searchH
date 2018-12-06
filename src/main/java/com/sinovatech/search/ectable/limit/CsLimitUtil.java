package com.sinovatech.search.ectable.limit;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

public class CsLimitUtil
  implements ILimitUtil
{
  public static String CUSTOM_LIMIT_TARGET_PAGE = "targetPageNum";

  public static String LIMIT_TABLE_ID_ = "LIMIT_TABLE_ID_";
  private String tableid;

  public LimitInfo getLimitInfo(HttpServletRequest request, String tableId, int rowDisplayed)
  {
    if (StringUtils.isBlank(tableId))
    {
      tableId = "Ex";
    }
    this.tableid = tableId;

    LimitInfo limitInfo = new LimitInfo();

    String targetPage = request.getParameter(tableId + "_" + 
      CUSTOM_LIMIT_TARGET_PAGE);
    if (StringUtils.isBlank(targetPage))
    {
      limitInfo.setPageNum(1);
    }
    else {
      limitInfo.setPageNum(Integer.valueOf(targetPage).intValue());
    }

    if (rowDisplayed < 1)
    {
      rowDisplayed = 10;
    }

    limitInfo.setRowDisplayed(rowDisplayed);

    return limitInfo;
  }

  public void setLimitInfo(HttpServletRequest request, LimitInfo limitInfo)
  {
    request.setAttribute(LIMIT_TABLE_ID_, this.tableid);

    request.setAttribute(this.tableid + "_" + "LIMITINFO", limitInfo);
  }
}