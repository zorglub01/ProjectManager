/**
 * 
 */
package com.services.credentials;

import java.util.ArrayList;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.jmd.test.jsf.LoginBean;

/**
 * @author thomas.foret
 *
 */
public class CredentialsManager {
	
	public static ArrayList<SelectItem> getMenuEntry(HttpSession session, LoginBean user){
		System.out.println(user.getNom() +"/" + user.getMdp() + "/" + session.getId() );
		AppUser currentUser = new AppUser(session.getId(), user.getNom(),user.getMdp());
		ArrayList<UserGroup> currentGroups = loadUserGroups(currentUser);
		currentUser.setUserGroups(currentGroups);
		
		return null;
		
	}

	private static ArrayList<UserGroup> loadUserGroups(AppUser currentUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
