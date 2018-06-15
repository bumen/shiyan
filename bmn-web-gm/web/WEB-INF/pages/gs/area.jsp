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
		{field:'channelCode',title:'渠道',  width:50, formatter: formatChannel},
		{field:'areaId',title:'大区',  ftype:"int", width:50,fadd:true, sortable:true},
		{field:'name',title:'名称', editor:"textbox", ftype:"text", width:120,fadd:true},
		{field:'state',title:'状态',  ftype:"text", width:120,fadd:false, formatter: formatState},
		{field:'whiteUsed',title:'白名单',ftype:"text", width:120,fadd:false, formatter: formatWhite},
		{field:'whiteList',title:'白名单', ftype:"text", width:120,fadd:false, hidden:true},
		{field:'operate',title:"操作", formatter:formatOperate}
	 ];
	 
	  $('#channelCode').combobox({
		 url:'channel/queryTree',
		 method:'get',
		 valueField:'id',
		 textField:'text',
	 });
	
	 $('#treeGrid').datagrid({
			rownumbers:true,
			fit:true,
			customObj:{lastEditIndex:-1, editing:false, lastEditName:""},//自定义属性，lastEditIndex上次编辑的行index ,// editing 当前是否打开行编辑
			remoteSort:false,
			singleSelect: true,
			url:'area/list',
			//queryParams:{
			//	'logQ.oprid':_oprid,
			//	'logQ.queryStr':'{"log_b_time":"' + getStringDate(new Date()) + '"}'
			//},
			fitColumns:true,
			method:'get',
			toolbar:'#dataGrid_tb',
			pagination:true,
			columns:getDataGridColumns(columnHref),
			pageSize:50,
			//onDblClickRow:onDbClickRow,
			onLoadSuccess:function(data){
			//	$('#queryDialog').dialog('close', true);
			},
			onLoadError:function() {
				//divShowTip("数据提交失败！");
			}
			,onClickCell: onClickCell
			//, onContextMenu: onTreeGridContextMenu
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
			c.width = 100;
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
</script>
</head>


<body>
	<table id="treeGrid" class="easyui-datagrid" ></table>
	<div id="dataGrid_tb" style="padding: 5px; height: auto">
		<div style="margin-top: 5px">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<!--  <td>
						<a href="#" class="easyui-linkbutton" iconCls="icon-edit" data-options="plain:true,onClick:onClickDefaultPremit" >设置默认权限</a>
					</td>
					<td style="width:10px;">
						<div class="datagrid-btn-separator"></div>
					</td>-->
					<td style="padding-left:21px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true,onClick:onClickAddArea">添加</a>
					</td>
	
				</tr>
			</table>
		</div>
	</div>
	
	<div id="mm" class="easyui-menu" style="width:120px;">
		<div onclick="appendLeaf()" data-options="iconCls:'icon-add'">Append</div>
		<div onclick="removeLeaf()" data-options="iconCls:'icon-remove'">Remove</div>
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
	   var modifyButton = createCustomGridButton(index, "white", "edit", "白名单");
	   var cancelButton = createCustomGridButton(index, "del", "cancel", "删除");
	   return modifyButton  +"&nbsp;&nbsp;"+ cancelButton;
}

function whiteOperateButton(node) {
 	 var rowId = node.getAttribute("rowId");
	 $('#treeGrid').datagrid('selectRow', rowId);
	 var rowNode = $('#treeGrid').datagrid('getSelected');
	 if(rowNode) {
	 	 var areaId = rowNode.areaId;
	 	 
	 	 var oldWhites = rowNode.whiteList;
	 	 $('#whiteEditDialogFrom_table_white').textbox("setValue", oldWhites.join(","));
	 	 
	 	 $('#whiteEditDialogFrom_table_areaId').val(areaId);
	 	 $('#whiteEditDialogFrom_table_channelCode').val(rowNode.channelCode);
		
		 $('#whiteEditDialog').dialog('setTitle', "添加 " + areaId + " 分区白名单");
	 	 $('#whiteEditDialog').dialog('open', true);
	 }
	
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
							url:"area/delete",
							data:{
								'channelCode': rowNode.channelCode,
								'areaId': rowNode.areaId
							},
							success:function(data){
								if(data.result) {
									$('#treeGrid').datagrid('deleteRow', parseInt(rowId));
									$('#treeGrid').datagrid('reload');
									$('#treeGrid').datagrid('sort', {
										sortName: 'id',
										sortOrder: 'asc'
									});
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



function downWhiteOperateButton(node) {
	  updateWhiteState(node, false);
}

function upWhiteOperateButton(node){
	 updateWhiteState(node, true);
}

function updateWhiteState(node, status) {
	var rowId = node.getAttribute("rowId");
	 $('#treeGrid').datagrid('selectRow', rowId);
	 var rowNode = $('#treeGrid').datagrid('getSelected');
	 if(rowNode) {
	 		$.ajax({
				type:"POST",
				url:"area/uwhite",
				data:{
					'channelCode': rowNode.channelCode,
					'areaId': rowNode.areaId,
				},
				success:function(data){
					if(data.result) {
						rowNode.whiteUsed = status;
						$('#treeGrid').datagrid('updateRow', {
							index : parseInt(rowId),
							row: rowNode
						});
						divShowTip("数据已更新！");
					} else {
						divShowTip("数据更新失败");
					}
				}
			});
	 }
}

function onClickCell(index, field, value) {
	var opt = $('#treeGrid').datagrid('options');
	var custom = opt.customObj;
	
	//当前正处行编辑状态
	if(custom.editing) {
		//且当前选中其它非编辑行时，则取消当前行编辑状态，并保存数据
		var oldIndex = custom.lastEditIndex;
		if(oldIndex != index || field != "name") {
			var nameEditor = $('#treeGrid').treegrid('getEditor', {id:oldIndex, field:'name'});
			if(nameEditor == null) return;
			
			var newName = $(nameEditor.target).textbox('getValue');
			if(!newName || newName == "")
				return;
				
			if(custom.lastEditName != newName) {
				var rowNode = $('#treeGrid').datagrid('getRow', {"id":"treeGrid", 'index':oldIndex});
				sendNewAreaName(rowNode.id, newName);
			}
			
			
			if(field == "name") {
				if($('#treeGrid').datagrid('validateRow', index)) {
					custom.editing = true;				
					custom.lastEditIndex = index;
					custom.lastEditName = value; 
					$('#treeGrid').datagrid("beginEdit", index);
				}
			} else {
				custom.editing = false;
				custom.lastEditIndex = -1;
				custom.lastEditName = "";
			}
			$('#treeGrid').datagrid('endEdit', oldIndex);
		}
		
	} else {
		if(field == "name") {
			if($('#treeGrid').datagrid('validateRow', index)) {
				custom.editing = true;				
				custom.lastEditIndex = index;
				custom.lastEditName = value; 
				$('#treeGrid').datagrid("beginEdit", index);
			}
		}
	}
}

function sendNewAreaName(areaId, name) {
	 $.ajax({
		type:"POST",
		url:"area/uname",
		data:{
			'areaId': areaId,
			'name': name
		},
		success:function(data){
			if(data.result) {
				divShowTip("数据已更新！");
			} else {
				divShowTip("数据更新失败");
			}
		}
	});
}


function formatWhite(value, row, index) {
	if(value) {
		return createCustomGridButton(index, "downWhite", "close", "关闭");
	} else {
		return createCustomGridButton(index, "upWhite", "open", "开启");
	}
}

function formatState(value, row, index) {
	if(value == "stop") {
		return value + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +createCustomGridButton(index, "upState", "open", "开启");
	} else {
		return value + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +createCustomGridButton(index, "downState", "close", "关闭");
	}
}

function downStateOperateButton(node) {
	  updateState(node, "stop");
}

function upStateOperateButton(node){
	 updateState(node, "free");
}

function updateState(node, status) {
	var rowId = node.getAttribute("rowId");
	 $('#treeGrid').datagrid('selectRow', rowId);
	 var rowNode = $('#treeGrid').datagrid('getSelected');
	 if(rowNode) {
	 		$.ajax({
				type:"POST",
				url:"area/ustate",
				data:{
					'channelCode': rowNode.channelCode,
					'areaId': rowNode.areaId,
				},
				success:function(data){
					if(data.result) {
						rowNode.state = status;
						$('#treeGrid').datagrid('updateRow', {
							index : parseInt(rowId),
							row: rowNode
						});
						divShowTip("数据已更新！");
					} else {
						divShowTip("数据更新失败");
					}
				}
			});
	 }
}

</script>
</html>