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

import java.io.StringReader;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.util.OpenmrsConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * This class will parse an xml update.rdf file
 * 
 * @version 1.0
 */
public class UpdateFileParser {
	
	private static Log log = LogFactory.getLog(UpdateFileParser.class);
	
	private String content;
	
	// these properties store the 'best fit' (most recent update that will fit with the current code version)
	private String moduleId = null;
	
	private String currentVersion = null;
	
	private String downloadURL = null;
	
	/**
	 * Default constructor
	 * 
	 * @param s String to parse (Contents of update.rdf file)
	 */
	public UpdateFileParser(String s) {
		this.content = s;
	}
	
	/**
	 * Parse the contents of the update.rdf file.
	 * 
	 * @throws ModuleException
	 */
	public void parse() throws ModuleException {
		StringReader stringReader = null;
		try {
			Document updateDoc = null;
			try {
				stringReader = new StringReader(content);
				InputSource inputSource = new InputSource(stringReader);
				inputSource.setSystemId("./");
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				updateDoc = db.parse(inputSource);
			}
			catch (Exception e) {
				log.warn("Unable to parse content");
				throw new ModuleException("Error parsing update.rdf file: " + content, e);
			}
			
			Element rootNode = updateDoc.getDocumentElement();
			
			String configVersion = rootNode.getAttribute("configVersion");
			
			if (!validConfigVersions().contains(configVersion))
				throw new ModuleException("Invalid configVersion: '" + configVersion + "' found In content: " + content);
			
			if ("1.0".equals(configVersion)) {
				// the only update in the xml file is the 'best fit'
				this.moduleId = getElement(rootNode, configVersion, "moduleId");
				this.currentVersion = getElement(rootNode, configVersion, "currentVersion");
				this.downloadURL = getElement(rootNode, configVersion, "downloadURL");
			} else if ("1.1".equals(configVersion)) {
				
				this.moduleId = rootNode.getAttribute("moduleId");
				
				NodeList nodes = rootNode.getElementsByTagName("update");
				this.currentVersion = ""; // default to the lowest version possible
				
				// loop over all 'update' tags
				for (Integer i = 0; i < nodes.getLength(); i++) {
					Element currentNode = (Element) nodes.item(i);
					String currentVersion = getElement(currentNode, configVersion, "currentVersion");
					// if the currently saved version is less than the current tag
					if (ModuleUtil.compareVersion(this.currentVersion, currentVersion) < 0) {
						String requireOpenMRSVersion = getElement(currentNode, configVersion, "requireOpenMRSVersion");
						// if the openmrs code version is compatible, this node is a winner
						if (requireOpenMRSVersion == null
						        || ModuleUtil.matchRequiredVersions(OpenmrsConstants.OPENMRS_VERSION_SHORT,
						            requireOpenMRSVersion)) {
							this.currentVersion = currentVersion;
							this.downloadURL = getElement(currentNode, configVersion, "downloadURL");
						}
					}
				}
			}
		}
		catch (ModuleException e) {
			// rethrow the moduleException
			throw e;
		}
		finally {
			if (stringReader != null)
				stringReader.close();
		}
		
	}
	
	/**
	 * Generic method to get a module tag
	 * 
	 * @param element
	 * @param version
	 * @param tag
	 * @return
	 */
	private static String getElement(Element element, String version, String tag) {
		if (element.getElementsByTagName(tag).getLength() > 0)
			return element.getElementsByTagName(tag).item(0).getTextContent();
		return "";
	}
	
	/**
	 * List of the valid sqldiff versions
	 * 
	 * @return
	 */
	private static List<String> validConfigVersions() {
		List<String> versions = new Vector<String>();
		versions.add("1.0");
		versions.add("1.1");
		return versions;
	}
	
	/**
	 * @return the downloadURL
	 */
	public String getDownloadURL() {
		return downloadURL;
	}
	
	/**
	 * @return the moduleId
	 */
	public String getModuleId() {
		return moduleId;
	}
	
	/**
	 * @return the version
	 */
	public String getCurrentVersion() {
		return currentVersion;
	}
	
}
