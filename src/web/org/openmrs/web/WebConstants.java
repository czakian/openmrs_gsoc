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
package org.openmrs.web;

public class WebConstants {
	
	public static final String INIT_REQ_UNIQUE_ID = "__INIT_REQ_UNIQUE_ID__";
	
	public static final String OPENMRS_CONTEXT_HTTPSESSION_ATTR = "__openmrs_context";
	
	public static final String OPENMRS_USER_CONTEXT_HTTPSESSION_ATTR = "__openmrs_user_context";
	
	public static final String OPENMRS_CLIENT_IP_HTTPSESSION_ATTR = "__openmrs_client_ip";
	
	public static final String OPENMRS_LOGIN_REDIRECT_HTTPSESSION_ATTR = "__openmrs_login_redirect";
	
	public static final String OPENMRS_MSG_ATTR = "openmrs_msg";
	
	public static final String OPENMRS_MSG_ARGS = "openmrs_msg_arguments";
	
	public static final String OPENMRS_ERROR_ATTR = "openmrs_error";
	
	public static final String OPENMRS_ERROR_ARGS = "openmrs_error_arguments";
	
	public static final String OPENMRS_LANGUAGE_COOKIE_NAME = "__openmrs_language";
	
	public static final String OPENMRS_USER_OVERRIDE_PARAM = "__openmrs_user_over_id";
	
	public static final String OPENMRS_ANALYSIS_IN_PROGRESS_ATTR = "__openmrs_analysis_in_progress";
	
	public static final String OPENMRS_DYNAMIC_FORM_IN_PROGRESS_ATTR = "__openmrs_dynamic_form_in_progress";
	
	public static final String OPENMRS_PATIENT_SET_ATTR = "__openmrs_patient_set";
	
	public static final Integer OPENMRS_PATIENTSET_PAGE_SIZE = 25;
	
	public static final String OPENMRS_DYNAMIC_FORM_KEEPALIVE = "__openmrs_dynamic_form_keepalive";
	
	public static final String OPENMRS_HEADER_USE_MINIMAL = "__openmrs_use_minimal_header";
	
	public static final String OPENMRS_PORTLET_MODEL_NAME = "model";
	
	public static final String OPENMRS_PORTLET_LAST_REQ_ID = "__openmrs_portlet_last_req_id";
	
	public static final String OPENMRS_PORTLET_CACHED_MODEL = "__openmrs_portlet_cached_model";
	
	// these vars filled in by org.openmrs.web.Listener at webapp start time
	public static String BUILD_TIMESTAMP = "";
	
	public static String WEBAPP_NAME = "openmrs";
	
	// ComplexObsHandler views specific to the web layer:
	public static final String HTML_VIEW = "html_view";
	
	public static final String HYPERLINK_VIEW = "hyperlink_view";
	
	/**
	 * Page in the webapp used for initial setup of the database connection if no valid one exists
	 */
	public static final String SETUP_PAGE_URL = "initialsetup";
	
	/**
	 * The url of the module repository. This is filled in at startup by the value in web.xml
	 */
	public static String MODULE_REPOSITORY_URL = "";
	
	/**
	 * Global property name for the number of times one IP can fail at logging in before being
	 * locked out. A value of 0 for this property means no IP lockout checks.
	 * 
	 * @see org.openmrs.web.servlet.LoginServlet
	 */
	public static String GP_ALLOWED_LOGIN_ATTEMPTS_PER_IP = "security.loginAttemptsAllowedPerIP";
	
	/**
	 * User names of the logged-in users are stored in this map (session id -> user name) in the
	 * ServletContext under this key
	 */
	public static final String CURRENT_USERS = "CURRENT_USERS";
}
