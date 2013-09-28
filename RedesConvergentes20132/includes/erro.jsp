<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<c:choose>
	<c:when test="${mensagem != null}">
		${mensagem}
	</c:when>
	<c:otherwise>A comunicação foi interrompida inesperadamente.</c:otherwise>
</c:choose>