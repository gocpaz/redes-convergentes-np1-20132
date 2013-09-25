<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SNMP Helper</title>
<link type="text/css" rel="stylesheet" href="css/ui-darkness/jquery-ui-1.10.3.custom.min.css"/>
<script type="text/javascript" src="dwr/engine.js"></script>
<script type="text/javascript" src="dwr/util.js"></script>
<script type="text/javascript" src="dwr/interface/SMNPdwr.js"></script>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript">
	dwr.engine.setActiveReverseAjax(true);
	dwr.engine.setErrorHandler(erroServer);
	var current;
	function updateCurrentInterval(ip){
		current = setInterval(function() {
			SMNPdwr.getMessage(ip,{
				callback : function(snmp) {
					//RouterData
					var table = $("#router").html();
					table = table.replace(":ipAddress",snmp.ipAddress);
					table = table.replace(":upTime",snmp.sysUpTime);
					//Set the template table for the router
					var ipId = snmp.ipAddress.split(".").join("");
					$("#tab"+ipId).html(table);
				},
				errorHandler : function(message) {
					clearInterval(current);
					alert(message);
				}
			});
		}, 2000);
	}
	
	function erroServer(){
		clearInterval(current);
		alert("Não foi possível trazer as informações atualizadas, verifique a sua conexão.");
	}
	
	function createNewTab() {
		if($("#tabs:hidden").length > 0){
			$("#tabs").show();
			$("#tabs").tabs();
		}
		var ip = $("#ipAddress").val();
		var ipId = $("#ipAddress").val().split(".").join("");
		if($("#tab"+ipId).length == 0){
			$("#menu").append("<li><a href='#tab"+ipId+"' onclick='changeData(this);'>"+ip+"</a></li>");
			$("#tabs").append("<div id='tab"+ipId+"'></div>");
			$("#tabs").tabs("refresh");
			$("#tabs").tabs("option", "active", -1);
			clearInterval(current);
			updateCurrentInterval(ip);
		}else{
			alert("O endereço de IP já foi adicionado.");
		}
	}
	
	function changeData(link){
		clearInterval(current);
		var ip = $(link).attr("href").substr(4);
		updateCurrentInterval(ip);
	}
</script>
</head>
<body>
	<input type="text" id="ipAddress"/>
	<input type="button" value="Add IP" onclick="createNewTab();" />
	<div id="tabs" style="display: none;">
		<ul id="menu">
		</ul>
	</div>
	
	<div id="router" style="display: none;">
		<table>
			<tr>
				<td>Endereco IP</td>
				<td>:ipAddress</td>
			</tr>
			<tr>
				<td>UP Time</td>
				<td>:upTime</td>
			</tr>
		</table>
	</div>
</body>
</html>