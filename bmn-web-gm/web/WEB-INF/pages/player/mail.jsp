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
$(function(){
	var columnHref = [
		{field:'id', hidden:true},
		{field:'title',title:'标题'},
		{field:'content',title:'内容'},
		{field:'gold',title:'金币'},
		{field:'iron',title:'银币'},
		{field:'exp',title:'经验'},
		{field:'honor',title:'荣誉'},
		{field:'tankExp',title:'全局经验'},
		{field:'diamond',title:'钻石'},
		{field:'corpsCoin',title:'军团币'},
		{field:'read',title:'状态', formatter: formatRead},
		{field:'attach',title:'附件', formatter: formatAttach},
		{field:'attachget',title:'附件状态', formatter: formatAttachget},
		{field:'creatTime',title:'创建时间', formatter: formatTime, sortable:true},
		{field:'operate',title:"操作", formatter:formatOperate}
	 ];
	 
	 $('#treeGrid').datagrid({
			rownumbers:true,
			fit:true,
			remoteSort:false,
			singleSelect: true,
			fitColumns:true,
			method:'get',
			toolbar:'#dataGrid_tb',
			columns:getDataGridColumns(columnHref),
			pagination:false
		});
		
	 
	 //document.getElementById('areaAddDialogForm').innerHTML = creatAreaAddDialogFormItems(columnHref);
	 
	 
	 $('#areaAddDialog').dialog({
			width:350,  
		    title:"添加分区",
		    height:250,
		  //  content:$('#areaAddDialogForm'),
		    cache: false,
		    minimizable:false,
		    maximizable:false,
		    collapsible:true,
		    closed:true,	//初始化时，关闭
		    modal:true,
		    buttons:[{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					onAddDialogFormSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#areaAddDialog').dialog('close', true);
				}
			}],
			onClose:function(){
				$('#areaAddDialogForm').form('clear');
			}
	 });
	 
	 $('#whiteEditDialog').dialog({
			width:550,  
		    title:"添加白名单",
		    height:390,
		    content:$('#whiteEditDialogForm'),
		    cache: false,
		    minimizable:false,
		    maximizable:false,
		    collapsible:true,
		    closed:true,	//初始化时，关闭
		    modal:true,
		    buttons:[{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					whiteEditDialogSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#whiteEditDialog').dialog('close', true);
				}
			}],
			onClose:function(){
				$('#whiteEditDialogForm').form('clear');
			}
	 });
});

function onAddDialogFormSubmit() {
	var tableId = "AddDialogForm_table";
	var node = $('#treeGrid').treegrid('getSelected');
	var areaId = $('#areaId').numberbox("getValue");
	var name = $('#areaName').textbox("getValue");
	var channelCode = $('#channelCode').combobox("getValue");
	if(name == "") return;
	if(areaId == "") return;
	if(channelCode == "") return;
	
	var jsonObj = {};
	jsonObj['channelCode'] = channelCode;
	jsonObj['areaId'] = areaId;
	jsonObj['name'] = name;
	
	loadingTipShow();
	
	$.ajax({
		url:"area/add",
		type:"post",
		data: jsonObj,
		success:function(retData){
			//{"info":"","obj":{"id":4,"name":"ddd","state":"stop","timestamp":0,"whiteList":[],"whiteUsed":false},"result":1,"success":true}
			var result = JSON.parse(retData);
			loadingTipClose();
			if(typeof(result) == 'object') {
				if(result.success) {
					$('#treeGrid').datagrid('appendRow',result.obj); 
					$('#areaAddDialog').dialog('close', true);
					divShowTip("添加成功！");
					return;
				}
				
				divShowTip(result.info);
				return;
			} 
			
			divShowTip("数据提交失败！");
		},error:function() {
			loadingTipClose();
			divShowTip("数据提交失败！");
		}
	});
}

function whiteEditDialogSubmit() {
	var whites = $('#whiteEditDialogFrom_table_white').textbox("getValue");
	var areaId = $('#whiteEditDialogFrom_table_areaId').val();
	var channelCode = $('#whiteEditDialogFrom_table_channelCode').val();
	
	loadingTipShow();
	$.ajax({
			type:"POST",
			url:"area/addWhite",
			data:{
				'channelCode':channelCode,
				'areaId': areaId,
				'whites': whites
			},
			success:function(retData){
				loadingTipClose();
				if(retData.result) {
					 var rowNode = $('#treeGrid').datagrid('getSelected');
					 if(rowNode) {
					 	if(rowNode.id = areaId) {
					 		rowNode.whiteList = whites.split(",");
					 		var _index = $('#treeGrid').datagrid('getRowIndex', rowNode);
					 		$('#treeGrid').datagrid('updateRow', {
					 			index : _index,
					 			row: rowNode
					 		});
					 	}
					 }
				
					divShowTip("数据已更新！");
					$('#whiteEditDialog').dialog('close', true);
				} else {
					divShowTip(retData.info);
				}
			},error:function() {
				loadingTipClose();
				divShowTip("数据提交失败！");
			}
	});
}

function getDataGridColumns(hrefArray) {
	var columns = [];
	$(hrefArray).each(function(index){
		var c = hrefArray[index];
		//if(c.field != "name") {
			//c.width = 100;
			c.align = "center";
		//}
			columns.push(c);
	});
	return [columns];
}
function creatAreaAddDialogFormItems(hrefArray) {
	var tableHtml = [];
	var tableId = "AddDialogForm_table";
	tableHtml.push('<table id="'+tableId+'" width=100% border="0">');
	
	var count = 0;
	$.each(hrefArray, function(index, c){
		if(c.fadd) {
			var c_id = tableId + "_" + c.field;
			var ftype = c.ftype;
			if(count == 0) {
				tableHtml.push('<tr>');
			}
			count = count + 1;
			tableHtml.push('<td align="right" nowrap="nowrap" width="10%">' + c.title + ':</td>');
			tableHtml.push('<td  align="left" width="20%" style="text-align:left;">');
			switch(ftype) {
				case "text":
					tableHtml.push('<input class="easyui-textbox" id="'+c_id+'" data-options="height:25, width:150" />');
					break;
				case "int":
					tableHtml.push('<input class="easyui-numberbox" id="'+c_id+'" data-options="height:25, width:150" />');
					break;
			}
			tableHtml.push('</td>');
			if(count == 2) {
				count = 0;
				tableHtml.push('</tr>');
			}
		}
	});
	tableHtml.push('</table>');
	
	return tableHtml.join("");
}

function onClickAddArea() {
	$('#areaAddDialog').dialog("open", true);
}

function formatChannel(value, row, index) {
	var _v = value;
	if(typeof(value) != "undefined" && value  != "") {
		_v = getChannelName(value);
	} else {
		_v = "";
	}
	return _v;
}

function getChannelName(key) {
	var datas = $('#channelCode').combobox('getData');
	
	for(i in datas) {
		var data = datas[i];
		if(data.id == key) {
			return data.text;
		}
	}
	
	return "";
}

function queryBtnClick() {
	var roleId = $('#dataGrid_roleId').textbox('getValue');
	if(roleId == "" || Number(roleId) <= 0)
		return;
	
	$.ajax({
		url:"mail/query",
		type:"post",
		data: {
			'roleId':roleId
		},
		success:function(retData){
			var result = JSON.parse(retData);
			if(typeof(result) == 'object') {
				var data = result.data;
				if(result.success) {
					$('#treeGrid').datagrid('loadData', data);
					
					divShowTip("查询成功！");
					return;
				}
				
				divShowTip(data);
				return;
			} 
			
			divShowTip("数据提交失败！");
		},error:function() {
			loadingTipClose();
			divShowTip("数据提交失败！");
		}
	});
	 
}
</script>
</head>


<body>
	<table id="treeGrid" class="easyui-datagrid" ></table>
	<div id="dataGrid_tb" style="padding: 5px; height: auto">
		<div style="margin-top: 5px">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td style="padding-left:21px;">
						<input id="dataGrid_roleId" class="easyui-numberbox" data-options="prompt:'请输入角色ID'" style="width: 150px;float:right">
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" data-options="plain:true, onClick:queryBtnClick">查询</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	
	<!-- addDialog -->
	<div id="areaAddDialog" style="padding:10px;">
		<form id="areaAddDialogForm" method="post">
			<table cellpadding="5">
					<tr>
		    			<td>渠道:</td>
		    			<td>
		    				<input class="easyui-combobox" id="channelCode" data-options="" style="height:28px;width:200px;"></input>
						</td>
		    		</tr>
		    		<tr>
		    			<td>分区:</td>
		    			<td>
		    				<input class="easyui-numberbox" id="areaId" data-options="" style="height:28px;width:200px;"></input>
		    			 </td>
		    		</tr>
		    		<tr>
		    			<td>名称:</td>
		    			<td>
		    				<input class="easyui-textbox" id="areaName" data-options="" style="height:28px;width:200px;"></input>
		    			 </td>
		    		</tr>
			</table>
		</form>
	</div>
	
	
	<!-- whiteEditDialog -->
	<div id="whiteEditDialog" style="padding:10px;"></div>
	<form id="whiteEditDialogForm" method="post">
		<table id="whiteEditDialogFrom_table">
			<tr>
				<td>
					<input class="easyui-textbox" id="whiteEditDialogFrom_table_white" data-options="multiline:true" style="width:510px;height:240px" />
					<input id="whiteEditDialogFrom_table_areaId" type="hidden"/>
					<input id="whiteEditDialogFrom_table_channelCode" type="hidden"/>
				</td>
			</tr>
			<tr>
				<td>
					<p style="color:red">提示：IP, IP, IP</p>
				</td>
			</tr>
		</table>
	</form>
</body>


<script type="text/javascript">

function formatOperate(value, row, index) {
	   var cancelButton = createCustomGridButton(index, "del", "cancel", "删除");
	   return cancelButton;
}

function delOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	 $('#treeGrid').datagrid('selectRow', rowId);
	 var rowNode = $('#treeGrid').datagrid('getSelected');
	 if(rowNode) {
	 	$.messager.confirm("操作提示", "您确定要执行删除操作吗？", function (data) {
	        if (data) {
		       	 $.ajax({
							type:"POST",
							url:"mail/delete",
							data:{
								'roleId': rowNode.reciverId,
								'mailId': rowNode.id
							},
							success:function(data){
								if(data == "1") {
									$('#treeGrid').datagrid('deleteRow', parseInt(rowId));
									$('#treeGrid').datagrid('reload');
									divShowTip("数据已更新！");
								} else {
									divShowTip(data.info);
								}
							}
					});
	        }
	   	 });
	 }
}

function formatRead(value, row, index) {
	if(value) {
		return "已读";
	}
	return "未读";
}

function formatTime(value, row, index) {
	if(value > 0) {
		return formatDate(value);
	}
	return value;
}

function formatAttachget(value, row, index) {
	if(value) {
		return "已领取";
	}
	return "未领取";
}

function formatAttach(value, row, index) {
	var r = "";
	for(var i in value) {
		var goods = value[i];
		r += goods.goodsId + ":" + goods.count;
		r += ";";
	}
	return r;
}


</script>
</html>