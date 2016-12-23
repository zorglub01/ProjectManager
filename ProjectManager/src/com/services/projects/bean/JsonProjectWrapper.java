/**
 * 
 */
package com.services.projects.bean;

import com.services.projects.model.Phases;
import com.services.projects.model.Project;
import com.services.projects.model.Sprint;

/**
 * @author thomas
 *
 */
public class JsonProjectWrapper {
	
	private Project oProject;
	
	private Project getProject(){
		return this.oProject;
	}
	
	public JsonProjectWrapper(){
		this.oProject = new Project();
	}
	
	public JsonProjectWrapper( Project _project){
		this.oProject=_project;
	}
	
	public String getKeyId(){
		return this.getProject().getKeyId();
	}
	
	public void setKeyId(String _val){
		this.getProject().setKeyId(_val);
	}
	
	public String getName(){
		return this.getProject().getName();
	}

	public void setName(String _val){
		this.getProject().setName(_val);
	}

	
	public String getShortName(){
		return this.getProject().getName();
	}
	

	public void setShortName(String _val){
		this.getProject().setShortName(_val);
	}
	
	public String getDescription(){
		return this.getProject().getDescription();
	}
	
	public void setDescription(String _val){
		this.getProject().setDescription(_val);
	}
	
	public Integer getNbPhase(){
		Phases _phase = this.getProject().getPhases();
		if(_phase == null) {
			Phases _firstone = new Phases();
			this.getProject().setPhases(_firstone);
			}
		int _size = this.getProject().getPhases().getPhase().size();
		return new Integer(_size);
	}


}
