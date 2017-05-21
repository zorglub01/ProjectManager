/**
 * 
 */
package com.services.credentials;

import com.services.projects.model.User;
import com.services.projects.utils.DBObject;

/**
 * @author thomas
 *
 */
public class UserWrapper implements DBObject {
	private User user;

	public UserWrapper(User _user){
		this.setUser(_user);
	}
	
	/** 
	 * @see com.services.projects.utils.DBObject#getPrimaryKeyId()
	 */
	public String getPrimaryKeyId() {
		return this.getUser().getKeyId();
	}

	public void setPrimaryKeyId(String _keyId){
		this.getUser().setKeyId(_keyId);
	}
	
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
