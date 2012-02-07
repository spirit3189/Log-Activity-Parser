package com.tibco.tools.activityparser;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tibco.tools.activityparser.models.Activity;
import com.tibco.tools.activityparser.models.ActivityCollection;
import com.tibco.tools.activityparser.models.LogActivityBuilder;
import com.tibco.tools.activityparser.models.LogActivityFormat;
import com.tibco.tools.activityparser.models.LogMatchResult;
import com.tibco.tools.activityparser.models.LogMatchTypes;
import com.tibco.tools.activityparser.models.LogRecord;
import com.tibco.tools.activityparser.utils.LogHelper;

/**
 * Class parses the log file & returns a collection of activities grouped by
 * thread name. *
 * @author Jagdeesh Karicherla
 */

public class LogParser {

	private Pattern logRecordPattern = null;
	private LogActivityFormat logActivityFormat = null;
	private LogRecordMatcher recordMatcher = null;
	private Map<String, LogActivityBuilder> logActivitiesGroupedByThread = null;
	private Vector<Date> logActivitiesTimeStamps = null;
	private Vector<String> logActivitiesThreadNames = null;

	public void setLogFormatProperties(String formatPropertiesPath) {
		LogActivityFormatter logFormatter = new LogActivityFormatter();
		if (formatPropertiesPath.length() > 0)
			logActivityFormat = logFormatter.load(formatPropertiesPath);
		else
			logActivityFormat = logFormatter.loadDefaults();
	}

	public Map<String, LogActivityBuilder> getActivitiesGroupedByThread() {
		return logActivitiesGroupedByThread;
	}

	public Vector<String> getLogActivitiesThreadNames() {
		Vector<String> newVect = new Vector<String>(new LinkedHashSet<String>(
				logActivitiesThreadNames));
		Collections.sort(newVect);
		return newVect;
	}

	public void parse(String logFilePath) throws Exception {
		initParse();
		InputStream logFileStream = null;
		try {
			logFileStream = new FileInputStream(logFilePath);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					logFileStream));
			String line = null;
			while ((line = reader.readLine()) != null) {
				matchLine(line);
			}
			for (Object keyMap : getActivitiesGroupedByThread().keySet()) {
				LogActivityBuilder activityBuilder = getActivitiesGroupedByThread()
						.get(keyMap);
				activityBuilder.signalEOF();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logFileStream.close();
		}

	}

	public Vector<Date> getTimeStamps() {
		Vector<Date> timeStamps = new Vector<Date>();
		for (Object keyMap : getActivitiesGroupedByThread().keySet()) {
			LogActivityBuilder activityBuilder = getActivitiesGroupedByThread()
					.get(keyMap);
			for (int i = 0; i < activityBuilder.getActivityList().size(); i++) {
				Activity activity = activityBuilder.getActivityList().get(i);
				timeStamps.add(activity.getStartEvent().getTimeStamp());
				timeStamps.add(activity.getEndEvent().getTimeStamp());
			}
		}
		Collection<Date> noDuplicateDates = new LinkedHashSet<Date>(timeStamps);
		timeStamps.clear();
		timeStamps.addAll(noDuplicateDates);
		Collections.sort(timeStamps);
		return timeStamps;
	}

	public ActivityCollection getErrorActivities() {

		ActivityCollection errorActivityCollection = new ActivityCollection();
		Vector<Date> errorTimeStamps = new Vector<Date>();
		List<Activity> errorActivities = new ArrayList<Activity>();
		for (Object keyMap : getActivitiesGroupedByThread().keySet()) {
			LogActivityBuilder activityBuilder = getActivitiesGroupedByThread()
					.get(keyMap);
			for (int i = 0; i < activityBuilder.getActivityList().size(); i++) {
				Activity activity = activityBuilder.getActivityList().get(i);
				if (activity.getHasError()) {
					errorActivities.add(activity);
					errorTimeStamps
							.add(activity.getStartEvent().getTimeStamp());
					errorTimeStamps.add(activity.getEndEvent().getTimeStamp());
				}
			}
		}
		Collection<Date> noDuplicateDates = new LinkedHashSet<Date>(
				errorTimeStamps);
		errorTimeStamps.clear();
		errorTimeStamps.addAll(noDuplicateDates);
		Collections.sort(errorTimeStamps);
		errorActivityCollection.setTimeStamps(errorTimeStamps);
		errorActivityCollection.setActivities(errorActivities);
		return errorActivityCollection;
	}

	private void initParse() {
		logRecordPattern = Pattern.compile(logActivityFormat.LogRecordFormat);
		recordMatcher = new LogRecordMatcher();
		recordMatcher.initialize(logActivityFormat);
		logActivitiesGroupedByThread = new HashMap<String, LogActivityBuilder>();
		logActivitiesTimeStamps = new Vector<Date>();
		logActivitiesThreadNames = new Vector<String>();
	}

	private void matchLine(String line) throws Exception {
		Matcher matcher = logRecordPattern.matcher(line);
		if (matcher.matches()) {
			String threadname = matcher.group(logActivityFormat.ThreadGroup);
			String message = matcher.group(logActivityFormat.MessageGroup);
			String Level = matcher.group(logActivityFormat.LevelGroup);
			LogMatchResult matchResult = null;
			if (Level.equalsIgnoreCase("ERROR")
					|| Level.equalsIgnoreCase("FATAL")) {
				matchResult = new LogMatchResult(LogMatchTypes.EXCEPTION,
						message);
			} else
				matchResult = recordMatcher.match(message);
			if (matchResult != null) {
				LogRecord logrecord = prepareLogRecord(matcher);
				logActivitiesTimeStamps.add(logrecord.getTimeStamp());
				addLogRecord(threadname, matchResult, logrecord);
			}
			return;
		}
	}

	private void addLogRecord(String threadname, LogMatchResult matchResult,
			LogRecord logrecord) {
		LogMatchTypes matchtype = matchResult.getType();
		if (logActivitiesGroupedByThread.containsKey(threadname)) {
			switch (matchtype) {
			case START:
				logActivitiesGroupedByThread.get(threadname)
						.setStartofActivity(logrecord);
				break;
			case END:
				logActivitiesGroupedByThread.get(threadname).setEndofActivity(
						logrecord);
				break;
			case EXCEPTION:
				logActivitiesGroupedByThread.get(threadname).setErrorActivity(
						logrecord);
				break;
			}

		} else {
			LogActivityBuilder builder = new LogActivityBuilder();
			builder.setThreadName(threadname);
			switch (matchtype) {
			case START:
				builder.setStartofActivity(logrecord);
				break;
			case END:
				builder.setEndofActivity(logrecord);
				break;
			case EXCEPTION:
				builder.setErrorActivity(logrecord);
				break;
			}
			logActivitiesGroupedByThread.put(threadname, builder);
			logActivitiesThreadNames.add(threadname);
		}
	}

	public LogRecord prepareLogRecord(Matcher matcher) {
		LogRecord logRecord = new LogRecord();
		logRecord.setTimeStamp(LogHelper.parseDate(matcher
				.group(logActivityFormat.TimestampGroup)));
		logRecord.setThreadName(matcher.group(logActivityFormat.ThreadGroup));
		logRecord.setLevel(matcher.group(logActivityFormat.LevelGroup));
		logRecord.setclassName(matcher.group(logActivityFormat.ClassGroup));
		logRecord.setMessage(matcher.group(logActivityFormat.MessageGroup));
		return logRecord;
	}

}
