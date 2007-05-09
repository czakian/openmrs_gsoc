package org.openmrs.web.dwr;

public class EnteredField {

	private Integer fieldId;
	private String value;
	private String valueClass;
	private String dateTime;
	
	public EnteredField() { }
	
	public EnteredField(String s) {
		String[] temp = s.split("\\^");
		fieldId = Integer.valueOf(temp[0]);
		dateTime = temp[1];
		value = temp[2];
	}

	public boolean isEmpty() {
		return value == null || value.length() == 0;
	}
	
	public Double getValueAsDouble() {
		return value == null ? null : Double.valueOf(value);
	}
	
	public Integer getValueAsInteger() {
		return value == null ? null : Integer.valueOf(value);
	}
	
	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getFieldId() {
		return fieldId;
	}

	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueClass() {
		return valueClass;
	}

	public void setValueClass(String valueClass) {
		this.valueClass = valueClass;
	}
	
	
	
}
