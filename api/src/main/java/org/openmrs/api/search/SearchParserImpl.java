package org.openmrs.api.search;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.openmrs.util.OpenmrsConstants;

/**
 *
 * @author czakian
 */
public class SearchParserImpl implements SearchParser {
	
	//lucene syntax characters
	private static final String[] SPECIALS = new String[]{
		"+", "-", "&&", "||", "!", "(", ")", "{", "}",
		"[", "]", "^", "\"", "~", "*", "?", ":", "\\"
	};
	
	private String[] fields;
	
	private Analyzer analyzer;
	
	private Occur[] flags;
	
	Map<Pattern, ChainedParser<String, String>> syntaxArgs;
	
	@Override
	public String parseSyntax(String queryString, Map<Pattern, ChainedParser<String, String>> links) {
		String parsedString = queryString;
		boolean hasMatch = true;
		Set<Pattern> patterns = links.keySet();
		
		while (!links.isEmpty() && hasMatch) {
			
			for (Pattern p : patterns) {
				if (p.matcher(parsedString).matches()) {
					parsedString = links.get(p).eval(parsedString);
					hasMatch = true;
					System.out.println("Match found at:" + p);
				} else {
					hasMatch = false;
				}
			}
			
		}
		System.out.println(parsedString);
		return parsedString;
	}
	
	@Override
	public Query parse(String queryString) {
		Query query = null;
		try {
			query = MultiFieldQueryParser.parse(
							OpenmrsConstants.LUCENE_VERSION, 
							parseSyntax(queryString, syntaxArgs),
							fields, 
							flags,
							analyzer
							);
		}
		catch (ParseException ex) {
			Logger.getLogger(SearchParserImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return query;
	}
	
	public Query parse(String[] queries) {
		Query query = null;
		try {
			query = MultiFieldQueryParser.parse(OpenmrsConstants.LUCENE_VERSION, queries, fields, analyzer);
		}
		catch (ParseException ex) {
			Logger.getLogger(SearchParserImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return query;
	}
	
	@Override
	public void setFields(String[] fields) {
		this.fields = fields;
	}
	
	@Override
	public String[] getFields() {
		return fields;
	}
	
	@Override
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
	@Override
	public Analyzer getAnalyzer() {
		return analyzer;
	}
	
	@Override
	public void setSyntax(Map<Pattern, ChainedParser<String, String>> links) {
		syntaxArgs = links;
	}
	
	@Override
	public void addSyntax(Pattern pattern, ChainedParser<String, String> link) {
		syntaxArgs.put(pattern, link);
	}
	
	@Override
	public Map<Pattern, ChainedParser<String, String>> getSyntax() {
		return syntaxArgs;
	}
	
	/*
	 *convenience method to escape special characters in lucene syntax
	 */
	public String escapeSpecials(String clientQuery) {
		String regexOr = "";
		for (String special : SPECIALS) {
			regexOr += (special
							.equals(SPECIALS[0]) ? "" : "|") + "\\"
							+ special.substring(0, 1);
		}
		clientQuery = clientQuery
						.replaceAll("(?<!\\\\)(" + regexOr + ")",
						"\\\\$1");
		return clientQuery.trim();
	}

	@Override
	public void setFlags(Occur[] flags) {
		this.flags = flags;
	}

	@Override
	public Occur[] getFags() {
		return flags;
	}

}
