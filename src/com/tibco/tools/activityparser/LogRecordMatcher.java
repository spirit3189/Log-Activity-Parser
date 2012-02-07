package com.tibco.tools.activityparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tibco.tools.activityparser.models.LogActivityFormat;
import com.tibco.tools.activityparser.models.LogMatchResult;
import com.tibco.tools.activityparser.models.LogMatchTypes;

/**
 * This class is useful in matching a message against patterns like start , end
 * , exception.
 * @author Jagdeesh Karicherla

 */
public class LogRecordMatcher {
	
	/** The start formats. */
	private List<Pattern> startFormats = null;
	
	/** The end formats. */
	private List<Pattern> endFormats = null;
	
	/** The exception formats. */
	List<Pattern> exceptionFormats = null;

	/**
	 * Match.
	 *
	 * @param message the message
	 * @return the log match result
	 */
	public LogMatchResult match(String message) {
		Matcher matcher = null;

		for (int i = 0; i < startFormats.size(); i++) {
			matcher = startFormats.get(i).matcher(message);
			if (matcher.matches()) {
				LogMatchResult match = new LogMatchResult(LogMatchTypes.START,
						message);
				return match;
			}
		}

		for (int i = 0; i < endFormats.size(); i++) {
			matcher = endFormats.get(i).matcher(message);
			if (matcher.matches()) {
				LogMatchResult match = new LogMatchResult(LogMatchTypes.END,
						message);
				return match;
			}
		}

		for (int i = 0; i < exceptionFormats.size(); i++) {
			matcher = exceptionFormats.get(i).matcher(message);
			if (matcher.matches()) {
				LogMatchResult match = new LogMatchResult(
						LogMatchTypes.EXCEPTION, message);
				return match;
			}
		}
		return null;
	}

	/**
	 * Initialize.
	 *
	 * @param activityFormat the activity format
	 */
	public void initialize(LogActivityFormat activityFormat) {
		startFormats = new ArrayList<Pattern>();
		endFormats = new ArrayList<Pattern>();
		exceptionFormats = new ArrayList<Pattern>();
		for (int i = 0; i < activityFormat.StartFormat.size(); i++) {
			Pattern exceptionPattern = Pattern
					.compile(activityFormat.StartFormat.get(i));
			startFormats.add(exceptionPattern);
		}
		for (int i = 0; i < activityFormat.EndFormat.size(); i++) {
			Pattern exceptionPattern = Pattern.compile(activityFormat.EndFormat
					.get(i));
			endFormats.add(exceptionPattern);
		}
		for (int i = 0; i < activityFormat.ExceptionFormat.size(); i++) {
			Pattern exceptionPattern = Pattern
					.compile(activityFormat.ExceptionFormat.get(i));
			exceptionFormats.add(exceptionPattern);
		}
	}

}
