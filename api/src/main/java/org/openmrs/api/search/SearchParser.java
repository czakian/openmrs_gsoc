package org.openmrs.api.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;

public interface SearchParser {
	
	public void setFields(String fields);
	
	public void getFields();
	
	public void setAnalyzer(Analyzer analyzer);
	
	public Analyzer getAnalyzer();
	
	/*
	 * check to make sure a user is authorized to search
	 * on the fields we want to parse and exclude fields
	 * the user shouldn't see
	 */
	public boolean isAuthorized();
	
	public QueryParser parseSyntax(String searchString);
	
}
