<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SNMP Helper</title>
<style>
	.button{
		background-image: url(css/ui-darkness/images/ui-icons_222222_256x240.png);
		background-repeat: no-repeat;
		background-position:0px -192px;
		width:16px; 
		height:15px;
		display:inline-block;
	}
	.size{width:auto; height: auto !important;}
</style>
<link type="text/css" rel="stylesheet" href="css/ui-darkness/jquery-ui-1.10.3.custom.min.css"/>
<script type="text/javascript" src="dwr/engine.js"></script>
<script type="text/javascript" src="dwr/util.js"></script>
<script type="text/javascript" src="dwr/interface/SMNPdwr.js"></script>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="js/index.js"></script>
</head>
<body>
	Community: <input type="text" id="community" value="public"/>
	Endereço IP<input type="text" id="ipAddress"/>
	<font style="cursor: pointer;" onclick="createNewTab();"><i class="button"></i>Monitorar</font>
	<h2>Dispositivos</h2>
	<div id="tabs" class="size">
		<ul id="menu">
		</ul>
	</div>
	<jsp:include page="includes/routerTemplate.jsp"></jsp:include>
</body>
</html>