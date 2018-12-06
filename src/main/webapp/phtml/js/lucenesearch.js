var i = 2;
$(document).ready(function() {
	$("#pageNo").val("1");
	luceneSearch.query();
	funnames = 'luceneSearch.query()';
	//splitPage.pageSplitJz();
		//addBankCard.query();
});

var luceneSearch = {
	query : function() {
		var map = getUrlParams();
		var commandId = map.get("commandId");
		var app_id = map.get("app_id");
		var url = "/luceneSearch/search.wap";
		var params = "backdata=data&callback=luceneSearch.queryCallback&commandId=" + commandId+"&app_id=" + app_id+ QueryMain.getJson();
		params = QueryMain.setParams(params);
		QueryMain.loadData(url, params);
	},
	queryCallback : function(data) {
		//渲染模板
		QueryMain.processTemplateDataSplit("#result", "#nextPage", "templateContainer", data);
		initPagination(data.limit.pageNum, data.limit.totalNum, data.limit.rowDisplayed);
	},
	addRow : function(method,obj) {
		if(method == "add"){ 
			R = document.getElementById('searchTable').insertRow(i);
			C = R.insertCell();
			C.innerHTML = "<div name='index"+i+"' style='text-align: center;' id='index"+i+"'></div>";
			C = R.insertCell();
			C.innerHTML = " <div style='text-align: center;'><input type='text' id='tab_querys' name='tab_querys'/></div>";
			C = R.insertCell();
			C.innerHTML = "<div style='text-align: center;'><select name='tab_conditions' id='tab_conditions'><option value='1'>等于</option><option value='2'>LIKE</option><option value='3'>大于</option><option value='4'>小于</option></select></div>";
			C = R.insertCell();
			C.innerHTML = "<div style='text-align: center;'><select name='tab_logic' id='tab_logic'><option value='1'>AND</option><option value='2'>OR</option></select></div>";
			C = R.insertCell();
			C.innerHTML = "<img id='addBtn' name='addBtn' src='../common/images/add.jpg' style='cursor: pointer;' onclick=\"luceneSearch.addRow('add')\" class='ml10' /><img id='delBtn' name='delBtn' src='../common/images/remove.jpg' style='cursor: pointer;'	 onclick=\"luceneSearch.addRow('del',this)\" class='ml10' />";
			$('#index'+i).html($("#indexDemo").html());
			i ++;
		}else{
			 searchTable.deleteRow(obj.parentElement.parentElement.rowIndex);
			 i--;
		}
	}
};