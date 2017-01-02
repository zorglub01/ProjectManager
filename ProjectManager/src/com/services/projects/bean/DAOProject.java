/**
 * 
 */
package com.services.projects.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.services.credentials.profiles.UserProfile;
import com.services.projects.model.Project;
import com.services.projects.utils.DBManager;
import com.services.projects.utils.ModelManagerHelper;

/**
 * @author thomas
 *
 */
public class DAOProject implements DBManager {
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
			urlDbPath = initUrlDbPath(rootCtxPath, DB_PATH);
			repository = new TreeMap<String, Path>();
			System.out.println(urlDbPath);
			Path folder = Paths.get(urlDbPath);
			@SuppressWarnings("unchecked")
			Collection<File> _files = FileUtils.listFiles(folder.toFile(), TrueFileFilter.INSTANCE,
					TrueFileFilter.INSTANCE);
			for (File file : _files) {
				System.out.println(file.getPath());
				String _name = FilenameUtils.getBaseName(file.getName());
				String _ext = FilenameUtils.getExtension(file.getName());
				if (_ext.equalsIgnoreCase("xml") && _name.startsWith(HTTP_UPLOAD_PARAM_VAL + "_")) {
					String[] _tmp = _name.split("_");
					repository.put(_tmp[1], Paths.get(file.toURI()));
				}

			}

		}
		return instance;
	}

	/**
	 * @param rootCtxPath
	 */
	private static String initUrlDbPath(String rootCtxPath, String dbPathREl) {
		String _res = null;
		String _endSep = "";
		if (!rootCtxPath.endsWith("/"))
			_endSep = "/";
		_res = rootCtxPath + _endSep + DB_PATH;
		return _res;
	}

	public static DAOProject getInstance() {
		return instance;
	}

	public void saveProjectList(List<JsonProjectWrapper> _projectList) {
		for (JsonProjectWrapper _project : _projectList) {
			try {
				this.saveProject(_project);
			} catch (JAXBException e) {
				System.out.println("Project saved Failed:" + _project.getShortName());
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

	/**
	 * Return a JsonWrapperProject
	 * The search is based on the shortName of the project
	 * @param _name
	 * @return JsonWrapperProject or null if not existing or not registered
	 * @throws JAXBException
	 */
	public JsonProjectWrapper findProjectByName(String _name) {
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
		if(this.isProjectExist(_project.getShortName())){
			_tmpProject = this.findProjectByName(_project.getShortName());					
		}else{
			_tmpProject = this.findProjectByName(PROJECT_DEFAULT);
			String _shortName = this.getGeneratedShortName(_project);
			_tmpProject.setShortName(_shortName);
			_tmpProject.setKeyId(_shortName);
		}
		_tmpProject.setName(_project.getName());
		_tmpProject.setDescription(_project.getDescription());
		_tmpProject.getFirstSprint().setStartDate(_project.getStartDate());
		_tmpProject.getLastSprint().setEndDate(_project.getEndDate());
		
		Path _fileToSave = ModelManagerHelper.getFilePath(_tmpProject, this);
		ModelManagerHelper.<Project>saveModel(_tmpProject.getProject(),_fileToSave.toFile()); 
		repository.put(_tmpProject.getShortName(), _fileToSave);
		System.out.println(_fileToSave);

	}

	private String getGeneratedShortName(JsonProjectWrapper _project) {
		String _res=null;
		_res = _project.getName();
		_res = _res.replaceAll(" ", "");
		_res = _res.replaceAll("_", "");
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
		this.saveProject(_pTmp);
		Files.deleteIfExists(_fileTmp);
	}

	public <T> List<T> getRegisteredObjects(Class<T> t) {
		ArrayList<JsonProjectWrapper> _res = new ArrayList<JsonProjectWrapper>();
		for (String profilename : repository.keySet()) {
			JsonProjectWrapper _tmp = this.findProjectByName(profilename);
			_res.add(_tmp);
		}
		return (List<T>) _res;
	}

	

}
