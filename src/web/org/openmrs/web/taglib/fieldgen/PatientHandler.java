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
package org.openmrs.web.taglib.fieldgen;

import org.openmrs.Patient;

public class PatientHandler extends AbstractFieldGenHandler implements FieldGenHandler {
	
	private String defaultUrl = "patient.field";
	
	public void run() {
		setUrl(defaultUrl);
		// the following lines are adapted from UserHandler:
		checkEmptyVal((Patient) null);
		if (fieldGenTag != null) {
			Object initialValue = this.fieldGenTag.getVal();
			setParameter("initialValue", initialValue == null ? "" : initialValue);
		}
	}
}
