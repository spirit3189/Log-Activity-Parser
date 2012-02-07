package com.tibco.tools.activityparser.ui.views.filters;

import java.io.File;

/**
 * A file filter for *.log
 * 
 * @author Jagdeesh Karicherla
 */
public class LogFileFilter extends javax.swing.filechooser.FileFilter {
	public boolean accept(File file) {
		String filename = file.getName();
		return filename.endsWith(".log");
	}

	public String getDescription() {
		return "*.log";
	}
}
