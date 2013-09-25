<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<div id="router" style="display: none;">
	<div id="ipId">${ipId}</div>
	<table>
		<tr>
			<td>Endereco IP</td>
			<td>${retorno.ipAddress}</td>
		</tr>
		<tr>
			<td>UP Time</td>
			<td>:upTime</td>
		</tr>
	</table>
</div>