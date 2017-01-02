/**
 * 
 */
package com.services.projects.bean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.services.projects.model.Phases;
import com.services.projects.model.Project;
import com.services.projects.model.Sprint;
import com.services.projects.utils.DBObject;

/**
 * @author thomas
 *
 */
public class JsonProjectWrapper implements DBObject {
	
	private Project oProject;
	
	private XMLGregorianCalendar startDate;
	
	private XMLGregorianCalendar endDate;
	
	private int nbPhase=0;
	
	Project getProject(){
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
		return this.getProject().getShortName();
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
	
	public void setNbPhase(int _nbPhase){
		this.nbPhase = _nbPhase;
	}

	public String getPrimaryKeyId() {
		return this.getShortName();
	}

	public void setProject(Project _res) {
		this.oProject = _res;
		
	}

	/**
	 * @return the startDate
	 */
	
	public XMLGregorianCalendar getStartDate() {
		Sprint _fSprint = this.getFirstSprint();
		if(_fSprint !=null) this.startDate = _fSprint.getStartDate();
		return this.startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(XMLGregorianCalendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	
	public XMLGregorianCalendar getEndDate() {
		Sprint _fSprint = this.getLastSprint();
		if( _fSprint !=null)
			this.endDate = _fSprint.getEndDate();
		return this.endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(XMLGregorianCalendar endDate) {
		this.endDate = endDate;
	}
	
	
	Sprint getFirstSprint(){
		Phases _p = this.getProject().getPhases();
		Sprint _res = null;
		if(_p != null){
			List<Sprint> _sP = _p.getPhase();
			Collections.sort(_sP, new SprintStartDateSorter());
			_res = _sP.get(0);
		}
		return _res;
	}
	
	
	Sprint getLastSprint(){
		Phases _p = this.getProject().getPhases();
		Sprint _res = null;
		if(_p != null){
			List<Sprint> _sP = _p.getPhase();
			Collections.sort(_sP, new SprintEndDateSorter());
			_res = _sP.get(_sP.size()-1);
		}		
		return _res;
	}
	


}

class SprintStartDateSorter implements Comparator<Sprint>{

	public int compare(Sprint o1, Sprint o2) {
		long s1SD = o1.getStartDate().toGregorianCalendar().getTimeInMillis();
		long s2SD = o2.getStartDate().toGregorianCalendar().getTimeInMillis();
		int _res = 0;
		if(s1SD < s2SD) _res = -1;
		if(s1SD > s2SD) _res = 1;
		return _res;
	}
	
}


class SprintEndDateSorter implements Comparator<Sprint>{

	public int compare(Sprint o1, Sprint o2) {
		long s1SD = o1.getEndDate().toGregorianCalendar().getTimeInMillis();
		long s2SD = o2.getEndDate().toGregorianCalendar().getTimeInMillis();
		int _res = 0;
		if(s1SD < s2SD) _res = -1;
		if(s1SD > s2SD) _res = 1;
		return _res;
	}
	
}




