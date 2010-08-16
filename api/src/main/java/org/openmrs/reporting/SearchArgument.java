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
package org.openmrs.reporting;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * This class represents an argument as might be submitted from a web user interface.
 * 
 * @deprecated see reportingcompatibility module
 */
@Root
@Deprecated
public class SearchArgument {
	
	private String name;
	
	private String value;
	
	@SuppressWarnings("unchecked")
	private Class propertyClass;
	
	public SearchArgument() {
	}
	
	@SuppressWarnings("unchecked")
	public SearchArgument(String name, String value, Class propertyClass) {
		super();
		this.name = name;
		this.value = value;
		this.propertyClass = propertyClass;
	}
	
	public String toString() {
		return name + " (" + propertyClass + ") = " + value;
	}
	
	@Attribute(required = true)
	public String getName() {
		return name;
	}
	
	@Attribute(required = true)
	public void setName(String name) {
		this.name = name;
	}
	
	@Attribute(required = true)
	public String getValue() {
		return value;
	}
	
	@Attribute(required = true)
	public void setValue(String value) {
		this.value = value;
	}
	
	@SuppressWarnings("unchecked")
	@Attribute(required = true)
	public Class getPropertyClass() {
		return propertyClass;
	}
	
	@SuppressWarnings("unchecked")
	@Attribute(required = true)
	public void setPropertyClass(Class propertyClass) {
		this.propertyClass = propertyClass;
	}
	
}
