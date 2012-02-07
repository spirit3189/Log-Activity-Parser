/*
 *  File: EventMonitoringExample.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import com.tibco.tools.activityparser.LogActivityFormatter;
import com.tibco.tools.activityparser.models.Activity;
import com.tibco.tools.activityparser.models.FlatParseTableModel;
import com.tibco.tools.activityparser.models.LogActivityBuilder;
import com.tibco.tools.activityparser.models.ParseResultModel;

import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.model.DefaultTimeBarNode;
import de.jaret.util.ui.timebars.model.HierarchicalTimeBarModel;
import de.jaret.util.ui.timebars.model.HierarchicalViewStateListener;
import de.jaret.util.ui.timebars.model.ISelectionRectListener;
import de.jaret.util.ui.timebars.model.ITimeBarChangeListener;
import de.jaret.util.ui.timebars.model.ITimeBarViewState;
import de.jaret.util.ui.timebars.model.PPSInterval;
import de.jaret.util.ui.timebars.model.TBRect;
import de.jaret.util.ui.timebars.model.TimeBarModel;
import de.jaret.util.ui.timebars.model.TimeBarNode;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.model.TimeBarSelectionListener;
import de.jaret.util.ui.timebars.model.TimeBarSelectionModel;
import de.jaret.util.ui.timebars.strategy.IIntervalSelectionStrategy;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.BoxTimeScaleRenderer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultHierarchyRenderer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTimeScaleRenderer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTitleRenderer;

/**
 * 
 * @author Peter Kliem
 */
public class TimeBarView {
	TimeBarViewer _tbv;
	TimeBarMarkerImpl _tm;

	public TimeBarView() {
	}

	public JPanel buildTimeBar(ParseResultModel initModel) {
		final JPanel timePanel = new JPanel();
		timePanel.setLayout(new BorderLayout());
		TimeBarModel flatModel = TimeBarModelCreator
				.createTimeBarFlatModel(initModel);

		final TimeBarViewer _tbv = new TimeBarViewer();
		JaretDate timeBarStartoffsetDate = new JaretDate(initModel
				.getTimeStamps().get(0));
		JaretDate timeBarEndoffsetDate = new JaretDate(initModel
				.getTimeStamps().get(initModel.getTimeStamps().size() - 1));

		_tbv.setMinDate(timeBarStartoffsetDate.copy().backSeconds(1));
		_tbv.setMaxDate(timeBarEndoffsetDate.copy().advanceSeconds(1));
		_tbv.setTimeScalePosition(TimeBarViewer.TIMESCALE_POSITION_TOP);
		_tbv.setMilliAccuracy(true);
		_tbv.setYAxisWidth(200);
		_tbv.setStrictClipTimeCheck(true);
		// allow marker grabbing in the diagram area
		_tbv.setMarkerDraggingInDiagramArea(true);
		// _tbv.setAdjustMinMaxDatesByModel(true);
		_tbv.setPixelPerSecond(100000);

		// enable region selection
		_tbv.setRegionRectEnable(true);
		// _tbv.setOptimizeScrolling(true);
		// draw row grid
		_tbv.setDrawRowGrid(true);

		// setup header renderer
		_tbv.setHeaderRenderer(new TimeBarHeaderRenderer());

		// set a name for the viewer and setup the default title renderer
		_tbv.setName("TimeBar");
		_tbv.setTitleRenderer(new DefaultTitleRenderer());
		_tbv.setVariableXScale(true);
		_tbv.getSelectionModel().addTimeBarSelectionListener(
				new TimeBarSelectionListener() {

					@Override
					public void selectionChanged(TimeBarSelectionModel arg0) {
						timePanel.requestFocusInWindow();

					}

					@Override
					public void elementRemovedFromSelection(
							TimeBarSelectionModel arg0, Object arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void elementAddedToSelection(
							TimeBarSelectionModel arg0, Object arg1) {

					}
				});
		class scrollAction implements AdjustmentListener {
			public void adjustmentValueChanged(AdjustmentEvent ae) {

				int value = ae.getValue();
				System.out.println("scrolling value changed :" + value);
				JaretDate date = _tbv.getMinDate().copy();
				if (_tbv.isMilliAccuracy()) {
					date.advanceMillis(value);
				} else {
					// date.advanceSeconds(value / _timeFactor);
				}
				_tbv.scrollDateToVisible(date);
			}
		}

		AbstractAction handleRightKey = new AbstractAction() {

			public void actionPerformed(ActionEvent e) {

			}
		};

		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				int keyCode = e.getKeyCode();

				switch (keyCode) {
				case KeyEvent.VK_UP:
					// handle up
					if (timePanel.isFocusOwner() && e.getSource() == timePanel) {
						if (e.getID() == KeyEvent.KEY_PRESSED) {
							System.out.println("up key");
							int newSelectedTimeBarRowIndex = 0;
							if (!(_tbv.getSelectionModel().getSelectedRows()
									.size() > 0)) {
								newSelectedTimeBarRowIndex = 0;
							}
							if (_tbv.getSelectionModel().getSelectedRows()
									.size() > 0) {
								TimeBarRow selectedTimeBarRow = _tbv
										.getSelectionModel().getSelectedRows()
										.get(0);
								for (int i = 0; i < _tbv.getModel()
										.getRowCount(); i++) {
									if (selectedTimeBarRow.equals(_tbv
											.getModel().getRow(i))) {
										newSelectedTimeBarRowIndex = (i - 1 > 0) ? i - 1
												: 0;

									}
								}

							}
							_tbv.getSelectionModel().setSelectedRow(
									_tbv.getModel().getRow(
											newSelectedTimeBarRowIndex));

						}

					}
					break;
				case KeyEvent.VK_DOWN:
					// handle down
					if (timePanel.isFocusOwner() && e.getSource() == timePanel) {
						if (e.getID() == KeyEvent.KEY_PRESSED) {
							System.out.println("down key");
							int newSelectedTimeBarRowIndex = 0;
							if (!(_tbv.getSelectionModel().getSelectedRows()
									.size() > 0)) {
								newSelectedTimeBarRowIndex = 0;
							}
							if (_tbv.getSelectionModel().getSelectedRows()
									.size() > 0) {
								TimeBarRow selectedTimeBarRow = _tbv
										.getSelectionModel().getSelectedRows()
										.get(0);
								for (int i = 0; i < _tbv.getModel()
										.getRowCount(); i++) {
									if (selectedTimeBarRow.equals(_tbv
											.getModel().getRow(i))) {
										newSelectedTimeBarRowIndex = (i + 1 > _tbv
												.getModel().getRowCount() - 1) ? i
												: i + 1;

									}
								}

							}
							_tbv.getSelectionModel().setSelectedRow(
									_tbv.getModel().getRow(
											newSelectedTimeBarRowIndex));

						}
					}
					break;
				case KeyEvent.VK_LEFT:
				// handle left
				{

					if (timePanel.isFocusOwner() && e.getSource() == timePanel) {
						if (e.getID() == KeyEvent.KEY_PRESSED) {

							System.out.println("left key");
							if (!(_tbv.getSelectionModel().getSelectedRows()
									.size() > 0)) {
								_tbv.getSelectionModel().setSelectedRow(
										_tbv.getModel().getRow(0));
							}
							if (_tbv.getSelectionModel().getSelectedRows()
									.size() > 0) {
								TimeBarRow selectedTimeBarRow = _tbv
										.getSelectionModel().getSelectedRows()
										.get(0);
								Interval selectedInterval = null;
								int decrementedInterval = 0;
								if (_tbv.getSelectionModel()
										.getSelectedIntervals().size() > 0) {
									selectedInterval = _tbv.getSelectionModel()
											.getSelectedIntervals().get(0);
									int selectedIntervalIndex = _tbv
											.getModel()
											.getRowForInterval(selectedInterval)
											.getIntervals()
											.indexOf(selectedInterval);
									decrementedInterval = (selectedIntervalIndex - 1) > 0 ? selectedIntervalIndex
											: selectedIntervalIndex - 1;

								}
								_tbv.getSelectionModel().setSelectedInterval(
										selectedTimeBarRow.getIntervals().get(
												decrementedInterval));
								_tbv.scrollIntervalToVisible(
										selectedTimeBarRow,
										selectedTimeBarRow.getIntervals().get(
												decrementedInterval));
							}

						}
					}
					break;
				}
				case KeyEvent.VK_RIGHT: {
					if (timePanel.isFocusOwner() && e.getSource() == timePanel) {
						if (e.getID() == KeyEvent.KEY_PRESSED) {

							System.out.println("right key");
							if (!(_tbv.getSelectionModel().getSelectedRows()
									.size() > 0)) {
								_tbv.getSelectionModel().setSelectedRow(
										_tbv.getModel().getRow(0));
							}
							if (_tbv.getSelectionModel().getSelectedRows()
									.size() > 0) {
								TimeBarRow selectedTimeBarRow = _tbv
										.getSelectionModel().getSelectedRows()
										.get(0);
								Interval selectedInterval = null;
								int incrementedInterval = 0;
								if (_tbv.getSelectionModel()
										.getSelectedIntervals().size() > 0) {
									selectedInterval = _tbv.getSelectionModel()
											.getSelectedIntervals().get(0);
									int selectedIntervalIndex = _tbv
											.getModel()
											.getRowForInterval(selectedInterval)
											.getIntervals()
											.indexOf(selectedInterval);
									incrementedInterval = (selectedIntervalIndex + 1 > selectedTimeBarRow
											.getIntervals().size() - 1) ? selectedIntervalIndex
											: selectedIntervalIndex + 1;

								}
								_tbv.getSelectionModel().setSelectedInterval(
										selectedTimeBarRow.getIntervals().get(
												incrementedInterval));
								_tbv.scrollIntervalToVisible(
										selectedTimeBarRow,
										selectedTimeBarRow.getIntervals().get(
												incrementedInterval));
							}

						}
					}
					break;
				}
				}
				return false;
			}

		});

		_tbv.setDrawOverlapping(true);
		_tbv.getSelectionModel().setMultipleSelectionAllowed(false);
		// allow different row heights
		_tbv.getTimeBarViewState().setUseVariableRowHeights(true);
		// add a double click listener for checking on the header
		_tbv.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Point origin = e.getPoint();
					if (_tbv.getDelegate().getYAxisRect().contains(origin)) {

						TimeBarRowForActivity row = (TimeBarRowForActivity) _tbv
								.getRowForXY(origin.x, origin.y);
						if (row != null) {
							if (row instanceof TimeBarRowForActivity) {
								TimeBarRowForActivity erow = (TimeBarRowForActivity) row;
								if (!erow.isExpanded()) {
									// expand
									_tbv.getTimeBarViewState()
											.setDrawOverlapping(row, false);
									_tbv.getTimeBarViewState().setRowHeight(
											row,
											calculateRowHeight(
													_tbv.getDelegate(),
													_tbv.getTimeBarViewState(),
													row));
									erow.setExpanded(true);
								} else {
									// fold
									_tbv.getTimeBarViewState()
											.setDrawOverlapping(row, true);
									_tbv.getTimeBarViewState().setRowHeight(
											row,
											_tbv.getTimeBarViewState()
													.getDefaultRowHeight());
									erow.setExpanded(false);
								}
							}
						}
					}
				}
			}

			/**
			 * Calculate the optimal row height
			 * 
			 * @param delegate
			 * @param timeBarViewState
			 * @param row
			 * @return
			 */
			public int calculateRowHeight(TimeBarViewerDelegate delegate,
					ITimeBarViewState timeBarViewState, TimeBarRow row) {
				int maxOverlap = timeBarViewState.getDefaultRowHeight();
				int height = delegate.getMaxOverlapCount(row) * maxOverlap;
				return height;
			}

		});

		_tbv.addTimeBarChangeListener(new ITimeBarChangeListener() {

			public void intervalChangeCancelled(TimeBarRow row,
					Interval interval) {
				System.out.println("CHANGE CANCELLED " + row + " " + interval);
			}

			public void intervalChangeStarted(TimeBarRow row, Interval interval) {
				System.out.println("CHANGE STARTED " + row + " " + interval);
			}

			public void intervalChanged(TimeBarRow row, Interval interval,
					JaretDate oldBegin, JaretDate oldEnd) {
				System.out.println("CHANGE DONE " + row + " " + interval);
			}

			public void intervalIntermediateChange(TimeBarRow row,
					Interval interval, JaretDate oldBegin, JaretDate oldEnd) {
				System.out.println("CHANGE INTERMEDIATE " + row + " "
						+ interval);
			}

			public void markerDragStarted(TimeBarMarker marker) {
				System.out.println("Marker drag started " + marker);
			}

			public void markerDragStopped(TimeBarMarker marker) {
				System.out.println("Marker drag stopped " + marker);
			}

		});

		// do not allow row selections
		_tbv.getSelectionModel().setRowSelectionAllowed(true);
		DefaultTimeScaleRenderer btscr = new DefaultTimeScaleRenderer();
		_tbv.setTimeScaleRenderer(btscr);
		// register additional renderer
		_tbv.registerTimeBarRenderer(TimeBarInterval.class,
				new TimeBarActivityRenderer());
		// _tbv.setMinDate(flatModel.getMinDate().copy().advanceHours(-1));
		// _tbv.setMaxDate(flatModel.getMaxDate().copy().advanceHours(1));
		_tbv.setAdjustMinMaxDatesByModel(true);
		_tbv.setModel(flatModel);

		// add a marker
		_tm = new TimeBarMarkerImpl(true, _tbv.getModel().getMinDate().copy()
				.advanceMillis(5));
		_tm.setDescription("Timebarmarker");
		_tbv.addMarker(_tm);

		// do not show the root node
		_tbv.setHideRoot(true);
		_tbv.setOptimizeScrolling(false);
		// add a popup menu for EventIntervals
		Action action = new AbstractAction("IntervalAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};
		JPopupMenu pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.registerPopupMenu(TimeBarInterval.class, pop);

		// add a popup menu for the body
		final Action bodyaction = new AbstractAction("BodyAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};
		pop = new JPopupMenu("Operations");
		pop.add(bodyaction);
		pop.add(new RunMarkerAction(_tbv));

		// add the zoom action
		pop.add(new ZoomAction(_tbv));
		// add the rem selection action
		pop.add(new ResetRegionSelectionAction(_tbv));

		_tbv.setBodyContextMenu(pop);

		// add a popup menu for the hierarchy
		action = new AbstractAction("HierarchyAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};
		pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.setHierarchyContextMenu(pop);

		// add a popup menu for the header
		action = new AbstractAction("HeaderAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};
		pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.setHeaderContextMenu(pop);

		// add a popup menu for the time scale
		action = new AbstractAction("TimeScaleAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};
		pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.setTimeScaleContextMenu(pop);

		// add a popup menu for the title area
		action = new AbstractAction("TitleAction") {
			public void actionPerformed(ActionEvent e) {

			}
		};
		pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.setTitleContextMenu(pop);
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
		timePanel.add(_tbv);
		TimeBarControlPanel cp = new TimeBarControlPanel(_tbv, _tm, 100); // TODO
		timePanel.add(cp);
		return timePanel;
	}

	public void setEndDate(TimeBarViewer tbv, JaretDate endDate) {
		int secondsDisplayed = tbv.getSecondsDisplayed();
		JaretDate startDate = endDate.copy().advanceSeconds(-secondsDisplayed);
		tbv.setStartDate(startDate);
	}

	boolean isInRange(JaretDate date, double min, double max) {
		int secondsDisplayed = _tbv.getSecondsDisplayed();
		JaretDate minDate = _tbv.getStartDate().copy()
				.advanceSeconds(min * secondsDisplayed);
		JaretDate maxDate = _tbv.getStartDate().copy()
				.advanceSeconds(max * secondsDisplayed);
		return minDate.compareTo(date) > 0 && maxDate.compareTo(date) < 0;
	}

	class TimeBarViewerDragGestureListener implements DragGestureListener {
		public void dragGestureRecognized(DragGestureEvent e) {
			Component c = e.getComponent();
			System.out.println("component " + c);
			System.out.println(e.getDragOrigin());

			// boolean markerDragging = _tbv.getDelegate()
			// .isMarkerDraggingInProgress();
			// if (markerDragging) {
			// return;
			// }

			List<Interval> intervals = _tbv.getDelegate().getIntervalsAt(
					e.getDragOrigin().x, e.getDragOrigin().y);
			if (intervals.size() > 0) {
				Interval interval = intervals.get(0);
				e.startDrag(null, new StringSelection("Drag "
						+ ((TimeBarInterval) interval).getTitle()));
				return;
			}
			Point origin = e.getDragOrigin();
			if (_tbv.getDelegate().getYAxisRect().contains(origin)) {
				TimeBarRow row = _tbv.getRowForXY(origin.x, origin.y);
				if (row != null) {
					e.startDrag(null, new StringSelection("Drag ROW " + row));
				}
			}

		}
	}

	/**
	 * Simple zoom action.
	 * 
	 * @author kliem
	 * @version $Id: EventMonitoringExample.java 1073 2010-11-22 21:25:33Z kliem
	 *          $
	 */
	class ZoomAction extends AbstractAction implements ISelectionRectListener {
		TimeBarViewer _tbv;

		public ZoomAction(TimeBarViewer tbv) {
			super("Zoom to selection");
			_tbv = tbv;
			setEnabled(false);
			_tbv.addSelectionRectListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			if (_tbv.getRegionRect() != null) {
				TBRect tbrect = _tbv.getRegionRect();
				JaretDate startDate = tbrect.startDate;
				int seconds = tbrect.endDate.diffSeconds(tbrect.startDate);
				int pixel = _tbv.getDelegate().getDiagramRect().width;
				double pps = ((double) pixel) / ((double) seconds);
				_tbv.clearRegionRect();
				_tbv.setPixelPerSecond(pps);
				_tbv.setStartDate(startDate);
				// TODO row scaling
			}
		}

		public void regionRectChanged(TimeBarViewerDelegate delegate,
				TBRect tbrect) {
			setEnabled(true);
		}

		public void regionRectClosed(TimeBarViewerDelegate delegate) {
			setEnabled(false);
		}

		public void selectionRectChanged(TimeBarViewerDelegate delegate,
				JaretDate beginDate, JaretDate endDate, List<TimeBarRow> rows) {
			// TODO Auto-generated method stub

		}

		public void selectionRectClosed(TimeBarViewerDelegate delegate) {
			// TODO Auto-generated method stub

		}

	}

	class ResetRegionSelectionAction extends AbstractAction {
		TimeBarViewer _tbv;

		public ResetRegionSelectionAction(TimeBarViewer tbv) {
			super("Remove selection");
			_tbv = tbv;
		}

		public void actionPerformed(ActionEvent e) {
			_tbv.clearRegionRect();
		}

	}

	class RunMarkerAction extends AbstractAction {
		TimeBarViewer _tbv;

		public RunMarkerAction(TimeBarViewer tbv) {
			super("Run marker");
			_tbv = tbv;
		}

		public void actionPerformed(ActionEvent e) {
			_tm.setDate(_tbv.getModel().getMinDate().copy());

			final Timer timer = new Timer(40, null);
			ActionListener al = new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					_tm.setDate(_tm.getDate().copy().advanceMillis(40));
					if (_tm.getDate().compareTo(_tbv.getModel().getMaxDate()) > 0) {
						timer.stop();
					}
				}

			};

			timer.addActionListener(al);
			timer.setRepeats(true);
			timer.setDelay(40);
			timer.start();
		}

	}

}
