package com.tibco.tools.activityparser.models;

import java.util.Date;

/**
 * The Class LogRecord.
 * 
 * @author Jagdeesh Karicherla
 */
public class LogRecord {

	/** The time stamp. */
	private Date timeStamp = null;

	/** The level. */
	private String level = null;

	/** The thread name. */
	private String threadName = null;

	/** The message. */
	private String message = null;

	/** The class name. */
	private String className = null;

	/**
	 * Sets the time stamp.
	 * 
	 * @param timeStamp
	 *            the new time stamp
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Gets the time stamp.
	 * 
	 * @return the time stamp
	 */
	public Date getTimeStamp() {
		return this.timeStamp;
	}

	/**
	 * Sets the level.
	 * 
	 * @param level
	 *            the new level
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * Gets the level.
	 * 
	 * @return the level
	 */
	public String getLevel() {
		return this.level;
	}

	/**
	 * Sets the thread name.
	 * 
	 * @param threadName
	 *            the new thread name
	 */
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	/**
	 * Gets the thread name.
	 * 
	 * @return the thread name
	 */
	public String getThreadName() {
		return this.threadName;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Sets the class name.
	 * 
	 * @param className
	 *            the new class name
	 */
	public void setclassName(String className) {
		this.className = className;
	}

	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return this.className;
	}
}
