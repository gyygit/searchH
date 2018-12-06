var flag = true;
$(document).ready(function() {
	synonymWord.query();
	funnames = 'synonymWord.query()';
});

var synonymWord = {
		query:function(){
			var map = getUrlParams();
			var id = map.get("id");
			//id为空：新增，不为空：修改。
			var url = "/synonymWord/beforeEditSynonymWord.wap";
			var params="backdata=data&callback=synonymWord.queryCallback&id="+id;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		
		queryCallback:function(data){
			//渲染模板
			jQuery("#content").setTemplateElement("synonym_template",null,{filter_data: false}).processTemplate(data);
			if(data.m.id==""){
				synonymWord.getSearchAppBeforeAdd();
			}
		},
		
		save:function(){
			if(flag) {
				if(synonymWord.validDate1()) {
					var commCode = synonymWord.getCheckedCommandCode();
					if(commCode==undefined || commCode ==null ||commCode ==""){
						alert("业务数据分类名称不能为空!");
					}else{
						if(synonymWord.validDate2()) {
							flag = false;
							var url = "";
							//id为空：新增，不为空：修改。
							var inputId = $("#tab_id").val();
							if(inputId == ""){
								url = "/synonymWord/addSynonymWord.wap";
							}else{
								url = "/synonymWord/editSynonymWord.wap";
							}	
							var params="backdata=data&callback=synonymWord.processData&synonymArray="+$("#tab_synonymArray").text()+QueryMain.getJson()+"&commandCode="+commCode;
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
				location.href="/searchH/phtml/searchapp/synonymwordmanage.html";
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
			if ($("#tab_keyWord").val() == "") {
			    alert("关键词不能为空!");
			    return false;
			} else if ($("#tab_keyWord").val().length > 32) {
			    alert("关键词仅允许输入32字符!");
			    return false;
			} else if ($("#tab_synonymArray").text() == "") {
			    alert("同义词组不能为空!");
			    return false;
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
						var app = "<select name='tab_appCode' id='mySelect' onchange='synonymWord.getCommandDemo()'>";
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