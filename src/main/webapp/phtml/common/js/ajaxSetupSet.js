$.ajaxSetup({      
    contentType:"application/x-www-form-urlencoded;charset=utf-8",      
    complete:function(XMLHttpRequest,textStatus){   
    	// 通过XMLHttpRequest取得响应头，sessionstatus，
        var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus"); 
        var loginUrl=XMLHttpRequest.getResponseHeader("loginUrl"); 
        
        if(sessionstatus=="sessionOut"){ 
        	if('undefined'!=typeof(AppFunLoader)){
				return AppFunLoader.goLogin();
        	};
            window.location.replace(loginUrl);      
        }   
    }   
}); 