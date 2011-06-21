<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage Tokens" otherwise="/login.htm" redirect="/module/logic/manageTokens.list" />

<%@ include file="localHeader.jsp"%>

<openmrs:htmlInclude file="/moduleResources/logic/css/datatables.css" />
<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />
<openmrs:htmlInclude file="/moduleResources/logic/js/jquery.dataTables.min.js" />

<script>
	$j = jQuery.noConflict();
	$j(document).ready(function() {
			$j('.datatable').dataTable( {
				"bProcessing": true,
				"bServerSide": true,
				"sAjaxSource": "listTokensQuery.form",
				"bSort": false,
				"bPaginate": true,
				"bLengthChange": false,
				"iDisplayLength": 20,
				"oSearch": { sSearch: "" },
				"aoColumns": [
					{ "fnRender": function(oObj) { return '<a href="logic.form?token=' + oObj.aData[1] + '"><spring:message code="logic.token.manage.test"/></a>'; } },
					{ "fnRender": function(oObj) { return '<a href="editTokenRegistration.form?id=' + oObj.aData[4] + '">' + oObj.aData[1] + '</a>'; } },
					{ "fnRender": function(oObj) { return oObj.aData[2].split(".").pop() } },
					null,
					{ "bVisible": false }
				]
			});
	});
</script>

<h2><spring:message code="logic.token.manage.title"/></h2>

<table class="datatable">
	<thead>
		<tr>
			<th></th>
			<th><spring:message code="logic.TokenRegistration.token"/></th>
			<th><spring:message code="logic.TokenRegistration.ruleProvider"/></th>
			<th><spring:message code="logic.TokenRegistration.configuration"/></th>
			<th>ID</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>

<br/>
<br/>

<%@ include file="/WEB-INF/template/footer.jsp"%>
