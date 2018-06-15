<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="/struts-tags" prefix="s"%>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.json.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/plugins/jquery.tabs.js"></script>
<style>
</style>
<script language="javascript">
var _oprid = '<s:property value="#request.logQ.oprid"/>';
$(function(){
	/*var columnHref = [
			{field:'name',title:'角色名称', ftype:"text", fedit:true, fquery:true},
			{field:'isGM',title:'是否是GM', ftype:"int", fedit:false, fquery:true}              
	] */
	//loadingTipShow();
	/*  $.ajax({
		type:"POST",
		url:"log_queryTitle",
		dataType:'json',
		data:{
			'logQ.oprid' : _oprid
		},
		success:function(msg){
			init(msg, _oprid);
		
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			//alert(textStatus)
		}
		
	});  */
	var columnHref = ${requestScope.title};
	 $('#dataGrid').datagrid({
			checkOnSelect: true,
			showFooter:true,
			rownumbers:true,
			fit:true,
			url:'log_query',
			queryParams:{
				'logQ.oprid':_oprid,
				'logQ.queryStr':'{"log_b_time":"' + getStringDate(new Date()) + '"}'
			},
			fitColumns:true,
			method:'get',
			toolbar:'#dataGrid_tb',
			pagination:true,
			columns:getDataGridColumns(columnHref),
			pageSize:50,
			onDblClickRow:onDbClickRow,
			onLoadSuccess:function(data){
				$('#queryDialog').dialog('close', true);
			},
			onLoadError:function() {
				divShowTip("数据提交失败！");
			},
			loadFilter:function(data) {
				var dataRows = data.rows;
				$.each(dataRows, function(i, row){
					var intDate = row.time;
					if(intDate != "") {
						var d = new Date();
						d.setTime(Number(intDate));
						row.time = getStringDate(d);
					}
				});
				return data;
			}
		});
	
	/* $('#dataGrid').datagrid('getPager').pagination({
        pageSize: 50,
        beforePageText: "转到",
        afterPageText: "共{pages}页",
        displayMsg: '当前显示从{from}到{to}共{total}记录',
        onBeforeRefresh: function (pageNumber, pageSize) {
            $(this).pagination('loading');
            $(this).pagination('loaded');
        }
    }); */
	//document.getElementById('infoDialog_form').innerHTML = 	createInfoFormItems(columnHref);
	/* $('#infoDialog').dialog({    
	    width:850,  
	    title:"详细信息",
	    height:600,
	   // content:$('#infoDialog_form'),
	    cache: false,
	    toolbar:'#infoDialog_tb',
	    minimizable:false,
	    maximizable:true,
	    collapsible:true,
	    closed:true,	//初始化时，关闭
	    modal:true,
	    buttons:[{
			text:'确定',
			iconCls:'icon-ok',
			handler:function(){
				$('#infoDialog').dialog('close', true);
			}
		},{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
				$('#infoDialog').dialog('close', true);
			}
		}],
		onClose:function(){
			$('#infoDialog_form').form('clear');
			onRemoveEditInfoDialog();
		}
	}); */
	document.getElementById('queryDialog_form').innerHTML = createQueryFormItems(columnHref);
	$('#queryDialog').dialog({    
	    width:850,  
	    title:"查询条件对话框",
	    height:600,
	    content:$('#queryDialog_form'),
	    cache: false,
	    minimizable:false,
	    maximizable:true,
	    collapsible:true,
	    closed:true,	//初始化时，关闭
	    modal:true,
	    buttons:[{
			text:'查询',
			iconCls:'icon-ok',
			handler:function(){
				submitDialogQuery(columnHref, _oprid);
			}
		},{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
				$('#queryDialog').dialog('close', true);
			}
		}],
		onClose:function(){
			//$('#queryDialog_form').form('clear');
		}
	}); 
	/*
	$('#editDialog').dialog({    
	    width:650,  
	    title:"修改对话框",
	    height:600,
	    cache: false,
	    minimizable:false,
	    maximizable:true,
	    collapsible:true,
	    closed:true,	//初始化时，关闭
	    modal:true,
	    buttons:[{
			text:'提交',
			iconCls:'icon-ok',
			handler:function(){
				submitToolsPanelForm();
				$('#editDialog').dialog('close', true);
			}
		},{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
				$('#editDialog').dialog('close', true);
			}
		}],
		onClose:function(){
			$('#editDialog_form').form('clear');
			$('#editDialog_toolsPanel_dataGrid').datagrid('loadData', []);
		}
	}); 
	
	
	//document.getElementById('editDialog_form').innerHTML = createEditFormItems(columnHref);
	
	$('#editDialog_toolsPanel_dataGrid').datagrid({
		checkOnSelect: true,
		showFooter:true,
		rownumbers:true,
		fit:true,
		//url:'datagrid_data1.json',
		fitColumns:true,
		method:'get',
		toolbar:'#editDialog_toolsPanel_dataGrid_tb',
		pagination:false,
		columns:[[
				{field:'edit_checkAll',checkbox:true},
				{field:'type',title:'类型',width:100,align:"center"},
				{field:'type_code',title:'类型Code',width:100,align:"center"},
				{field:'number',title:'数量',width:100,align:"center"},
				{field:'type_value',hidden:true},
		  ]],
	}); */
	
	$('#tools_Query_bdate').datetimebox().datetimebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
			return date <= d2;
		}
	});
	
	$('#tools_Query_edate').datetimebox().datetimebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate()+1);
			return date <= d2;
		}
	});
	
	$('#query_form_table_log_b_time').datetimebox().datetimebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
			return date <= d2;
		}
	});
	$('#query_form_table_log_e_time').datetimebox().datetimebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate()+1);
			return date <= d2;
		}
	});
	
});
function submitDialogQuery(columnHref, _oprid) {
	var tableId = "query_form_table";
	var qJson = {};
	$.each(columnHref, function(i, c){
		var qid = tableId + "_" + c.field;
		var value = $('#' + qid).val();
		if(c.field == "log_b_time" || c.field == "log_e_time") {
			value = $('#'+qid).datebox('getValue');
		}
		if(value != "")
			qJson[c.field] = value;
	});
	$('#dataGrid').datagrid({
		url:'log_query',
		queryParams:{
			'logQ.oprid':_oprid,
			'logQ.queryStr':$.toJSON(qJson)
		}
	});
	
}

function createInfoFormItems(hrefArray) {
	var tableHtml = [];
	var tableId = "info_form_table";
	tableHtml.push('<table id="'+tableId+'" width=100% border="0">');
	
	var count = 0;
	$(hrefArray).each(function(index){
		var c = hrefArray[index];
		var c_id = tableId + "_" + c.field;
		var ftype = c.ftype;
		if(count == 0) {
			tableHtml.push('<tr>');
		}
		count = count + 1;
		tableHtml.push('<td align="right" nowrap="nowrap" width="10%">' + c.title + ':</td>');
		tableHtml.push('<td  align="left" width="20%" style="text-align:left;">');
		var displayTip = "display:none;";
		if(c.fedit) {
			displayTip = "";
		}
		switch(ftype) {
			case "text":
				tableHtml.push('<input class="easyui-textbox"  fedit="'+c.fedit+'" type="text" id="'+c_id+'" name="'+c.field+'" data-options="required:false,editable:false,height:25,width:150px" />');
				//tableHtml.push('<font style="'+displayTip+'color:red">*<font>');
				break;
			case "int":
				tableHtml.push('<input class="easyui-numberbox"  fedit="'+c.fedit+'" type="text" id="'+c_id+'" name="'+c.field+'" data-options="required:false,editable:false,height:25,width:150px" />');
				//tableHtml.push('<font style="'+displayTip+'color:red">*<font>');
				break;
			case "date":
				tableHtml.push('<input class="easyui-datetimebox" fedit="'+c.fedit+'" id="'+c_id+'" name="'+c.field+'" data-options="required:false,showSeconds:false" value="=" style="width:150px"/>');
				break;
			case "textarea":
				count = 2;
				tableHtml.push('<input class="easyui-textbox" fedit="'+c.fedit+'" id="'+c_id+'" name="'+c.field+'" data-options="multiline:true" style="height: 500px; width:500px;"/>');
				break;
		}
		tableHtml.push('</td>');
		if(count == 2) {
			count = 0;
			tableHtml.push('</tr>');
		}
		
	});
	tableHtml.push('</table>');
	return tableHtml.join("");
}
function createQueryFormItems(hrefArray) {
	var tableHtml = [];
	var tableId = "query_form_table";
	tableHtml.push('<table id="'+tableId+'" width=100% border="0">');
	
	var count = 0;
	$(hrefArray).each(function(index){
		var c = hrefArray[index];
		var query = c.fquery;
		if(query) {
			var qid = tableId + "_" + c.field;
			
			if(count == 0) {
				tableHtml.push('<tr>');
			}
			count = count + 1;
			tableHtml.push('<td align="right" nowrap="nowrap" width="10%">' + c.title + ':</td>');
			tableHtml.push('<td  align="left" width="20%" style="text-align:left;">');
			var ftype = c.ftype;
			switch(ftype) {
				case "text":
					tableHtml.push('<input class="easyui-textbox" type="text" id="'+qid+'"  data-options="height:25,width:150" />');
					break;
				case "int":
					tableHtml.push('<input class="easyui-numberbox" type="text" id="'+qid+'"  data-options="height:25,width:150" />');
					break;
				case "date":
					tableHtml.push('<input class="easyui-datetimebox" id="'+qid+'"  data-options="" value="=" style="width:150px"/>');
					break;
				case "textarea":
					count = 2;
					tableHtml.push('<input class="easyui-textbox" id="'+qid+'"  data-options="multiline:true" style="height: 500px; width:500px;"/>');
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
function createEditFormItems(hrefArray) {
	var tableHtml = [];
	var tableId = "edit_form_table";
	tableHtml.push('<table id="'+tableId+'" width="100%" border="0">');
	
	var count = 0;
	$(hrefArray).each(function(index){
		var c = hrefArray[index];
		var fedit = c.fedit;
		if(fedit) {
			var ftype = c.ftype;
			var name = 'player.' + c.field;
			var _title = c.title;
			if(c.field != "name") {
				_title = '±' + _title;
			}
			if(count == 0) {
				tableHtml.push('<tr>');
			}
			count = count + 1;
			tableHtml.push('<td align="right" nowrap="nowrap" width="10%">' + _title + ':</td>');
			tableHtml.push('<td  align="left" width="20%" style="text-align:left;">');
			switch(ftype) {
				case "text":
					tableHtml.push('<input class="easyui-textbox" type="text" name="'+name+'" data-options="width:150,height:25" />');
					break;
				case "int":
					tableHtml.push('<input class="easyui-numberbox" type="text" name="'+name+'" data-options="width:150,height:25" />');
					break;
				case "date":
					tableHtml.push('<input class="easyui-datetimebox" name="'+name+'" data-options="showSeconds:false" value="=" style="width:150px"/>');
					break;
				case "textarea":
					count = 2;
					tableHtml.push('<input class="easyui-textbox" name="'+name+'" data-options="multiline:true" style="height: 500px; width:500px;"/>');
					break;
			}
			tableHtml.push('</td>');
			if(count == 2) {
				count = 0;
				tableHtml.push('</tr>');
			}
		}
	});
	tableHtml.push('<tr><td></td><td><input type="hidden" name="player.tools" id="edit_form_table_tools"/></td></tr>');
	tableHtml.push('</table>');
	return tableHtml.join("");
}
function getDataGridColumns(hrefArray) {
	var columns = [];
	columns.push({field:'checkAll',checkbox:true});
	$(hrefArray).each(function(index){
		var c = hrefArray[index];
		c.width = 100;
		c.align = "center";
		columns.push(c);
	});
	return [columns];
}

function onDbClickRow(index, rowData) {
	$('#infoDialog_form').form("load", rowData);
	$('#infoDialog').dialog("open",true);
	
}

function onEditInfoDialog() {
	var inputArray = $('#infoDialog_form').find('input[needReadonly="true"]');
	if(typeof(inputArray) != 'undefined' && inputArray.length > 0) {	;//防止多次点击
		return;
	}
	var inputArray = $('#infoDialog_form').find('input[id*="info_form_table"]');
	$.each(inputArray, function(i, node){
		var fedit = node.getAttribute("fedit");
		if(fedit == "true") {
			var nextArray = $(node).parent().find('input');
			$.each(nextArray, function(j, next){
				var r = next.getAttribute("readonly");
				if(typeof(r) == "string") {
					$(next).removeAttr("readonly");
					$(next).attr("needReadonly", true);
				}
			});
		}
	});
}
function onRemoveEditInfoDialog() {
	var inputArray = $('#infoDialog_form').find('input[needReadonly="true"]');
	$.each(inputArray, function(i, node){
		$(node).attr("readonly", "readonly");
		$(node).removeAttr("needReadonly");
	});
}

function onRemoveRow() {
	var rowArray = $('#dataGrid').datagrid('getChecked');
	if(rowArray) {
		if(rowArray.length > 0) 
			dialogTip(rowArray);
	}
}

function onClickQuery() {
	$('#queryDialog').dialog("open",true);
}

function dialogTip(rowArray) {
	$.messager.confirm("操作提示", "您确定要执行操作吗？", function (data) {
        if (data) {
       	 $.each(rowArray, function(i, row){
					var index = $('#dataGrid').datagrid('getRowIndex', row);
					$('#dataGrid').datagrid('deleteRow', index);
				});
        }
    });
}

function doSearch() {
	var name = $('#dataGrid_searchName').searchbox('getValue');
	if(name == "" || isNaN(name))return;
	var jsonObj = {};
	jsonObj.uid = name;
	jsonObj.log_b_time = getStringDate(new Date());
	
	$('#dataGrid').datagrid({
		url:'log_query',
		queryParams:{
			'logQ.oprid':_oprid,
			'logQ.queryStr':$.toJSON(jsonObj)
		}
	});
}

function simpleQuery() {
	var bdate = $('#tools_Query_bdate').datebox('getValue');
	var edate = $('#tools_Query_edate').datebox('getValue');
	if(bdate == "") return;
	var jsonObj = {};
	jsonObj.log_b_time = bdate;
	jsonObj.log_e_time = edate;
	
	$('#dataGrid').datagrid({
		url:'log_query',
		queryParams:{
			'logQ.oprid':_oprid,
			'logQ.queryStr':$.toJSON(jsonObj)
		}
	});
}

function onClickEdit() {
	$('#editDialog').dialog("open", true);
}

function onClickToolsAdd() {
	var typeText = $('#toolPanel_toolTypes').combobox("getText");
	var typeValue = $('#toolPanel_toolTypes').combobox("getValue");
	var typeCode = $('#toolPanel_typeCode').val();
	var num = $('#toolPanel_toolNum').val();
	if(typeCode == "" || num == "") return;
	var row = {type:typeText, type_code:typeCode, number:num, type_value:typeValue};
	
	var rows = $('#editDialog_toolsPanel_dataGrid').datagrid('getRows');
	var _index = -1;
	$.each(rows, function(i, _row){
		if(typeText == _row.type && typeCode == _row.type_code) {
			row.number = Number(num) + Number(_row.number);
			_index = $('#editDialog_toolsPanel_dataGrid').datagrid('getRowIndex', _row);
		}
	});
	if(_index >= 0) {
		$('#editDialog_toolsPanel_dataGrid').datagrid('deleteRow', _index);
	}
	$('#editDialog_toolsPanel_dataGrid').datagrid('appendRow', row);
	$('#editDialog_toolsPanel_dataGrid').datagrid('sort', 'type');
	$('#toolPanel_typeCode').textbox("setValue", "");
	$('#toolPanel_toolNum').textbox("setValue", "");
}

function onClickToolsRemove() {
	var rowArray = $('#editDialog_toolsPanel_dataGrid').datagrid('getChecked');
	if(rowArray) {
		if(rowArray.length > 0) {
			$(rowArray).each(function(i){
				var row = rowArray[i];
				var index = $('#editDialog_toolsPanel_dataGrid').datagrid('getRowIndex', row);
				$('#editDialog_toolsPanel_dataGrid').datagrid('deleteRow', index);
			});
		}
	}
}

function submitToolsPanelForm() {
	var array = [];
	var rows = $('#editDialog_toolsPanel_dataGrid').datagrid('getRows');
	$.each(rows, function(i, row){
		var _v = row.type_value + ":" + row.type_code + ":" + row.number;
		array.push(_v);
	});
	var tools = array.join(";");
	$('#edit_form_table_tools').val(tools);
	$('#editDialog_form').form('submit', 
		{
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
				}
				return isValid;	// 返回false终止表单提交
			},
			success: function(){
				$.messager.progress('close');	// 如果提交成功则隐藏进度条
			}
		});
}
	
</script>
</head>
<body >
	
	<table id="dataGrid" class="easyui-datagrid easyui-datafrid" style="width: 700px; height: 250px" > </table>
	<div id="dataGrid_tb" style="padding: 5px; height: auto">
		<div style="margin-top: 5px">
		<table cellspacing="0" cellpadding="0" border="0">
			<tr>
				 <td>
					<a href="#" class="easyui-linkbutton" iconCls="icon-search" data-options="plain:true,onClick:onClickQuery" >条件查询</a>
				</td>
				<td>
					<a href="#" class="easyui-linkbutton" iconCls="icon-remove" data-options="plain:true,onClick:onRemoveRow">删除</a> 
				</td>
				<td style="width:10px;">
					<div class="datagrid-btn-separator"></div>
				</td>
				<td>
					<input id="dataGrid_searchName" class="easyui-searchbox" data-options="prompt:'请输入玩家UID',searcher:doSearch" style="width: 150px;"/>
				</td>
				<td style="width:5px;padding:5px;">
					<div class="datagrid-btn-separator"></div>
				</td>
				<td>
					开始时间：<input id="tools_Query_bdate" class="easyui-datetimebox" data-options="" value="=" style="width: 150px;"/>
				</td>
				<td>
					&nbsp;结束时间：<input id="tools_Query_edate" class="easyui-datetimebox" data-options="" value="=" style="width: 150px;"/>
				</td>
				<td>
					&nbsp;<a href="#" class="easyui-linkbutton" iconCls="icon-search" data-options="plain:true,onClick:simpleQuery">查询</a> 
				</td>
			</tr>
		</table>
		</div>
	</div>
	
	<div id="delDialog"></div> 
	<!-- 详细信息对话框DOM -->
	<div id="infoDialog"></div>
	<form id="infoDialog_form" fit="true"></form>
	<div id="infoDialog_tb">
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true,onClick:onEditInfoDialog">修改</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-help',plain:true,onClick:onHelpDialog">帮助</a>
	</div>
	<!-- 查询对话框DOM -->
	<div id="queryDialog"></div>
	<form id="queryDialog_form" fit="true" ></form>
	
	<!-- 修改对话框DOM -->
	<div id="editDialog" style="padding:10px;">
		<div class="easyui-layout" data-options="fit:true">
			<div id="editDialog_infoPanel" data-options="region:'north',collapsible:false,split:true" style="height: 200px;padding: 10px"></div>
			<div id="editDialog_toolsPanel" data-options="region:'center',collapsible:false">
				<div id="editDialog_toolsPanel_dataGrid" fit="true" style="padding: 5px; height: auto"> </div>
			</div>
		</div>
	</div>
	<form id="editDialog_form" fit="true" method="post" action="GS_updatePlayer"></form>
	<div id="editDialog_toolsPanel_dataGrid_tb" style="padding: 5px; height: auto">
		<div style="margin-top: 5px">
			<select class="easyui-combobox" id="toolPanel_toolTypes" panelHeight="auto" style="width:100px">
				<option value="6">道具</option>
				<option value="7">英雄</option>
				<option value="12">符文</option>
				<option value="15">守护兽</option>
			</select>
			&nbsp;&nbsp;类型Code:<input class="easyui-textbox" id="toolPanel_typeCode" type="text" data-options="width:80,height:25" />
			&nbsp;&nbsp;数量:<input class="easyui-textbox" id="toolPanel_toolNum" type="text" data-options="width:80,height:25" />
			<a href="#" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true,onClick:onClickToolsAdd">添加</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-remove" data-options="plain:true,onClick:onClickToolsRemove">删除</a>
		</div>
	</div>
</body>
<script language="javascript">
function onHelpDialog() {
	var tip = [];
	tip.push("加<font color=red>*</font>字段可修改!");
	$.messager.alert('Info', tip.join('<br/>'));
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

function getStringDate(date) {
	var y = date.getFullYear();  
    var M = date.getMonth()+1;  
    var d = date.getDate();  
    var h = date.getHours();  
    var m = date.getMinutes();
    var s = date.getSeconds();
    return  y + '-' + (M<10?('0'+M):M) + '-' + (d<10?('0'+d):d) + ' ' + (h<10?('0'+h):h)+':' + (m<10?('0'+m):m)+':'  + (s<10?('0'+s):s);  
}
</script>
</html>