var flag = true;
$(document).ready(function() {
	editPassword.query();
	funnames = 'editPassword.query()';
//	 $("#tab_old_password").blur(function(){
//	 });
});

var editPassword = {
		query:function(){
			var map = getUrlParams();
			var id = map.get("id");
			var url = "/user/beforeEditUser.wap";
			var params="backdata=data&callback=editPassword.queryCallback&id="+id;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			//渲染模板
			jQuery("#content").setTemplateElement("editPassWord_template",null,{filter_data: false}).processTemplate(data);
			
		},
		save:function(pass){
			if(flag) {
				var url = "";
				if(editPassword.validDate(pass)) {
					flag = false;
					url = "/user/updatePassWord.wap";
					var params="backdata=data&callback=editPassword.processData"+QueryMain.getJson();
					QueryMain.loadData(url,params);
				}
			}
		},
		processData:function(data){
			if(data.flag=="OK"){
				alert("保存成功");
				flag = true;
				location.href="/searchH/phtml/searchapp/userlb.html";
			}
			else{
				alert("保存成功");
				flag = true;
//				location.href="/searchH/phtml/searchapp/userlb.html";
				alert(data.flag);
				location.href="/searchH/";
			}
		},
		
		validDate:function(pass){
			if($("#tab_old_password").val()==""){
				alert("原密码不能为空!");
				return false;
			}
			if($("#tab_old_password").val()!=pass){
				alert("原密码输入错误！");
				return false;
			}
			if($("#tab_password").val()==""){
				alert("新密码不能为空!");
				return false;
			}
			if($("#tab_verify_password").val()==""){
				alert("确认新密码不能为空!");
				return false;
			}
			if($("#tab_password").val()!= $("#tab_verify_password").val()){
				alert("新密码和确认新密码不一致!");
				return false;
			}
			var pwd = $("#tab_password").val();
			if(pwd.length<6 || pwd.length>15){
				alert("密码长度在6-15之间！");
				return false;
			}
			var patrn=/^(\w){6,15}$/;
			if (!patrn.exec(pwd)){
				alert("密码只能输入6-15个字母、数字、下划线！");
				return false 
			}
			return true;
		}
};

function CheckOldPwd(){
	 var oldPass=$("#tab_old_password").val();
	 var userId = $("#tab_id").val();
	 $.ajax({
		 type: "POST",
			url : "/searchH/user/checkOldPassword",
			data: {"userId":userId,"oldPass":oldPass},  
	        dataType:"json",  
	        async:false,  
	        cache:false,  
	        success: function(result){
	        	if(result.success){
	        		if(result.data.message=='OK'){
	        		}else{
	        			alert(result.data.message);
	        		}
	        	}
	        }
	 })
}
