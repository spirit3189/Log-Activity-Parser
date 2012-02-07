/*
 *  File: EventMonitoringControlPanel.java 
 *  Copyright (c) 2004-2008  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.tibco.tools.activityparser.ui.timebar;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.BoxTimeScaleRenderer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultGridRenderer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTimeScaleRenderer;

/**
 * Control panel for the event monitoring example.
 * 
 * @author Jagdeesh Karicherla @ main author Peter Kliem
 */
public class TimeBarControlPanel extends JPanel {
	TimeBarViewer _viewer;
	JSlider _timeScaleSlider;
	JSlider _rowHeigthSlider;
	JComboBox _sorterCombo;
	JComboBox _filterCombo;
	JComboBox _intervalFilterCombo;
	TimeBarMarkerImpl _marker;
	JButton _freisetzenButton;

	public TimeBarControlPanel(TimeBarViewer viewer, TimeBarMarkerImpl marker,
			int initalSecondsDisplayed) {
		_viewer = viewer;
		_marker = marker;
		this.setPreferredSize(new Dimension(500, 100));
		setLayout(new FlowLayout());
		createControls(initalSecondsDisplayed);
	}

	/**
     * 
     */
	private void createControls(int initialSeconds) {

		JLabel zoomLabel = new JLabel("Zoom Time scale ");

		final double slidermax = 1000; // slider maximum

		_timeScaleSlider = new JSlider(0, (int) slidermax);

		_timeScaleSlider.setPreferredSize(new Dimension(_timeScaleSlider
				.getPreferredSize().width * 4, _timeScaleSlider
				.getPreferredSize().height));
		add(zoomLabel);
		add(_timeScaleSlider);

		_timeScaleSlider.setMinimum(1);
		_timeScaleSlider.setMaximum(100000);
		_timeScaleSlider.setValue((int) _viewer.getPixelPerSecond());

		_timeScaleSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int slidedValue = _timeScaleSlider.getValue();
				double pps = (double) slidedValue;
				// pps = pps / (24.0 * 60.0 * 60.0 * 1000.0);
				System.out.println("scale " + slidedValue + "pps " + pps);
				_viewer.setPixelPerSecond(pps);
			}
		});

		JLabel rowHeightLabel = new JLabel("Row Height Slider");

		_rowHeigthSlider = new JSlider(10, 300);
		_rowHeigthSlider.setValue(_viewer.getRowHeight());
		_rowHeigthSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				_viewer.setRowHeight(_rowHeigthSlider.getValue());
			}
		});
		add(rowHeightLabel);
		add(_rowHeigthSlider);

		final JCheckBox boxTSRCheck = new JCheckBox("BoxTimeScaleRenderer");
		boxTSRCheck
				.setSelected(_viewer.getTimeScaleRenderer() instanceof BoxTimeScaleRenderer);
		boxTSRCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (boxTSRCheck.isSelected()) {
					BoxTimeScaleRenderer btscr = new BoxTimeScaleRenderer();
					_viewer.setTimeScaleRenderer(btscr);
					if (_viewer.getGridRenderer() instanceof DefaultGridRenderer) {
						((DefaultGridRenderer) _viewer.getGridRenderer())
								.setTickProvider(btscr);
					}
				} else {
					DefaultTimeScaleRenderer dtscr = new DefaultTimeScaleRenderer();
					_viewer.setTimeScaleRenderer(dtscr);
				}
			}
		});
		add(boxTSRCheck);
	}

}