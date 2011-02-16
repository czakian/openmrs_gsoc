<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Edit Patients" otherwise="/login.htm" redirect="/admin/patients/mergePatients.form"/>

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>

<openmrs:htmlInclude file="/scripts/dojo/dojo.js" />

<script type="text/javascript">
var dupSize=0;
var currSelectedTab = null;
var tabCount = 0;
	dojo.require("dojo.widget.openmrs.PatientSearch");
	
	function changePrimary(dir) {
		var left = document.getElementById("left");
		var right= document.getElementById("right");
		
		if (dir == "left") {
			left.className = "preferred";
			right.className = "notPreferred";
		}
		else {
			left.className = "notPreferred";
			right.className = "preferred";
		}
		
		var cell = document.getElementById("patientDivider");
		cell.innerHTML = "";
		var img = document.createElement("img");
		var src = "${pageContext.request.contextPath}/images/" + dir + "Arrow.gif";
		img.src = src;
		cell.appendChild(img);
	}

	dojo.addOnLoad( function() {
	
		dojo.event.topic.subscribe("pSearch/select", 
			function(msg) {
				var patient = msg.objs[0];
				if (patient.patientId != undefined && patient.patientId != "${patient1.patientId}") {
					if("${patient1.patientId}" != ""){
						var query = "?patientId=${patient1.patientId}&patientId=" + patient.patientId;
					}else{
						var query = "?patientId="+patient.patientId;
					}
					document.location = "mergePatients.form" + query;
				}
			}
		);
	});
	

dojo.addOnLoad(collectInfo);

function collectInfo(){
		var patientNames = document.getElementById("PatientNames");
		var patientIdentifiers = document.getElementById("PatientIdentifiers");
		var patientAddress = document.getElementById("PatientAddress");
		var patientInfos = document.getElementById("PatientInfos");
		var encounters = document.getElementById("Encounters");
		var pref = true;
		var defPreferred;
		var count = 0;
		var notPreferredCount = 0;

		<c:forEach items="${patientList}" var="patient" varStatus="status">
			count++;
			var isPreferred = false;
			if("${patient.voided}"!="true" && pref){
				defPreferred = document.getElementById("${status.index}");
				defPreferred.checked = true;
				pref = false;
				isPreferred = true;
			}else
				notPreferredCount++;
			
			<c:forEach items="${patient.names}" var="name">
					patientNames.value = patientNames.value+"${name}|";
			</c:forEach>
			patientNames.value = patientNames.value+"#";
			
			<c:forEach items="${patient.identifiers}" var="identifier">
					patientIdentifiers.value = patientIdentifiers.value+"${identifier.identifier} ${identifier.identifierType.name}|";
			</c:forEach>
			patientIdentifiers.value = patientIdentifiers.value+"#";

			<c:forEach items="${patient.addresses}" var="address">
					patientAddress.value = patientAddress.value+"${address.address1} ${address.address2} ${address.cityVillage}|";
			</c:forEach>
			patientAddress.value = patientAddress.value+"#";

			patientInfos.value = patientInfos.value+"${patient.patientId},${patient.gender},<openmrs:formatDate date='${patient.birthdate}' type='short' />,<openmrs:formatDate date='${patient.deathDate}' type='short' />,${patient.creator.personName} - <openmrs:formatDate date='${patient.dateCreated}' type='long' />,${patient.changedBy.personName} - <openmrs:formatDate date='${patient.dateChanged}' type='long' />,${patient.voided}#";
			if(!isPreferred){
				addPatientTab('${status.index}', notPreferredCount);
			}
		</c:forEach>
		<c:forEach items="${patientEncounters}" var="encounters" varStatus="status">
			<c:forEach items="${encounters}" var="encounter">
						encounters.value = encounters.value+"${encounter.encounterType.name} ${encounter.location.name} <openmrs:formatDate date='${encounter.encounterDatetime}' type='short' /> <a href=\"${pageContext.request.contextPath}/admin/encounters/encounter.form?encounterId=${encounter.encounterId}\"><spring:message code='general.view'/></a>|";
			</c:forEach>
			encounters.value = encounters.value+"#";
		</c:forEach>
		
		if(count>2){
			document.getElementById("selectionList").style.display = "";
			document.getElementById("p1").style.display="none";
			document.getElementById("p2").style.display="none";
			display(defPreferred, false);
		}
		else{
			document.getElementById("np1").style.display="none";
			document.getElementById("np2").style.display="none";
			document.getElementById("tabsRow").style.display="none";
		}
}


function display(obj, updateTabs){
	var prefId = obj.id;
	var check = true;
	var patientsNames = document.getElementById("PatientNames").value.split('#');
	var patientsIdentifiers = document.getElementById("PatientIdentifiers").value.split('#');
	var patientsAddress = document.getElementById("PatientAddress").value.split('#');
	var patientsInfos = document.getElementById("PatientInfos").value.split('#');
	var encounters = document.getElementById("Encounters").value.split('#');
	dupSize = patientsNames.length-1;
	var nextTabIndex = 0;
	
	for(var i=0;i<patientsNames.length-1;i++){
		
		if(prefId==i){
			document.getElementById(prefId+"tr").className = "searchHighlight";
		}else{
			var className = "oddRow";
			if(i%2 == 0)
				className = "evenRow";
			document.getElementById(i+"tr").className = className;
			if(updateTabs == true){
				updatePatientTab(nextTabIndex, i);
				nextTabIndex++;
			}
		}

		var patientNames = patientsNames[i].split('|');
		var names = "";
		for(var j=0;j<patientNames.length-1;j++){//name print Started
			names = names+"<li>"+patientNames[j];
		}//name print Ended
		
		var patientIdentifiers = patientsIdentifiers[i].split('|');
		var identifiers = "";
		for(var j=0;j<patientIdentifiers.length-1;j++){//identifier print Started
			identifiers = identifiers+"<li>"+patientIdentifiers[j];
		}//identifier print Ended

		var patientAddress = patientsAddress[i].split('|');
		var address = "";
		for(var j=0;j<patientAddress.length-1;j++){//address print Started
			address = address+"<li>"+patientAddress[j];
		}//address print Ended
		
		var patientInfos = patientsInfos[i].split(',');
		var infos = "";
		for(var j=0;j<patientInfos.length;j++){//info print Started
			infos = patientInfos[j];
			if(i==prefId){
				if(j==1){
					if(patientInfos[j].indexOf('F')>=0){
						infos = "<img src=\"${pageContext.request.contextPath}/images/female.gif\" />";
					}else{
						infos = "<img src=\"${pageContext.request.contextPath}/images/male.gif\" />";
					}
				}else{ 
					if(patientInfos[j]=="true"){
						document.getElementById("voidCheck1").style.display = "";
						infos = "Yes";
					}else if(patientInfos[j]=="false"){
						document.getElementById("voidCheck1").style.display = "none";
						infos = "No";
					}
				}
				document.getElementById("info1"+j).innerHTML = infos;
			}else if(check){
				if(j==1){
					if(patientInfos[j].indexOf('F')>=0){
						infos = "<img src=\"${pageContext.request.contextPath}/images/female.gif\" />";
					}else{
						infos = "<img src=\"${pageContext.request.contextPath}/images/male.gif\" />";
					}
				}else{ 
					if(patientInfos[j]=="true"){
						document.getElementById("voidCheck2").style.display = "";
						infos = "Yes";
					}else if(patientInfos[j]=="false"){
						document.getElementById("voidCheck2").style.display = "none";
						infos = "No";
					}
				}
				document.getElementById("info2"+j).innerHTML = infos;
			}
		}//info print Ended

		var encounter = encounters[i].split('|');
		var enco = "";
		for(var j=0;j<encounter.length-1;j++){
			enco = enco+"<li>"+encounter[j];
		}


		if(i==prefId){
			document.getElementById('name1').innerHTML = names;
			document.getElementById('identifier1').innerHTML = identifiers;
			document.getElementById('address1').innerHTML = address;
			document.getElementById('encounter1').innerHTML = enco;
			document.getElementById('edit1').href = "patient.form?patientId="+patientInfos[0];
		}
		else if(check){
			check = false;
			document.getElementById('name2').innerHTML = names;
			document.getElementById('identifier2').innerHTML = identifiers;
			document.getElementById('address2').innerHTML = address;
			document.getElementById('encounter2').innerHTML = enco;
			document.getElementById('edit2').href = "patient.form?patientId="+patientInfos[0];
		}
	}//end of main for
	
	//if we have multiple patients to merge, set the first patient tab as selected
	if(dupSize > 1){
		updateTabState('tab0');
	}
}

function unPrefPatient(i){
	i = parseInt(i);
	var patientsNames = document.getElementById("PatientNames").value.split('#');
	var patientsIdentifiers = document.getElementById("PatientIdentifiers").value.split('#');
	var patientsAddress = document.getElementById("PatientAddress").value.split('#');
	var patientsInfos = document.getElementById("PatientInfos").value.split('#');
	var encounters = document.getElementById("Encounters").value.split('#');

	var patientNames = patientsNames[i].split('|');
		var names = "";
		for(var j=0;j<patientNames.length-1;j++){//name print Started
			names = names+"<li>"+patientNames[j];
		}//name print Ended
		
		var patientIdentifiers = patientsIdentifiers[i].split('|');
		var identifiers = "";
		for(var j=0;j<patientIdentifiers.length-1;j++){//identifier print Started
			identifiers = identifiers+"<li>"+patientIdentifiers[j];
		}//identifier print Ended

		var patientAddress = patientsAddress[i].split('|');
		var address = "";
		for(var j=0;j<patientAddress.length-1;j++){//address print Started
			address = address+"<li>"+patientAddress[j];
		}//address print Ended
		
		var patientInfos = patientsInfos[i].split(',');
		var infos = "";
		for(var j=0;j<patientInfos.length;j++){//info print Started
			infos = patientInfos[j];
				if(j==1){
					if(patientInfos[j].indexOf('F')>=0){
						infos = "<img src=\"${pageContext.request.contextPath}/images/female.gif\" />";
					}else{
						infos = "<img src=\"${pageContext.request.contextPath}/images/male.gif\" />";
					}
				}else{ 
					if(patientInfos[j]=="true"){
						document.getElementById("voidCheck2").style.display = "";
						infos = "Yes";
					}else if(patientInfos[j]=="false"){
						document.getElementById("voidCheck2").style.display = "none";
						infos = "No";
					}
				}
				document.getElementById("info2"+j).innerHTML = infos;
		}//info print Ended

		var encounter = encounters[i].split('|');
		var enco = "";
		for(var j=0;j<encounter.length-1;j++){
			enco = enco+"<li>"+encounter[j];
		}

		document.getElementById('name2').innerHTML = names;
		document.getElementById('identifier2').innerHTML = identifiers;
		document.getElementById('address2').innerHTML = address;
		document.getElementById('encounter2').innerHTML = enco;
		document.getElementById('edit2').href = "patient.form?patientId="+patientInfos[0];
}

function addPatientTab(patientDataIndex, notPreferredCount){
	var tabsRow = document.getElementById("tabsRowRight");
	var lastTab = document.getElementById("lastTab");
	var tabSeparator = document.createElement('TD');
	tabSeparator.width = "2px";
	tabSeparator.className = 'mergePatientsTabSpaces';
	var newTab = document.createElement('TD');
	newTab.className = 'mergePatientsTab';
	newTab.id = 'tab'+tabCount;
	newTab.innerHTML = "<div class='mergePatientsTabDiv'>"+omsgs.tabLabelPrefix+" "+notPreferredCount+"</div>";
	if(tabsRow){
		tabsRow.insertBefore(tabSeparator, lastTab);
		tabsRow.insertBefore(newTab , lastTab);
		tabCount++;
		$j(newTab).click(function(){
			updateTabState(newTab.id);
			unPrefPatient(patientDataIndex);
		});
	}
}

//updates a single tab when the preferred patient is changed to match 
//the new patient data for the tab at the specified index
function updatePatientTab(tabIndex, patientDataIndex){
	var tab = document.getElementById("tab"+tabIndex);
	if(tab){
		$j(tab).click(function(){
			unPrefPatient(patientDataIndex);
		})
	}
}

//sets the selected tab and stores its value
function updateTabState(selectedTabId){
	//un select the current patient tab if we have any
	if(currSelectedTab)
		currSelectedTab.className = "mergePatientsTab";
	
	var selectedTab = document.getElementById(selectedTabId);
	if(selectedTab){
		selectedTab.className = "mergePatientsSelectedTab";
		currSelectedTab = selectedTab;
	}
}

function generateMergeList(){
	var conf = confirm('Are you sure you want to merge these patients?');
	if(conf){
		var preferred = document.getElementById('pref');
		var nonPreferred = document.getElementById('nonPref');
		if(dupSize>2){
			nonPreferred.value = "";
			var patients = document.getElementsByName('preferred2');
			for(var i=0;i<patients.length;i++){
				if(patients[i].checked == false){
					nonPreferred.value = nonPreferred.value+patients[i].value+",";
				}else preferred.value = patients[i].value;
			}
			return true;
		}else{
			var preferred = document.getElementById('pref');
			var nonPreferred = document.getElementById('nonPref');
			if(document.getElementById("${patient1.patientId}preferred").checked){
				preferred.value = document.getElementById("${patient1.patientId}preferred").value;
				nonPreferred.value = document.getElementById("${patient2.patientId}preferred").value;
			}else if(document.getElementById("${patient2.patientId}preferred").checked){
				preferred.value = document.getElementById("${patient2.patientId}preferred").value;
				nonPreferred.value = document.getElementById("${patient1.patientId}preferred").value;
			}else{
				return false;
			}
		}

	}else return false;
}

</script>

<style>
	#patientDivider {
		border-left: 1px solid black;
		border-right: 1px solid black;
	}
	.notPreferred {
		color: gray;
	}
	.preferred {
		background-color: lightgreen;
	}
</style>

<spring:hasBindErrors name="patient">
	<spring:message code="fix.error"/>
	<div class="error">
		<c:forEach items="${errors.allErrors}" var="error">
			<spring:message code="${error.code}" text="${error.code}"/><br/><!-- ${error} -->
		</c:forEach>
	</div>
</spring:hasBindErrors>

<h2><spring:message code="Patient.merge.title"/></h2>

<spring:message code="Patient.merge.warning" />

<br/><br/>
<div id="selectionList" style="display:none">
<b class="boxHeader"><spring:message code="Select a Preferred Patient" /></b>
<div class="box" style="max-height:160px; overflow:auto">
<table class="box" cellspacing="2" cellpadding="2">
<tr><th></th><th><spring:message code="Patient.id"/></th><th><spring:message code="Patient.identifiers"/></th><th><spring:message code="PersonName.givenName"/></th><th><spring:message code="PersonName.middleName"/></th><th><spring:message code="PersonName.familyName"/></th><th><spring:message code="Person.age"/></th><th><spring:message code="Person.gender"/></th><th><spring:message code="Person.birthdate"/></th></tr>
<c:forEach items="${patientList}" var="patient" varStatus="status">
<tr id="${status.index}tr" class="<c:choose>
				<c:when test="${status.index % 2 == 0}">evenRow</c:when>
				<c:otherwise>oddRow</c:otherwise>
			</c:choose>"><td><input type="radio" id="${status.index}" name="preferred2" value="${patient.patientId}" onclick="display(this, true)" <c:if test="${patient.voided==true}">disabled</c:if>/></td><td>${patient.patientId}</td><td><c:forEach items="${patient.identifiers}" var="identifier" varStatus="entries">
							<c:if test="${entries.index==0}">${identifier}</c:if>
						</c:forEach> </td><c:forEach items="${patient.names}" var="name" varStatus="entries">
						<c:if test="${entries.index==0}">
			<td>${name.givenName}</td><td>${name.middleName}</td><td>${name.familyName}</td></c:if></c:forEach>
					<td>${patient.age}</td><td>
			<c:choose>
				<c:when test="${patient.gender == 'M'}">
					<img src="${pageContext.request.contextPath}/images/male.gif" />
				</c:when>
				<c:when test="${patient.gender == 'F'}">
					<img src="${pageContext.request.contextPath}/images/female.gif" />
				</c:when>
				<c:otherwise>${patient.gender}</c:otherwise>
			</c:choose>
		</td><td><openmrs:formatDate date="${patient.birthdate}" type="short" /></td></tr>
</c:forEach>
</table>
<input type="hidden" id="PatientNames"/>
<input type="hidden" id="PatientIdentifiers"/>
<input type="hidden" id="PatientAddress"/>
<input type="hidden" id="PatientInfos"/>
<input type="hidden" id="Encounters"/>
</div>
</div>
<br/><br/>
<form method="post">
	<table width="100%" id="patientTable" cellpadding="1" cellspacing="0">
		<colgroup>
			<col width="46%" id="left" class="preferred">
			<col width="22">
			<col id="right"<c:if test="${patient2.patientId != null}">class="notPreferred"</c:if>>
		</colgroup>
		<tr>
			<td width="46%"></td>
			<td align="center" valign="middle" rowspan="9" id="patientDivider">
				<img src="${pageContext.request.contextPath}/images/leftArrow.gif"/>
			</td>
			<td></td>
		</tr>
		<c:if test="${patient2.patientId != null}">
			<tr>
				<td valign="top">
					<h4 id="p1">
						<input type="radio" name="preferred1" id="${patient1.patientId}preferred" value="${patient1.patientId}" onclick="if (this.checked) changePrimary('left')" <c:if test="${!patient1.voided}">checked</c:if><c:if test="${patient1.voided}">disabled</c:if> />
						<label for="${patient1.patientId}preferred"><spring:message code="Patient.merge.preferred" /></label>
						<c:if test="${patient1.voided}">
						<div class="retiredMessage">
							<div><spring:message code="Patient.voided"/></div>
						</div>
					</c:if>
					</h4>
					<h4 id="np1">
						<center><b><spring:message code="Patient.merge.preferred"/></b></center>
						<div class="retiredMessage" id="voidCheck1" style="display:none">
							<div><spring:message code="Patient.voided"/></div>
						</div>
					</h4>
				</td>
				<td valign="top">
					<h4 id="p2">
						<input type="radio" name="preferred1" id="${patient2.patientId}preferred" value="${patient2.patientId}" onclick="if (this.checked) changePrimary('right')" <c:if test="${patient2.voided}">disabled</c:if>/>
						<label for="${patient2.patientId}preferred"><spring:message code="Patient.merge.preferred" /></label>
						<c:if test="${patient2.voided}">
							<div class="retiredMessage">
								<div><spring:message code="Patient.voided"/></div>
							</div>
						</c:if>
					</h4>
					<h4 id="np2">
						<center><b><spring:message code="Patient.merge.notPreferred"/></b></center>
						<div class="retiredMessage" id="voidCheck2" style="display:none">
							<div><spring:message code="Patient.voided"/></div>
						</div>
					</h4>
				</td>
			</tr>
		</c:if>
		<tr id="tabsRow">
			<td></td>
			<td class="mergePatientsTabsColumn">
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr id="tabsRowRight"><td class="mergePatientsTabSpaces" width="10px">&nbsp;</td><td id="lastTab" class="mergePatientsTabSpaces"></td>
				</tr></table>
			</td>
		</tr>
		<tr>
			<td valign="top">
				<h4><spring:message code="Patient.names"/></h4>
				<ol id="name1">
					<c:forEach items="${patient1.names}" var="name">
							<li>${name.givenName} ${name.middleName} ${name.familyName}
						</c:forEach>
				</ol>
			</td>
			<c:if test="${patient2.patientId == null}">
				<td rowspan="6" valign="top">
					<h4><spring:message code="Patient.select"/></h4>
					<div dojoType="PatientSearch" widgetId="pSearch"></div>
				</td>
			</c:if>
			<c:if test="${patient2.patientId != null}">
				<td valign="top">
					<h4><spring:message code="Patient.names"/></h4>
					<ol id="name2">
						<c:forEach items="${patient2.names}" var="name">
							<li>${name.givenName} ${name.middleName} ${name.familyName}
						</c:forEach>
					</ol>
				</td>
			</c:if>
		</tr>
		<tr>
			<td valign="top">
				<h4><spring:message code="Patient.identifiers"/></h4>
				<ol id="identifier1">
					<c:forEach items="${patient1.identifiers}" var="identifier">
						<li>${identifier.identifier} ${identifier.identifierType.name}
					</c:forEach>
				</ol>
			</td>
			<c:if test="${patient2.patientId != null}">
				<td valign="top">
					<h4><spring:message code="Patient.identifiers"/></h4>
					<ol id="identifier2">
						<c:forEach items="${patient2.identifiers}" var="identifier">
							<li>${identifier.identifier} ${identifier.identifierType.name}
						</c:forEach>
					</ol>
				</td>
			</c:if>
		</tr>
		<tr>
			<td valign="top">
				<h4><spring:message code="Patient.addresses"/></h4>
				<ol id="address1">
					<c:forEach items="${patient1.addresses}" var="address">
						<li>${address.address1} ${address.address2} ${address.cityVillage}
					</c:forEach>
				</ol>
			</td>
			<c:if test="${patient2.patientId != null}">
				<td valign="top">
					<h4><spring:message code="Patient.addresses"/></h4>
					<ol id="address2">
						<c:forEach items="${patient2.addresses}" var="address">
							<li>${address.address1} ${address.address2} ${address.cityVillage}
						</c:forEach>
					</ol>
				</td>
			</c:if>
		</tr>
		<tr>
			<td valign="top">
				<h4><spring:message code="Patient.information"/></h4>
				<c:set var="patient" value="${patient1}" />


	<table>
		<tr>
			<th align="left"><spring:message code="general.id"/></th>
			<td id="info10">${patient.patientId}</td>
		</tr>
		<tr>
			<th align="left"><spring:message code="Person.gender"/></th>
			<td id="info11">
				<c:choose>
					<c:when test="${patient.gender == 'M'}">
						<img src="${pageContext.request.contextPath}/images/male.gif" />
					</c:when>
					<c:when test="${patient.gender == 'F'}">
						<img src="${pageContext.request.contextPath}/images/female.gif" />
					</c:when>
					<c:otherwise>${patient.gender}</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<th align="left"><spring:message code="Person.birthdate"/></th>
			<td id="info12"><openmrs:formatDate date="${patient.birthdate}" type="short" /></td>
		</tr>
		
		<tr>
			<th align="left"><spring:message code="Person.deathDate"/></th>
			<td id="info13"><openmrs:formatDate date="${patient.deathDate}" type="short" /></td>
		</tr>
		<tr>
			<th align="left"><spring:message code="general.createdBy" /></th>
			<td id="info14">
				${patient.creator.personName} -
				<openmrs:formatDate date="${patient.dateCreated}" type="long" />
			</td>
		</tr>
		<tr>
			<th align="left"><spring:message code="general.changedBy" /></th>
			<td id="info15">
				${patient.changedBy.personName} -
				<openmrs:formatDate date="${patient.dateChanged}" type="long" />
			</td>
		</tr>
		<tr>
			<th align="left"><spring:message code="general.voided"/></th>
			<td id="info16">
				<c:choose>
					<c:when test="${patient.voided}">
						<spring:message code="general.yes"/>
					</c:when>
					<c:otherwise>
						<spring:message code="general.no"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>

			</td>
			<c:if test="${patient2.patientId != null}">
				<td valign="top">
					<h4><spring:message code="Patient.information"/></h4>
					<c:set var="patient" value="${patient2}" />

	<table>
		<tr>
			<th align="left"><spring:message code="general.id"/></th>
			<td id="info20">${patient.patientId}</td>
		</tr>
		<tr>
			<th align="left"><spring:message code="Person.gender"/></th>
			<td id="info21">
				<c:choose>
					<c:when test="${patient.gender == 'M'}">
						<img src="${pageContext.request.contextPath}/images/male.gif" />
					</c:when>
					<c:when test="${patient.gender == 'F'}">
						<img src="${pageContext.request.contextPath}/images/female.gif" />
					</c:when>
					<c:otherwise>${patient.gender}</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<th align="left"><spring:message code="Person.birthdate"/></th>
			<td id="info22"><openmrs:formatDate date="${patient.birthdate}" type="short" /></td>
		</tr>
		
		<tr>
			<th align="left"><spring:message code="Person.deathDate"/></th>
			<td id="info23"><openmrs:formatDate date="${patient.deathDate}" type="short" /></td>
		</tr>
		<tr>
			<th align="left"><spring:message code="general.createdBy" /></th>
			<td id="info24">
				${patient.creator.personName} -
				<openmrs:formatDate date="${patient.dateCreated}" type="long" />
			</td>
		</tr>
		<tr>
			<th align="left"><spring:message code="general.changedBy" /></th>
			<td id="info25">
				${patient.changedBy.personName} -
				<openmrs:formatDate date="${patient.dateChanged}" type="long" />
			</td>
		</tr>
		<tr>
			<th align="left"><spring:message code="general.voided"/></th>
			<td id="info26">
				<c:choose>
					<c:when test="${patient.voided}">
						<spring:message code="general.yes"/>
					</c:when>
					<c:otherwise>
						<spring:message code="general.no"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>

				</td>
			</c:if>
		</tr>
		<tr>
			<td valign="top">
				<h4><spring:message code="Patient.encounters"/></h4>
				<ol id="encounter1">
					<c:forEach items="${patient1Encounters}" var="encounter">
						<li>
							${encounter.encounterType.name}
							${encounter.location.name}
							<openmrs:formatDate date="${encounter.encounterDatetime}" type="short" />
							<a href="${pageContext.request.contextPath}/admin/encounters/encounter.form?encounterId=${encounter.encounterId}">
								<spring:message code="general.view"/>
							</a>
					</c:forEach>
				</ol>
			</td>
			<c:if test="${patient2.patientId != null}">
				<td valign="top">
					<h4><spring:message code="Patient.encounters"/></h4>
					<ol id="encounter2">
						<c:forEach items="${patient2Encounters}" var="encounter">
							<li>
								${encounter.encounterType.name}
								${encounter.location.name}
								<openmrs:formatDate date="${encounter.encounterDatetime}" type="short" />
								<a href="${pageContext.request.contextPath}/admin/encounters/encounter.form?encounterId=${encounter.encounterId}">
									<spring:message code="general.view"/>
								</a>
						</c:forEach>
					</ol>
				</td>
			</c:if>
		</tr>
		<tr>
			<td>
				<a href="patient.form?patientId=${patient1.patientId}" id="edit1"><spring:message code="Patient.edit"/></a>
			</td>
			<c:if test="${patient2.patientId != null}">
				<td>
					<a href="patient.form?patientId=${patient2.patientId}" id="edit2"><spring:message code="Patient.edit"/></a>
				</td>
			</c:if>
			</tr>
	</table>
	
	<c:if test="${patient2.patientId != null}">
		<br />
		<input type="submit" name="action" value='<spring:message code="Patient.merge"/>' onclick="return generateMergeList();" >
		<input type="hidden" id="pref" name="preferred" value=""/>
		<input type="hidden" id="nonPref" name="nonPreferred" value=""/>
		<input type="hidden" name="redirectURL" value='<request:header name="referer" />' />
	</c:if>
</form>

<br/>

<%@ include file="/WEB-INF/template/footer.jsp" %>