package com.tibco.tools.activityparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.tibco.tools.activityparser.models.LogActivityFormat;
import com.tibco.tools.activityparser.utils.LogHelper;

/**
 * This class is useful in parsing the properties file to get the user defined
 * start ,end, exception patterns , log record format , time stamp format.Once
 * load is called, this can be used from anywhere in the app.
 * This is a singleton class. 
 * @author Jagdeesh Karicherla
 */

public class LogActivityFormatter {

	private static LogActivityFormatter instance = null;

	protected LogActivityFormatter() {
	}

	public static LogActivityFormatter getInstance() {
		if (instance == null) {
			instance = new LogActivityFormatter();
			instance.loadDefaults();
		}
		return instance;
	}

	private LogActivityFormat format;

	public LogActivityFormat getLogActivityFormat() {
		return this.format;
	}

	public LogActivityFormat load(String propertiesPath) {
		Properties formatProperties = new Properties();
		format = new LogActivityFormat();
		try {
			FileInputStream propsStream = new FileInputStream(propertiesPath);
			formatProperties.load(propsStream);
			propsStream.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fillFormats(formatProperties);
		return format;
	}

	public LogActivityFormat loadDefaults() {
		Properties formatProperties = new Properties();
		format = new LogActivityFormat();
		try {
			InputStream in = getClass().getResourceAsStream(
					"patterns.properties");
			formatProperties.load(in);
			fillFormats(formatProperties);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return format;
	}

	private void fillFormats(Properties formatProperties) {
		String[] startFormats = formatProperties.getProperty(
				"start.activity.pattern").split(";");
		List<String> startList = new ArrayList<String>();
		Collections.addAll(startList, startFormats);
		format.StartFormat = startList;

		String[] endFormats = formatProperties.getProperty(
				"end.activity.pattern").split(";");
		List<String> endList = new ArrayList<String>();
		Collections.addAll(endList, endFormats);
		format.EndFormat = endList;

		String[] exceptionFormats = formatProperties.getProperty(
				"exception.activity.pattern").split(";");
		List<String> exceptionList = new ArrayList<String>();
		Collections.addAll(exceptionList, exceptionFormats);
		format.ExceptionFormat = exceptionList;

		format.LogRecordFormat = formatProperties
				.getProperty("log.record.pattern");
		format.TimeStampFormat = formatProperties
				.getProperty("log.record.timestamp.pattern");
		format.TimestampGroup = Integer.parseInt(formatProperties
				.getProperty("timestamp.group"));
		format.ThreadGroup = Integer.parseInt(formatProperties
				.getProperty("thread.group"));
		format.LevelGroup = Integer.parseInt(formatProperties
				.getProperty("level.group"));
		format.MessageGroup = Integer.parseInt(formatProperties
				.getProperty("message.group"));
		format.ClassGroup = Integer.parseInt(formatProperties
				.getProperty("class.group"));
		format.ErrorColor = LogHelper.stringToColor(formatProperties
				.getProperty("error.color"));
		format.HightLightColor = LogHelper.stringToColor(formatProperties
				.getProperty("highlight.color"));
		format.NormalColor = LogHelper.stringToColor(formatProperties
				.getProperty("normal.color"));
		format.PseudoSuffix = formatProperties
				.getProperty("pseudoActivity.suffix");
	}

}
