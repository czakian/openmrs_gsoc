<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage Rule Definitions" otherwise="/login.htm" redirect="/module/logic/manageRuleDefinitions.list" />

<%@ include file="localHeader.jsp"%>

<h2><spring:message code="logic.rule.manage.title"/></h2>

<a href="editRuleDefinition.form"><spring:message code="logic.rule.manage.add"/></a>

<br/><br/>

<div class="boxHeader">
	<spring:message code="logic.rule.manage.existing"/>
</div>
<table class="box">
	<thead>
		<tr>
			<th><spring:message code="logic.RuleDefinition.name"/></th>
			<th><spring:message code="logic.RuleDefinition.language"/></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="rule" items="${ rules }">
			<tr>
				<td><a href="editRuleDefinition.form?id=${ rule.id }">${ rule.name }</a></td>
				<td>${ rule.language }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>