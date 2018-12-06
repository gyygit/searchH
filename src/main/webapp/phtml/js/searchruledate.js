var flag = true;
$(document).ready(function() {
	searchRuleDate.query();
	funnames= 'searchRuleDate.query()';
	//splitPage.pageSplitJz();
	//addBankCard.query();
});

var searchRuleDate = {
		query:function(){
			var map = getUrlParams();
			var id = map.get("id");
			var appId= map.get("appId");
			//id为空，直接渲染模板;id不为空，查询后渲染模板
			var url = "/searchRuleDate/beforeEditSearchRuleDate.wap";
			var params="backdata=data&callback=searchRuleDate.queryCallback&id="+id+"&appId="+appId;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
			//渲染模板
//				jQuery("#content").setTemplateElement("app_template",null,{filter_data: false}).processTemplate(null);
		},
		queryCallback:function(data){
			//渲染模板
			jQuery("#content").setTemplateElement("app_template",null,{filter_data: false}).processTemplate(data);
		},
		save:function(){
			if(flag) {
				if(searchRuleDate.validDate()){
					flag = false;
					var url = "";
					//id为空：新增，不为空：修改。
					var inputHidden = $("input[name='tab_id']").val();
					if(inputHidden == "")
						url = "/searchRuleDate/addSearchRuleDate.wap";
					else
						url = "/searchRuleDate/editSearchRuleDate.wap";
					var params="backdata=data&callback=searchRuleDate.processData"+QueryMain.getJson();
					QueryMain.loadData(url,params);
				}
			}
		},
		processData:function(data){
			if(data.flag=="OK"){
				alert("保存成功");
				flag = true;
				location.href="/searchH/phtml/searchapp/searchruledatelb.html?app_id="+data.appId;
			}
			else{
				alert(data.flag);
				flag = true;
			}
		},
		validDate:function(){
			var fileldDateType = $('input:radio[name="tab_fileldDateType"]:checked').val();
			var fieldIndexType = $('input:radio[name="tab_fieldIndexType"]:checked').val();
			var fieldStoreType = $('input:radio[name="tab_fieldStoreType"]:checked').val();
			if($("#tab_appCode").val() == ""){
				alert("应用编码不能为空!");
				return false;
			}
			else if($("#tab_fieldName").val() == ""){
				alert("字段名称不能为空!");
				return false;
			}
			else if(fileldDateType == null){
				alert("请选择是否为主键!");
				return false;
			
			}
			else if(fieldIndexType == null && fileldDateType != '4'){
				alert("请选择字段索引类型!");
				return false;
			}
			else if(fieldStoreType == null && fileldDateType != '4'){
				alert("请选择字段值是否存储!");
				return false;	
			}
			
			//长度验证
			if($("#tab_fieldName").val() != ""){
			  if($("#tab_fieldName").val().length > 50){
			    alert("字段名称仅允许输入50字符");
			    return false;
			  }
			}
			return true;
		}
};