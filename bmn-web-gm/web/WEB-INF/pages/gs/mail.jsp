<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/demo/demo.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.json.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/plugins/jquery.tabs.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js" type="text/javascript" charset="utf-8"></script>

<script type="text/javascript">

$(function(){
	//$('#roleNames').textbox('disable');	 
	
	 $('#channelCode').combobox({
		 url:'channel/queryTree',
		 method:'get',
		 valueField:'id',
		 textField:'text'
	 });
	 
	 $('#areaList').combobox({
		 url:'area/comboList',
		 method:'get',
		 valueField:'id',
		 textField:'text',
		 panelHeight:'auto'
	 });
	 
	 document.getElementById("isSendNewPlayer").disabled = true;
	 
});

function isAllOnClick(node) {
	var isSendNewPlayer = document.getElementById("isSendNewPlayer");
	var a = node.checked;
	if(a) {
		isSendNewPlayer.disabled = false;
			
		$('#roleNames').textbox('clear');	 
		$('#roleNames').textbox('disable');	 
	} else {
		isSendNewPlayer.disabled = true;
		isSendNewPlayer.checked = false;
		
		$('#roleNames').textbox('enable');	 
	}
}

function submitForm() {
	var checkNode = document.getElementById("isAll");
	if(!checkNode.checked) {
		var names = $('#roleNames').textbox('getValue');	
		if(!names || names == "") {
			divShowTip("请输入角色名！");
			return;
		}
	}
	var title = $('#mailTitle').textbox('getValue');
	var content = $('#mailContent').textbox('getValue');	
	if(!title || title == "" || !content || content == ""){
			divShowTip("请填写邮件标题和内容");
			return;
	}
	
	
	var areaId = $('#areaList').combobox('getValue');
	
	if(checkNode.checked) {
		var noNewPlayer = document.getElementById("isSendNewPlayer").checked;
		if(noNewPlayer) {
			sendAreaMail(areaId);
		} else {
			sendAreaMailNoNewPlayer(areaId);	
		}
	} else {
		sendRoleMail(areaId);
	}
}

function sendAreaMail(areaId) {
		var data = formatFromData(areaId);
		data.type = 2;
		
		var log = buildLog(data, "全服发");
		
		var url = "mail/sendmail";
		var cb = function(result){
			log += buildLogProp(result.info, "");
			trace(log);
		}
		
		sendJsonData(url, JSON.stringify(data), cb);
}

function sendAreaMailNoNewPlayer(areaId) {
		var data = formatFromData(areaId);
		data.type = 4;
		
		var log = buildLog(data, "全服发送不包括新玩家");
		
		var url = "mail/sendmail";
		var cb = function(result){
			log += buildLogProp(result.info, "");
			trace(log);
		}
		
		sendJsonData(url, JSON.stringify(data), cb);
}

function sendRoleMail(areaId) {
		var data = formatFromData(areaId);
		data.type = 3;
		
		var log = buildLog(data, "单发");
		
		var url = "mail/sendmail";
		var cb = function(result){
			log += buildLogProp(result.info, "");
			trace(log);
		}
		
		sendJsonData(url, JSON.stringify(data), cb);
}

function buildLog(data, type) {
	var log = buildLogProp("分区", data.areaId);
		log += buildLogProp("渠道", getChannelName(data.serviceId));
		log += buildLogProp("发送类型",  type);
		log += buildLogProp("邮件标题", data.title);
		log += buildLogProp("时间", formatDate(new Date().getTime()));
	return log;
}

function buildLogProp(name, value) {
	var prop = name + ": " + value + "</br>";
	return prop;
}


function formatFromData(areaId) {
	var serviceId = $('#channelCode').combobox('getValue');
	var title = $('#mailTitle').textbox('getValue');	
	var content = $('#mailContent').textbox('getValue');	
	var gold =$('#mailGold').numberbox('getValue');	 
	var exp = $('#mailExp').numberbox('getValue');	
	var iron = $('#mailIron').numberbox('getValue');	
	var honor = $('#mailHonor').numberbox('getValue');	
	var tankExp = $('#mailTankExp').numberbox('getValue');	
	var diamond = $('#mailDiamond').numberbox('getValue');	
	var corpsCoin = $('#mailCorpsCoin').numberbox('getValue');	
	
	gold = gold == "" ? 0 : gold;
	exp = exp == "" ? 0 : exp;
	iron = iron == "" ? 0 : iron;
	honor = honor == "" ? 0 : honor;
	tankExp = tankExp == "" ? 0 : tankExp;
	diamond = diamond == "" ? 0 : diamond;
	corpsCoin = corpsCoin == "" ? 0 : corpsCoin;
	
	var roleNameStr = $('#roleNames').textbox('getValue');	
	var roleNameArray = [];
	if(roleNameStr != "") {
		roleNameArray = roleNameStr.split(";");
	}
	
	var goodsStr = $('#mailGoodsid').textbox('getValue');	
	var goodsArray = [];
	if(goodsStr != "") {
		goodsArray = goodsStr.split(";");
	}
	
	var opt = {
		'areaId':areaId,
		'serviceId': serviceId,
		'type':0,
		'title': title,
		'content': content,
		'gold':gold,
		'exp':exp,
		'iron':iron,
		'honor':honor,
		'tankExp':tankExp,
		'diamond':diamond,
		'corpsCoin':corpsCoin,
		'roleNames':roleNameArray,
		'goodsList' : goodsArray
	}
	
	return opt;
}

function sendJsonData(url, data, cb) {
		$.ajax({
			type : "POST",
			url  : url,
			contentType : "application/json",
			data : data,
			success : cb
		});
}


function getChannelName(channelCode) {
	var datas = $('#channelCode').combobox('getData');
	for(i in datas) {
		var data = datas[i];
		
		if(data.id == channelCode) {
			return data.text;
		}
	}
	
	return "掌趣";
}

function mailShowTip(_msg) {
	$.messager.show({
		id: '' + new Date(),
		//title:'邮件发送消息',
        msg: _msg,
        showType: 'fade',
        timeout: 3000,
        showSpeed: 300,
        height: 'auto',
    	style:{
    		right:'',
    		bottom:''
		}
	});
}

function trace(v) {
	mailShowTip(v);
}
</script>
</head>


<body>
	<center>
	<div class="easyui-panel" data-options="border:0" style="width:613px;">
		<div id="p" class="easyui-panel" title="条件" style="width:600px;height:340px;padding:5px;"
				data-options="collapsible:true">
				<div style="padding:0px 0px 0px 60px">
				<table cellpadding="5">
		    		<tr>
		    			<td>分区:</td>
		    			<td>
		    				<input class="easyui-combobox" id="areaList" data-options="" style="height:25px;width:100px;"></input>
						</td>
		    		</tr>
		    		<tr>
		    			<td>渠道:</td>
		    			<td>
		    				<input class="easyui-combobox" id="channelCode" data-options="" style="height:25px;width:100px;"></input>
		    			 </td>
		    		</tr>
		    		<tr>
		    			<td>
				    			<p style="float:left;margin-top:5px;">全服发送: </p>
				    			<input id="isAll" onClick="isAllOnClick(this)"  type="checkbox" style="float:right;width:20px;height:20px;"/>
		    			</td>
		    			<td>
		    				<p style="float:left;margin-top:5px;">发新玩家: </p>
		    			 	<input id="isSendNewPlayer"  type="checkbox" style="width:20px;height:20px;"/>
		    			 </td>
		    		</tr>
		    		<tr>
		    			<td>角色ID:</td>
		    			<td><input class="easyui-textbox" id="roleNames" data-options="required:true, missingMessage:'必填项',multiline:true,prompt:'格式(分号)：角色ID;角色ID;角色ID'" style="height:128px;width:200px;"></input></td>
		    		</tr>
		    	</table>
		    	</div>
		</div>
		<div  class="easyui-panel" title="邮件" style="width:600px;height:610px;padding:5px;"
				data-options="">
			<div style="float:left;padding:0px 0px 0px 60px">
				    <form id="orderForm" method="post">
				    	<table cellpadding="5">
				    		<tr>
				    			<td>标题:</td>
				    			<td><input class="easyui-textbox"  id="mailTitle" data-options="required:true, missingMessage:'必填项'" style="height:28px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td>内容:</td>
				    			<td><input class="easyui-textbox" id="mailContent" data-options="required:true, missingMessage:'必填项',multiline:true" style="height:128px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td>物品Id:</td>
				    			<td><input class="easyui-textbox" id="mailGoodsid" data-options="prompt:'格式(分号)：id,数量; id,数量'" style="height:28px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td>金币:</td>
				    			<td><input class="easyui-numberbox" id="mailGold" data-options="" value=0 style="height:28px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td>银币:</td>
				    			<td><input class="easyui-numberbox" id="mailIron" data-options=""  value=0 style="height:28px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td>经验:</td>
				    			<td><input class="easyui-numberbox" id="mailExp" data-options=""  value=0 style="height:28px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td>荣誉:</td>
				    			<td><input class="easyui-numberbox" id="mailHonor" data-options="" value=0 style="height:28px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td>全局经验:</td>
				    			<td><input class="easyui-numberbox" id="mailTankExp" data-options="value:0" style="height:28px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td>钻石:</td>
				    			<td><input class="easyui-numberbox" id="mailDiamond" data-options="value:0" style="height:28px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td>军团币:</td>
				    			<td><input class="easyui-numberbox" id="mailCorpsCoin" data-options="value:0" style="height:28px;width:200px;"></input></td>
				    		</tr>
				    		<tr>
				    			<td colspan=2 align=center><a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="height:38px;width:80px;">发送</a></td>
				    		</tr>
				    	</table>
				    </form>
		    </div>
		    <div style="width:200px; height:400px;float:right;padding:10px 0px 0px 0px; style='font-color: red';margin:10px;">
		    	<p id="tipsInfo" style="color: red;white-space:normal; width:200px;word-wrap:break-word; 
word-break:break-all; 
overflow: hidden;"></p>
		    </div>
		</div>
	</div>
	</center>
</body>