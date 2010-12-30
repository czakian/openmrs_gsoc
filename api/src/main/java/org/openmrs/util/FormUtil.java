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
package org.openmrs.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;

import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Drug;
import org.openmrs.Form;
import org.openmrs.FormField;
import org.openmrs.hl7.HL7Constants;

/**
 * OpenMRS utilities related to forms.
 * 
 * @see org.openmrs.Form
 * @see org.openmrs.FormField
 * @see org.openmrs.Field
 * @see org.openmrs.FieldType
 * @see org.openmrs.FieldAnswer
 */
public class FormUtil {
	
	/**
	 * Converts a string into a valid XML token (tag name)
	 * 
	 * @param s string to convert into XML token
	 * @return valid XML token based on s
	 */
	public static String getXmlToken(String s) {
		// Converts a string into a valid XML token (tag name)
		// No spaces, start with a letter or underscore, not 'xml*'
		
		// if len(s) < 1, return '_blank'
		if (s == null || s.length() < 1)
			return "_blank";
		
		// xml tokens must start with a letter
		String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_";
		
		// after the leading letter, xml tokens may have
		// digits, period, or hyphen
		String nameChars = letters + "0123456789.-";
		
		// special characters that should be replaced with valid text
		// all other invalid characters will be removed
		Hashtable<String, String> swapChars = new Hashtable<String, String>();
		swapChars.put("!", "bang");
		swapChars.put("#", "pound");
		swapChars.put("\\*", "star");
		swapChars.put("'", "apos");
		swapChars.put("\"", "quote");
		swapChars.put("%", "percent");
		swapChars.put("<", "lt");
		swapChars.put(">", "gt");
		swapChars.put("=", "eq");
		swapChars.put("/", "slash");
		swapChars.put("\\\\", "backslash");
		
		// start by cleaning whitespace and converting to lowercase
		s = s.replaceAll("^\\s+", "").replaceAll("\\s+$", "").replaceAll("\\s+", "_").toLowerCase();
		
		// swap characters
		Set<Entry<String, String>> swaps = swapChars.entrySet();
		for (Entry<String, String> entry : swaps) {
			if (entry.getValue() != null)
				s = s.replaceAll(entry.getKey(), "_" + entry.getValue() + "_");
			else
				s = s.replaceAll(String.valueOf(entry.getKey()), "");
		}
		
		// ensure that invalid characters and consecutive underscores are
		// removed
		String token = "";
		boolean underscoreFlag = false;
		for (int i = 0; i < s.length(); i++) {
			if (nameChars.indexOf(s.charAt(i)) != -1) {
				if (s.charAt(i) != '_' || !underscoreFlag) {
					token += s.charAt(i);
					underscoreFlag = (s.charAt(i) == '_');
				}
			}
		}
		
		// remove extraneous underscores before returning token
		token = token.replaceAll("_+", "_");
		token = token.replaceAll("_+$", "");
		
		// make sure token starts with valid letter
		if (letters.indexOf(token.charAt(0)) == -1 || token.startsWith("xml"))
			token = "_" + token;
		
		// return token
		return token;
	}
	
	/**
	 * Generates a new, unique tag name for any given string
	 * 
	 * @param s string to convert into a unique XML tag
	 * @param tagList java.util.Vector containing all previously created tags. If the tagList is
	 *            null, it will be initialized automatically
	 * @return unique XML tag name from given string (guaranteed not to duplicate any tag names
	 *         already within <code>tagList</code>)
	 */
	public static String getNewTag(String s, Vector<String> tagList) {
		String token = getXmlToken(s);
		if (tagList.contains(token)) {
			int i = 1;
			while (tagList.contains(token + "_" + i))
				i++;
			String tagName = token + "_" + i;
			tagList.add(tagName);
			return tagName;
		} else {
			tagList.add(token);
			return token;
		}
	}
	
	/**
	 * Returns a sorted and structured map of <code>FormField</code>s for the given OpenMRS form.
	 * The root sections of the schema are stored under a key of zero (i.e.,
	 * <code>java.lang.Integer.<em>valueOf(0)</em></code>). All other entries represent sequences of
	 * children stored under the identifier (<code>formField.<em>getFormFieldId()</em></code>) of
	 * their parent FormField. The form structure is sorted by the natural sorting order of the
	 * <code>FormField</code>s (as defined by the <em>.equals()</em> and <em>.compareTo()</em>
	 * methods).
	 * 
	 * @param form form for which structure is requested
	 * @return sorted map of <code>FormField</code>s, where the top-level fields are under the key
	 *         zero and all other leaves are stored under their parent <code>FormField</code>'s id.
	 */
	public static TreeMap<Integer, TreeSet<FormField>> getFormStructure(Form form) {
		TreeMap<Integer, TreeSet<FormField>> formStructure = new TreeMap<Integer, TreeSet<FormField>>();
		Integer base = Integer.valueOf(0);
		formStructure.put(base, new TreeSet<FormField>());
		
		for (FormField formField : form.getFormFields()) {
			FormField parent = formField.getParent();
			if (parent == null) {
				// top-level branches should be added to the base
				formStructure.get(base).add(formField);
			} else {
				// child branches/leaves are added to their parent's branch
				if (!formStructure.containsKey(parent.getFormFieldId()))
					formStructure.put(parent.getFormFieldId(), new TreeSet<FormField>());
				formStructure.get(parent.getFormFieldId()).add(formField);
			}
		}
		
		return formStructure;
	}
	
	public static String dateToString() {
		return dateToString(new Date());
	}
	
	private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	public static String dateToString(Date date) {
		String dateString = dateFormatter.format(new Date());
		// ISO 8601 requires a colon in time zone offset (Java doesn't
		// include the colon, so we need to insert it
		return dateString.substring(0, 22) + ":" + dateString.substring(22);
	}
	
	/**
	 * Get a string somewhat unique to this form. Combines the form's id and version and build
	 * 
	 * @param form Form to get the uri for
	 * @return String representing this form
	 */
	public static String getFormUriWithoutExtension(Form form) {
		return form.getFormId() + "-" + form.getVersion() + "-" + form.getBuild();
	}
	
	/**
	 * Turn the given concept into a string acceptable to for hl7 and forms
	 * 
	 * @param concept Concept to convert to a string
	 * @param locale Locale to use for the concept name
	 * @return String representation of the given concept
	 */
	public static String conceptToString(Concept concept, Locale locale) {
		ConceptName localizedName = concept.getBestName(locale);
		return conceptToString(concept, localizedName);
	}
	
	/**
	 * Turn the given concept/concept-name pair into a string acceptable for hl7 and forms
	 * 
	 * @param concept Concept to convert to a string
	 * @param localizedName specific localized concept-name
	 * @return String representation of the given concept
	 */
	public static String conceptToString(Concept concept, ConceptName localizedName) {
		return concept.getConceptId() + "^" + localizedName.getName() + "^" + HL7Constants.HL7_LOCAL_CONCEPT; // + "^"
		// + localizedName.getConceptNameId() + "^" + localizedName.getName() + "^" + FormConstants.HL7_LOCAL_CONCEPT_NAME;
	}
	
	/**
	 * Turn the given drug into a string acceptable for hl7 and forms
	 * 
	 * @param drug Drug to convert to a string
	 * @return String representation of the given drug
	 */
	public static String drugToString(Drug drug) {
		return drug.getDrugId() + "^" + drug.getName() + "^" + HL7Constants.HL7_LOCAL_DRUG;
	}
}
