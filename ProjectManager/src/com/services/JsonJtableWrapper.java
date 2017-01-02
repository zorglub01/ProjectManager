/**
 * 
 */
package com.services;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.services.projects.utils.ModelManagerHelper;

/**
 * @author thomas
 *
 */
public class JsonJtableWrapper {
	
	/**
	 * @return the result
	 */
	public String getResult() {
		if(this.result == null || this.result.trim().equalsIgnoreCase("")) this.result="ERROR";
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the records
	 */
	public ArrayList<Object> getRecords() {
		if(this.records == null) this.records = new ArrayList<Object>();
		return records;
	}
	/**
	 * @param records the records to set
	 */
	public void setRecords(ArrayList<Object> records) {
		this.records = records;
	}
	private String result;
	private ArrayList<Object> records;
	private Object record;
	private String errorMessage;
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the record
	 */
	
	public Object getRecord() {
		return this.record;
	}
	/**
	 * @param record the record to set
	 */
	public void setRecord(Object record) {
		this.record = record;
	}
	
	public String toJsonString() throws JsonProcessingException{
		String jsonInString = ModelManagerHelper.getJsonStream(this);
		jsonInString = jsonInString.replaceFirst("result", "Result");
		jsonInString = jsonInString.replaceFirst("records", "Records");
		jsonInString = jsonInString.replaceFirst("record", "Record");
		jsonInString = jsonInString.replaceFirst("errorMessage", "Message");
		System.out.println(jsonInString);
		return jsonInString ;
	}
	
	

}
