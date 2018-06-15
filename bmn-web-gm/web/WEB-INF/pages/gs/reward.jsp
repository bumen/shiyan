<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="${pageContext.request.contextPath}/jquery/jquery.min.js" type="text/javascript" charset="utf-8" ></script>
<script src="${pageContext.request.contextPath}/js/common.js" type="text/javascript" charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/js/reward.js" type="text/javascript" charset="utf-8" ></script>
</head>
<body>

	服务器列表：
	<select id="areaId" >
		<c:forEach var="area" items="${areaList}">
			<option value="${area.areaId}">${area.name}</option>
		</c:forEach>
	</select>  
	</br></br>
	<table>
		<tr><td>物品ID：</td><td><input id="goodsId" /></td></tr>
		<tr><td>数量：</td><td><input id="goodsNum" /></td></tr>
		<tr><td>金币：</td><td><input id="gold"/></td></tr>
		<tr><td>银币：</td><td><input id="iron"/></td></tr>
		<tr><td>提示信息：</td><td>
		
		<textarea id="gmTip" rows=4 cols=30>
		
		</textarea></td></tr>
	</table>
	</br>
	<button id="queryBtn" onclick="send()">发送</button>
	
	<p id="tipsInfo" style="color: red"></p>
	
	
</body>
</html>