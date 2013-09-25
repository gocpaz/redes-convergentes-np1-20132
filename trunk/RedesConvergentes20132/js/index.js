dwr.engine.setActiveReverseAjax(true);
dwr.engine.setErrorHandler(erroServer);
var current;

function updateCurrentInterval(ip){
	current = setInterval(function() {
		$.post("pageloader.pl?ipAddress="+ip,{
			callback : function(table) {
				var ipId = $(table).find("#ipId").html();
				$("#tab"+ipId).html($(table).find("#router").html());
			},
			errorHandler : function(message) {
				clearInterval(current);
				$("#tab"+ipId).remove();
				$("#li"+ipId).remove();
				$("#tabs").tabs("refresh");
				$("#tabs").tabs("option", "active", -1);
				alert(message);
			}
		});
	}, 2000);
}

function updateCurrentIntervalDWR(ip){
	current = setInterval(function() {
		SMNPdwr.getMessage(ip,{
			callback : function(snmp) {
				//RouterData
				var table = $("#router").html();
				table = table.replace(":ipAddress",snmp.ipAddress);
				table = table.replace(":upTime",snmp.sysUpTime);
				//Set the template table for the router
				var ipId = snmp.ipAddress.split(".").join("");
				$("#tab"+ipId).html("");
				$("#tab"+ipId).html(table);
			},
			errorHandler : function(message) {
				clearInterval(current);
				$("#tab"+ipId).remove();
				$("#li"+ipId).remove();
				$("#tabs").tabs("refresh");
				$("#tabs").tabs("option", "active", -1);
				alert(message);
			}
		});
	}, 2000);
}

function erroServer(){
	clearInterval(current);
	alert("Não foi possível trazer as informações atualizadas, verifique a sua conexão.");
}

function isIpValid(ip){
	if(ip == '' || ip == undefined){
		alert("Informe um endereço de ip de um dispositivo.");
		return false;
	}
	if(ip.indexOf(".") == -1){
		alert("O endereço de ip é inválido.");
		return false;
	}
	var ipId = $("#ipAddress").val().split(".").join("");
	if($("#tab"+ipId).length > 0){
		alert("O endereço de ip já foi adicionado.");
		return false;
	}
	return true;
}

function createNewTab() {
	var ip = $.trim($("#ipAddress").val());
	if(isIpValid(ip)){
		if($("#tabs:hidden").length > 0){
			$("#tabs").show();
		}
		var ipId = $("#ipAddress").val().split(".").join("");
		$("#menu").append("<li id='li"+ipId+"'><a href='#tab"+ipId+"' style='cursor: pointer;' onclick='changeData(this);'>"+ip+"</a><span class='ui-icon ui-icon-close'></span></li>");
		$("#tabs").append("<div id='tab"+ipId+"'></div>");
		$("#tabs").tabs("refresh");
		$("#tabs").tabs("option", "active", -1);
		clearInterval(current);
		updateCurrentInterval(ip);
	}
}

function changeData(link) {
	clearInterval(current);
	var ip = $.trim($(link).html());
	updateCurrentInterval(ip);
}
$(function() {
	$("#tabs").tabs();
	$("#tabs").delegate("span.ui-icon-close", "click", function() {
		clearInterval(current);
		var panelId = $(this).closest("li").remove().attr("aria-controls");
		$("#" + panelId).remove();
		$("#tabs").tabs("refresh");
	});
});