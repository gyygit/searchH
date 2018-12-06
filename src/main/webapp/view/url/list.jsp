<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ec" uri="/WEB-INF/tld/extremecomponents.tld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="imgPath" value="${ctx}/images/"/>
<html>
<head>
	<title>请求管理</title>
	
	<link rel="stylesheet" type="text/css" href="${ctx}/css/extremecomponents.css" />
	<script type="text/javascript" src="${ctx}/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/user/AdUrl.js"></script>
</head>

<body>
	 <ec:table tableId="test" action="${ctx}/request/list" method="post" items="users" var="var" view="sinova" width="99%" 
					imagePath="${imgPath}/table/*.gif" showExports="false" onInvokeAction="$('#test').submit();">
					<ec:row>
						<ec:column title="序列" property="id" width="30%"></ec:column>
						<ec:column title="名字" property="name" width="30%"></ec:column>
					 
						<ec:column title="修改" property="oper" width="10%" filterCell="oper" sortable="false">
							<a href="#nogo" onclick="">编辑</a>
						</ec:column>
					</ec:row>
			</ec:table>
	<form:form id="inputForm" modelAttribute="userInfo" action="${ctx}/request/create" method="post" >
		姓名：<input type="text" id="name" name="name" size="50" value="${userInfo.name}" />&nbsp;	
		年龄：<input type="text" id="age" name="age" size="50" value="${userInfo.age}" />&nbsp;	
		性别<input type="text" id="sex" name="sex" size="50" value="${userInfo.sex}" />&nbsp;	
		<input id="button" type="button" value="提交" onclick="adUrl.save();"/>&nbsp;	
	</form:form>
</body>
</html>
