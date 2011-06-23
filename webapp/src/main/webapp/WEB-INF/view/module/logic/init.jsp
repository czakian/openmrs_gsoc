<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage Tokens" otherwise="/login.htm" redirect="/admin/index.htm" />

<%@ include file="localHeader.jsp"%>

<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />
<script type="text/javascript">
	var $j = jQuery.noConflict();
</script>

<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<openmrs:htmlInclude file="/scripts/jquery-ui/css/redmond/jquery-ui-1.7.2.custom.css" />

<script type="text/javascript">
	var isRunning = false;

	function run() {
		var isRunning = false;
		$j("#loading").show();

		$j.ajax({
			url: "status.form",
			cache: false,
			dataType: "json",
			success: function(data) {
	    	 	isRunning = data.running;
			}
		});
      
      if (isRunning == true) {
   	   $j("#statusText").html("<spring:message code="logic.init.status.running"/>" );
   	  	followProgress();
      } else {
    	   $j("#statusText").html("<spring:message code="logic.init.status.running"/>" );
    	   $j("#runnow").hide();
    	  	$j.ajax({
	 		   type: "POST",
	 		   cache: false,
	 		   url: "load.form"
	 		});
    	  	followProgress();
      }
	}

	function followProgress() {
		var dots = "";
	  		
		var i = setInterval(function() { 
			$j.ajax({
				url: "status.form",
				cache: false,
				dataType: "json",
				success: function(data) {
					var object = eval(data);
					isRunning = object.running;
					dots = dots + ". ";
					if (dots.length > 10) dots = ""; 					
	            $j("#statusText").html("<spring:message code="logic.init.status.running"/>" + dots);

					if (data.running == false) {
						$j("#statusText").html('<spring:message code="logic.init.status.complete"/>');
						$j("#loading").hide();
						$j("#complete").show();
						clearInterval(i);
						return;
					}
					}
			});
		}, 1500);

	}
	
	$j(document).ready(function() {
		initRunning();
	});

	function initRunning() {
		$j.ajax({
			url: "status.form",
			cache: false,
			dataType: "json",
			success: function(data) {
		  		if (data.running == true) {
					$j("#runnow").hide();
					$j("#loading").show();
		   	   $j("#statusText").html("<spring:message code="logic.init.status.running"/>" );
		   	  	followProgress();
				}
			}
		});

	}
		
</script>

<h2><spring:message code="logic.init.title"/></h2>
<p style="width: 700px; white-space: normal;"><spring:message code="logic.init.description"/></p>

<p>
	<spring:message code="logic.init.propertyHelp"/>
	<openmrs:portlet url="globalProperties" parameters="propertyPrefix=logic.defaultTokens.conceptClasses|hidePrefix=false"/>
</p>

<input class="btn-submit" id="runnow" name="runnow" accesskey="r" value="<spring:message code="logic.init.submit.button"/>" type="button" style="width: 100px;" onclick="javascript:run();"/>

<p>
	<img id="loading" src="${pageContext.request.contextPath}/images/loading.gif" style="display: none; margin-right: 10px;"/>
	<img id="complete" src="${pageContext.request.contextPath}/images/checkmark.png" style="display: none; margin-right: 10px;"/>
	<span id="statusText" ></span></p>

<%@ include file="/WEB-INF/template/footer.jsp"%>
