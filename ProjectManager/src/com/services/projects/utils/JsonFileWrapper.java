/**
 * 
 */
package com.services.projects.utils;

import java.io.File;

/**
 * @author thomas
 *
 */
public class JsonFileWrapper {
	private File localFile;
	private String hrefLocation;

	/**
	 * @return the hrefLocation
	 */
	public String getHrefLocation() {
		return hrefLocation;
	}

	/**
	 * @param hrefLocation the hrefLocation to set
	 */
	public void setHrefLocation(String hrefLocation) {
		this.hrefLocation = hrefLocation;
	}

	public JsonFileWrapper(File _file) {
		this.setLocalFile(_file);
	}

	/**
	 * @return the localFile
	 */
	public File getLocalFile() {
		return this.localFile;
	}

	/**
	 * @param localFile the localFile to set
	 */
	public void setLocalFile(File localFile) {
		this.localFile = localFile;
		if(localFile !=null){
			this.hrefLocation="/ProjectManager/DB/";
			this.hrefLocation+=localFile.getParentFile().getName()+"/"+localFile.getName();
		}
	}
	
	
	

}
