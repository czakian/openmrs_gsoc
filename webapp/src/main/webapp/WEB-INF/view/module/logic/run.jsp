<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />
<script type="text/javascript">
	var $j = jQuery.noConflict();

	$j(document).ready(function() {
		$j("#showErrorDetails").click( function() {
			$j("#errorDetails").toggle();	
		});
	});
</script>

<openmrs:require privilege="View Administration Functions" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="localHeader.jsp"%>

<h2><spring:message code="logic.start.title"/></h2>

<br/><h3><spring:message code="logic.tester.results.title"/></h3>
<br />

<c:choose>
	<c:when test="${not empty error}">
		<spring:message code="Hl7inQueue.queueList.errorMessage.header"/>: ${error} 
		<c:if test="${not empty detail}" >
			<a id="showErrorDetails" href="#"><spring:message code="logic.tester.error.details"/></a>
			<br/><br/><div id="errorDetails" style="display: none; font-size: 10px;">${detail}</div>
		</c:if>
	</c:when>
	<c:otherwise>
		<strong><spring:message code="Encounter.patient"/>:</strong> ${patient.patientIdentifier}: ${patient.personName}<br/>
		<strong><spring:message code="SearchResults.resultsFor"/> ${logicRule}:</strong> ${result}<br/>
	</c:otherwise>
</c:choose>

<form method="get" action="logic.form" name="back" id="back">
<input type="hidden" id="patientIdField" name="patientId" value="${patientId}" />
<br/><p><a href="#" onclick="document.back.submit();"><spring:message code="logic.tester.results.again"/></a></p>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>