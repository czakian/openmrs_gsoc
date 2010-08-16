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
package org.openmrs.web.dwr;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.FormField;

public class FormFieldListItem {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private Integer formFieldId;
	
	private Integer parent;
	
	private Integer fieldNumber;
	
	private String fieldPart = "";
	
	private Integer pageNumber;
	
	private Integer minOccurs;
	
	private Integer maxOccurs;
	
	private String required = "";
	
	private String creator = "";
	
	private String changedBy = "";
	
	private FieldListItem field = null;
	
	public FormFieldListItem() {
	}
	
	public FormFieldListItem(FormField ff, Locale locale) {
		
		if (ff != null) {
			formFieldId = ff.getFormFieldId();
			if (ff.getParent() != null)
				parent = ff.getParent().getFormFieldId();
			field = new FieldListItem(ff.getField(), locale);
			fieldNumber = ff.getFieldNumber();
			fieldPart = ff.getFieldPart();
			pageNumber = ff.getPageNumber();
			minOccurs = ff.getMinOccurs();
			maxOccurs = ff.getMaxOccurs();
			required = ff.isRequired() == true ? "yes" : "no";
			if (ff.getCreator() != null)
				creator = ff.getCreator().getPersonName().toString();
			if (ff.getChangedBy() != null)
				changedBy = ff.getChangedBy().getPersonName().toString();
		}
	}
	
	public String getChangedBy() {
		return changedBy;
	}
	
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public void setCreator(String c) {
		this.creator = c;
	}
	
	public FieldListItem getField() {
		return field;
	}
	
	public void setField(FieldListItem field) {
		this.field = field;
	}
	
	public String getFieldPart() {
		return fieldPart;
	}
	
	public void setFieldPart(String fieldPart) {
		this.fieldPart = fieldPart;
	}
	
	public Integer getFormFieldId() {
		return formFieldId;
	}
	
	public void setFormFieldId(Integer formFieldId) {
		this.formFieldId = formFieldId;
	}
	
	public Integer getFieldNumber() {
		return fieldNumber;
	}
	
	public void setFieldNumber(Integer fieldNumber) {
		this.fieldNumber = fieldNumber;
	}
	
	public Integer getMaxOccurs() {
		return maxOccurs;
	}
	
	public void setMaxOccurs(Integer maxOccurs) {
		this.maxOccurs = maxOccurs;
	}
	
	public Integer getMinOccurs() {
		return minOccurs;
	}
	
	public void setMinOccurs(Integer minOccurs) {
		this.minOccurs = minOccurs;
	}
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public Integer getParent() {
		return parent;
	}
	
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	
	public String getRequired() {
		return required;
	}
	
	public void setRequired(String required) {
		this.required = required;
	}
	
}
