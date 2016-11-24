package com.services.credentials.profiles;

import com.services.credentials.ResourceEntry;

class DefaultProfile extends UserProfile {

	public DefaultProfile(){
		this.setProfileName("Default");	
		this.setProfileDesc("Default profile with limited access");
		ResourceEntry item2 = new ResourceEntry("help/help.jsp", "Help pages");		
		this.getResources().add(item2);
	}
}
