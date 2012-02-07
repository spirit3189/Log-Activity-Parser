package com.tibco.tools.activityparser.models;

import java.util.List;

import javax.swing.SwingWorker;

import com.tibco.tools.activityparser.LogParser;

/**
 * Background worker which does all the log parsing and publishes the model to
 * UI.
 * 
 * @author Jagdeesh Karicherla
 */
public class LogParseWorker extends SwingWorker<Void, Object> {

	/** The log file path. */
	private final String logFilePath;

	/** The parser properties file path. */
	private final String parserPropertiesFilePath;

	/** The informable. */
	private final IUpdateInformable informable;

	/**
	 * Instantiates a new log parse worker.
	 * 
	 * @param logFilePath
	 *            the log file path
	 * @param parserPropertiesFilePath
	 *            the parser properties file path
	 * @param informable
	 *            the informable
	 */
	public LogParseWorker(String logFilePath, String parserPropertiesFilePath,
			IUpdateInformable informable) {
		this.logFilePath = logFilePath;
		this.informable = informable;
		this.parserPropertiesFilePath = parserPropertiesFilePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {
		final LogParser logParser = new LogParser();
		logParser.setLogFormatProperties(parserPropertiesFilePath);
		logParser.parse(logFilePath);
		ParseResultModel model = new ParseResultModel();
		model.setThreadNames(logParser.getLogActivitiesThreadNames());
		model.setTimeStamps(logParser.getTimeStamps());
		model.setErrorActivities(logParser.getErrorActivities());
		model.setActivitiesGroupedByThread(logParser
				.getActivitiesGroupedByThread());
		publish(model);
		// update the progress
		// setProgress((i + 1) * 100 / size);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#process(java.util.List)
	 */
	@Override
	protected void process(List<Object> messages) {

		for (Object message : messages) {
			informable.messagePublished(message);
		}
	}
}
