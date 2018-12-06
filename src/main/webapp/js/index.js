/**
 * 首页加载banner
 */
$(function(){
	$.getJSON("http://518.10010.com/adweb/activity/showAd?placeCode=WCF_HOME_AD_001&areaCode="+$.cookie('CHANGE_AREA')+"&jsoncallback=?",jsonfeed=function(json){
		var data = eval('('+json+')');
		$(".sybj_tr_l").setTemplateURL("/wcf/template/banner.html");
		$(".sybj_tr_l").processTemplate(data);
		
		$("#shops-focus1").adfocus({
	        drection: "left",
	        numbox: ".numboxcont",
	        imgbox: ".imgbox",
	        speed: 5000,
	        addClass: "pptOn",
			imgboxWidth:890,
			imgboxHeight:300,
			imgLen:data.ad.item.length,
			pull:"",
			usevent:"mouseover"
	    });
		hotTop();
		$("#slider .slide").eq(0).trigger('mouseover');
	});
});

/**
 * 首页加载公告
 */
$(function(){
	$.getJSON("http://518.10010.com/adweb/activity/showNotice?placeCode=WCF_NT_001&areaCode="+$.cookie('CHANGE_AREA')+"&jsoncallback=?",jsonfeed=function(json){
		var data = eval('('+json+')');
		$(".gonggao_ul").setTemplateURL("/wcf/template/placard.html");
		$(".gonggao_ul").processTemplate(data);
	});
	//idxUserInfo.executequery();
});

/**
 * 首页加载超级返
 */
$(function(){
	$.getJSON("/wcf/dynamic/ad/showCjf?placeCode=WCF_NT_001&areaCode="+$.cookie('CHANGE_AREA')+"&jsoncallback=?",jsonfeed=function(data){
		$("#cjf").setTemplateURL("/wcf/template/cjf.html");
		$("#cjf").processTemplate(data);
	});
});

var idxUserInfo = {
		executequery:function(){
			var url = "/wcf/dynamic/user/getuser";
			var reqData = "backdata=data&callback=idxUserInfo.callbackhandler&busiException=idxUserInfo.busiException";
			QueryMain.loadData(url,reqData);
		},
		callbackhandler:function(data){
			/**/
			if(data.loginState != '0'){
				//未登录
				$('.sy_xxkb_1').hide();
				$('.sy_xxkb_2').show();
				//QueryMain.showLoginModal();
			}else{
				//已登录
				$('.sy_xxkb_1').show();
				$('.sy_xxkb_2').hide();
			}
			
			QueryMain.closeModal();
		},
		busiException:function(data){
			//
		}
}





