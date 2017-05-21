/**
 * 
 */
package com.services.projects.bean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.services.credentials.profiles.UserProfile;
import com.services.projects.model.Phases;
import com.services.projects.model.Project;
import com.services.projects.model.Sprint;
import com.services.projects.utils.DBManager;
import com.services.projects.utils.ModelManagerHelper;

/**
 * @author thomas
 *
 */
public class DAOProject implements DBManager<JsonProjectWrapper> {
	private static final String PROJECT_DEFAULT = "Default";
	private static final String HTTP_UPLOAD_PARAM_VAL = "project";
	/**
	 * Static absolute file path where the xml files are stored
	 */
	private static String urlDbPath;
	/**
	 * Static relative DB dir location
	 */
	private static String DB_PATH = "DB/projects";
	private static TreeMap<String, Path> repository = null;
	private static DAOProject instance = null;

	private DAOProject() {

	}

	/**
	 * @return the hTTP_UPLOAD_PARAM_VAL
	 */
	public String getHTTP_UPLOAD_PARAM_VAL() {
		return HTTP_UPLOAD_PARAM_VAL;
	}

	/**
	 * Initial the DAO based on the root DB directory folder
	 * 
	 * @param rootCtxPath
	 * @return
	 */
	public static DAOProject getInstance(String rootCtxPath) {

		if (instance != null) {
			return instance;
		} else {
			instance = new DAOProject();
			instance = (DAOProject)ModelManagerHelper.initDaoInstance(rootCtxPath,instance);

		}
		return instance;
	}

	public static DAOProject getInstance() {
		return instance;
	}

	public void saveProjectList(List<JsonProjectWrapper> _projectList) {
		for (JsonProjectWrapper _project : _projectList) {
			try {
				this.saveProject(_project);
			} catch (JAXBException e) {
				System.out.println("Project saved Failed:" + _project.getPrimaryKeyId());
			}
		}

	}

	/**
	 * return true or false
	 * 
	 * @param _name
	 * @return true if a project exists and registered
	 */
	public boolean isProjectExist(String _name) {
		boolean _res = false;
		JsonProjectWrapper _tmp;
		_tmp = this.findProjectByName(_name);
		_res = (_tmp != null);
		return _res;
	}
	

	public JsonProjectWrapper findByPrimaryKey(JsonProjectWrapper _obj) {
		// TODO Auto-generated method stub
		return this.findProjectByName(_obj.getPrimaryKeyId());
	}
	
	

	/**
	 * Return a JsonWrapperProject
	 * The search is based on the shortName of the project
	 * @param _name
	 * @return JsonWrapperProject or null if not existing or not registered
	 * @throws JAXBException
	 */
	private JsonProjectWrapper findProjectByName(String _name) {
		JsonProjectWrapper _res = null;
		Path _uriFile = repository.get(_name);
		if (_uriFile != null && _uriFile.toFile().exists()) {
			Project _resTmp;
			try {
				_resTmp = ModelManagerHelper.<Project>loadModel(_uriFile.toFile(), Project.class);
				_res = new JsonProjectWrapper(_resTmp);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return _res;
	}

	public void saveProject(JsonProjectWrapper _project) throws JAXBException {
		JsonProjectWrapper  _tmpProject = null;		
		if(this.isProjectExist(_project.getPrimaryKeyId())){
			_tmpProject = this.findProjectByName(_project.getPrimaryKeyId());					
		}else{
			_tmpProject = this.findProjectByName(PROJECT_DEFAULT);
			String _shortName = this.getGeneratedShortName(_project);
			_tmpProject.setPrimaryKeyId(_shortName);
			_tmpProject.setKeyId(_shortName);
		}
		_tmpProject.setName(_project.getName());
		_tmpProject.setDescription(_project.getDescription());
		Phases _newProjPhases = _project.getProject().getPhases();
		if(_newProjPhases != null){
			List<Sprint> _SprintList = _newProjPhases.getPhase();
			List<Sprint> _oldSprints = _tmpProject.getProject().getPhases().getPhase();
			List<Sprint> _newSprints = ProjectService.updateSprints(_oldSprints, _SprintList);
			
			_tmpProject.getProject().getPhases().getPhase().clear();
			_tmpProject.getProject().getPhases().getPhase().addAll(_newSprints);
			
		}
		if(_project.getStartDate()!=null)	_tmpProject.getFirstSprint().setStartDate(_project.getStartDate());
		if(_project.getEndDate() !=null)	_tmpProject.getLastSprint().setEndDate(_project.getEndDate());
		
		
		Path _fileToSave = ModelManagerHelper.getFilePath(_tmpProject, this);
		ModelManagerHelper.<Project>saveModel(_tmpProject.getProject(),_fileToSave.toFile()); 
		repository.put(_tmpProject.getPrimaryKeyId(), _fileToSave);
		System.out.println(_fileToSave);

	}

	private String getGeneratedShortName(JsonProjectWrapper _project) {
		String _res=null;
		_res = _project.getName();
		_res = _res.replaceAll(" ", "");
		_res = _res.replaceAll("_", "");
		_res = _res.replaceAll(".", "");
		
		Integer _int = new Integer(0);
		while(this.isProjectExist(_res)){
			_res += _int++;
		}
		return _res.trim();
	}

	public String getPrefixPathName() {
		// TODO Auto-generated method stub
		return HTTP_UPLOAD_PARAM_VAL;
	}

	public String getUrlDbPath() {
		String _res;
		_res = urlDbPath;
		return _res;
	}

	public String getExtensionPathName() {
		return "xml";
	}

	/**
	 * 
	 * @see com.services.projects.utils.DBManager#scanDbFile()
	 */
	public String scanDbFile() throws JsonProcessingException {
		String _res = ModelManagerHelper.getFileScanAsJson(this);
		return _res;
	}

	/**
	 * @param stream
	 * @param filename
	 * @throws IOException
	 * @throws JAXBException
	 */
	public void importFile(InputStream stream, String filename) throws IOException, JAXBException {

		String extension = "tmp";
		Path _path = Paths.get(this.getUrlDbPath());
		Path _fileTmp = Files.createTempFile(_path, filename + "_", "." + extension);
		Files.copy(stream, _fileTmp, StandardCopyOption.REPLACE_EXISTING);
		Project _res = ModelManagerHelper.<Project>loadModel(_fileTmp.toFile(), Project.class);
		JsonProjectWrapper _pTmp = this.findProjectByName(_res.getShortName());
		if (_pTmp == null) {
			_pTmp = this.findProjectByName(PROJECT_DEFAULT);
		}
		_pTmp.setProject(_res);
		
		Path _fileToSave = ModelManagerHelper.getFilePath(_pTmp, this);
		ModelManagerHelper.<Project>saveModel(_pTmp.getProject(),_fileToSave.toFile()); 
		repository.put(_pTmp.getPrimaryKeyId(), _fileToSave);
		System.out.println(_fileToSave);
		Files.deleteIfExists(_fileTmp);
	}

	
	public List<JsonProjectWrapper> getRegisteredObjects() {
		List<JsonProjectWrapper> _res = new ArrayList<JsonProjectWrapper>();
		for (String profilename : repository.keySet()) {
			JsonProjectWrapper _tmp = this.findProjectByName(profilename);
			_res.add(_tmp);
		}
		return _res;
	}

	public void setUrlDbPath(String _dbPath) {
		// TODO Auto-generated method stub
		DAOProject.urlDbPath=_dbPath;
		System.out.println(DAOProject.urlDbPath);
	}

	public TreeMap<String, Path> getRepository() {
		if(DAOProject.repository == null){
			DAOProject.repository = new TreeMap<String, Path>();
		}
		return DAOProject.repository;
	}

	/** 
	 * @see com.services.projects.utils.DBManager#getDBPath()
	 */
	public String getDBPath() {
		// TODO Auto-generated method stub
		return DAOProject.DB_PATH;
	}


	

}
