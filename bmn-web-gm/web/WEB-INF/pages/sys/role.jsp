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
		{field:'name',title:'角色名', ftype:"text"},
		//{field:'oprPermit',title:'permits', ftype:"text", hidden:true},
		{field:'bindCount',title:'状态', ftype:"text", formatter:formatState},             
		{field:'operate',title:'操作', ftype:"opr", formatter:formatOperate}
	 ];
	
	$('#dataGrid').datagrid({
		singleSelect: true,
		showFooter:true,
		rownumbers:true,
		fit:true,
		fitColumns:true,
		url:'role/query',
		toolbar:'#dataGrid_tb',
		pagination:true,
		columns:getDataGridColumns(columnHref),
		pageSize:10,
		//onDblClickRow:onDbClickRow,
		onLoadSuccess:function(data){
		},
		onBeforeLoad: function(param) {
		},
		onLoadError:function() {
		}
	});
	
	//document.getElementById('leafAddDialogForm').innerHTML = creatLeafAddDialogFormItems(columnHref);
	
	 $('#bindPermitDialog_tree').combotree({
		 url:'permit/selectTree',
		 method:'get',
		 multiple:true,
		 width:250,
		 cascadeCheck:false,
		 lines:true,
		 panelHeight:490,
	 	 onCheck:function(node, checked) {
			 var treeNode = $('#bindPermitDialog_tree').combotree('tree');	// 获取树对象
			 if(checked) {
				 onClickTreeChecked(node, treeNode);
			 } else {
				 onClickTreeUnchecked(node, treeNode);
			 }
		 }
	 });
	 
	 $('#bindPermitDialog').dialog({
			width:300,  
		    title:"绑定权限",
		    height:600,
		   // content:$('#bindPermitDialog_tree'),
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
					onBindPermitDialogSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#bindPermitDialog').dialog('close', true);
				}
			}],
			onClose:function(){
				//$('#leafAddDialogForm').form('clear');
			}
	 });
});

function onClickTreeChecked(node, treeNode) {
	var pNode = treeNode.tree('getParent', node.target);
	if(pNode != null) {
		treeNode.tree('check', pNode.target);
		onClickTreeChecked(pNode, treeNode);
	}
}
function onClickTreeUnchecked(node, treeNode) {
	if(node != null) {
		var children = treeNode.tree('getChildren', node.target);
		var c = false;
		for(var i = 0, l = children.length; i < l; i++) {
			var child = children[i];
			c = child.checked;
			if(c)
			  break;
		}
		if(c) {
			treeNode.tree('check', node.target);	
			return c;
		} else {
			var pNode = treeNode.tree('getParent', node.target);
			if(pNode) {
				treeNode.tree('uncheck', pNode.target);
				onClickTreeUnchecked(pNode, treeNode);
			}
		}
	}
}

function getDataGridColumns(hrefArray) {
	var columns = [];
	$(hrefArray).each(function(index){
		var c = hrefArray[index];
		c.width = 100;
		c.align = "center";
		columns.push(c);
	});
	return [columns];
}
function formatOperate(value, row, index) {
	   var i = row.id;
	   var modifyButton = createCustomGridButton(index, "edit", "edit", "绑定权限");
	   var  delButton = createCustomGridButton(index, "cancel", "cancel", "删除");
	   return modifyButton+"&nbsp&nbsp" + delButton;
}

function createCustomGridButton(rowId, type, icon, title) {
	var htmlArray  = [];
	var _f = type + "OperateButton(this)";
	htmlArray.push('<a rowId="'+rowId+'" group="" onclick="'+_f+'" class="l-btn l-btn-small l-btn-plain my_a" href="javascript:void(0)">');
	htmlArray.push('<span class="l-btn-left l-btn-icon-left">');
	htmlArray.push('<span class="l-btn-text">'+title+'</span>');
	htmlArray.push('<span class="l-btn-icon icon-'+icon+'">&nbsp;</span>');
	htmlArray.push('</span>');
	htmlArray.push('</a>');
	return htmlArray.join("");
}

function editOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	document.getElementById("bindPermitDialog").setAttribute("rowId", rowId);
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	
	var str = rowNode.permitStr;
	var permitArray = str.split(",");
	$('#bindPermitDialog_tree').combotree("setValues", permitArray);
	$('#bindPermitDialog').dialog('open', true);
	return;
	
	var jsonStr = {};
	jsonStr.id = rowNode.id;
	$.ajax({
			type:"POST",
			url:"role/queryRolePermit",
			data:{
				'roleId':rowNode.id
			},
			success:function(_ret){
				var ret = JSON.parse(_ret);
				var result = ret.result;
				if(result == "1") {
					var permitStr = ret.data;
					var permitArray = permitStr.split(",");
					$('#bindPermitDialog_tree').combotree("setValues", permitArray);
					$('#bindPermitDialog').dialog('open', true);
				}
			}
		});
}

function cancelOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	var roleId = rowNode.id;
	if(roleId){
		$.messager.confirm("操作提示", "您确定要执行删除操作吗？", function (data) {
	        if (data) {
	       	 		$.ajax({
						type:"POST",
						url:"role/del",
						data:{
							'roleId': roleId
						},
						success:function(msg){
							if(msg == "1") {
								$('#dataGrid').datagrid('deleteRow', parseInt(rowId));
								$('#dataGrid').datagrid('reload', {});
								divShowTip("删除成功！");
							} else {
								divShowTip("删除失败！");
							}
						}
					});
	        }
	    });
	}
}

function onBindPermitDialogSubmit() {
	var rowId = document.getElementById("bindPermitDialog").getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	var treeNode = $('#bindPermitDialog_tree').combotree('tree');	// 获取树对象
	var nodeArray = treeNode.tree('getChecked', ['checked']);	
	var idArray = [];
	$.each(nodeArray, function(i, node){
		idArray.push(node.id);
	});
	var jsonObj = {};
	jsonObj.permitIds = idArray.join(",");
	jsonObj.id = rowNode.id;
	loadingTipShow();
	$.ajax({
		type:"POST",
		url:"role/bind",
		data:{
			'roleId': rowNode.id,
			'permitIds':idArray.join(",")
		},
		success:function(msg){
			loadingTipClose();
			if(msg == "1") {
				rowNode.permitStr = idArray.join(",");
				$('#dataGrid').datagrid('updateRow',{
								index: parseInt(rowId),
								row: rowNode
							}); 
				divShowTip("绑定成功！");
				$('#bindPermitDialog').dialog('close', true);
			} else {
				divShowTip("绑定失败！");
			}
		}
	});
}
function onClickDefaultPremit() {
	
}

function onClickAddRole() {
	var rolename = $('#dataGrid_addRole').textbox("getValue");
	if(rolename == "") return;
	var jsonStr = {};
	jsonStr['name'] = rolename;
	$.ajax({
		type:"POST",
		url:"role/add",
		data:{
			'name': rolename
		},
		success:function(retData){
			var jsonData = eval("("+retData+")");
			if(typeof(jsonData) == "object") {
				$('#dataGrid').datagrid('appendRow', jsonData); 
				divShowTip("角色添加成功！");
			} else {
				divShowTip("添加失败！");
			}
		}
	});
}
</script>
</head>
<body>
	<table id="dataGrid" class="easyui-datagrid" style="width: 700px; height: 250px" > </table>
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
						<input id="dataGrid_addRole" class="easyui-textbox" data-options="prompt:'请输入角色名'" style="width: 150px;float:right">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true,onClick:onClickAddRole">添加</a>
					</td>
	
				</tr>
			</table>
		</div>
	</div>
	
	<!-- addDialog -->
	<div id="bindPermitDialog" style="padding:10px;">
		<input id="bindPermitDialog_tree" />  
	</div>
	<form id="bindPermitDialogForm"></form>
</body>
<script type="text/javascript">
function formatState(value, row, index) {
	if(value > 0) {
		value = "已绑定";
	} else {
		value = "未绑定";
	}
	return value;
}
function divShowTip(_msg) {
	$.messager.show({
        msg: _msg,
        showType: 'fade',
        width:'200px',
        height:'40px',
        timeout: 700,
    	style:{
    		right:'',
    		background:'#FFF',
    		padding: 0,
    		//top:document.body.scrollTop+document.documentElement.scrollTop,
    		bottom:''
		}
	});
}
function loadingTipShow() {           
	$("<div class=\"datagrid-mask\" style='z-index:10000'></div>").css(
				{  
					display: "block", 
					width: "100%",
					height:  $(document).height() 
				}
			).appendTo("body");            
	$("<div class=\"datagrid-mask-msg\" style='z-index:10000'></div>").html("正在处理，请稍候...").appendTo("body").css(
				{ 
					display: "block", 
					left: ( $(document.body).outerWidth(true) - 190) / 2, top: ($(document).height() - 45) / 2 
				}
	);       
}
function loadingTipClose() {            
	$("div[class='datagrid-mask']").remove();            
	$("div[class='datagrid-mask-msg']").remove();       
}
</script>
</html>