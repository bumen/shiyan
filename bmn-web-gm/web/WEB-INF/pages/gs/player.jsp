<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="${pageContext.request.contextPath}/jquery/jquery.min.js" type="text/javascript" charset="utf-8" ></script>
<script src="${pageContext.request.contextPath}/js/common.js" type="text/javascript" charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/js/user.js" type="text/javascript" charset="utf-8" ></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/demo/demo.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.json.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/plugins/jquery.tabs.js"></script>

<script type="text/javascript">
$(function(){
	$('#loginForbidBeginTime').datetimebox({
		
	
	});
});

function disableAccount() {
	var areaId = $("#areaList").val();
	if (isEmpty(areaId)){ 
		divShowTip("请选择服务器");
		return;
	}
	var name = $("#accountName").val();
	if(isEmpty(name)){
		divShowTip("请填写账号");
		return;
	}
	
	var type = $('#accountType').val();
	var beginTime = $('#loginForbidBeginTime').datetimebox('getValue');
	var endTime = $('#loginForbidEndTime').datetimebox('getValue');
	
	if(beginTime == "") {
		divShowTip("请输入开始时间");
		return;
	}
	
	if(endTime == "") {
		divShowTip("请输入结束时间");
		return;
	}
	
	$.ajax({
		url:"loginForbid",
		type:"post",
		data: {
			areaId : areaId,
			type: type,
			account: name,
			beginDate:beginTime,
			endDate: endTime
		},
		success:function(retData){
				divShowTip(retData.info);
		},error:function() {
			divShowTip("数据提交失败！");
		}
	});
	
}
</script>
</head>
<body>

	服务器列表：
	<select id="areaList" >
		<c:forEach var="area" items="${areaList}">
			<option value="${area.id}">${area.text}</option>
		</c:forEach>
	</select>  
	<select id="accountType">
		<option value="1" selected>账号</option>
		<option value="2">角色</option>
		<option value="3">角色Id</option>
	</select>
	<input id="accountName" type="text" /> 
	<button id="queryBtn" onclick="queryAccount()">查询</button>
	
	</br></br>
	开始时间：<input id="loginForbidBeginTime" class="easyui-datetimebox" value="" style="width:200px">
	结束时间：<input id="loginForbidEndTime" class="easyui-datetimebox" value="" style="width:200px">
	<button id="disableBtn" onclick="disableAccount()">封号</button>
	
	<!--</br></br>
	开始时间：<input id="talkForbidBeginTime"  class="easyui-datetimebox" value="" style="width:200px">
	结束时间：<input id="talkForbidEndTime" class="easyui-datetimebox" value="" style="width:200px">
	<button id="enableBtn" onclick="enableTalk()">禁言</button>-->
	
	<p id="tipsInfo" style="color: red"></p>
	<table id="accountTable" >	
		<tr><td>区服名 ：</td><td id="areaName" > </td></tr>
		<tr><td>ID ：</td><td id="roleId" > </td></tr>
		<tr><td>名字：</td><td id="roleName"></td></tr>
		<tr><td>账号：</td><td id="uid"></td></tr>
		<tr><td>等级：</td><td><input id="level" type="text" /> </td></tr>
		<tr><td>VIP等级：</td><td><input id="vipLevel" type="text" /> </td></tr>
		<tr><td>经验：</td><td><input id="exp" type="text" /> </td></tr>
		<tr><td>金币：</td><td><input id="gold" type="text" /> </td></tr>
		<tr><td>银币：</td><td><input id="iron" type="text" /> </td></tr>
		<tr><td>钻石：</td><td><input id="diamonds" type="text" /> </td></tr>
		<tr><td>荣誉：</td><td><input id="honor" type="text" /> </td></tr>
		<tr><td>军衔id : </td><td><input id="armytitle" type="text" /> </td></tr>
		<tr><td>军团id : </td><td><input id="corpsId" type="text" /> </td></tr>
		<tr><td>军团名称 : </td><td><input id="corpsName" type="text" /> </td></tr>
		<tr><td>累计充值金额: </td><td><input id="payRmb" type="text" /> </td></tr>
		<tr><td>最后登录时间 : </td><td><input id="lastLoginTime" type="text" /> </td></tr>
		<tr><td>离线: </td><td><input id="offlineTime" type="text" /> </td></tr>
		<tr><td>坦克1分区 : </td><td><input id="tank1AreaId" type="text" /> </td></tr>
		<tr><td>坦克1角色ID : </td><td><input id="tank1RoleId" type="text" style="width:280px;"/> </td></tr>
		
		<tr id="insertAfter"><td>账号状态 : </td><td id="roleStatus"></td></tr>
		
		
		
	</table>

	</br>
	
	<!--邮件：
	账号：<input id="mailreciver" value="请输入账号"/>
	标题：<input id="mailtitle"/>
	内容：<textarea id="mailcontent" rows="10" cols="30" value="请输入内容">
	物品：<input id="mailtitle"/>-->
</body>
</html>