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
	          		{field:'uin',title:'账号', ftype:"text"},
	          		{field:'roleid',title:'角色名', ftype:"text", formatter:formatRole},
	          		{field:'channels',title:'渠道', ftype:"text", formatter:formatChannel},
	          		{field:'isOpen',title:'状态', ftype:"text", formatter:formatIsOpen},
	          		{field:'operate',title:'操作', ftype:"opr", formatter:formatOperate}
	          	 ];
	          	 
	          	 
  	 $('#bindRoleDialog_select').combobox({
		 url:'role/selectTree',
		 method:'get',
		 width:250,
		 panelHeight:490
	 });
	 
	  $('#bindRoleChannelDialog_select').combobox({
		 url:'channel/queryTree',
		 method:'get',
		 valueField:'id',
		 textField:'text',
		 multiple:true
	 });
	 
	 $('#dataGrid').datagrid({
				singleSelect: true,
				showFooter:true,
				rownumbers:true,
				fit:true,
				fitColumns:true,
				url:'user/query',
				//toolbar:'#dataGrid_tb',
				pagination:true,
				columns:getDataGridColumns(columnHref),
				pageSize:10,
				//onDblClickRow:onDbClickRow,
				onLoadSuccess:function(data){
					//footerPager();
				},
				onBeforeLoad: function(param) {
					//footerPager();
				},
				onLoadError:function() {
				}
			});
	
	
	
	
	 $('#bindRoleDialog').dialog({
			width:300,  
		    title:"分配角色",
		    height:600,
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
					onBindRoleDialogSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#bindRoleDialog').dialog('close', true);
				}
			}],
			onClose:function(){
				//$('#leafAddDialogForm').form('clear');
			}
	 });
	 
	 $('#bindRoleChannelDialog').dialog({
			width:300,  
		    title:"分配渠道",
		    height:'auto',
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
					onBindRoleChannelDialogSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#bindRoleChannelDialog').dialog('close', true);
				}
			}],
			onClose:function(){
				//$('#leafAddDialogForm').form('clear');
			}
	 });
});

function createOperateRow() {
	var tableHtml = [];
	var opr1 = '启用<input type="radio" name="openUser" value="1"/> | 禁用<input type="radio" name="openUser" value="0"/>';
	tableHtml.push(opr1);
	return tableHtml.join("");
}
function getDataGridColumns(hrefArray) {
	var columns = [];
	//columns.push({field:'checkAll',checkbox:true});
	$(hrefArray).each(function(index){
		var c = hrefArray[index];
		c.width = 100;
		c.align = "center";
		columns.push(c);
	});
	return [columns];
}
function formatRole(value, row, index) {
	var _v = value;
	if(typeof(value) != "undefined" && value != "") {
		_v = getRoleName(value);
	} else {
		_v = "";
	}
	return _v;
}

function getRoleName(key) {
	var datas = $('#bindRoleDialog_select').combobox('getData');
	for(i in datas) {
		var data = datas[i];
		if(data.id == key) {
			return data.text;
		}
	}
	return "";
}

function formatChannel(value, row, index) {
	var _v = value;
	if(typeof(value) != "undefined" && value.length != 0) {
		_v = getChannelName(value);
	} else {
		_v = "";
	}
	return _v;
}

function getChannelName(key) {
	var result = [];
	var datas = $('#bindRoleChannelDialog_select').combobox('getData');
	
	for(v in key) {
		for(i in datas) {
			var data = datas[i];
			if(data.id == key[v]) {
				result.push(data.text);
			}
		}
	}
	
	return result.join(",");
}

function formatOperate(value, row, index) {
	   var modifyButton = createCustomGridButton(index, "edit", "edit", "分配角色");
	   var channelButton = createCustomGridButton(index, "channel", "edit", "分配渠道");
	   var isOpen = row.isOpen;
	   var openButton ="";
	   var delButton = "";
	  
	   var isSelf = row.uin == '${sessionScope._USER_.uin}';
	   if(!isSelf) {
		   if(isOpen != 0){
			   openButton = createCustomGridButton(index, "isOpen", "close", "禁用");
		   } else {
			   openButton = createCustomGridButton(index, "isOpen", "open", "启用");
		   }
		   delButton = createCustomGridButton(index, "cancel", "cancel", "删除");
	   }
	   
	   return modifyButton +"&nbsp&nbsp" + channelButton +"&nbsp&nbsp" + openButton + "&nbsp&nbsp" + delButton;
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
function isOpenOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	$.messager.confirm("操作提示", "您确定要执行操作吗？", function (data) {
        if (data) {
       	 var jsonStr = {};
       	 jsonStr.id = rowNode.id;
       	 if(rowNode.isOpen != 0) {
       	 	jsonStr.isOpen = 0;
       		rowNode.isOpen = 0;
       	 }else {
       		jsonStr.isOpen = 1;
       		rowNode.isOpen = 1;       		 
       	 }
       	 $.ajax({
					type:"POST",
					url:"user/open",
					data:{
						'uin': rowNode.uin,
						'isOpen':jsonStr.isOpen
					},
					success:function(msg){
						if(msg == "1") {
							$('#dataGrid').datagrid('updateRow',{
								index: parseInt(rowId),
								row: rowNode
							}); 
							divShowTip("数据已更新！");
						} else {
							divShowTip("数据更新失败！");
						}
					}
				});
        }
    });
}
function editOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	document.getElementById("bindRoleDialog").setAttribute("rowId", rowId);
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	
	if(rowNode.roleid > 0 ){
		$('#bindRoleDialog_select').combobox("select", rowNode.roleid);
	}
	$('#bindRoleDialog').dialog('open', true);
}

function channelOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	document.getElementById("bindRoleChannelDialog").setAttribute("rowId", rowId);
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	
	var channelArray = rowNode.channels;
	
	if(channelArray){
		$('#bindRoleChannelDialog_select').combobox("setValues", channelArray);
	}
	$('#bindRoleChannelDialog').dialog('open', true);
}

function cancelOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	var Uin = rowNode.uin;
	if(Uin){
		$.messager.confirm("操作提示", "您确定要执行删除操作吗？", function (data) {
	        if (data) {
	       	 		$.ajax({
						type:"POST",
						url:"user/del",
						data:{
							uin: Uin
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

function onBindRoleChannelDialogSubmit() {
	var rowId = document.getElementById("bindRoleChannelDialog").getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	
	var channelIds = $('#bindRoleChannelDialog_select').combobox('getValues');
	
	loadingTipShow();
	$.ajax({
		type:"POST",
		url:"user/bindChannel",
		dataType:'json',
		data: {
			'uin':rowNode.uin,
			'codes':channelIds.join(",")
		},
		success:function(msg){
			loadingTipClose();
			var result = msg.result;
			if(result == "1") {
				divShowTip("绑定成功！");
				$('#dataGrid').datagrid('reload', {});
				$('#bindRoleChannelDialog').dialog('close', true);
			} else {
				divShowTip("绑定失败！");
			}
		
		},error:function(XMLHttpRequest, textStatus, errorThrown) {
			divShowTip("绑定失败！");
			loadingTipClose();
		} 
	});
}

function onBindRoleDialogSubmit() {
	var rowId = document.getElementById("bindRoleDialog").getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	
	var s_roleId = $('#bindRoleDialog_select').combobox('getValue');
	if(s_roleId == "" || s_roleId == rowNode.roleid) return;
	var jsonObj = {};
	jsonObj.uin = rowNode.uin;
	jsonObj.roleId = s_roleId;
	loadingTipShow();
	$.ajax({
		type:"POST",
		url:"user/bind",
		dataType:'json',
		data: {
			'uin':rowNode.uin,
			'roleId':s_roleId
		},
		success:function(msg){
			loadingTipClose();
			var result = msg.result;
			if(result == "1") {
				divShowTip("绑定成功！");
				rowNode.roleid = s_roleId;
				$('#dataGrid').datagrid('updateRow', {
					index : parseInt(rowId),
					row : rowNode
				});
				$('#bindRoleDialog').dialog('close', true);
			} else {
				divShowTip("绑定失败！");
			}
		
		},error:function(XMLHttpRequest, textStatus, errorThrown) {
			divShowTip("绑定失败！");
			loadingTipClose();
		} 
	});

}
</script>
</head>
<body>
	<table id="dataGrid" class="easyui-datagrid" style="width: 700px; height: 250px" > </table>
	
	<!-- addDialog -->
	<div id="bindRoleDialog" style="padding:10px;">
		<input id="bindRoleDialog_select"/>  
	</div>
	
	<div id="bindRoleChannelDialog" style="padding:10px;">
		<input id="bindRoleChannelDialog_select"/>  
	</div>
</body>
<script type="text/javascript">
function formatIsOpen(value, row, index) {
	if(value > 0)
		value = "已启用";
	else 
		value = "未启用";
	return value;
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
</script>
</html>