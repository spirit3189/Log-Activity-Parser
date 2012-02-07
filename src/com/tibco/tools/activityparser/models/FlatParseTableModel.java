package com.tibco.tools.activityparser.models;

import java.util.Date;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * A Class used to flatten the parse model returned by by the parser so that it
 * can be used by the table & other views easily.
 * 
 * @author Jagdeesh Karicherla
 */
public class FlatParseTableModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActivityCollection errorActivityCollection = null;
	private Vector<Date> timeStamps;

	public ActivityCollection getErrorActivityCollection() {
		return errorActivityCollection;
	}

	public void setErrorActivities(ActivityCollection errors) {
		this.errorActivityCollection = errors;
	}

	public Vector<Date> getTimeStamps() {
		return timeStamps;
	}

	public void setTimeStamps(Vector<Date> timeStamps) {
		this.timeStamps = timeStamps;
	}

	public FlatParseTableModel() {
		super();
		timeStamps = new Vector<Date>();
		errorActivityCollection = new ActivityCollection();
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
