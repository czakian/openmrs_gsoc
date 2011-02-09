package org.openmrs.web.controller.patient;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.web.WebConstants;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class MergePatientsFormController extends SimpleFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object object,
	        BindException errors) throws Exception {
		//ModelAndView view = super.processFormSubmission(request, response, object, errors);
		
		log.debug("Number of errors: " + errors.getErrorCount());
		
		for (Object o : errors.getAllErrors()) {
			ObjectError e = (ObjectError) o;
			log.debug("Error name: " + e.getObjectName());
			log.debug("Error code: " + e.getCode());
			log.debug("Error message: " + e.getDefaultMessage());
			log.debug("Error args: " + e.getArguments());
			log.debug("Error codes: " + e.getCodes());
		}
		
		// call onSubmit manually so that we don't have to call
		// super.processFormSubmission()
		return onSubmit(request, response, object, errors);
	}
	
	/**
	 * The onSubmit function receives the form/command object that was modified by the input form
	 * and saves it to the db
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
	        BindException errors) throws Exception {
		
		HttpSession httpSession = request.getSession();
		
		if (Context.isAuthenticated()) {
			String view = getSuccessView();
			PatientService ps = Context.getPatientService();
			
			String pref = request.getParameter("preferred");
			String[] nonPreferred = request.getParameter("nonPreferred").split(",");
			String redirectURL = request.getParameter("redirectURL");
			
			Patient preferred = ps.getPatient(Integer.valueOf(pref));
			List<Patient> notPreferred = new ArrayList<Patient>();
			
			view = view + "?patientId=" + preferred.getPatientId();
			for (int i = 0; i < nonPreferred.length; i++) {
				notPreferred.add(ps.getPatient(Integer.valueOf(nonPreferred[i])));
				view = view + "&patientId=" + nonPreferred[i];
			}
			
			try {
				ps.mergePatients(preferred, notPreferred);
			}
			catch (APIException e) {
				log.error("Unable to merge patients", e);
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Patient.merge.fail");
				return showForm(request, response, errors);
			}
			int index = redirectURL.indexOf(request.getContextPath(), 2);
			if (index != -1) {
				redirectURL = redirectURL.substring(index);
				if (redirectURL.contains(getSuccessView()))
					redirectURL = "findDuplicatePatients.htm";
			} else
				redirectURL = view;
			
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Patient.merged");
			
			return new ModelAndView(new RedirectView(redirectURL));
		}
		
		return new ModelAndView(new RedirectView(getFormView()));
	}
	
	/**
	 * This is called prior to displaying a form for the first time. It tells Spring the
	 * form/command object to load into the request
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		
		Patient p1 = new Patient();
		
		if (Context.isAuthenticated()) {
			String[] patientIds = request.getParameterValues("patientId");
			if (patientIds != null && patientIds.length > 0) {
				String patientId = patientIds[0];
				Integer pId = Integer.valueOf(patientId);
				p1 = Context.getPatientService().getPatient(pId);
			}
		}
		
		return p1;
	}
	
	/**
	 * Called prior to form display. Allows for data to be put in the request to be used in the view
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request, Object obj, Errors errors) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Patient p1 = (Patient) obj;
		Patient p2 = new Patient();
		Collection<Encounter> patient1Encounters = new Vector<Encounter>();
		Collection<Encounter> patient2Encounters = new Vector<Encounter>();
		List<Patient> patientList = new ArrayList<Patient>();
		List<Collection<Encounter>> encounterList = new ArrayList<Collection<Encounter>>();
		if (Context.isAuthenticated()) {
			EncounterService es = Context.getEncounterService();
			patient1Encounters = es.getEncountersByPatient(p1);
			
			String[] patientIds = request.getParameterValues("patientId");
			if (patientIds != null)
				for (String patient : patientIds) {
					patientList.add(Context.getPatientService().getPatient(Integer.valueOf(patient)));
					encounterList.add(es.getEncountersByPatient(Context.getPatientService().getPatient(
					    Integer.valueOf(patient))));
				}
			if (patientIds != null && patientIds.length > 1 && !patientIds[0].equals(patientIds[1])) {
				String patientId = patientIds[1];
				Integer pId = Integer.valueOf(patientId);
				p2 = Context.getPatientService().getPatient(pId);
				patient2Encounters = es.getEncountersByPatient(p2);
			}
			
		}
		
		map.put("patient1Encounters", patient1Encounters);
		map.put("patient2Encounters", patient2Encounters);
		map.put("patientEncounters", encounterList);
		map.put("patient2", p2);
		map.put("patientList", patientList);
		
		return map;
	}
	
}
