package com.tibco.tools.activityparser.models;

import java.awt.Color;
import java.util.List;

/**
 * A Class used to persist various user configured options(fetched from sources like properties file)
 * 
 * @author Jagdeesh Karicherla
 */
public class LogActivityFormat {
	
	public List<String> StartFormat;
	
	public List<String> EndFormat;
	
	public String LogRecordFormat;
	
	public String TimeStampFormat;
	public List<String> ExceptionFormat;
	public int ThreadGroup;
	public int ClassGroup;
	public int TimestampGroup;
	public int LevelGroup;
	public int MessageGroup;
	public Color HightLightColor;
	public Color ErrorColor;
	public Color NormalColor;
	public String PseudoSuffix;

}
