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
package org.openmrs.web.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.junit.Test;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.test.Verifies;
import org.openmrs.web.test.BaseWebContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;

public class GlobalPropertyPortletControllerTest extends BaseWebContextSensitiveTest {
	
	/**
	 * @see {@link GlobalPropertyPortletController#populateModel(HttpServletRequest,Map)}
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Verifies(value = "should exclude multiple prefixes", method = "populateModel(HttpServletRequest,Map<String,Object>)")
	public void populateModel_shouldExcludeMultiplePrefixes() throws Exception {
		//given
		GlobalPropertyPortletController portletController = new GlobalPropertyPortletController();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "");
		Map<String, Object> model = new HashMap<String, Object>();
		
		//when
		String excludePrefix = "file.started;file.mandatory";
		model.put("excludePrefix", excludePrefix);
		GlobalProperty[] globalProperties = { new GlobalProperty("file.started", ""),
		        new GlobalProperty("file.mandatory", ""), new GlobalProperty("file.other", "") };
		Context.getAdministrationService().saveGlobalProperties(Arrays.asList(globalProperties));
		
		//then
		portletController.populateModel(request, model);
		List<GlobalProperty> properties = (List<GlobalProperty>) model.get("properties");
		Assert.assertFalse(properties.contains(globalProperties[0]));
		Assert.assertFalse(properties.contains(globalProperties[1]));
		Assert.assertTrue(properties.contains(globalProperties[2]));
	}
	
	/**
	 * @see {@link GlobalPropertyPortletController#setupModelForModule(Map)}
	 */
	@Test
	@Verifies(value = "should change model if forModule is present", method = "setupModelForModule(Map<String,Object>)")
	public void setupModelForModule_shouldChangeModelIfForModuleIsPresent() throws Exception {
		//given
		GlobalPropertyPortletController portletController = new GlobalPropertyPortletController();
		Map<String, Object> model = new HashMap<String, Object>();
		
		//when
		String forModule = "file";
		model.put("forModule", forModule);
		
		//then
		portletController.setupModelForModule(model);
		Assert.assertEquals(forModule + ".", model.get("propertyPrefix"));
		Assert.assertEquals("true", model.get("hidePrefix"));
		Assert.assertEquals(forModule + ".started;" + forModule + ".mandatory", model.get("excludePrefix"));
	}
	
	/**
	 * @see {@link GlobalPropertyPortletController#setupModelForModule(Map)}
	 */
	@Test
	@Verifies(value = "should not change mode if forModule is not present", method = "setupModelForModule(Map<String,Object>)")
	public void setupModelForModule_shouldNotChangeModeIfForModuleIsNotPresent() throws Exception {
		//given
		GlobalPropertyPortletController portletController = new GlobalPropertyPortletController();
		Map<String, Object> model = new HashMap<String, Object>();
		
		//when
		
		//then
		portletController.setupModelForModule(model);
		Assert.assertNull(model.get("propertyPrefix"));
		Assert.assertNull(model.get("hidePrefix"));
		Assert.assertNull(model.get("excludePrefix"));
	}
	
	/**
	 * @see {@link GlobalPropertyPortletController#setupModelForModule(Map)}
	 */
	@Test
	@Verifies(value = "should not override excludePrefix but concatenate", method = "setupModelForModule(Map<String,Object>)")
	public void setupModelForModule_shouldNotOverrideExcludePrefixButConcatenate() throws Exception {
		//given
		GlobalPropertyPortletController portletController = new GlobalPropertyPortletController();
		Map<String, Object> model = new HashMap<String, Object>();
		
		//when
		String forModule = "file";
		String excludePrefix = "file.custom";
		model.put("forModule", forModule);
		model.put("excludePrefix", excludePrefix);
		
		//then
		portletController.setupModelForModule(model);
		Assert.assertEquals(excludePrefix + ";" + forModule + ".started;" + forModule + ".mandatory", model
		        .get("excludePrefix"));
	}
}
