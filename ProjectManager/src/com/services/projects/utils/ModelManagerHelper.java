/**
 * 
 */
package com.services.projects.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.services.projects.model.Profile;
import com.services.projects.model.Project;
import com.services.projects.model.Task;

/**
 * @author thomas.foret
 *
 */
public class ModelManagerHelper{
	
	public static <T> void saveModel(T t) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(t.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(t,System.out);
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
		
		try {
			
			File _testFile1 = new File(profileXmlUrl.toURI());									
			Profile _res1 = ModelManagerHelper.<Profile>loadModel(_testFile1, Profile.class);
			System.out.println(_res1);			
			ModelManagerHelper.<Profile>saveModel(_res1);
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	

}
