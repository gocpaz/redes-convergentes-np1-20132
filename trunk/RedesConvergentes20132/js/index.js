dwr.engine.setActiveReverseAjax(true);
dwr.engine.setErrorHandler(erroServer);
var current;
var community;
function updateCurrentInterval(ip){
	current = setInterval(function() {
		$.ajax({
			method: "post",
			url: "pageloader.pl",
			data: "ipAddress="+ip+"&community="+community,
			success: function(table) {
				var ipId = $(table)[2].innerHTML;
				$("#tab"+ipId).html($(table)[0].innerHTML);
			},
			error: function(response, status, error){
				clearInterval(current);
				//var msg = "Request Timeout, verifique se o IP está correto e se o SNMP está habilitado.";
				var msg = $.trim(response.responseText);
				alert(msg);
			}
		});
	}, 5000);
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
	community = $.trim($("#community").val());
	$("#community").attr("readonly","readonly");
	$("#community").css("background-color","#f0f0f0");
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
		if($("span.ui-icon-close").length == 0){
			$("#community").removeAttr("readonly");
			$("#community").css("background-color","");
		}
	});
});