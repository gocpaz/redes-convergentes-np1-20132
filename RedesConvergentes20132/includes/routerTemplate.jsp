<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<div id="router" style="display: none;">
	<table>
		<tr>
			<td>Device model</td>
			<td style="word-wrap: break-word; width: 300px;">${retorno.deviceModel}</td>
		</tr>
		<tr>
			<td>Endereco IP</td>
			<td>${retorno.ipAddress}</td>
		</tr>
		<tr>
			<td>UP Time</td>
			<td>${retorno.sysUpTime}</td>
		</tr>
		<tr>
			<td colspan="2">Tabela de Rotas</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellpadding="3" style="background-color: #5E5D5A;">
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
	</table>
</div>
<div id="ipId">${ipId}</div>