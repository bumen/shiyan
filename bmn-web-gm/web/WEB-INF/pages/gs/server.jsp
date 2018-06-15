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
      		{field:'id',title:'',  ftype:"text", width:50, hidden:true},
      		{field:'areaId',title:'大区',  ftype:"select", width:50,fadd:true, furl:"'area/comboList'" ,sortable:true},
			{field:'ip',title:'外网IP', ftype:"text",fadd:true, width:120, editor:"textbox"},
			{field:'lanIp',title:'内网IP', ftype:"text",fadd:true, width:120, editor:"textbox"},
			{field:'port',title:'端口',  ftype:"int", fadd:true,width:120},
			{field:'active',title:'状态',  ftype:"text", width:120, formatter:formatActive},
			{field:'userCount',title:'在线人数', ftype:"text", width:120},
			{field:'operate',title:"操作", formatter:formatOperate}
  	 ];
  	 
	 $('#dataGrid').datagrid({
				singleSelect: true,
				rownumbers:true,
				customObj:{lastEditIndex:-1, editing:false, lastEditIp:"", editField:""},//自定义属性，lastEditIndex上次编辑的行index ,// editing 当前是否打开行编辑, editFiled:当前编辑列名
				fit:true,
				remoteSort:false,
				fitColumns:true,
				url:'area/ipList',
				toolbar:'#dataGrid_tb',
				pagination:true,
				sortName:'areaId',
				columns:getDataGridColumns(columnHref),
				pageSize:50,
				//onDblClickRow:onDbClickRow,
				onLoadSuccess:function(data){
					//footerPager();
				},
				onBeforeLoad: function(param) {
					//footerPager();
				},
				onLoadError:function() {
				}
				,onClickCell: onClickCell
			});
			
	 $.extend($.fn.datagrid.methods, {
			editCell: function(jq, param){
				return jq.each(function(){
					var node = $(this);
					var opts = node.datagrid('options');
					var fields = node.datagrid('getColumnFields',true).concat(node.datagrid('getColumnFields'));
					for(var i=0; i<fields.length; i++){
						var col = node.datagrid('getColumnOption', fields[i]);
						col.editor1 = col.editor;
						if (fields[i] != param.field){
							col.editor = null;
						}
					}
					node.datagrid('beginEdit', param.index);
					for(var i=0; i<fields.length; i++){
						var col = node.datagrid('getColumnOption', fields[i]);
						col.editor = col.editor1;
					}
				});
			}
		});
	
	
	 document.getElementById('addDialogForm').innerHTML = creatDialogFormItems('Ip', 'Add', columnHref);
	 $('#addDialog').dialog({
			width:550,  
		    title:"添加服务器",
		    height:200,
		    cache: false,
		    minimizable:false,
		    maximizable:false,
		    collapsible:true,
		    closed:true,	//初始化时，关闭
		    modal:true,
		    content:$('#addDialogForm'),
		    buttons:[{
				text:'提交',
				iconCls:'icon-ok',
				handler:function(){
					addDialogSubmit();
				}
			},{
				text:'关闭',
				iconCls:'icon-cancel',
				handler:function(){
					$('#addDialog').dialog('close', true);
				}
			}],
			onOpen: function(){
				var tableId = "IpAddDialogForm_table";
				$('#'+tableId + "_areaId").combobox('reload');
			},
			onClose:function(){
				$('#addDialogForm').form('clear');
			}
	 });
	 
});

function endEditing(){
		var opt = $('#dataGrid').datagrid('options');
		var custom = opt.customObj;
		var oldIndex = custom.lastEditIndex;
		if (oldIndex == -1){return true}
		
		if ($('#dataGrid').datagrid('validateRow', oldIndex)){
			$('#dataGrid').datagrid('endEdit', oldIndex);
			custom.lastEditIndex = -1;
			return true;
		} else {
			return false;
		}
}

function onClickCell(index, field, value) {
	var opt = $('#dataGrid').datagrid('options');
	var custom = opt.customObj;
	
	if(custom.editing) {
		var oldIndex = custom.lastEditIndex;
		var oldEditField = custom.editField;
		if ($('#dataGrid').datagrid('validateRow', oldIndex)) {
		
			var nameEditor = $('#dataGrid').treegrid('getEditor', {id:oldIndex, field: oldEditField});
			if(nameEditor)  {
				var newIp = $(nameEditor.target).textbox('getValue');
				if(!newIp || newIp == "")
					return;
					
				if(custom.lastEditIp != newIp) {
					var rowNode = $('#dataGrid').datagrid('getRow', {"id":"dataGrid", 'index':oldIndex});
					sendNewAreaIp(rowNode.id, newIp, oldEditField);
				}
			}
		}
	}
	
	if (endEditing()){
		if(field == "ip" || field == "lanIp") {
			$('#dataGrid').datagrid('selectRow', index)
					.datagrid('editCell', {index:index,field:field});
					
			custom.editing = true;
			custom.lastEditIndex = index;
			custom.lastEditIp = value; 
			custom.editField = field;
		} else {
			custom.editing = false;
			custom.lastEditIndex = -1;
			custom.lastEditIp = "";
			custom.editField = "";
		}
	}
	
	return;
	//当前正处行编辑状态
	if(custom.editing) {
		//且当前选中其它非编辑行时，则取消当前行编辑状态，并保存数据
		var oldIndex = custom.lastEditIndex;
		var oldEditField = custom.editField;
		if(oldIndex != index || field != "ip" || field != "lanIp") {
			
			var nameEditor = $('#dataGrid').treegrid('getEditor', {id:oldIndex, field: oldEditField});
			if(nameEditor == null) return;
			
			var newIp = $(nameEditor.target).textbox('getValue');
			if(!newIp || newIp == "")
				return;
				
			if(custom.lastEditIp != newIp) {
				var rowNode = $('#dataGrid').datagrid('getRow', {"id":"dataGrid", 'index':oldIndex});
				sendNewAreaIp(rowNode.id, newIp, oldEditField );
			}
			
			
			if(field == "ip" || field == "lanIp") {
				begainEditField(custom, field, index, value);
			} else {
				custom.editing = false;
				custom.lastEditIndex = -1;
				custom.lastEditIp = "";
				custom.editField = "";
				$('#dataGrid').datagrid('endEdit', oldIndex);
			}
		}
		
	} else {
		begainEditField(custom, field, index, value);
	}
}

function begainEditField(custom, field, index, value) {
		var oldEditor = null;
		if(field == "ip") {
			var col = $('#dataGrid').datagrid('getColumnOption', "lanIp");
			oldEditor = col.editor;
			col.editor = null;
		}
		
		if(field == "lanIp") {
			var col = $('#dataGrid').datagrid('getColumnOption', "ip");
			oldEditor = col.editor;
			col.editor = null;
		}
		
		if(oldEditor) {
			if($('#dataGrid').datagrid('validateRow', index)) {
				custom.editing = true;				
				custom.lastEditIndex = index;
				custom.lastEditIp = value; 
				custom.editField = field;
				$('#dataGrid').datagrid("beginEdit", index);
			}
		}
		
		if(field == "ip") {
			var col = $('#dataGrid').datagrid('getColumnOption', "lanIp");
			col.editor = oldEditor;
		}
		
		if(field == "lanIp") {
			var col = $('#dataGrid').datagrid('getColumnOption', "ip");
			col.editor = oldEditor;
		}
}

function sendNewAreaIp(id, newIp, editField) {
	$.ajax({
			type:"POST",
			url:"area/ipUpdate",
			data:{
				'id':id,
				'field':editField,
				'ip':newIp
			},
			success:function(data){
				var result = JSON.parse(data);
				if(result && result.success) {
					divShowTip("更新成功！");
				} else {
					divShowTip("更新失败！");
				}
			}
	});
}

function addDialogSubmit() {
	var tableId = "IpAddDialogForm_table";
	
	var ip = $('#'+tableId + "_ip").textbox("getValue");
	if(!ip || ip == "")
		return;
		
	var lanIp = $('#'+tableId + "_lanIp").textbox("getValue");
	if(!lanIp || lanIp == "")
		return;
	var port = $('#'+tableId + "_port").numberbox("getValue");
	if(!port || port < 0)
		return;
		
	var areaId = $('#'+tableId + "_areaId").combobox('getValue');
	if(!areaId) 
		return;
		
	$.ajax({
			type:"POST",
			url:"area/ipAdd",
			data:{
				'areaId': areaId, 
				'ip': ip,
				'lanIp': lanIp,
				'port':port
			},
			success:function(data){
				var result = JSON.parse(data);
				if(result && result.success) {
					var areaIp = result.obj;
					$('#addDialog').dialog('close', true);
					
					$('#dataGrid').datagrid('appendRow', areaIp); 
					$('#dataGrid').datagrid('sort', {
							sortName: 'areaId',
							sortOrder: 'asc'
					}); 
					
					divShowTip("数据已更新！");
				} else {
					divShowTip("数据更新失败！");
				}
			}
		});
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
  return createCustomGridButton(index, "cancel", "cancel", "删除");
}

function cancelOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	if(rowNode){
		$.messager.confirm("操作提示", "您确定要执行删除操作吗？", function (data) {
	        if (data) {
	       	 		$.ajax({
						type:"POST",
						url:"area/ipDel",
						data:{
							'id':rowNode.id
						},
						success:function(data){
							if(data.result) {
								$('#dataGrid').datagrid('deleteRow', parseInt(rowId));
								$('#dataGrid').datagrid('reload');
								$('#dataGrid').datagrid('sort', {
										sortName: 'areaId',
										sortOrder: 'asc'
								});
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


function onClickAddIp() {
	 $('#addDialog').dialog('open', true);
}
</script>
</head>
<body>
	<table id="dataGrid" class="easyui-datagrid" style="width: 700px; height: 250px" > </table>
	<div id="dataGrid_tb" style="padding: 5px; height: auto">
		<div style="margin-top: 5px">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td style="padding-left:21px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true,onClick:onClickAddIp">添加</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<!-- addDialog -->
	<div id="addDialog" style="padding:10px;"></div>
	<form id="addDialogForm"></form>
</body>
<script type="text/javascript">
function formatActive(value, row, index) {
	if(value)
		return "start";
	else 
		return "stop";
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