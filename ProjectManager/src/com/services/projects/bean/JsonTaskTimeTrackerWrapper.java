/**
 * 
 */
package com.services.projects.bean;

import com.services.projects.model.Task;
import com.services.projects.model.TimeLineType;

/**
 * @author thomas
 *
 */
public class JsonTaskTimeTrackerWrapper {
	
	Task task=null;

	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}
	
	public TimeLineType getBudgetLine(){
		return this.getTask().getTrackEntries().getBudgetLine();
	}
	public TimeLineType getProgressLine(){
		return this.getTask().getTrackEntries().getProgressLine();
	}
	
	

}
