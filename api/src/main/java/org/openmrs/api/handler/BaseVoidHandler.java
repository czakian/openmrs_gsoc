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

import org.openmrs.User;
import org.openmrs.Voidable;
import org.openmrs.annotation.Handler;
import org.openmrs.aop.RequiredDataAdvice;

/**
 * This is the super interface for all void* actions that take place on all services. The
 * {@link RequiredDataAdvice} class uses AOP around each method in every service to check to see if
 * its a void* method. If it is a void* method, this class is called to handle setting the
 * {@link Voidable#isVoided()}, {@link Voidable#setVoidReason(String)},
 * {@link Voidable#setVoiddBy(User)}, and {@link Voidable#setDateVoidd(Date)}. <br/>
 * <br/>
 * Child collections on this {@link Voidable} that are themselves a {@link Voidable} are looped over
 * and also voided by the {@link RequiredDataAdvice} class.<br/>
 * <br/>
 * This class will only set the voidedBy and dateVoided attributes if voided is set to false. If
 * voided is set to true it is assumed that this object is in a list of things that is getting
 * voided but that it itself was previously voided. The workaround to this is that if the voided bit
 * is true OR the voidedBy is null, the voidedBy, dateVoided, and voidReason will be set.
 * 
 * @see RequiredDataAdvice
 * @see UnvoidHandler
 * @since 1.5
 */
@Handler(supports = Voidable.class)
public class BaseVoidHandler implements VoidHandler<Voidable> {
	
	/**
	 * Sets all void attributes to the given parameters.
	 * 
	 * @see org.openmrs.api.handler.RequiredDataHandler#handle(org.openmrs.OpenmrsObject,
	 *      org.openmrs.User, java.util.Date, java.lang.String)
	 * @should set the voided bit
	 * @should set the voidReason
	 * @should set voidedBy
	 * @should not set voidedBy if non null
	 * @should set dateVoided
	 * @should not set dateVoided if non null
	 * @should not set the voidReason if already voided
	 * @should set voidedBy even if voided bit is set but voidedBy is null
	 */
	public void handle(Voidable voidableObject, User voidingUser, Date voidedDate, String voidReason) {
		
		// skip over all work if the object is already voided
		if (!voidableObject.isVoided() || voidableObject.getVoidedBy() == null) {
			
			voidableObject.setVoided(true);
			voidableObject.setVoidReason(voidReason);
			
			if (voidableObject.getVoidedBy() == null) {
				voidableObject.setVoidedBy(voidingUser);
			}
			if (voidableObject.getDateVoided() == null) {
				voidableObject.setDateVoided(voidedDate);
			}
		}
	}
	
}
