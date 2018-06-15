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
		{field:'serviceId',title:'渠道', ftype:"text", formatter: formatChannel},
		{field:'name',title:'名称', ftype:"text"},
		{field:'title',title:'标题', ftype:"text"},                      
		{field:'version',title:'版本', ftype:"text", formatter: formatVersion},                      
		{field:'orderId',title:'排序', ftype:"text", formatter:formatOrder},                      
		{field:'operate',title:'操作', ftype:"opr", formatter:formatOperate}
	 ];
	 
	  $('#channelCode').combobox({
		 url:'channel/queryTree',
		 method:'get',
		 valueField:'id',
		 textField:'text'
	 });
	 
	 $('#areaVersion').combobox({
		 url:'version/options',
		 method:'get',
		 valueField:'id',
		 textField:'text',
		 panelHeight:'auto'
	 });
	
	$('#dataGrid').datagrid({
		singleSelect: true,
		showFooter:true,
		rownumbers:true,
		fit:true,
		fitColumns:true,
		url:'notice/query',
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
	
	 $('#addDialog').dialog({
			width:850,  
		    title:"公告",
		    height:590,
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
					addSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#addDialog').dialog('close', true);
				}
			}],
			onClose:function(){
				$('#areaAddDialogForm').form('clear');
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

function formatOrder(value, row, index) {
		//return value;
	   var openButton = createCustomGridButton(index, "downMove", "close", "下移");
	   var delButton = createCustomGridButton(index, "upMove", "open", "上移");
	   if(index == 0) {
	   	delButton = "";
	   }
	   
	   var rows = $('#dataGrid').datagrid('getRows');
	   if(index == rows.length -1) {
	   		openButton = "";
	   }
	  
	   return delButton  + "&nbsp&nbsp" + openButton;
	
}
function formatOperate(value, row, index) {
	   var  editButton = createCustomGridButton(index, "edit", "edit", "查看/修改");
	   var  delButton = createCustomGridButton(index, "cancel", "cancel", "删除");
	   return editButton + " &nbsp;&nbsp;" + delButton;
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
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	var id = rowNode.id;
	if(rowNode) {
		$('#name').textbox('setValue', rowNode.name);	
		$('#title').textbox('setValue', rowNode.title);	
		$('#content').textbox('setValue', rowNode.content);	
		$('#channelCode').combobox("setValue", rowNode.serviceId);
		$('#areaVersion').combobox("setValue", rowNode.version);
		$('#addDialog').dialog({
		    buttons:[{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					editSubmit(id);
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#addDialog').dialog('close', true);
				}
			}]
	 	});
	 
		$('#addDialog').dialog('open', true);
	}
}

function downMoveOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	var index = $('#dataGrid').datagrid('getRowIndex', rowNode);
	
	var downIndex = index + 1;
	var rows = $('#dataGrid').datagrid('getRows');
	if(downIndex >= rows.length)
		return;
		
	var row = rows[downIndex];
	
	$.ajax({
		type:"POST",
		url:"notice/move",
		data:{
			'downId':rowNode.id,
			'upId'	: row.id
		},
		success:function(msg){
			if(msg == "1") {
				$('#dataGrid').datagrid('reload', {});
				divShowTip("移动成功！");
			} else {
				divShowTip("移动失败！");
			}
		}
	});
}

function upMoveOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	var index = $('#dataGrid').datagrid('getRowIndex', rowNode);
	
	var upIndex = index - 1;
	var rows = $('#dataGrid').datagrid('getRows');
	if(upIndex < 0) 
		return;
		
	var row = rows[upIndex];
	
	$.ajax({
		type:"POST",
		url:"notice/move",
		data:{
			'downId': row.id,
			'upId'	: rowNode.id
		},
		success:function(msg){
			if(msg == "1") {
				$('#dataGrid').datagrid('reload', {});
				divShowTip("移动成功！");
			} else {
				divShowTip("移动失败！");
			}
		}
	});
}

function cancelOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	var id = rowNode.id;
	if(id){
		$.messager.confirm("操作提示", "您确定要执行删除操作吗？", function (data) {
	        if (data) {
	       	 		$.ajax({
						type:"POST",
						url:"notice/del",
						data:{
							'id': id
						},
						success:function(msg){
							if(msg == "1") {
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

function addSubmit() {
	var name = $('#name').textbox('getValue');	
	var title = $('#title').textbox('getValue');	
	var content = $('#content').textbox('getValue');	
	var channelCode = $('#channelCode').combobox("getValue");
	var areaVersion = $('#areaVersion').combobox("getValue");
	if(name == "" || title == "" || content == "") {
		divShowTip("名称-标题-内容 为必填项");
		return;
	}
	
	if(areaVersion == "") {
		divShowTip("请选择版本");
		return; 
	}
	
	$.ajax({
		type:"POST",
		url:"notice/add",
		data:{
			'name': name,
			'title':title,
			'content':content,
			'channelCode':channelCode,
			'areaVersion':areaVersion
		},
		success:function(msg){
			if(msg == "1") {
				$('#dataGrid').datagrid('reload',{}); 
				divShowTip("添加成功！");
				$('#addDialog').dialog('close', true);
			} else {
				divShowTip(msg);
			}
		}
	});
}

function editSubmit(id) { 
	var name = $('#name').textbox('getValue');	
	var title = $('#title').textbox('getValue');	
	var content = $('#content').textbox('getValue');	
	var channelCode = $('#channelCode').combobox("getValue");
	var areaVersion = $('#areaVersion').combobox("getValue");
	if(name == "" || title == "" || content == "") {
		divShowTip("名称-标题-内容 为必填项");
		return;
	}
	
	if(areaVersion == "") {
		divShowTip("请选择版本");
		return; 
	}
	
	$.ajax({
		type:"POST",
		url:"notice/edit",
		data:{
			'id': id,
			'name': name,
			'title':title,
			'content':content,
			'channelCode':channelCode,
			'areaVersion':areaVersion
		},
		success:function(msg){
			if(msg == "1") {
				$('#dataGrid').datagrid('reload',{}); 
				divShowTip("修改成功！");
				$('#addDialog').dialog('close', true);
			} else {
				divShowTip(msg);
			}
		}
	});
}

function onClickAdd() {
		$('#addDialog').dialog({
		    buttons:[{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					addSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#addDialog').dialog('close', true);
				}
			}]
	 	});
	$('#addDialog').dialog('open', true);
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

function formatVersion(value, row, index) {
	var _v = value;
	if(typeof(value) != "undefined" && value  != "") {
		_v = getVersionName(value);
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
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true,onClick:onClickAdd">添加</a>
					</td>
	
				</tr>
			</table>
		</div>
	</div>
	
	<!-- addDialog -->
	<div id="addDialog" style="padding:10px;">
		<form id="areaAddDialogForm" method="post">
			<table cellpadding="5">
					<tr>
		    			<td>渠道:</td>
		    			<td>
		    				<input class="easyui-combobox" id="channelCode" data-options="" style="height:28px;width:200px;"></input>
						</td>
		    		</tr>
		    		<tr>
		    			<td>版本:</td>
		    			<td>
		    				<input class="easyui-combobox" id="areaVersion" data-options="" style="height:28px;width:200px;"></input>
						</td>
		    		</tr>
					<tr>
		    			<td>名称:</td>
		    			<td>
		    				<input class="easyui-textbox" id="name" data-options="" style="height:28px;width:200px;"></input>
						</td>
		    		</tr>
		    		<tr>
		    			<td>标题:</td>
		    			<td>
		    				<input class="easyui-textbox" id="title" data-options="" style="height:28px;width:200px;"></input>
		    			 </td>
		    		</tr>
		    		<tr>
		    			<td>内容:</td>
		    			<td>
		    				<input class="easyui-textbox" id="content" data-options="multiline:true" style="height:320px;width:700px;"></input>
		    			 </td>
		    		</tr>
			</table>
		</form>
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