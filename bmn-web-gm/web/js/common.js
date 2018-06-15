$.ajaxSetup({
    cache : false,
    global : true,
    dataFilter: function(data) {
        // 如果后台返回的是login字符串则可以跳转到登录页面
        if(data && data == 'login'){
 		   var isOk=confirm("无操作权限，如有疑问请联系平台管理员；是否返回到登录页？");
		   if(isOk){
	            parent.window.location.href = 'login';
	            return;
		   }
		   return;
        }
    	return data;
    }
   /* ,
    complete: function(req, status) {
        var reqText = req.responseText;
        // 如果后台返回的是login字符串则可以跳转到登录页面
        if(reqText && reqText == 'login'){
 		   var isOk=confirm("无操作权限，如有疑问请联系平台管理员；是否返回到登录页？");
		   if(isOk){
	            parent.window.location.href = 'login';
	            return;
		   }
		   return false;
        }
    }*/
});

if($.fn.datagrid) {
	$.extend($.fn.datagrid.methods, {
			getRow: function(jq, param){
					var id = param.id;
					var index = param.index;
					var rows = $('#' + id).datagrid('getRows');
					for(i in rows) {
						var _index = $('#' + id).datagrid('getRowIndex', rows[i]);
						if(_index == index) {
							return rows[i];
						}
					}
					return null;
				}
	});
}

