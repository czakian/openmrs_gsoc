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
package org.openmrs.web.controller.program;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptStateConversion;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.ConceptEditor;
import org.openmrs.propertyeditor.ProgramWorkflowEditor;
import org.openmrs.propertyeditor.ProgramWorkflowStateEditor;
import org.openmrs.web.WebConstants;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class StateConversionFormController extends SimpleFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		
		binder.registerCustomEditor(Concept.class, new ConceptEditor());
		binder.registerCustomEditor(ProgramWorkflow.class, new ProgramWorkflowEditor());
		binder.registerCustomEditor(ProgramWorkflowState.class, new ProgramWorkflowStateEditor());
	}
	
	/**
	 * This is called prior to displaying a form for the first time. It tells Spring the
	 * form/command object to load into the request
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		log.debug("called formBackingObject");
		
		ConceptStateConversion conversion = null;
		
		if (Context.isAuthenticated()) {
			ProgramWorkflowService ps = Context.getProgramWorkflowService();
			String conversionId = ServletRequestUtils.getStringParameter(request, "conceptStateConversionId");
			if (conversionId != null) {
				log.debug("conversion ID is " + conversionId);
				try {
					conversion = ps.getConceptStateConversion(Integer.valueOf(conversionId));
					log.debug("Csc is now " + conversion);
				}
				catch (NumberFormatException nfe) {
					log.error("conversionId passed is not a valid number");
				}
			} else {
				log.debug("conversionID is null");
			}
		}
		
		if (conversion == null) {
			log.debug("Conversion is null");
			conversion = new ConceptStateConversion();
		} else {
			log.debug("Conversion is not null");
		}
		
		return conversion;
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
		log.debug("about to save " + obj);
		
		HttpSession httpSession = request.getSession();
		
		String view = getFormView();
		
		if (Context.isAuthenticated()) {
			ConceptStateConversion c = (ConceptStateConversion) obj;
			Context.getProgramWorkflowService().saveConceptStateConversion(c);
			view = getSuccessView();
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Program.conversion.saved");
		}
		
		return new ModelAndView(new RedirectView(view));
	}
	
}
