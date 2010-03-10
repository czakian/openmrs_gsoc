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
package org.openmrs.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.db.PatientDAO;
import org.openmrs.patient.IdentifierValidator;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.validator.PatientIdentifierValidator;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains methods pertaining to Patients in the system Use:<br/>
 * 
 * <pre>
 *   List<Patient> patients = Context.getPatientService().getAllPatients();
 * </pre>
 * 
 * @see org.openmrs.api.context.Context
 * @see org.openmrs.Patient
 */
@Transactional
public interface PatientService extends OpenmrsService {
	
	/**
	 * Sets the DAO for this service. This is done by DI and Spring. See the
	 * applicationContext-service.xml definition file.
	 * 
	 * @param dao DAO for this service
	 */
	public void setPatientDAO(PatientDAO dao);
	
	/**
	 * @see #savePatient(Patient)
	 * @deprecated replaced by #savePatient(Patient)
	 */
	@Authorized( { OpenmrsConstants.PRIV_ADD_PATIENTS } )
	public Patient createPatient(Patient patient) throws APIException;
	
	/**
	 * Saved the given <code>patient</code> to the database
	 * 
	 * @param patient patient to be created or updated
	 * @return patient who was created or updated
	 * @throws APIException
	 * @should create new patient from existing person plus user object
	 * @should not throw a NonUniqueObjectException when called with a hand constructed patient
	 *         regression 1375
	 * @should fail when patient does not have any patient identifiers
	 * @should fail when patient does not have required patient identifiers
	 */
	@Authorized( { OpenmrsConstants.PRIV_ADD_PATIENTS, OpenmrsConstants.PRIV_EDIT_PATIENTS })
	public Patient savePatient(Patient patient) throws APIException;
	
	/**
	 * Get patient by internal identifier
	 * 
	 * @param patientId internal patient identifier
	 * @return patient with given internal identifier
	 * @throws APIException
	 * @should return null object if patient id doesnt exist
	 * @should fetch patient with given patient id
	 * @should return null when patient with given patient id does not exist
	 */
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	@Transactional(readOnly = true)
	public Patient getPatient(Integer patientId) throws APIException;
	
	/**
	 * Get patient by universally unique identifier.
	 * 
	 * @param uuid universally unique identifier
	 * @return the patient that matches the uuid
	 * @throws APIException
	 * @should fetch patient with given uuid
	 * @should return null if patient not found with given uuid 
	 */
	@Transactional(readOnly = true)
	public Patient getPatientByUuid(String uuid) throws APIException;
	
	/**
	 * Get patient identifier by universally unique identifier.
	 * 
	 * @param uuid universally unique identifier
	 * @return the patient identifier that matches the uuid
	 * @throws APIException
	 * @should fetch patient identifier with given uuid
	 * @should return null if patient identifier not found with given uuid 
	 */
	@Transactional(readOnly = true)
	public PatientIdentifier getPatientIdentifierByUuid(String uuid) throws APIException;
	
	/**
	 * @see #savePatient(Patient)
	 * @deprecated replaced by #savePatient(Patient)
	 */
	public Patient updatePatient(Patient patient) throws APIException;
	
	/**
	 * Returns all non voided patients in the system
	 * 
	 * @return non voided patients in the system
	 * @see #getAllPatients(boolean)
	 * @throws APIException
	 * @should fetch all non voided patients
	 */
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	@Transactional(readOnly = true)
	public List<Patient> getAllPatients() throws APIException;
	
	/**
	 * Returns patients in the system
	 * 
	 * @param includeVoided if false, will limit the search to non-voided patients
	 * @return patients in the system
	 * @throws APIException
	 * @should fetch voided patients when given include voided is true
	 * @should fetch non voided patients when given include voided is false
	 */
	@Authorized({ OpenmrsConstants.PRIV_VIEW_PATIENTS } )
	@Transactional(readOnly = true)
	public List<Patient> getAllPatients(boolean includeVoided) throws APIException;
	
	/**
	 * @deprecated use #getPatientByIdentifier(String) instead
	 */
	@Transactional(readOnly = true)
	public Patient identifierInUse(String identifier, PatientIdentifierType type, Patient ignorePatient);
	
	/**
	 * @deprecated replaced by {@link #getPatients(String, String, List)}
	 */
	@Transactional(readOnly = true)
	public List<Patient> getPatientsByIdentifier(String identifier, boolean includeVoided) throws APIException;
	
	/**
	 * Get patients based on given criteria The identifier is matched with the regex
	 * <code>OpenmrsConstants.PATIENT_IDENTIFIER_REGEX</code> All parameters are optional and
	 * nullable. If null, it is not included in the search. Will not return voided patients
	 * 
	 * @param name (optional) this is a slight break from the norm, patients with a partial match on
	 *            this name will be returned
	 * @param identifier (optional) only patients with a matching identifier are returned
	 * @param identifierTypes (optional) the PatientIdentifierTypes to restrict to
	 * @param matchIdentifierExactly (required) if true, then the given <code>identifier</code> must
	 *            equal the id in the database. if false, then the identifier is 'searched' for by
	 *            using a regular expression
	 * @return patients that matched the given criteria (and are not voided)
	 * @throws APIException
	 * @should fetch all patients that partially matches given name
	 * @should fetch all patients that partially matches given identifier when match identifier exactly equals false
	 * @should fetch all patients that exactly matches given identifier when match identifier exactly equals true
	 * @should fetch all patients that match given identifier types
	 * @should not return duplicates
	 * @should return empty list when no match is found
	 * @should search familyName2 with name
	 * @should not allow percentage wildcard character
	 * @should not allow asterisk wildcard character
	 * @should support simple regex 
 	 * @should support pattern using last digit as check digit  
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public List<Patient> getPatients(String name, String identifier, List<PatientIdentifierType> identifierTypes,
	                                 boolean matchIdentifierExactly) throws APIException;
	
	/**
	 * @deprecated replaced by a call to {@link #getPatients(String, String, List, boolean)} with
	 *             "false" as the last parameter
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public List<Patient> getPatients(String name, String identifier, List<PatientIdentifierType> identifierTypes)
	                                                                                                             throws APIException;
	
	/**
	 * @deprecated replaced by getPatients( ... )
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public List<Patient> getPatientsByIdentifierPattern(String identifier, boolean includeVoided) throws APIException;
	
	/**
	 * @deprecated replaced by {@link #getPatients(String, String, List)}
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public List<Patient> getPatientsByName(String name) throws APIException;
	
	/**
	 * @deprecated replaced by getPatients( ... )
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public List<Patient> getPatientsByName(String name, boolean includeVoided) throws APIException;
	
	/**
	 * Void patient record (functionally delete patient from system)
	 * 
	 * @param patient patient to be voided
	 * @param reason reason for voiding patient
	 * @return the voided patient
	 * @should void given patient with given reason
	 * @should void all patient identifiers associated with given patient
	 * @should return voided patient with given reason
	 * @should return null when patient is null
	 */
	@Authorized( { OpenmrsConstants.PRIV_DELETE_PATIENTS })
	public Patient voidPatient(Patient patient, String reason) throws APIException;
	
	/**
	 * Unvoid patient record
	 * 
	 * @param patient patient to be revived
	 * @return the revived Patient
	 * @should unvoid given patient 
	 * @should return unvoided patient
	 */
	@Authorized( { OpenmrsConstants.PRIV_DELETE_PATIENTS })
	public Patient unvoidPatient(Patient patient) throws APIException;
	
	/**
	 * @see #purgePatient(Patient)
	 * @deprecated replaced by {@link #purgePatient(Patient)}
	 */
	@Authorized( { OpenmrsConstants.PRIV_PURGE_PATIENTS })
	public void deletePatient(Patient patient) throws APIException;
	
	/**
	 * Delete patient from database. This <b>should not be called</b> except for testing and
	 * administration purposes. Use the void method instead.
	 * 
	 * @param patient patient to be deleted
	 * @throws APIException
	 * @see #voidPatient(org.openmrs.Patient,java.lang.String)
	 * @should delete patient from database
	 */
	@Authorized( { OpenmrsConstants.PRIV_PURGE_PATIENTS })
	public void purgePatient(Patient patient) throws APIException;
	
	/**
	 * @deprecated replaced by {@link #getPatientIdentifiers(String, List, List, List, Boolean)}
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENT_IDENTIFIERS })
	public List<PatientIdentifier> getPatientIdentifiers(PatientIdentifierType patientIdentifierType) throws APIException;
	
	/**
	 * Get all patientIdentifiers that match all of the given criteria Voided identifiers are not
	 * returned
	 * 
	 * @param identifier the full identifier to match on
	 * @param patientIdentifierTypes the type of identifiers to get
	 * @param locations the locations of the identifiers to match
	 * @param patients the patients containing these identifiers
	 * @param isPreferred if true, limits to only preferred identifiers if false, only non
	 *            preferred. if null, ignores preferred status
	 * @return PatientIdentifiers matching these criteria
	 * @should return only non voided patients and patient identifiers
	 * @throws APIException
	 * @should fetch patient identifiers that exactly matches given identifier
	 * @should fetch patient identifiers that partially matches given identifier
	 * @should fetch patient identifiers that match given patient identifier types
	 * @should fetch patient identifiers that match given locations
	 * @should fetch patient identifiers that match given patients
	 * @should fetch preferred patient identifiers when given is preferred equals true
	 * @should fetch non preferred patient identifiers when given is preferred equals false
	 * @should fetch preferred and non preferred patient identifiers when given is preferred is null
	 * 
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENT_IDENTIFIERS })
	public List<PatientIdentifier> getPatientIdentifiers(String identifier,
	                                                     List<PatientIdentifierType> patientIdentifierTypes,
	                                                     List<Location> locations, List<Patient> patients,
	                                                     Boolean isPreferred) throws APIException;
	
	/**
	 * @deprecated replaced by {@link #getPatientIdentifiers(String, List, List, List, Boolean)}
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENT_IDENTIFIERS })
	public List<PatientIdentifier> getPatientIdentifiers(String identifier, PatientIdentifierType pit) throws APIException;
	
	/**
	 * Update patient identifier
	 * 
	 * @param patientIdentifier identifier to be updated
	 * @deprecated patient identifiers should not be updated directly; rather, after changing
	 *             patient identifiers, use {@link #savePatient(Patient)} to save changes to the
	 *             database
	 * @throws APIException
	 */
	@Authorized( { OpenmrsConstants.PRIV_EDIT_PATIENT_IDENTIFIERS })
	public void updatePatientIdentifier(PatientIdentifier patientIdentifier) throws APIException;
	
	/**
	 * Create or update a PatientIdentifierType
	 * 
	 * @param patientIdentifierType PatientIdentifierType to create or update
	 * @return the saved type
	 * @throws APIException
	 * @should create new patient identifier type
	 * @should update existing patient identifier type 
	 */
	@Authorized( { OpenmrsConstants.PRIV_MANAGE_IDENTIFIER_TYPES })
	public PatientIdentifierType savePatientIdentifierType(PatientIdentifierType patientIdentifierType) throws APIException;
	
	/**
	 * @see #getAllPatientIdentifierTypes()
	 * @deprecated replaced by {@link #getAllPatientIdentifierTypes()}
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_IDENTIFIER_TYPES })
	public List<PatientIdentifierType> getPatientIdentifierTypes() throws APIException;
	
	/**
	 * Get all patientIdentifier types
	 * 
	 * @return patientIdentifier types list
	 * @throws APIException
	 * @should fetch all non retired patient identifier types 
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_IDENTIFIER_TYPES })
	public List<PatientIdentifierType> getAllPatientIdentifierTypes() throws APIException;
	
	/**
	 * Get all patientIdentifier types
	 * 
	 * @param includeRetired true/false whether retired types should be included
	 * @return patientIdentifier types list
	 * @throws APIException
	 * 
	 * @should fetch patient identifier types including retired when include retired is true
	 * @should fetch patient identifier types excluding retired when include retired is false
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_IDENTIFIER_TYPES })
	public List<PatientIdentifierType> getAllPatientIdentifierTypes(boolean includeRetired) throws APIException;
	
	/**
	 * Get all patientIdentifier types that match the given criteria
	 * 
	 * @param name name of the type to match on
	 * @param format the string format to match on
	 * @param required if true, limits to only identifiers marked as required if false, only non
	 *            required. if null, ignores required bit
	 * @param hasCheckDigit if true, limits to only check digit'd identifiers if false, only non
	 *            checkdigit'd. if null, ignores checkDigit
	 * @return patientIdentifier types list
	 * @throws APIException
	 * 
	 * @should fetch patient identifier types that match given name with given format
	 * @should fetch required patient identifier types when given required is true
	 * @should fetch non required patient identifier types when given required is false
	 * @should fetch any patient identifier types when given required is null
	 * @should fetch patient identifier types with check digit when given has check digit is true
	 * @should fetch patient identifier types without check digit when given has check digit is false
	 * @should fetch any patient identifier types when given has check digit is null
	 * 
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_IDENTIFIER_TYPES })
	public List<PatientIdentifierType> getPatientIdentifierTypes(String name, String format, Boolean required,
	                                                             Boolean hasCheckDigit) throws APIException;
	
	/**
	 * Get patientIdentifierType by internal identifier
	 * 
	 * @param patientIdentifierTypeId
	 * @return patientIdentifierType with specified internal identifier
	 * @throws APIException
	 * 
	 * @should fetch patient identifier with given patient identifier type id
	 * @should return null when patient identifier identifier does not exist
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_IDENTIFIER_TYPES })
	public PatientIdentifierType getPatientIdentifierType(Integer patientIdentifierTypeId) throws APIException;
	
	/**
	 * Get patient identifierType by universally unique identifier
	 * 
	 * @param patientIdentifierTypeId
	 * @return patientIdentifierType with specified internal identifier
	 * @throws APIException
	 * 
	 * @should fetch patient identifier type with given uuid 
	 * @should return null when patient identifier type with given uuid does not exist
	 */
	@Transactional(readOnly = true)
	public PatientIdentifierType getPatientIdentifierTypeByUuid(String uuid) throws APIException;
	
	/**
	 * @deprecated use {@link #getPatientIdentifierTypeByName(String)}
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_IDENTIFIER_TYPES })
	public PatientIdentifierType getPatientIdentifierType(String name) throws APIException;
	
	/**
	 * Get patientIdentifierType by exact name
	 * 
	 * @param name
	 * @return patientIdentifierType with given name
	 * @throws APIException
	 * @should fetch patient identifier type that exactly matches given name
	 * @should not return patient identifier type that partially matches given name
	 * @should return null when patient identifier type with given name does not exist
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_IDENTIFIER_TYPES })
	public PatientIdentifierType getPatientIdentifierTypeByName(String name) throws APIException;
	
	/**
	 * Retire a type of patient identifier
	 * 
	 * @param patientIdentifierType type of patient identifier to be retired
	 * @param reason the reason to retire this identifier type
	 * @return the retired type
	 * @throws APIException
	 * @should retire patient identifier type with given reason
	 * @should throw error when reason is empty
	 */
	@Authorized( { OpenmrsConstants.PRIV_MANAGE_IDENTIFIER_TYPES })
	public PatientIdentifierType retirePatientIdentifierType(PatientIdentifierType patientIdentifierType, String reason)
	                                                                                                                    throws APIException;
	
	/**
	 * Unretire a type of patient identifier
	 * 
	 * @param patientIdentifierType type of patient identifier to be unretired
	 * @return the unretired type
	 * @throws APIException
	 * 
	 * @should untire patient identifier type
	 * @should return unretired patient identifier type
	 */
	@Authorized( { OpenmrsConstants.PRIV_MANAGE_IDENTIFIER_TYPES })
	public PatientIdentifierType unretirePatientIdentifierType(PatientIdentifierType patientIdentifierType)
	                                                                                                       throws APIException;
	
	/**
	 * Purge PatientIdentifierType (cannot be undone)
	 * 
	 * @param patientIdentifierType PatientIdentifierType to purge from the database
	 * @throws APIException
	 * @should delete type from database
	 * @should delete patient identifier type from database
	 */
	@Authorized( { OpenmrsConstants.PRIV_PURGE_IDENTIFIER_TYPES })
	public void purgePatientIdentifierType(PatientIdentifierType patientIdentifierType) throws APIException;
	
	/**
	 * Convenience method to validate a patient identifier. Checks for things like blank
	 * identifiers, invalid check digits, etc
	 * 
	 * @param patientIdentifier identifier to be validated
	 * @see #checkPatientIdentifiers(Patient)
	 * @throws PatientIdentifierException if the identifier is invalid
	 * @deprecated use {@link PatientIdentifierValidator#validateIdentifier(PatientIdentifier)}
	 */
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENT_IDENTIFIERS })
	public void checkPatientIdentifier(PatientIdentifier patientIdentifier) throws PatientIdentifierException;
	
	/**
	 * Convenience method to validate all identifiers for a given patient
	 * 
	 * @param patient patient for which to validate identifiers
	 * @see #checkPatientIdentifiers(Patient)
	 * @throws PatientIdentifierException if one or more of the identifiers are invalid
	 * 
	 * @should validate when patient has all required and no duplicate and no blank patient identifiers
	 * @should ignore voided patient identifier
	 * @should remove identifier and throw error when patient has blank patient identifier
	 * @should throw error when patient has null patient identifiers
	 * @should throw error when patient has empty patient identifiers
	 * @should throw error when patient has identical identifiers
	 * @should throw error when patient does not have one or more required identifiers
	 * @should require one non voided patient identifier
	 */
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENT_IDENTIFIERS })
	public void checkPatientIdentifiers(Patient patient) throws PatientIdentifierException;
	
	/**
	 * @see #getPatients(String)
	 * @deprecated use #getPatients(String)
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public List<Patient> findPatients(String query, boolean includeVoided) throws APIException;
	
	/**
	 * Generic search on patients based on the given string. Implementations can use this string to
	 * search on name, identifier, etc Voided patients are not returned in search results
	 * 
	 * @param query the string to search on
	 * @return a list of matching Patients
	 * @should force search string to be greater than minsearchcharacters global property
	 * @should allow search string to be one according to minsearchcharacters global property
	 * @should fetch patients with patient identifiers matching given query
	 * @should fetch patients with any name matching given query 
	 * @should return empty list if given query length less than minimum search characters
	 * @should not fail when minimum search characters is null 
	 * @should not fail when minimum search characters is invalid integer
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public List<Patient> getPatients(String query) throws APIException;
	
	/**
	 * @see #getPatientByExample(Patient)
	 * @deprecated use #getPatientByExample(Patient)
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public Patient findPatient(Patient patientToMatch) throws APIException;
	
	/**
	 * This method tries to find a patient in the database given the attributes on the given
	 * <code>patientToMatch</code> object. Assumes there could be a PersonAttribute on this Patient
	 * with PersonAttributeType.name = "Other Matching Information". This PersonAttribute has a
	 * "value" that is just key value pairs in the form of key:value;nextkey:nextvalue;
	 * 
	 * @param patientToMatch
	 * @return null if no match found, a fresh patient object from the db if is found
	 * 
	 * @should fetch patient matching patient id of given patient  
	 * @should not fetch patient matching any other patient information 
	 * @should return null when no patient matches given patient to match
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public Patient getPatientByExample(Patient patientToMatch) throws APIException;
	
	/**
	 * @deprecated use {@link #getDuplicatePatientsByAttributes(List)}
	 * @see #getDuplicatePatientsByAttributes(List)
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public List<Patient> findDuplicatePatients(Set<String> attributes) throws APIException;
	
	/**
	 * Search the database for patients that both share the given attributes. Each attribute that is
	 * passed in must be identical to what is stored for at least one other patient for both
	 * patients to be returned.
	 * 
	 * @param attributes attributes on a Person or Patient object. similar to: [gender, givenName,
	 *            middleName, familyName]
	 * @return list of patients that match other patients
	 * @throws APIException
	 * 
	 * @should fetch patients that exactly match on all given attributes
	 * @should not return patients that exactly match on some but not all given attributes
	 */
	@Transactional(readOnly = true)
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENTS })
	public List<Patient> getDuplicatePatientsByAttributes(List<String> attributes) throws APIException;
	
	/**
	 * Convenience method to join two patients' information into one record.
	 * <ol>
	 * <li>Moves object (encounters/obs) pointing to <code>nonPreferred</code> to point at
	 * <code>preferred</code></li>
	 * <li>Copies data (gender/birthdate/names/ids/etc) from <code>nonPreferred</code> to
	 * <code>preferred</code> IFF the data is missing or null in <code>preferred</code></li>
	 * <li><code>notPreferred</code> is marked as voided</li>
	 * </ol>
	 * 
	 * @param preferred The Patient to merge to
	 * @param notPreferred The Patient to merge from (and then void)
	 * @throws APIException
	 * @should not merge the same patient to itself
	 * @should copy nonvoided names to preferred patient
	 * @should copy nonvoided identifiers to preferred patient
	 * @should copy nonvoided addresses to preferred patient
	 * @should not copy over relationships that are only between the preferred and notpreferred patient
	 * @should not merge patient with itself
	 * @should merge non voided encounters from non preferred to preferred patient 
	 * @should merge non duplicate patient identifiers from non preferred to preferred patient 
	 * @should merge non duplicate patient names from non preferred to preferred patient 
	 * @should merge non duplicate addresses from non preferred to preferred patient 
	 * @should merge non voided patient programs from non preferred to preferred patient 
	 * @should merge non voided relationships from non preferred to preferred patient 
	 * @should merge observations associated with encounters from non preferred to preferred patient 
	 * @should merge non voided person attributes from non preferred to preferred patient 
	 * @should merge other non voided observations from non preferred to preferred patient 
	 * @should merge other non voided orders from non preferred to preferred patient 
	 * @should merge non preferred death date when preferred death date is not null or empty
     * @should merge non preferred death cause when preferred death cause is not null or empty
	 * @should void non preferred patient
	 */
	@Authorized( { OpenmrsConstants.PRIV_EDIT_PATIENTS })
	public void mergePatients(Patient preferred, Patient notPreferred) throws APIException;
	
	/**
	 * Convenience method to establish that a patient has left the care center. This API call is
	 * responsible for: 1) Closing workflow statuses 2) Terminating programs 3) Discontinuing orders
	 * 4) Flagging patient table (if applicable) 5) Creating any relevant observations about the
	 * patient
	 * 
	 * TODO keep this in the PatientService?  Or move to appropriate service?
	 * 
	 * @param patient - the patient who has exited care
	 * @param dateExited - the declared date/time of the patient's exit
	 * @param reasonForExit - the concept that corresponds with why the patient has been declared as
	 *            exited
	 * @throws APIException
	 * 
	 * @should save reason for exit observation for given patient
	 * @should set death date and cause when given reason for exit equals death
	 * @should discontinue all orders associated with given patient
	 * @should terminate all program workflows associated with given paitent
	 * @should throw error when given patient is null
	 * @should throw error when given date exited is null
	 * @should throw error when given reason for exist is null
	 * @should be tested more thoroughly
	 */
	@Authorized( { OpenmrsConstants.PRIV_EDIT_PATIENTS })
	public void exitFromCare(Patient patient, Date dateExited, Concept reasonForExit) throws APIException;
	
	/**
	 * Convenience method to establish that a patient has died. In addition to exiting the patient
	 * from care (see above), this method will also set the appropriate patient characteristics to
	 * indicate that they have died, when they died, etc.
	 * 
	 * TODO Keep this in the PatientService?  Or move to appropriate service
	 * 
	 * @param patient - the patient who has died
	 * @param dateDied - the declared date/time of the patient's death
	 * @param causeOfDeath - the concept that corresponds with the reason the patient died
	 * @param otherReason - if the concept representing the reason is OTHER NON-CODED, and a
	 *            string-based "other" reason is supplied
	 * @throws APIException
	 * @should be tested more thoroughly
	 */
	@Authorized( { OpenmrsConstants.PRIV_EDIT_PATIENTS })
	public void processDeath(Patient patient, Date dateDied, Concept causeOfDeath, String otherReason) throws APIException;
	
	/**
	 * Convenience method that saves the Obs that indicates when and why the patient died (including
	 * any "other" reason there might be)
	 * 
	 * TODO keep this in the PatientService?
	 * 
	 * @param patient - the patient who has died
	 * @param dateDied - the declared date/time of the patient's death
	 * @param causeOfDeath - the concept that corresponds with the reason the patient died
	 * @param otherReason - if the concept representing the reason is OTHER NON-CODED, and a
	 *            string-based "other" reason is supplied
	 * @throws APIException
	 * 
	 * @should throw error when given patient is null
	 * @should throw error when given death date is null
	 * @should throw error when given cause is null is null
	 * @should throw error when cause of death global property is not specified
	 * @should throw error when patient already has more than one cause of death observations
	 * @should modify existing cause of death observation 
	 * @should set death attributes as long as patient is not already dead
	 * @should be tested more thoroughly
	 * 
	 */
	@Authorized(value = { OpenmrsConstants.PRIV_VIEW_PATIENTS, OpenmrsConstants.PRIV_EDIT_OBS }, requireAll = true)
	public void saveCauseOfDeathObs(Patient patient, Date dateDied, Concept causeOfDeath, String otherReason)
	                                                                                                         throws APIException;
	
	/**
	 * Gets an identifier validator matching the given class.
	 * 
	 * @param clazz identifierValidator which validator to get.
     * @should return patient identifier validator given class
	 */
	public IdentifierValidator getIdentifierValidator(Class<IdentifierValidator> clazz);
	
	/**
     * @should return patient identifier validator given class name
     */
	public IdentifierValidator getIdentifierValidator(String pivClassName);
	
	/**
	 * @return the default IdentifierValidator
	 * @should return default patient identifier validator
	 */
	public IdentifierValidator getDefaultIdentifierValidator();
	
	/**
	 * @return All registered PatientIdentifierValidators
	 * @should return all registered patient identifier validators
	 */
	public Collection<IdentifierValidator> getAllIdentifierValidators();
	
	/**
	 * Checks whether the given patient identifier is already assigned to a patient other than
	 * patientIdentifier.patient
	 * 
	 * @param patientIdentifier the patient identifier to look for in other patients
	 * @return whether or not the identifier is in use by a patient other than
	 *         patientIdentifier.patient
	 * @should return true when patientIdentifier contains a patient and another patient has this id
	 * @should return false when patientIdentifier contains a patient and no other patient has this id
	 * @should return true when patientIdentifier does not contain a patient and a patient has this id
	 * @should return false when patientIdentifier does not contain a patient and no patient has this id
	 * @should ignore voided patientIdentifiers
	 * @should ignore voided patients
	 */
	@Authorized(OpenmrsConstants.PRIV_VIEW_PATIENTS)
	@Transactional(readOnly = true)
	public boolean isIdentifierInUseByAnotherPatient(PatientIdentifier patientIdentifier);
	
	/**
	 * Returns a patient identifier that matches the given patientIndentifier id
	 * 
	 * @param patientIdentifier the patientIdentifier id
	 * @return the patientIdentifier matching the Id
	 * @throws APIException
	 * @should return the patientIdentifier with the given id
	 */
	@Authorized( { OpenmrsConstants.PRIV_VIEW_PATIENT_IDENTIFIERS })
	@Transactional(readOnly = true)
	public PatientIdentifier getPatientIdentifier(Integer patientIdentifierId) throws APIException;
	
	/**
	 * Void patient identifier (functionally delete patient identifier from system)
	 * 
	 * @param patient patientIdentifier to be voided
	 * @param reason reason for voiding patient identifier
	 * @return the voided patient identifier
	 * @throws APIException
	 * @should void given patient identifier with given reaso
	 * @should throw an APIException if the reason is null
	 * @should throw an APIException if the reason is an empty string
	 * @should throw an APIException if the reason is a white space character
	 */
	@Authorized( { OpenmrsConstants.PRIV_DELETE_PATIENT_IDENTIFIERS })
	public PatientIdentifier voidPatientIdentifier(PatientIdentifier patientIdentifier, String reason) throws APIException;
	
	/**
	 * Saved the given <code>patientIndentifier</code> to the database
	 * 
	 * @param patientIndentifier patientIndentifier to be created or updated
	 * @return patientIndentifier that was created or updated
	 * @throws APIException
	 * @should create new patientIndentifier
	 * @should update an existing patient identifier
	 * @should throw an APIException when a null argument is passed
	 * @should throw an APIException when one of the required fields is null
	 * @should throw an APIException if the patientIdentifier string is a white space
	 * @should throw an APIException if the patientIdentifier string is an empty string
	 * 
	 */
	@Authorized( { OpenmrsConstants.PRIV_ADD_PATIENT_IDENTIFIERS, OpenmrsConstants.PRIV_EDIT_PATIENT_IDENTIFIERS })
	public PatientIdentifier savePatientIdentifier(PatientIdentifier patientIdentifier) throws APIException;
	
	/**
	 * Purge PatientIdentifier (cannot be undone)
	 * 
	 * @param patientIdentifier PatientIdentifier to purge from the database
	 * @throws APIException
	 * @should delete patient identifier from database
	 */
	@Authorized( { OpenmrsConstants.PRIV_PURGE_PATIENT_IDENTIFIERS })
	public void purgePatientIdentifier(PatientIdentifier patientIdentifier) throws APIException;
	
}
