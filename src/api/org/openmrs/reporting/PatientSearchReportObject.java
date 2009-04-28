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
package org.openmrs.reporting;

import org.openmrs.util.OpenmrsConstants;

/**
 * @deprecated see reportingcompatibility module
 */
@Deprecated
public class PatientSearchReportObject extends AbstractReportObject {
	
	private PatientSearch patientSearch;
	
	public PatientSearchReportObject() {
		super.setType(OpenmrsConstants.REPORT_OBJECT_TYPE_PATIENTSEARCH);
		super.setSubType(OpenmrsConstants.REPORT_OBJECT_TYPE_PATIENTSEARCH);
	}
	
	public PatientSearchReportObject(String name, PatientSearch search) {
		this();
		setName(name);
		setPatientSearch(search);
	}
	
	public PatientSearch getPatientSearch() {
		return patientSearch;
	}
	
	public void setPatientSearch(PatientSearch patientSearch) {
		this.patientSearch = patientSearch;
	}
	
}
