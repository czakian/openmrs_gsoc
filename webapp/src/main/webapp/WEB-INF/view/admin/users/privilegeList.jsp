<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Privileges" otherwise="/login.htm" redirect="/admin/users/privilege.list" />
<spring:message var="pageTitle" code="Privilege.manage.titlebar" scope="page"/>	
	
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="Privilege.manage.title"/></h2>

<a href="privilege.form"><spring:message code="Privilege.add"/></a>
<br/><br/>

<b class="boxHeader"><spring:message code="Privilege.list.title"/></b>
<form method="post" class="box">
	<table>
		<tr>
			<th> </th>
			<th> <spring:message code="Privilege.privilege"/> </th>
			<th> <spring:message code="general.description"/> </th>
		</tr>
	<c:forEach var="map" items="${privilegeList}">
		<tr>
			<td>
				<c:if test="${map.value == false}">
					<input type="checkbox" name="privilegeId" value="<c:out value="${map.key.privilege}"/>">
				</c:if>
				<c:if test="${map.value == true}">
					<img src="${pageContext.request.contextPath}/images/lock.gif"/>
				</c:if>
			</td>
			<td>
				<a href="privilege.form?privilege=<c:out value="${map.key.privilege}"/>">
					<c:out value="${map.key.privilege}"/>
				</a>
			</td>
			<td><c:out value="${map.key.description}"/></td>
		</tr>
	</c:forEach>
	</table>
	<input type="submit" value="<spring:message code="Privilege.delete"/>">
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>