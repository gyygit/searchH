function initPagination(pageNumt, totalNumt, rowDisplayedt) {
	var num_entries = totalNumt;
	$("#Pagination").pagination(num_entries, {
		callback : pageselectCallback,
		items_per_page : rowDisplayedt,
		current_page : pageNumt - 1,
		num_display_entries : 2,//每行 固定显示几行 ....XXXXX....
		num_edge_entries : 2,//开头结尾 固定显示几个标签
		prev_text : "上一页",
		next_text : "下一页"
	});
}
function pageselectCallback(page_index, jq) {
	document.getElementById("pageNo").value = page_index + 1;
	//searchAppList.query();   
	eval(funnames);
	return true;
}
