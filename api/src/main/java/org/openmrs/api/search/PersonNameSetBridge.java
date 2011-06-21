/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.api.search;

import org.hibernate.search.bridge.StringBridge;

/**
 *
 * @author czakian
 */
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;
import org.openmrs.PersonName;

public class PersonNameSetBridge implements TwoWayFieldBridge {
	
	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		
		if (value == null) {
			return;
		}
		
		// we expect a Set<String> here. checking for Set for simplicity
		if (!(value instanceof Set)) {
			throw new IllegalArgumentException("support limited to Set");
		}
		
		@SuppressWarnings("unchecked")
		Set<PersonName> set = (Set<PersonName>) value;
		
		for (PersonName n : set) {
			Field field = new Field(name, n.toString(), luceneOptions.getStore(), luceneOptions.getIndex(), luceneOptions
			        .getTermVector());
			field.setBoost(luceneOptions.getBoost());
			document.add(field);
		}
	}
	
	@Override
	public Object get(String name, Document document) {
		Field[] fields = document.getFields(name);
		Set<String> set = new HashSet<String>();
		for (Field field : fields) {
			System.out.println("adding to " + document + " " + field.stringValue());
			set.add(field.stringValue());
		}
		return set;
	}
	
	@Override
	public String objectToString(Object value) {
		if (value == null) {
			return "";
		} else if (value instanceof String) {
			return (String) value;
		} else {
			return String.valueOf(value);
		}
	}
}
