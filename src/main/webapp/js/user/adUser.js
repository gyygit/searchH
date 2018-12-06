var adUser = {
  //列表
  index:function () {
	$("#container").load("list");
  },
  //加载数据，转入添加或编辑页面
  beforeAddOrEdit:function(id){
    if (id == null || "" ==id) {
        id = "";
	} else {
		id = "?id=" + id;
	}
	$('#container').load("beforeAddOrEdit" + id);
  },
  //增加新子模板
  addChild:function(){
    //获取子模板
     $.post("getTemplates", {}, 
		    function(responseText) {
		      var objs = eval(responseText);
		      getChild(objs);
		});
  },
  save:function(){
     $('#inputForm').ajaxSubmit({
					success : function(responseText) {
						var re = eval(responseText);
						if (re.status == "200") {
							Main.msg(re.message);
							adUser.index();
						} else {
							Main.error(re.message);
						}
					}
				});
  },
  del:function(id){
      if (!confirm("确认要删除该数据吗?"))
			return false;
	  $.post("del", {id:id}, 
		    function(responseText) {
		      var re = eval(responseText);
		      if (re.status == "200") {
					Main.msg(re.message);
					adUser.index();
			  } else {
					Main.error(re.message);
			  }
		});
  },
  deleteMost : function() {
		var id=adUser.getMostSelected();
		if(id != ""){
		   adUser.del(id);
		}
  },
  getMostSelected : function() {
		var ids = "";
		$("input[name='ckids']:checked").each(function() {
				if (ids == '') {
				ids = $(this).val();
			} else {
				ids += "," + $(this).val();
			}
		});
		if (ids == "") {
			alert("请选择至少一条记录");
		}
	    return ids;
  },
  view:function(id){
     if (id == null || "" ==id) {
           id = "";
		} else {
			id = "?id=" + id;
		}
		$('#container').load("view"+id);
  },
  /**
  *查看模板
  *id--模板的id号
  */
  viewTemplate : function(id){
     var placeId=$("#placeId").val();
	 var t = "?templateid=" + id+"&placeId="+placeId;
	 $('#container').load("viewTemplate"+t);
  },
  /**
  *查看接入渠道
  *id--接入渠道的id号
  */
  viewSys : function(id){
     var placeId=$("#placeId").val();
	 var t = "?sysid=" + id+"&placeId="+placeId;
	 $('#container').load("viewSys"+t);
  }
};
$(document).ready(function () {
	adUser.index();
});
