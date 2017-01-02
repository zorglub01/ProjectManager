/**
 * 
 */
package com.services.projects.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * All the DAO must implement this interface
 * @author thomas
 *
 */
public interface DBManager {

	/**
	 * @return prefix of each xml file used to persiste an DBObject
	 */
	public String getPrefixPathName();

	/**
	 * @return the full path of the directory where the xml files are stored
	 */
	public String getUrlDbPath();

	/**
	 * @return the extension file used to denote the xml files 
	 */
	public String getExtensionPathName();
	
	/**
	 * The feature allow to export the data from any DAO
	 * @return json string describing the list of files located in the getUrlDBPath directory
	 * @throws JsonProcessingException
	 */
	public String scanDbFile() throws JsonProcessingException;

	
	/**
	 * name of the business scope used as param from the UI
	 * @return
	 */
	public String getHTTP_UPLOAD_PARAM_VAL();
	
	/**
	 * The DAO must have an Import entry point
	 * @param stream
	 * @param filename
	 * @throws IOException
	 * @throws JAXBException
	 */
	public void importFile(InputStream stream, String filename) throws IOException, JAXBException;
	
	
	public <T> List<T> getRegisteredObjects(Class<T> t);
	
}
