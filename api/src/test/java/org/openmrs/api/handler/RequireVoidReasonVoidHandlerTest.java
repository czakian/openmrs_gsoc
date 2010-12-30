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
package org.openmrs.api.handler;

import java.util.Date;

import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.Voidable;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.Verifies;

/**
 * Tests for the {@link RequireVoidReasonVoidHandler} class.
 */
public class RequireVoidReasonVoidHandlerTest extends BaseContextSensitiveTest {
	
	/**
	 * @see {@link RequireVoidReasonVoidHandler#handle(Voidable,User,Date,String)}
	 */
	@Test(expected = IllegalArgumentException.class)
	@Verifies(value = "should throw IllegalArgumentException if Patient voidReason is null", method = "handle(Voidable,User,Date,String)")
	public void handle_shouldThrowIllegalArgumentExceptionIfPatientVoidReasonIsNull() throws Exception {
		Patient p = Context.getPatientService().getPatient(2);
		Context.getPatientService().voidPatient(p, null);
	}
	
	/**
	 * @see {@link RequireVoidReasonVoidHandler#handle(Voidable,User,Date,String)}
	 */
	@Test(expected = IllegalArgumentException.class)
	@Verifies(value = "should throw IllegalArgumentException if Encounter voidReason is empty", method = "handle(Voidable,User,Date,String)")
	public void handle_shouldThrowIllegalArgumentExceptionIfEncounterVoidReasonIsEmpty() throws Exception {
		Encounter e = Context.getEncounterService().getEncounter(3);
		Context.getEncounterService().voidEncounter(e, "");
	}
	
	/**
	 * @see {@link RequireVoidReasonVoidHandler#handle(Voidable,User,Date,String)}
	 */
	@Test(expected = IllegalArgumentException.class)
	@Verifies(value = "should throw IllegalArgumentException if Obs voidReason is blank", method = "handle(Voidable,User,Date,String)")
	public void handle_shouldThrowIllegalArgumentExceptionIfObsVoidReasonIsBlank() throws Exception {
		Obs o = Context.getObsService().getObs(7);
		Context.getObsService().voidObs(o, "  ");
	}
	
	/**
	 * @see {@link RequireVoidReasonVoidHandler#handle(Voidable,User,Date,String)}
	 */
	@Test
	@Verifies(value = "should not throw Exception if voidReason is not blank", method = "handle(Voidable,User,Date,String)")
	public void handle_shouldNotThrowExceptionIfVoidReasonIsNotBlank() throws Exception {
		Obs o = Context.getObsService().getObs(7);
		Context.getObsService().voidObs(o, "Some Reason");
	}
	
	/**
	 * @see {@link RequireVoidReasonVoidHandler#handle(Voidable,User,Date,String)}
	 */
	@Test
	@Verifies(value = "not throw Exception if voidReason is null for unsupported types", method = "handle(Voidable,User,Date,String)")
	public void handle_shouldNotThrowExceptionIfVoidReasonIsNullForUnsupportedTypes() throws Exception {
		Person p = Context.getPersonService().getPerson(1);
		Context.getPersonService().voidPerson(p, null);
	}
}
