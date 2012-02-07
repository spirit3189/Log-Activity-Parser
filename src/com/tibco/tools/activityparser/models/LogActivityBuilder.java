package com.tibco.tools.activityparser.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tibco.tools.activityparser.utils.LogHelper;

/**
 * Core Class that has end results of parser loaded.
 * 
 * @author Jagdeesh Karicherla
 */

public class LogActivityBuilder {

	/** The thread name. */
	private String threadName;

	/** The current start log record. */
	private LogRecord currentStartLogRecord = null;

	/** The current end log record. */
	private LogRecord currentEndLogRecord = null;

	/** The current error log record. */
	private LogRecord currentErrorLogRecord = null;

	/** The current scan has error. */
	private Boolean currentScanHasError = false;

	/** The activities. */
	private List<Activity> activities = null;

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
	 * Sets the startof activity.
	 * 
	 * @param startRecord
	 *            the new startof activity
	 */
	public void setStartofActivity(LogRecord startRecord) {
		if (currentStartLogRecord != null) // already a start means the case of
											// consecutive starts
		{
			Activity pseudoActivity = new Activity();
			pseudoActivity.setStartEvent(currentStartLogRecord);

			if (currentScanHasError) {
				pseudoActivity.setEndEvent(currentErrorLogRecord);
				pseudoActivity.setHasError(true);
				currentScanHasError = false;
				currentErrorLogRecord = null;
			} else {
				pseudoActivity.setEndEvent(createPseudoAcitivity(startRecord,
						LogPseudoTimeChange.DECREASE));
				pseudoActivity.setHasPseudoEnd(true);
			}

			pseudoActivity.setThreadName(threadName);

			if (pseudoActivity.getStartEvent().getTimeStamp()
					.compareTo(pseudoActivity.getEndEvent().getTimeStamp()) > 0) {
				LogRecord tempStart = pseudoActivity.getStartEvent();
				LogRecord tempEnd = pseudoActivity.getEndEvent();
				pseudoActivity.setStartEvent(tempEnd);
				pseudoActivity.setEndEvent(tempStart);
			}

			this.activities.add(pseudoActivity);
		}

		if (currentStartLogRecord == null && currentErrorLogRecord != null) {
			Activity activity = new Activity();
			activity.setStartEvent(createPseudoAcitivity(currentErrorLogRecord,
					LogPseudoTimeChange.DECREASE));
			activity.setHasPseudoStart(true);
			activity.setHasError(true);
			activity.setEndEvent(currentErrorLogRecord);
			currentScanHasError = false;
			activity.setThreadName(threadName);
			this.activities.add(activity);
		}
		currentStartLogRecord = startRecord;
		currentEndLogRecord = null;
	}

	/**
	 * Sets the endof activity.
	 * 
	 * @param endRecord
	 *            the new endof activity
	 */
	public void setEndofActivity(LogRecord endRecord) {
		// check if start activity is set
		if (currentStartLogRecord != null) {
			Activity activity = new Activity();
			activity.setStartEvent(currentStartLogRecord);
			activity.setEndEvent(endRecord);
			if (currentScanHasError) {
				activity.setHasError(true);
				currentScanHasError = false;
				currentErrorLogRecord = null;
			}
			activity.setThreadName(threadName);
			this.activities.add(activity);
		} else { // - means end without start
			Activity activity = new Activity();
			activity.setStartEvent(createPseudoAcitivity(endRecord,
					LogPseudoTimeChange.DECREASE));
			activity.setHasPseudoStart(true);
			activity.setEndEvent(endRecord);
			if (currentScanHasError) {
				activity.setStartEvent(currentErrorLogRecord); // exception -end
				activity.setHasError(true);
				currentScanHasError = false;
				currentErrorLogRecord = null;
			}
			activity.setThreadName(threadName);
			this.activities.add(activity);
		}
		currentStartLogRecord = null;
		currentEndLogRecord = null;
	}

	/**
	 * Sets the error activity.
	 * 
	 * @param errorRecord
	 *            the new error activity
	 */
	public void setErrorActivity(LogRecord errorRecord) {
		if (currentScanHasError) {

			if (currentStartLogRecord == null)// exception1 - exception2
			{
				Activity activity = new Activity();
				activity.setStartEvent(currentErrorLogRecord);
				activity.setException(true);
				activity.setHasError(true);
				activity.setEndEvent(currentErrorLogRecord);
				activity.setThreadName(threadName);
				this.activities.add(activity);
			}
			if (currentStartLogRecord != null && currentErrorLogRecord != null) // start-exception1
																				// -exception2
			{
				Activity activity = new Activity();
				activity.setStartEvent(currentStartLogRecord);
				activity.setHasError(true);
				activity.setEndEvent(currentErrorLogRecord);
				currentStartLogRecord = null;
				activity.setThreadName(threadName);
				this.activities.add(activity);
			}
		}
		currentScanHasError = true;
		currentErrorLogRecord = errorRecord;
	}

	/**
	 * Gets the activity list.
	 * 
	 * @return the activity list
	 */
	public List<Activity> getActivityList() {
		return this.activities;
	}

	/**
	 * Instantiates a new log activity builder.
	 */
	public LogActivityBuilder() {
		activities = new ArrayList<Activity>();
	}

	/**
	 * Creates the pseudo acitivity.
	 * 
	 * @param record
	 *            the record
	 * @param timechange
	 *            the timechange
	 * @return the log record
	 */
	public LogRecord createPseudoAcitivity(LogRecord record,
			LogPseudoTimeChange timechange) {
		LogRecord logRecord = new LogRecord();
		Date recordTimeStamp = record.getTimeStamp();
		Date pseudoDate = null;
		try {
			pseudoDate = LogHelper.changeDate(recordTimeStamp, timechange);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logRecord.setTimeStamp(pseudoDate);
		logRecord.setThreadName(record.getThreadName());
		logRecord.setLevel(record.getLevel());
		logRecord.setclassName(record.getClassName());
		logRecord.setMessage(record.getMessage());
		return logRecord;
	}

	/**
	 * Signal eof.
	 */
	public void signalEOF() {
		if (currentErrorLogRecord != null) {
			if (currentStartLogRecord != null) {
				Activity activity = new Activity();
				activity.setThreadName(currentStartLogRecord.getThreadName());
				activity.setStartEvent(currentStartLogRecord);
				activity.setHasError(true);
				activity.setEndEvent(currentErrorLogRecord);
				this.activities.add(activity);
				if (currentStartLogRecord.getTimeStamp().compareTo(
						currentErrorLogRecord.getTimeStamp()) > 0) {
					activity.setEndEvent(currentStartLogRecord);
					activity.setHasError(true);
					activity.setStartEvent(currentErrorLogRecord);
				}
				currentStartLogRecord = null;
			} else if (currentEndLogRecord != null) {
				Activity activity = new Activity();
				activity.setStartEvent(currentEndLogRecord);
				activity.setThreadName(currentEndLogRecord.getThreadName());
				activity.setHasError(true);
				activity.setEndEvent(currentErrorLogRecord);
				this.activities.add(activity);
				currentEndLogRecord = null;
			} else {
				Activity pseudoActivity = new Activity();
				pseudoActivity.setThreadName(currentErrorLogRecord
						.getThreadName());
				pseudoActivity.setStartEvent(currentErrorLogRecord);
				pseudoActivity.setHasError(true);
				pseudoActivity.setException(true);
				pseudoActivity.setEndEvent(currentErrorLogRecord);
				this.activities.add(pseudoActivity);
			}
			currentErrorLogRecord = null;
		} else {
			if (currentStartLogRecord != null) {
				Activity pseudoActivity = new Activity();
				pseudoActivity.setThreadName(currentStartLogRecord
						.getThreadName());
				pseudoActivity.setStartEvent(currentStartLogRecord);
				pseudoActivity.setHasError(true);
				pseudoActivity.setEndEvent(createPseudoAcitivity(
						currentStartLogRecord, LogPseudoTimeChange.INCREASE));
				pseudoActivity.setHasPseudoEnd(true);
				this.activities.add(pseudoActivity);
			}

			if (currentEndLogRecord != null) {
				Activity pseudoActivity = new Activity();
				pseudoActivity.setStartEvent(currentEndLogRecord);
				pseudoActivity.setThreadName(currentEndLogRecord
						.getThreadName());
				pseudoActivity.setHasError(true);
				pseudoActivity.setEndEvent(createPseudoAcitivity(
						currentEndLogRecord, LogPseudoTimeChange.INCREASE));
				pseudoActivity.setHasPseudoEnd(true);
				currentEndLogRecord = null;
				this.activities.add(pseudoActivity);
			}
		}
	}
}
