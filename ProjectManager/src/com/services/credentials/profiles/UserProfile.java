/**
 * 
 */
package com.services.credentials.profiles;

import java.util.ArrayList;

import com.services.credentials.ResourceEntry;

/**
 * @author thomas.foret
 *
 */
public abstract class UserProfile {
	
	private String profileName;
	private String profileDesc;
	private ArrayList<ResourceEntry> resources;
	/**
	 * @return the resources
	 */
	public ArrayList<ResourceEntry> getResources() {
		if (this.resources == null) this.resources = new ArrayList<ResourceEntry>();
		return this.resources;
	}
	/**
	 * @param resources the resources to set
	 */
	void setResources(ArrayList<ResourceEntry> resources) {
		this.resources = resources;
	}
	
	/**
	 * @param profileName the profileName to set
	 */
	protected void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	/**
	 * @return the profileName
	 */
	String getProfileName() {
		return this.profileName;
	}
	/**
	 * @return the profileDesc
	 */
	String getProfileDesc() {
		return this.profileDesc;
	}
	/**
	 * @param profileDesc the profileDesc to set
	 */
	protected void setProfileDesc(String profileDesc) {
		this.profileDesc = profileDesc;
	}
	
	
	public static UserProfile getAdminProfile(){
		return new AdminProfile();
	}
	
	public static UserProfile getDefaultProfile(){
		return new DefaultProfile();
	}
	
	
	

}
