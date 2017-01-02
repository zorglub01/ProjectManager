/**
 * 
 */
package com.services.projects.bean;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.services.projects.utils.ModelManagerHelper;

/**
 * @author thomas
 *
 */
public class ProjectBeanWrapper {
	private String projectListAsJson=null;
	
	public String getProjectListAsJson(){
		
		try{
			// TEST CODE using bundle data
			//ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/lang/util/msg");
			//if(this.projectListAsJson ==null)
			//	this.projectListAsJson = bundle.getString("project_list");
			List<JsonProjectWrapper> _resTmp = DAOProject.getInstance().<JsonProjectWrapper>getRegisteredObjects(JsonProjectWrapper.class);
			this.projectListAsJson = ModelManagerHelper.getJsonStream(_resTmp);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return this.projectListAsJson;
	}
	public void setProjectListAsJson(String _json){
		this.projectListAsJson = _json;
	}
	
	public String saveProject(){
		String _res=null;
		
		_res = this.projectListAsJson;
		try {
			List<JsonProjectWrapper> _projectList;
			_projectList = ModelManagerHelper.<JsonProjectWrapper>getObjectListFromJson(_res, JsonProjectWrapper.class);
			DAOProject.getInstance().saveProjectList(_projectList);
			System.out.println("Redy to persiste : " + _projectList);
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
		return _res;
	}
	
	public String getProjectAsJson(){
		String _res=null;
		return _res;
	}

}
