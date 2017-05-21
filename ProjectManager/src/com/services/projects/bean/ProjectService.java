/**
 * 
 */
package com.services.projects.bean;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.services.projects.model.Sprint;
import com.services.projects.model.Task;
import com.services.projects.model.Tasks;
import com.services.projects.model.TimeLineType;
import com.services.projects.model.TrackEntriesType;
import com.services.projects.model.TrackVector;
import com.services.projects.utils.ModelManagerHelper;

import javafx.util.converter.LocalDateStringConverter;

/**
 * @author thomas
 *
 */
public class ProjectService {
	
	/**
	 * Project's budget min estimate entry
	 */
	public static final String PROJECT_WL_INF="proj_wlInf";
	/**
	 * Project's budget max estimate entry
	 */
	public static final String PROJECT_WL_SUP="proj_wlInf";
	/**
	 * Project's budget average estimate entry
	 */
	public static final String PROJECT_WL_AVG="proj_wlAvg";
	/**
	 * Project's budget standard deviation
	 */
	public static final String PROJECT_WL_STDDEV="proj_wlStdDev";
	/**
	 * Sprint's budget min estimate entry
	 */
	public static final String SPRINT_WL_INF="sprint_wlInf";
	/**
	 * Sprint's budget max estimate entry
	 */
	public static final String SPRINT_WL_SUP="sprint_wlInf";
	/**
	 * Sprint's budget average estimate entry
	 */
	public static final String SPRINT_WL_AVG="sprint_wlAvg";	
	/**
	 * Sprint's budget standard deviation
	 */
	public static final String SPRINT_WL_STDDEV="sprint_wlStdDev";
	
	public static final String SPRINT_WL_REMAINING="sprint_wlRemaining";
	public static final String TASK_PROGRESS="task_progress";
	
	public static final String SPRINT_TASK_SEP="Z";
	
	/**
	 * Check and auto correct sprints and tasks.
	 * Return the ERRORs and actions applied
	 * @param _project
	 * @return
	 */
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
				_msg1+=  "[ACTION: Sprint dates Auto correction]";
				_res.add(_msg1);
				LocalDate _sDate = LocalDate.of(_sDateStart.getYear(),_sDateStart.getMonth(), _sDateStart.getDay());
				LocalDate _eDate = _sDate.with(TemporalAdjusters.firstDayOfNextMonth());
				_sprint.setEndDate(ModelManagerHelper.convertToXmlGregorianCalendar(_eDate) );
			}
			List<Task> _taskList = _sprint.getTaskList().getTask();
			int _nbTask=0;
			for( Task _task : _taskList){
				XMLGregorianCalendar _tDateStart = _task.getMetric().getEstimate().getStartDate();
				
				
				if(_tDateStart.compare(_sDateStart)==DatatypeConstants.LESSER){
					String _msg1 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ERROR: Start dates is lesser than Sprint Start Date]";
					_msg1 += "[ACTION: Start Date Auto correction]";
					_res.add(_msg1);
					_task.getMetric().getEstimate().setStartDate(_sDateStart);
					_tDateStart=_sDateStart;
				}
				
				if(_tDateStart.compare(_sDateEnd)==DatatypeConstants.GREATER){
					String _msg1 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ERROR: Start Date is greater than Sprint End Date]";
					_msg1+="[ACTION: Start Date Auto correction]";
					_res.add(_msg1);
					
					_task.getMetric().getEstimate().setStartDate(_sDateStart);
					_tDateStart=_sDateEnd;
				}
				
				XMLGregorianCalendar _tDateEnd = _task.getMetric().getEstimate().getEndDate();
				if(_sDateEnd.compare(_tDateEnd)==DatatypeConstants.LESSER){
					String _msg1 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ERROR: End Date is greater than Sprint End date]";
					_msg1+= "[ACTION: End date Auto correction]";
					_res.add(_msg1);
					
					_task.getMetric().getEstimate().setEndDate(_sDateEnd);
					_tDateEnd = _sDateEnd;
				}
								
				if( _tDateEnd.compare(_tDateStart)==DatatypeConstants.LESSER){
					String _msg1 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ERROR:End Date is lower than Start Date]";
					_msg1+="[ACTION: End date Auto correction]";
					_res.add(_msg1);
					
					LocalDate _sDate = ModelManagerHelper.convertToLocalDate(_tDateStart);
					Integer _wlSup = _task.getMetric().getEstimate().getWlSup();
					int daysToAdd = Integer.divideUnsigned(_wlSup.intValue(), 8);
					LocalDate _eDate = _sDate.plusDays(daysToAdd);
					_task.getMetric().getEstimate().setEndDate(ModelManagerHelper.convertToXmlGregorianCalendar(_eDate));
				}
				
				Integer _wlInf = _task.getMetric().getEstimate().getWlInf();
				Integer _wlSup = _task.getMetric().getEstimate().getWlSup();
				if(_wlInf.compareTo(_wlSup) > 0){
					String _msg1 = "[SPRINT:"+_sprint.getName()+"]" +"[TASK:"+_task.getName()+"]" + "[ERROR: Workload not valid]";
					_msg1+="[ACTION: Workload Auto correction applied]";
					_task.getMetric().getEstimate().setWlInf(_wlSup);
					_res.add(_msg1);
					
				}
				_nbTask++;
			}
			_nbSprint++;
		}
		return _res;
	}
	
	
	
	
	/**
	 * Return the sprint informations as a TreeMap. The TreeMap has the entries
	 * SPRINT_WL_INF, SPRINT_WL_SUP, SPRINT_WL_AVG, SPRINT_WL_STDDEV, SPRINT_WL_REMAINING
	 * 
	 * @param _sprint
	 * @return
	 */
	public static TreeMap<String,Double>  computeSprintBudget(Sprint _sprint){
		TreeMap<String,Double> _res = new TreeMap<String,Double>();
		List<Task> _taskList = _sprint.getTaskList().getTask();
		int _budgetInf = 0;
		int _budgetSup = 0;
		double _avg=0;
		double _stdDev=0;
		double _card=0;
		double _wlConsumed=0;
		for(Task _task : _taskList){
			_budgetInf  += _task.getMetric().getEstimate().getWlInf().intValue();
			_budgetSup  += _task.getMetric().getEstimate().getWlSup().intValue();
			_wlConsumed +=  getConsumedWorkLoad(_task);
			_avg += (_budgetInf + _budgetSup)/2;
			_stdDev += (Math.pow(_budgetInf,2) + Math.pow(_budgetSup,2))/2;
			_card+=1;
		}
		_res.put(SPRINT_WL_INF, new Double(_budgetInf));
		_res.put(SPRINT_WL_SUP, new Double(_budgetSup));
		if( _card > 0){
			_avg = _avg/_card;
			_stdDev = (_stdDev/_card) - Math.pow(_avg,2); 
		}
		_res.put(SPRINT_WL_AVG, _avg);
		_res.put(SPRINT_WL_STDDEV, _stdDev);
		_res.put(SPRINT_WL_REMAINING, (_budgetSup-_wlConsumed));
		return _res;
	}
	
	
	public static TreeMap<String,Double>  computeProjectBudget(JsonProjectWrapper _project){
		
		TreeMap<String,Double> _res1 = new TreeMap<String,Double>();
		List<Sprint> _sprintList = _project.getProject().getPhases().getPhase();
		
		double _card=0;
		double _wlInf=0;
		double _wlSup=0;
		double _avg=0;
		double _stdDev=0;
		for(Sprint _sprint : _sprintList){
			TreeMap<String,Double> _sprintMetric = computeSprintBudget(_sprint);
			_wlInf += _sprintMetric.get(SPRINT_WL_INF);
			_wlSup += _sprintMetric.get(SPRINT_WL_SUP);
			_avg += _sprintMetric.get(SPRINT_WL_AVG);
			_stdDev+=_sprintMetric.get(SPRINT_WL_STDDEV);
			_card+=1;
		}
		if(_card>0){
			_avg = (_avg / _card);
			_stdDev=(_stdDev/_card);
		}
		_res1.put(PROJECT_WL_INF, new Double(_wlInf));
		_res1.put(PROJECT_WL_SUP, new Double(_wlSup));
		_res1.put(PROJECT_WL_AVG, new Double(_avg));
		_res1.put(PROJECT_WL_STDDEV, new Double(_stdDev));
		return _res1;
	}
	
	
	/**
	 * Update or merge the sprints/phases.
	 * Keep the timetracking 
	 * @param _oldSprints
	 * @param _newSprints
	 * @return
	 */
	public static List<Sprint> updateSprints(List<Sprint> _oldSprints, List<Sprint> _newSprints){
		List<Sprint> _res=new ArrayList<Sprint>();
		
		for(Sprint _oldSprint : _oldSprints){
			String _oldSKeyId = _oldSprint.getKeyId();
			Sprint _newSprint = findSprintById(_oldSKeyId, _newSprints);
			if(_newSprint != null){
				_oldSprint.setEndDate(_newSprint.getEndDate());
				_oldSprint.setStartDate(_newSprint.getStartDate());
				_oldSprint.setName(_newSprint.getName());
	
				Tasks _updatedTasks = updateTaskList(_oldSprint, _newSprint.getTaskList());
				_oldSprint.setTaskList(_updatedTasks);
				_newSprints.remove(_newSprint);
				_res.add(_oldSprint);
			}else{
				TreeMap<String, Double> _budgetStatus = computeSprintBudget(_oldSprint);
				double _wlR = _budgetStatus.get(SPRINT_WL_REMAINING);
				double _wlS = _budgetStatus.get(SPRINT_WL_SUP);
				if( (_wlR-_wlS) != 0)_res.add(_oldSprint); 
			}
		}
		for(Sprint _sprintToAdd : _newSprints){
			
			_sprintToAdd.setKeyId(generateSpringKeyId(_sprintToAdd));
			int _index=0;
			for(Task _task : _sprintToAdd.getTaskList().getTask()){
				_task = getValidTaskWithTrackEntries(_task);
				_task.setId(generateTaskId(_sprintToAdd, _task)); 
				_sprintToAdd.getTaskList().getTask().set(_index, _task);
				_index++;
			}
			_res.add(_sprintToAdd);
		}
		return _res;
	}
	
	
	private static String generateSpringKeyId(Sprint _newSprint){
		String _res=_newSprint.getName();
		_res = _res.replaceAll(" ", "");
		_res = _res.replaceAll("\\.", "");
		_res = _res.replaceAll("-", "");
		
		return _res;
	}
	
	
	public static Sprint findSprintById(String _spId,List<Sprint> _searchList){
		Sprint _res=null;
		for(Sprint _newSprint : _searchList){
			if(_newSprint.getKeyId().equals(_spId)){
				_res = _newSprint ;
				break;
			}
		}
		return _res;
	}
	
	
	public static Task findTaskByIdIntoTaskList(String _taskId, Tasks _newTaskList){
		Task _res=null;
		for(Task _newTask : _newTaskList.getTask()){
			if(_newTask.getId().equals(_taskId)){
				_res=_newTask;
				break;
			}
		}
		return _res;
	}
	
	public static double getConsumedWorkLoad(Task  _task){
		double _res = 0;
		TrackEntriesType _trackEntries = _task.getTrackEntries();
		if(_trackEntries !=null ){
			TimeLineType _budgetLine = _trackEntries.getBudgetLine();
			List<TrackVector> _trackTimeLine = _budgetLine.getTrack();
			for(TrackVector _trackTime: _trackTimeLine){
				_res+=_trackTime.getProgressFactor();
			}
		}
		return _res;
	}

	private static Tasks updateTaskList(Sprint _Sprint, Tasks _newTaskList) {
		// TODO Auto-generated method stub
		Tasks _res = new Tasks();
		Tasks _oldTaskList = _Sprint.getTaskList();
		
		for(Task _oldTask : _oldTaskList.getTask()){
			String _taskId = _oldTask.getId();
			Task _newTask = findTaskByIdIntoTaskList(_taskId, _newTaskList);
			if(_newTask != null){
				_oldTask.setDescription(_newTask.getDescription());
				_oldTask.setName(_newTask.getName());
				_oldTask.setMetric(_newTask.getMetric());
				_oldTask.setStatus(_newTask.getStatus());
				if(_newTask.getTrackEntries()!=null){
					//TODO Managed update tracktime
					_oldTask.setTrackEntries(_newTask.getTrackEntries());
				}
				_newTaskList.getTask().remove(_newTask);
				_res.getTask().add(_oldTask);
			}else{
				double _consumedWL = getConsumedWorkLoad(_oldTask);
				if(_consumedWL > 0){
					_res.getTask().add(_oldTask);
				}
			}
		}
		
		for(Task _TaskToAdd : _newTaskList.getTask()){
			if(_TaskToAdd.getTrackEntries() == null){
				TrackEntriesType _te = new TrackEntriesType();
				_te.setBudgetLine(new TimeLineType());
				_te.setProgressLine(new TimeLineType());
				TrackVector _defaultBudgetEntry = new TrackVector();
				_defaultBudgetEntry.setProgressFactor(0);
				_defaultBudgetEntry.setTaskOwner("NONE");
				_defaultBudgetEntry.setTrackTime(ModelManagerHelper.convertToXmlGregorianCalendar(LocalDate.now()));
				_te.getBudgetLine().getTrack().add(_defaultBudgetEntry);
				
				TrackVector _defaultProgressEntry = new TrackVector();
				_defaultProgressEntry.setProgressFactor(0);
				_defaultProgressEntry.setTaskOwner("NONE");
				_defaultProgressEntry.setTrackTime(ModelManagerHelper.convertToXmlGregorianCalendar(LocalDate.now()));
				_te.getProgressLine().getTrack().add(_defaultProgressEntry);
				_TaskToAdd.setTrackEntries(_te);
			}
			_TaskToAdd = getValidTaskWithTrackEntries(_TaskToAdd);
			_TaskToAdd.setId(generateTaskId(_Sprint,_TaskToAdd));
			_res.getTask().add(_TaskToAdd);
			
		}
		return _res;
	}

	private static Task getValidTaskWithTrackEntries(Task _task){
		if(_task.getTrackEntries() == null){
			TrackEntriesType _te = new TrackEntriesType();
			_te.setBudgetLine(new TimeLineType());
			_te.setProgressLine(new TimeLineType());
			TrackVector _defaultBudgetEntry = new TrackVector();
			_defaultBudgetEntry.setProgressFactor(0);
			_defaultBudgetEntry.setTaskOwner("NONE");
			_defaultBudgetEntry.setTrackTime(ModelManagerHelper.convertToXmlGregorianCalendar(LocalDate.now()));
			_te.getBudgetLine().getTrack().add(_defaultBudgetEntry);
			
			TrackVector _defaultProgressEntry = new TrackVector();
			_defaultProgressEntry.setProgressFactor(0);
			_defaultProgressEntry.setTaskOwner("NONE");
			_defaultProgressEntry.setTrackTime(ModelManagerHelper.convertToXmlGregorianCalendar(LocalDate.now()));
			_te.getProgressLine().getTrack().add(_defaultProgressEntry);
			_task.setTrackEntries(_te);
		}
		return _task;
	}
	
	
	private static String generateTaskId(Sprint _sprint,Task _task){
		String _res=null;
		_res = _task.getName().replaceAll(" ","");
		_res = _task.getName().replaceAll(".","");
		_res = _task.getName().replaceAll("Z","z");
		_res = _sprint.getKeyId()+SPRINT_TASK_SEP+_res;
		return _res;
	}




	/**
	 * Build a treemap based ont he given trackvector json list
	 * the key is taskOwner+taskTime
	 * @param _taskWlEntries
	 * @return
	 */
	public static TreeMap<String, TrackVector> buildEntriesFromJson(JsonNode _taskWlEntries) {
		TreeMap<String, TrackVector> _res = new TreeMap<String, TrackVector>();
		for(JsonNode _taskWlEntry : _taskWlEntries){
			String _jtaskOwner = _taskWlEntry.get("taskOwner").asText();
			String _jdateTime = _taskWlEntry.get("trackTime").asText();
			double _wl = _taskWlEntry.get("progressFactor").asDouble();
			String _key = _jtaskOwner+_jdateTime;
			if(_res.containsKey(_key)){
				TrackVector _tmpEntry = _res.get(_key);
				double _nWl = _tmpEntry.getProgressFactor();
				_nWl += _wl;
				_tmpEntry.setProgressFactor(_nWl);
			}else{
				TrackVector _tEntry = new TrackVector();
				_tEntry.setProgressFactor(_wl);
				_tEntry.setTaskOwner(_jtaskOwner);
				LocalDate _tmpDate = LocalDate.parse(_jdateTime);
				_tEntry.setTrackTime(ModelManagerHelper.convertToXmlGregorianCalendar(_tmpDate));
				_res.put(_key, _tEntry);
			}
		}
		return _res;
	}
	

}
