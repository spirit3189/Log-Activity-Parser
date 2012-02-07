package com.tibco.tools.activityparser;

import java.util.Date;

import org.apache.commons.collections.Predicate;

/**
 * The Class LogActivityTimePredicate checks whether a date falls in the range
 * of given min -  max dates.
 * @author Jagdeesh Karicherla

 */
public class LogActivityTimePredicate implements Predicate {

	/** The min time. */
	private Date minTime;

	/** The max time. */
	private Date maxTime;

	/**
	 * Instantiates a new log activity time predicate.
	 * 
	 * @param minTime
	 *            the min time
	 * @param maxTime
	 *            the max time
	 */
	public LogActivityTimePredicate(Date minTime, Date maxTime) {
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	public boolean evaluate(Object object) {
		boolean satisfies = false;
		if (object instanceof Date) {
			Date dateCompared = (Date) object;
			if (dateCompared.compareTo(maxTime) <= 0
					&& dateCompared.compareTo(minTime) >= 0)
				satisfies = true;
		}
		return satisfies;
	}
}
