/**
 * 
 */
package com.services.credentials.profiles;

import java.io.ByteArrayOutputStream;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.services.projects.model.Credentials;
import com.services.projects.model.Profile;
import com.services.projects.model.ProfileURI;
import com.services.projects.model.Right;
import com.services.projects.utils.ModelManagerHelper;

/**
 * @author thomas
 *
 */
public class DAOProfile {
	private static final String PROFILE_DEFAULT = "Default";
	private static DAOProfile instance=null;
	/**
	 * Static absolute file path where the xml files are stored
	 */
	private static String urlDbPath;
	/**
	 * Static relative DB dir location
	 */
	private static String DB_PATH="DB/profiles";
	private static TreeMap<String, Path> repository=null;
	private ArrayList<String> mapParamList= new ArrayList<String>();
	private static String HTTP_UPLOAD_PARAM_VAL="profile";
	
	
	private DAOProfile() {
		this.mapParamList.add("description");
		this.mapParamList.add("name");
	}
	
	/**
	 * @return the mapParamList
	 */
	private synchronized ArrayList<String> getMapParamList() {
		return mapParamList;
	}

	/**
	 * Initial the DAO based on the root DB directory folder
	 * @param rootCtxPath
	 * @return
	 */
	public static DAOProfile getInstance(String rootCtxPath){
		
		if(instance != null){
			return instance;
		}else{
			instance = new DAOProfile();
			String _endSep="";
			if(!rootCtxPath.endsWith("/"))_endSep="/";
			urlDbPath=rootCtxPath+_endSep+DB_PATH;
			repository = new TreeMap<String, Path>();
			System.out.println(urlDbPath);
			Path folder = Paths.get(urlDbPath);
			Collection<File> _files = FileUtils.listFiles(folder.toFile(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			for (File file : _files) {
				System.out.println(file.getPath());
				String _name  = FilenameUtils.getBaseName(file.getName());
				String _ext  = FilenameUtils.getExtension(file.getName());
				if(_ext.equalsIgnoreCase("xml") && _name.startsWith("profile_")){
					String[] _tmp = _name.split("_"); 
					repository.put(_tmp[1], Paths.get(file.toURI()));
				}
				
			}
			
		}
		return 	instance;		
	}
	
	public String scanDbFile() throws JsonProcessingException{
		Path folder = Paths.get(urlDbPath);
		String _res = ModelManagerHelper.getFileScanAsJson(folder);
		return _res;
	}
	
	
	public static DAOProfile getInstance(){
		return instance;
	}
	
	public List<UserProfile> getRegisterProfiles(){
		ArrayList<UserProfile> _res = new ArrayList<UserProfile>();
		for (String profilename : repository.keySet()) {
			UserProfile _tmp = findProfileByName(profilename);
			_res.add(_tmp);
		}
		return _res;
	}

	public UserProfile findProfileByName(String name) {		
		Path _filepath = repository.get(name);
		UserProfile _res1=null;
		try {
			Profile _res = ModelManagerHelper.<Profile>loadModel(_filepath.toFile(), Profile.class);
			_res1 = new UserProfile();
			_res1.setProfile(_res);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		return _res1;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public boolean isExistingProfile(String name){
		boolean _res=false;
		UserProfile _existingProfile = this.findProfileByName(name);
		if(_existingProfile != null) _res=true;
		return _res;
	}

	public UserProfile create(Map<String, String[]> parameterMap) throws JAXBException{
		
		UserProfile _res=null;
		
		String[] _desc = parameterMap.get("description");
		String[] _name = parameterMap.get("name");
		if(!this.isExistingProfile(_name[0])){
			_res = this.findProfileByName(PROFILE_DEFAULT);
			_res.getProfile().setDescription(_desc[0]);
			_res.getProfile().setName(_name[0]);
			Path filePath = getFilePath(_res);
			try {
				_res.getProfile().setKeyId(GetHexaKey(filePath));
				this.saveProfile(_res,filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			_res = this.findProfileByName(_name[0]);
		}
		
		return _res;
	}
	
	private static String GetHexaKey(Path _path) throws IOException{
		String _res=null;
		char[] _res1 = Hex.encodeHex(_path.toFile().getName().getBytes());
		_res = new String(_res1);
		//_res = compress(_path.toString());
		return _res;
	}
	
	 private static String compress(String str) throws IOException {
	        if (str == null || str.length() == 0) {
	            return str;
	        }
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        GZIPOutputStream gzip = new GZIPOutputStream(out);
	        gzip.write(str.getBytes());
	        gzip.close();
	        return out.toString("ISO-8859-1");
	    }
	
	private static Path getFilePath(UserProfile usrProfile){
		String filename = "profile_"+usrProfile.getProfileName();
		String extension = "xml";
		String _tmpFilePath = urlDbPath + "/"+filename + "."+extension;
		Path filePath = Paths.get(_tmpFilePath);
		return filePath;
	}

	private void saveProfile(UserProfile _res, Path  filePathToSave) throws IOException, JAXBException {	
		Path file=null;
		
		
		if( filePathToSave == null){
			System.out.println("Update mode:" + _res.getProfileName());
			file = repository.get(_res.getProfileName());
		}else{
			System.out.println("Create mode" + _res.getProfileName());
			try{
				file = Files.createFile(filePathToSave);
			}catch(Exception e){
				System.out.println("Error on Create mode");
				IOException _ex = new IOException("Create mode failde : " + filePathToSave, e.getCause());
				throw _ex;
			}
		}
		
		ModelManagerHelper.saveModel(_res.getProfile(), file.toFile());
		repository.put(_res.getProfileName(), file);
		
	}

	public UserProfile updateResources(String _profileId, Map<String,String[]> parameterMap) throws IOException, JAXBException {
		UserProfile _res = this.findProfileByName(_profileId);
		String _path = parameterMap.get(UserProfile.HTTP_PARAM_ResourcePath)[0];
		String _desc = parameterMap.get("description")[0];
		String _cred = parameterMap.get("credentials")[0];
		ProfileURI _pUri=null; 
		if (_res.isResourceExist(_path)){
			_pUri = _res.getResourceURI(_path);
		}else{
			_pUri=new ProfileURI();
			_pUri.setPath(_path);
			_pUri.setCredentials(new Credentials());
			_res.addResource(_pUri);
		}
		_pUri.setDescription(_desc);
		_pUri.getCredentials().getRights().clear();
		if(_cred.indexOf("C")>-1){
			_pUri.getCredentials().getRights().add(Right.CREATE);
		}
		if(_cred.indexOf("R")>-1){
			_pUri.getCredentials().getRights().add(Right.READ);
		}
		if(_cred.indexOf("U")>-1){
			_pUri.getCredentials().getRights().add(Right.UPDATE);
		}
		if(_cred.indexOf("D")>-1){
			_pUri.getCredentials().getRights().add(Right.DELETE);
		}
		this.saveProfile(_res, null);
		return _res;
	}

	public void deleteResources(String _profileId, Map<String, String[]> parameterMap) throws JAXBException, IOException {
		UserProfile _res = this.findProfileByName(_profileId);
		String _path = parameterMap.get(UserProfile.HTTP_PARAM_ResourcePath)[0];
		ProfileURI _pUri = _res.getResourceURI(_path);
		System.out.println("Delete mode:" +_pUri.getPath() );
		ProfileURI _res1 = _res.removeResources(_pUri);
		if(_res1 == null){
			JAXBException _ex = new JAXBException("ERROR WHILE REMOVING " + _path);
			throw _ex;
		}
		this.saveProfile(_res,null);
		
	}

	/**
	 * @return the hTTP_UPLOAD_PARAM_VAL
	 */
	public static String getHTTP_UPLOAD_PARAM_VAL() {
		return HTTP_UPLOAD_PARAM_VAL;
	}

	public void importFile(InputStream stream, String filename) throws IOException, JAXBException {
		
		String extension = "tmp";
		String _pathToSave = urlDbPath ;
		Path _path = Paths.get(_pathToSave);
		Path _fileTmp = Files.createTempFile(_path, filename + "_", "." + extension);
		Files.copy(stream, _fileTmp, StandardCopyOption.REPLACE_EXISTING);
		Profile _res = ModelManagerHelper.<Profile>loadModel(_fileTmp.toFile(), Profile.class);
		UserProfile _pTmp = this.findProfileByName(_res.getName());
		if( _pTmp != null ){
			_pTmp.setProfile(_res);
			this.saveProfile(_pTmp, null);
		}else{
			UserProfile _upTmp = this.findProfileByName(PROFILE_DEFAULT); 
			_upTmp.setProfile(_res);
			Path _newFilePath = DAOProfile.getFilePath(_upTmp);
			this.saveProfile(_upTmp, _newFilePath);
		}
		Files.deleteIfExists(_fileTmp);
	}

	
	
}
