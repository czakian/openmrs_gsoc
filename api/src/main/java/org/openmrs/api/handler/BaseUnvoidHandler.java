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
 * This is the super interface for all unvoid* actions that take place on all services. The
 * {@link RequiredDataAdvice} class uses AOP around each method in every service to check to see if
 * its a unvoid* method. If it is a unvoid* method, this class is called to handle setting the
 * {@link Voidable#isVoided()}, {@link Voidable#setVoidReason(String)},
 * {@link Voidable#setVoiddBy(User)}, and {@link Voidable#setDateVoidd(Date)} all to null. <br/>
 * <br/>
 * Child collections on this {@link Voidable} that are themselves a {@link Voidable} are looped over
 * and also unvoided by the {@link RequiredDataAdvice} class.<br/>
 * <br/>
 * 
 * @see RequiredDataAdvice
 * @see VoidHandler
 * @since 1.5
 */
@Handler(supports = Voidable.class)
public class BaseUnvoidHandler implements UnvoidHandler<Voidable> {
	
	/**
	 * Called around every unvoid* method to set everything to null.<br/>
	 * <br/>
	 * 
	 * @see org.openmrs.api.handler.RequiredDataHandler#handle(org.openmrs.OpenmrsObject,
	 *      org.openmrs.User, java.util.Date, java.lang.String)
	 * @should unset the voided bit
	 * @should unset the voider
	 * @should unset the dateVoided
	 * @should unset the voidReason
	 * @should only act on already voided objects
	 * @should not act on objects with a different dateVoided
	 */
	public void handle(Voidable voidableObject, User voidingUser, Date origParentVoidedDate, String unused) {
		
		// only operate on voided objects
		if (voidableObject.isVoided()) {
			
			// only unvoid objects that were voided at the same time as the parent object
			if (origParentVoidedDate == null || origParentVoidedDate.equals(voidableObject.getDateVoided())) {
				voidableObject.setVoided(false);
				voidableObject.setVoidedBy(null);
				voidableObject.setDateVoided(null);
				voidableObject.setVoidReason(null);
			}
		}
	}
	
}
