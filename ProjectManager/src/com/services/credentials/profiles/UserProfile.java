/**
 * 
 */
package com.services.credentials.profiles;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import com.services.credentials.ResourceEntry;
import com.services.projects.model.Profile;
import com.services.projects.model.ProfileURI;
import com.services.projects.utils.DBObject;

/**
 * @author thomas.foret
 *
 */
public class UserProfile implements DBObject {
	
	public static final String HTTP_PARAM_ResourcePath="path";
	
	private Profile profile = new Profile();
	private TreeMap<String,ProfileURI> uriResourceMap=new TreeMap<String,ProfileURI>();
	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return this.profile;
	}
	/**
	 * @param profile the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
		for (ProfileURI myResource : this.getProfile().getResources().getResource()) {
			this.registerResource(myResource);			
		}
		
	}
	/**
	 * @return the resources
	 */
	public ArrayList<ResourceEntry> getResources() {
		ArrayList<ResourceEntry> _res = new ArrayList<ResourceEntry>();
		for (ProfileURI myResource : this.getProfile().getResources().getResource()) {
			ResourceEntry rEntry = new ResourceEntry(myResource.getPath(), myResource.getDescription());
			_res.add(rEntry);
			this.registerResource(myResource);			
		}
		return _res;
	}
	
	protected boolean isResourceExist(String _path){
		boolean _res=false;
		ProfileURI _tmp = this.uriResourceMap.get(_path);
		if(_tmp !=null) _res=true;
		return _res;
	}
	
	
	
	protected void registerResource(ProfileURI _pUri){
		this.uriResourceMap.put(_pUri.getPath(), _pUri);
	}
	
	protected ProfileURI unRegisterResource(ProfileURI _pUri){
		return this.uriResourceMap.remove(_pUri.getPath());
	}
	
	
	/**
	 * @return the profileName
	 */
	String getProfileName() {
		return this.getProfile().getName();
	}
	/**
	 * @return the profileDesc
	 */
	String getProfileDesc() {
		return this.getProfile().getDescription();
	}
	
	
	public static UserProfile getAdminProfile() throws JAXBException{
		UserProfile _res1 = new UserProfile();
		_res1.setPrimaryKeyId("Admin");
		_res1 = getDAO().findByPrimaryKey(_res1);
		return _res1 ;
	}
	
	
	private static DAOProfile getDAO(){
		DAOProfile _res=DAOProfile.getInstance();
		if(_res==null){
			ServletContext cContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
			String _rootPathApp = cContext.getRealPath("/");
			_res = DAOProfile.getInstance(_rootPathApp);

		}
		return _res;
	}

	
	public static UserProfile getDefaultProfile() throws JAXBException{
		UserProfile _res1 = new UserProfile();
		_res1.setPrimaryKeyId("Default");
		_res1 = getDAO().findByPrimaryKey(_res1);
		return _res1 ;
	}
	public ProfileURI getResourceURI(String _path) {
		
		return this.uriResourceMap.get(_path);
	}
	public void addResource(ProfileURI _pUri) {
		this.getProfile().getResources().getResource().add(_pUri);
		this.registerResource(_pUri);
		
	}
	public ProfileURI removeResources(ProfileURI _pUri) {
		int index=0;
		ProfileURI _res=null;
		for (ProfileURI _prui : this.getProfile().getResources().getResource()){
			if(_prui.getPath().equalsIgnoreCase(_pUri.getPath())){
				this.getProfile().getResources().getResource().remove(index);
				_res = this.unRegisterResource(_pUri);
				break;
			}
			index++;
		}
		return _res;
		
	}

	/* (non-Javadoc)
	 * @see com.services.projects.utils.DBObject#getPrimaryKeyId()
	 */
	public String getPrimaryKeyId() {
		return this.getProfile().getName();
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.services.projects.utils.DBObject#setPrimaryKeyId(java.lang.String)
	 */
	public void setPrimaryKeyId(String _pmKid) {
		this.getProfile().setName(_pmKid);
		
	}
	
	

}
