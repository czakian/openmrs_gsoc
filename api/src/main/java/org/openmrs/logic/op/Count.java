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
package org.openmrs.logic.op;

/**
 * The Count operator will return the number of results returned by the logic service<br />
 * <br />
 * Example: <br />
 * - <code>logicService.parse("EncounterDataSource.ENCOUNTER_KEY").count();</code><br />
 * The above will give us a criteria to get the number of encounter type we have in the system
 */
public class Count implements TransformOperator {
	
	public String toString() {
		return "Count";
	}
	
}
