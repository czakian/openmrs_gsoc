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
package org.openmrs.scheduler.tasks;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.report.EvaluationContext;
import org.openmrs.reporting.ReportObjectService;
import org.openmrs.reporting.export.DataExportReportObject;
import org.openmrs.reporting.export.DataExportUtil;
import org.openmrs.scheduler.TaskDefinition;

/**
 * Generates a data export
 * 
 * @deprecated see reportingcompatibility module
 */
@Deprecated
public class GenerateDataExportTask extends AbstractTask {
	
	// Logger 
	private Log log = LogFactory.getLog(GenerateDataExportTask.class);
	
	// Instance of configuration information for task
	private String idString = "";
	
	private EvaluationContext context;
	
	/**
	 * @see org.openmrs.scheduler.tasks.AbstractTask#initialize(TaskDefinition)
	 */
	public void initialize(TaskDefinition definition) {
		super.initialize(definition);
		this.idString = definition.getProperty("dataExportIds");
	}
	
	public void setEvaluationContext(EvaluationContext context) {
		this.context = context;
	}
	
	public EvaluationContext getEvaluationContext() {
		return this.context;
	}
	
	/**
	 * Process the next form entry in the database and then remove the form entry from the database.
	 */
	public void execute() {
		Context.openSession();
		try {
			log.debug("Generating data exports...");
			
			if (!Context.isAuthenticated())
				authenticate();
			
			if (idString != null && idString.length() > 0) {
				idString = idString.replace(",", " ");
				
				String[] ids = idString.split(" ");
				
				List<DataExportReportObject> reports = new Vector<DataExportReportObject>();
				ReportObjectService rs = Context.getReportObjectService();
				
				for (String id : ids) {
					if (id != null) {
						id = id.trim();
						if (id.length() > 0) {
							DataExportReportObject report = (DataExportReportObject) rs.getReportObject(Integer.valueOf(id));
							reports.add(report);
						}
					}
				}
				
				DataExportUtil.generateExports(reports, this.getEvaluationContext());
			}
			
		}
		catch (Exception e) {
			log.error("Error running generate data export queue task", e);
			throw new APIException("Error running generate data export queue task", e);
		}
		finally {
			Context.closeSession();
		}
		
	}
	
}
