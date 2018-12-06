<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
 
<script type="text/javascript">
location.href="<%=com.sinovatech.common.config.GlobalConfig.getProperty("weburl","webRegUrl")%>?redirectURL=<%=com.sinovatech.common.config.GlobalConfig.getProperty("weburl", "regBackUrl")%>";
</script>
