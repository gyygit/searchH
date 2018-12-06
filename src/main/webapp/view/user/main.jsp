<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ec" uri="/WEB-INF/tld/extremecomponents.tld"%>
<html>
<head>
	<title>用户管理</title>
</head>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<body>
	 <h2>
	 	<a href="${ctx}/user/list">查询</a>
	 	<a href="${ctx}/user/create">新增</a>
	 </h2>
</body>
</html>
