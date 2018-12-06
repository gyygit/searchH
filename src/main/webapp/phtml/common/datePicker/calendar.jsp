
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/js/datePicker/WdatePicker.js"></script>

<script type="text/javascript">
		function WdatePickerSsSetup(ctrId){  
			WdatePicker({el:''+ctrId,skin:'default',dateFmt:'yyyy-MM-dd'});
			return false;
		}
		function WdatePickerSsSetup(ctrId,fmt){  
			WdatePicker({el:''+ctrId,skin:'default',dateFmt:''+fmt});
			return false;
		}
		
		function WdatePickerMinSetup(ctrId,beforeId){
			var min = document.getElementById(beforeId).value;
			if(min == ""){
				alert("\u8bf7\u9009\u62e9\u521b\u5efa\u65f6\u95f4");
				return;
			}
			WdatePicker({el:''+ctrId,skin:'default',minDate:min});
			return false;
		}
		
		function WdatePickerMinFmtSetup(ctrId,beforeId,fmt){
			var min = document.getElementById(beforeId).value;
			if(min == ""){
				alert("\u8bf7\u9009\u62e9\u521b\u5efa\u65f6\u95f4");
				return;
			}
			WdatePicker({el:''+ctrId,skin:'default',minDate:min,dateFmt:''+fmt});
			return false;
		}
		
		function WdatePickerChangeSetup(ctrId,beforeId){
			var min = document.getElementById(beforeId).value;
			if(min == ""){
				alert("\u8bf7\u9009\u62e9\u521b\u5efa\u65f6\u95f4");
				return;
			}
			WdatePicker({el:''+ctrId,skin:'default',minDate:min,onpicking:function(dp){if(!confirm('\u539f\u6765\u4e0a\u7ebf\u65f6\u95f4\u4e3a: '+dp.cal.getDateStr()+', \u8981\u7528\u65b0\u4e0a\u7ebf\u65f6\u95f4:' + dp.cal.getNewDateStr() + '\u8986\u76d6\u5417?')) return true;}});
		}
		
</script>