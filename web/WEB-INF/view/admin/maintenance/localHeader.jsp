<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message code="admin.title.short"/></a>
	</li>
	<openmrs:hasPrivilege privilege="Audit">
		<li <c:if test="<%= request.getRequestURI().contains("systemInfo") %>">class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/maintenance/systemInfo.htm">
				<spring:message code="SystemInfo.overview"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Edit Patients">
		<li <c:if test="<%= request.getRequestURI().contains("mrnGenerator") %>">class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/maintenance/mrnGenerator.htm">
				<spring:message code="MRNGenerator.generate.identifiers"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Audit">
		<li <c:if test="<%= request.getRequestURI().contains("auditPatientIdentifiers") %>">class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/maintenance/auditPatientIdentifiers.htm">
				<spring:message code="AuditPatientIdentifiers.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Patients">
		<li <c:if test="<%= request.getRequestURI().contains("quickReport") %>">class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/maintenance/quickReport.htm">
				<spring:message code="QuickReport.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Global Properties">
		<li <c:if test="<%= request.getRequestURI().contains("globalProps") %>">class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/maintenance/globalProps.form">
				<spring:message code="GlobalProperty.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Server Log">
		<li <c:if test="<%= request.getRequestURI().contains("serverLog") %>">class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/maintenance/serverLog.form">
				<spring:message code="ServerLog.view"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Data Entry Statistics">
		<li <c:if test="<%= request.getRequestURI().contains("dataEntryStat") %>">class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/admin/maintenance/dataEntryStats.list">
				<spring:message code="DataEntryStatistics.title"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:extensionPoint pointId="org.openmrs.admin.maintenance.localHeader" type="html">
			<c:forEach items="${extension.links}" var="link">
				<li <c:if test="${fn:endsWith(pageContext.request.requestURI, link.key)}">class="active"</c:if> >
					<a href="${pageContext.request.contextPath}/${link.key}"><spring:message code="${link.value}"/></a>
				</li>
			</c:forEach>
	</openmrs:extensionPoint>
</ul>