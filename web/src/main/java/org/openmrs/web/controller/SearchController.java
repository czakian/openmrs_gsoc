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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
	
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(SearchController.class);
	
	/**
	 * Render the search results page
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/search.htm")
	public String search(ModelMap modelMap, @RequestParam("q") String q) {
		modelMap.addAttribute("q", q);
		Context.getSearchService().indexExistingData();
		return "searchResults";
	}
	
	/**
	 * Render the actual search results as JSON
	 * 
	 * @param q
	 * @return
	 */
	@RequestMapping(value = "/getSearchResults.json", method = RequestMethod.GET)
	public List<Map<String, Object>> searchResults(@RequestParam("q") String q) {
		// perform search
		List<Object> resultObjs = new ArrayList<Object>();
		// resultObjs = Context.getSearchService().search(q);
		
		String[] pFields = { "names.prefix", "names.givenName", "names.familyName", "names.Id" };
		String[] dFields = { "name", "description" };
		
		// get results for each indexed class ...
		
		List<Object> objs = Context.getSearchService().search(q, Person.class, pFields);
		if (objs != null)
			resultObjs.addAll(objs);
		
		objs = Context.getSearchService().search(q, Drug.class, pFields);
		if (objs != null)
			resultObjs.addAll(objs);
		
		// parse resultObjs to simple maps
		return convertResults(resultObjs);
	}
	
	/**
	 * convert results from objects to a list of maps (for use in JSP)
	 * 
	 * @param objs
	 * @return 
	 */
	private List<Map<String, Object>> convertResults(List<Object> objs) {
		List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
		
		// give up quickly if nothing to do
		if (objs == null)
			return out;
		
		for (Object o : objs) {
			Map<String, Object> rep = new HashMap<String, Object>();
			if (o instanceof Person) {
				Person p = (Person) o;
				rep.put("class", p.isPatient() ? "Patient" : "Person");
				rep.put("id", p.isPatient() ? ((Patient) p).getPatientId() : p.getPersonId());
				rep.put("title", p.getPersonName() == null ? "unknown" : p.getPersonName().toString());
				String description = "";
				if (p.getAge() != null)
					description = description + p.getAge().toString() + " year old ";
				description = description + (p.getDead() ? "dead " : "") + p.getGender();
				if (p.getPersonAddress() != null && p.getPersonAddress().getCityVillage() != null)
					description = description + " from " + p.getPersonAddress().getCityVillage();
				rep.put("description", description.trim().length() > 0 ? description.trim() : "no information available");
			} else if (o instanceof Drug) {
				Drug d = (Drug) o;
				rep.put("class", "Drug");
				rep.put("id", d.getDrugId());
				rep.put("title", d.getName());
				rep.put("description", d.getDescription());
			}
			out.add(rep);
		}
		return out;
	}
}
