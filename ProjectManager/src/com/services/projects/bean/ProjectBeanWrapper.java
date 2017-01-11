/**
 * 
 */
package com.services.projects.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.services.projects.model.Sprint;
import com.services.projects.utils.ModelManagerHelper;

/**
 * @author thomas
 *
 */
public class ProjectBeanWrapper {
	private String projectListAsJson=null;
	
	private String currentProject=null;
	
	private String projectPhases=null;
	private String errorMessage=null;
	private boolean asError=false;
	
	
	public List<SelectItem> getAvailableProjects(){
		ArrayList<SelectItem> _res = new ArrayList<SelectItem>();
		List<JsonProjectWrapper> _resTmp = DAOProject.getInstance().<JsonProjectWrapper>getRegisteredObjects(JsonProjectWrapper.class);
		for(JsonProjectWrapper _proj : _resTmp){
			SelectItem _prjEntry = new SelectItem();
			_prjEntry.setLabel(_proj.getName());
			_prjEntry.setValue(_proj.getShortName());
			_prjEntry.setDescription(_proj.getDescription());
			_res.add(_prjEntry);
		}
		return _res;
	}
	
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
	
	public String savePhase(){
		String _res=null;
		_res = this.projectPhases;
		String _ccProjectName = this.getCurrentProject();
		
		List<Sprint> _SprintList=null;
		try {
			_SprintList = ModelManagerHelper.<Sprint>getObjectListFromJson(_res, Sprint.class);
			JsonProjectWrapper _jsonProject = DAOProject.getInstance().findProjectByName(_ccProjectName);
			boolean _resTmp = _jsonProject.getProject().getPhases().getPhase().retainAll(_SprintList);
			_jsonProject.getProject().getPhases().getPhase().addAll(_SprintList);
			List<String> _errorMessage = ProjectService.checkSprintCoherency(_jsonProject);
			if(!_errorMessage.isEmpty()) {
				this.setErrorMessage(_errorMessage.toString());
				this.setAsError(true);
			}else this.setAsError(false);
					
			DAOProject.getInstance().saveProject(_jsonProject);
			this.projectPhases = null;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _res;
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
	
	
	public String getProjectPhases(){
		String _cProject = this.getCurrentProject();
		if(this.projectPhases == null){
			try {
				JsonProjectWrapper _jsonProject = DAOProject.getInstance().findProjectByName(_cProject );
				this.projectPhases = ModelManagerHelper.getJsonStream(_jsonProject.getProject().getPhases().getPhase());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return this.projectPhases;
	}
	
	public String getProjectAsJson(){
		String _res=null;
		JsonProjectWrapper _jsonProject = DAOProject.getInstance().findProjectByName(this.currentProject);
		try {
			_res = ModelManagerHelper.getJsonStream(_jsonProject.getProject());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return _res;
	}

	/**
	 * @return the currentProject
	 */
	public String getCurrentProject() {
		ExternalContext _ctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession _session = (HttpSession)_ctx.getSession(true);
		String _cProject = _ctx.getRequestParameterMap().get("project");
		
		System.out.println("param from HTPP req:" + _cProject  + " from session" );
		
		if(this.currentProject == null ){
			this.setCurrentProject(_cProject);
		}else if(this.currentProject != null && _cProject!=null && !this.currentProject.equals(_cProject)){
			this.setCurrentProject(_cProject);
			this.setProjectPhases(null);
		}
		
		return this.currentProject;
	}

	/**
	 * @param currentProject the currentProject to set
	 */
	public void setCurrentProject(String currentProject) {
		this.currentProject = currentProject;
	}

	/**
	 * @param projectPhases the projectPhases to set
	 */
	public void setProjectPhases(String projectPhases) {
		this.projectPhases = projectPhases;
	}
	
	public XMLGregorianCalendar getCurrentProjectEndDate(){
		String _name = this.getCurrentProject();
		JsonProjectWrapper _jsonProject = DAOProject.getInstance().findProjectByName(_name);
		XMLGregorianCalendar _res = _jsonProject.getEndDate();
		return _res;
	}
	
	public XMLGregorianCalendar getCurrentProjectStartDate(){
		String _name = this.getCurrentProject();
		JsonProjectWrapper _jsonProject = DAOProject.getInstance().findProjectByName(_name);
		XMLGregorianCalendar _res = _jsonProject.getStartDate();
		return _res;
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
