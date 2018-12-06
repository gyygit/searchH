package com.sinovatech.search.ectable.limit;

import javax.servlet.http.HttpServletRequest;

public abstract interface ILimitUtil {
	public abstract LimitInfo getLimitInfo(
			HttpServletRequest paramHttpServletRequest, String paramString,
			int paramInt);

	public abstract void setLimitInfo(
			HttpServletRequest paramHttpServletRequest, LimitInfo paramLimitInfo);
}