<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>

<style type="text/css"> 


div{
	display: block;
}

.dialog {
	background-color: #fff;
   	position:absolute;
   	left:0px;
   	top:0px;
   	margin-top:300px;
   	margin-left: 40%;
    z-index : 10001; 
    overflow: hidden;
}

.title {
    background-color: #000;
    position: relative;
    border-bottom: 1px solid #dedede;
    cursor: pointer;
    opacity : 0.75;
	padding : 3px;
}

.content {
	padding: 5px 15px;
}

.footer{
    position: relative;
	opacity : 0.75;
	padding : 3px;
}
.btnGroup {
    float: none;
    text-align: center;
}

.btnLink {
	width: 50px;
    display: inline-block;
    line-height: 25px;
    text-align: center;
    color: #000;
    cursor: pointer;
    background-color: #ff9500;
    font-size: 16px;
}
.ttBox {
}

.ttBox .tt {
    font-size: 18px;
    display: block;
    float: left;
    height: 30px;
    position: relative;
}


</style> 

<script src="${pageContext.request.contextPath}/jquery/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/js/common.js" type="text/javascript" charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/js/area.js" type="text/javascript" charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/js/areaIp.js" type="text/javascript" charset="utf-8"></script>
</head>

<script type="text/javascript">
$(function(){ 
	
}); 

</script>

<body>
	服务器列表
	<br />
	<br />

	<table id="areaTable" border="1">
		<tr>
			<td id="title_id" >ID</td>
			<td id="title_name" >名称</td>
			<td id="title_ip" align="center">IP</td>
			<td id="title_port" >端口</td>
			<td id="title_count" >人数</td>
			<td id="title_state" >状态</td>
			<td id="title_state" >白名单</td>
			<td id="title_state"  align="center">操作</td>
		</tr>
		<c:forEach items="${areaList}"  var="area"> 
			<c:set var="num" value="${fn:length(area.items)}"/>
			<c:set var="f" value="1"/>
			<c:forEach items="${area.items}"  var="item"> 
				<c:choose>
				   <c:when test="${f == 1}">    
						<c:set var="f" value="0"/>	
						<tr>
							<td align="center" rowspan=${num}>${area.id}</td>
							<td align="center"  rowspan=${num}>${area.name}</td>
							<td align="center">${item.ip}</td>
							<td align="center">${item.port}</td>
							<td align="center">${item.userCount}</td>
							<td align="center"  rowspan=${num}>${area.state}</td>
							<td align="center" id="whiteInput" rowspan=${num}>
								<c:choose>
				  					<c:when test="${area.whiteList != null && fn:length(area.whiteList) > 0}">  
				  						${area.whiteList}
									</c:when>
							   </c:choose>
							</td>
							<td align="center"  rowspan=${num}>
								<input type="button"  value="+" onclick="addIp(${area.id}, '${area.name}')"/>
								<input type="button"  value="X" onclick="delArea(${area.id})"/>
								<input type="button"  value="添加白名单" onclick="addWhite(${area.id}, '${area.name}', '${area.whiteList}')"/>
								
								<c:choose>
				  					 <c:when test="${area.whiteUsed == true}">   
										<input type="button"  value="关闭白名单" onclick="useWhiteList(${area.id})"/>
									</c:when>
								 	<c:when test="${area.whiteUsed == false}">  
								   		<input type="button"  value="启用白名单" onclick="useWhiteList(${area.id})"/>
								   </c:when>
							   </c:choose>
							</td>
						</tr>   	
				   </c:when>
				   <c:otherwise>  
				   		<tr>
							<td  align="center">${item.ip}</td>
							<td  align="center">${item.port}</td>
							<td  align="center">${item.userCount}</td>
				   		</tr>   
				   </c:otherwise>
			   </c:choose>
			</c:forEach>
		</c:forEach>
	</table>
		<p id="tipsInfo" style="color: red"></p>	
		<input type="button" value="+" onclick="addEditor()" />
		
		
	
	<div style="margin-top:20px;">
		<p>停服公告：</p>
		<textarea id="serverNotice" rows="10" cols="50">${notice}</textarea>
		</br>
		<input type="button" onclick="saveNotice()" value="保存"/>
	</div>

</body>
</html>
