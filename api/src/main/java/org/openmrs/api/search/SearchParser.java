package org.openmrs.api.search;

import java.util.Map;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;

public interface SearchParser {
	
	public void setFields(String fields);
	
	public void getFields();
	
	public void setAnalyzer(Analyzer analyzer);
	
	public Analyzer getAnalyzer();
	
	public boolean isAuthorized();
	
	public QueryParser parseSyntax(String searchString, Map<Pattern, ChainedParser<String, String>> links);
	
}
