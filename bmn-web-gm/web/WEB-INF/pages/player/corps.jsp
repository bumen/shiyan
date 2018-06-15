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
		{field:'roleId',title:'角色ID'},
		{field:'roleName',title:'角色名'},
		{field:'job',title:'职位'}
	 ];
	 
	  $('#areaList').combobox({
		 url:'/gm/sys/area/comboList',
		 method:'get',
		 valueField:'id',
		 textField:'text',
	 });
	 
	 $('#treeGrid').datagrid({
			rownumbers:true,
			fit:true,
			remoteSort:false,
			singleSelect: true,
			fitColumns:true,
			method:'get',
			toolbar:'#dataGrid_tb',
			columns:getDataGridColumns(columnHref),
			pagination:false
		});
		
	 
});

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

function queryIdBtnClick() {
	queryBtnClick(0);
}

function queryNameBtnClick() {
	queryBtnClick(1);
}

function queryBtnClick(type) {
	var areaId = $('#areaList').combobox('getValue');
	if(type == 1) {
		if(areaId == "")
			return;
	}
		
		
	var corpsKey = $('#dataGrid_corpsKey').textbox('getValue');
	if(corpsKey == "")
		return;
		
	$.ajax({
		url:"corps/query",
		type:"post",
		data: {
			'areaId':areaId,
	 		'corpsKey':corpsKey,
	 		'type': type
		},
		success:function(retData){
			var result = JSON.parse(retData);
			if(typeof(result) == 'object') {
				var data = result.data;
				if(result.success) {
					$('#dataGrid_corpsId').numberbox('setValue', data.corpsId); 
					$('#dataGrid_corpsName').textbox('setValue', data.corpsName); 
					$('#dataGrid_corpsLevel').numberbox('setValue', data.corpsLevel); 
					$('#dataGrid_corpsActive').numberbox('setValue', data.corpsActive); 
					$('#dataGrid_corpsRoleSize').textbox('setValue', "(" + data.corpsRoleSize + "/" + data.corpsRoleMax + ")"); 
					
					var roles = data.roles;
					$('#treeGrid').datagrid('loadData', roles);
					
					divShowTip("查询成功！");
					return;
				}
				
				divShowTip(data);
				return;
			} 
			
			divShowTip("数据提交失败！");
		},error:function() {
			loadingTipClose();
			divShowTip("数据提交失败！");
		}
	});
	
}
</script>
</head>


<body>
	<table id="treeGrid" class="easyui-datagrid" ></table>
	<div id="dataGrid_tb" style="padding: 5px; height: auto">
		<div style="margin-top: 5px">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td style="padding-left:21px;">
						分区：<input class="easyui-combobox" id="areaList" data-options="" style="height:28px;width:100px;"></input>
						<input id="dataGrid_corpsKey" class="easyui-textbox" data-options="prompt:'请输入军团名称或ID'" style="height:28px;width: 150px;float:right">
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" data-options="plain:true, onClick:queryIdBtnClick">ID查询</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" data-options="plain:true, onClick:queryNameBtnClick">名称查询</a>
					</td>
				</tr>
				<tr id="corpsInfoPanel" style="margin-top:15px;">
					<td style="padding-top:15px;padding-left:21px;">
						军团ID：<input id="dataGrid_corpsId" class="easyui-numberbox" data-options="readonly:true" style="height:28px;width: 150px;float:right"/>
						军团名称：<input id="dataGrid_corpsName" class="easyui-textbox" data-options="readonly:true" style="height:28px;width: 150px;float:right"/>
						军团等级：<input id="dataGrid_corpsLevel" class="easyui-numberbox" data-options="readonly:true" style="height:28px;width: 150px;float:right"/>
						军团人数：<input id="dataGrid_corpsRoleSize" class="easyui-textbox" data-options="readonly:true" style="height:28px;width: 150px;float:right"/>
						军团活跃：<input id="dataGrid_corpsActive" class="easyui-numberbox" data-options="readonly:true" style="height:28px;width: 150px;float:right"/>
					</td>
				</tr>
			</table>
		</div>
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
							url:"mail/delete",
							data:{
								'roleId': rowNode.reciverId,
								'mailId': rowNode.id
							},
							success:function(data){
								if(data == "1") {
									$('#treeGrid').datagrid('deleteRow', parseInt(rowId));
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

function formatterCorpsRoleSize(value, row, index) {
	var max = row.corpsRoleMax;
	return "(" + value + "/" + max + ")";
}


</script>
</html>