<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Edit Concepts" otherwise="/login.htm" redirect="/admin/concepts/conceptDrug.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<openmrs:htmlInclude file="/scripts/dojo/dojo.js" />

<script type="text/javascript">
	dojo.require("dojo.widget.openmrs.ConceptSearch");
	dojo.require("dojo.widget.openmrs.OpenmrsPopup");

	<request:existsParameter name="autoJump">
		var autoJump = <request:parameter name="autoJump"/>;
	</request:existsParameter>

	dojo.addOnLoad( function() {
		
		var cSelection = dojo.widget.manager.getWidgetById("conceptSelection");
		
		dojo.event.topic.subscribe("conceptSearch/select", 
			function(msg) {
				cSelection.displayNode.innerHTML = "<a href='#View Concept' onclick='return gotoConcept(\"concept\")'>" + msg.objs[0].name + "</a>";
				cSelection.hiddenInputNode.value = msg.objs[0].conceptId;
			}
		);
	});

	function gotoConcept(tagName, conceptId) {
		if (conceptId == null)
			conceptId = $(tagName).value;
		window.location = "${pageContext.request.contextPath}/dictionary/concept.form?conceptId=" + conceptId;
		return false;
	}

</script>

<style>
	#table th {
		text-align: left;
	}
</style>

<h2><spring:message code="ConceptDrug.manage.title"/></h2>

<openmrs:extensionPoint pointId="org.openmrs.admin.concepts.conceptDrugForm.afterTitle" type="html" parameters="drugId=${drug.drugId}" />

<c:if test="${drug.retired}">
<form action="" method="post">
	<div class="retiredMessage">
	<div>
	<spring:message code="ConceptDrug.retiredMessage"/>
	${drug.retiredBy.personName}
				<openmrs:formatDate date="${drug.dateRetired}" type="medium" />
				-
				${drug.retireReason}
				<input type="submit" value='<spring:message code="ConceptDrug.unretireDrug"/>' name="unretireDrug"/>
			
	</div>
	</div>
	</form>
</c:if>

<spring:hasBindErrors name="drug">
	<spring:message code="fix.error"/>
	<br />
</spring:hasBindErrors>

<form method="post">
<fieldset>
<table cellpadding="3" cellspacing="0" id="table">
	<tr>
		<th><spring:message code="general.name"/></th>
		<td>
			<spring:bind path="drug.name">			
				<input type="text" name="${status.expression}" size="40" 
					   value="${status.value}" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if> 
			</spring:bind>
		</td>
	</tr>
	<tr>
		<th><spring:message code="ConceptDrug.concept"/></th>
		<td>
			<spring:bind path="drug.concept">
				<div dojoType="ConceptSearch" widgetId="conceptSearch" conceptId="${status.value.conceptId}" showVerboseListing="true" includeClasses="Drug;"></div>
				<div dojoType="OpenmrsPopup" widgetId="conceptSelection" hiddenInputName="conceptId" hiddenInputId="concept" searchWidget="conceptSearch" searchTitle='<spring:message code="ConceptDrug.find"/>'></div>
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>				
			</spring:bind>
		</td>
	</tr>
	<tr>
		<th><spring:message code="ConceptDrug.combination"/></th>
		<td>
			<spring:bind path="drug.combination">	
				<input type="hidden" name="_${status.expression}" value=""/>		
				<input type="checkbox" name="${status.expression}" 
					   <c:if test="${status.value == true}">checked</c:if> />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if> 
			</spring:bind>
		</td>
	</tr>
	<tr>
		<th><spring:message code="ConceptDrug.doseStrength"/></th>
		<td>
			<spring:bind path="drug.doseStrength">			
				<input type="text" name="${status.expression}" size="10" 
					   value="${status.value}" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if> 
			</spring:bind>
		</td>
	</tr>
	<tr>
		<th><spring:message code="ConceptDrug.units"/></th>
		<td>
			<spring:bind path="drug.units">			
				<input type="text" name="${status.expression}" size="10" 
					   value="${status.value}" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if> 
			</spring:bind>
		</td>
	</tr>
	<tr>
		<th><spring:message code="ConceptDrug.minimumDailyDose"/></th>
		<td>
			<spring:bind path="drug.minimumDailyDose">			
				<input type="text" name="${status.expression}" size="10" 
					   value="${status.value}" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if> 
			</spring:bind>
		</td>
	</tr>
	<tr>
		<th><spring:message code="ConceptDrug.maximumDailyDose"/></th>
		<td>
			<spring:bind path="drug.maximumDailyDose">
				<input type="text" name="${status.expression}" size="10" 
					   value="${status.value}" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if> 
			</spring:bind>
		</td>
	</tr>
	
	<c:if test="${drug.creator != null}">
		<tr>
			<th><spring:message code="general.createdBy" /></th>
			<td>
				<a href="#View User" onclick="return gotoUser(null, '${drug.creator.userId}')">${drug.creator.personName}</a> -
				<openmrs:formatDate date="${drug.dateCreated}" type="medium" />
			</td>
		</tr>
	</c:if>
</table>

<openmrs:extensionPoint pointId="org.openmrs.admin.concepts.conceptDrugForm.inForm" type="html" parameters="drugId=${drug.drugId}" />

<br />
<input type="hidden" name="phrase" value='<request:parameter name="phrase" />'/>
<input type="submit" value='<spring:message code="ConceptDrug.save"/>'>
&nbsp;
<input type="button" value='<spring:message code="general.cancel"/>' onclick="history.go(-1); return; document.location='index.htm?autoJump=false&phrase=<request:parameter name="phrase"/>'">
</fieldset>
</form>
<br/>
<br/>
<c:if test="${not drug.retired && not empty drug.drugId}">
	<form action="" method="post">
		<fieldset>
			<h4><spring:message code="ConceptDrug.retireDrug"/></h4>
			
			<b><spring:message code="general.reason"/></b>
			<input type="text" value="" size="40" name="retireReason" />
			<spring:hasBindErrors name="drug">
				<c:forEach items="${errors.allErrors}" var="error">
					<c:if test="${error.code == 'retireReason'}"><span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span></c:if>
				</c:forEach>
			</spring:hasBindErrors>
			<br/>
			<input type="submit" value='<spring:message code="ConceptDrug.retireDrug"/>' name="retireDrug"/>
		</fieldset>
	</form>
</c:if>
<script type="text/javascript">
	document.getElementById('retiredReasonRow').style.display = document.getElementById('retired').checked ==true ? '' : 'none';
</script>

<openmrs:extensionPoint pointId="org.openmrs.admin.concepts.conceptDrugForm.footer" type="html" parameters="drugId=${drug.drugId}" />

<%@ include file="/WEB-INF/template/footer.jsp" %>