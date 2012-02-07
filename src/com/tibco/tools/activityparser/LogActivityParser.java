package com.tibco.tools.activityparser;

import javax.swing.UIManager;

import com.tibco.tools.activityparser.ui.UIBuilder;

/**
 * The Class LogActivityParser contains the main method.
 * 
 * @author Jagdeesh Karicherla
 */

public class LogActivityParser {
	public static void main(String[] args) {
		try {
			try {

				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					UIBuilder.createAndShowGUI();
				}
			});

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
