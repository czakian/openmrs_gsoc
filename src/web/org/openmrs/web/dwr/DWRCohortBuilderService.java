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
package org.openmrs.web.dwr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.api.context.Context;
import org.openmrs.cohort.CohortSearchHistory;
import org.openmrs.reporting.AbstractReportObject;
import org.openmrs.reporting.PatientFilter;
import org.openmrs.reporting.PatientSearch;
import org.openmrs.reporting.PatientSearchReportObject;
import org.openmrs.reporting.PatientSet;
import org.openmrs.reporting.ReportObject;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;

public class DWRCohortBuilderService {

	protected final Log log = LogFactory.getLog(getClass());
	
	public Integer getResultCountForFilterId(Integer filterId) {
		PatientFilter pf = Context.getReportService().getPatientFilterById(filterId);
		if (pf == null)
			return null;
		PatientSet everyone = Context.getPatientSetService().getAllPatients();
		PatientSet filtered = pf.filter(everyone);
		return filtered.size();
	}
	
	private CohortSearchHistory getMySearchHistory() {
		return (CohortSearchHistory) Context.getVolatileUserData("CohortBuilderSearchHistory");
	}
	
	/**
	 * @param index
	 * @return the number of patients in the resulting PatientSet
	 */
	public Integer getResultCountForSearch(int index) {
		CohortSearchHistory history = getMySearchHistory();
		PatientSet ps = history.getPatientSet(index);
		return ps.size();
	}
	
	public PatientSet getResultForSearch(int index) {
		CohortSearchHistory history = getMySearchHistory();
		PatientSet ps = history.getPatientSet(index);
		return ps;
	}
	
	public PatientSet getResultCombineWithAnd() {
		CohortSearchHistory history = getMySearchHistory();
		PatientSet ps = history.getPatientSetCombineWithAnd();
		return ps;
	}
	
	public PatientSet getResultCombineWithOr() {
		CohortSearchHistory history = getMySearchHistory();
		PatientSet ps = history.getPatientSetCombineWithOr();
		return ps;
	}
	
	public PatientSet getLastResult() {
		CohortSearchHistory history = getMySearchHistory();
		PatientSet ps = history.getLastPatientSet();
		return ps;
	}
	
	public List<ListItem> getSavedSearches() {
		List<ListItem> ret = new ArrayList<ListItem>();
		List<AbstractReportObject> savedSearches = Context.getReportService().getReportObjectsByType(OpenmrsConstants.REPORT_OBJECT_TYPE_PATIENTSEARCH);
		for (ReportObject ps : savedSearches) {
			ListItem li = new ListItem();
			li.setId(ps.getReportObjectId());
			li.setName(ps.getName());
			li.setDescription(ps.getDescription());
			ret.add(li);
		}
		return ret;
	}
	
	public List<ListItem> getSavedFilters() {
		List<ListItem> ret = new ArrayList<ListItem>();
		List<PatientFilter> savedFilters = Context.getReportService().getAllPatientFilters();
		for (PatientFilter pf : savedFilters) {
			ListItem li = new ListItem();
			li.setId(pf.getReportObjectId());
			li.setName(pf.getName());
			li.setDescription(pf.getDescription());
			ret.add(li);
		}
		return ret;
	}
	
	public List<ListItem> getSavedCohorts() {
		List<ListItem> ret = new ArrayList<ListItem>();
		List<Cohort> cohorts = Context.getCohortService().getCohorts();
		for (Cohort c : cohorts) {
			ListItem li = new ListItem();
			li.setId(c.getCohortId());
			li.setName(c.getName());
			li.setDescription(c.getDescription());
			ret.add(li);
		}
		return ret;
	}

	public String getFilterResultAsCommaSeparatedIds(Integer filterId) {
		PatientFilter pf = Context.getReportService().getPatientFilterById(filterId);
		if (pf == null)
			return "";
		else
			return pf.filter(Context.getPatientSetService().getAllPatients()).toCommaSeparatedPatientIds();
	}
	
	public String getCohortAsCommaSeparatedIds(Integer cohortId) {
		Cohort c = Context.getCohortService().getCohort(cohortId);
		if (c == null)
			return "";
		else
			return c.toPatientSet().toCommaSeparatedPatientIds();
	}
	
	public List<ListItem> getSearchHistories() {
		List<ListItem> ret = new ArrayList<ListItem>();
		List<CohortSearchHistory> histories = Context.getReportService().getSearchHistories();
		for (CohortSearchHistory h : histories) {
			ListItem li = new ListItem();
			li.setId(h.getReportObjectId());
			li.setName(h.getName());
			li.setDescription(h.getDescription());
			ret.add(li);
		}
		return ret;
	}
	
	public void saveSearchHistory(String name, String description) {
		CohortSearchHistory history = getMySearchHistory();
		if (history.getReportObjectId() != null)
			throw new RuntimeException("Re-saving search history Not Yet Implemented");
		history.setName(name);
		history.setDescription(description);
		Context.getReportService().createSearchHistory(history);
	}
		
	public void loadSearchHistory(Integer id) {
		Context.setVolatileUserData("CohortBuilderSearchHistory", Context.getReportService().getSearchHistory(id));
	}
	
	/**
	 * Saves an element from the search history as a PatientSearch 
	 * @param name The name to give the saved filter
	 * @param description The description to give the saved filter
	 * @param indexInHistory The index into the authenticated user's search history
	 */
	public Boolean saveHistoryElement(String name, String description, Integer indexInHistory) {
		CohortSearchHistory history = getMySearchHistory();
		try {
			PatientSearch ps = history.getSearchHistory().get(indexInHistory);
			if (ps == null)
				return false;
			// some searches depend on history, so we must detach them
			ps = ps.copyAndDetachFromHistory(history);
			PatientSearchReportObject ro = new PatientSearchReportObject();
			ro.setName(name);
			ro.setDescription(description);
			ro.setPatientSearch(ps);
			Integer newId = Context.getReportService().createReportObject(ro);
			history.getSearchHistory().set(indexInHistory, PatientSearch.createSavedSearchReference(newId));
			return true;
		} catch (Exception ex) {
			log.error("Exception", ex);
			return false;
		}
	}
	
	public void saveCohort(String name, String description, String commaSeparatedIds) {
		Set<Integer> ids = new HashSet<Integer>(OpenmrsUtil.delimitedStringToIntegerList(commaSeparatedIds, ","));
		Cohort cohort = new Cohort();
		cohort.setName(name);
		cohort.setDescription(description);
		cohort.setMemberIds(ids);
		Context.getCohortService().createCohort(cohort);
	}
	
	/**
	 * This isn't really useful because most of the properties don't have DWR converters.
	 * I'm leaving it here in case I get to work on it later.
	 */
	public CohortSearchHistory getUserSearchHistory() {
		return getMySearchHistory();
	}
	
}
