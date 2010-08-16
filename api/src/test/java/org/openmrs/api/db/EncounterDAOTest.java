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
package org.openmrs.api.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.HibernateEncounterDAO;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.Verifies;

/**
 * This class tests the {@link EncounterDAO} linked to from the Context. Currently that file is the
 * {@link HibernateEncounterDAO} This should only have to test methods that don't really have
 * equivalents at the {@link EncounterService} layer.
 */
public class EncounterDAOTest extends BaseContextSensitiveTest {
	
	private EncounterDAO dao = null;
	
	/**
	 * Run this before each unit test in this class. The "@Before" method in
	 * {@link BaseContextSensitiveTest} is run right before this method.
	 * 
	 * @throws Exception
	 */
	@Before
	public void runBeforeEachTest() throws Exception {
		executeDataSet("org/openmrs/api/db/include/EncounterDAOTest-initialData.xml");
		
		if (dao == null)
			// fetch the dao from the spring application context
			// this bean name matches the name in /metadata/spring/applicationContext-service.xml
			dao = (EncounterDAO) applicationContext.getBean("encounterDAO");
	}
	
	/**
	 * @see {@link EncounterDAO#getSavedEncounterDatetime(Encounter)}
	 */
	@Test
	@Verifies(value = "should get saved encounter datetime from database", method = "getSavedEncounterDatetime(Encounter)")
	public void getSavedEncounterDatetime_shouldGetSavedEncounterDatetimeFromDatabase() throws Exception {
		
		Encounter encounter = Context.getEncounterService().getEncounter(1);
		
		Date newDate = new Date();
		
		// sanity check: make sure the date on the encounter isn't "right now" 
		assertNotSame(encounter.getEncounterDatetime(), newDate);
		
		// save the date from the db for later checks
		Date origDate = encounter.getEncounterDatetime();
		
		// change the date on the java pojo (NOT in the database)
		encounter.setEncounterDatetime(newDate);
		
		// the value from the database should match the original date from the 
		// encounter right after /it/ was fetched from the database
		Date encounterDateFromDatabase = dao.getSavedEncounterDatetime(encounter);
		assertEquals(origDate, encounterDateFromDatabase);
	}
	
}
