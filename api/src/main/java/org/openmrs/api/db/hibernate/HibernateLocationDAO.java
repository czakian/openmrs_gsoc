/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.api.db.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.LocationDAO;

/**
 * Hibernate location-related database functions
 */
public class HibernateLocationDAO implements LocationDAO {
	
	private SessionFactory sessionFactory;
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#setSessionFactory(org.hibernate.SessionFactory)
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#saveLocation(org.openmrs.Location)
	 */
	public Location saveLocation(Location location) {
		if (location.getChildLocations() != null && location.getLocationId() != null) {
			// hibernate has a problem updating child collections
			// if the parent object was already saved so we do it 
			// explicitly here
			for (Location child : location.getChildLocations())
				if (child.getLocationId() == null)
					saveLocation(child);
		}
		
		sessionFactory.getCurrentSession().saveOrUpdate(location);
		return location;
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getLocation(java.lang.Integer)
	 */
	public Location getLocation(Integer locationId) {
		return (Location) sessionFactory.getCurrentSession().get(Location.class, locationId);
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getLocation(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Location getLocation(String name) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Location.class).add(
		    Expression.eq("name", name));
		
		List<Location> locations = criteria.list();
		if (null == locations || locations.isEmpty()) {
			return null;
		}
		return locations.get(0);
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getAllLocations(boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<Location> getAllLocations(boolean includeRetired) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Location.class);
		if (!includeRetired) {
			criteria.add(Expression.eq("retired", false));
		} else {
			//push retired locations to the end of the returned list
			criteria.addOrder(Order.asc("retired"));
		}
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#deleteLocation(org.openmrs.Location)
	 */
	public void deleteLocation(Location location) {
		sessionFactory.getCurrentSession().delete(location);
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#saveLocation(org.openmrs.Location)
	 */
	public LocationTag saveLocationTag(LocationTag tag) {
		sessionFactory.getCurrentSession().saveOrUpdate(tag);
		return tag;
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getLocationTag(java.lang.Integer)
	 */
	public LocationTag getLocationTag(Integer locationTagId) {
		return (LocationTag) sessionFactory.getCurrentSession().get(LocationTag.class, locationTagId);
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getLocationTagByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public LocationTag getLocationTagByName(String tag) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTag.class).add(
		    Expression.eq("name", tag));
		
		List<LocationTag> tags = criteria.list();
		if (null == tags || tags.isEmpty()) {
			return null;
		}
		return tags.get(0);
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getAllLocationTags(boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<LocationTag> getAllLocationTags(boolean includeRetired) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTag.class);
		if (!includeRetired) {
			criteria.add(Expression.like("retired", false));
		}
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getLocations(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<LocationTag> getLocationTags(String search) {
		return sessionFactory.getCurrentSession().createCriteria(LocationTag.class)
		// 'ilike' case insensitive search
		        .add(Expression.ilike("name", search, MatchMode.START)).addOrder(Order.asc("name")).list();
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#deleteLocationTag(org.openmrs.LocationTag)
	 */
	public void deleteLocationTag(LocationTag tag) {
		sessionFactory.getCurrentSession().delete(tag);
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getLocationByUuid(java.lang.String)
	 */
	public Location getLocationByUuid(String uuid) {
		return (Location) sessionFactory.getCurrentSession().createQuery("from Location l where l.uuid = :uuid").setString(
		    "uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getLocationTagByUuid(java.lang.String)
	 */
	@Override
	public LocationTag getLocationTagByUuid(String uuid) {
		return (LocationTag) sessionFactory.getCurrentSession().createQuery("from LocationTag where uuid = :uuid")
		        .setString("uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.api.db.LocationDAO#getCountOfLocations(String, Boolean)
	 */
	@Override
	public Long getCountOfLocations(String nameFragment, Boolean includeRetired) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Location.class);
		if (!includeRetired)
			criteria.add(Expression.eq("retired", false));
		
		if (StringUtils.isNotBlank(nameFragment))
			criteria.add(Expression.ilike("name", nameFragment, MatchMode.START));
		
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	/**
	 * @see LocationDAO#getLocations(String, Integer, Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Location> getLocations(String nameFragment, boolean includeRetired, Integer start, Integer length)
	        throws DAOException {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Location.class);
		if (!includeRetired)
			criteria.add(Restrictions.eq("retired", false));
		
		if (StringUtils.isNotBlank(nameFragment))
			criteria.add(Restrictions.ilike("name", nameFragment, MatchMode.START));
		
		criteria.addOrder(Order.asc("name"));
		if (start != null)
			criteria.setFirstResult(start);
		if (length != null && length > 0)
			criteria.setMaxResults(length);
		
		return criteria.list();
	}
	
	/**
	 * @see LocationDAO#getRootLocations(boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Location> getRootLocations(boolean includeRetired) throws DAOException {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Location.class);
		if (!includeRetired)
			criteria.add(Expression.eq("retired", false));
		
		criteria.add(Expression.isNull("parentLocation"));
		
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}
	
}
