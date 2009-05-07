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
package org.openmrs.web.encounter;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Obs;
import org.openmrs.test.SkipBaseSetup;
import org.openmrs.web.controller.encounter.EncounterDisplayController;
import org.openmrs.web.controller.encounter.EncounterDisplayController.FieldHolder;
import org.openmrs.web.test.BaseWebContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Test the methods on the org.openmrs.web.controller.encounter.EncounterDisplayController
 */
public class EncounterDisplayControllerTest extends BaseWebContextSensitiveTest {
	
	protected static final String DISPLAY_CONTROLLER_DATA = "org/openmrs/web/encounter/include/EncounterDisplayControllerTest.xml";
	
	/**
	 * Makes sure that the processing done in the encounter form controller is done properly for a
	 * normal encounter
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings( { "unchecked" })
	@Test
	@SkipBaseSetup
	public void shouldGetNormalEncounterPageData() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet(DISPLAY_CONTROLLER_DATA);
		authenticate();
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("encounterId", "3");
		
		HttpServletResponse response = new MockHttpServletResponse();
		
		EncounterDisplayController controller = new EncounterDisplayController();
		
		ModelAndView modelAndView = controller.handleRequest(request, response);
		
		Map<String, Object> model = (Map<String, Object>) modelAndView.getModel().get("model");
		
		// make sure there is a "pages" element on the page
		Map<Integer, List<FieldHolder>> pages = (Map<Integer, List<FieldHolder>>) model.get("pages");
		Assert.assertNotNull(pages);
		
	}
	
	/**
	 * If there are multiple obs in an obs group that share the same concept (question), then they
	 * should each be able to be displayed Verifies fix for bug #1025
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings( { "unchecked" })
	@Test
	public void shouldAllowMoreThanOneObsPerConceptInObsGroup() throws Exception {
		executeDataSet("org/openmrs/web/encounter/include/EncounterDisplayControllerTest-multiconceptsinobsGroup.xml");
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("encounterId", "5");
		EncounterDisplayController controller = new EncounterDisplayController();
		ModelAndView modelAndView = controller.handleRequest(request, new MockHttpServletResponse());
		
		Map<String, Object> model = (Map<String, Object>) modelAndView.getModel().get("model");
		
		// make sure there is a "pages" element on the page
		Map<Integer, List<FieldHolder>> pages = (Map<Integer, List<FieldHolder>>) model.get("pages");
		Assert.assertNotNull(pages);
		
		// this should be the only page
		List<FieldHolder> fieldHoldersOnPage = pages.get(999);
		
		// to account for when the junit test is run through via junit-report
		if (fieldHoldersOnPage == null)
			fieldHoldersOnPage = pages.get(0);
		
		for (FieldHolder fieldHolder : fieldHoldersOnPage) {
			// the first and only field holder should be an obs group
			Assert.assertTrue(fieldHolder.isObsGrouping());
			
			Map<Obs, List<List<Obs>>> matrix = fieldHolder.getObsGroupMatrix();
			Assert.assertEquals(1, matrix.keySet().size());
			
			List<List<Obs>> listOfCells = matrix.get(new Obs(16));
			
			// there should be only one column/cell
			List<Obs> firstAndOnlyCell = listOfCells.get(0);
			
			// within that cell, there should be two obs: #17 and #18
			Assert.assertEquals(2, firstAndOnlyCell.size());
			Assert.assertTrue(firstAndOnlyCell.contains(new Obs(17)));
			Assert.assertTrue(firstAndOnlyCell.contains(new Obs(18)));
		}
	}
	
}
