package org.openmrs.api.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.api.SearchService;
import org.openmrs.api.db.PatientDAO;
import org.openmrs.api.db.SearchDAO;
import org.openmrs.api.search.SearchParser;

public class SearchServiceImpl implements SearchService {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	private SearchDAO dao;
	
	@Override
	public void onStartup() {
		dao.openFullTextSession();
	}
	
	@Override
	public void onShutdown() {
		dao.closeFullTextSession();
	}
	
	@Override
	public void setSearchDAO(SearchDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public void openFullTextSession() {
		dao.openFullTextSession();
	}
	
	@Override
	public void closeFullTextSession() {
		dao.closeFullTextSession();
	}
	
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		dao.setSessionFactory(sessionFactory);
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return dao.getSessionFactory();
	}
	
	@Override
	public void indexExistingData() {
		dao.indexExistingData();
	}
	
	@Override
	public void setSearchParser(SearchParser parser) {
		dao.setSearchParser(parser);
	}
	
	@Override
	public SearchParser getSearchParser() {
		return dao.getSearchParser();
	}
	
	@Override
	public void setSearchString(String searchString) {
		dao.setSearchString(searchString);
	}
	
	@Override
	public String getSearchString() {
		return dao.getSearchString();
	}
	
	@Override
	public List search() {
		return dao.search();
	}
	
	@Override
	public List search(String param, Class clazz, String[] fields) {
		return dao.search(param, clazz, fields);
	}
}
