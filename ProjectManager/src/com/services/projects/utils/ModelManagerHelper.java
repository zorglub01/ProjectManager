/**
 * 
 */
package com.services.projects.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.JsonJtableWrapper;
import com.services.projects.model.Profile;
import com.services.projects.model.Project;
import com.services.projects.model.Task;

/**
 * @author thomas.foret
 *
 */
public class ModelManagerHelper{
	
	public static String getFileScanAsJson(Path _rootDir) throws JsonProcessingException{
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
	
	
	public static String getJsonStream(Object _object) throws JsonProcessingException{

		ObjectMapper mymapper = new ObjectMapper();
		mymapper.setVisibility(PropertyAccessor.GETTER, Visibility.PROTECTED_AND_PUBLIC);
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
		URL taskXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/PMOTask.xml");
		URL profileXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/Profile.xml");
		URL pmoXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/PMOProject1.xml");
		
		testProject(pmoXmlUrl);
		
		testTask(taskXmlUrl);
		
		testProfile(profileXmlUrl);
		
		testFileScan();
		
	}

	private static void testFileScan() {
		URL taskXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/");
		try {
			Path _rootPath = Paths.get(taskXmlUrl.toURI());
			String _res = getFileScanAsJson(_rootPath);
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
	
	
	

}
