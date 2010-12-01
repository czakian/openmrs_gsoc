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
package org.openmrs.api;

import java.util.List;

import org.openmrs.Person;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.annotation.Authorized;
import org.openmrs.annotation.Logging;
import org.openmrs.util.PersonByNameComparator;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains methods pertaining to Users in the system Use:<br/>
 * 
 * <pre>
 * 
 * 
 * List&lt;User&gt; users = Context.getUserService().getAllUsers();
 * </pre>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface UserService extends OpenmrsService {
	
	/**
	 * Saves a user to the database.
	 * 
	 * @param user
	 * @param password
	 * @return a User object
	 * @throws APIException
	 * @should create new user with basic elements
	 * @should should create user who is patient already
	 * @should update users username
	 * @should grant new roles in roles list to user
	 * @should fail to create the user with a weak password
	 */
	@Authorized({ PrivilegeConstants.ADD_USERS, PrivilegeConstants.EDIT_USERS })
	@Logging(ignoredArgumentIndexes = { 1 })
	public User saveUser(User user, String password) throws APIException;
	
	/**
	 * @see #saveUser(User, String)
	 * @deprecated replaced by {@link #saveUser(User, String)}
	 */
	@Deprecated
	@Authorized({ PrivilegeConstants.ADD_USERS })
	@Logging(ignoredArgumentIndexes = { 1 })
	public User createUser(User user, String password) throws APIException;
	
	/**
	 * Get user by internal user identifier.
	 * 
	 * @param userId internal identifier
	 * @return requested user
	 * @throws APIException
	 * @should fetch user with given userId
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public User getUser(Integer userId) throws APIException;
	
	/**
	 * Get user by the given uuid.
	 * 
	 * @param uuid
	 * @return
	 * @throws APIException
	 * @should fetch user with given uuid
	 * @should find object given valid uuid
	 * @should return null if no object found with given uuid
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public User getUserByUuid(String uuid) throws APIException;
	
	/**
	 * Get user by username (user's login identifier)
	 * 
	 * @param username user's identifier used for authentication
	 * @return requested user
	 * @throws APIException
	 * @should get user by username
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public User getUserByUsername(String username) throws APIException;
	
	/**
	 * true/false if username or systemId is already in db in username or system_id columns
	 * 
	 * @param user User to compare
	 * @return boolean
	 * @throws APIException
	 * @should verify that username and system id is unique
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public boolean hasDuplicateUsername(User user) throws APIException;
	
	/**
	 * Get users by role granted
	 * 
	 * @param role Role that the Users must have to be returned
	 * @return users with requested role
	 * @throws APIException
	 * @should fetch users assigned given role
	 * @should not fetch user that does not belong to given role
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public List<User> getUsersByRole(Role role) throws APIException;
	
	/**
	 * Save changes to given <code>user</code> to the database.
	 * 
	 * @param user
	 * @throws APIException
	 * @see #saveUser(User, String)
	 * @deprecated replaced by {@link #saveUser(User, String)}
	 */
	@Deprecated
	@Authorized({ PrivilegeConstants.EDIT_USERS })
	public void updateUser(User user) throws APIException;
	
	/**
	 * Use should be UserService.saveUser(user.addRole(role))
	 * 
	 * @deprecated use {@link org.openmrs.User#addRole(Role)}
	 */
	@Deprecated
	@Authorized({ PrivilegeConstants.EDIT_USERS })
	public void grantUserRole(User user, Role role) throws APIException;
	
	/**
	 * Use UserService.saveUser(user.removeRole(role))
	 * 
	 * @deprecated use {@link org.openmrs.User#removeRole(Role)}
	 */
	@Deprecated
	@Authorized({ PrivilegeConstants.EDIT_USERS })
	public void revokeUserRole(User user, Role role) throws APIException;
	
	/**
	 * Mark user as voided (effectively deleting user without removing their data &mdash; since
	 * anything the user touched in the database will still have their internal identifier and point
	 * to the voided user for historical tracking purposes.
	 * 
	 * @param user
	 * @param reason
	 * @return the given user voided out
	 * @throws APIException
	 * @should void user and set attributes
	 * @deprecated use {@link #retireUser(User, String)}
	 */
	@Deprecated
	@Authorized({ PrivilegeConstants.EDIT_USERS })
	public User voidUser(User user, String reason) throws APIException;
	
	/**
	 * Clear voided flag for user (equivalent to an "undelete" or Lazarus Effect for user)
	 * 
	 * @param user
	 * @return the given user unvoided
	 * @throws APIException
	 * @should unvoid and unmark all attributes
	 * @deprecated use {@link #unretireUser(User)}
	 */
	@Deprecated
	@Authorized({ PrivilegeConstants.EDIT_USERS })
	public User unvoidUser(User user) throws APIException;
	
	/**
	 * Deactive a user account so that it can no longer log in.
	 * 
	 * @param user
	 * @param reason
	 * @throws APIException
	 * @should retire user and set attributes
	 */
	@Authorized({ PrivilegeConstants.EDIT_USERS })
	public User retireUser(User user, String reason) throws APIException;
	
	/**
	 * Clears retired flag for a user.
	 * 
	 * @param user
	 * @param reason
	 * @throws APIException
	 * @should unretire and unmark all attributes
	 */
	@Authorized({ PrivilegeConstants.EDIT_USERS })
	public User unretireUser(User user) throws APIException;
	
	/**
	 * @see #voidUser(User, String)
	 * @see #purgeUser(User)
	 * @deprecated use {@link #purgeUser(User)}
	 */
	@Deprecated
	@Authorized({ PrivilegeConstants.DELETE_USERS })
	public void deleteUser(User user) throws APIException;
	
	/**
	 * Completely remove a location from the database (not reversible). This method delegates to
	 * #purgeLocation(location, boolean) method.
	 * 
	 * @param user the User to remove from the database.
	 * @should delete given user
	 */
	@Authorized({ PrivilegeConstants.PURGE_USERS })
	public void purgeUser(User user) throws APIException;
	
	/**
	 * Completely remove a user from the database (not reversible). This is a delete from the
	 * database. This is included for troubleshooting and low-level system administration. Ideally,
	 * this method should <b>never</b> be called &mdash; <code>Users</code> should be
	 * <em>voided</em> and not <em>deleted</em> altogether (since many foreign key constraints
	 * depend on users, deleting a user would require deleting all traces, and any historical trail
	 * would be lost). This method only clears user roles and attempts to delete the user record. If
	 * the user has been included in any other parts of the database (through a foreign key), the
	 * attempt to delete the user will violate foreign key constraints and fail.
	 * 
	 * @param cascade <code>true</code> to delete associated content
	 * @should throw APIException if cascade is true
	 * @should delete given user when cascade equals false
	 * @should not delete user roles for given user when cascade equals false
	 */
	@Authorized({ PrivilegeConstants.PURGE_USERS })
	public void purgeUser(User user, boolean cascade) throws APIException;
	
	/**
	 * @deprecated use {@link #getAllPrivileges()}
	 */
	@Deprecated
	@Transactional(readOnly = true)
	public List<Privilege> getPrivileges() throws APIException;
	
	/**
	 * Returns all privileges currently possible for any User
	 * 
	 * @return Global list of privileges
	 * @throws APIException
	 * @should return all privileges in the system
	 */
	@Transactional(readOnly = true)
	public List<Privilege> getAllPrivileges() throws APIException;
	
	/**
	 * @deprecated use {@link #getAllRoles()}
	 */
	@Deprecated
	@Transactional(readOnly = true)
	public List<Role> getRoles() throws APIException;
	
	/**
	 * Returns all roles currently possible for any User
	 * 
	 * @return Global list of roles
	 * @throws APIException
	 * @should return all roles in the system
	 */
	@Transactional(readOnly = true)
	public List<Role> getAllRoles() throws APIException;
	
	/**
	 * @deprecated use {@link org.openmrs.Role#getInheritedRoles()}
	 */
	@Deprecated
	@Transactional(readOnly = true)
	public List<Role> getInheritingRoles(Role role) throws APIException;
	
	/**
	 * Save the given role in the database
	 * 
	 * @param role Role to update
	 * @return the saved role
	 * @throws APIException
	 * @should throw error if role inherits from itself
	 * @should save given role to the database
	 */
	@Authorized({ PrivilegeConstants.MANAGE_ROLES })
	public Role saveRole(Role role) throws APIException;
	
	/**
	 * Complete remove a role from the database
	 * 
	 * @param role Role to delete from the database
	 * @throws APIException
	 * @should throw error when role is a core role
	 * @should return if role is null
	 * @should delete given role from database
	 */
	@Authorized({ PrivilegeConstants.PURGE_ROLES })
	public void purgeRole(Role role) throws APIException;
	
	/**
	 * Save the given privilege in the database
	 * 
	 * @param privilege Privilege to update
	 * @return the saved privilege
	 * @throws APIException
	 * @should save given privilege to the database
	 */
	@Authorized({ PrivilegeConstants.MANAGE_PRIVILEGES })
	public Privilege savePrivilege(Privilege privilege) throws APIException;
	
	/**
	 * Completely remove a privilege from the database
	 * 
	 * @param privilege Privilege to delete
	 * @throws APIException
	 * @should delete given privilege from the database
	 * @should throw error when privilege is core privilege
	 */
	@Authorized({ PrivilegeConstants.PURGE_PRIVILEGES })
	public void purgePrivilege(Privilege privilege) throws APIException;
	
	/**
	 * Returns role object with given string role
	 * 
	 * @return Role object for specified string
	 * @throws APIException
	 * @should fetch role for given role name
	 */
	@Transactional(readOnly = true)
	public Role getRole(String r) throws APIException;
	
	/**
	 * Get Role by its UUID
	 * 
	 * @param uuid
	 * @return
	 * @should find object given valid uuid
	 * @should return null if no object found with given uuid
	 */
	@Transactional(readOnly = true)
	public Role getRoleByUuid(String uuid) throws APIException;
	
	/**
	 * Returns Privilege in the system with given String privilege
	 * 
	 * @return Privilege
	 * @throws APIException
	 * @should fetch privilege for given name
	 */
	@Transactional(readOnly = true)
	public Privilege getPrivilege(String p) throws APIException;
	
	/**
	 * Get Privilege by its UUID
	 * 
	 * @param uuid
	 * @return
	 * @should find object given valid uuid
	 * @should return null if no object found with given uuid
	 * @should fetch privilege for given uuid
	 */
	@Transactional(readOnly = true)
	public Privilege getPrivilegeByUuid(String uuid) throws APIException;
	
	/**
	 * @deprecated use {@link #getAllUsers()}
	 */
	@Deprecated
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public List<User> getUsers() throws APIException;
	
	/**
	 * Returns all users in the system
	 * 
	 * @return Global list of users
	 * @throws APIException
	 * @should fetch all users in the system
	 * @should not contains any duplicate users
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public List<User> getAllUsers() throws APIException;
	
	/**
	 * Changes the <code>user<code>'s password
	 * ** Restricted to Super User access**
	 * 
	 * @param u user
	 * @param pw new password
	 * @throws APIException
	 * @should change password for the given user and password
	 */
	@Authorized({ PrivilegeConstants.EDIT_USER_PASSWORDS })
	@Logging(ignoredArgumentIndexes = { 1 })
	public void changePassword(User u, String pw) throws APIException;
	
	/**
	 * Changes the current user's password.
	 * 
	 * @param pw current password
	 * @param pw2 new password
	 * @throws APIException
	 * @should match on correctly hashed sha1 stored password
	 * @should match on incorrectly hashed sha1 stored password
	 * @should match on sha512 hashed password
	 * @should be able to update password multiple times
	 */
	@Logging(ignoredArgumentIndexes = { 0, 1 })
	public void changePassword(String pw, String pw2) throws APIException;
	
	/**
	 * Changes the current user's password directly. This is most useful if migrating users from
	 * other systems and you want to retain the existing passwords. This method will simply save the
	 * passed hashed password and salt directly to the database.
	 * 
	 * @param user the user whose password you want to change
	 * @param hashedPassword - the <em>already hashed</em> password to store
	 * @param salt - the salt which should be used with this hashed password
	 * @throws APIException
	 * @since 1.5
	 * @should change the hashed password for the given user
	 */
	@Authorized({ PrivilegeConstants.EDIT_USER_PASSWORDS })
	public void changeHashedPassword(User user, String hashedPassword, String salt) throws APIException;
	
	/**
	 * Changes the passed user's secret question and answer.
	 * 
	 * @param u User to change
	 * @param question
	 * @param answer
	 * @throws APIException
	 * @since 1.5
	 * @should change the secret question and answer for given user
	 */
	@Authorized({ PrivilegeConstants.EDIT_USER_PASSWORDS })
	@Logging(ignoredArgumentIndexes = { 1, 2 })
	public void changeQuestionAnswer(User u, String question, String answer) throws APIException;
	
	/**
	 * Changes the current user's secret question and answer.
	 * 
	 * @param pw user's password
	 * @param q question
	 * @param a answer
	 * @throws APIException
	 * @should match on correctly hashed stored password
	 * @should match on incorrectly hashed stored password
	 */
	@Logging(ignoreAllArgumentValues = true)
	public void changeQuestionAnswer(String pw, String q, String a) throws APIException;
	
	/**
	 * Compares <code>answer</code> against the <code>user</code>'s secret answer.
	 * 
	 * @param u user
	 * @param answer
	 * @throws APIException
	 * @should return true when given answer matches stored secret answer
	 * @should return false when given answer does not match the stored secret answer
	 */
	@Transactional(readOnly = true)
	@Logging(ignoredArgumentIndexes = { 1 })
	public boolean isSecretAnswer(User u, String answer) throws APIException;
	
	/**
	 * Return a list of users sorted by personName (see {@link PersonByNameComparator}) if any part
	 * of the search matches first/last/system id and the user has one at least one of the given
	 * <code>roles</code> assigned to them
	 * 
	 * @param nameSearch string to compare to the beginning of user's given/middle/family/family2
	 *            names
	 * @param roles all the Roles the user must contain
	 * @param includeVoided true/false whether to include voided users
	 * @return list of users matching the given attributes
	 * @should match search to familyName2
	 * @should fetch voided users if includedVoided is true
	 * @should not fetch voided users if includedVoided is false
	 * @should fetch users with name that contains given nameSearch
	 * @should fetch users with systemId that contains given nameSearch
	 * @should fetch users with at least one of the given role objects
	 * @should not fetch duplicate users
	 * @should fetch all users if nameSearch is empty or null
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public List<User> getUsers(String nameSearch, List<Role> roles, boolean includeVoided) throws APIException;
	
	/**
	 * @deprecated use {@link #getUsers(String, List, boolean)}
	 */
	@Deprecated
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public List<User> findUsers(String name, List<String> roles, boolean includeVoided) throws APIException;
	
	/**
	 * @deprecated use {@link #getUsersByName(String, String, boolean)}
	 */
	@Deprecated
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public List<User> findUsers(String givenName, String familyName, boolean includeVoided) throws APIException;
	
	/**
	 * Search for a list of users by exact first name and last name.
	 * 
	 * @param givenName
	 * @param familyName
	 * @param includeVoided
	 * @return List<User> object of users matching criteria
	 * @should fetch users exactly matching the given givenName and familyName
	 * @should fetch voided users whenincludeVoided is true
	 * @should not fetch any voided users when includeVoided is false
	 * @should not fetch any duplicate users
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public List<User> getUsersByName(String givenName, String familyName, boolean includeVoided) throws APIException;
	
	/**
	 * Get all user accounts that belong to a given person.
	 * 
	 * @param person
	 * @param includeRetired
	 * @return all user accounts that belong to person, including retired ones if specified
	 * @throws APIException
	 * @should fetch all accounts for a person when include retired is true
	 * @should not fetch retired accounts when include retired is false
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public List<User> getUsersByPerson(Person person, boolean includeRetired) throws APIException;
	
	/**
	 * @deprecated use {@link #getUsers(String, List, boolean)}
	 */
	@Deprecated
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_USERS })
	public List<User> getAllUsers(List<Role> roles, boolean includeVoided) throws APIException;
	
	/**
	 * Adds the <code>key</code>/<code>value</code> pair to the given <code>user</code>.
	 * <p>
	 * <b>Implementations of this method should handle privileges</b>
	 * 
	 * @param user
	 * @param key
	 * @param value
	 * @return the user that was passed in and added to
	 * @should return null if user is null
	 * @should throw error when user is not authorized to edit users
	 * @should add property with given key and value when key does not already exist
	 * @should modify property with given key and value when key already exists
	 */
	public User setUserProperty(User user, String key, String value) throws APIException;
	
	/**
	 * Removes the property denoted by <code>key</code> from the <code>user</code>'s properties.
	 * <b>Implementations of this method should handle privileges</b>
	 * 
	 * @param user
	 * @param key
	 * @return the user that was passed in and removed from
	 * @should return null if user is null
	 * @should throw error when user is not authorized to edit users
	 * @should remove user property for given user and key
	 */
	public User removeUserProperty(User user, String key) throws APIException;
	
	/**
	 * Get/generate/find the next system id to be doled out. Assume check digit /not/ applied in
	 * this method
	 * 
	 * @return new system id
	 */
	public String generateSystemId();
	
}
