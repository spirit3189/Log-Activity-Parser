package com.tibco.tools.activityparser.models;

/**
 * The Class Activity.
 * /**
 * @author Jagdeesh Karicherla
 */
public class Activity {
	
	/** The start event. */
	private LogRecord startEvent;
	
	/** The end event. */
	private LogRecord endEvent;
	
	/** The thread name. */
	private String threadName;
	
	/** The has error. */
	private boolean hasError;
	
	/** The is exception. */
	private boolean isException;

	public boolean isHasPseudoStart() {
		return hasPseudoStart;
	}

	public void setHasPseudoStart(boolean hasPseudoStart) {
		this.hasPseudoStart = hasPseudoStart;
	}

	public boolean isHasPseudoEnd() {
		return hasPseudoEnd;
	}

	public void setHasPseudoEnd(boolean hasPseudoEnd) {
		this.hasPseudoEnd = hasPseudoEnd;
	}

	private boolean hasPseudoStart;
	
	private boolean hasPseudoEnd;
	
	/**
	 * Sets the start event.
	 *
	 * @param record the new start event
	 */
	public void setStartEvent(LogRecord record) {
		this.startEvent = record;
	}

	/**
	 * Gets the start event.
	 *
	 * @return the start event
	 */
	public LogRecord getStartEvent() {
		return this.startEvent;
	}

	/**
	 * Sets the end event.
	 *
	 * @param record the new end event
	 */
	public void setEndEvent(LogRecord record) {
		this.endEvent = record;
	}

	/**
	 * Gets the end event.
	 *
	 * @return the end event
	 */
	public LogRecord getEndEvent() {
		return this.endEvent;
	}

	/**
	 * Sets the thread name.
	 *
	 * @param threadName the new thread name
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
	 * Sets the checks for error.
	 *
	 * @param error the new checks for error
	 */
	public void setHasError(boolean error) {
		this.hasError = error;
	}

	/**
	 * Gets the checks for error.
	 *
	 * @return the checks for error
	 */
	public boolean getHasError() {
		return this.hasError;
	}

	/**
	 * Instantiates a new activity.
	 */
	public Activity() {
		hasError = false;
		isException =false;
	}

	/**
	 * Checks if is exception.
	 *
	 * @return true, if is exception
	 */
	public boolean isException() {
		return isException;
	}

	/**
	 * Sets the exception.
	 *
	 * @param isException the new exception
	 */
	public void setException(boolean isException) {
		this.isException = isException;
	}
}
