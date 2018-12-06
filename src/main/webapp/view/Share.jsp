<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8" />
<meta name="format-detection" content="telephone=no" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style"
	content="black-translucent">
<title>${data.UserAdInfo.tWlmAdInfoDTO.title}</title>
<link type="text/css" rel="stylesheet" href="/wolmweb/css/reset.css" />
<link type="text/css" rel="stylesheet" href="/wolmweb/css/layout.css" />
<link type="text/css" rel="stylesheet" href="/wolmweb/css/base.css" />

<script type="text/javascript" src="/wolmweb/js/jquery-1.8.0.min.js" ></script>
<!--[if IE 6]>
<script type="text/javascript" src="/wolmweb/js/png.js"></script>
<![endif]-->
<script type="text/javascript" src="/wolmweb/js/base.js" ></script>
<script src="/wolmweb/js/commonBase.js"></script>
</head>
<body>
<div id="registerPage">
	<div class="registerHeader shareHeader">
    	<div class="content"><img src="/wolmweb/images/logo.png"></div>
    </div>
    	 ${data.msg}
    	 <input type="hidden" id="shareTitle" value="${data.UserAdInfo.tWlmAdInfoDTO.title}" /> 
		<input type="hidden" id="lineLink" value="${data.shoturl}" />
		<input type="hidden" id="descContent" value="${data.UserAdInfo.tWlmAdInfoDTO.content}" />
    	 
    <div id="share_code">
    	<p class="tit">分享到微信朋友圈</p>
        <img id="qrImageUrl" src="${data.UserAdInfo.tWlmAdGetInfoDTO.qrImageUrl}">
        <p class="padT10">微信扫描二维码即可将网页分享到我的朋友圈</p>
    </div>
<!-- JiaThis Button BEGIN -->
<script type="text/javascript">
	var jiathis_config;
</script>
<script type="text/javascript" src="http://v3.jiathis.com/code/jiathis_r.js?btn=r5.gif&move=0" charset="utf-8"></script>
<!-- JiaThis Button END -->

    </div>
<input type="hidden" id="shareTitle" /> 
<input type="hidden" id="lineLink"/>
<input type="hidden" id="descContent"/>
<div id="footer">
	<div class="footNav clearFix tc line_grey">
    	<a href="#" class="grey">企业法人营业执照</a>|
    	<a href="#" class="grey">基础电信业务经营许可证</a>|
    	<a href="#" class="grey">增值电信业务经营许可证</a>|
    	<a href="#" class="grey">网络文化经营许可证</a>
    </div>
    <div class="space-05"></div>
    <p class="tc">Copyright@ 1999-2014&nbsp;中国联通&nbsp;版权所有</p>
    <p class="tc">中华人民共和国增值电信业务经营许可证 经营许可证编号：A2.B1.B2-20090003</p>
    <div class="tc padT15"><img src="/wolmweb/images/icon_foot.png" width="344" height="41" /></div>
</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	jiathis_config = {
			url:$("#lineLink").val(),
			summary:$("#descContent").val(),
			title:$("#shareTitle").val(),
			showClose:false,
			shortUrl:false,
			hideMore:false
		};
});
</script>
</html>