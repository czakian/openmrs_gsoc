<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ attribute name="formFieldName" required="true" %>
<%@ attribute name="formFieldId" required="false" %>
<%@ attribute name="displayFieldId" required="false" %>
<%@ attribute name="searchLabel" required="false" %> <%-- deprecated --%>
<%@ attribute name="searchLabelCode" required="false" %> <%-- deprecated --%>
<%@ attribute name="initialValue" required="false" %> <%-- This should be a locationId --%>
<%@ attribute name="linkUrl" required="false" %> <%-- deprecated --%>
<%@ attribute name="callback" required="false" %> <%-- gets the locationId sent back --%>

<openmrs:htmlInclude file="/dwr/interface/DWRLocationService.js" />
<openmrs:htmlInclude file="/scripts/jquery/autocomplete/OpenmrsAutoComplete.js" />

<c:if test="${empty formFieldId}">
	<c:set var="formFieldId" value="${formFieldName}_id" />
</c:if>
<c:if test="${empty displayFieldId}">
	<c:set var="displayFieldId" value="${formFieldId}_selection" />
</c:if>

<script type="text/javascript">
	
	$j(document).ready( function() {

		// set up the autocomplete
		new AutoComplete("${displayFieldId}", new CreateCallback().locationCallback(), {
			select: function(event, ui) {
				jquerySelectEscaped("${formFieldId}").val(ui.item.object.locationId);
					
				<c:if test="${not empty callback}">
				if (ui.item.object) {
					// only call the callback if we got a true selection, not a click on an error field
					${callback}(ui.item.object.locationId);
				}
				</c:if>
			}
		});

		// get the name of the location that they passed in the id for
		<c:if test="${not empty initialValue}">
			jquerySelectEscaped("${formFieldId}").val("${initialValue}");
			DWRLocationService.getLocation("${initialValue}", function(loc) { jquerySelectEscaped("${displayFieldId}").val(loc.name);});
		</c:if>
		
	})
</script>

<input type="text" id="${displayFieldId}" />
<input type="hidden" name="${formFieldName}" id="${formFieldId}" />
