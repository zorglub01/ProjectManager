package com.services.credentials;

public class ResourceEntry {
	
	public ResourceEntry(String path, String desc) {
		super();
		this.path = path;
		this.desc = desc;
	}
	private String path;
	private String desc;
	/**
	 * @return the path
	 */
	String getPath() {
		return this.path;
	}
	/**
	 * @param path the path to set
	 */
	void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the desc
	 */
	String getDesc() {
		return this.desc;
	}
	/**
	 * @param desc the desc to set
	 */
	void setDesc(String desc) {
		this.desc = desc;
	}
	

}
