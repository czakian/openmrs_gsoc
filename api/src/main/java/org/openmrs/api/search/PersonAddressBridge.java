/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.api.search;

import java.lang.reflect.Field;
import java.util.Set;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 *
 * @author czakian
 */
public class PersonAddressBridge implements FieldBridge {
	
	private Class clazz;
	
	@Override
	public void set(String name, Object o, Document dcmnt, LuceneOptions lo) {
		Set set = (Set) o;
		clazz = o.getClass();
		Field[] fields = clazz.getFields();
		
		//add all the fields from the class. 
		for (Field f : fields) {}
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
