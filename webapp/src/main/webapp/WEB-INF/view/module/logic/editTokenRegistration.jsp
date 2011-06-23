<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage Tokens" otherwise="/login.htm" redirect="/module/logic/manageLogicRules.list" />

<%@ include file="localHeader.jsp"%>

<form method="post" action="deleteToken.form">
	<input type="hidden" name="id" value="${ tokenRegistration.id }"/>
	<input type="submit" value="<spring:message code="logic.token.edit.delete"/>" style="float: right"/>
</form>

<h2><spring:message code="logic.token.edit.title"/></h2>

<form method="post">
	<table>
		<tr>
			<th><spring:message code="logic.TokenRegistration.token"/></th>
			<td>
				<spring:bind path="tokenRegistration.token">
					<input size="40" type="text" name="${status.expression}" value="${status.value}"/>
					<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<th><spring:message code="logic.TokenRegistration.ruleProvider"/></th>
			<td>
				<spring:bind path="tokenRegistration.providerClassName">
					<input size="40" type="text" name="${status.expression}" value="${status.value}"/>
					<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<th><spring:message code="logic.TokenRegistration.configuration"/></th>
			<td>
				<spring:bind path="tokenRegistration.configuration">
					<input size="40" type="text" name="${status.expression}" value="${status.value}"/>
					<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<th><spring:message code="logic.TokenRegistration.providerToken"/></th>
			<td>
				<spring:bind path="tokenRegistration.providerToken">
					<input size="40" type="text" name="${status.expression}" value="${status.value}"/>
					<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
					<spring:message code="logic.token.edit.providerToken.warning"/>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<th></th>
			<td>
				<input type="submit" value="<spring:message code="general.save"/>"/>
				<input type="button" value="<spring:message code="general.cancel"/>" onClick="window.location = 'manageTokens.list';"/>
			</td>
		</tr>
	</table>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>