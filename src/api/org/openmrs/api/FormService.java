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
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Field;
import org.openmrs.FieldAnswer;
import org.openmrs.FieldType;
import org.openmrs.Form;
import org.openmrs.FormField;
import org.openmrs.annotation.Authorized;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service contains methods relating to Form, FormField, and Field. Methods relating to
 * FieldType are in AdministrationService
 */
@Transactional
public interface FormService extends OpenmrsService {
	
	/**
	 * Create or update the given Form in the database
	 * 
	 * @param form the Form to save
	 * @return the Form that was saved
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public Form saveForm(Form form) throws APIException;
	
	/**
	 * @deprecated use {@link #saveForm(Form)}
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public Form createForm(Form form) throws APIException;
	
	/**
	 * Get form by internal form identifier
	 * 
	 * @param formId internal identifier
	 * @return requested form
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public Form getForm(Integer formId) throws APIException;
	
	/**
	 * Get form by exact name match. If there are multiple forms with this name, then this returns
	 * the one with the highest version (sorted alphabetically)
	 * 
	 * @param name exact name of the form to fetch
	 * @return requested form
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public Form getForm(String name) throws APIException;
	
	/**
	 * Get form by exact name & version match. If version is null, then this method behaves like
	 * {@link #getForm(String)}
	 * 
	 * @param name exact name of the form to fetch
	 * @param version exact version of the form to fetch
	 * @return requested form
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public Form getForm(String name, String version) throws APIException;
	
	/**
	 * Gets all Forms, including retired ones.
	 * 
	 * @return all Forms, including retired ones
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getAllForms() throws APIException;
	
	/**
	 * Gets all forms, possibly including retired ones
	 * 
	 * @param includeRetired whether or not to return retired forms
	 * @return all forms, possibly including retired ones
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getAllForms(boolean includeRetired) throws APIException;
	
	/**
	 * Gets all forms with name similar to the given name. (The precise fuzzy matching algorithm is
	 * not specified.)
	 * 
	 * @param fuzzyName approximate name to match
	 * @param onlyLatestVersion whether or not to return only the latest version of each form (by
	 *            name)
	 * @return forms with names similar to fuzzyName
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getForms(String fuzzyName, boolean onlyLatestVersion);
	
	/**
	 * @deprecated use
	 *             {@link #getForms(String, Boolean, Collection, Boolean, Collection, Collection, Collection)}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getForms(String partialNameSearch, Boolean published, Collection<EncounterType> encounterTypes,
	                           Boolean retired, Collection<FormField> containingAnyFormField,
	                           Collection<FormField> containingAllFormFields);
	
	/**
	 * Gets all forms that match all the (nullable) criteria
	 * 
	 * @param partialNameSearch partial search of name
	 * @param published whether the form is published
	 * @param encounterTypes whether the form has any of these encounter types
	 * @param retired whether the form is retired
	 * @param containingAnyFormField includes forms that contain any of the specified FormFields
	 * @param containingAllFormFields includes forms that contain all of the specified FormFields
	 * @param fields whether the form has any of these fields. If a field is used more than once on
	 *            a form, that form is returning more than once in this list
	 * @return All forms that match the criteria
	 * @should get multiple of the same form by field
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getForms(String partialNameSearch, Boolean published, Collection<EncounterType> encounterTypes,
	                           Boolean retired, Collection<FormField> containingAnyFormField,
	                           Collection<FormField> containingAllFormFields, Collection<Field> fields);
	
	/**
	 * Returns all published forms (not including retired ones)
	 * 
	 * @return all published non-retired forms
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getPublishedForms() throws APIException;
	
	/**
	 * Get all forms. If publishedOnly is true, a form must be marked as 'published' to be included
	 * in the list
	 * 
	 * @param published
	 * @return List of forms
	 * @throws APIException
	 * @deprecated use {@link #getAllForms()} or {@link #getPublishedForms()}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getForms(boolean publishedOnly) throws APIException;
	
	/**
	 * Get all forms. If publishedOnly is true, a form must be marked as 'published' to be included
	 * in the list. If includeRetired is true 'retired' must be set to false to be include in the
	 * list
	 * 
	 * @param publishedOnly
	 * @param includeRetired
	 * @return
	 * @throws APIException
	 * @deprecated use {@link #getAllForms()} or {@link #getPublishedForms()} or
	 *             {@link #getForms(String, Boolean, Collection, Boolean, Collection, Collection)}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getForms(boolean publishedOnly, boolean includeRetired) throws APIException;
	
	/**
	 * Save changes to form
	 * 
	 * @param form
	 * @throws APIException
	 * @deprecated use {@link #saveForm(Form)}
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void updateForm(Form form) throws APIException;
	
	/**
	 * Duplicate this form and form_fields associated with this form
	 * 
	 * @param form
	 * @return New duplicated form
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public Form duplicateForm(Form form) throws APIException;
	
	/**
	 * Retires the Form, leaving it in the database, but removing it from data entry screens
	 * 
	 * @param form the Form to retire
	 * @param reason the retiredReason to set
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void retireForm(Form form, String reason) throws APIException;
	
	/**
	 * Unretires a Form that had previous been retired.
	 * 
	 * @param form the Form to revive
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void unretireForm(Form form) throws APIException;
	
	/**
	 * Completely remove a Form from the database. This is not reversible. It will fail if this form
	 * has already been used to create Encounters
	 * 
	 * @param form
	 * @return
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void purgeForm(Form form) throws APIException;
	
	/**
	 * Completely remove a Form from the database. This is not reversible. !! WARNING: Calling this
	 * method with cascade=true can be very destructive !!
	 * 
	 * @param form
	 * @param cascade whether or not to cascade delete all dependent objects (including encounters!)
	 * @return
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void purgeForm(Form form, boolean cascade) throws APIException;
	
	/**
	 * Delete form from database. This is included for troubleshooting and low-level system
	 * administration. Ideally, this method should <b>never</b> be called &mdash; <code>Forms</code>
	 * should be <em>retired</em> and not <em>deleted</em> altogether (since many foreign key
	 * constraints depend on forms, deleting a form would require deleting all traces, and any
	 * historical trail would be lost). This method only clears form roles and attempts to delete
	 * the form record. If the form has been included in any other parts of the database (through a
	 * foreign key), the attempt to delete the form will violate foreign key constraints and fail.
	 * 
	 * @param form
	 * @throws APIException
	 * @deprecated use {@link #purgeForm(Form)}
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void deleteForm(Form form) throws APIException;
	
	/**
	 * @deprecated use {@link #getAllFieldTypes()}
	 */
	@Authorized(OpenmrsConstants.PRIV_VIEW_FIELD_TYPES)
	@Transactional(readOnly = true)
	public List<FieldType> getFieldTypes() throws APIException;
	
	/**
	 * Get all field types in the database including the retired ones
	 * 
	 * @return list of all field types
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_VIEW_FIELD_TYPES)
	@Transactional(readOnly = true)
	public List<FieldType> getAllFieldTypes() throws APIException;
	
	/**
	 * Get all field types in the database with or without retired ones
	 * 
	 * @param includeRetired true/false whether to include the retired field types
	 * @return list of all field types
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_VIEW_FIELD_TYPES)
	@Transactional(readOnly = true)
	public List<FieldType> getAllFieldTypes(boolean includeRetired) throws APIException;
	
	/**
	 * Get fieldType by internal identifier
	 * 
	 * @param fieldType id to get
	 * @return fieldType with given internal identifier
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FIELD_TYPES)
	public FieldType getFieldType(Integer fieldTypeId) throws APIException;
	
	/**
	 * @deprecated use {@link #getAllForms()}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getForms() throws APIException;
	
	/**
	 * @deprecated use {@link #getFormsContainingConcept(Concept)}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public Set<Form> getForms(Concept c) throws APIException;
	
	/**
	 * Returns all forms that contain the given concept as a field in their schema. (includes
	 * retired forms)
	 * 
	 * @param concept the concept to search for in forms
	 * @return forms containing the specified concept in their schema
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> getFormsContainingConcept(Concept concept) throws APIException;
	
	/**
	 * @deprecated use {@link Form#getFormFields()}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<FormField> getFormFields(Form form) throws APIException;
	
	/**
	 * Returns all FormFields in the database
	 * 
	 * @return all FormFields in the database
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<FormField> getAllFormFields() throws APIException;
	
	/**
	 * @return list of fields in the db matching part of search term
	 * @throws APIException
	 * @deprecated use {@link #getFields(String)}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Field> findFields(String searchPhrase) throws APIException;
	
	/**
	 * Find all Fields whose names are similar to or contain the given phrase. (The exact similarity
	 * algorithm is unspecified.) (includes retired fields)
	 * 
	 * @param fuzzySearchPhrase
	 * @return Fields with names similar to or containing the given phrase
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Field> getFields(String fuzzySearchPhrase) throws APIException;
	
	/**
	 * @deprecated use {@link #getFieldsByConcept(Concept)}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Field> findFields(Concept concept) throws APIException;
	
	/**
	 * Finds all Fields that point to the given concept, including retired ones.
	 * 
	 * @param concept the concept to search for in the Field table
	 * @return fields that point to the given concept
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Field> getFieldsByConcept(Concept concept) throws APIException;
	
	/**
	 * Fetches all Fields in the database, including retired ones
	 * 
	 * @return all Fields
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Field> getAllFields() throws APIException;
	
	/**
	 * Fetches all Fields in the database, possibly including retired ones
	 * 
	 * @param includeRetired whether or not to include retired Fields
	 * @return all Fields
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Field> getAllFields(boolean includeRetired) throws APIException;
	
	/**
	 * Returns all Fields that match these (nullable) criteria
	 * 
	 * @param forms on any of these Forms
	 * @param fieldTypes having any of these FieldTypes
	 * @param concepts for any of these Concepts
	 * @param tableNames for any of these table names
	 * @param attributeNames for any of these attribute names
	 * @param selectMultiple whether to return only select-multi fields
	 * @param containsAllAnswers fields with all the following answers
	 * @param containsAnyAnswer fields with any of the following answers
	 * @param retired only retired/unretired fields
	 * @return all Fields matching the given criteria
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Field> getFields(Collection<Form> forms, Collection<FieldType> fieldTypes, Collection<Concept> concepts,
	                             Collection<String> tableNames, Collection<String> attributeNames, Boolean selectMultiple,
	                             Collection<FieldAnswer> containsAllAnswers, Collection<FieldAnswer> containsAnyAnswer,
	                             Boolean retired) throws APIException;
	
	/**
	 * @deprecated use {@link #getAllFields()}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Field> getFields() throws APIException;
	
	/**
	 * Gets a Field by internal database id
	 * 
	 * @param fieldId the id of the Field to fetch
	 * @return the Field with the given id
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public Field getField(Integer fieldId) throws APIException;
	
	/**
	 * Creates or updates the given Field
	 * 
	 * @param field the Field to save
	 * @return the Field that was saved
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public Field saveField(Field field) throws APIException;
	
	/**
	 * @deprecated use {@link #saveField(Field)}
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void createField(Field field) throws APIException;
	
	/**
	 * @deprecated use {@link #saveField(Field)}
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void updateField(Field field) throws APIException;
	
	/**
	 * Completely removes a Field from the database. Not reversible.
	 * 
	 * @param field the Field to purge
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void purgeField(Field field) throws APIException;
	
	/**
	 * Completely removes a Field from the database. Not reversible. !! WARNING: calling this with
	 * cascade=true can be very destructive !!
	 * 
	 * @param field the Field to purge
	 * @param cascade whether to cascade delete all FormFields pointing to this field
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void purgeField(Field field, boolean cascade) throws APIException;
	
	/**
	 * @deprecated use {@link #purgeField(Field)}
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void deleteField(Field field) throws APIException;
	
	/**
	 * Gets a FormField by internal database id
	 * 
	 * @param fieldId the internal id to search on
	 * @return the FormField with the given id
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public FormField getFormField(Integer formFieldId) throws APIException;
	
	/**
	 * Finds the FormField defined for this form/concept combination Calls
	 * {@link #getFormField(Form, Concept, Collection)} with an empty ignore list and with
	 * <code>force</code> set to false
	 * 
	 * @param form Form that this concept was found on
	 * @param concept (question) on this form that is being requested
	 * @return Formfield for this concept on this form
	 * @throws APIException
	 * @see {@link #getFormField(Form, Concept, Collection)}
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public FormField getFormField(Form form, Concept concept) throws APIException;
	
	/**
	 * Finds the FormField defined for this form/concept combination while discounting any form
	 * field found in the <code>ignoreFormFields</code> collection This method was added when
	 * needing to relate observations to form fields during a display. The use case would be that
	 * you know a Concept for a obs, which was defined on a form (via a formField). You can relate
	 * the formFields to Concepts easily enough, but if a Form reuses a Concept in two separate
	 * FormFields you don't want to only associate that first formField with that concept. So, keep
	 * a running list of formFields you've seen and pass them back in here to rule them out.
	 * 
	 * @param form Form that this concept was found on
	 * @param concept Concept (question) on this form that is being requested
	 * @param ignoreFormFields FormFields to ignore (aka already seen formfields)
	 * @param force if true and there are zero matches because all formFields were ignored (because
	 *            of ignoreFormFields) than the first result is returned
	 * @return Formfield for this concept on this form
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public FormField getFormField(Form form, Concept concept, Collection<FormField> ignoreFormFields, boolean force)
	                                                                                                                throws APIException;
	
	/**
	 * Creates or updates the given FormField
	 * 
	 * @param formField the FormField to save
	 * @return the formField that was just saved
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public FormField saveFormField(FormField formField) throws APIException;
	
	/**
	 * @deprecated use {@link #saveFormField(FormField)}
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void createFormField(FormField formField) throws APIException;
	
	/**
	 * @deprecated use {@link #saveFormField(FormField)}
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void updateFormField(FormField formField) throws APIException;
	
	/**
	 * Completely removes the given FormField from the database. This is not reversible
	 * 
	 * @param formField the FormField to purge
	 * @return the FormField that was purged
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void purgeFormField(FormField formField) throws APIException;
	
	/**
	 * @deprecated use {@link #purgeFormField(FormField)}
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public void deleteFormField(FormField formField) throws APIException;
	
	/**
	 * @deprecated use
	 *             {@link #getForms(String, Boolean, Collection, Boolean, Collection, Collection)}
	 * @see #getForms(String, Boolean, Collection, Boolean, Collection, Collection)
	 */
	@Transactional(readOnly = true)
	@Authorized(OpenmrsConstants.PRIV_VIEW_FORMS)
	public List<Form> findForms(String text, boolean includeUnpublished, boolean includeRetired);
	
	/**
	 * Retires field
	 * 
	 * @param field the Field to retire
	 * @return the Field that was retired
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public Field retireField(Field field) throws APIException;
	
	/**
	 * Unretires field
	 * 
	 * @param field the Field to unretire
	 * @return the Field that was unretired
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FORMS)
	public Field unretireField(Field field) throws APIException;
	
	/**
	 * Saves the given field type to the database
	 * 
	 * @param fieldType the field type to save
	 * @return the saved field type
	 * @throws APIException
	 * @should create new field type
	 * @should update existing field type
	 */
	@Authorized(OpenmrsConstants.PRIV_MANAGE_FIELD_TYPES)
	public FieldType saveFieldType(FieldType fieldType) throws APIException;
	
	/**
	 * Deletes the given field type from the database. This should not be done. It is preferred to
	 * just retired this field type with #retireFieldType(FieldType)
	 * 
	 * @param fieldType the field type to purge
	 * @throws APIException
	 */
	@Authorized(OpenmrsConstants.PRIV_PURGE_FIELD_TYPES)
	public void purgeFieldType(FieldType fieldType) throws APIException;
	
}
