<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ec" uri="/WEB-INF/tld/extremecomponents.tld"%>
<%@ taglib prefix="d" uri="/WEB-INF/tld/domain.tld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="imgPath" value="${ctx}/images"></c:set>
<html>
<head>
	<title>用户管理</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/extremecomponents.css" />
	<script type="text/javascript" src="${ctx}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
	 <ec:table tableId="ec" action="${ctx}/user/listByPage" method="post" items="users" var="var" view="sinova" width="99%" 
					imagePath="${imgPath}/table/*.gif" showExports="false"  retrieveRowsCallback="limit" filterRowsCallback="limit"  onInvokeAction="$('#ec').submit();">
					<ec:row>
						<ec:column title="序列" property="id" width="30%"></ec:column>
						<ec:column title="名字" property="name" width="30%">
						</ec:column>
						<ec:column title="性别" property="sex" width="8%" filterCell="droplist" filterOptions="BMSDOMAIN.SEX">
							<d:viewDomain value="${var.sex}" domain="SEX" />
						</ec:column>
						<ec:column title="修改" property="oper" width="10%" filterCell="oper" sortable="false">
							<a href="#nogo" onclick="">编辑</a>
						</ec:column>
					</ec:row>
			</ec:table>
	<form:form id="inputForm" modelAttribute="userInfo" action="${ctx}/user/create" method="post" >
		姓名<input type="text" id="name" name="name" size="50" value="${userInfo.name}" />&nbsp;	
		年龄<input type="text" id="age" name="age" size="3" value="${userInfo.age}" />&nbsp;	
		性别：<d:selectDomain domain="SEX" name="sex"/>
		<input id="submit" type="submit" value="提交"/>&nbsp;	
	</form:form>
</body>
</html>
