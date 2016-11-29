package com.services.credentials.profiles;

import com.services.credentials.ResourceEntry;

class AdminProfile extends UserProfile {
	
	
	public AdminProfile() {
		this.setProfileName("Administrator");
		this.setProfileDesc("Admin profile to manage the web site");
		ResourceEntry item1 = new ResourceEntry("admin/credAdmin.faces", "Admin page");
		ResourceEntry item2 = new ResourceEntry("credPM.faces", "Project Page");
		ResourceEntry item3 = new ResourceEntry("help/help.jsp", "Help pages");
		this.getResources().add(item1);
		this.getResources().add(item2);
		this.getResources().add(item3);
	}

}
