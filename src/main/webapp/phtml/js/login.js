$(document).ready(function(){
	jQuery("#code").focus();
	jQuery("#submit").bind("click",function(){beforeSubmit()});
	jQuery("#reset").bind("click",function(){resetText()});
});

function beforeSubmit(){
	var code = jQuery("#code").val();
	if(code == ""){
		showMessage("请输入登录账号！");
		return false;
	}
	if(!/^\w+$/g.test(code)){
		showMessage("请输入正确的登录账号！");
		return false;
	}
	if(code.indexOf("http://")>0 || code.indexOf("https://")>0 || code.indexOf("html")>0 || code.indexOf(".")>0){
		showMessage("登录账号不正确，请重新输入！");
		return false;
	}
	var pass = jQuery("#password").val();
	if(pass == ""){
		showMessage("请输入密码！");
		return false;
	}
	jQuery("#password").val(enString(pass));
	
	jQuery.ajax({
		type : "post",
		cache : "false",
		url : "/searchH/user/login",
		dataType : "json",
		data : jQuery("#loginForm").serializeArray(),
		success : function(result){
			if(result.success){
				if(result.data.message=='OK'){
					location.href="/searchH/phtml/searchapp/searchapplb.html";
				}else{
					showMessage(result.data.message);
					return false;
				}
			}else{
				showMessage("登陆后台出现异常！");
				return false;
			}
		},
		error : function(result){
			showMessage("请求出错了");
			return false;
		}
	});	
}

function showMessage(text){
	setDisable();
	document.onkeydown = function(e){ 
		 var ev = document.all ? window.event : e;
		 if(ev.keyCode==13) {
			 hide("cover","bccg");
			 removeDisable();
			 document.onkeydown = function(e){ 
				 var ev = document.all ? window.event : e;
				 if(ev.keyCode==13) {
					 jQuery("#submit").click();
				}
			}
		}
	}
	jQuery("#message").text(text);
	show("cover","bccg");
	jQuery("#goUrl").unbind("click").bind("click",function(){removeDisable();});
	jQuery("#closeUrl").unbind("click").bind("click",function(){removeDisable();});
}

function setDisable(){
	jQuery("#code").attr("disabled",true);
	jQuery("#password").attr("disabled",true);
}

function removeDisable(){
	jQuery("#code")[0].disabled = false;
	jQuery("#password")[0].disabled = false;
}

function resetText(){
	document.getElementById("loginForm").reset();
}

function enterIn(evt){
	var evt = evt ? evt : (window.event ? window.event : null);// 兼容ie和ff
	if (evt.keyCode == 13) {
		getExplorer(evt);
		beforeSubmit();
	}
}

function getExplorer(e) {
	var explorer = window.navigator.userAgent;
	// ie
	if (explorer.indexOf("MSIE") >= 0) {
		e.returnValue=false;
	}
	// firefox
	else if (explorer.indexOf("Firefox") >= 0) {
		e.preventDefault();
	}
	// Chrome
	else if (explorer.indexOf("Chrome") >= 0) {
		e.returnValue=false;
	}
	// Opera
	else if (explorer.indexOf("Opera") >= 0) {
		e.returnValue=false;
	}
	// Safari
	else if (explorer.indexOf("Safari") >= 0) {
		e.returnValue=false;
	}
}