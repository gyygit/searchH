$(document).ready(function() {
	$("#pageNo").val("1");
	clickVisitLogList.query();
	funnames= 'clickVisitLogList.query()';
});

var clickVisitLogList = {
		query:function(){
			var url = "/searchClickVisitLog/queryClickVisitLog.wap";
			var params="backdata=data&callback=clickVisitLogList.queryCallback"+QueryMain.getJson();
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			var name = data.username;
			if(name == null || name == 'null' || name == ''){
				location.href="/searchH/";
			}else{
				$("#chartArea1").show();
				$("#chartArea2").show();
				$("#bdEchart").show();
				//渲染模板
				QueryMain.processTemplateDataSplit("#result", "#nextPage", "templateContainer", data);
				initPagination(data.limit.pageNum,data.limit.totalNum,data.limit.rowDisplayed);
				if(data.limit.pageNum==1){
					if(data.groupJson1=="[]"){
						$("#bdEchart").hide();
					}
					eCharts1(eval(data.groupJson1));
					eCharts2(eval(data.groupJson2));
				}
				$("#user").text(data.username);
				$("#chartArea2").hide();
				$("#tab1").css("backgroundColor","#FF7F50");
				$("#tab2").css("backgroundColor","");
			}
		},
		
		exit:function(id){
			$.ajax({
				type:"post",
				url : "/searchH/user/exit",
				dataType : "json",
				success:function(res){
					if(res.success){
						location.href="/searchH/";
					}
				}
			});
		},
		changeTab1:function(){
			$("#chartArea1").show();
			$("#chartArea2").hide();
			$("#tab1").css("backgroundColor","#FF7F50");
			$("#tab2").css("backgroundColor","");
		},
		changeTab2:function(){
			$("#chartArea1").hide();
			$("#chartArea2").show();
			$("#tab1").css("backgroundColor","");
			$("#tab2").css("backgroundColor","#FF7F50");
	    }
}

function eCharts1(data){
	var myChart = echarts.init(document.getElementById('chartArea1'));
	var json = data;
	var agg = [];
	var num = [];
	for(var i in json){
		agg.push(json[i].groupKey);
		num.push(json[i].groupNum);
	}
	console.log(agg);
	console.log(num);
	var groupK = agg;
	var groupN = num;
	var option = {
			 tooltip: {
	             trigger: 'axis',
	             axisPointer: { // 坐标轴指示器，坐标轴触发有效
	                 type: 'line' // 默认为直线，可选为：'line' | 'shadow'
	             }
	         },
	         legend: {
	             orient: 'horizontal',
	             x: 'center',
	             data: ['访问量'],
	             textStyle: {
	                  fontSize: '14'
	             }
	         },
	         toolbox: {
	             show: true,
	             orient: 'vertical',
	             x: 'right',
	             y: 'center'

	         },
	         calculable: true,
	         xAxis: [{
	             type: 'category',
	             data: groupK
	         }],
	         yAxis: [{
	             type: 'value'
	         }],
	        series: [{
	            name: '访问量',
	            type: 'bar',
	            data: groupN
	        	}
	        ]
	};
	myChart.setOption(option);
}

function eCharts2(data){
	var myChart = echarts.init(document.getElementById('chartArea2'));
	var json = data;
	var agg = [];
	var num = [];
	for(var i in json){
		agg.push(json[i].groupKey);
		num.push(json[i].groupNum);
	}
	console.log(agg);
	console.log(num);
	var groupK = agg;
	var groupN = num;
	var option = {
			tooltip: {
				trigger: 'axis',
				axisPointer: { // 坐标轴指示器，坐标轴触发有效
					type: 'line' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend: {
				orient: 'horizontal',
				x: 'center',
				data: ['搜索量'],
				textStyle: {
					fontSize: '14'
				}
			},
			toolbox: {
				show: true,
				orient: 'vertical',
				x: 'right',
				y: 'center'
					
			},
			calculable: true,
			xAxis: [{
				type: 'category',
				data: groupK
			}],
			yAxis: [{
				type: 'value'
			}],
			series: [{
				name: '搜索量',
				type: 'bar',
				data: groupN
			}
			]
	};
	myChart.setOption(option);
}