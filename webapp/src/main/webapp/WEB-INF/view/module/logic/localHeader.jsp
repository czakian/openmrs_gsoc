<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message code="admin.title.short"/></a>
	</li>
	<openmrs:hasPrivilege privilege="Manage Tokens">
		<li <c:if test='<%= request.getRequestURI().contains("Token") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/logic/manageTokens.list">
				<spring:message code="logic.token.manage.title"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Rule Definitions">
		<li <c:if test='<%= request.getRequestURI().contains("RuleDefinition") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/logic/manageRuleDefinitions.list">
				<spring:message code="logic.rule.manage.title"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<li <c:if test='<%= request.getRequestURI().contains("logic/logic") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/logic/logic.form">
			<spring:message code="logic.tester.title"/>
		</a>
	</li>
	<openmrs:hasPrivilege privilege="Manage Tokens">
		<li <c:if test='<%= request.getRequestURI().contains("logic/init") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/logic/init.form">
				<spring:message code="logic.init.title"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
</ul>