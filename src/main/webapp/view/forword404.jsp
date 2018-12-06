<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//String referer = request.getHeader("referer");

String aaaa = request.getSession().getServletContext().getRealPath("/");
out.println("===================>"+aaaa); 
%>
 
<script type="text/javascript">
//var referer = '${referer}';
//if(referer.indexOf(".web")!=-1){
	//location.href="/wolmweb/ot/404web.html";
//}else{
	location.href="/wolm/ot/404.html";
//}
</script>
