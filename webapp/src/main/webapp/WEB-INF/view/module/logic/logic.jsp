<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="localHeader.jsp"%>

<openmrs:htmlInclude file="/scripts/dojo/dojo.js" />
<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />

<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<openmrs:htmlInclude file="/scripts/jquery-ui/css/redmond/jquery-ui-1.7.2.custom.css" />
<script type="text/javascript">
	var $j = jQuery.noConflict();
</script>
<style media="screen" type="text/css">
    .defaultText { width: 300px; }
    .defaultTextActive { color: #a1a1a1; font-style: italic; }
</style>

<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/logic/css/jquery.autocomplete.css"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/logic/js/jquery.autocomplete.pack.js"/>

<script type="text/javascript">
	var patientId = 0;
	var patientIdentifier;
	var patientName;

	dojo.require("dojo.widget.openmrs.PatientSearch");
	
	dojo.addOnLoad( function() {
		$j("#patientError").hide();
		$j("#logicRuleError").hide();
		
		searchWidget = dojo.widget.manager.getWidgetById("pSearch");			
		
		dojo.event.topic.subscribe("pSearch/select", 
			function(msg) {
				patientId = msg.objs[0].patientId;
				patientIdentifier = msg.objs[0].identifier;
				patientName = msg.objs[0].personName;

				var findPatientDiv = document.getElementById("findPatient");
				var showPatientDiv = document.getElementById("showPatient");
				
				var patientIdField = document.getElementById("patientIdField");

				patientIdField.value = patientId;
				
				findPatientDiv.style.display = "none";
				showPatientDiv.style.display = "block";
				showPatientDiv.innerHTML = patientIdentifier + ": " + patientName + "&nbsp;&nbsp;&nbsp;<a href='javascript:choosePatient();' style='color: #8fabc7; font-size: .8em;'><spring:message code="logic.tester.step1.newPatient"/></a>";
			}
		);
		
		searchWidget.inputNode.select();
		//changeClassProperty("description", "display", "none");

		$j("#logicRuleField").autocomplete('tokens.form', {
			multiple: false,
			minChars: 2,
			mustMatch: false,
			selectFirst: true,
			max: 100,
			delay: 10,
			formatResult: function (data, position, cnt) {
				return '"' + data + '"';
			}
		});

		<c:choose>
			<c:when test="${not empty patient}">
				$j("#findPatient").hide();
				$j("#showPatient").show();
				$j("#showPatient").html("${patient.patientIdentifier}: ${patient.personName}&nbsp;&nbsp;&nbsp;<a href='javascript:choosePatient();' style='color: #8fabc7; font-size: .8em;'><spring:message code="logic.tester.step1.newPatient"/></a>");
				
				$j("#logicRuleField").focus();
			</c:when>
			<c:otherwise>
				$j("#findPatient").show();
			</c:otherwise>
		</c:choose>

		$j(".defaultText").focus(function(srcc) {
			if ($j(this).val() == $(this).title) {
				$j(this).removeClass("defaultTextActive");
				$j(this).val("");
			}
		});
	    	    
		$j(".defaultText").blur(function() {
			if ($j(this).val() == "") {
				$j(this).addClass("defaultTextActive");
				$j(this).val($(this).title);
			}
		});
	    	    
		$j(".defaultText").blur();  
	});

	function validate() {
		var patientIdValue = document.getElementById("patientIdField").value;
		var logicRuleValue = document.getElementById("logicRuleField").value;
		
		if (patientIdValue == 0) {
			$j("#patientError").show();
			return false;
		} else {
			$j("#patientError").hide();
		}

		if (logicRuleValue == null || logicRuleValue.length == 0 || $j("#logicRuleField").val() == $j("#logicRuleField")[0].title) {
			$j("#logicRuleError").show();
			return false;
		}
			
		return true;
	}

	
	function choosePatient() {
		patientId = null;
		patientIdentifier = null;
		patientName = null;

		$j("#findPatient").show();
		$j("#showPatient").hide();
	}
	
</script>

<h2><spring:message code="logic.tester.title"/></h2>

<p><spring:message code="logic.tester.instructions"/></p>

<br/>
<h3><spring:message code="logic.tester.step1.title"/></h3>

<div id="findPatient" style="width: 90%; margin-left: 20px; display: none;">
	<b class="boxHeader"><spring:message code="Patient.find"/></b>
	<div class="box">
		<div dojoType="PatientSearch" widgetId="pSearch" inputName="patientName" searchLabel='<spring:message code="Patient.searchBox"/>' patientId='<request:parameter name="patientId" />' showIncludeVoided='true'></div>
	</div>
	<span class="error" id="patientError" style="display: none;"><spring:message code="Patient.select"/></span>
</div>
<h3><div id="showPatient" style="display: none; color: #627588; margin-left: 20px;"></div></h3>

<form action="run.form" method="post" onsubmit="return validate();">
	<br/>
	<h3><spring:message code="logic.tester.step2.title"/></h3>	
	<input type="text" name="logicRule" id="logicRuleField" class="defaultText" title="<spring:message code="logic.tester.step2.hint"/>" autocomplete="off" style="width: 405px; margin-left: 20px;" <c:if test="${not empty token}">value="${ token }"</c:if>/>
	<span class="error" id="logicRuleError" style="display: none;"><spring:message code="error.required" arguments="Logic Rule Token" /></span>
	
	<br/><br/>
	<h3><spring:message code="logic.tester.step3.title"/></h3>
	<input type="submit" name="<spring:message code="general.submit"/>" value="<spring:message code="general.submit"/>" style="margin-left: 20px;"/>
	<input type="hidden" id="patientIdField" name="patientId" value="${patientId}" />
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
