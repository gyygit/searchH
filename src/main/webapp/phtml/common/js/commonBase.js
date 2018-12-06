var _v = "?_=" + (new Date()).getTime();
var expChar=[',',';','<','>','\'','*','script','?','&','/','\\'];
// $.cookie('CHANGE_AREA','059');
var hiddenstr;
var QueryMain = {
	/**
	 * Get context path
	 */
        getCxtPath : function() {
    		return '/' + window.location.pathname.split('/')[1];
        },
	cleanSearch:function(){
		var inputArray=$("input[name^='tab_'][type='text']");
		inputArray.each(//使用数组的循环函数 循环这个input数组
		    function (){
		       $(this).val('');//循环中的每一个input元素
		     }
		)
		var selectArray = $("select[name^='tab_']");
		selectArray.each(
			function(){
				$(this).val('');
			}
		);
	},
	//删除
	btn_delete_click:function(funcName, ids, deleteName){
		if(ids == "" || ids == null){
			ids= QueryMain.getChecks("items").join(",");	
			if(ids == "" || ids == null){
				alert("请选择一条或多条记录!");
				return false;
			}else{
				if(confirm("确认要批量删除选择项吗？")){
					var url = funcName;
					var params="backdata=data&callback=QueryMain.deleteCallback&ids="+ids;
					params = QueryMain.setParams(params);
					QueryMain.loadData(url,params);
					return;
				}
			}
		}else if(confirm("确定要删除'"+deleteName+"'吗？")){
			var url = funcName;
			var params="backdata=data&callback=QueryMain.deleteCallback&ids="+ids;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		}
	},
	divShowOrClose:function (divId,status){
		$("#"+divId).css("display",status);
	},
	deleteCallback:function (){
		if(data.flag == "OK"){
			alert("删除成功!");
			window.location.reload();//刷新当前页
		}else{
			alert(data.flag);
			window.location.reload();//刷新当前页
		}
	},
	getChecks:function (name){
		var re = new Array();
		var ck = document.getElementsByName(name);
		for(var i=0; i<ck.length; i++)
		{
			if(ck[i].checked)
			{
				re.push(ck[i].value);
			}
		}
		return re;
	},
	//全选
   	checkAll:function() {
    	var booleanCheck = "";
		if($(".checkedAll").attr('checked')=='checked'){
			booleanCheck = true;
		}else{
			booleanCheck = false;
		}
		var itemsArray = $("input[type=checkbox][name=items]");
		itemsArray.each(
			function(){
				$(this).attr("checked",booleanCheck);
			}
		)
     },

     checkPart : function() {
		if (!$(this).is(':checked')) {
			$(".checkedAll").attr('checked', false);
		}
	},
    replace_all:function(str,olds,news)
    {
    	  var reg = new RegExp(olds,"g");
    	  return  str.replace(reg,news);
    },
	// 封装数据
	getJson:function(){
		var returnJson = "";
		var inputArray=$("input[name^='tab_'][type='text']");
		inputArray.each(//使用数组的循环函数 循环这个input数组
		    function (){
		        var input =$(this);//循环中的每一个input元素
		        var attrName = input.attr("name");
	        	returnJson += "&"+ attrName.substring(attrName.indexOf("_") + 1) + "=" + $.trim(QueryMain.replace_all($(this).val(),"&","%26"));
		     }
		)
		var passArray=$("input[name^='tab_'][type='password']");
		passArray.each(//使用数组的循环函数 循环这个input数组
		    function (){
		        var input =$(this);//循环中的每一个input元素
		        var attrName = input.attr("name");
	        	returnJson += "&"+ attrName.substring(attrName.indexOf("_") + 1) + "=" + $.trim($(this).val());
		     }
		)
		//获得单选按钮的值 
		var itemArray = $("input[type='radio']:checked");
		itemArray.each(
			function(){
				returnJson += "&" + $(this).attr("name").substring($(this).attr("name").indexOf("_") + 1) + "=" + $(this).val();
			}
		)
		//隐藏域
		var hiddenArray = $("input[name^='tab_'][type='hidden']");
		hiddenArray.each(
			function(){
				returnJson += "&" + $(this).attr("name").substring($(this).attr("name").indexOf("_") + 1) + "=" + $.trim(QueryMain.replace_all($(this).val(),"&","%26"));
			}	
		)
		//下拉选框
		var selectArray = $("select[name^='tab_']");
		selectArray.each(
			function(){
				returnJson +=  "&" + $(this).attr("name").substring($(this).attr("name").indexOf("_") + 1) + "=" + $(this).val();
			}
		);
		//多选待添加
		var selectBoxArray = $("checkbox[name^='tab_']");
		selectBoxArray.each(
			function(){
				returnJson +=  "&" + $(this).arrt("name").substring($(this).attr("name").indexOf("_") +1) + "=" + $(this).val();
				
			}
		);
		//console.log(returnJson);
		return returnJson;
	},
	printBody : function() {
		window.print();
	},
	loadModal : function() {
		// jQuery("<section id='loading' class='dn'><img
		// src='/images/loading.png' width='25' class='loadingImg
		// loadingImg_going'><p
		// class='space-05'></p><p>玩命加载中...</p></section>").modal({
		// escClose:true,
		// close:true,
		// zIndex:"9999999"
		// });
		$('.dragMore').css("display", 'block');
		$('.loadingImg').addClass('loadingImg_going');
		
	},
	closeModal : function() {
		$('.dragMore').css("display", 'none');
		$('.loadingImg').removeClass('loadingImg_going');

	},
	loadData : function(argUrl, paramdata) { // 异步加载
		radomT = "?v=" + (new Date()).getTime();
		var url = "/searchH" + argUrl + radomT;
		jQuery.ajax({
					url : url,
					type : "POST",
					async : true,
					dataType : "script",
					data : paramdata,
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					beforeSend : function() {
						QueryMain.loadModal();
					}
				});
		// 10秒后自动隐藏loading图片
		 setTimeout("QueryMain.closeModal()", 10000);
	},
	loadDataM : function(argUrl, paramdata) {
		radomT = "?v=" + (new Date()).getTime();
		var url = "/searchH" + argUrl + radomT;
		jQuery.ajax({
					url : url,
					type : "POST",
					async : false,
					dataType : "script",
					data : paramdata,
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					beforeSend : function() {
						QueryMain.loadModal();
					}
				});
		// 10秒后自动隐藏loading图片
		 setTimeout("QueryMain.closeModal()", 10000);
	},
	loadDataD : function(argUrl, paramdata,disabledObj,typ) { // 异步加载
		radomT = "?v=" + (new Date()).getTime();
		var url = "/searchH" + argUrl + radomT;
		var href;
		jQuery.ajax({
					url : url,
					type : "POST",
					async : false,
					dataType : "script",
					data : paramdata,
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					beforeSend : function() {
						if(typ=="zt"){
							href = $(obj).attr("href");
							$(obj).attr("href","#");
						}else{
							$(disabledObj).attr('disabled',"disabled");
						}
						QueryMain.loadModal();
					},
					complete : function(){
						if(typ=="zt"){
							setTimeout(function (){
								$(obj).attr("href",href);
							}, 30000);
						}else{
							setTimeout(function (){
								$(disabledObj).removeAttr('disabled');
							}, 30000);
						}
						
					}
				});
		// 10秒后自动隐藏loading图片
		 setTimeout("QueryMain.closeModal()", 10000);
	},
	loadDataP : function(argUrl, paramdata) {
		radomT = "?v=" + (new Date()).getTime();
		var url = "/searchH" + argUrl + radomT;
		jQuery.ajax({
					url : url,
					type : "POST",
					async : true,
					dataType : "script",
					data : paramdata,
					contentType : "application/x-www-form-urlencoded;charset=UTF-8"
				});
	},
	showLoginModal : function() {
		$('#l_').show();
		$('.alpha').show();
		$('.rejectionClose').click(function() {
					$('.alpha').hide();
					$('#l_').hide();
					window.location = "http://wcf.10010.com/wcf/";
				});
	},
	// 处理数据，加载jstemplate模板
	processTemplateData : function(show, templateId, data) {
		jQuery(show).setTemplateElement(templateId).processTemplate(data);
		QueryMain.closeModal();
	},
	// 处理分页显示
	processPageTag : function(totalSize, currentPage, pageSize) {
		var totalPage = (totalSize % pageSize == 0
				? totalSize / pageSize
				: Math.round(totalSize / pageSize));
		if (currentPage > totalPage) {
			currentPage = totalPage;
		}
		var last = currentPage - 1;
		var next = currentPage + 1;
		if (last < 1) {
			last = 1;
		}
		if (next > totalPage) {
			next = totalPage;
		}
		var start = pageSize * (currentPage - 1);
		var end = currentPage * pageSize;
		var pageInfo = {
			'totalSize' : totalSize,
			'totalPage' : totalPage,
			'currentPage' : currentPage,
			'pageSize' : pageSize,
			'last' : last,
			'next' : next,
			'start' : start,
			'end' : end
		};
		return pageInfo;
	},
	// 处理数据，分页显示数据
	processPageQuery : function(show, templateId, data, currentPage, pageSize) {
		var start = pageSize * (currentPage - 1);
		var end = currentPage * pageSize;
		var subjson = QueryMain.sliceData(data, start, end);
		QueryMain.processTemplateData(show, templateId, subjson);
	},
	goPage : function(show, templateId, totalSize, pageinput, pageSize) {
		var gopage = jQuery(pageinput).val();
		QueryMain.processPageTag(show, templateId, totalSize, gopage, pageSize);
	},
	exceptionBack : function(data) {
		alert(data.msg);
	},
	getHTML : function(id) {
		html = jQuery("#" + id).html();
		return html;
	},
	// 获取cookie
	getCookie : function(objName) {// 获取指定名称的cookie的值
		var arrStr = document.cookie.split(";");
		var o = "";
		for (var i = 0; i < arrStr.length; i++) {
			var temp = arrStr[i].split("=");
			if (jQuery.trim(temp[0]) == jQuery.trim(objName)) {
				o = unescape(jQuery.trim(temp[1]));
				break;
			}
		}
		return o;
	},
	processTemplateDataSplit : function(show, show_hidden, templateId, data) {
		if (data.limit != null) {
			if(data.limit.pageNum==1){
				$(show).html("");
			}
			if (data.limit.pageNum <= data.limit.totalPage) {// 超过页数就不进行加载了
//				jQuery(show_hidden).setTemplateElement(templateId)
//						.processTemplate(data);
				jQuery(show).setTemplateElement(templateId)
						.processTemplate(data);
//				hiddenstr = $(show_hidden).html();
//				$(show).append(hiddenstr);
				QueryMain.setPageNo(data.limit.pageNum + 1);
				pplock = true;
				if (data.limit.pageNum == data.limit.totalPage) {// 若是最后一页,则不会请求数据
					pplock = false;
					QueryMain.closeModal();
				}
			} else {
				pplock = false;
				QueryMain.closeModal();
				jQuery(show).setTemplateElement(templateId)
						.processTemplate(data);
			}
		} else {
			QueryMain.closeModal();
			jQuery(show).setTemplateElement(templateId).processTemplate(data);

		}

	},
	setPageNo : function(pageNo) {
		$("#pageNo").val(pageNo);
	},
	setParams : function(params) {
		var pageNo = $("#pageNo").val();
		params = params + "&pageNo=" + pageNo;
		return params;
	}
};

function subStringName(name, len) {
	var objN = getNameLength(name, len);
	return objN.pos == 0 ? name : name.substring(0, objN.pos);
}
// 获取名字占位长度-中文2西文1

function getNameLength(str, max) {
	var result = {
		"len" : 0,
		"pos" : 0
	};
	for (var i = 0; i < str.length; i++) {
		if (str.charCodeAt(i) < 0 || str.charCodeAt(i) > 128) {
			result.len += 1;
		}
		result.len += 1;
		if (max != 0 && result.len > max) {
			result.pos = i;
			return result;
		}
	}
	return result;
}
function getUrlParams() {
	var searchStr = location.search;
	searchStr = searchStr.substr(1);
	var searchs = searchStr.split("&");
	var map = getMap();

	for (var i = 0; i < searchs.length; i++) {
		var search = searchs[i].split("=");
		map.put(search[0], search[1]);
	}
	return map;
}

// 定义简单Map
function getMap() {// 初始化map_,给map_对象增加方法，使map_像Map
	var map_ = new Object();
	map_.put = function(key, value) {
		map_[key + '_'] = value;
	};
	map_.get = function(key) {
		return map_[key + '_'];
	};
	map_.remove = function(key) {
		delete map_[key + '_'];
	};
	map_.keyset = function() {
		var ret = "";
		for (var p in map_) {
			if (typeof p == 'string' && p.substring(p.length - 1) == "_") {
				ret += ",";
				ret += p.substring(0, p.length - 1);
			}
		}
		if (ret == "") {
			return ret.split(",");
		} else {
			return ret.substring(1).split(",");
		}
	};
	return map_;
}

$(function(){
	$('input[type="text"]').keyup(function(){
		var content = jQuery(this).val();
		$.each(expChar,function(i,char){
			content = content.replace(char,'');
		});
		jQuery(this).val(content);
	});
	//如果接入bms 请注释以下代码
	$("#leftDiv").load("left.html");
    $("#leftDiv2").load("../left.html");
});




