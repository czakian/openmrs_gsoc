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
                var totals = {
                        'Person': 0,
                        'Patient': 0,
                        'Drug': 0,
                        'Concept': 0
                }
                
                if (results == null || results.mapList == null || results.mapList.length == 0) {
                        $j("#filter").hide();
                        $j("#noresults").show();
                } else {
                        $j("#results").html("");
                        $j(results.mapList).each(function(index, value){
                                $j("#results").append(renderSnippet(index, value));
                        });
                        $j("#sizeTotal").html(results.mapList.length);
                        $j("#filter").show();
                        $j("#results").show();
                }
                $j("#resultsBlock").fadeIn();
        };

        function updateResults(query){
                $j("#resultsBlock").fadeOut("fast", function(){
                        $j("#results").hide();
                        $j("#noresults").hide();
                        getResults(query);
                });
        }

        function renderSnippet(index, value) {
                var snippet = '<div id="searchResult' + index + '" class="result '+ value['class'] + '">';
                snippet += renderLink(value);
                snippet += '<div class="description">' + value['description'] + '</div>';
                snippet += '</div>\n';
                return snippet;
        }

        function renderLink(obj) {
                var urlMap = {
                        'Patient': '<openmrs:contextPath/>/patientDashboard.form?patientId={id}',
                        'Person': '<openmrs:contextPath/>/personDashboard.form?personId={id}',
                        'Drug': '<openmrs:contextPath/>/dictionary/concept.htm?conceptId={id}'
                }
                var urlPattern = urlMap[obj['class']].replace("{id}", obj['id']);
                return '<a href="' + urlPattern + '">' + obj['title'] + '</a>';
        }

        $j(document).ready(function(){
                getResults("${q}");
                $j("#search input:submit").click(function(){
                        updateResults($j("#search input[name=q]").val());
                        return false;
                })
        });
        
</script>

<style type="text/css">
        .result { margin: 0 0 1em; min-height: 2em; }
        .resultType { font-size: 0.75em; }
        .Person, .Patient {
                background: transparent url(<openmrs:contextPath/>/images/male.gif) no-repeat left 3px;
                padding-left: 24px;
        }
        .result .description { color: #333; margin:0; padding:0; }
        
        #search {padding-left: 10.5em; font-size:1em; white-wrap:none; padding-bottom: 1em;}
        #search input[type="text"] { width: 80%; }
        #searchHelp { font-size: 0.85em; }
        #resultsBlock { width: 60%; }
        #filter { float:left; width: 10em; }
        #filter ul { margin-left: 0; padding-left: 0; list-style: none; }
        #results { margin-left: 10em; border-left: 1px solid #aaa; }
        #content { text-align: center; }
        #searchBlock { margin: 0 auto; width: 60%; text-align: left; }
</style>

<div id="searchBlock">
        <form id="search" action="#" method="GET">
                <input type="text" name="q" value="${q}"/>
                <input type="submit" value="<spring:message code="Search.search"/>"/>
                <div id="searchHelp">
                        Hint: use <a href="http://lucene.apache.org/java/2_4_0/queryparsersyntax.html">wildcards and other characters</a> in your search to increase your results.
                </div>
        </form>

        <div id="resultsBlock" style="display:none">
                <div id="filter">
                        Filter <span id="sizeTotal">these</span> results:
                        <ul>
                                <li><a href="#">Patients</a> (<span id="sizePatients">#</span>)</li>
                                <li><a href="#">People</a> (<span id="sizePeople">#</span>)</li>
                                <li><a href="#">Drugs</a> (<span id="sizeDrugs">#</span>)</li>
                                <li><a href="#">Concepts</a> (<span id="sizeConcepts">#</span>)</li>
                        </ul>
                </div>
                <div id="results" style="display:none">
                Loading ...
                </div>
                <div id="noresults" style="display:none;">
                        <spring:message code="Search.no-results"/>
                </div>
        </div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
