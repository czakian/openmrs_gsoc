<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Encounter Types" otherwise="/login.htm" redirect="/admin/encounters/encounterType.list" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<h2><spring:message code="EncounterType.manage.title"/></h2>	

<a href="encounterType.form"><spring:message code="EncounterType.add"/></a> 

<openmrs:extensionPoint pointId="org.openmrs.admin.encounters.encounterTypeList.afterAdd" type="html" />

<br />
<br />

<b class="boxHeader"><spring:message code="EncounterType.list.title"/></b>
<form method="post" class="box">
	<table>
		<tr>
			<th> </th>
			<th> <spring:message code="general.name" /> </th>
			<th> <spring:message code="general.description" /> </th>
		</tr>
		<c:forEach var="encounterType" items="${encounterTypeList}">
			<tr>
				<td valign="top"><input type="checkbox" name="encounterTypeId" value="${encounterType.encounterTypeId}"></td>
				<td valign="top">
					<a href="encounterType.form?encounterTypeId=${encounterType.encounterTypeId}">
					   ${encounterType.name}
					</a>
				</td>
				<td valign="top">${encounterType.description}</td>
			</tr>
		</c:forEach>
	</table>
	<openmrs:extensionPoint pointId="org.openmrs.admin.encounters.encounterTypeList.inForm" type="html" />
	
	<input type="submit" value="<spring:message code="EncounterType.delete"/>" name="action">
</form>

<openmrs:extensionPoint pointId="org.openmrs.admin.encounters.encounterTypeList.footer" type="html" />

<%@ include file="/WEB-INF/template/footer.jsp" %>