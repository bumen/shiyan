<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/demo/demo.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.json.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/plugins/jquery.tabs.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js" type="text/javascript" charset="utf-8"></script>
<style type="text/css">
	.myhovr:hover {
		cursor:pointer;
	}
</style>
<script type="text/javascript">
function submitForm(){
	var logId = $('#logId').textbox("getValue");
	var roleId = $('#roleId').textbox("getValue");
	
	$.ajax({
		type:"POST",
		url:"tester/log",
		data:{
			'logId': logId,
			'roleId': roleId
		},
		success:function(code){
			if(code == "-1") {
				divShowTip("设置成功！");
				clearForm();
			} else {
				divShowTip(ResultParser.toError(code));
			}
		}
	});
	
}
function clearForm(){
	$('#orderForm').form('clear');
}
</script>

</head>
<body>
<center>
<div class="easyui-panel" title="订单" style="width:500px">
		<div style="padding:10px 60px 20px 100px">
	    <form id="orderForm" method="post">
	    	<table cellpadding="5">
	    		<tr>
	    			<td>日志级别:</td>
	    			<td><select  class="easyui-combobox" id="logId" data-options="required:false, 
	    			missingMessage:'必填项'" style="height:28px;width:200px;">
	    			<option value="1">DEBUG</option>   
				    <option value="2">INFO</option>   
				    <option value="3">WARN</option>   
				    <option value="4">ERROR</option>   
				    <option value="5">FATAL</option>  
	    			</select></td>
	    		</tr>
	    		<tr>
	    			<td>角色ID:</td>
	    			<td><input class="easyui-numberbox" type="text" id="roleId" data-options="required:false, missingMessage:'必填项'" style="height:28px;width:200px;"></input></td>
	    		</tr>
	    	</table>
	    </form>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="height:38px;width:80px;">提交</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()"  style="height:38px;width:80px;">重置</a>
	    </div>
	    </div>
	</div>
</center>	
</body>
</html>