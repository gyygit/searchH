var flag = true;
$(document).ready(function() {
	dictionary.query();
	funnames= 'dictionary.query()';
	//splitPage.pageSplitJz();
	//addBankCard.query();
});

var dictionary = {
		query:function(){
			var map = getUrlParams();
			var id = map.get("id");
			var indexId = map.get("indexId");
			//id为空，直接渲染模板;id不为空，查询后渲染模板
			var url = "/dictionary/beforeEditDictionary.wap";
			var params="backdata=data&callback=dictionary.queryCallback&id="+id+"&indexId="+indexId;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			//渲染模板
			jQuery("#content").setTemplateElement("dictionary_template",null,{filter_data: false}).processTemplate(data);
		},
		save:function(){
			if(flag) {
				if(dictionary.validDate()) {
					flag = false;
					var url = "";
					//id为空：新增，不为空：修改。
					var inputHidden = $("input[name='tab_id']").val();
					if(inputHidden == "")
						url = "/dictionary/addDictionary.wap";
					else	
						url = "/dictionary/editDictionary.wap";
					var params="backdata=data&callback=dictionary.processData"+QueryMain.getJson() +"&value=" + $("#tab_value").text();
					
					QueryMain.loadData(url,params);
				}
			}
		},
		processData:function(data){
	
			if(data.flag=="OK"){
				alert("保存成功");
				flag = true;
				location.href="/searchH/phtml/searchapp/dictionarylb.html?index_id="+data.indexId;
			}
			else{
				alert(data.flag);
				flag = true;
			}
		},
		validDate:function(){
		
			if($("#tab_code").val() == null || $("#tab_code").val() == ""){
				alert("code必填项不能为空!");
				return false;
			}else if($("#tab_code").val().length >80){
				alert("子表code仅允许输入80字符!")
				return false;
			}
			
			else if($("#tab_name").val() == null || $("#tab_name").val() == ""){
				alert("名称不能为空!");
				return false;
			}else if($("#tab_name").val().length >80){
				alert("名称仅允许输入80字符!")
				return false;
			}
			else if( $("#tab_value").text() ==""){
				alert("值不能为空!");
				return false;
			} else if($("#tab_value").val().length >500){
				alert("值仅允许输入500字符!")
				return false;
			}else if($("#tab_description").val().length >100){
				alert("描述仅允许输入100字符!")
				return false;
			}else if($("#tab_dictionaryImgUrl").val().length >200){
				alert("图片地址仅允许输入200字符!")
				return false;
			}
			
			
			return true;
		}
};