<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false"%>

<div id="router" style="display: none;">
	<table cellpadding="2" style="background-color: #5E5D5A;">
		<tr bgcolor="#000000">
			<td>Device model</td>
			<td style="word-wrap: break-word; width: 300px;">${retorno.deviceModel}</td>
		</tr>
		<c:if test="${retorno.iosVersion != null}">
			<tr bgcolor="#000000">
				<td>IOS Version</td>
				<td>${retorno.iosVersion}</td>
			</tr>
		</c:if>
		<tr bgcolor="#000000">
			<td>Endereco IP</td>
			<td>${retorno.ipAddress}</td>
		</tr>
		<tr bgcolor="#000000">
			<td>UP Time</td>
			<td>${retorno.sysUpTime}</td>
		</tr>
		<tr bgcolor="#000000">
			<td>Last Reset</td>
			<td>${retorno.lastReset}</td>
		</tr>
		<c:if test="${fn:length(retorno.interfaces) > 0}">
			<tr bgcolor="#000000">
				<td colspan="2">Interfaces</td>
			</tr>
			<tr>
				<td colspan="2">
					<table cellpadding="3" style="background-color: #5E5D5A; width:100%;">
						<tr>
							<td rowspan="2">Nome</td>
							<td rowspan="2">Endereço IP</td>
							<td rowspan="2">Endereço MAC</td>
							<td rowspan="2">Status</td>
							<td colspan="2">Transmitidos</td>
							<td colspan="2">Descartados</td>
						</tr>
						<tr>
							<td>In</td>
							<td>Out</td>
							<td>In</td>
							<td>Out</td>
						</tr>
						<c:forEach items="${retorno.interfaces}" var="rota" varStatus="i">
							<c:set var="cor" value="#0A0A0A"></c:set>
							<c:if test="${i.count % 2 == 0}">
								<c:set var="cor" value="#000000"></c:set>
							</c:if>
							<tr bgcolor="${cor}">
								<td>${rota.name}</td>
								<td>${rota.ip}</td>
								<td>${rota.mac}</td>
								<td>${rota.status}</td>
								<td>${rota.pkTrafegadosIn}</td>
								<td>${rota.pkTrafegadosOut}</td>
								<td>${rota.pkDescartadosIn}</td>
								<td>${rota.pkDescartadosOut}</td>
							</tr>
						</c:forEach>
					</table>
				</td>
			</tr>
		</c:if>
		<c:if test="${fn:length(retorno.rotas) > 0}">
			<tr bgcolor="#000000">
				<td colspan="2">Rotas</td>
			</tr>
			<tr>
				<td colspan="2">
					<table cellpadding="3" style="background-color: #5E5D5A;width:100%;">
						<tr>
							<td>Destino</td>
							<td>Mascara</td>
							<td>NextHop</td>
							<td>Tipo</td>
							<td>Protocolo</td>
						</tr>
						<c:forEach items="${retorno.rotas}" var="rota" varStatus="i">
							<c:set var="cor" value="#0A0A0A"></c:set>
							<c:if test="${i.count % 2 == 0}">
								<c:set var="cor" value="#000000"></c:set>
							</c:if>
							<tr bgcolor="${cor}">
								<td>${rota.ipRouteEntry}</td>
								<td>${rota.ipRouteMask}</td>
								<td>${rota.ipRouteNextHop}</td>
								<td>${rota.ipRouteType}</td>
								<td>${rota.ipRouteProtocol}</td>
							</tr>
						</c:forEach>
					</table>
				</td>
			</tr>
		</c:if>
	</table>
</div>
<div id="ipId">${ipId}</div>