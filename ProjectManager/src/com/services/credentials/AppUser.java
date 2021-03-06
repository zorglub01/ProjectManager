/**
 * 
 */
package com.services.credentials;

import java.util.ArrayList;

import com.services.credentials.profiles.UserProfile;

/**
 * @author thomas.foret
 *
 */
public class AppUser {
	
	private String loginName;
	private String loginPwd;
	private String description;
	private String sessionId;
	private Integer userid;
	
	private ArrayList<UserGroup> userGroups;
	private ArrayList<UserProfile> userProfile;
	
	private AppUser(){
		
	}
	
	public AppUser(String sessionId, String loginName, String loginPwd){
		this.setSessionId(sessionId);
		this.setLoginName(loginName);
		this.setLoginPwd(loginPwd);
	}
	
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return this.loginName;
	}

	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the loginPwd
	 */
	public String getLoginPwd() {
		return this.loginPwd;
	}

	/**
	 * @param loginPwd the loginPwd to set
	 */
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * @return the userGroups
	 */
	public ArrayList<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(ArrayList<UserGroup> currentGroups) {
		this.userGroups = currentGroups;
		
	}

	

	public void setUserProfiles(ArrayList<UserProfile> userProfiles) {
		// TODO Auto-generated method stub
		this.userProfile = userProfiles;
		
	}
	
	
	/**
	 * @return
	 */
	public ArrayList<UserProfile> getUserProfiles() {
		// TODO Auto-generated method stub
		return this.userProfile;
		
	}

	
	
	
	

}
