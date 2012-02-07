package com.tibco.tools.activityparser.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * The Class ParseResultModel.
 * @author Jagdeesh Karicherla
 */
 
public class ParseResultModel {
	
	/** The thread names. */
	private List<String> threadNames;
	
	/** The time stamps. */
	private Vector<Date> timeStamps;
	
	/** The error activities. */
	private ActivityCollection errorActivities;
	
	/**
	 * Gets the error activities.
	 *
	 * @return the error activities
	 */
	public ActivityCollection getErrorActivities() {
		return errorActivities;
	}

	/**
	 * Sets the error activities.
	 *
	 * @param errorActivities the new error activities
	 */
	public void setErrorActivities(ActivityCollection errorActivities) {
		this.errorActivities = errorActivities;
	}

	/** The activities grouped by thread. */
	private Map<String, LogActivityBuilder> activitiesGroupedByThread;

	/**
	 * Instantiates a new parses the result model.
	 */
	public ParseResultModel() {
		threadNames = new ArrayList<String>();
		timeStamps = new Vector<Date>();
		activitiesGroupedByThread = new HashMap<String, LogActivityBuilder>();
	}

	/**
	 * Gets the thread names.
	 *
	 * @return the thread names
	 */
	public List<String> getThreadNames() {
		return threadNames;
	}

	/**
	 * Sets the thread names.
	 *
	 * @param threadNames the new thread names
	 */
	public void setThreadNames(List<String> threadNames) {
		this.threadNames = threadNames;
	}

	/**
	 * Gets the time stamps.
	 *
	 * @return the time stamps
	 */
	public Vector<Date> getTimeStamps() {
		return timeStamps;
	}

	/**
	 * Sets the time stamps.
	 *
	 * @param timeStamps the new time stamps
	 */
	public void setTimeStamps(Vector<Date> timeStamps) {
		this.timeStamps = timeStamps;
	}

	/**
	 * Gets the activities grouped by thread.
	 *
	 * @return the activities grouped by thread
	 */
	public Map<String, LogActivityBuilder> getActivitiesGroupedByThread() {
		return activitiesGroupedByThread;
	}

	/**
	 * Sets the activities grouped by thread.
	 *
	 * @param activitiesGroupedByThread the activities grouped by thread
	 */
	public void setActivitiesGroupedByThread(
			Map<String, LogActivityBuilder> activitiesGroupedByThread) {
		this.activitiesGroupedByThread = activitiesGroupedByThread;
	}
}
