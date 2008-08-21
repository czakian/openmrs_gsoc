<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ attribute name="observations" required="true" type="java.util.Set" %>
<%@ attribute name="concept" required="true" type="java.lang.Integer" %>
<%@ attribute name="locale" required="true" type="java.util.Locale" %>
<%@ attribute name="label" required="false" type="java.lang.String" %>
<%@ attribute name="showUnits" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showDate" required="false" type="java.lang.Boolean" %>
<%@ attribute name="labelIfNone" required="false" type="java.lang.String" %>

<c:set var="mostRecentObs_foundAny" value="false" />
<c:forEach items="${openmrs:sort(openmrs:filterObsByConcept(observations, concept), 'obsDatetime', true)}" var="o" end="0">
	<c:if test="${label != null}">
		<span class="obsLabel"><spring:message code="${label}" />:</span>
	</c:if>
	<span class="obsValue"><openmrs:format obsValue="${o}"/></span>
	<c:if test="${showUnits}">
		<openmrs:concept conceptId="${o.concept.conceptId}" var="c" nameVar="n" numericVar="nv">
			<c:if test="${nv != null}">
				<span class="obsUnits"><spring:message code="Units.${nv.units}" /></span>
			</c:if>
		</openmrs:concept>
	</c:if>
	<span class="obsDate"><c:if test="${showDate}">(<openmrs:formatDate date="${o.obsDatetime}" type="medium" />)</c:if></span>
	<c:set var="mostRecentObs_foundAny" value="true" />
</c:forEach>

<c:if test="${labelIfNone != null && mostRecentObs_foundAny == 'false'}">
	<spring:message code="${labelIfNone}"/>
</c:if>