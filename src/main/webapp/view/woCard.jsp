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
<title>${title}</title>
<link rel="stylesheet" href="/wolm/css/jquery.mobile.min.css" />
<link rel="stylesheet" href="/wolm/css/reset.css" />
<link rel="stylesheet" href="/wolm/css/layout.css" />
<link rel="stylesheet" href="/wolm/css/base.css" />
<script src="/wolm/js/jquery.min.js"></script>
<script src="/wolm/js/jquery.mobile.min.js"></script>
<script src="/wolm/js/WeixinApi.js"></script>
<script type="text/javascript" src="http://image.shop.10010.com/upay/biz/js/payfee/googlemobile.js"></script>
</head>
<body>
	${msg}
	<input type="hidden" id="shareTitle" value="${title}" /> 
	<input type="hidden" id="imgUrl" value="${userAdInfo.picPath}${userAdInfo.tWlmAdPicsDTO[0].picUrl}" /> 
	<input type="hidden" id="lineLink" value="${shoturl}" />
	<input type="hidden" id="descContent" value="${desc}" />
	<script type="text/javascript">
		// 需要分享的内容，请放到ready里
		WeixinApi.ready(function(Api) {

			// 微信分享的数据
			var wxData = {
				"appId" : "", // 服务号可以填写appId
				"imgUrl" : $("#imgUrl").val(),
				"link" : $("#lineLink").val(),
				"desc" : $("#descContent").val(),
				"title" : $("#shareTitle").val()
			};

			// 分享的回调
			var wxCallbacks = {

			};

			// 用户点开右上角popup菜单后，点击分享给好友，会执行下面这个代码
			Api.shareToFriend(wxData, wxCallbacks);

			// 点击分享到朋友圈，会执行下面这个代码
			Api.shareToTimeline(wxData, wxCallbacks);

			// 点击分享到腾讯微博，会执行下面这个代码
			Api.shareToWeibo(wxData, wxCallbacks);

		});
	</script>
	<!-- noscritp -->
<noscript> 
<h1>
<a href="/" title="成功面前你不孤单，致富路上有沃相伴-中国联通沃联盟CPS 广告推广平台">
    成功面前你不孤单，致富路上有沃相伴-中国联通沃联盟 CPS 广告推广平台
</a></h1> 
<h2>创业可以简单，成功不再遥远</h2> 
<p>把握当下，分享快乐，成就未来。沃联盟邀您赚取推广汇报，实现创业梦想。</p> 
<h2>沃联盟介绍</h2> 
<p>中国联通沃联盟平台，是中国联通全新推出的广告推广平台，开启 CPS 联盟新模式。
快速赚钱，只需4步：注册账号，挑选广告，分享广告，赚取收益。2014 联通校园代理招
募中……成功面前你不孤单，致富路上有沃相伴-联通网上营业厅。  </p> 
</noscript> 
</body>
</html>