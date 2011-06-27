package org.openmrs.api.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.api.search.SearchParser;

public interface SearchDAO {
	
	public void openFullTextSession();
	
	public void closeFullTextSession();
	
	public void setEntity(Class entity);
	
	public Class getEntity();
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public SessionFactory getSessionFactory();
	
	public void indexExistingData();
	
	public void setSearchParser(SearchParser parser);
	
	public SearchParser getSearchParser();
	
	public List search(String param, Class clazz, String[] fields);
	
	public List search();
	
	public List search(String searchString);
	
}
