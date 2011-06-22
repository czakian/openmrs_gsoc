package org.openmrs.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.openmrs.Person;
import org.openmrs.api.db.SearchDAO;
import org.openmrs.api.search.SearchParser;

public class HibernateSearchDAO implements SearchDAO {
	
	public static final Version LUCENE_VERSION = Version.LUCENE_31;
	
	private SearchParser parser;
	
	private SessionFactory sessionFactory;
	
	private FullTextSession fullTextSession;
	
	private Class entity;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	public void openFullTextSession() {
		fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
	}
	
	@Override
	public void closeFullTextSession() {
		fullTextSession.close();
	}
	
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	public void indexExistingData() {
		if (fullTextSession == null)
			this.openFullTextSession();
		
		try {
			fullTextSession.createIndexer().startAndWait();
		}
		catch (InterruptedException e) {
			log.debug(e);
		}
	}
	
	@Override
	public void setSearchParser(SearchParser parser) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public SearchParser getSearchParser() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setSearchString(String searchString) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getSearchString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List search(String param, Class clazz, String[] fields) {
		if (fullTextSession == null)
			this.openFullTextSession();
		
		//not really sure why hibernate examples had this here. 
		//but they never use it...soooo?
		//QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
		Query query = null;
		List result = null;
		try {
			query = new MultiFieldQueryParser(LUCENE_VERSION, fields, new StandardAnalyzer(LUCENE_VERSION)).parse(param);
			// wrap Lucene query in a org.hibernate.Query
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Person.class);
			
			// execute search
			result = hibQuery.list();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public List search() {
		if (fullTextSession == null)
			this.openFullTextSession();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery((Query) getSearchParser(), getEntity());
		List result = hibQuery.list();
		return result;
	}
	
	@Override
	public void setEntity(Class entity) {
		this.entity = entity;
	}
	
	@Override
	public Class getEntity() {
		return entity;
	}
}
