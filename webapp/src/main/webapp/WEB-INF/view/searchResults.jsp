<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ include file="/WEB-INF/template/header.jsp" %>

<script type="text/javascript">
        function getResults(query) {
                $j.ajax({
                        url: "<openmrs:contextPath/>/getSearchResults.json",
                        data: {q: query},
                        success: setResults
                });
        }

        function setResults(results) {
                $j("#loading").fadeOut("fast", function(){
                        $j("#heading").fadeIn("fast", function(){
                                if (results == null || results.mapList == null || results.mapList.length == 0)
                                        $j("#noresults").fadeIn();
                                else {
                                        $j("#results").html("");
                                        $j(results).each(function(index, value){
                                                $j("#results").append(index + ": " + value);
                                        });
                                        $j("#results").fadeIn();
                                }
                        });
                });
        };

        function updateResults(query){
                $j("#results").fadeOut("fast", function(){
                        $j("#noresults").fadeOut("fast", function(){
                                $j("#heading").fadeOut("fast", function(){
                                        $j("#loading").fadeIn("fast", function() {
                                                getResults(query);
                                        })
                                }) 
                        })
                });
        }

        $j(document).ready(function(){
                getResults("${q}");
                $j("#search input:submit").click(function(){
                        updateResults($j("#search input[name=q]").val());
                        return false;
                })
        });
        
</script>

<form id="search" action="#" method="GET" style="text-align:center; font-size:2em; white-wrap:none; padding-bottom: 1em;">
        <input type="text" name="q" value="${q}"/>
        <input type="submit" value="<spring:message code="Search.search"/>"/>
</form>

<div id="loading" style="text-align:center; font-style:italic; font-size:1.25em;">
        Loading...
</div>

<h2 id="heading" style="display:none"><spring:message code="Search.title"/></h2>

<div id="noresults" style="display:none;">
        <spring:message code="Search.no-results"/>
</div>

<div id="results" style="display:none">
        <div class="result">
                <a class="title" href="#">test title</a>
                <span class="description">test description</span>
        </div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
