package com.tibco.tools.activityparser.models;

/**
 * Purpose : To publish messages returned by background worker threads.
 * 
 * @author Jagdeesh Karicherla
 */

public interface IUpdateInformable {
	
	/**
	 * Message published.
	 *
	 * @param message the message
	 */
	void messagePublished(Object message);
}


