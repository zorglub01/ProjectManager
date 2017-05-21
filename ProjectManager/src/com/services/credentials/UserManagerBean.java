package com.services.credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.services.credentials.profiles.DAOProfile;
import com.services.credentials.profiles.UserProfile;
import com.services.projects.bean.DAOProject;
import com.services.projects.bean.JsonProjectWrapper;
import com.services.projects.model.User;
import com.services.projects.utils.ModelManagerHelper;

public class UserManagerBean {

	private String usersAsJson=null;
	private String errorMessage=null;
	private boolean asError=false;

	/**
	 * @return the usersAsJson
	 */
	public String getUsersAsJson() {
		
		try{
			List<UserWrapper> _resTmp = DAOUser.getInstance().getRegisteredObjects();
			ArrayList<User> _list = new ArrayList<User>(); 
			for(UserWrapper _uW : _resTmp){
				_list.add(_uW.getUser()); 
			}
			this.usersAsJson = ModelManagerHelper.getJsonStream(_list);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return usersAsJson;
	}

	/**
	 * @param usersAsJson the usersAsJson to set
	 */
	public void setUsersAsJson(String usersAsJson) {
		this.usersAsJson = usersAsJson;
	}
	
	
	public void saveUsers(){
		List<TreeMap> _res = null;
		List<User> _res1 = null;
		try {
			String _tmp = this.usersAsJson;
			_res = ModelManagerHelper.getObjectListFromJson(_tmp, TreeMap.class);
			_res1  = ModelManagerHelper.getObjectListFromJson(_tmp, User.class);
			int _pos=0;
			for (User user : _res1) {
				Object _profile = _res.get(_pos).get("profile_data");
				UserProfile _currentProfile = new UserProfile();
				_currentProfile.setPrimaryKeyId(_profile.toString()); 
				_currentProfile = DAOProfile.getInstance().findByPrimaryKey(_currentProfile);
				user.getProfile().clear();
				user.getProfile().add(_currentProfile.getProfile());
				UserWrapper _usrWrapper = new UserWrapper(user);
				DAOUser.getInstance().saveUser(_usrWrapper);
				_pos++;
			}
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the asError
	 */
	public boolean isAsError() {
		return asError;
	}

	/**
	 * @param asError the asError to set
	 */
	public void setAsError(boolean asError) {
		this.asError = asError;
	}
	
	
}
