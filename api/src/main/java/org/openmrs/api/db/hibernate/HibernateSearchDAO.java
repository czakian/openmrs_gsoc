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
import org.openmrs.util.OpenmrsConstants;

public class HibernateSearchDAO implements SearchDAO {
	
	private SearchParser parser;
	
	private SessionFactory sessionFactory;
	
	private FullTextSession fullTextSession;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private Class entity;
	
	private FullTextSession getFullTextSession() {
		if (fullTextSession == null)
			this.openFullTextSession();
		return fullTextSession;
	}
	
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
		try {
			this.getFullTextSession().createIndexer().startAndWait();
		}
		catch (InterruptedException e) {
			log.debug(e);
		}
	}
	
	@Override
	public void setSearchParser(SearchParser parser) {
		this.parser = parser;
	}
	
	@Override
	public SearchParser getSearchParser() {
		return parser;
	}
	
	@Override
	public List search(String param, Class clazz, String[] fields) {
		this.openFullTextSession();
		
		Query query = null;
		List result = null;
		try {
			query = new MultiFieldQueryParser(OpenmrsConstants.LUCENE_VERSION, fields, new StandardAnalyzer(
			        OpenmrsConstants.LUCENE_VERSION)).parse(param);
			
			// wrap Lucene query in a org.hibernate.Query
			org.hibernate.Query hibQuery = this.getFullTextSession().createFullTextQuery(query, clazz);
			
			// execute search
			result = hibQuery.list();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List search(String searchString) {
		if (fullTextSession == null)
			this.openFullTextSession();
		
		return this.getFullTextSession().createFullTextQuery(parser.parse(searchString), entity).list();
	}
	
	@Override
	public List search() {
		this.openFullTextSession();
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = this.getFullTextSession().createFullTextQuery((Query) getSearchParser(), getEntity());
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
