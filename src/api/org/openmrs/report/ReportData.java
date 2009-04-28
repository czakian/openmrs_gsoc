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
package org.openmrs.report;

import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

/**
 * Report Data obtained from evaluating a ReportSchema with a given EvaluationContext.
 * 
 * @deprecated see reportingcompatibility module
 */
@Root
@Deprecated
public class ReportData {
	
	private ReportSchema reportSchema;
	
	private EvaluationContext evaluationContext;
	
	@SuppressWarnings("unchecked")
	private Map<String, DataSet> dataSets;
	
	public ReportData() {
	}
	
	@SuppressWarnings("unchecked")
	@ElementMap(required = false)
	public Map<String, DataSet> getDataSets() {
		return dataSets;
	}
	
	@SuppressWarnings("unchecked")
	@ElementMap(required = false)
	public void setDataSets(Map<String, DataSet> dataSets) {
		this.dataSets = dataSets;
	}
	
	/**
	 * @return Returns the EvaluationContext that was used to obtain this ReportData.
	 */
	//@Element(required=false)
	public EvaluationContext getEvaluationContext() {
		return evaluationContext;
	}
	
	/**
	 * Saves the EvaluationContext that was used to obtain this ReportData.
	 * 
	 * @param evaluationContext
	 */
	//@Element(required=false)
	public void setEvaluationContext(EvaluationContext evaluationContext) {
		this.evaluationContext = evaluationContext;
	}
	
	@Element
	public ReportSchema getReportSchema() {
		return reportSchema;
	}
	
	@Element
	public void setReportSchema(ReportSchema reportSchema) {
		this.reportSchema = reportSchema;
	}
	
}
