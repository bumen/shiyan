<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		{field:'versionId',title:'版本',  width:50, hidden:true, formatter: formatVersion, sortable:true},
		{field:'versionMin',title:'版本',  width:50, formatter: formatVersion},
		{field:'areaId',title:'大区',  ftype:"int", width:50,fadd:true, sortable:true, sorter: function(a,b){
			return a > b ? 1 : -1;
		}},
		{field:'operate',title:"操作", formatter:formatOperate}
	 ];
	 
	 $('#areaVersion').combobox({
		 url:'version/options',
		 method:'get',
		 valueField:'id',
		 textField:'text',
		 panelHeight:'auto'
	 });
	 
	 $('#areaSelect').combobox({
		 url:'area/comboList',
		 method:'get',
		 valueField:'id',
		 textField:'text',
		 panelHeight:'auto'
	 });
	 
	 
	 $('#treeGrid').datagrid({
			rownumbers:true,
			fit:true,
			remoteSort:false,
			singleSelect: true,
			url:'version/list',
			fitColumns:true,
			remoteSort:false,
			method:'get',
			toolbar:'#dataGrid_tb',
			pagination:true,
			columns:getDataGridColumns(columnHref),
			pageSize:50,
			sortName:'versionId',
			onLoadSuccess:function(data){
				console.log(JSON.stringify(data));
				
				var nextId = 0;
				var mergesIndex = 0;
				
				var rows = data.rows;
				for(i in rows) {
					var row = rows[i];
					
					var id = row.versionId;
					var rowspan =  row.rowspan;
					if(nextId != id) {
						if(rowspan > 0) {
							$(this).datagrid('mergeCells',{
								index: mergesIndex,
								field: 'versionMin',
								rowspan: rowspan
							});		
						} else {
							rowspan = 1;
						}
						nextId = id;	
						mergesIndex += rowspan;				
					}
				}
			},
			onLoadError:function() {
			
			}
		});
	 
	 
	 $('#versionDialog').dialog({
			width:350,  
		    title:"添加版本号",
		    height:250,
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
					versionDialogFormSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#versionDialog').dialog('close', true);
				}
			}],
			onClose:function(){
				$('#versionDialog').form('clear');
			}
	 });
	 
	  $('#areaAddDialog').dialog({
			width:350,  
		    title:"添加分区",
		    height:250,
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
	 
});

function versionDialogFormSubmit() {
	var versionMin = $('#versionMin').textbox("getValue");
	var versionMax = $('#versionMax').textbox("getValue");
	if(versionMin == "" || versionMax == "") return;
	
	$.ajax({
		url:"version/edit",
		type:"post",
		data: {
			'versionMin': versionMin,
			'versionMax': versionMax
		},
		success:function(retData){
			if(retData == '1') {
				$('#treeGrid').datagrid('reload');
				divShowTip("添加成功！");
				$('#versionDialog').dialog('close', true);
				return;
			} 
			divShowTip("数据提交失败！");
		},error:function() {
			divShowTip("数据提交失败！");
		}
	});
}
function onAddDialogFormSubmit() {
	var areaVersion = $('#areaVersion').combobox("getValue");
	var areaId = $('#areaSelect').combobox("getValue");
	if(areaVersion == "") return;
	if(areaId == "") return;
	
	var jsonObj = {};
	jsonObj['areaVersion'] = areaVersion;
	jsonObj['areaId'] = areaId;
	
	loadingTipShow();
	
	$.ajax({
		url:"version/addArea",
		type:"post",
		data: jsonObj,
		success:function(retData){
			var result = JSON.parse(retData);
			loadingTipClose();
			if(typeof(result) == 'object') {
				if(result.success) {
					$('#treeGrid').datagrid('reload');
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

function onClickVersion() {
	var oldVersion = $('#version_code_panel').text();	
	$('#form_version_code_panel').text(oldVersion);
	$('#versionDialog').dialog("open", true);
}

function onClickAddArea() {
	$('#areaAddDialog').dialog("open", true);
}

function formatVersion(value, row, index) {
	var min = value;
	var max = row.versionMax;
	
	return min + "--" + max;
}

function getVersionName(key) {
	var datas = $('#areaVersion').combobox('getData');
	
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
					<td style="padding-left:10px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-edit" data-options="plain:true,onClick:onClickVersion">添加版本号</a>
					</td>
					
					<td style="padding-left:10px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true,onClick:onClickAddArea">绑定分区</a>
					</td>
	
				</tr>
			</table>
		</div>
	</div>
	
	<!-- versionDialog -->
	<div id="versionDialog" style="padding:10px;">
		<form id="areaAddDialogForm" method="post">
			<table cellpadding="5">
					<tr>
		    			<td>版本号</td>
		    		</tr>
		    		<tr>
		    			<td>
		    				Min:&nbsp<input class="easyui-textbox" id="versionMin" data-options="" style="height:28px;width:200px;"></input>
						</td>
		    		</tr>
		    		<tr>
		    			<td>
		    				Max:<input class="easyui-textbox" id="versionMax" data-options="" style="height:28px;width:200px;"></input>
						</td>
		    		</tr>
			</table>
		</form>
	</div>
	
	<!-- addDialog -->
	<div id="areaAddDialog" style="padding:10px;">
		<form id="areaAddDialogForm" method="post">
			<table cellpadding="5">
					<tr>
		    			<td>版本:</td>
		    			<td>
		    				<input class="easyui-combobox" id="areaVersion" data-options="" style="height:28px;width:200px;"></input>
						</td>
		    		</tr>
	    			<tr>
		    			<td>分区:</td>
		    			<td>
		    				<input class="easyui-combobox" id="areaSelect" data-options="" style="height:28px;width:200px;"></input>
						</td>
		    		</tr>
			</table>
		</form>
	</div>
	
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
							url:"version/deleteArea",
							data:{
								'areaId': rowNode.areaId,
								'versionId': rowNode.versionId
							},
							success:function(retData){
								var result = JSON.parse(retData);
								if(result.success) {
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

</script>
</html>