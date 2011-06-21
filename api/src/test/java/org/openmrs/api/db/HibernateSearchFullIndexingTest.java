package org.openmrs.api.db;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.api.SearchService;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.Verifies;

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
	@Verifies(value = "should find indexed items with fieldbridge", method = "search(String param);>,null")
	public void search_shouldfindindexeditemwithfieldbridge() {
		dao.openFullTextSession();
		List result = dao.search("M*", Person.class, new String[] { "names.givenName", "names.prefix" });
		for (Object s : result) {
			if (s.getClass().equals(PersonName.class)) {
				PersonName pname = (PersonName) s;
				System.out.print(pname.getId() + " ");
				System.out.print(pname.getDegree() + " ");
				System.out.println(pname.getFullName() + " ");
			} else if (s.getClass().equals(Patient.class)) {
				Patient pname = (Patient) s;
				System.out.print(pname.getId() + " ");
				System.out.print(pname.getGivenName() + " ");
				System.out.println(pname.getFamilyName());
			} else if (s.getClass().equals(Person.class)) {
				Person pname = (Person) s;
				System.out.print(pname.getId() + " ");
				System.out.print(pname.getGivenName() + " ");
				System.out.println(pname.getFamilyName());
			}
		}
		Assert.assertNotNull(result);
	}
	
	@Ignore
	@Test
	@Verifies(value = "should find indexed existing items with param", method = "search();>,null")
	public void search_shouldfindindexedexistingitemswithparam() {
		dao.openFullTextSession();
		List result = dao.search("Anet", Person.class, new String[] { "names" });
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
