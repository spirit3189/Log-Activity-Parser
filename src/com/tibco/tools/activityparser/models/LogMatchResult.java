package com.tibco.tools.activityparser.models;
/**
 * Parser - single  scan result.
 * 
 * @author Jagdeesh Karicherla
 */

public class LogMatchResult {
	private LogMatchTypes TYPE;
	private String message;

	public LogMatchResult(LogMatchTypes TYPE, String message) {
		this.message = message;
		this.TYPE = TYPE;
	}

	public void setType(LogMatchTypes type) {
		this.TYPE = type;
	}

	public LogMatchTypes getType() {
		return this.TYPE;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
