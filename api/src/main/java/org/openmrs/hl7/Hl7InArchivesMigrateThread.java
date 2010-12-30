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
package org.openmrs.hl7;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;

/**
 * Separate thread to move the hl7 in archives from the database tables to the filesystem. It is
 * highly recommended to start this thread by calling "startHl7ArchiveMigration(UserContext)" method
 * in the service layer as opposed to calling the thread's start() method to ensure the thread is
 * started after making all the necessary checks.
 * 
 * @see {@link HL7Service#startHl7ArchiveMigration()}
 */
public class Hl7InArchivesMigrateThread extends Thread {
	
	private static final Log log = LogFactory.getLog(Hl7InArchivesMigrateThread.class);
	
	/**
	 * Map holds data about the progress of the transfer process, that is numberTransferred and
	 * numberOfFailedTransfers
	 */
	private static Map<String, Integer> progressStatusMap;
	
	/**
	 * number of days to keep when migrating
	 */
	private static Integer daysKept = 365;
	
	/**
	 * Whether or not activity should continue with this thread
	 */
	private static boolean active = false;
	
	/**
	 * User Context to be used for authentication and privilege checks
	 */
	private UserContext userContext;
	
	/**
	 * Flag to keep track of the status of the migration process
	 */
	private static Status transferStatus = Status.NONE;
	
	/**
	 * The different states this thread can be in at a given point during migration
	 */
	public enum Status {
		RUNNING, STOPPED, COMPLETED, ERROR, NONE
	}
	
	/**
	 * Constructor to initialize variables
	 */
	public Hl7InArchivesMigrateThread() {
		this.userContext = Context.getUserContext();
		progressStatusMap = new HashMap<String, Integer>();
		progressStatusMap.put(HL7Constants.NUMBER_TRANSFERRED_KEY, 0);
		progressStatusMap.put(HL7Constants.NUMBER_OF_FAILED_TRANSFERS_KEY, 0);
	}
	
	/**
	 * @return the daysKept
	 */
	public static Integer getDaysKept() {
		return daysKept;
	}
	
	/**
	 * @param daysKept the daysKept to set
	 */
	public static void setDaysKept(Integer daysKept) {
		Hl7InArchivesMigrateThread.daysKept = daysKept;
	}
	
	/**
	 * @return the active
	 */
	public static boolean isActive() {
		return active;
	}
	
	/**
	 * @param active the active to set
	 */
	public static void setActive(boolean active) {
		Hl7InArchivesMigrateThread.active = active;
	}
	
	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		Context.openSession();
		Context.setUserContext(userContext);
		transferStatus = Status.RUNNING;
		
		while (isActive() && transferStatus == Status.RUNNING) {
			try {
				// migrate the archives
				if (isActive())
					Context.getHL7Service().migrateHl7InArchivesToFileSystem(progressStatusMap);
				
				//if transfer is done when user didn't just stop it
				if (transferStatus != Status.STOPPED)
					transferStatus = Status.COMPLETED;
				
			}
			catch (APIException api) {
				// log this as a debug, because we want to swallow minor errors 
				log.debug("Unable to migrate HL7 archive", api);
				
				try {
					Thread.sleep(HL7Constants.THREAD_SLEEP_PERIOD);
				}
				catch (InterruptedException e) {
					log.warn("Hl7 in archive migration thread has been abnormally interrupted", e);
				}
				
			}
			catch (Exception e) {
				transferStatus = Status.ERROR;
				log.warn("Some error occurred while migrating hl7 archives", e);
			}
		}
		// clean up
		Context.closeSession();
		setActive(false);
	}
	
	/**
	 * convenience method to set transfer status and active flag to stop migration
	 */
	public static void stopMigration() {
		transferStatus = Status.STOPPED;
		setActive(false);
	}
	
	/**
	 * @return the transferStatus
	 */
	public static Status getTransferStatus() {
		return transferStatus;
	}
	
	/**
	 * @return the numberTransferred at a given time during migration
	 */
	public static Integer getNumberTransferred() {
		if (progressStatusMap == null)
			return 0;
		return progressStatusMap.get(HL7Constants.NUMBER_TRANSFERRED_KEY);
	}
	
	/**
	 * Gets the number of failed transfers during migration, this could be that the system couldn't
	 * write them to the file system or couldn't be deleted from the database.
	 * 
	 * @return the numberOfFailedTransfers
	 */
	public static Integer getNumberOfFailedTransfers() {
		if (progressStatusMap == null)
			return 0;
		return progressStatusMap.get(HL7Constants.NUMBER_OF_FAILED_TRANSFERS_KEY);
	}
	
	/**
	 * @return the userContext
	 */
	public UserContext getUserContext() {
		return this.userContext;
	}
	
}
