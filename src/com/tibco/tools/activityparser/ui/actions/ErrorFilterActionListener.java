package com.tibco.tools.activityparser.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.tibco.tools.activityparser.models.Activity;
import com.tibco.tools.activityparser.models.ActivityCollection;
import com.tibco.tools.activityparser.models.FlatParseTableModel;
import com.tibco.tools.activityparser.models.LogTableCell;
import com.tibco.tools.activityparser.models.LogTableCellType;
import com.tibco.tools.activityparser.ui.extensions.ButtonTabComponent;
import com.tibco.tools.activityparser.ui.extensions.TableColumnAdjuster;
import com.tibco.tools.activityparser.ui.table.CustomCellRenderer;
import com.tibco.tools.activityparser.ui.table.CustomTable;
import com.tibco.tools.activityparser.utils.FlatModelHelper;

/**
 * The listener interface for receiving errorFilterAction events. The class that
 * is interested in processing a errorFilterAction event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addErrorFilterActionListener<code> method. When
 * the errorFilterAction event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author Jagdeesh Karicherla
 * 
 * @see ErrorFilterActionEvent
 */
public class ErrorFilterActionListener implements ActionListener {

	JTabbedPane tabPanel;

	/** The source model. */
	FlatParseTableModel sourceModel;

	public ErrorFilterActionListener(JTabbedPane jtp,
			FlatParseTableModel sourceModel) {
		super();
		this.tabPanel = jtp;
		this.sourceModel = sourceModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		SwingWorker<CustomTable, Void> customTableBuilder = new SwingWorker<CustomTable, Void>() {

			@Override
			protected CustomTable doInBackground() throws Exception {
				final CustomTable table = new CustomTable();
				final DefaultTableModel model = new DefaultTableModel();
				List<String> colNames = new ArrayList<String>();
				ActivityCollection errorActivities = sourceModel
						.getErrorActivityCollection();
				model.addColumn("TimeStamp");
				colNames.add("TimeStamp");
				for (int i = 0; i < errorActivities.getTimeStamps().size(); i++) {
					model.insertRow(i, new Object[] { errorActivities
							.getTimeStamps().get(i) });
				}

				for (int i = 0; i < errorActivities.getActivities().size(); i++) {
					Activity activity = errorActivities.getActivities().get(i);
					String threadName = activity.getThreadName();
					if (!colNames.contains(threadName)) {
						colNames.add(threadName);
						model.addColumn(threadName);
					}
					int startRow = errorActivities.getTimeStamps().indexOf(
							activity.getStartEvent().getTimeStamp());
					int stopRow = errorActivities.getTimeStamps().indexOf(
							activity.getEndEvent().getTimeStamp());
					LogTableCell startCell = new LogTableCell();
					String startValue = activity.isException() ? "exception"
							: "start";
					startCell.setCellValue(startValue);
					startCell.setToolTip(activity.getStartEvent().getMessage());
					startCell.setColumnNumber(colNames.indexOf(threadName));
					startCell.setRowNumber(startRow);
					startCell.setStartRowNumber(startRow);
					startCell.setEndRowNumber(stopRow);
					if (activity.getHasError()) {
						startCell.setType(LogTableCellType.ERROR);
					}
					model.setValueAt(startCell, startCell.getRowNumber(),
							startCell.getColumnNumber());

					LogTableCell endCell = new LogTableCell();
					String endValue = activity.isException() ? "exception"
							: "end";
					endCell.setCellValue(endValue);
					endCell.setToolTip(activity.getEndEvent().getMessage());
					endCell.setColumnNumber(colNames.indexOf(threadName));
					endCell.setRowNumber(stopRow);
					endCell.setStartRowNumber(startRow);
					endCell.setEndRowNumber(stopRow);
					if (activity.getHasError()) {
						endCell.setType(LogTableCellType.ERROR);
					}
					model.setValueAt(endCell, endCell.getRowNumber(),
							endCell.getColumnNumber());
					for (int row = startRow + 1; row <= stopRow - 1; row++) {
						LogTableCell emptyCell = new LogTableCell();
						emptyCell.setCellValue(" ");
						emptyCell.setToolTip(threadName);
						emptyCell.setColumnNumber(colNames.indexOf(threadName));
						emptyCell.setRowNumber(row);
						emptyCell.setStartRowNumber(startRow);
						emptyCell.setEndRowNumber(stopRow);
						emptyCell.setType(LogTableCellType.ERROR);
						model.setValueAt(emptyCell, emptyCell.getRowNumber(),
								emptyCell.getColumnNumber());

					}

				}
				table.setModel(model);
				for (int i = 0; i < table.getColumnCount(); i++) {
					table.getColumnModel().getColumn(i)
							.setCellRenderer(new CustomCellRenderer());
				}

				table.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						// showPopup(e);
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						showPopup(e);
					}

					void showPopup(MouseEvent e) {
						if ((e.isPopupTrigger())
								&& (e.getComponent() instanceof JTable)) {
							int[] selectedRowIndexes = table.getSelectedRows();
							JPopupMenu popup = new JPopupMenu();
							if (selectedRowIndexes.length > 1) {
								int rowIndexStart = table.getSelectionModel()
										.getMinSelectionIndex();
								int rowIndexEnd = table.getSelectionModel()
										.getMaxSelectionIndex();
								TableColumn selectedColumn = table
										.getColumnModel()
										.getColumn(
												table.getColumnModel()
														.getSelectionModel()
														.getLeadSelectionIndex());
								JMenuItem analyzeMenuItem = new JMenuItem(
										"Analyze this range separately");
								analyzeMenuItem
										.addActionListener(new FilterActionListener(
												tabPanel, model, rowIndexStart,
												rowIndexEnd, selectedColumn
														.getHeaderValue()
														.toString()));
								popup.add(analyzeMenuItem);
								// popup.show(e.getComponent(), e.getX(),
								// e.getY());
							} else {
								int focusRow = table.getSelectionModel()
										.getLeadSelectionIndex();
								int focusCol = table.getColumnModel()
										.getSelectionModel()
										.getLeadSelectionIndex();
								if (focusRow != -1 && focusCol != -1) {
									Object value = table.getModel().getValueAt(
											focusRow, focusCol);
									if (value != null
											&& value instanceof LogTableCell) {
										LogTableCell cellValue = (LogTableCell) value;
										JMenuItem parallelErrorActivityMenuItem = new JMenuItem(
												"Show error activites concurrent to this activity.");
										TableColumn selectedColumn = table
												.getColumnModel()
												.getColumn(
														table.getColumnModel()
																.getSelectionModel()
																.getLeadSelectionIndex());
										parallelErrorActivityMenuItem
												.addActionListener(new FilterActionListener(
														tabPanel,
														model,
														cellValue
																.getStartRowNumber(),
														cellValue
																.getEndRowNumber(),
														selectedColumn
																.getHeaderValue()
																.toString()));
										popup.add(parallelErrorActivityMenuItem);

										JMenuItem parallelActivityMenuItem = new JMenuItem(
												"Show all activites concurrent to this activity.");

										Date startDate = (Date) model.getValueAt(
												cellValue.getStartRowNumber(),
												0);
										int startDateIndex = sourceModel
												.getTimeStamps().indexOf(
														startDate);
										Date endDate = (Date) model.getValueAt(
												cellValue.getEndRowNumber(), 0);
										int endDateIndex = sourceModel
												.getTimeStamps().indexOf(
														endDate);
										parallelActivityMenuItem
												.addActionListener(new FilterActionListener(
														tabPanel,
														sourceModel,
														startDateIndex,
														endDateIndex,
														selectedColumn
																.getHeaderValue()
																.toString()));
										popup.add(parallelActivityMenuItem);

									}
								}
							}
							popup.show(e.getComponent(), e.getX(), e.getY());

						}
					}
				});

				TableColumnAdjuster tca = new TableColumnAdjuster(table);
				tca.adjustColumns();
				return table;
			}

			@Override
			public void done() {
				CustomTable filterTable;
				try {

					filterTable = get();
					JScrollPane scrollPane = new JScrollPane(filterTable,
							ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
							ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					String filterTabName = FlatModelHelper
							.getFilterTabName(tabPanel);
					tabPanel.add(filterTabName, scrollPane);

					tabPanel.setTabComponentAt(tabPanel.getTabCount() - 1,
							new ButtonTabComponent(filterTabName, tabPanel));
					tabPanel.setSelectedIndex(tabPanel.getTabCount() - 1);
					tabPanel.setToolTipTextAt(tabPanel.getTabCount() - 1,
							"show only error activities");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};
		customTableBuilder.execute();
	}
}
