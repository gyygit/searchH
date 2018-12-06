<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ec" uri="/WEB-INF/tld/extremecomponents.tld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="imgPath" value="${ctx}/images/"/>
<html>
<head>
	<title>用户管理</title>
	
	<link rel="stylesheet" type="text/css" href="${ctx}/css/extremecomponents.css" />
	<script type="text/javascript" src="${ctx}/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/user/AdUser.js"></script>
</head>

<body>
	<form:form id="inputForm" modelAttribute="userInfo" action="${ctx}/test/create" method="post" >
		 <table>  
        <tr>  
            <td>用户名:</td>  
            <td><input type="text" name="username" value="Name Test" /></td>  
            <td>*普通字符串</td>  
        </tr>  
        <tr>  
            <td>生日:</td>  
            <td><input type="text" name="bdate" value="2013-03-07" /></td>  
            <td>*支持格式: yyyy-MM-dd 或 MM/dd/yyyy</td>  
        </tr>  
        <tr>  
            <td>积分:</td>  
            <td><input type="text" name="point" value="1,000" /></td>  
            <td>*支持纯数字或带逗号分隔的数字</td>  
        </tr>  
        <tr>  
            <td colspan="3"><input type="submit" value="提交" /></td>  
        </tr>  
    </table>  
	</form:form>
</body>
</html>
