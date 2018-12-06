<%@ page language="java" contentType="text/javascript"  pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%--
<%
	String backdata = request.getParameter("backdata");
	String callback = request.getParameter("callback");
%>
var <%=backdata%>=${json};
<%=callback%>(<%=backdata%>);
--%>
var ${backdata}=${json};
${callback}(${backdata});