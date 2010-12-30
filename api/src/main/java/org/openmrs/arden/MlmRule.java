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
package org.openmrs.arden;

import org.openmrs.logic.Rule;

/**
 * A medical logic module (MLM) is an independent unit in a health knowledge base that combines the
 * knowledge required and the definition of the way it should be applied for a single health
 * decision (from: wikipedia). Currently the way to create an MlmRule is using Arden syntax and then
 * transform them to Rule object using the <code>ArdenService</code>.
 * 
 * @since 1.8
 */
public interface MlmRule extends Rule {
	
	/**
	 * Returns the priority value of the rule. This value determines in what order the rules get
	 * evaluated.
	 * 
	 * @return Integer priority value
	 */
	public Integer getPriority();
	
	/**
	 * Returns a descriptive title for the rule.
	 * 
	 * @return String title for the rule
	 */
	public String getTitle();
	
	/**
	 * Returns the version of the rule.
	 * 
	 * @return Double version of the rule
	 */
	public Double getVersion();
	
	/**
	 * Returns the institution that authored this rule
	 * 
	 * @return String institution that authored this rule
	 */
	public String getInstitution();
	
	/**
	 * Returns the name of the person that authored this rule
	 * 
	 * @return String name of the person that authored this rule
	 */
	public String getAuthor();
	
	/**
	 * Returns the name of the specialist this rule applies to
	 * 
	 * @return String name of the specialist this rule applies to
	 */
	public String getSpecialist();
	
	/**
	 * Returns the date this rule was created
	 * 
	 * @return String date this rule was created
	 */
	public String getDate();
	
	/**
	 * Returns the purpose of this rule
	 * 
	 * @return String purpose of this rule
	 */
	public String getPurpose();
	
	/**
	 * Returns an explanation of why this rule is needed
	 * 
	 * @return String explanation of why this rule is needed
	 */
	public String getExplanation();
	
	/**
	 * Returns keywords that can be used to classify the rule
	 * 
	 * @return String keywords that can be used to classify the rule
	 */
	public String getKeywords();
	
	/**
	 * Returns citations that can be used as reference for this rule
	 * 
	 * @return String citations that can be used as reference for this rule
	 */
	public String getCitations();
	
	/**
	 * Returns urls that can be used as reference for this rule
	 * 
	 * @return String urls that can be used as reference for this rule
	 */
	public String getLinks();
	
	/**
	 * Returns the portion of the rule logic that pulls the data to run the rule
	 * 
	 * @return String portion of the rule logic that pulls the data to run the rule
	 */
	public String getData();
	
	/**
	 * Returns the logic that is applied to the data to decide what action should be taken
	 * 
	 * @return String the logic that is applied to the data to decide what action should be taken
	 */
	public String getLogic();
	
	/**
	 * Returns the action that should be taken when the logic evaluates to true
	 * 
	 * @return String action that should be taken when the logic evaluates to true
	 */
	public String getAction();
	
	/**
	 * Returns the type of the rule. This type is used along with the priority to evaluate
	 * prioritized rule sets
	 * 
	 * @return String type of the rule
	 */
	public String getType();
	
	public Integer getAgeMin();
	
	public Integer getAgeMax();
	
	public String getAgeMinUnits();
	
	public String getAgeMaxUnits();
	
}
