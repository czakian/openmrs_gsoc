<%@ include file="/WEB-INF/template/include.jsp" %>

<c:set var="HIV_PROGRAM_CONCEPT_ID" value="1482" />
<c:set var="TB_PROGRAM_CONCEPT_ID" value="1483" />

<openmrs:require privilege="View Patients" otherwise="/login.htm" redirect="/index.htm" />

	<%-- Header showing preferred name, id, and treatment status --%>
	<c:if test="${empty model.patientReasonForExit}">
		<div id="patientHeader" class="boxHeader">
	</c:if>
	<c:if test="${not empty model.patientReasonForExit}">
		<div id="patientHeader" class="boxHeaderRed">
	</c:if>
		<div id="patientHeaderPatientName">${model.patient.personName}</div>
		<div id="patientHeaderPreferredIdentifier">
			<c:if test="${fn:length(model.patient.activeIdentifiers) > 0}">
				<c:forEach var="identifier" items="${model.patient.activeIdentifiers}" begin="0" end="0">
					<span class="patientHeaderPatientIdentifier"><span id="patientHeaderPatientIdentifierType">${identifier.identifierType.name}<openmrs:extensionPoint pointId="org.openmrs.patientDashboard.afterPatientHeaderPatientIdentifierType" type="html" parameters="identifierLocation=${identifier.location.name}"/>:</span> ${identifier.identifier}</span>
				</c:forEach>
			</c:if>
		</div>
		<table id="patientHeaderGeneralInfo">
			<tr>
				<td id="patientHeaderPatientGender">
					<c:if test="${model.patient.gender == 'M'}"><img src="${pageContext.request.contextPath}/images/male.gif" alt='<spring:message code="Person.gender.male"/>' id="maleGenderIcon"/></c:if>
					<c:if test="${model.patient.gender == 'F'}"><img src="${pageContext.request.contextPath}/images/female.gif" alt='<spring:message code="Person.gender.female"/>' id="femaleGenderIcon"/></c:if>
				</td>
				<td id="patientHeaderPatientAge">
					<openmrs:extensionPoint pointId="org.openmrs.patientDashboard.beforePatientHeaderPatientAge" type="html" parameters="patientId=${model.patient.patientId}" />
					<c:if test="${model.patient.age > 0}">${model.patient.age} <spring:message code="Person.age.years"/></c:if>
					<c:if test="${model.patient.age == 0}">< 1 <spring:message code="Person.age.year"/></c:if>
					<span id="patientHeaderPatientBirthdate"><c:if test="${not empty model.patient.birthdate}">(<c:if test="${model.patient.birthdateEstimated}">~</c:if><openmrs:formatDate date="${model.patient.birthdate}" type="medium" />)</c:if><c:if test="${empty model.patient.birthdate}"><spring:message code="Person.age.unknown"/></c:if></span>
				</td>
				<openmrs:globalProperty key="use_patient_attribute.tribe" defaultValue="false" var="showTribe"/>
				<c:if test="${showTribe}">
					<td id="patientHeaderPatientTribe">
						<spring:message code="Patient.tribe"/>:
						<b>${model.patient.tribe.name}</b>
						<openmrs:extensionPoint pointId="org.openmrs.patientDashboard.afterPatientHeaderPatientTribe" type="html" parameters="patientId=${model.patient.patientId}" />
					</td>
				</c:if>
				<openmrs:globalProperty key="use_patient_attribute.healthCenter" defaultValue="false" var="showHealthCenter"/>
				<c:if test="${showHealthCenter && not empty model.patient.attributeMap['Health Center']}">
					<td id="patientHeaderHealthCenter">
						<spring:message code="PersonAttributeType.HealthCenter"/>:
						<b>${model.patient.attributeMap['Health Center'].hydratedObject}</b>
					</td>
				</c:if>
				<td id="patientDashboardHeaderExtension">
					<openmrs:extensionPoint pointId="org.openmrs.patientDashboard.Header" type="html" parameters="patientId=${model.patient.patientId}" />
				</td>
				<td style="width: 100%;">&nbsp;</td>
				<td id="patientHeaderOtherIdentifiers">
					<c:if test="${fn:length(model.patient.activeIdentifiers) > 1}">
						<c:forEach var="identifier" items="${model.patient.activeIdentifiers}" begin="1" end="1">
							<span class="patientHeaderPatientIdentifier">${identifier.identifierType.name}<openmrs:extensionPoint pointId="org.openmrs.patientDashboard.afterPatientHeaderPatientIdentifierType" type="html" parameters="identifierLocation=${identifier.location.name}"/>: ${identifier.identifier}</span>
						</c:forEach>
					</c:if>
					<c:if test="${fn:length(model.patient.activeIdentifiers) > 2}">
						<div id="patientHeaderMoreIdentifiers">
							<c:forEach var="identifier" items="${model.patient.activeIdentifiers}" begin="2">
								<span class="patientHeaderPatientIdentifier">${identifier.identifierType.name}<openmrs:extensionPoint pointId="org.openmrs.patientDashboard.afterPatientHeaderPatientIdentifierType" type="html" parameters="identifierLocation=${identifier.location.name}"/>: ${identifier.identifier}</span>
							</c:forEach>
						</div>
					</c:if>
				</td>
				<c:if test="${fn:length(model.patient.activeIdentifiers) > 2}">
					<td width="32">
						<small><a id="patientHeaderShowMoreIdentifiers" onclick="return showMoreIdentifiers()" title='<spring:message code="patientDashboard.showMoreIdentifers"/>'><spring:message code="general.nMore" arguments="${fn:length(model.patient.activeIdentifiers) - 2}"/></a></small>
					</td>
				</c:if>
			</tr>
		</table>
	</div>
	<c:if test="${empty model.patientReasonForExit}">
		<div id="patientSubheader" class="box">
	</c:if>
	<c:if test="${not empty model.patientReasonForExit}">
		<div id="patientSubheaderExited" class="boxRed">
	</c:if>
		<c:forEach items="${model.patientCurrentPrograms}" var="p" varStatus="s">
			<c:if test="${p.program.concept.conceptId == HIV_PROGRAM_CONCEPT_ID}">
				<table><tr>
					<th><spring:message code="Program.hiv"/></th>
					<td>|</td>
					<td><spring:message code="Program.enrolled"/>:</td>
					<th><openmrs:formatDate date="${p.dateEnrolled}" type="medium" /></th>
					<td>|</td>

					<c:forEach items="${p.currentStates}" var="patientState" varStatus="s">
						<c:if test="${s.index > 0}"><td>|</td></c:if>
						<td>${patientState.state.programWorkflow.concept.name}:</td>
						<th>${patientState.state.concept.name}</th>
					</c:forEach>
				</tr></table>
			</c:if>
		</c:forEach>
		<c:forEach items="${model.patientCurrentPrograms}" var="p" varStatus="s">
			<c:if test="${p.program.concept.conceptId == TB_PROGRAM_CONCEPT_ID}">
				<table><tr>
					<th><spring:message code="Program.tb"/></th>
					<td>|</td>
					<td><spring:message code="Program.enrolled"/>:</td>
					<th><openmrs:formatDate date="${p.dateEnrolled}" type="medium" /></th>
					<td>|</td>
					<td><spring:message code="Program.group"/>:</td>
					<th><openmrs_tag:mostRecentObs observations="${model.patientObs}" concept="1378" locale="${model.locale}" /></th>
				</tr></table>
			</c:if>
		</c:forEach>
		<table id="patientHeaderObs">
			<openmrs:globalProperty key="concept.weight" var="weightConceptId"/>
			<openmrs:globalProperty key="concept.height" var="heightConceptId"/>
			<openmrs:globalProperty key="concept.cd4_count" var="cd4ConceptId"/>
			
			<tr>
				<th id="patientHeaderObsWeight">
					<spring:message code="Patient.bmi"/>: ${model.patientBmiAsString}
				</th>
				<td> 
					<small>
						(
						<spring:message code="Patient.weight"/>:
						<openmrs_tag:mostRecentObs observations="${model.patientObs}" concept="${weightConceptId}" showUnits="true" locale="${model.locale}" showDate="false" />
						,
						<spring:message code="Patient.height"/>:
						<openmrs_tag:mostRecentObs observations="${model.patientObs}" concept="${heightConceptId}" showUnits="true" locale="${model.locale}" showDate="false" />
						)
					</small>
				</th>
				<td id="patientHeaderObsCD4">
					<spring:message code="Patient.cd4"/>:
					<openmrs_tag:mostRecentObs observations="${model.patientObs}" concept="${cd4ConceptId}" locale="${model.locale}" />
				</td>
				<td id="patientHeaderObsReturnVisit">
					<spring:message code="Patient.returnVisit"/>:
					<openmrs_tag:mostRecentObs observations="${model.patientObs}" concept="5096" locale="${model.locale}" />
				</td>
				<td id="patientHeaderObsRegimen">
					<spring:message code="Patient.regimen" />:
					<span id="patientHeaderRegimen">
						<c:forEach items="${model.currentDrugOrders}" var="drugOrder" varStatus="drugOrderStatus">
							<c:if test="${!empty drugOrder.drug}">${drugOrder.drug.name}</c:if><c:if test="${empty drugOrder.drug}">${drugOrder.concept.name.name}</c:if>
							<c:if test="${!drugOrderStatus.last}">, </c:if>
						</c:forEach>
					</span>
				</td>
			</tr>
		</table>
		<table><tr>
			<td><spring:message code="Patient.lastEncounter"/>:</td>
			<th>
				<c:forEach items='${openmrs:sort(model.patientEncounters, "encounterDatetime", true)}' var="lastEncounter" varStatus="lastEncounterStatus" end="0">
					${lastEncounter.encounterType.name} @ ${lastEncounter.location.name}, <openmrs:formatDate date="${lastEncounter.encounterDatetime}" type="medium" />
				</c:forEach>
				<c:if test="${fn:length(encounters) == 0}">
					<spring:message code="Encounter.no.previous"/>
				</c:if>	
			</th>
		</tr></table>
	</div>
	
	<script type="text/javascript">
		function showMoreIdentifiers() {
			if (identifierElement.style.display == '') {
				linkElement.innerHTML = '<spring:message code="general.nMore" arguments="${fn:length(model.patient.activeIdentifiers) - 2}"/>';
				identifierElement.style.display = "none";
			}
			else {
				linkElement.innerHTML = '<spring:message code="general.nLess" arguments="${fn:length(model.patient.activeIdentifiers) - 2}"/>';
				identifierElement.style.display = "";
			}
		}
		
		var identifierElement = document.getElementById("patientHeaderMoreIdentifiers");
		var linkElement = document.getElementById("patientHeaderShowMoreIdentifiers");
		if (identifierElement)
			identifierElement.style.display = "none";
		
	</script>
	