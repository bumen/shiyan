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
		{field:'name',title:'名称', editor:"textbox", ftype:"text", width:120,fadd:true},
		{field:'url',title:'权限值', editor:"textbox", ftype:"text", fadd:true}
	 ];
	
	 $('#treeGrid').treegrid({
			rownumbers:true,
			fit:true,
			idField: 'id',
			treeField: 'name',
			animate: true,
			collapsible: true,
			url:'permit/query',
			//queryParams:{
			//	'logQ.oprid':_oprid,
			//	'logQ.queryStr':'{"log_b_time":"' + getStringDate(new Date()) + '"}'
			//},
			fitColumns:true,
			method:'get',
			//toolbar:'#dataGrid_tb',
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
			//,onContextMenu: onTreeGridContextMenu
		});
	 
	 
	 document.getElementById('leafAddDialogForm').innerHTML = creatLeafAddDialogFormItems(columnHref);
	 
	 $('#leafAddDialog').dialog({
			width:550,  
		    title:"添加权限",
		    height:200,
		    content:$('#leafAddDialogForm'),
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
					onleafAddDialogFormSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#leafAddDialog').dialog('close', true);
				}
			}],
			onClose:function(){
				$('#leafAddDialogForm').form('clear');
			}
	 });
});

function onleafAddDialogFormSubmit() {
	var tableId = "leafAddDialogForm_table";
	var node = $('#treeGrid').treegrid('getSelected');
	var name = $('#' + tableId + '_name').textbox("getValue");
	var path = $('#' + tableId + '_path').textbox("getValue");
	if(name == "") return;
	
	var jsonObj = {};
	jsonObj['name'] = name;
	jsonObj['path'] = path;
	jsonObj['parentId'] = node.id;
	
	var formStr = $.toJSON(jsonObj);
	if(formStr == "") {
		return false;
	}
	loadingTipShow();
	$.ajax({
		url:"permit/add",
		type:"post",
		contentType : "application/json",
		data: formStr,
		success:function(retData){
			var jsonData = JSON.parse(retData);
			loadingTipClose();
			if(typeof(jsonData) == 'object') {
				divShowTip("数据已更新！");
				$('#treeGrid').treegrid('append',{
					parent: node.id,
					data: [jsonData]
				}); 
				$('#leafAddDialog').dialog('close', true);
			} else {
				divShowTip("数据提交失败！");
			}
		},error:function() {
			loadingTipClose();
			divShowTip("数据提交失败！");
		}
	});
}


function okOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	$('#treeGrid').treegrid('select', rowId);
	var rowNode = $('#treeGrid').treegrid('getSelected');
	
	var nameEditor = $('#treeGrid').treegrid('getEditor', {id:rowId, field:'name'});
	if(nameEditor == null) return;
	var rowName = $(nameEditor.target).textbox('getValue');
	if(rowName == "") return;
	
	var jsonStr = {};
	jsonStr.pid = rowId;
	jsonStr.name = rowName;
	//if(rowNode.type == 1) {
		var pathEditor = $('#treeGrid').treegrid('getEditor', {id:rowId, field:'path'});
		rowPath = $(pathEditor.target).textbox('getValue');
		if(rowPath == "")return;
		jsonStr.path = rowPath;
	//}
	$.messager.confirm("操作提示", "您确定要执行操作吗？", function (data) {
        if (data) {
       	 $('#treeGrid').treegrid('endEdit', rowId);
       	 $.ajax({
					type:"POST",
					url:"permit/edit",
					data:jsonStr,
					success:function(msg){
						if(msg == "1") {
							divShowTip("数据已更新！");
						} else {
							divShowTip("数据更新失败！");
						}
					}
				});
        }
    });
}

function getDataGridColumns(hrefArray) {
	var columns = [];
	$(hrefArray).each(function(index){
		var c = hrefArray[index];
		if(c.field != "name") {
			c.width = 100;
			c.align = "center";
		}
			columns.push(c);
	});
	return [columns];
}
function creatLeafAddDialogFormItems(hrefArray) {
	var tableHtml = [];
	var tableId = "leafAddDialogForm_table";
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
function onTreeGridContextMenu(e, row) {
	e.preventDefault();
	$(this).treegrid('select', row.id);
	$('#mm').menu('show',{
		left: e.pageX,
		top: e.pageY
	});
}

function appendLeaf(){
	var tableId = "leafAddDialogForm_table";
	var node = $('#treeGrid').treegrid('getSelected');
	/*if(node.type == 2) {
		divShowTip("功能权限由系统生成！");
		return;
		$('#' + tableId + '_path').textbox('disable');
	} else {
	} */
	$('#' + tableId + '_path').textbox('enable');
	$('#leafAddDialog').dialog("open", true);
}
function removeLeaf(){
	var node = $('#treeGrid').treegrid('getSelected');
	if(node._parentId == 0) {
		divShowTip("不能删除Root权限！");
		return;
	}
	if (node){
		var idArray = [];
		getChildrenIds(node, idArray);
		
		$.messager.confirm("操作提示", "您确定要执行操作吗？", function (data) {
	         if (data) {
	        	 $.ajax({
						type:"POST",
						url:"permit/del",
						data:{
							'children':idArray.join(",")
						},
						success:function(msg){
							if(msg == "1") {
								divShowTip("数据已更新！");
								$('#treeGrid').treegrid('remove', node.id);
							} else {
								divShowTip("删除操作异常！");
							}
						}
					});
	         }
	     });
		
		
	}
}
function getChildrenIds(node, idArray) {
	idArray.push(node.id);
	var children = node.children;
	if(typeof(children) == "object" && children.length != 0) {
		for(var i = 0, l = children.length; i < l; i++) {
			var child = children[i];
			getChildrenIds(child, idArray);
		}
	}
}
</script>
</head>
<body>
	<table id="treeGrid" class="easyui-treegrid" ></table>
	
	<div id="mm" class="easyui-menu" style="width:120px;">
		<div onclick="appendLeaf()" data-options="iconCls:'icon-add'">Append</div>
		<div onclick="removeLeaf()" data-options="iconCls:'icon-remove'">Remove</div>
	</div>
	
	<!-- addDialog -->
	<div id="leafAddDialog" style="padding:10px;"></div>
	<form id="leafAddDialogForm" method="post"></form>
</body>
</html>