var flag = true;
$(document).ready(function() {
	
	dictionaryindex.query();
	funnames= 'dictionaryindex.query()';
	
	//splitPage.pageSplitJz();
	//addBankCard.query();
});
var dictionaryindex = {
		bindingClick : function(){
			$("#tab_TYC").click(function() {
				if($("#tab_TYC").attr('checked') == "checked") {
					$("#tab_MGC").attr("checked",false);
//					$("#nameAndCodeDiv").css("display", "none");
					$("#tab_indexcode").val("TYC");
					$("#tab_indexname").val("同义词");
					$("#tab_indexname").attr("readonly","readonly")
					$("#tab_indexcode").attr("readonly","readonly")
				}else {
					$("#tab_indexcode").val("");
					$("#tab_indexcode").removeAttr("readonly");
					$("#tab_indexname").val("");
					$("#tab_indexname").removeAttr("readonly");
//					$("#nameAndCodeDiv").css("display", "block");
				}
			}),
			$("#tab_MGC").click(function() {
				if($("#tab_MGC").attr('checked') == "checked") {
					$("#tab_TYC").attr("checked",false);
//					$("#nameAndCodeDiv").css("display", "none");
					$("#tab_indexcode").val("MGC");
					$("#tab_indexname").val("敏感词");
					$("#tab_indexname").attr("readonly","readonly")
					$("#tab_indexcode").attr("readonly","readonly")
				}else {
					$("#tab_indexcode").val("");
					$("#tab_indexcode").removeAttr("readonly");
					$("#tab_indexname").val("");
					$("#tab_indexname").removeAttr("readonly");
//					$("#nameAndCodeDiv").css("display", "block");
				}
			})
		},
		query:function(){
			var map = getUrlParams();
			var id = map.get("id");
			//id为空，直接渲染模板;id不为空，查询后渲染模板
				var url = "/dictionaryindex/beforeEditDictionaryindex.wap";
				var params="backdata=data&callback=dictionaryindex.queryCallback&id="+id;
				params = QueryMain.setParams(params);
				QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			//渲染模板
			jQuery("#content").setTemplateElement("dictionaryindex_template",null,{filter_data: false}).processTemplate(data);
			dictionaryindex.bindingClick();
		},
		save:function(){
			if(flag) {
				if(dictionaryindex.validDate()){
					flag = false;
					var url = "";
					//id为空：新增，不为空：修改。
					var inputHidden = $("input[name='tab_id']").val();
					if(inputHidden == "")
						url = "/dictionaryindex/addDictionaryindex.wap";
					else	
						url = "/dictionaryindex/editDictionaryindex.wap";
					var params="backdata=data&callback=dictionaryindex.processData"+QueryMain.getJson();
					QueryMain.loadData(url,params);
				}
			}
		},
		processData:function(data){
			if(data.flag=="OK"){
				alert("保存成功");
				flag = true;
				location.href="/searchH/phtml/searchapp/dictionaryindexlb.html";
				//location.href="/searchH/phtml/searchapp/dictionaryindexlb.html?id="+data.Id;
			}
			else{
				alert(data.flag);
				flag = true;
			}
		},
		validDate:function(){
			
			if($("#tab_indexcode").val() == ""){
				alert("数据字典主表code不能为空!");
				return false;
			}else if($("#tab_indexcode").val().length >=80){
				alert("数据字典主表code仅允许输入80字符!")
				return false;
			}
			else if($("#tab_indexname") == null && $("#tab_indexname").val() == ""){
				alert("数据字典主表名称不能为空!");
				return false;
			}else if($("#tab_indexname").val().length >80){
				alert("数据字典主表名称仅允许输入80字符!")
				return false;
			}else if($("#tab_description").val().length >200){
				alert("数据字典主表描述仅允许输入200字符!")
				return false;
			}
			return true;
		}
};