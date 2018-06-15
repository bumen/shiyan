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
		{field:'name',title:'名称', ftype:"text"},
		{field:'code',title:'ID', ftype:"text"},             
		{field:'operate',title:'操作', ftype:"opr", formatter:formatOperate}
	 ];
	
	$('#dataGrid').datagrid({
		singleSelect: true,
		showFooter:true,
		rownumbers:true,
		fit:true,
		fitColumns:true,
		url:'channel/query',
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
	
});

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
		if(row.code == "-1")
			return;
			
	   var  delButton = createCustomGridButton(index, "cancel", "cancel", "删除");
	   return delButton;
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

function cancelOperateButton(node) {
	var rowId = node.getAttribute("rowId");
	$('#dataGrid').datagrid('selectRow', rowId);
	var rowNode = $('#dataGrid').datagrid('getSelected');
	var code = rowNode.code;
	if(code){
		$.messager.confirm("操作提示", "您确定要执行删除操作吗？", function (data) {
	        if (data) {
	       	 		$.ajax({
						type:"POST",
						url:"channel/del",
						data:{
							'code': code
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


function onClickAdd() {
	var name = $('#dataGrid_channel_name').textbox("getValue");
	var code = $('#dataGrid_channel_id').textbox("getValue");
	
	if(name == "") return;
	if(code == "") return;
	
	$.ajax({
		type:"POST",
		url:"channel/add",
		data:{
			'name': name,
			'code':code
		},
		success:function(retData){
			var result = JSON.parse(retData);
			if(typeof(result) == "object") {
				$('#dataGrid').datagrid('appendRow', result); 
				divShowTip("添加成功！");
				$('#dataGrid_channel_name').textbox("setValue", "");
				$('#dataGrid_channel_id').textbox("setValue", "");
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
						<input id="dataGrid_channel_name" class="easyui-textbox" data-options="prompt:'请输入渠道名'" style="width: 150px;float:right">--<input id="dataGrid_channel_id" class="easyui-textbox" data-options="prompt:'请输入渠道ID'" style="width: 150px;float:right">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true,onClick:onClickAdd">添加</a>
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