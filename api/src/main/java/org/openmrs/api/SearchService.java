package org.openmrs.api;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.api.db.SearchDAO;
import org.openmrs.api.search.SearchParser;
import org.springframework.transaction.annotation.Transactional;

//@Transactional
public interface SearchService extends OpenmrsService {
	
	public void setSearchDAO(SearchDAO dao);
	
	public void openFullTextSession();
	
	public void closeFullTextSession();
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public SessionFactory getSessionFactory();
	
	//@Transactional(readOnly = true)
	public void indexExistingData();
	
	public void setSearchParser(SearchParser parser);
	
	public SearchParser getSearchParser();
	
	public void setSearchString(String searchString);
	
	public String getSearchString();
	
	//@Transactional
	public List search();
}
