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
	var orderId = $('#orderId').textbox("getValue");
	var serviceId = $('#serviceId').textbox("getValue");
	var channelId = $('#channelId').textbox("getValue");
	var propId = $('#propId').textbox("getValue");
	var roleId = $('#roleId').textbox("getValue");
	var userId = $('#userId').textbox("getValue");
	var actualPrice = $('#actualPrice').textbox("getValue");
	
	$.ajax({
		type:"POST",
		url:"tester/recharge",
		data:{
			'orderId': orderId,
			'serviceId': serviceId,
			'channelId': channelId,
			'propId': propId,
			'roleId': roleId,
			'userId': userId,
			'actualPrice':actualPrice
		},
		success:function(retData){
			if(retData == "1") {
				divShowTip("充值成功！");
				clearForm();
			} else {
				divShowTip("充值失败！");
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
	    			<td>订单号:</td>
	    			<td><input class="easyui-textbox" type="text" id="orderId" data-options="required:true, missingMessage:'必填项'" style="height:28px;width:200px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>渠道:</td>
	    			<td><input class="easyui-textbox" type="text" id="serviceId" data-options="required:false, missingMessage:'必填项'" style="height:28px;width:200px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>推广渠道:</td>
	    			<td><input class="easyui-textbox" type="text" id="channelId" data-options="required:false, missingMessage:'必填项'" style="height:28px;width:200px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>商品ID:</td>
	    			<td><input class="easyui-numberbox" id="propId" data-options="required:true, missingMessage:'必填项'" style="height:28px;width:200px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>角色ID:</td>
	    			<td><input class="easyui-numberbox" id="roleId" data-options="required:true, missingMessage:'必填项'" style="height:28px;width:200px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>用户ID:</td>
	    			<td><input class="easyui-textbox" id="userId" data-options="required:false, missingMessage:'必填项'" style="height:28px;width:200px;"></input></td>
	    		</tr>
	    		<tr>
	    			<td>充值金额:</td>
	    			<td><input class="easyui-numberbox" id="actualPrice" data-options="required:true, missingMessage:'必填项'" style="height:28px;width:200px;"></input></td>
	    		</tr>
	    	</table>
	    </form>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="height:38px;width:80px;">充值</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()"  style="height:38px;width:80px;">重置</a>
	    </div>
	    </div>
	</div>
</center>	
</body>
</html>