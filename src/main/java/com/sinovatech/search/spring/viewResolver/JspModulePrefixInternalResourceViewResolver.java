package com.sinovatech.search.spring.viewResolver;

import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 路径规则定义：<br />
 * ApplicationContextPath/child system path/ module path/function path/args or
 * query string
 * 
 * @author think
 * 
 */
public class JspModulePrefixInternalResourceViewResolver extends
		InternalResourceViewResolver {
	@Override
	protected Object getCacheKey(String viewName, Locale locale) {
		return getPrefix() + viewName;
	}

	@Override
	protected String getPrefix() {
		StringBuilder prefix = new StringBuilder();
		prefix.append(super.getPrefix());

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();

		String ctxPath = request.getContextPath();
		String uri = request.getRequestURI();
		StringTokenizer st = new StringTokenizer(uri, "/");

		if (StringUtils.isBlank(ctxPath)) {// 应用名称为空
			if (st.countTokens() >= 2) {
				prefix.append(st.nextToken()).append("/")
						.append(st.nextToken()).append("/");
			}
		} else {
			if (st.countTokens() >= 3) {
				st.nextToken();

				prefix.append(st.nextToken()).append("/")
						.append(st.nextToken()).append("/");
			}
		}

		return prefix.toString();
	}
}
