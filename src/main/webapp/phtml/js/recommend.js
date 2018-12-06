var flag = true;
$(document).ready(function() {
	recommend.query();
	funnames = 'recommend.query()';
});

var recommend = {
		query:function(){
			var map = getUrlParams();
			var id = map.get("id");
			//id为空：新增，不为空：修改。
			var url = "/recommend/beforeEditRecommend.wap";
			var params="backdata=data&callback=recommend.queryCallback&id="+id;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			//渲染模板
			jQuery("#content").setTemplateElement("recommend_template",null,{filter_data: false}).processTemplate(data);
			if(data.m.id==""){
				recommend.getSearchAppBeforeAdd();
			}
		},
		save:function(){
			if(flag) {
				if(recommend.validDate1()) {
					var commCode = recommend.getCheckedCommandCode();
					if(commCode==undefined || commCode ==null ||commCode ==""){
						alert("业务数据分类名称不能为空!");
						//return false;
					}else{
						if(recommend.validDate2()) {
							flag = false;
							var url = "";
							//id为空：新增，不为空：修改。
							var inputId = $("#tab_id").val();
							if(inputId == ""){
								url = "/recommend/addRecommend.wap";
							}else{
								url = "/recommend/editRecommend.wap";
							}	
							var params="backdata=data&callback=recommend.processData"+QueryMain.getJson()+"&commandCode="+commCode;
							QueryMain.loadData(url,params);
						}
					}
				}
			}
		},
		processData:function(data){
			if(data.flag=="OK"){
				alert("保存成功");
				flag = true;
				location.href="/searchH/phtml/searchapp/recommendlb.html";
			}
			else{
				alert(data.flag);
				flag = true;
			}
		},
		validDate1:function(){
			if($('#mySelect option:selected') .val()=="0"){
				alert("业务应用名称不能为空，请选择！");
				return false;
			}
			return true;
		},
		validDate2:function(){
			if($("#tab_searchKeyword").val()==""){
				alert("搜索关键词不能为空！");
				return false;
			}
			if($("#tab_title").val()==""){
				alert("标题不能为空！");
				return false;
			}
			if($("#tab_url").val()==""){
				alert("URL不能为空！");
				return false;
			}
			if($("#tab_url").val()!=undefined && $("#tab_url").val()!=""){
		        if($("#tab_url").val().length > 1000){
		          alert("url仅允许输入1000字符");
		          return false;
		        }
		        //& 符 转义
		        $("#tab_url").val($("#tab_url").val().replace(/\&/g,"%26"));
		     }
			return true;
		},
		
		getSearchAppBeforeAdd:function(){
			$.ajax({
				type : "post",
				url : "/searchH/recommend/getAppBeforeAddRecommend",
				dataType : "json",
				success:function(res){
					if(res.success){
						$("#addAppCode").val("");
						var list = res.data.apps;
						var app = "<select name='tab_appCode' id='mySelect' onchange='recommend.getCommandDemo()'>";
						app += "<option value='0'>--请选择--</option>";
						for(var i=0; i<list.length;i++){
							var code = list[i].appCode;
							var name = list[i].appName;
							app += "<option value='"+ code+"'>"+name+"</option>"
						}
						app += "</select>";
						$("#addAppCode").append(app);
						
					}
				},
				error : function(res){
					alert("出错了呗");
				}
			});
		},
		
		getCommandDemo:function(){
			$("#addCommandCode").empty();
			var appcode = $('#mySelect option:selected') .val();
			if(appcode!='0'){
				$.ajax({
					type: "POST",
					url : "/searchH/recommend/getCommandBeforeAddRecommend",
					data: {"appcode":appcode},  
			        dataType:"json",  
			        async:false,  
			        cache:false,  
			        success: function(result){
			        	var list = result.data.commands;
			        	for(var i=0; i<list.length;i++){
							var code = list[i].commandCode;
							var name = list[i].commandName;
							var command = "<label><input name='tab_commandCode' type='radio' value='"+ code+"' />"+name+"</label> &nbsp;&nbsp;"
							$("#addCommandCode").append(command);
						}
			        },
			        error : function(res){
						alert("出错了>..");
					}
				});
			}
		},
		
		getCheckedCommandCode : function(){
//			var comm = "";
//			$("input:radio[name='tab_commandCode']:checked").each(function() {
//				comm += $(this).val() + ",";
//	        });
//			return comm.substr(0,comm.length-1);
			return $("input[name='tab_commandCode']:checked").val();
//			return $("input:radio[name='tab_commandCode']:checked").val();
		},
		
		getCommandOnChange : function(){
			$("#editCommandChecked").empty();
			var appcode = $('#editAppChange option:selected') .val();
			if(appcode!='0'){
				$.ajax({
					type: "POST",
					url : "/searchH/recommend/getCommandBeforeAddRecommend",
					data: {"appcode":appcode},  
			        dataType:"json",  
			        async:false,  
			        cache:false,  
			        success: function(result){
			        	var list = result.data.commands;
			        	for(var i=0; i<list.length;i++){
							var code = list[i].commandCode;
							var name = list[i].commandName;
							var command = "<label><input name='tab_commandCode' type='radio' value='"+ code+"' />"+name+"</label> &nbsp;&nbsp;"
							$("#editCommandChecked").append(command);
						}
			        },
			        error : function(res){
						alert("报错了");
					}
				});
			}
		}
		
};