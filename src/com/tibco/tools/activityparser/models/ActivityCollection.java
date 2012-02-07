package com.tibco.tools.activityparser.models;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * The Class ActivityCollection.
 * @author Jagdeesh Karicherla
 */
public class ActivityCollection {

	/** The time stamps. */
	Vector<Date> timeStamps = null;

	/** The activities. */
	List<Activity> activities = null;

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
	 * @param timeStamps
	 *            the new time stamps
	 */
	public void setTimeStamps(Vector<Date> timeStamps) {
		this.timeStamps = timeStamps;
	}

	/**
	 * Gets the activities.
	 * 
	 * @return the activities
	 */
	public List<Activity> getActivities() {
		return activities;
	}

	/**
	 * Sets the activities.
	 * 
	 * @param activities
	 *            the new activities
	 */
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

}
