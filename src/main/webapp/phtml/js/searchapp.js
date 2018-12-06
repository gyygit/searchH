var flag = true;
$(document).ready(function() {
	searchApp.query();
	funnames = 'searchApp.query()';
	// splitPage.pageSplitJz();
	// addBankCard.query();
});

var searchApp = {
	query : function() {
		var map = getUrlParams();
		var id = map.get("id");
		var method = map.get("method");
		// id为空，直接渲染模板;id不为空，查询后渲染模板
		var url = "/searchApp/beforeEditSearchApp.wap";
		var params = "backdata=data&callback=searchApp.queryCallback&id=" + id + "&method=" + method;
		params = QueryMain.setParams(params);
		QueryMain.loadData(url, params);
	},
	queryCallback : function(data) {
		// 渲染模板
		if(data.method === "edit"){
			jQuery("#content").setTemplateElement("app_template_edit", null, {
				filter_data : false
			}).processTemplate(data);
		} else {
			jQuery("#content").setTemplateElement("app_template", null, {
				filter_data : false
			}).processTemplate(data);
		}
	},
	save : function() {
		if(flag) {
			if (searchApp.validDate()) {
				flag = false;
				var url = "";
				// id为空：新增，不为空：修改。
				var inputHidden = $("input[name='tab_id']").val();
				if (inputHidden == "")
					url = "/searchApp/addSearchApp.wap";
				else
					url = "/searchApp/editSearchApp.wap";
				var params = "backdata=data&callback=searchApp.processData&IPS=" + $("#tab_IPS").html() + QueryMain.getJson();
				QueryMain.loadData(url, params); 
			}
		}
	},
	processData : function(data) {
		if (data.flag == "OK") {
			alert("保存成功");
			flag = true;
			location.href = "/searchH/phtml/searchapp/searchapplb.html";
		} else {
			alert(data.flag);
			flag = true;
		}
	},
	validDate : function() {
		var regstr = /^[0-9a-zA-Z]*$/g;
		var indexType = $("#tab_indexType").val();
		if ($("#tab_appName").val() == "") {
			alert("业务应用名称不能为空!");
			return false;
		} else if ($("#tab_appName").val().length > 50) {
			alert("业务应用名称仅允许输入50字符!");
			return false;
		}

		else if ($("#tab_appCode").val() == "") {
			alert("应用编码不能为空!");
			return false;
		} else if ($("#tab_appCode").val().length > 32) {
			alert("应用编码仅允许输入32字符!");
			return false;
		} else if (indexType == null && $("#tab_indexType").val() == "") {
			alert("请选择索引方式!");
			return false;
		} else if ($("#tab_indexPath").val() == "") {
			alert("索引路径不能为空!");
			return false;
		} else if ($("#tab_indexPath").val().length > 100) {
			alert("索引路径仅允许输入100字符!");
			return false;
			
		} else if ($("#tab_IPS").text() == "") {
      alert("IP规则不能为空!");
      return false;
      
    } 
		//IP规则长度验证
    if ($("#tab_IPS").text() != "") {
      var ips = $("#tab_IPS").text();
      if(ips.length>1000){
        alert("IP规则仅允许输入1000字符!");
        return false;
      }
    }
    //同步脚本长度验证
    if ($("#tab_shellPath").val() != "") {
      if($("#tab_shellPath").val().length>100){
        alert("同步脚本仅允许输入100字符!");
        return false;
      }
    }
		//如果是推送
		if(indexType == "1"){
			var index= "";
			$("#tab_loopCindexTime").val(index);
		}
		//如果不是推送
		if (indexType != "1" && indexType != "4" ) {
			if($("#tab_loopCindexTime").val() == ""){
				alert("定时重建索引不能为空!");
				return false;
			}
			if (indexType == "1" && isNaN($("#tab_loopCindexTime").val())) {
				alert("定时重建索引只能为数字!");
				return false;
			} else if ( !isNaN($("#tab_loopCindexTime").val())) {
				var type = "^[0-9]*[1-9][0-9]*$";
				var re = new RegExp(type);
				if ( $("#tab_loopCindexTime").val().match(re) == null) {
					alert("定时重建索引只能为正整数!");
					return false;
				}
			}
		}
		return true;
	}
};