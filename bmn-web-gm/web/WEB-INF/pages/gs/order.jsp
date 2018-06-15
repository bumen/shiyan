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
      		{field:'orderId',title:'订单ID'},
			{field:'userId',title:'用户ID'},
      		{field:'roleId',title:'角色ID'},
			{field:'propId',title:'商品ID', sortable:true},
			{field:'actualPrice',title:'充值RMB', sortable:true},
			{field:'serviceId',title:'渠道', width:120, formatter:formatChannel},
			{field:'channelId',title:'推广渠道ID', width:120}
			,{field:'createTime',title:'充值时间', width:120, formatter: formatCreateTime, sortable:true}
			//, {field:'operate',title:"操作", formatter:formatOperate}
  	 ];
  	 
  	  $('#channelCode').combobox({
		 url:'channel/queryTree',
		 method:'get',
		 valueField:'id',
		 textField:'text',
		 panelHeight:'auto'
	 });
  	 
	 $('#dataGrid').datagrid({
				singleSelect: true,
				rownumbers:true,
				fit:true,
				remoteSort:false,
				fitColumns:true,
				url:'pay/list',
				queryParams:{
					'roleId':-1
				},
				toolbar:'#dataGrid_tb',
				pagination:true,
				//sortName:'propId',
				columns:getDataGridColumns(columnHref),
				pageSize:50
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
						url:"pay/del",
						data:{
							'orderIndex': rowId
						},
						success:function(data){
							if(data.result) {
								$('#dataGrid').datagrid('deleteRow', parseInt(rowId));
								$('#dataGrid').datagrid('reload');
								//$('#dataGrid').datagrid('sort', {
								//		sortName: 'areaId',
								//		sortOrder: 'asc'
								//});
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

function formatCreateTime(value, row, index) {
	if(typeof(value) != "undefined" && value != "") {
		return formatDate(value);
	}

	return value;
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
	
	return key;
}

function onClickQuery() {
	var roleId = $('#dataGrid_roleId').textbox("getValue");
	if(roleId == "") return;
	
	 $('#dataGrid').datagrid('load', {
				'roleId':roleId
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
					<td style="padding-left:21px;">
						<input id="dataGrid_roleId" class="easyui-textbox" data-options="prompt:'请输入角色ID'" style="width: 150px;float:right">
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" data-options="plain:true,onClick:onClickQuery">查询</a>
					</td>
	
				</tr>
			</table>
		</div>
	</div>
	
	<input class="easyui-combobox" id="channelCode" data-options="hidden:true"></input>
</body>
</html>