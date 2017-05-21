/**
 * 
 */
package com.services.credentials;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import com.jmd.test.jsf.LoginBean;
import com.services.credentials.profiles.UserProfile;

/**
 * @author thomas.foret
 *
 */
public class CredentialsManager {
	
	public static ArrayList<SelectItem> getMenuEntry(HttpSession session, LoginBean user) throws URISyntaxException, JAXBException, MalformedURLException{
		System.out.println(user.getNom() +"/" + user.getMdp() + "/" + session.getId() );
		AppUser currentUser = new AppUser(session.getId(), user.getNom(),user.getMdp());
		ArrayList<UserGroup> currentGroups = loadUserGroups(currentUser);
		currentUser.setUserGroups(currentGroups);
		ArrayList<UserProfile> userProfiles = loadUserProfile(currentUser);
		currentUser.setUserProfiles(userProfiles);
		
		ArrayList<SelectItem> _res = new ArrayList<SelectItem>();
		
		for (UserProfile userProfile : userProfiles) {
			
			for(ResourceEntry resourceE : userProfile.getResources()){
				String _javaScript="$(\"#_"+ resourceE.getPath()+"\").load(\""+resourceE.getPath()+"\");";
				SelectItem _res1 = new SelectItem(resourceE.getPath(), resourceE.getDesc(), _javaScript);
				_res.add(_res1);	
			}
			
			
		}
			
		return _res;
		
	}

	/**
	 * Find out the user's profiles
	 * It's used to build the menu based on the credentials
	 * @param currentUser
	 * @return
	 * @throws JAXBException 
	 * @throws URISyntaxException 
	 * @throws MalformedURLException 
	 */
	private static ArrayList<UserProfile> loadUserProfile(AppUser currentUser) throws URISyntaxException, JAXBException, MalformedURLException {
		// TODO Find out from the DB the profiles of the current user
		//
		ArrayList<UserProfile> _res = new ArrayList<UserProfile>();
		if( currentUser.getLoginName().equalsIgnoreCase("Admin") || currentUser.getLoginName().equalsIgnoreCase("Admin2")){
			UserProfile profile1 = UserProfile.getAdminProfile();
			_res.add(profile1);
		}else{
			UserProfile profile1 = UserProfile.getDefaultProfile();
			_res.add(profile1);
			
			
		}
		
		return _res;
	}

	private static ArrayList<UserGroup> loadUserGroups(AppUser currentUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
