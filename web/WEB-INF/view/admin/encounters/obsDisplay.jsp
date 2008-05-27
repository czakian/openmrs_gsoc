<%@ include file="/WEB-INF/template/include.jsp" %>
<c:set var="obsList" value="${requestScope.obsList}" />
<c:set var="field" value="${requestScope.field}" />
<c:set var="level" value="${requestScope.level}" />
<c:set var="padding" value="${level*4}em" />

<c:forEach var="obs" items="${obsList}">
	<c:choose>
		<c:when test="${obs.obsGrouping}">
			<tr class="<c:if test="${obs.voided}">voided </c:if>obsGroupHeader">
				<td>${field.fieldNumber}<c:if test="${field.fieldPart != null && field.fieldPart != ''}">.${field.fieldPart}</c:if></td>
				<td colspan="4" style="padding-left: ${padding}">${field.field.concept.name.name}</td>
			</tr>
			<tr class="<c:if test="${obs.voided}">voided </c:if>">
				<td colspan="5"><%-- this is the empty row to mimic the description row--%></td>
			</tr>
			<c:set var="obsList" value="${obs.groupMembers}" scope="request"/>
			<c:set var="field" value="${otherFormFields[groupMember]}" scope="request"/>
			<c:set var="level" value="${level+1}" scope="request"/>
			<c:import url="obsDisplay.jsp" />
		</c:when>
		<c:otherwise>
			<tr class="<c:if test="${obs.voided}">voided </c:if><c:choose><c:when test="${count % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>" onmouseover="mouseover(this)" onmouseout="mouseout(this)" onclick="click('${obs.obsId}')">
				<td class="fieldNumber">${field.fieldNumber}<c:if test="${field.fieldPart != null && field.fieldPart != ''}">.${field.fieldPart}</c:if></td>
				<td class="obsConceptName" style="padding-left: ${padding}"><a href="${pageContext.request.contextPath}/admin/observations/obs.form?obsId=${obs.obsId}" onclick="return click('${obs.obsId}')">${obs.concept.name.name}</a></td>
				<td class="obsValue"><openmrs:format obsValue="${obs}" /></td>
				<td class="obsAlerts" valign="middle" align="right">
					<span class="obsEdit"><c:if test="${fn:contains(editedObs, obs.obsId)}"><img src="${pageContext.request.contextPath}/images/alert.gif" title='<spring:message code="Obs.edited"/>' /></c:if></span>
					<span class="obsComment"><c:if test="${obs.comment != null && obs.comment != ''}"><img src="${pageContext.request.contextPath}/images/note.gif" title="${obs.comment}" /></c:if></span>
				</td>
				<td class="obsCreator" style="white-space: nowrap;">
					${obs.creator.personName} -
					<openmrs:formatDate date="${obs.dateCreated}" type="medium" />
				</td>
			</tr>
			<tr class="<c:if test="${obs.voided}">voided </c:if><c:choose><c:when test="${status.index % 2 == 0}">evenRow</c:when><c:otherwise>oddRow</c:otherwise></c:choose>" onmouseover="mouseover(this, true)" onmouseout="mouseout(this, true)" onclick="click('${obs.obsId}')">
				<td colspan="5" style="padding-left: ${padding}"><div class="description">${obs.concept.name.description}</div></td>
			</tr>
		</c:otherwise>
	</c:choose>
</c:forEach>