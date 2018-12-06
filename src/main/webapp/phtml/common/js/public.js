// JavaScript Document
$('a').bind("focus", function(){    $(this).blur();})
//为右侧iframe宽高赋值 开始
	$(function(){
		if ($.browser.msie && ($.browser.version == '6.0') && !$.support.style) {//判断浏览器是否为IE6
			var winHeight = $(window).height() - 107;	
			var winWidth = $(window).width() - 220;
			$(".mainBoxWarp").height(winHeight);
			$("#iframeRight").width(winWidth);
			$("#iframeRight").height(winHeight);
			$(".pcs_tab_ul").width(winWidth);
			$(".myspan").width(winWidth /11 - 38);
			$("#treeBox").height(winHeight - 50);
			$(".bmLeft").height(winHeight + 75);
			$("#navBOX").width(winWidth);
			$("#nav_1").width(winWidth);
			$("#nav_1").find("span").width(winWidth /10);
			//var leng = $("#nav_1").children("span").length + 1;
			//$("#nav_1").find("span").width(winWidth /leng);
			//$("#gnqxNew").height(winHeight);
		}else{
			setInterval(function(){
				var winHeight = $(window).height() - 107;	
				var winWidth = $(window).width() - 220;
				$(".mainBoxWarp").height(winHeight);
				$("#iframeRight").width(winWidth);
				$("#iframeRight").height(winHeight);
				$(".pcs_tab_ul").width(winWidth);
				$(".myspan").width(winWidth /11 - 40);
				$("#treeBox").height(winHeight - 50);
				$(".bmLeft").height(winHeight + 75);
				$("#navBOX").width(winWidth);
				$("#nav_1").width(winWidth);
				$("#nav_1").find("span").width(winWidth /10);
				//var leng = $("#nav_1").children("span").length + 1;
				//$("#nav_1").find("span").width(winWidth /leng);
				//$("#gnqxNew").height(winHeight);
			},1)
		}
					 
	});
	//拖拽窗口 重新计算iframe宽高
	window.onresize = function (){
		var winHeight = $(window).height() - 107;	
			var winWidth = $(window).width() - 220;
			$(".mainBoxWarp").height(winHeight);
			$("#iframeRight").width(winWidth);
			$("#iframeRight").height(winHeight);
			$(".pcs_tab_ul").width(winWidth);
			$(".myspan").width(winWidth /11 - 38);
			$("#treeBox").height(winHeight - 50);
			$(".bmLeft").height(winHeight + 75);
			$("#navBOX").width(winWidth);
			$("#nav_1").width(winWidth);
			$("#nav_1").find("span").width(winWidth /10);
			//var leng = $("#nav_1").children("span").length + 1;
			//$("#nav_1").find("span").width(winWidth /leng);
			//$("#gnqxNew").height(winHeight);
	}
	//拖拽窗口 重新计算iframe宽高
//为右侧iframe宽高赋值 结束

//表格基偶行样式 开始	
$(document).ready(function(){  
	$(".tb_content1 tr:even").removeClass("odd");  
	$(".tb_content1 tr:odd").addClass("odd");  
});   
//表格基偶行样式 开始	

// 下拉框效果
$(function(){
	
	$(".selectbox dt span").click(function(e){
		e.stopPropagation(); 
		$(".selectbox dd").hide()
		$(".selectbox").css("z-index","1");
		$(e.target).parent().parent().find("dd").show();
		$(e.target).parents().css("z-index","100006");
	});
	$(".selectbox dd p").click(function(e){
		$(e.target).addClass('sel').siblings().removeClass('sel');
		$(e.target).parent().parent().find("dt span.fleft").empty().append($(this).text());
		$(e.target).parent().hide();
		$(e.target).parent().parent().css("z-index","1");
	});
	$(document).click(function(e){
		$(".selectbox dd").hide();
		$(this).parents().css({"z-index":"1"});
	});
	$(".selectbox").mouseleave(function(){
		$(this).find("dd").hide();	
		$(this).css({"z-index":"1"});
	})	   
})


//弹出层
function show(cover,id){	
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
	var s;   
	(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : 
	(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
	(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;		
	//如果是ie6，隐藏页面select
	if(Sys.ie=="6.0"){
		var n=document.getElementsByTagName("select").length;
		var m=document.getElementById(id).getElementsByTagName("select").length;
		for(var i=0;i<n;i++){
			document.getElementsByTagName("select")[i].style.display= 'none';}
		for(var j=0;j<m;j++){		
			document.getElementById(id).getElementsByTagName("select")[j].style.display= '';}
	}	
	var objCover=document.getElementById(cover);
	var objId=document.getElementById(id);
	var scrollW=document.documentElement.scrollWidth;
	var scrollH=document.documentElement.scrollHeight;
	if (Sys.safari || Sys.chrome){
		var scrollH=document.body.scrollHeight;
		var T=(document.documentElement.clientHeight-objId.clientHeight)/2+document.body.scrollTop;
	}else{
		var T=(document.documentElement.clientHeight-objId.clientHeight)/2+document.documentElement.scrollTop;}
	var L=(document.documentElement.clientWidth-objId.clientWidth)/2+document.documentElement.scrollLeft;	
	objCover.style.width=scrollW+"px";
	objCover.style.height=scrollH+"px";
	objCover.style.visibility="visible";	
	objId.style.top=T+"px";
	objId.style.left=L+"px";
	objId.style.visibility="visible";

		var popTopH =$("#"+id).find(".popTOP").height() +10;
		var winHeight = $(window).height() - 242;
		$("#"+id).find(".popInfoBox").height(winHeight - popTopH);
		$("#"+id).find(".popInfoBox").css("padding-top",popTopH);

	window.onresize=function (){	
		var objCover=document.getElementById(cover);
		var objId=document.getElementById(id);
		var scrollW=document.documentElement.scrollWidth;
		if(document.documentElement.clientHeight >= document.documentElement.scrollHeight){
			var scrollH=document.documentElement.clientHeight;	
		}else{
			var scrollH=document.documentElement.scrollHeight;}
		if (Sys.safari || Sys.chrome) {
			var T=(document.documentElement.clientHeight-objId.clientHeight)/2+document.body.scrollTop;
		}else{
			var T=(document.documentElement.clientHeight-objId.clientHeight)/2+document.documentElement.scrollTop;}
		var L=(document.documentElement.clientWidth-objId.clientWidth)/2+document.documentElement.scrollLeft;		
		objCover.style.width=scrollW+"px";
		objCover.style.height=scrollH+"px";		
		objId.style.top=T+"px";
		objId.style.left=L+"px";
		
	}
}

function showTB(cover,id){	
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
	var s;   
	(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : 
	(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
	(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;		
	//如果是ie6，隐藏页面select
	if(Sys.ie=="6.0"){
		var n=document.getElementsByTagName("select").length;
		var m=document.getElementById(id).getElementsByTagName("select").length;
		for(var i=0;i<n;i++){
			document.getElementsByTagName("select")[i].style.display= 'none';}
		for(var j=0;j<m;j++){		
			document.getElementById(id).getElementsByTagName("select")[j].style.display= '';}
	}	
	var objCover=document.getElementById(cover);
	var objId=document.getElementById(id);
	var scrollW=document.documentElement.scrollWidth;
	var scrollH=document.documentElement.scrollHeight;
	if (Sys.safari || Sys.chrome){
		var scrollH=document.body.scrollHeight;
		var T=(document.documentElement.clientHeight-objId.clientHeight)/2+document.body.scrollTop;
	}else{
		var T=(document.documentElement.clientHeight-objId.clientHeight)/2+document.documentElement.scrollTop;}
	var L=(document.documentElement.clientWidth-objId.clientWidth)/2+document.documentElement.scrollLeft;	
	objCover.style.width=scrollW+"px";
	objCover.style.height=scrollH+"px";
	objCover.style.visibility="visible";	
	objId.style.top=50+"px";
	objId.style.left=L+"px";
	objId.style.visibility="visible";

		var popTopH =$("#"+id).find(".popTOP").height() +10;
		var winHeight = $(window).height() - 242;
		$("#"+id).find(".popInfoBox").height(winHeight - popTopH);
		$("#"+id).find(".popInfoBox").css("padding-top",popTopH);

	window.onresize=function (){	
		var objCover=document.getElementById(cover);
		var objId=document.getElementById(id);
		var scrollW=document.documentElement.scrollWidth;
		if(document.documentElement.clientHeight >= document.documentElement.scrollHeight){
			var scrollH=document.documentElement.clientHeight;	
		}else{
			var scrollH=document.documentElement.scrollHeight;}
		if (Sys.safari || Sys.chrome) {
			var T=(document.documentElement.clientHeight-objId.clientHeight)/2+document.body.scrollTop;
		}else{
			var T=(document.documentElement.clientHeight-objId.clientHeight)/2+document.documentElement.scrollTop;}
		var L=(document.documentElement.clientWidth-objId.clientWidth)/2+document.documentElement.scrollLeft;		
		objCover.style.width=scrollW+"px";
		objCover.style.height=scrollH+"px";		
		objId.style.top=50+"px";
		objId.style.left=L+"px";
		
	}
}

function hide(cover,id){
	//将页面全部select换件设为可用状态
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
	var s;    
	(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : 
	(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;	
	if(Sys.ie=="6.0"){
		var n=document.getElementsByTagName("select").length;
		for(var i=0;i<n;i++){
			document.getElementsByTagName("select")[i].style.display= '';
		}
	}
	var objCover=document.getElementById(cover);
	var objId=document.getElementById(id);
	objCover.style.visibility="hidden";
	objId.style.visibility="hidden";
}
//给弹出层中table的父级元素赋值高度
function popTable(x){
			var popTopH =$("#"+x).find(".popTOP").height() +10;
			var winHeight = $(window).height() - 242;
			$("#"+x).find(".popInfoBox").height(winHeight - popTopH);
			$("#"+x).find(".popInfoBox").css("padding-top",popTopH);
	}
//给弹出层中table的父级元素赋值高度


/**
 * artDialog
 */
var DialogTemplate = {
	/**
	 * 成功信息提示框
	 * @param message 信息内容。
	 * @param fun 关闭提示框后的执行方法。可为空。
	 */
	//信息类型（“success”：成功；“error”：失败、错误；“warning”：警告；“question”：问题、询问；“info”：信息；）
	showSuccessMsg : function(message, fun) {
		var content = "<div class='fc-693 fb dialog-tip'><div class='col-md-3 col-sm-3 col-xs-3'><i class='fa fa-check-circle-o'></i></div><div class='col-md-9 col-sm-9 col-xs-9'><p > <span>"
				+ message + "</span></p></div></div>";
		showMessage(content, fun);
	},
	/**
	 * 失败或错误信息提示框
	 * @param message 信息内容。
	 * @param fun 关闭提示框后的执行方法。可为空。
	 */
	showErrorMsg : function(message, fun) {
		var content = "<div class='fc-e00 fb dialog-tip'><div class='col-md-3 col-sm-3 col-xs-3'><i class='fa fa-times-circle-o'></i></div><div class='col-md-9 col-sm-9 col-xs-9'><p > <span>"
				+ message + "</span></p></div></div>";
		showMessage(content, fun);
	},
	/**
	 * 警告信息提示框
	 * @param message 信息内容。
	 * @param fun 关闭提示框后的执行方法。可为空。
	 */
	showWarningMsg : function(message, fun) {
		var content = "<div class='fc-f60 fb dialog-tip'><div class='col-md-3 col-sm-3 col-xs-3'><i class='fa fa-exclamation-triangle'></i></div><div class='col-md-9 col-sm-9 col-xs-9'><p > <span>"
				+ message + "</span></p></div></div>";
		showMessage(content, fun);
	},
	/**
	 * 提示信息提示框
	 * @param message 信息内容。
	 * @param fun 关闭提示框后的执行方法。可为空。
	 */
	showQuestionMsg : function(message, fun) {
		var content = "<div class='fc-39e fb dialog-tip'><div class='col-md-3 col-sm-3 col-xs-3'><i class='fa fa-question-circle'></i></div><div class='col-md-9 col-sm-9 col-xs-9'><p > <span>"
				+ message + "</span></p></div></div>";
		showMessage(content, fun);
	},
	/**
	 * 普通信息提示框
	 * @param message 信息内容。
	 * @param fun 关闭提示框后的执行方法。可为空。
	 */
	showInfoMsg : function(message, fun) {
		var content = "<div class='fc-39e fb dialog-tip'><div class='col-md-3 col-sm-3 col-xs-3'><i class='fa fa-info-circle'></i></div><div class='col-md-9 col-sm-9 col-xs-9'><p > <span>"
				+ message + "</span></p></div></div>";
		showMessage(content, fun);
	},
	showLoading : function() {
		var content = '<span class="ui-dialog-loading">Loading..</span>';
		showMessage(content);
	},
	/**
	 * 信息对话框
	 * @param message 信息内容。
	 * @param okFun 点击“确认”按钮后，关闭对话框后的执行方法。可为空。
	 * @param cancelFun 点击“取消”按钮后，关闭对话框后的执行方法。可为空。
	 */
	showConfirm : function(message, okFun, cancelFun) {
		var text = "<div class='fc-f60 fb dialog-tip'><div class='col-md-3 col-sm-3 col-xs-3'><i class='fa fa-question-circle'></i></div><div class='col-md-9 col-sm-9 col-xs-9'><p > <br><br><span><font color='red'>"
				+ message + "</font></span></p></div></div>";
		dialog({
			fixed : true,
			width : 400,
			content : text,
			button : [ {
				value : '取消',
				callback : function() {
					this.close().remove();
					if (cancelFun) {
						cancelFun();
					}
					return false;
				}
			}, {
				value : '确定',
				callback : function() {
					this.close().remove();
					if (okFun) {
						okFun();
					}
					return false;
				},
				autofocus : true
			} ]
		}).showModal();
	},
	/**
	 * 是否删除询问对话框
	 * @param message 信息内容。
	 * @param fun 点击“确认”按钮后，关闭对话框后的执行方法。可为空。
	 */
	delConfirm : function(fun) {
		var text = "<div class='fc-f60 fb dialog-tip'><div class='col-md-3 col-sm-3 col-xs-3'><i class='fa fa-question-circle'></i></div>"
				+ "<div class='col-md-9 col-sm-9 col-xs-9'><p > <span>确定要删除吗？</span></p></div></div>";
		dialog({
			fixed : true,
			width : 400,
			content : text,
			okValue : "确定",
			ok : function() {
				this.close().remove();
				if (fun) {
					fun();
				}
				return false;
			},
			cancelValue : "取消",
			cancel : function() {
			}
		}).showModal();
	},
	/**
	 * 显示页面对话框
	 * @param titleText 标题
	 * @param w	宽度
	 * @param h	高度
	 * @param urlpath 页面链接地址
	 * @param fun 关闭对话框后执行的方法，可为空
	 */
	showPage : function(titleText, w, h, urlpath, fun) {
		dialog({
			title : titleText,
			fixed : true,
			width : w,
			height : h,
			url : urlpath,
			cancelValue : "取消",
			onclose : function() {
				this.close().remove();
				if (fun) {
					fun();
				}
			}
		}).showModal();
	}
};

/**
 * 显示信息提示框
 * @param text 信息内容。
 * @param fun 关闭提示框后的执行方法。可为空。
 */
var showMessage = function(text, fun) {
	if (text == null || text == undefined || text == '') {
		text = "<div class='fc-f60 fb dialog-tip'><div class='col-md-3 col-sm-3 col-xs-3'><i class='fa fa-exclamation-triangle'></i></div><div class='col-md-9 col-sm-9 col-xs-9'><p > <span>"
				+ message + "</span></p></div></div>";
	}
	dialog({
		fixed : true,
		width : 400,
		content : text,
		button : [ {
			value : '确定',
			callback : function() {
				this.close().remove();
				if (fun) {
					fun();
				}
				return false;
			}
		} ]
	}).showModal();
};