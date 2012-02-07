package com.tibco.tools.activityparser.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXBusyLabel;

import com.tibco.tools.activityparser.LogActivityFormatter;
import com.tibco.tools.activityparser.ui.views.MainView;

/**
 * Main UI builder class.
 * 
 * @author Jagdeesh Karicherla
 */
public class UIBuilder {
	public static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		LogActivityFormatter.getInstance();
		final JFrame frame = new JFrame("Log Activity Parser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container frameContent = frame.getContentPane();
		frameContent.setLayout(new BorderLayout());
		MainView view = new MainView();
		JPanel leftPanel = new JPanel();
		view.prepareLeftPanel(leftPanel);
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new TitledBorder("Table"));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		JXBusyLabel label = new JXBusyLabel();
		label.setVisible(false);
		JTabbedPane tabbedPanel = new JTabbedPane();

		rightPanel.add(label);
		rightPanel.add(tabbedPanel);
		view.createTabs(rightPanel);
		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftPanel, rightPanel);
		mainPanel.setOneTouchExpandable(true);
		frame.getContentPane().add(mainPanel);
		frame.setVisible(true);
		frame.setVisible(true);
		frame.setState(Frame.NORMAL);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		frame.setSize(dimension);
		frame.setFocusable(true);

	}
}
