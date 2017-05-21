/**
 * 
 */
package com.services.projects.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.services.projects.model.Sprint;
import com.services.projects.model.Task;
import com.services.projects.model.TrackVector;
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
		List<JsonProjectWrapper> _resTmp = DAOProject.getInstance().getRegisteredObjects();
		for(JsonProjectWrapper _proj : _resTmp){
			SelectItem _prjEntry = new SelectItem();
			_prjEntry.setLabel(_proj.getName());
			_prjEntry.setValue(_proj.getPrimaryKeyId());
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
			List<JsonProjectWrapper> _resTmp = DAOProject.getInstance().getRegisteredObjects();
			this.projectListAsJson = ModelManagerHelper.getJsonStream(_resTmp);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return this.projectListAsJson;
	}
	
	public void setProjectListAsJson(String _json){
		this.projectListAsJson = _json;
	}
	
	public Double getBudjet(){
		String _ccProjectName = this.getCurrentProject();
		JsonProjectWrapper _jsonProject = this.getCurrentProjectWrapper();
		TreeMap<String, Double> _resTmp = ProjectService.computeProjectBudget(_jsonProject);
		return _resTmp.get(ProjectService.PROJECT_WL_SUP);
	}
	
	private JsonProjectWrapper  getCurrentProjectWrapper(){
		String _ccProjectName = this.getCurrentProject();
		JsonProjectWrapper _sPro = new JsonProjectWrapper();
		_sPro.setPrimaryKeyId(_ccProjectName);
		JsonProjectWrapper _jsonProject = DAOProject.getInstance().findByPrimaryKey(_sPro);
		return _jsonProject;
	}
	
	public String savePhase(){
		String _res=null;
		_res = this.projectPhases;
		String _ccProjectName = this.getCurrentProject();
		
		List<Sprint> _SprintList=null;
		try {
			_SprintList = ModelManagerHelper.<Sprint>getObjectListFromJson(_res, Sprint.class);
			JsonProjectWrapper _jsonProject = this.getCurrentProjectWrapper();
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
		if(this.projectPhases == null && _cProject!=null){
			try {
				JsonProjectWrapper _jsonProject = this.getCurrentProjectWrapper();
				this.projectPhases = ModelManagerHelper.getJsonStream(_jsonProject.getProject().getPhases().getPhase());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.projectPhases;
	}
	
	
	public void saveTracking(){		
		
		System.out.println("TO SAVE");
		System.out.println(this.projectPhases);
		JsonNode _data = ModelManagerHelper.convertAsJsonNode(this.projectPhases);
		JsonProjectWrapper _jsonProject = this.getCurrentProjectWrapper();
		if(_data.isArray()){
			for(JsonNode _taskJson : _data){
				String _cSprintId = _taskJson.get("SprintId").asText();
				String _cTaskId = _taskJson.get("taskId").asText();
				Sprint _cSprint = ProjectService.findSprintById(_cSprintId, _jsonProject.getProject().getPhases().getPhase());
				Task _cTask = ProjectService.findTaskByIdIntoTaskList(_cTaskId, _cSprint.getTaskList());
				List<TrackVector> _cTimeWLEntries = _cTask.getTrackEntries().getBudgetLine().getTrack();
				JsonNode _taskWlEntries  =_taskJson.get("taskWlEntries");
				TreeMap<String,TrackVector> _trackEntriesForUpdate = ProjectService.buildEntriesFromJson(_taskWlEntries);
				
				for(TrackVector _cTrackEntry : _cTimeWLEntries){
					String _key = _cTrackEntry.getTaskOwner()+_cTrackEntry.getTrackTime().toXMLFormat();	
					if(_trackEntriesForUpdate.containsKey(_key)){
						TrackVector _nTrackEntry = _trackEntriesForUpdate.get(_key);
						_cTrackEntry.setProgressFactor(_nTrackEntry.getProgressFactor());
						_trackEntriesForUpdate.remove(_key);
					}else{
						_cTrackEntry.setProgressFactor(0);
					}		
				}
				if(!_trackEntriesForUpdate.isEmpty()){
					for(TrackVector _nTEntries : _trackEntriesForUpdate.values()){
						_cTimeWLEntries.add(_nTEntries);
					}
				}
				if(_cTimeWLEntries.size() > 1){
					Iterator<TrackVector> _iter = _cTimeWLEntries.iterator();
					while(_iter.hasNext()){
						if(_iter.next().getProgressFactor() == 0) _iter.remove();
					}
				}
			}
		}
		try {
			DAOProject.getInstance().saveProject(_jsonProject);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.projectPhases = null;
		
	}
	
	public void setTaskTrackingAsJson(String _data){
		this.projectPhases = _data;
	}
	
	
	public String getTaskTrackingAsJson(){
		String _cProject = this.getCurrentProject();
		if(this.projectPhases == null && _cProject!=null){
			try {
				JsonProjectWrapper _jsonProject = this.getCurrentProjectWrapper();
				JSONArray _resList = new JSONArray();
				for(Sprint _sprint : _jsonProject.getProject().getPhases().getPhase()){
					TreeMap<String,Double> _overViewMetrics = ProjectService.computeSprintBudget(_sprint);
					for( Task _task : _sprint.getTaskList().getTask()){
						JSONObject _jtask = new JSONObject();
						_jtask.put("SprintId", _sprint.getKeyId());
						_jtask.put("SprintName", _sprint.getName());
						_jtask.put("taskId", _task.getId());
						_jtask.put("taskName", _task.getName());
						_jtask.put("taskDesc", _task.getDescription());
						_jtask.put("taskWl", _task.getMetric().getEstimate().getWlSup());
						_jtask.put("taskWlConsumed", ProjectService.getConsumedWorkLoad(_task));
						JSONArray _trackList = new JSONArray();
						for(TrackVector _tVector : _task.getTrackEntries().getBudgetLine().getTrack()){
							JSONObject _jTVector = new JSONObject();
							_jTVector.put("trackTimeId", _tVector.getTrackTime()+_tVector.getTaskOwner()); 
							_jTVector.put("trackTime", _tVector.getTrackTime()); 
							_jTVector.put("progressFactor", _tVector.getProgressFactor()); 
							_jTVector.put("taskOwner", _tVector.getTaskOwner()); 
							_trackList.put(_jTVector);
						}
						_jtask.put("taskWlEntries",_trackList);
						_resList.put(_jtask);
					}
				}
				this.projectPhases = _resList.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.projectPhases;
	}
	
	public String getProjectAsJson(){
		String _res=null;
		JsonProjectWrapper _jsonProject = this.getCurrentProjectWrapper();
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
		String _cProject = _ctx.getRequestParameterMap().get("project");
		
		System.out.println("param from HTPP req:" + _cProject  + " from session" );
		if(_cProject != null){
			this.setCurrentProject(_cProject);			
		}		
		return this.currentProject;
	}

	/**
	 * @param currentProject the currentProject to set
	 */
	public void setCurrentProject(String currentProject) {
		this.currentProject = currentProject;
		//this.setProjectPhases(null);
		//this.setAsError(false);	
	}

	/**
	 * @param projectPhases the projectPhases to set
	 */
	public void setProjectPhases(String projectPhases) {
		this.projectPhases = projectPhases;
	}
	
	public XMLGregorianCalendar getCurrentProjectEndDate(){
		JsonProjectWrapper _jsonProject = this.getCurrentProjectWrapper();
		XMLGregorianCalendar _res = _jsonProject.getEndDate();
		return _res;
	}
	
	public XMLGregorianCalendar getCurrentProjectStartDate(){
		JsonProjectWrapper _jsonProject = this.getCurrentProjectWrapper();
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
