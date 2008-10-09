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
package org.openmrs.messagesource;

import java.util.Properties;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

/**
 * Interface for a service which expands on the basic capabilities 
 * of a message source. 
 * 
 * ABKTODO: move this into a different package. This is part of the
 * application framework or presentation layer, not the domain. But, 
 * the module support presumes to manipulate the application context
 * (the file system), so this needs to be somewhere above the module's 
 * package dependency path. Luckily, the package dependency graph is
 * thoroughly cyclic, so it can probably go anywhere.
 */
@Transactional
public interface MessageSourceService extends MutableMessageSource {
	
	/**
	 * Get the message with the given code from the current user's
	 * selected locale.
	 * 
	 * @see #getMessage(String arg0, Object[] arg1, java.util.Locale arg2)
	 * @param s message code to retrieve
	 * @return the translated message
	 */
	@Transactional(readOnly=true)
	public String getMessage(String s);
	
	/**
	 * Gets the message source service which is currently providing
	 * services.
	 * 
     * @return the activeMessageSource
     */
	@Transactional(readOnly=true)
	public MutableMessageSource getActiveMessageSource();

	/**
	 * Sets the message source service which will actually provide services. 
	 * 
     * @param activeMessageSourceService the activeMessageSourceService to set
     */
    public void setActiveMessageSource(MutableMessageSource activeMessageSource);
    

	/**
	 * Presumes to append the messages to a message.properties file which is
	 * already being monitored by the super
	 * ReloadableResourceBundleMessageSource.
	 * 
	 * This is a blind, trusting hack.
	 * 
	 * @see org.openmrs.message.MessageSourceService#publishProperties(java.util.Properties,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public void publishProperties(Properties props, String locale,
	        String namespace, String name, String version);

	/**
     * @return the availableMessageSources
     */
	@Transactional(readOnly=true)
	public Set<MutableMessageSource> getMessageSources();

	/**
     * @param availableMessageSources the availableMessageSources to set
     */
    public void setMessageSources(
            Set<MutableMessageSource> availableMessageSources);
	
}
