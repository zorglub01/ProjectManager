/**
 * 
 */
package com.services.projects.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.services.JsonJtableWrapper;
import com.services.projects.bean.JsonProjectWrapper;
import com.services.projects.model.Profile;
import com.services.projects.model.Project;
import com.services.projects.model.Task;
import com.services.projects.model.User;

/**
 * @author thomas.foret
 *
 */
public class ModelManagerHelper{
	
	
	/**
	 * return a path for the xml file denoted by the DBobject passed as param and 
	 * the DBManager or DAO in charge of the object.
	 * The path is : DBManager.getUrlDbPath()/DBObject.getPrefixPathName()"_"DBObject.getPrimaryKeyId()"."DBObject.getExtensionPathName()
	 * 
	 * @param _dbobject
	 * @param _dbmgr
	 * @return
	 */
	public static Path getFilePath(DBObject _dbobject, DBManager _dbmgr){
		String filename = _dbmgr.getPrefixPathName()+"_"+_dbobject.getPrimaryKeyId();
		String extension = _dbmgr.getExtensionPathName();
		String _dbpath = _dbmgr.getUrlDbPath();
		String _tmpFilePath = _dbpath + "/"+filename + "."+extension;
		Path filePath = Paths.get(_tmpFilePath);
		return filePath;
	}
	
	public static String getFileScanAsJson(DBManager _dbMgr) throws JsonProcessingException{
		Path folder = Paths.get(_dbMgr.getUrlDbPath());
		String _res = ModelManagerHelper.getFileScanAsJson(folder);
		return _res;
	}
	
	
	public static String getFileScanAsJson(Path _rootDir) throws JsonProcessingException{
		@SuppressWarnings("unchecked")
		Collection<File> _files = FileUtils.listFiles(_rootDir.toFile(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		ArrayList<JsonFileWrapper> _list = new ArrayList<JsonFileWrapper>();
		for(File _file: _files){
			JsonFileWrapper _wrapper = new JsonFileWrapper(_file);
			_list.add(_wrapper);
		}
		ObjectMapper mymapper = new ObjectMapper();
		mymapper.setVisibility(PropertyAccessor.GETTER, Visibility.PUBLIC_ONLY);
		String jsonInString = mymapper.writeValueAsString(_list);
		System.out.println(jsonInString);
		return jsonInString ;		
	}
	
	public static <T> T getObjectFromJson(String jsonInString,Class<T> clazz) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mymapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		mymapper.setDateFormat(df);
		mymapper.setVisibility(PropertyAccessor.GETTER, Visibility.PROTECTED_AND_PUBLIC);
		
		T _res = (T)mymapper.readValue(jsonInString, clazz);
		return _res;
	}
	
	
	public static <T> List<T> getObjectListFromJson(String jsonInString,Class<T> clazz) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mymapper = new ObjectMapper();
		mymapper.setVisibility(PropertyAccessor.GETTER, Visibility.PROTECTED_AND_PUBLIC);
		mymapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		com.fasterxml.jackson.databind.type.CollectionType _colType = mymapper.getTypeFactory().constructCollectionType(List.class, clazz);
		List<T> _res = mymapper.readValue(jsonInString, _colType);
		return _res;
	}
	
	
	
	public static String getJsonStream(Object _object) throws JsonProcessingException{

		ObjectMapper mymapper = new ObjectMapper();
		mymapper.setVisibility(PropertyAccessor.GETTER, Visibility.PROTECTED_AND_PUBLIC);
		mymapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mymapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); 
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		mymapper.setDateFormat(df);
		String jsonInString = mymapper.writeValueAsString(_object);
		System.out.println(jsonInString);
		return jsonInString ;
	}
	
	
	
	
	
	public static <T> void saveModel(T t) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(t.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(t,System.out);		
	}
	
	public static <T> void saveModel(T t,File _file ) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(t.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(t,_file);
		System.out.println(ModelManagerHelper.class.getName() + " saveModel into File:"+_file.getAbsolutePath());
	}
	
	
	
	public static <T> T loadModel(InputStream _xmlFile, Class<T> clazz) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		
		@SuppressWarnings("unchecked")
		T _res = (T)jaxbUnMarshaller.unmarshal(_xmlFile);
		return _res ;
		
	}
	
	
	public static <T> T loadModel(File _xmlFile, Class<T> clazz) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		
		@SuppressWarnings("unchecked")
		T _res = (T)jaxbUnMarshaller.unmarshal(_xmlFile);
		return _res ;
		
	}
	
	
	
	

	public static void main(String args[]){
		URL taskXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/PMOTask1.xml");
		URL profileXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/Profile.xml");
		URL pmoXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/project_Default.xml");
		URL userXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/user_Default.xml");
		
		testProject(pmoXmlUrl);
		
		testTask(taskXmlUrl);
		
		testProfile(profileXmlUrl);
		
		testUser(userXmlUrl);
		
		
		testFileScan();
		
		testJsonMessage();
		
		testDateOperation();
	}
	
	

	private static void testDateOperation() {
		LocalDate _sDate = LocalDate.now();
		System.out.println("_SDate : " + _sDate );
		LocalDate _eDate = _sDate .plusDays(0);
		System.out.println("_SDate + 0: " + _eDate );
		_eDate = _sDate .plusDays(1);
		System.out.println("_SDate + 1: " + _eDate );
		_eDate = _sDate .plusDays(20);
		System.out.println("_SDate + 20: " + _eDate );
		
	}

	private static void testJsonMessage(){
		System.out.println("TESTING JSON ARRAY to OBJECT");
		
		URL resource = ClassLoader.getSystemResource("com/lang/util/msg.properties");
		Properties _prop  = new Properties();
		
		try {
			_prop.load(resource.openStream());
			List<JsonProjectWrapper> _res = getObjectListFromJson(_prop.getProperty("project_list"),JsonProjectWrapper.class);
			for (JsonProjectWrapper jsonProjectWrapper : _res) {
				System.out.println(jsonProjectWrapper.getName());
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

	private static void testFileScan() {
		URL taskXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/");
		try {
			Path _rootPath = Paths.get(taskXmlUrl.toURI());
			String _res = getFileScanAsJson(_rootPath);
			System.out.println(_res);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @param pmoXmlUrl
	 */
	private static void testUser(URL userXmlUrl) {
		try {
			File _testFile2 = new File(userXmlUrl.toURI());									
			User _res2 = ModelManagerHelper.<User>loadModel(_testFile2, User.class);
			System.out.println(_res2);			
			ModelManagerHelper.<User>saveModel(_res2);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * @param pmoXmlUrl
	 */
	private static void testProject(URL pmoXmlUrl) {
		try {
			File _testFile2 = new File(pmoXmlUrl.toURI());									
			Project _res2 = ModelManagerHelper.<Project>loadModel(_testFile2, Project.class);
			System.out.println(_res2);			
			ModelManagerHelper.<Project>saveModel(_res2);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param taskXmlUrl
	 */
	private static void testTask(URL taskXmlUrl) {
		try {
			File _testFile = new File(taskXmlUrl.toURI());									
			Task _res = ModelManagerHelper.<Task>loadModel(_testFile, Task.class);
			System.out.println(_res);			
			ModelManagerHelper.<Task>saveModel(_res);
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param profileXmlUrl
	 */
	private static void testProfile(URL profileXmlUrl) {
		try {
			
			File _testFile1 = new File(profileXmlUrl.toURI());									
			Profile _res1 = ModelManagerHelper.<Profile>loadModel(_testFile1, Profile.class);
			System.out.println(_res1);			
			ModelManagerHelper.<Profile>saveModel(_res1);
			
			getJsonStream(_res1);
			
			
			JsonJtableWrapper _jsonReturn = new JsonJtableWrapper();
			_jsonReturn.getRecords().add(_res1);
			_jsonReturn.getRecords().add(_res1);
			_jsonReturn.setResult("OK");
			
			getJsonStream(_jsonReturn);
			
			
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static LocalDate convertToLocalDate(XMLGregorianCalendar _date) {
		LocalDate _res=null;
		if(_date != null ){
			_res = LocalDate.of(_date.getYear(),_date.getMonth(), _date.getDay());
		}
		return _res;
	}

	public static XMLGregorianCalendar convertToXmlGregorianCalendar(LocalDate _date){
		XMLGregorianCalendar _res=null;
		try {
			DatatypeFactory df = DatatypeFactory.newInstance();
			_res = df.newXMLGregorianCalendarDate(_date.getYear(), _date.getMonthValue(), _date.getDayOfMonth(), DatatypeConstants.FIELD_UNDEFINED);
	    } catch (DatatypeConfigurationException dce) {
	        throw new IllegalStateException(
	            "Exception while obtaining DatatypeFactory instance", dce);
	    }
		
		return _res;
	}
	
	public static XMLGregorianCalendar formatXMLGregorianCalendar(XMLGregorianCalendar _date){
		XMLGregorianCalendar _res=null;
		try {
			DatatypeFactory df = DatatypeFactory.newInstance();
			_res = df.newXMLGregorianCalendarDate(_date.getYear(), _date.getMonth(), _date.getDay(), DatatypeConstants.FIELD_UNDEFINED);
	    } catch (DatatypeConfigurationException dce) {
	        throw new IllegalStateException(
	            "Exception while obtaining DatatypeFactory instance", dce);
	    }
		
		return _res;
	}
	
	
	
	public static JsonNode convertAsJsonNode(String _jsonString){
		JsonNode _res=null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			_res = mapper.readTree(_jsonString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _res;
	}

	/**
	 * @param rootCtxPath
	 */
	public static String initUrlDbPath(String rootCtxPath, String dbPathREl) {
		String _res = null;
		String _endSep = "";
		if (!rootCtxPath.endsWith("/"))
			_endSep = "/";
		_res = rootCtxPath + _endSep + dbPathREl;
		return _res;
	}

	/**
	 * @param rootCtxPath
	 */
	public static DBManager initDaoInstance(String rootCtxPath, DBManager instance) {
		String _dbPath = initUrlDbPath(rootCtxPath, instance.getDBPath());
		instance.setUrlDbPath(_dbPath);
		Path folder = Paths.get(instance.getUrlDbPath());
		@SuppressWarnings("unchecked")
		Collection<File> _files = FileUtils.listFiles(folder.toFile(), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
		for (File file : _files) {
			System.out.println(file.getPath());
			String _name = FilenameUtils.getBaseName(file.getName());
			String _ext = FilenameUtils.getExtension(file.getName());
			if (_ext.equalsIgnoreCase("xml") && _name.startsWith(instance.getHTTP_UPLOAD_PARAM_VAL()+ "_")) {
				String[] _tmp = _name.split("_");
				instance.getRepository().put(_tmp[1], Paths.get(file.toURI()));
			}
	
		}
		return instance;
	}
	
	

}
