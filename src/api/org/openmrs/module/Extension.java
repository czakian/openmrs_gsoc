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
package org.openmrs.module;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An extension is a small snippet of code that is run at certain "extension points" throughout the
 * user interface
 * <p>
 * An extension is not necessarily tied to only one certain point. If all of the need return values
 * are defined it can be used to extend any point. A module can contain many extensions for many
 * different points.
 */

public abstract class Extension {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	// point which this extension is extending
	private String pointId;
	
	// id of the module implementing this point
	private String moduleId;
	
	// parameters given at the extension point
	private Map<String, String> parameterMap;
	
	/**
	 * String separating the pointId and media type in an extension id
	 * 
	 * @see #toExtensionId(String, MEDIA_TYPE)
	 */
	public static final String extensionIdSeparator = "|";
	
	/**
	 * All media types allowed by the module extension system. If an extension specifies 'html' as
	 * its media type, it is assumed to mainly work just within html rendering environments. If an
	 * extension has a null media type, it should work for any visual/text rendering environment
	 */
	public enum MEDIA_TYPE {
		html
	}
	
	/**
	 * default constructor
	 */
	public Extension() {
	}
	
	/**
	 * Called before being displayed each time
	 * 
	 * @param parameterMap
	 */
	public void initialize(Map<String, String> parameterMap) {
		log.debug("Initializing extension for point: " + pointId);
		this.setPointId(pointId);
		this.setParameterMap(parameterMap);
	}
	
	/**
	 * Get the point id
	 * 
	 * @return the <code>String</code> Point Id
	 */
	public String getPointId() {
		return pointId;
	}
	
	/**
	 * Set the point id
	 * 
	 * @param pointId
	 */
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	
	/**
	 * Get all of the parameters given to this extension point
	 * 
	 * @return key-value parameter map
	 */
	public Map<String, String> getParameterMap() {
		return parameterMap;
	}
	
	/**
	 * Parameters given at the extension point This method is usually called only during extension
	 * initialization
	 * 
	 * @param parameterMap key-value parameter map
	 */
	public void setParameterMap(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}
	
	/**
	 * Sets the content type of this extension. If null is returned this extension should work
	 * across all medium types
	 * 
	 * @return type of the medium that this extension works for
	 */
	public abstract Extension.MEDIA_TYPE getMediaType();
	
	/**
	 * Get the extension point id
	 * 
	 * @return the <code>String</code> Extension Id
	 */
	public String getExtensionId() {
		return toExtensionId(getPointId(), getMediaType());
	}
	
	/**
	 * If this method returns a non-null value then the return value will be used as the default
	 * content for this extension at this extension point
	 * 
	 * @return override content
	 */
	public String getOverrideContent(String bodyContent) {
		return null;
	}
	
	/**
	 * Get this extension's module id
	 * 
	 * @return the <code>String</code> Module Id
	 */
	public final String getModuleId() {
		return moduleId;
	}
	
	/**
	 * Set the module id of this extension
	 * 
	 * @param moduleId
	 */
	public final void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	/**
	 * Get the string representation of this extension
	 * 
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
		return "Extension: " + this.getExtensionId();
	}
	
	/**
	 * Convert the given pointId and mediaType to an extensionId. The extension id is usually
	 * pointid|mediaType if mediatype is null, extension id is just point id
	 * 
	 * @param pointId
	 * @param mediaType
	 * @return string extension id
	 */
	public static final String toExtensionId(String pointId, MEDIA_TYPE mediaType) {
		if (mediaType != null)
			return new StringBuffer(pointId).append(Extension.extensionIdSeparator).append(mediaType).toString();
		else
			return pointId;
	}
}
