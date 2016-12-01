/**
 * 
 */
package com.services.credentials.profiles;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import org.jboss.mx.loading.ClassLoaderUtils;

import com.services.credentials.ResourceEntry;
import com.services.projects.model.Profile;
import com.services.projects.model.ProfileURI;
import com.services.projects.utils.ModelManagerHelper;

/**
 * @author thomas.foret
 *
 */
public class UserProfile {
	
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
	
	
	public static UserProfile getAdminProfile() throws JAXBException{		
		FacesContext fCtx = FacesContext.getCurrentInstance();
		InputStream fileXmlUrl = fCtx.getExternalContext().getResourceAsStream("/WEB-INF/classes/com/services/credentials/profiles/profile_Admin.xml");										
		Profile _res1 = ModelManagerHelper.<Profile>loadModel(fileXmlUrl, Profile.class);
		UserProfile myProfile = getUserProfile(_res1);
		return myProfile;
	}
	
	
	private static UserProfile getUserProfile(Profile _xmlprofile){
		UserProfile myProfile = new UserProfile();
		myProfile.setProfileName(_xmlprofile.getName());
		myProfile.setProfileDesc(_xmlprofile.getDescription());
		myProfile.setResources(null);
		for (ProfileURI myResource : _xmlprofile.getResources().getResource()) {
			ResourceEntry rEntry = new ResourceEntry(myResource.getPath(), myResource.getDescription());
			myProfile.getResources().add(rEntry);
			
		}
		return myProfile;
	}
	
	public static UserProfile getDefaultProfile() throws JAXBException{
		FacesContext fCtx = FacesContext.getCurrentInstance();
		InputStream fileXmlUrl = fCtx.getExternalContext().getResourceAsStream("/WEB-INF/classes/com/services/credentials/profiles/profile_Default.xml");										
		Profile _res1 = ModelManagerHelper.<Profile>loadModel(fileXmlUrl, Profile.class);
		UserProfile myProfile = getUserProfile(_res1);		
		return myProfile;
	}
	
	
	

}
