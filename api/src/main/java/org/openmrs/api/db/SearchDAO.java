package org.openmrs.api.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.api.search.SearchParser;

public interface SearchDAO {
	
	public void openFullTextSession();
	
	public void closeFullTextSession();
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public SessionFactory getSessionFactory();
	
	public void indexExistingData();
	
	public void setSearchParser(SearchParser parser);
	
	/*
	 * leave the option for us to make our own search parser
	 */
	public SearchParser getSearchParser();
	
	public void setSearchString(String searchString);
	
	public String getSearchString();
	
	public List search(String param, Class clazz, String[] fields);
	
	public List search();
	
}
