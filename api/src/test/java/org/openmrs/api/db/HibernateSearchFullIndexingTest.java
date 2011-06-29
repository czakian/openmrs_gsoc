package org.openmrs.api.db;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.Assert;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.api.SearchService;
import org.openmrs.api.context.Context;
import org.openmrs.api.search.ChainedParser;
import org.openmrs.api.search.ParseAND;
import org.openmrs.api.search.ParseOR;
import org.openmrs.api.search.SearchParser;
import org.openmrs.api.search.SearchParserImpl;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.openmrs.util.OpenmrsConstants;

public class HibernateSearchFullIndexingTest extends BaseContextSensitiveTest {
	
	private SearchDAO dao = null;
	
	private SearchService sService = null;
	
	/**
	 * Run this before each unit test in this class. The "@Before" method in
	 * {@link BaseContextSensitiveTest} is run right before this method.
	 * 
	 * @throws Exception
	 */
	@Before
	public void runBeforeEachTest() throws Exception {
		
		if (dao == null)
			// fetch the dao from the spring application context
			// this bean name matches the name in /metadata/spring/applicationContext-service.xml
			dao = (SearchDAO) applicationContext.getBean("searchDAO");
		if (sService == null)
			sService = Context.getSearchService();
	}
	
	@Ignore
	@Test
	@Verifies(value = "should index existing data", method = "indexExistingData();>,null")
	public void indexExistingData_shouldindexexistingdata() {
		dao.openFullTextSession();
		dao.indexExistingData();
		//need to modify the test to figure out what is in the database now and search for it...
		System.out.println("indexed test data");
		Assert.assertTrue(true);
	}
	
	@Test
	public void search_shouldusecustomsyntax() {
		String searchString = "Hipp* and H*";
		Class entity = Person.class;
		String[] fields = new String[] {"gender", "names.familyName", "names.givenName"};
		Occur[] flags = new Occur[] { Occur.SHOULD, Occur.SHOULD, Occur.SHOULD};
			
		SearchParser parser = new SearchParserImpl();
		parser.setAnalyzer(new StandardAnalyzer(OpenmrsConstants.LUCENE_VERSION));
		parser.setFields(fields);
		parser.setFlags(flags);

		parser.setSyntax(new HashMap<Pattern,ChainedParser<String,String>>());
		parser.addSyntax(Pattern.compile(".*\\sor\\s.*"), new ParseOR());
		parser.addSyntax(Pattern.compile(".*\\sand\\s.*"), new ParseAND());

		dao.setSearchParser(parser);
		dao.setEntity(entity);
		List results = dao.search(searchString);

	for (Object s : results) {
			Person p = (Person) s;
			System.out.print(p.getId() + " ");
			System.out.print(p.getGender() + " ");
			System.out.print(p.getGivenName() + " ");
			System.out.println(p.getAge());
		}	
		
	//Assert.assertEquals(dao.search(searchString, entity, fields), results);
		
	}
	
	@Ignore
	@Test
	@Verifies(value = "should find indexed existing items with param", method = "search();>,null")
	public void search_shouldfindindexedexistingitemswithparam() {
		dao.openFullTextSession();
		List result = dao.search("M", Person.class, new String[] { "gender" });
		for (Object s : result) {
			Person p = (Person) s;
			System.out.print(p.getId() + " ");
			System.out.print(p.getGender() + " ");
			System.out.print(p.getGivenName() + " ");
			System.out.print(p.getAddresses() + " '");
			System.out.println(p.getAge());
			
		}
		Assert.assertNotNull(result);
	}
	
	@Ignore
	@Test
	@Verifies(value = "should find indexed existing items", method = "search();>,null")
	public void search_shouldfindindexedexistingitems() {
		dao.openFullTextSession();
		List result = dao.search();
		for (Object s : result) {
			Person p = (Person) s;
			System.out.print(p.getId() + " ");
			System.out.print(p.getGender() + " ");
			System.out.print(p.getGivenName() + " ");
			System.out.println(p.getAge());
			
		}
		Assert.assertNotNull(result);
	}
	
}
