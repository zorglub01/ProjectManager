/**
 * 
 */
package com.services.projects.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeConstants.Field;
import javax.xml.namespace.QName;
import javax.xml.datatype.DatatypeFactory;

import com.services.projects.model.Sprint;
import com.services.projects.model.Task;

/**
 * @author thomas
 *
 */
public class ProjectService {
	
	public static List<String> checkSprintCoherency(JsonProjectWrapper _project){
		ArrayList<String> _res=new ArrayList<String>();
		XMLGregorianCalendar _projDateEnd = _project.getEndDate();
		XMLGregorianCalendar _projDateStart = _project.getStartDate();
		
		if( _projDateEnd.compare(_projDateStart) == DatatypeConstants.LESSER){
			String _msg = "[PROJECT:" + _project.getPrimaryKeyId()+"]"+"[ERROR:End date must be greater then the start date]";
			String _msg0 = "[PROJECT:" + _project.getPrimaryKeyId()+"]"+"[ACTION:Check Sprint dates]";
			_res.add(_msg);
		}
		List<Sprint> _sprintList = _project.getProject().getPhases().getPhase();
		int _nbSprint=0;
		for( Sprint _sprint : _sprintList){
			XMLGregorianCalendar _sDateStart = _sprint.getStartDate();
			XMLGregorianCalendar _sDateEnd = _sprint.getEndDate();
			if( _sDateEnd.compare(_sDateStart)==DatatypeConstants.LESSER){
				String _msg1 = "[SPRINT:"+_sprint.getName()+"]" + "[ERROR: Sprint dates are not valid]";
				String _msg2 = "[SPRINT:"+_sprint.getName()+"]" + "[ACTION: Auto correction applied]";
				_res.add(_msg1);
				_res.add(_msg2);
				LocalDate _sDate = LocalDate.of(_sDateStart.getYear(),_sDateStart.getMonth(), _sDateStart.getDay());
				LocalDate _eDate = _sDate.with(TemporalAdjusters.firstDayOfNextMonth());
				_sprint.setEndDate(convertToXmlGregorianCalendar(_eDate) );
			}
			List<Task> _taskList = _sprint.getTaskList().getTask();
			int _nbTask=0;
			for( Task _task : _taskList){
				XMLGregorianCalendar _tDateStart = _task.getMetric().getEstimate().getStartDate();
				XMLGregorianCalendar _tDateEnd = _task.getMetric().getEstimate().getEndDate();
				
				if(_tDateStart.compare(_sDateStart)==DatatypeConstants.LESSER){
					String _msg1 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ERROR: start dates is lesser than Sprint Start Date]";
					String _msg2 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ACTION: Auto correction applied]";
					_res.add(_msg1);
					_res.add(_msg2);
					_task.getMetric().getEstimate().setStartDate(_sDateStart);
					_tDateStart=_sDateStart;
				}
				
				if(_tDateStart.compare(_sDateEnd)==DatatypeConstants.GREATER){
					String _msg1 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ERROR: start dates is greater than Sprint End Date]";
					String _msg2 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ACTION: Auto correction applied]";
					_res.add(_msg1);
					_res.add(_msg2);
					_task.getMetric().getEstimate().setStartDate(_sDateStart);
					_tDateStart=_sDateEnd;
				}
				
				
				if(_sDateEnd.compare(_tDateEnd)==DatatypeConstants.LESSER){
					String _msg1 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ERROR: end date is greater than Sprint one]";
					String _msg2 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ACTION: Auto correction applied]";
					_res.add(_msg1);
					_res.add(_msg2);
					_task.getMetric().getEstimate().setEndDate(_sDateEnd);
					_tDateEnd = _sDateEnd;
				}
								
				if( _tDateEnd.compare(_tDateStart)==DatatypeConstants.LESSER){
					String _msg1 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ERROR: Task dates are not valid]";
					String _msg2 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ACTION: Auto correction applied]";
					_res.add(_msg1);
					_res.add(_msg2);
					LocalDate _sDate = convertToLocalDate(_tDateStart);
					Integer _wlSup = _task.getMetric().getEstimate().getWlSup();
					int daysToAdd = Integer.divideUnsigned(_wlSup.intValue(), 8);
					LocalDate _eDate = _sDate.plusDays(daysToAdd);
					_task.getMetric().getEstimate().setEndDate(convertToXmlGregorianCalendar(_eDate));
				}
				
				_nbTask++;
			}
			
			_nbSprint++;
		}
		
		return _res;
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
			GregorianCalendar gc = new GregorianCalendar(_date.getYear(),_date.getMonthValue(),_date.getDayOfMonth());
			_res = df.newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException(
                "Exception while obtaining DatatypeFactory instance", dce);
        }
		
		return _res;
	}
	
	
	

}
