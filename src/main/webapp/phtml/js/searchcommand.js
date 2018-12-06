var flag = true;
$(document).ready(function() {
	searchCommand.query();
	funnames= 'searchCommand.query()';
	//splitPage.pageSplitJz();
	//addBankCard.query();
});

var searchCommand = {
		query:function(){
			var map = getUrlParams();
			var id = map.get("id");
			var appId= map.get("appId");
			//id为空，直接渲染模板;id不为空，查询后渲染模板
			var url = "/searchCommand/beforeEditSearchCommand.wap";
			var params="backdata=data&callback=searchCommand.queryCallback&id="+id+"&appId="+appId;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
			//渲染模板
//				jQuery("#content").setTemplateElement("app_template",null,{filter_data: false}).processTemplate(null);
		},
		queryCallback:function(data){
			//渲染模板
			jQuery("#content").setTemplateElement("app_template",null,{filter_data: false}).processTemplate(data);
		},
		save:function(indexType){
			if(flag) {
				if(searchCommand.validDate(indexType)){
					flag = false;
					var url = "";
					//id为空：新增，不为空：修改。
					var inputHidden = $("input[name='tab_id']").val();
					if(inputHidden == "")
						url = "/searchCommand/addSearchCommand.wap";
					else
						url = "/searchCommand/editSearchCommand.wap";
					var params="backdata=data&callback=searchCommand.processData"+QueryMain.getJson();
					QueryMain.loadData(url,params);
				}
			}
		},
		processData:function(data){
			if(data.flag=="OK"){
				alert("保存成功!");
				flag = true;
				//页面跳转
				location.href="/searchH/phtml/searchapp/searchcommandlb.html?app_id="+data.appId;
			}
			else{
				alert(data.flag);
				flag = true;
			}
		},
		validDate:function(indexType){
			var regstr = /^[0-9a-zA-Z]*$/g;
			if($("#tab_commandCode").val() == ""){
				alert("业务数据分类编码不能为空!");
				return false;
			} 	else if($("#tab_commandName").val() == ""){
				alert("业务应用名称不能为空!");
				return false;
			}
			if(indexType == 2){
        if($("#tab_viewName").val() == ""){
          alert("视图名称不能为空!");
          return false;
        }
      }
			if($("#tab_userName").val()==""){
          alert("用户名称不能为空!");
          return false;
      }
      if($("#tab_passWord").val()==""){
          alert("密码不能为空!");
          return false;
      }
      if($("#tab_linkAddress").val()==""){
          alert("链接地址不能为空!");
          return false;
      }
      
	 if($('input:radio:checked').val() == "1"){
		  if($("#tab_proName").val() == ""){
			  alert("执行存储过程时存储过程名称不能为空");
			  return false;
		  }
		  
	 }
			//长度验证
			if($("#tab_commandCode").val() != ""){
			  if($("#tab_commandCode").val().length > 100){
			    alert("业务数据分类编码仅允许输入100字符");
			    return false;
			  }
			}
			if($("#tab_commandName").val() != ""){
        if($("#tab_commandName").val().length > 32){
          alert("业务应用名称仅允许输入32字符");
          return false;
        }
      }
			if($("#tab_userName").val()!=undefined && $("#tab_userName").val()!=""){
			  if($("#tab_userName").val().length > 36){
			    alert("用户名称仅允许输入36字符");
			    return false;
			  }
			}
      if($("#tab_passWord").val()!=undefined && $("#tab_passWord").val()!=""){
        if($("#tab_passWord").val().length > 36){
          alert("密码仅允许输入36字符");
          return false;
        }
      }
      if($("#tab_linkAddress").val()!=undefined && $("#tab_linkAddress").val()!=""){
        if($("#tab_linkAddress").val().length > 1000){
          alert("链接地址仅允许输入1000字符");
          return false;
        }
      }
      if($("#tab_localAddress").val()!=undefined && $("#tab_localAddress").val()!=""){
        if($("#tab_localAddress").val().length > 36){
          alert("本地存储地址仅允许输入36字符");
          return false;
        }
      }
      if($("#tab_port").val()!=undefined && $("#tab_port").val()!=""){
        if($("#tab_port").val().length > 36){
          alert("端口号仅允许输入36字符");
          return false;
        }
      }
      if($("#tab_linkDir").val()!=undefined && $("#tab_linkDir").val()!=""){
        if($("#tab_linkDir").val().length > 36){
          alert("远程存储文件夹仅允许输入36字符");
          return false;
        }
      }
      if($("#tab_viewName").val()!=undefined && $("#tab_viewName").val()!=""){
        if($("#tab_viewName").val().length > 100){
          alert("视图名称仅允许输入100字符");
          return false;
        }
      }
      if($("#tab_sqlWhere").val()!=undefined && $("#tab_sqlWhere").val()!=""){
        if($("#tab_sqlWhere").val().length > 100){
          alert("视图查询条件仅允许输入100字符");
          return false;
        }
      }
			
			return true;
		}
};


	
