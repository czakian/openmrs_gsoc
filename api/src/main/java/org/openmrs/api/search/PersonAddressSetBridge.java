package org.openmrs.api.search;

import java.lang.reflect.Field;
import java.util.Set;

import org.hibernate.search.bridge.StringBridge;
import org.openmrs.PersonAddress;

public class PersonAddressSetBridge implements StringBridge {
	
	// what about a null placeholder? mebbe, mebbe not. lets see how it goes. ;)
	@Override
	public String objectToString(Object value) {
		String result = null;
		Set set = (Set) value;
		for (Object addy : set.toArray()) {
			if (addy != null) {
				PersonAddress address = (PersonAddress) addy;
				Field[] fields = address.getClass().getFields();
				StringBuilder buf = new StringBuilder(1000);
				for (Field f : fields) {
					try {
						buf.append(" "); // space delimited for easy conversion
						// later
						buf.append(address.getClass().getField(f.toString()).toString());
					}
					catch (SecurityException e) {
						e.printStackTrace();
					}
					catch (NoSuchFieldException e) {
						e.printStackTrace();
					}
				}
				result = buf.toString();
			}
		}
		return result;
	}
}
