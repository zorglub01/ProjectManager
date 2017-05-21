package com.services.credentials;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.services.projects.model.Project;
import com.services.projects.model.User;
import com.services.projects.utils.DBManager;
import com.services.projects.utils.ModelManagerHelper;

public class DAOUser implements DBManager<UserWrapper> {
	
	private static final String USER_DEFAULT = "Default";
	private static final String HTTP_UPLOAD_PARAM_VAL = "user";
	
	/**
	 * Static absolute file path where the xml files are stored
	 */
	private static String urlDbPath;
	/**
	 * Static relative DB dir location
	 */
	private static String DB_PATH = "DB/users";
	private static TreeMap<String, Path> repository = null;
	private static DAOUser instance = null;
	

	private DAOUser(){
		
	}
	public String getPrefixPathName() {
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

	public String scanDbFile() throws JsonProcessingException {
		String _res = ModelManagerHelper.getFileScanAsJson(this);
		return _res;
	}

	public String getHTTP_UPLOAD_PARAM_VAL() {
		return HTTP_UPLOAD_PARAM_VAL;
	}
	
	
	public String saveUser(UserWrapper _usrW){
		String _res=null;
		Path _fileToSave =null;
		try {
			if( !this.isUserExist(_usrW)){
				String _keyId = this.generatePrimaryKeyId(_usrW);
				_usrW.setPrimaryKeyId(_keyId);
			}
			_fileToSave = ModelManagerHelper.getFilePath(_usrW, this);
			ModelManagerHelper.<User>saveModel(_usrW.getUser(),_fileToSave.toFile());
			repository.put(_usrW.getPrimaryKeyId(), _fileToSave);
			System.out.println(_fileToSave);
		} catch (JAXBException e) {
			e.printStackTrace();
			_res="ERROR";
		} 
		
		return _res;
	}
	

	private String generatePrimaryKeyId(final UserWrapper _usrW) {
		String _res="";
		String _login = _usrW.getUser().getFirstName();
		_res = _login.replaceAll(" ",".");
		_res = _res.toLowerCase(Locale.ROOT);
		_usrW.setPrimaryKeyId(_res);
		int i=0;
		while(this.isUserExist(_usrW)){
			_res += Integer.toString(i);
			i++;
		}
		return _res;
	}
	public void importFile(InputStream stream, String filename) throws IOException, JAXBException {
		String extension = "tmp";
		Path _path = Paths.get(this.getUrlDbPath());
		Path _fileTmp = Files.createTempFile(_path, filename + "_", "." + extension);
		Files.copy(stream, _fileTmp, StandardCopyOption.REPLACE_EXISTING);
		User _res = ModelManagerHelper.<User>loadModel(_fileTmp.toFile(), User.class);
		UserWrapper _usrWrapper = new UserWrapper(_res);
		UserWrapper _pTmp = this.findByPrimaryKey(_usrWrapper.getPrimaryKeyId());
		
		Path _fileToSave = ModelManagerHelper.getFilePath(_pTmp, this);
		ModelManagerHelper.<User>saveModel(_res,_fileToSave.toFile()); 
		repository.put(_usrWrapper.getPrimaryKeyId(), _fileToSave);
		System.out.println(_fileToSave);
		Files.deleteIfExists(_fileTmp);
		
	}
	
	public UserWrapper findByPrimaryKey(UserWrapper _usr){
		return this.findByPrimaryKey(_usr.getPrimaryKeyId());
	}
	
	
	public boolean isUserExist(UserWrapper _usr){
		UserWrapper _res = this.findByPrimaryKey(_usr);
		return (_res !=null);
	}

	private UserWrapper findByPrimaryKey(String primaryKeyId) {
		UserWrapper _res=null;
		Path _uriFile = this.getRepository().get(primaryKeyId);
		if (_uriFile != null && _uriFile.toFile().exists()) {
			User _resTmp;
			try {
				_resTmp = ModelManagerHelper.<User>loadModel(_uriFile.toFile(), User.class);
				_res = new UserWrapper(_resTmp);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return _res;
	}
	/** 
	 * 
	 * @see com.services.projects.utils.DBManager#getRegisteredObjects(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public List<UserWrapper> getRegisteredObjects() {
		List<UserWrapper> _res = new ArrayList<UserWrapper>();
		for (String keyId : repository.keySet()) {
			UserWrapper _tmp = this.findByPrimaryKey(keyId);
			_res.add(_tmp);
		}
		return  _res;
	}

	public static DAOUser getInstance(){
		return getInstance("");
	}
	
	/**
	 * @return the instance
	 */
	public static DAOUser getInstance(String rootCtxPath) {
		if(instance == null){
			instance = new DAOUser();
			instance = (DAOUser)ModelManagerHelper.initDaoInstance(rootCtxPath,instance);
			
		}
		return instance;
	}
	public String getDBPath() {
		return DAOUser.DB_PATH;
	}
	public void setUrlDbPath(String _dbPath) {
		DAOUser.urlDbPath=_dbPath;
		System.out.println(DAOUser.urlDbPath);
		
	}
	public TreeMap<String, Path> getRepository() {
		if(DAOUser.repository == null){
			DAOUser.repository = new TreeMap<String, Path>();
		}
		return DAOUser.repository;
	}

}
