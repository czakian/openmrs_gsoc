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
package org.openmrs;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.UserService;
import org.openmrs.util.OpenmrsUtil;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.load.Replace;

/**
 * A Person in the system. This can be either a small person stub, or indicative of an actual
 * Patient in the system. This class holds the generic person things that both the stubs
 * and patients share. Things like birthdate, names, addresses, and attributes are all generified
 * into the person table (and hence this super class)
 * 
 * @see org.openmrs.Patient
 */
@Root(strict = false)
public class Person extends BaseOpenmrsData implements java.io.Serializable {
	
	private transient static final Log log = LogFactory.getLog(Person.class);
	
	public static final long serialVersionUID = 2L;
	
	protected Integer personId;
	
	private Set<PersonAddress> addresses = null;
	
	private Set<PersonName> names = null;
	
	private Set<PersonAttribute> attributes = null;
	
	private String gender;
	
	private Date birthdate;
	
	private Boolean birthdateEstimated = false;
	
	private Boolean dead = false;
	
	private Date deathDate;
	
	private Concept causeOfDeath;
	
	private User personCreator;
	
	private Date personDateCreated;
	
	private User personChangedBy;
	
	private Date personDateChanged;
	
	private Boolean personVoided = false;
	
	private User personVoidedBy;
	
	private Date personDateVoided;
	
	private String personVoidReason;
	
	private boolean isPatient;
		
	/**
	 * Convenience map from PersonAttributeType.name to PersonAttribute.<br/>
	 * <br/>
	 * This is "cached" for each user upon first load. When an attribute is changed, the cache is
	 * cleared and rebuilt on next access.
	 */
	Map<String, PersonAttribute> attributeMap = null;
	
	/**
	 * default empty constructor
	 */
	public Person() {
	}
	
	/**
	 * This constructor is used to build a new Person object copy from another person object
	 * (usually a patient or a user subobject). All attributes are copied over to the new object.
	 * NOTE! All child collection objects are copied as pointers, each individual element is not
	 * copied. <br/>
	 * 
	 * @param person Person to create this person object from
	 */
	public Person(Person person) {
		if (person == null)
			return;
		
		personId = person.getPersonId();
		addresses = person.getAddresses();
		names = person.getNames();
		attributes = person.getAttributes();
		
		gender = person.getGender();
		birthdate = person.getBirthdate();
		birthdateEstimated = person.getBirthdateEstimated();
		dead = person.isDead();
		deathDate = person.getDeathDate();
		causeOfDeath = person.getCauseOfDeath();
		
		// base creator/voidedBy/changedBy info is not copied here
		// because that is specific to and will be recreated
		// by the subobject upon save
		
		setPersonCreator(person.getPersonCreator());
		setPersonDateCreated(person.getPersonDateCreated());
		setPersonChangedBy(person.getPersonChangedBy());
		setPersonDateChanged(person.getPersonDateChanged());
		setPersonVoided(person.isPersonVoided());
		setPersonVoidedBy(person.getPersonVoidedBy());
		setPersonDateVoided(person.getPersonDateVoided());
		setPersonVoidReason(person.getPersonVoidReason());
	}
	
	/**
	 * Default constructor taking in the primary key personId value
	 * 
	 * @param personId Integer internal id for this person
	 * @should set person id
	 */
	public Person(Integer personId) {
		this.personId = personId;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @should equal person with same person id
	 * @should not equal person with different person id
	 * @should not equal on null
	 * @should equal person objects with no person id
	 * @should not equal person objects when one has null person id
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Person) {
			Person person = (Person) obj;
			
			if (getPersonId() != null && person.getPersonId() != null)
				return personId.equals(person.getPersonId());
		}
		
		// if personId is null for either object, for equality the
		// two objects must be the same
		return this == obj;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 * @should have same hashcode when equal
	 * @should have different hash code when not equal
	 * @should get hash code with null attributes
	 */
	public int hashCode() {
		if (this.getPersonId() == null)
			return super.hashCode();
		return this.getPersonId().hashCode();
	}
	
	// Property accessors
	
	/**
	 * @return Returns the personId.
	 */
	@Attribute(required = true)
	public Integer getPersonId() {
		return personId;
	}
	
	/**
	 * @param personId The personId to set.
	 */
	@Attribute(required = true)
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
	/**
	 * @return person's gender
	 */
	@Attribute(required = false)
	public String getGender() {
		return this.gender;
	}
	
	/**
	 * @param gender person's gender
	 */
	@Attribute(required = false)
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	/**
	 * @return person's date of birth
	 */
	@Element(required = false)
	public Date getBirthdate() {
		return this.birthdate;
	}
	
	/**
	 * @param birthdate person's date of birth
	 */
	@Element(required = false)
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	/**
	 * @return true if person's birthdate is estimated
	 */
	public Boolean isBirthdateEstimated() {
		// if (this.birthdateEstimated == null) {
		// return new Boolean(false);
		// }
		return this.birthdateEstimated;
	}
	
	@Attribute(required = true)
	public Boolean getBirthdateEstimated() {
		return isBirthdateEstimated();
	}
	
	/**
	 * @param birthdateEstimated true if person's birthdate is estimated
	 */
	@Attribute(required = true)
	public void setBirthdateEstimated(Boolean birthdateEstimated) {
		this.birthdateEstimated = birthdateEstimated;
	}
	
	/**
	 * @return Returns the death status.
	 */
	public Boolean isDead() {
		return dead;
	}
	
	/**
	 * @return Returns the death status.
	 */
	@Attribute(required = true)
	public Boolean getDead() {
		return isDead();
	}
	
	/**
	 * @param dead The dead to set.
	 */
	@Attribute(required = true)
	public void setDead(Boolean dead) {
		this.dead = dead;
	}
	
	/**
	 * @return date of person's death
	 */
	@Element(required = false)
	public Date getDeathDate() {
		return this.deathDate;
	}
	
	/**
	 * @param deathDate date of person's death
	 */
	@Element(required = false)
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}
	
	/**
	 * @return cause of person's death
	 */
	@Element(required = false)
	public Concept getCauseOfDeath() {
		return this.causeOfDeath;
	}
	
	/**
	 * @param causeOfDeath cause of person's death
	 */
	@Element(required = false)
	public void setCauseOfDeath(Concept causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}
	
	/**
	 * @return list of known addresses for person
	 * @see org.openmrs.PersonAddress
	 * @should not get voided addresses
	 * @should not fail with null addresses
	 */
	@ElementList(required = false)
	public Set<PersonAddress> getAddresses() {
		if (addresses == null)
			addresses = new TreeSet<PersonAddress>();
		return this.addresses;
	}
	
	/**
	 * @param addresses Set<PersonAddress> list of known addresses for person
	 * @see org.openmrs.PersonAddress
	 */
	@ElementList(required = false)
	public void setAddresses(Set<PersonAddress> addresses) {
		this.addresses = addresses;
	}
	
	/**
	 * @return all known names for person
	 * @see org.openmrs.PersonName
	 * @should not get voided names
	 * @should not fail with null names
	 */
	@ElementList
	public Set<PersonName> getNames() {
		if (names == null)
			names = new TreeSet<PersonName>();
		return this.names;
	}
	
	/**
	 * @param names update all known names for person
	 * @see org.openmrs.PersonName
	 */
	@ElementList
	public void setNames(Set<PersonName> names) {
		this.names = names;
	}
	
	/**
	 * @return all known attributes for person
	 * @see org.openmrs.PersonAttribute
	 * @should not get voided attributes
	 * @should not fail with null attributes
	 */
	@ElementList
	public Set<PersonAttribute> getAttributes() {
		if (attributes == null)
			attributes = new TreeSet<PersonAttribute>();
		return this.attributes;
	}
	
	/**
	 * Returns only the non-voided attributes for this person
	 * 
	 * @return list attributes
	 * @should not get voided attributes
	 * @should not fail with null attributes
	 */
	public List<PersonAttribute> getActiveAttributes() {
		List<PersonAttribute> attrs = new Vector<PersonAttribute>();
		for (PersonAttribute attr : getAttributes()) {
			if (!attr.isVoided())
				attrs.add(attr);
		}
		return attrs;
	}
	
	/**
	 * @param attributes update all known attributes for person
	 * @see org.openmrs.PersonAttribute
	 */
	@ElementList
	public void setAttributes(Set<PersonAttribute> attributes) {
		this.attributes = attributes;
		attributeMap = null;
	}
	
	// Convenience methods
	
	/**
	 * Convenience method to add the <code>attribute</code> to this person's attribute list if the
	 * attribute doesn't exist already.<br/>
	 * <br/>
	 * Voids any current attribute with type = <code>newAttribute.getAttributeType()</code><br/>
	 * <br/>
	 * NOTE: This effectively limits persons to only one attribute of any given type **
	 * 
	 * @param newAttribute PersonAttribute to add to the Person
	 * @should fail when new attribute exist
	 * @should fail when new atribute are the same type with same value
	 * @should void old attribute when new attribute are the same type with different value
	 * @should remove attribute when old attribute are temporary
	 */
	public void addAttribute(PersonAttribute newAttribute) {
		newAttribute.setPerson(this);
		for (PersonAttribute currentAttribute : getActiveAttributes()) {
			if (currentAttribute.equals(newAttribute))
				return; // if we have the same PersonAttributeId, don't add the new attribute
			else if (currentAttribute.getAttributeType().equals(newAttribute.getAttributeType())) {
				if (currentAttribute.getValue() != null && currentAttribute.getValue().equals(newAttribute.getValue()))
					// this person already has this attribute
					return;
				
				// if the to-be-added attribute isn't already voided itself
				// and if we have the same type, different value
				if (newAttribute.isVoided() == false) {
					if (currentAttribute.getCreator() != null)
						currentAttribute.voidAttribute("New value: " + newAttribute.getValue());
					else
						// remove the attribute if it was just temporary (didn't have a creator
						// attached to it yet)
						removeAttribute(currentAttribute);
				}
			}
		}
		attributeMap = null;
		if (!OpenmrsUtil.collectionContains(attributes, newAttribute))
			attributes.add(newAttribute);
	}
	
	/**
	 * Convenience method to get the <code>attribute</code> from this person's attribute list if the
	 * attribute exists already.
	 * 
	 * @param attribute
	 * @should not fail when person attribute is null
	 * @should not fail when person attribute is not exist
	 * @should remove attribute when exist
	 */
	public void removeAttribute(PersonAttribute attribute) {
		if (attributes != null)
			if (attributes.remove(attribute))
				attributeMap = null;
	}
	
	/**
	 * Convenience Method to return the first non-voided person attribute matching a person
	 * attribute type
	 * 
	 * @param pat
	 * @return PersonAttribute
	 * @should not fail when attribute type is null
	 * @should not return voided attribute
	 * @should return null when given attribute type is not exist
	 */
	public PersonAttribute getAttribute(PersonAttributeType pat) {
		if (pat != null)
			for (PersonAttribute attribute : getAttributes()) {
				if (pat.equals(attribute.getAttributeType()) && !attribute.isVoided()) {
					return attribute;
				}
			}
		return null;
	}
	
	/**
	 * Convenience method to get this person's first attribute that has a PersonAttributeType.name
	 * equal to <code>attributeName</code>.
	 * 
	 * @param attributeName
	 */
	public PersonAttribute getAttribute(String attributeName) {
		if (attributeName != null)
			for (PersonAttribute attribute : getAttributes()) {
				PersonAttributeType type = attribute.getAttributeType();
				if (type != null && attributeName.equals(type.getName()) && !attribute.isVoided()) {
					return attribute;
				}
			}
		
		return null;
	}
	
	/**
	 * Convenience method to get this person's first attribute that has a PersonAttributeTypeId
	 * equal to <code>attributeTypeId</code>.
	 * 
	 * @param attributeTypeId
	 */
	public PersonAttribute getAttribute(Integer attributeTypeId) {
		for (PersonAttribute attribute : getActiveAttributes()) {
			if (attributeTypeId.equals(attribute.getAttributeType().getPersonAttributeTypeId()) && !attribute.isVoided()) {
				return attribute;
			}
		}
		return null;
	}
	
	/**
	 * Convenience method< to get all of this person's attributes that have a
	 * PersonAttributeType.name equal to <code>attributeName</code>.
	 * 
	 * @param attributeName
	 */
	public List<PersonAttribute> getAttributes(String attributeName) {
		List<PersonAttribute> ret = new Vector<PersonAttribute>();
		
		for (PersonAttribute attribute : getActiveAttributes()) {
			PersonAttributeType type = attribute.getAttributeType();
			if (type != null && attributeName.equals(type.getName()) && !attribute.isVoided()) {
				ret.add(attribute);
			}
		}
		
		return ret;
	}
	
	/**
	 * Convenience method to get all of this person's attributes that have a PersonAttributeType.id
	 * equal to <code>attributeTypeId</code>.
	 * 
	 * @param attributeTypeId
	 */
	public List<PersonAttribute> getAttributes(Integer attributeTypeId) {
		List<PersonAttribute> ret = new Vector<PersonAttribute>();
		
		for (PersonAttribute attribute : getActiveAttributes()) {
			if (attributeTypeId.equals(attribute.getAttributeType().getPersonAttributeTypeId()) && !attribute.isVoided()) {
				ret.add(attribute);
			}
		}
		
		return ret;
	}
	
	/**
	 * Convenience method to get all of this person's attributes that have a PersonAttributeType
	 * equal to <code>personAttributeType</code>.
	 * 
	 * @param personAttributeType
	 */
	public List<PersonAttribute> getAttributes(PersonAttributeType personAttributeType) {
		List<PersonAttribute> ret = new Vector<PersonAttribute>();
		for (PersonAttribute attribute : getAttributes()) {
			if (personAttributeType.equals(attribute.getAttributeType()) && !attribute.isVoided()) {
				ret.add(attribute);
			}
		}
		return ret;
	}
	
	/**
	 * Convenience method to get all of this person's attributes in map form: <String,
	 * PersonAttribute>.
	 */
	public Map<String, PersonAttribute> getAttributeMap() {
		if (attributeMap != null)
			return attributeMap;
		
		if (log.isDebugEnabled())
			log.debug("Current Person Attributes: \n" + printAttributes());
		
		attributeMap = new HashMap<String, PersonAttribute>();
		for (PersonAttribute attribute : getActiveAttributes()) {
			attributeMap.put(attribute.getAttributeType().getName(), attribute);
		}
		
		return attributeMap;
	}
	
	/**
	 * Convenience method for viewing all of the person's current attributes
	 * 
	 * @return Returns a string with all the attributes
	 */
	public String printAttributes() {
		String s = "";
		
		for (PersonAttribute attribute : getAttributes()) {
			s += attribute.getAttributeType() + " : " + attribute.getValue() + " : voided? " + attribute.isVoided() + "\n";
		}
		
		return s;
	}
	
	/**
	 * Convenience method to add the <code>name</code> to this person's name list if the name
	 * doesn't exist already.
	 * 
	 * @param name
	 */
	public void addName(PersonName name) {
		if (name != null) {
			name.setPerson(this);
			if (names == null)
				names = new TreeSet<PersonName>();
			if (!OpenmrsUtil.collectionContains(names, name))
				names.add(name);
		}
	}
	
	/**
	 * Convenience method remove the <code>name</code> from this person's name list if the name
	 * exists already.
	 * 
	 * @param name
	 */
	public void removeName(PersonName name) {
		if (names != null)
			names.remove(name);
	}
	
	/**
	 * Convenience method to add the <code>address</code> to this person's address list if the
	 * address doesn't exist already.
	 * 
	 * @param address
	 */
	public void addAddress(PersonAddress address) {
		if (address != null) {
			address.setPerson(this);
			if (addresses == null)
				addresses = new TreeSet<PersonAddress>();
			if (!OpenmrsUtil.collectionContains(addresses, address))
				addresses.add(address);
		}
	}
	
	/**
	 * Convenience method to remove the <code>address</code> from this person's address list if the
	 * address exists already.
	 * 
	 * @param address
	 */
	public void removeAddress(PersonAddress address) {
		if (addresses != null)
			addresses.remove(address);
	}
	
	/**
	 * Convenience method to get the "preferred" name for the person. Returns a blank PersonName
	 * object if no names are given.
	 * 
	 * @return Returns the "preferred" person name.
	 */
	public PersonName getPersonName() {
		// normally the DAO layer returns these in the correct order, i.e. preferred and non-voided first, but it's possible that someone
		// has fetched a Person, changed their names around, and then calls this method, so we have to be careful.
		if (getNames() != null && getNames().size() > 0) {
			for (PersonName name : getNames()) {
				if (name.isPreferred() && !name.isVoided())
					return name;
			}
			for (PersonName name : getNames()) {
				if (!name.isVoided())
					return name;
			}
			return null;
		}
		return null;
	}
	
	/**
	 * Convenience method to get the given name attribute on this person's preferred PersonName
	 * 
	 * @return String given name of the person
	 */
	public String getGivenName() {
		PersonName personName = getPersonName();
		if (personName == null)
			return "";
		else
			return personName.getGivenName();
	}
	
	/**
	 * Convenience method to get the middle name attribute on this person's preferred PersonName
	 * 
	 * @return String middle name of the person
	 */
	public String getMiddleName() {
		PersonName personName = getPersonName();
		if (personName == null)
			return "";
		else
			return personName.getMiddleName();
	}
	
	/**
	 * Convenience method to get the family name attribute on this person's preferred PersonName
	 * 
	 * @return String family name of the person
	 */
	public String getFamilyName() {
		PersonName personName = getPersonName();
		if (personName == null)
			return "";
		else
			return personName.getFamilyName();
	}
	
	/**
	 * Convenience Method to get the "preferred" address for person.
	 * 
	 * @return Returns the "preferred" person address.
	 */
	public PersonAddress getPersonAddress() {
		// normally the DAO layer returns these in the correct order, i.e. preferred and non-voided first, but it's possible that someone
		// has fetched a Person, changed their addresses around, and then calls this method, so we have to be careful.
		if (getAddresses() != null && getAddresses().size() > 0) {
			for (PersonAddress addr : getAddresses()) {
				if (addr.isPreferred() && !addr.isVoided())
					return addr;
			}
			for (PersonAddress addr : getAddresses()) {
				if (!addr.isVoided())
					return addr;
			}
			return null;
		}
		return null;
	}
	
	/**
	 * Convenience method to calculate this person's age based on the birthdate
	 * For a person who lived 1990 to 2000, age would be -5 in 1985, 5 in 1995, 10 in 2000, and 10 2010.
     *
	 * @return Returns age as an Integer.
     * @should get correct age after death
	 */
	public Integer getAge() {
		return getAge(null);
	}
	
	/**
	 * Convenience method: calculates the person's age on a given date based on the birthdate
	 *
	 * @param onDate (null defaults to today)
	 * @return int value of the person's age
	 * @should get age before birthday
	 * @should get age on birthday with no minutes defined
	 * @should get age on birthday with minutes defined
	 * @should get age after birthday
	 * @should get age after death
	 * @should get age with given date after death
	 * @should get age with given date before death
	 * @should get age with given date before birth
	 */
	public Integer getAge(Date onDate) {
		if (birthdate == null)
			return null;

        // Use default end date as today.
        Calendar today = Calendar.getInstance();
        // But if given, use the given date.
        if (onDate != null)
			today.setTime(onDate);

        // If date given is after date of death then use date of death as end date
        if(getDeathDate() != null && today.getTime().after(getDeathDate())) {
            today.setTime(getDeathDate());
        }

        Calendar bday = Calendar.getInstance();
		bday.setTime(birthdate);
		
		int age = today.get(Calendar.YEAR) - bday.get(Calendar.YEAR);
		
		// Adjust age when today's date is before the person's birthday
		int todaysMonth = today.get(Calendar.MONTH);
		int bdayMonth = bday.get(Calendar.MONTH);
		int todaysDay = today.get(Calendar.DAY_OF_MONTH);
		int bdayDay = bday.get(Calendar.DAY_OF_MONTH);
		
		if (todaysMonth < bdayMonth) {
			age--;
		} else if (todaysMonth == bdayMonth && todaysDay < bdayDay) {
			// we're only comparing on month and day, not minutes, etc
			age--;
		}
		
		return age;
	}
	
	/**
	 * Convenience method: sets a person's birth date from an age as of the given date Also sets
	 * flag indicating that the birth date is inexact. This sets the person's birth date to January
	 * 1 of the year that matches this age and date
	 * 
	 * @param age (the age to set)
	 * @param ageOnDate (null defaults to today)
	 */
	public void setBirthdateFromAge(int age, Date ageOnDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(ageOnDate == null ? new Date() : ageOnDate);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.add(Calendar.YEAR, -1 * age);
		setBirthdate(c.getTime());
		setBirthdateEstimated(true);
	}
	
	public User getPersonChangedBy() {
		return personChangedBy;
	}
	
	public void setPersonChangedBy(User changedBy) {
		this.personChangedBy = changedBy;
	}
	
	public Date getPersonDateChanged() {
		return personDateChanged;
	}
	
	public void setPersonDateChanged(Date dateChanged) {
		this.personDateChanged = dateChanged;
	}
	
	public User getPersonCreator() {
		return personCreator;
	}
	
	public void setPersonCreator(User creator) {
		this.personCreator = creator;
	}
	
	public Date getPersonDateCreated() {
		return personDateCreated;
	}
	
	public void setPersonDateCreated(Date dateCreated) {
		this.personDateCreated = dateCreated;
	}
	
	public Date getPersonDateVoided() {
		return personDateVoided;
	}
	
	public void setPersonDateVoided(Date dateVoided) {
		this.personDateVoided = dateVoided;
	}
	
	public void setPersonVoided(Boolean voided) {
		this.personVoided = voided;
	}
	
	public Boolean getPersonVoided() {
		return isPersonVoided();
	}
	
	public Boolean isPersonVoided() {
		return personVoided;
	}
	
	public User getPersonVoidedBy() {
		return personVoidedBy;
	}
	
	public void setPersonVoidedBy(User voidedBy) {
		this.personVoidedBy = voidedBy;
	}
	
	public String getPersonVoidReason() {
		return personVoidReason;
	}
	
	public void setPersonVoidReason(String voidReason) {
		this.personVoidReason = voidReason;
	}
	
	/**
	 * @return true/false whether this person is a patient or not
	 */
	public boolean isPatient() {
		return isPatient;
	}
	
	/**
	 * This should only be set by the database layer by looking at whether a row exists in the
	 * patient table
	 * 
	 * @param isPatient whether this person is a patient or not
	 */
	@SuppressWarnings("unused")
	private void setPatient(boolean isPatient) {
		this.isPatient = isPatient;
	}
	
	/**
	 * @return true/false whether this person is a user or not
	 * @deprecated use {@link UserService#getUsersByPerson(Person, boolean)}
	 */
	public boolean isUser() {
		return false;
	}
		
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Person(personId=" + personId + ")";
	}
	
	/**
	 * If the serializer wishes, don't serialize this entire object, just the important parts
	 * 
	 * @param sessionMap serialization session information
	 * @return Person object to serialize
	 * @see OpenmrsUtil#isShortSerialization(Map)
	 */
	@Replace
	public Person replaceSerialization(Map<?, ?> sessionMap) {
		if (OpenmrsUtil.isShortSerialization(sessionMap)) {
			// only serialize the person id
			return new Person(getPersonId());
		}
		
		// don't do short serialization
		return this;
	}
	
	/**
	 * @since 1.5
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	public Integer getId() {
		
		return getPersonId();
	}
	
	/**
	 * @since 1.5
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		setPersonId(id);
		
	}
	
}
