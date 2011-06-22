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
	
	private SearchParser parser;
	
	private SessionFactory sessionFactory;
	
	private FullTextSession fullTextSession;
	
	protected final Log log = LogFactory.getLog(getClass());
	
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
		this.openFullTextSession();
		
		// QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
		Query query = null;
		List result = null;
		try {
			query = new MultiFieldQueryParser(Version.LUCENE_31, fields, new StandardAnalyzer(Version.LUCENE_31))
			        .parse(param);
			// wrap Lucene query in a org.hibernate.Query
			org.hibernate.Query hibQuery = this.getFullTextSession().createFullTextQuery(query, clazz);
			
			// execute search
			result = hibQuery.list();
			
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public List search() {
		Transaction tx = null;
		List result = null;
		
		//question: how are transactions managed, and why can't I run one.
		//I suspect this has to do with spring having its own transaction manager?
		//probably should ask Jeremy about this one.
		try {
			//	tx = fullTextSession.beginTransaction();
			QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();
			org.apache.lucene.search.Query query = qb.keyword().fuzzy().onField("gender").boostedTo(5).andField("personId")
			        .matching("1 F M").createQuery();
			
			// wrap Lucene query in a org.hibernate.Query
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Person.class);
			
			// execute search
			result = hibQuery.list();
			
			//tx.commit();
		}
		catch (HibernateException e) {
			rollbackIfNeeded(tx);
		}
		return result;
	}
	
	private void rollbackIfNeeded(Transaction tx) {
		if (tx != null && tx.isActive()) {
			tx.rollback();
		}
	}
}
