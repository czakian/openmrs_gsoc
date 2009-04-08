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
package org.openmrs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.ReportService;
import org.openmrs.cohort.CohortDefinition;
import org.openmrs.report.EvaluationContext;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * This class represents a list of patientIds. If it is generated from a CohortDefinition via
 * {@link ReportService#evaluate(org.openmrs.report.ReportSchema, Cohort, EvaluationContext)} then
 * it will contain a link back to the CohortDefinition it came from and the EvalutionContext that
 * definition was evaluated in.
 * 
 * @see org.openmrs.cohort.CohortDefinition
 */
@Root(strict = false)
public class Cohort extends BaseOpenmrsData implements Serializable {
	
	public static final long serialVersionUID = 0L;
	
	public Log log = LogFactory.getLog(this.getClass());
	
	private Integer cohortId;
	
	private String name;
	
	private String description;
	
	private Set<Integer> memberIds;
	
	private CohortDefinition cohortDefinition;
	
	private EvaluationContext evaluationContext;
	
	public Cohort() {
		memberIds = new TreeSet<Integer>();
	}
	
	/**
	 * Convenience constructor to create a Cohort object that has an primarykey/internal identifier
	 * of <code>cohortId</code>
	 * 
	 * @param cohortId the internal identifier for this cohort
	 */
	public Cohort(Integer cohortId) {
		this();
		this.cohortId = cohortId;
	}
	
	/**
	 * This constructor does not check whether the database contains patients with the given ids,
	 * but
	 * 
	 * @see CohortService.saveCohort(Cohort) will.
	 * @param name
	 * @param description optional description
	 * @param ids option array of Integer ids
	 */
	public Cohort(String name, String description, Integer[] ids) {
		this();
		this.name = name;
		this.description = description;
		if (ids != null)
			memberIds.addAll(Arrays.asList(ids));
	}
	
	/**
	 * This constructor does not check whether the database contains patients with the given ids,
	 * but
	 * 
	 * @see CohortService.saveCohort(Cohort) will.
	 * @param name
	 * @param description optional description
	 * @param patients optional array of patients
	 */
	public Cohort(String name, String description, Patient[] patients) {
		this(name, description, (Integer[]) null);
		if (patients != null)
			for (Patient p : patients)
				memberIds.add(p.getPatientId());
	}
	
	/**
	 * This constructor does not check whether the database contains patients with the given ids,
	 * but
	 * 
	 * @see CohortService.saveCohort(Cohort) will.
	 * @param patientsOrIds optional collection which may contain Patients, or patientIds which may
	 *            be Integers, Strings, or anything whose toString() can be parsed to an Integer.
	 */
	@SuppressWarnings("unchecked")
	public Cohort(Collection patientsOrIds) {
		this(null, null, patientsOrIds);
	}
	
	/**
	 * This constructor does not check whether the database contains patients with the given ids,
	 * but
	 * 
	 * @see CohortService.saveCohort(Cohort) will.
	 * @param name
	 * @param description optional description
	 * @param patientsOrIds optional collection which may contain Patients, or patientIds which may
	 *            be Integers, Strings, or anything whose toString() can be parsed to an Integer.
	 */
	@SuppressWarnings("unchecked")
	public Cohort(String name, String description, Collection patientsOrIds) {
		this(name, description, (Integer[]) null);
		if (patientsOrIds != null) {
			for (Object o : patientsOrIds) {
				if (o instanceof Patient)
					memberIds.add(((Patient) o).getPatientId());
				else if (o instanceof Integer)
					memberIds.add((Integer) o);
				else
					memberIds.add(Integer.valueOf(o.toString()));
			}
		}
	}
	
	/**
	 * Convenience contructor taking in a string that is a list of comma separated patient ids This
	 * constructor does not check whether the database contains patients with the given ids, but
	 * 
	 * @see CohortService.saveCohort(Cohort) will.
	 * @param commaSeparatedIds
	 */
	public Cohort(String commaSeparatedIds) {
		this();
		for (StringTokenizer st = new StringTokenizer(commaSeparatedIds, ","); st.hasMoreTokens();) {
			String id = st.nextToken();
			memberIds.add(new Integer(id.trim()));
		}
	}
	
	/**
	 * @return Returns a comma-separated list of patient ids in the cohort.
	 */
	public String getCommaSeparatedPatientIds() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<Integer> i = getMemberIds().iterator(); i.hasNext();) {
			sb.append(i.next());
			if (i.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	public boolean contains(Patient patient) {
		return getMemberIds() != null && getMemberIds().contains(patient.getPatientId());
	}
	
	public boolean contains(Integer patientId) {
		return getMemberIds() != null && getMemberIds().contains(patientId);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("Cohort id=" + getCohortId());
		if (getName() != null)
			sb.append(" name=" + getName());
		if (getMemberIds() != null)
			sb.append(" size=" + getMemberIds().size());
		return sb.toString();
	}
	
	public boolean equals(Object obj) {
		if (this.getCohortId() == null)
			return false;
		if (obj instanceof Cohort) {
			Cohort c = (Cohort) obj;
			return (this.getCohortId().equals(c.getCohortId()));
		}
		return false;
	}
	
	public int hashCode() {
		if (this.getCohortId() == null)
			return super.hashCode();
		int hash = 8;
		hash = 31 * this.getCohortId() + hash;
		return hash;
	}
	
	public void addMember(Integer memberId) {
		getMemberIds().add(memberId);
	}
	
	public void removeMember(Integer memberId) {
		getMemberIds().remove(memberId);
	}
	
	public int size() {
		return getMemberIds() == null ? 0 : getMemberIds().size();
	}
	
	public int getSize() {
		return size();
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	// static utility methods
	
	/**
	 * Returns the union of two cohorts
	 * 
	 * @param a The first Cohort
	 * @param b The second Cohort
	 * @return Cohort
	 */
	public static Cohort union(Cohort a, Cohort b) {
		Cohort ret = new Cohort();
		ret.setName("(" + a.getName() + " + " + b.getName() + ")");
		if (a != null)
			ret.getMemberIds().addAll(a.getMemberIds());
		if (b != null)
			ret.getMemberIds().addAll(b.getMemberIds());
		return ret;
	}
	
	/**
	 * Returns the intersection of two cohorts
	 * 
	 * @param a The first Cohort
	 * @param b The second Cohort
	 * @return Cohort
	 */
	public static Cohort intersect(Cohort a, Cohort b) {
		Cohort ret = new Cohort();
		ret.setName("(" + a.getName() + " * " + b.getName() + ")");
		if (a != null && b != null) {
			ret.getMemberIds().addAll(a.getMemberIds());
			ret.getMemberIds().retainAll(b.getMemberIds());
		}
		return ret;
	}
	
	/**
	 * Subtracts a cohort from a cohort
	 * 
	 * @param a the original Cohort
	 * @param b the Cohort to subtract
	 * @return Cohort
	 */
	public static Cohort subtract(Cohort a, Cohort b) {
		Cohort ret = new Cohort();
		ret.setName("(" + a.getName() + " - " + b.getName() + ")");
		if (a != null) {
			ret.getMemberIds().addAll(a.getMemberIds());
			if (b != null)
				ret.getMemberIds().removeAll(b.getMemberIds());
		}
		return ret;
	}
	
	// getters and setters
	
	@Attribute(required = false)
	public Integer getCohortId() {
		return cohortId;
	}
	
	@Attribute(required = false)
	public void setCohortId(Integer cohortId) {
		this.cohortId = cohortId;
	}
	
	@Element(required = false)
	public String getDescription() {
		return description;
	}
	
	@Element(required = false)
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Element(required = false)
	public String getName() {
		return name;
	}
	
	@Element(required = false)
	public void setName(String name) {
		this.name = name;
	}
	
	@ElementList(required = true)
	public Set<Integer> getMemberIds() {
		return memberIds;
	}
	
	/**
	 * This method is only here for some backwards compatibility with the PatientSet object that
	 * this Cohort object replaced. Do not use this method.
	 * 
	 * @deprecated use #getMemberIds()
	 * @return the memberIds
	 */
	public Set<Integer> getPatientIds() {
		return getMemberIds();
	}
	
	@ElementList(required = true)
	public void setMemberIds(Set<Integer> memberIds) {
		this.memberIds = memberIds;
	}
	
	/**
	 * @return the cohortDefinition
	 */
	@Element(required = false)
	public CohortDefinition getCohortDefinition() {
		return cohortDefinition;
	}
	
	/**
	 * @param cohortDefinition the cohortDefinition to set
	 */
	@Element(required = false)
	public void setCohortDefinition(CohortDefinition cohortDefinition) {
		this.cohortDefinition = cohortDefinition;
	}
	
	/**
	 * @return the evaluationContext
	 */
	@Element(required = false)
	public EvaluationContext getEvaluationContext() {
		return evaluationContext;
	}
	
	/**
	 * @param evaluationContext the evaluationContext to set
	 */
	@Element(required = false)
	public void setEvaluationContext(EvaluationContext evaluationContext) {
		this.evaluationContext = evaluationContext;
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	public Integer getId() {
		
		return getCohortId();
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		setCohortId(id);
		
	}
}
