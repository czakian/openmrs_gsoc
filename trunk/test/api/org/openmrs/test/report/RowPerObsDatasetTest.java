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
package org.openmrs.test.report;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openmrs.Cohort;
import org.openmrs.api.DataSetService;
import org.openmrs.api.context.Context;
import org.openmrs.report.EvaluationContext;
import org.openmrs.report.ReportData;
import org.openmrs.report.ReportSchema;
import org.openmrs.report.RowPerObsDataSetDefinition;
import org.openmrs.report.impl.TsvReportRenderer;
import org.openmrs.reporting.PatientCharacteristicFilter;
import org.openmrs.reporting.PatientSearch;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.TestUtil;
import org.openmrs.util.OpenmrsUtil;
import org.simpleframework.xml.Serializer;

/**
 *
 */
public class RowPerObsDatasetTest extends BaseContextSensitiveTest {
	
	/**
	 * TODO: fix this so it uses asserts instead of printing to the screen
	 * 
	 * @throws Exception
	 */
	public void testSerialization() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet("org/openmrs/test/report/include/RowPerObsDatasetTest.xml");
		authenticate();
		
		EvaluationContext evalContext = new EvaluationContext();
		DataSetService service = Context.getDataSetService();
		PatientSearch kids = PatientSearch.createFilterSearch(PatientCharacteristicFilter.class);
		
		Calendar today = new GregorianCalendar();
		
		// the first patient in the dataset has a birthday in 2007, so subtract that from
		// the current year for that patients age, then add one to make sure that patient 
		// is in the cohort (the second patient was born in 2000, so they shouldn't 
		// get counted)
		Integer maxAge = today.get(Calendar.YEAR) - 2007 + 1;
		kids.addArgument("maxAge", maxAge.toString(), Integer.class);
		Cohort kidsCohort = Context.getCohortService().evaluate(kids, evalContext);

		RowPerObsDataSetDefinition definition = new RowPerObsDataSetDefinition();
		definition.setName("Row per Obs");
		//commenting this out because serializing PatientSearches is not yet implemented
		//definition.setFilter(kids);
		definition.getQuestions().add(Context.getConceptService().getConcept(5089));
		
		ReportSchema rs = new ReportSchema();
		rs.setName("Testing row-per-obs");
		rs.setDescription("Tesing RowPerObsDataSet*");
		rs.addDataSetDefinition(definition);
		
		Serializer serializer = OpenmrsUtil.getSerializer();
		StringWriter writer = new StringWriter();
		serializer.write(rs, writer);
		
		String expectedOutput = "<reportSchema id=\"1\">\n   <dataSets class=\"java.util.Vector\" id=\"2\">\n      <dataSetDefinition class=\"org.openmrs.report.RowPerObsDataSetDefinition\" id=\"3\" name=\"Row per Obs\">\n         <questions class=\"java.util.HashSet\" id=\"4\">\n            <concept id=\"5\" conceptId=\"5089\"/>\n         </questions>\n      </dataSetDefinition>\n   </dataSets>\n   <description id=\"6\"><![CDATA[Tesing RowPerObsDataSet*]]></description>\n   <name id=\"7\"><![CDATA[Testing row-per-obs]]></name>\n</reportSchema>";
		assertEquals(expectedOutput, writer.toString());
		
		rs = (ReportSchema) serializer.read(ReportSchema.class, writer.toString());
		assertEquals("Testing row-per-obs", rs.getName());
		assertEquals(1, rs.getDataSetDefinitions().size());
		
		ReportData data = Context.getReportService().evaluate(rs, kidsCohort, evalContext);
		System.out.println("Result=");
		
		StringWriter w = new StringWriter();
		new TsvReportRenderer().render(data, null, w);
		
		expectedOutput = "\"patientId\"	\"question\"	\"questionConceptId\"	\"answer\"	\"answerConceptId\"	\"obsDatetime\"	\"encounterId\"	\"obsGroupId\"	\n\"2\"	\"WEIGHT\"	\"5089\"	\"100.0\"	\"\"	\"2005-01-01 00:00:00.0\"	\"1\"	\"\"	\n";
		// (This line was used to generate the above line of code)
		// TestUtil.printAssignableToSingleString(w.toString());
		
		assertEquals(expectedOutput, w.toString());
		
		
	}
	
}
