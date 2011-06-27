package org.openmrs.api.search;

import java.util.Map;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

public interface SearchParser {
	
	public void setFields(String[] fields);
	
	public String[] getFields();
	
	public void setAnalyzer(Analyzer analyzer);
	
	public Analyzer getAnalyzer();
	
	public String parseSyntax(String searchString, Map<Pattern, ChainedParser<String, String>> links);
	
	public Query parse(String searchString);
	
}
