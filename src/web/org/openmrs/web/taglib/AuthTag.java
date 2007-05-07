package org.openmrs.web.taglib;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.web.WebConstants;

public class AuthTag extends TagSupport {

	public static final long serialVersionUID = 11233L;
	
	private final Log log = LogFactory.getLog(getClass());
	
	public int doStartTag() {

		HttpSession httpSession = pageContext.getSession();
		
		Context context = (Context)httpSession.getAttribute(WebConstants.OPENMRS_CONTEXT_HTTPSESSION_ATTR);

		if (context == null) {
			pageContext.setAttribute("authenticatedUser", null);
		}
		
		/*
		if (converse ^= context.isAuthenticated()) {
			pageContext.setAttribute("authenticatedUser", context.getAuthenticatedUser());
			return EVAL_BODY_INCLUDE;
		}
		*/
		
		else {
			pageContext.setAttribute("authenticatedUser", context.getAuthenticatedUser());	
		}
		return EVAL_BODY_INCLUDE;
	}

}
