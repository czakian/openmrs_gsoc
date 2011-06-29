package org.openmrs.api.search;

import java.util.Map;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;

public interface SearchParser {
	
	public void setFields(String[] fields);
	
	public String[] getFields();
	
	public void setAnalyzer(Analyzer analyzer);
	
	public Analyzer getAnalyzer();

	public void setSyntax(Map<Pattern, ChainedParser<String,String>> links);

	public void addSyntax(Pattern pattern, ChainedParser<String,String> link);

	public void setFlags(Occur[] flags);

	public Occur[] getFags();

	public Map<Pattern, ChainedParser<String,String>>  getSyntax();
	
	public String parseSyntax(String searchString, Map<Pattern, ChainedParser<String, String>> links);
	
	public Query parse(String searchString);
	
}
