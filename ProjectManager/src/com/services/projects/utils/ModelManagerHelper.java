/**
 * 
 */
package com.services.projects.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.services.projects.model.Task;

/**
 * @author thomas.foret
 *
 */
public class TaskManagerHelper {
	
	
	public static void main(String args[]){
		URL commonXmlUrl = ClassLoader.getSystemResource("com/services/projects/model/PMOTask.xml");
		
		try {
			File _testFile = new File(commonXmlUrl.toURI());
			Task _res = getInstance(_testFile);
			System.out.println(_res);
			
			saveInstance(_res);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Task getInstance(File _xmlFile) throws JAXBException{
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		
		Task _res = (Task)jaxbUnMarshaller.unmarshal(_xmlFile);
		return _res ;
	}
	
	public static void saveInstance(Task instance) throws JAXBException{
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(instance,System.out);
		
	}
	
	
	

}
