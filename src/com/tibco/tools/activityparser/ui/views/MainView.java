package com.tibco.tools.activityparser.ui.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXBusyLabel;

import com.tibco.tools.activityparser.LogActivityFormatter;
import com.tibco.tools.activityparser.models.Activity;
import com.tibco.tools.activityparser.models.ActivityCollection;
import com.tibco.tools.activityparser.models.FlatParseTableModel;
import com.tibco.tools.activityparser.models.IUpdateInformable;
import com.tibco.tools.activityparser.models.LogParseWorker;
import com.tibco.tools.activityparser.models.LogTableCell;
import com.tibco.tools.activityparser.models.LogTableCellType;
import com.tibco.tools.activityparser.models.ParseResultModel;
import com.tibco.tools.activityparser.models.ParseResultTableWorker;
import com.tibco.tools.activityparser.ui.actions.ErrorFilterActionListener;
import com.tibco.tools.activityparser.ui.actions.FilterActionListener;
import com.tibco.tools.activityparser.ui.extensions.ColumnHeaderToolTips;
import com.tibco.tools.activityparser.ui.extensions.TableColumnAdjuster;
import com.tibco.tools.activityparser.ui.table.CustomCellRenderer;
import com.tibco.tools.activityparser.ui.table.CustomTable;
import com.tibco.tools.activityparser.ui.timebar.TimeBarView;
import com.tibco.tools.activityparser.ui.views.filters.LogFileFilter;
import com.tibco.tools.activityparser.utils.FlatModelHelper;
import com.tibco.tools.activityparser.utils.LogHelper;

/**
 * Application's main view.
 * 
 * @author Jagdeesh Karicherla
 */
public class MainView implements ActionListener {
	ParseResultModel initModel = null;
	JTextField logPropsPath;
	JTextField logFilePath;
	JPanel rightPanel = null;
	JTabbedPane mainTabPanel = null;
	Boolean underProgress = false;
	JCheckBox timeLineView;
	JCheckBox mixView;
	JCheckBox errorOnlyView;

	public void prepareLeftPanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		JPanel leftTopPlaceHolder = new JPanel();
		leftTopPlaceHolder.setBorder(new TitledBorder("File"));
		leftTopPlaceHolder.setLayout(new GridLayout(1, 1, 0, 0));
		leftTopPlaceHolder.add(CreateFileBrowsePanel());
		// loadFilesPanel.add(leftTopPlaceHolder);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;

		panel.add(leftTopPlaceHolder, c);

		JPanel keysPanel = new JPanel();
		keysPanel.setBorder(new TitledBorder("Key Navigation"));
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 0;
		keysPanel.setLayout(new BoxLayout(keysPanel, BoxLayout.Y_AXIS));
		JPanel jumpToStartPanel = new JPanel();
		jumpToStartPanel.setLayout(new BoxLayout(jumpToStartPanel,
				BoxLayout.X_AXIS));
		jumpToStartPanel.add(Box.createRigidArea(new Dimension(30, 0)));
		JLabel jumptStartLabel = new JLabel("Jump to start :");
		jumpToStartPanel.add(jumptStartLabel);
		jumpToStartPanel.add(Box.createRigidArea(new Dimension(30, 10)));
		JLabel jumptStartKeyLabel = new JLabel("BackSpace");
		jumpToStartPanel.add(jumptStartKeyLabel);
		// JButton jumpToStart = new JButton("Jump to Start");
		jumpToStartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		keysPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		keysPanel.add(jumpToStartPanel);

		JPanel jumpToEndPanel = new JPanel();
		jumpToEndPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		jumpToEndPanel
				.setLayout(new BoxLayout(jumpToEndPanel, BoxLayout.X_AXIS));
		JLabel jumptoEndLabel = new JLabel("Jump to end :  ");
		jumpToEndPanel.add(Box.createRigidArea(new Dimension(30, 10)));
		jumpToEndPanel.add(jumptoEndLabel);
		jumpToEndPanel.add(Box.createRigidArea(new Dimension(40, 10)));
		JLabel jumptoEndKeyLabel = new JLabel("Enter");
		jumpToEndPanel.add(jumptoEndKeyLabel);
		keysPanel.add(jumpToEndPanel);
		keysPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(keysPanel, c);

		JPanel colorsPanel = new JPanel();
		colorsPanel.setBorder(new TitledBorder("Cell Colors"));
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		colorsPanel.setLayout(new BoxLayout(colorsPanel, BoxLayout.Y_AXIS));
		colorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		JPanel errorColorPanel = new JPanel();
		errorColorPanel.setLayout(new BoxLayout(errorColorPanel,
				BoxLayout.X_AXIS));
		JLabel errorColorLabelL = new JLabel("Activities with exceptions :");
		errorColorPanel.add(Box.createRigidArea(new Dimension(30, 10)));
		errorColorPanel.add(errorColorLabelL);
		errorColorPanel.add(Box.createRigidArea(new Dimension(30, 10)));
		JTextField errorColorText = new JTextField();
		errorColorText.setEditable(true);
		errorColorText.setHorizontalAlignment(JTextField.CENTER);
		errorColorText.setBackground(LogActivityFormatter.getInstance()
				.getLogActivityFormat().ErrorColor);
		errorColorText.setText(LogHelper.getColorName(LogActivityFormatter
				.getInstance().getLogActivityFormat().ErrorColor));
		errorColorPanel.add(errorColorText);
		// JButton jumpToStart = new JButton("Jump to Start");
		errorColorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		colorsPanel.add(errorColorPanel);

		JPanel normalColorPanel = new JPanel();
		normalColorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		normalColorPanel.setLayout(new BoxLayout(normalColorPanel,
				BoxLayout.X_AXIS));
		normalColorPanel.add(Box.createRigidArea(new Dimension(30, 10)));
		JLabel normalColorLabelL = new JLabel("Normal Activity :");
		normalColorPanel.add(normalColorLabelL);
		normalColorPanel.add(Box.createRigidArea(new Dimension(90, 10)));
		JTextField normalColorText = new JTextField();
		normalColorText.setHorizontalAlignment(JTextField.CENTER);
		normalColorText.setEditable(true);
		normalColorText.setBackground(LogActivityFormatter.getInstance()
				.getLogActivityFormat().NormalColor);
		normalColorText.setText(LogHelper.getColorName(LogActivityFormatter
				.getInstance().getLogActivityFormat().NormalColor));
		normalColorPanel.add(normalColorText);
		colorsPanel.add(normalColorPanel);
		colorsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(colorsPanel, c);

	}

	public JPanel CreateLogFilePanel() {
		JLabel browseLogFileLabel = new JLabel();
		browseLogFileLabel.setText("Log File :  ");
		JTextField logFilePath = new JTextField(25);
		JButton browseLogFile = new JButton("Browse");
		JPanel logFileBrowseContainer = new JPanel();
		logFileBrowseContainer.add(browseLogFileLabel);
		logFileBrowseContainer.add(logFilePath);
		logFileBrowseContainer.add(browseLogFile);
		// logFileBrowseContainer.setPreferredSize(new Dimension(30,150));
		return logFileBrowseContainer;
	}

	public JPanel CreatePropertiesFilePanel() {
		JLabel browsePropsFileLabel = new JLabel();
		browsePropsFileLabel.setText("Properties File : ");
		JTextField logPropsPath = new JTextField(25);
		JButton browseLogPropsFile = new JButton("Browse");

		JPanel logPropsFileBrowseContainer = new JPanel();
		logPropsFileBrowseContainer.add(browsePropsFileLabel);
		logPropsFileBrowseContainer.add(logPropsPath);
		logPropsFileBrowseContainer.add(browseLogPropsFile);
		// logPropsFileBrowseContainer.setPreferredSize(new Dimension(30,150));
		return logPropsFileBrowseContainer;
	}

	public JPanel CreateFileBrowsePanel() {
		final JPanel logPropsFileBrowseContainer = new JPanel();
		logPropsPath = new JTextField(25);
		final JButton browseLogPropsFile = new JButton("Browse");
		final JCheckBox defaultPropsCheckBox = new JCheckBox(
				"Use Default Properties");
		logPropsFileBrowseContainer.setEnabled(false);
		JLabel browseLogFileLabel = new JLabel();
		browseLogFileLabel.setText("Log File :  ");
		logFilePath = new JTextField(25);
		JButton browseLogFile = new JButton("Browse");
		browseLogFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new LogFileFilter());
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File chosenFile = fileChooser.getSelectedFile();
					String path = chosenFile.getPath();
					logFilePath.setText(path);
				} else if (returnVal == JFileChooser.CANCEL_OPTION) {
				}

			}
		});
		JPanel logFileBrowseContainer = new JPanel();
		logFileBrowseContainer.setLayout(new BoxLayout(logFileBrowseContainer,
				BoxLayout.X_AXIS));
		logFileBrowseContainer.add(browseLogFileLabel);
		logFileBrowseContainer.add(logFilePath);
		logFileBrowseContainer.add(browseLogFile);

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent
						.getSource();
				boolean selected = abstractButton.getModel().isSelected();
				if (selected) {
					logPropsPath.setEnabled(false);
					browseLogPropsFile.setEnabled(false);

				} else
					logPropsPath.setEnabled(true);
				browseLogPropsFile.setEnabled(true);
			}
		};
		browseLogPropsFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!defaultPropsCheckBox.isSelected()) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.addChoosableFileFilter(new LogFileFilter());
					int returnVal = fileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File chosenFile = fileChooser.getSelectedFile();
						String path = chosenFile.getPath();
						logPropsPath.setText(path);
					} else if (returnVal == JFileChooser.CANCEL_OPTION) {
					}

				}
			}
		});
		JPanel checkBoxContainer = new JPanel();
		checkBoxContainer.setLayout(new GridLayout(1, 3, 10, 10));
		defaultPropsCheckBox.setSelected(false);
		defaultPropsCheckBox.addActionListener(actionListener);
		checkBoxContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
		checkBoxContainer.add(defaultPropsCheckBox);

		JLabel browsePropsFileLabel = new JLabel();
		browsePropsFileLabel.setText("Properties File : ");
		logPropsFileBrowseContainer.setLayout(new BoxLayout(
				logPropsFileBrowseContainer, BoxLayout.X_AXIS));
		logPropsFileBrowseContainer.add(browsePropsFileLabel);
		logPropsFileBrowseContainer.add(logPropsPath);
		logPropsFileBrowseContainer.add(browseLogPropsFile);

		JPanel fileBrowsePanel = new JPanel();
		// fileBrowsePanel
		// .applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		// fileBrowsePanel.setLayout(new GridLayout(4, 1, 10, 10));
		fileBrowsePanel.setLayout(new BoxLayout(fileBrowsePanel,
				BoxLayout.Y_AXIS));
		fileBrowsePanel.add(logFileBrowseContainer);
		fileBrowsePanel.add(checkBoxContainer);
		fileBrowsePanel.add(logPropsFileBrowseContainer);

		mixView = new JCheckBox(
				"Show Main Grid view [ error & non-error activitites]");
		timeLineView = new JCheckBox("Show time-line view");
		errorOnlyView = new JCheckBox("Show 'error only' view");
		mixView.setSelected(true);
		mixView.addActionListener(actionListener);

		timeLineView.setSelected(true);
		timeLineView.addActionListener(actionListener);

		JPanel viewOptionsContainer = new JPanel();
		// viewOptionsContainer.setLayout(new BoxLayout(viewOptionsContainer,
		// BoxLayout.Y_AXIS));
		viewOptionsContainer.setLayout(new GridLayout(3, 1, 10, 10));
		errorOnlyView.setSelected(false);
		errorOnlyView.addActionListener(actionListener);
		viewOptionsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
		viewOptionsContainer.add(mixView);
		viewOptionsContainer.add(timeLineView);
		viewOptionsContainer.add(errorOnlyView);
		fileBrowsePanel.add(viewOptionsContainer);

		JPanel analyzeButtonPanel = new JPanel();
		analyzeButtonPanel.setBorder(new EmptyBorder(10, 0, 10, 10));
		JButton parseButton = new JButton("Analyze");
		parseButton.addActionListener(this);
		analyzeButtonPanel.add(parseButton);
		fileBrowsePanel.add(analyzeButtonPanel);
		return fileBrowsePanel;
	}

	JXBusyLabel busyLabel = null;

	public void createTabs(JPanel rightPanel) {
		this.rightPanel = rightPanel;
		this.busyLabel = (JXBusyLabel) rightPanel.getComponent(0);
		this.mainTabPanel = (JTabbedPane) rightPanel.getComponent(1);
	}

	public void setErrorOnlyTable(final FlatParseTableModel sourceModel) {
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
												mainTabPanel, model,
												rowIndexStart, rowIndexEnd,
												selectedColumn.getHeaderValue()
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
										TableColumn selectedColumn = table
												.getColumnModel().getColumn(
														focusCol);

										LogTableCell cellValue = (LogTableCell) value;
										// JMenuItem
										// parallelErrorActivityMenuItem = new
										// JMenuItem(
										// "Show error activites concurrent to this activity.");
										// parallelErrorActivityMenuItem
										// .addActionListener(new
										// FilterActionListener(
										// mainTabPanel,
										// model,
										// cellValue
										// .getStartRowNumber(),
										// cellValue
										// .getEndRowNumber(),
										// selectedColumn
										// .getHeaderValue()
										// .toString()));
										// popup.add(parallelErrorActivityMenuItem);

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
														mainTabPanel,
														sourceModel,
														startDateIndex,
														endDateIndex,
														selectedColumn
																.getHeaderValue()
																.toString()));
										popup.add(parallelActivityMenuItem);

										JMenuItem parallelErrorActivityMenuItem = new JMenuItem(
												"Show error activites concurrent to this activity.");
										parallelErrorActivityMenuItem
												.addActionListener(new FilterActionListener(
														mainTabPanel,
														model,
														cellValue
																.getStartRowNumber(),
														cellValue
																.getEndRowNumber(),
														selectedColumn
																.getHeaderValue()
																.toString()));
										popup.add(parallelErrorActivityMenuItem);

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
					if (filterTable.getRowCount() > 0) {
						JScrollPane scrollPane = new JScrollPane(
								filterTable,
								ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
								ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						String filterTabName = FlatModelHelper
								.getFilterTabName(mainTabPanel);
						mainTabPanel.add(filterTabName, scrollPane);

						// mainTabPanel
						// .setTabComponentAt(mainTabPanel.getTabCount() - 1,
						// new ButtonTabComponent(filterTabName,
						// mainTabPanel));
						mainTabPanel.setSelectedIndex(mainTabPanel
								.getTabCount() - 1);
						mainTabPanel.setToolTipTextAt(
								mainTabPanel.getTabCount() - 1,
								"show only error activities");
					}
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

	public void setTable(final FlatParseTableModel defaultTableModel) {

		final CustomTable dataTable = new CustomTable();

		JScrollPane scrollPane = new JScrollPane(dataTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dataTable.setModel(defaultTableModel);
		for (int i = 0; i < dataTable.getColumnModel().getColumnCount(); i++) {
			dataTable.getColumnModel().getColumn(i)
					.setCellRenderer(new CustomCellRenderer());
		}

		JTableHeader header = dataTable.getTableHeader();
		ColumnHeaderToolTips headerToolTips = new ColumnHeaderToolTips();
		// Assign a tooltip for each of the columns
		for (int c = 0; c < dataTable.getColumnCount(); c++) {
			TableColumn col = dataTable.getColumnModel().getColumn(c);
			headerToolTips.setToolTip(col, col.getHeaderValue().toString());
		}
		header.addMouseMotionListener(headerToolTips);
		// TableColumnAdjuster tca = new TableColumnAdjuster(dataTable);
		// tca.adjustColumns();
		dataTable.setFillsViewportHeight(false);
		dataTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		dataTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopup(e);
			}

			void showPopup(MouseEvent e) {
				if ((e.isPopupTrigger())
						&& (e.getComponent() instanceof JTable)) {
					int[] selectedRowIndexes = dataTable.getSelectedRows();
					JPopupMenu popup = new JPopupMenu();
					if (selectedRowIndexes.length > 1) {
						int rowIndexStart = dataTable.getSelectionModel()
								.getMinSelectionIndex();
						int rowIndexEnd = dataTable.getSelectionModel()
								.getMaxSelectionIndex();
						TableColumn selectedColumn = dataTable.getColumnModel()
								.getColumn(
										dataTable.getColumnModel()
												.getSelectionModel()
												.getLeadSelectionIndex());
						JMenuItem analyzeMenuItem = new JMenuItem(
								"Analyze this range separately");
						analyzeMenuItem
								.addActionListener(new FilterActionListener(
										mainTabPanel, defaultTableModel,
										rowIndexStart, rowIndexEnd,
										selectedColumn.getHeaderValue()
												.toString()));
						popup.add(analyzeMenuItem);
						// popup.show(e.getComponent(), e.getX(), e.getY());
					} else {
						int focusRow = dataTable.getSelectionModel()
								.getLeadSelectionIndex();
						int focusCol = dataTable.getColumnModel()
								.getSelectionModel().getLeadSelectionIndex();
						if (focusRow != -1 && focusCol != -1) {
							Object value = dataTable.getModel().getValueAt(
									focusRow, focusCol);
							if (value != null && value instanceof LogTableCell) {
								TableColumn selectedColumn = dataTable
										.getColumnModel()
										.getColumn(
												dataTable
														.getColumnModel()
														.getSelectionModel()
														.getLeadSelectionIndex());
								LogTableCell cellValue = (LogTableCell) value;
								dataTable.getValueAt(focusRow, focusCol);
								JMenuItem parallelActivityMenuItem = new JMenuItem(
										"Show activites concurrent to this activity.");
								parallelActivityMenuItem
										.addActionListener(new FilterActionListener(
												mainTabPanel,
												defaultTableModel, cellValue
														.getStartRowNumber(),
												cellValue.getEndRowNumber(),
												selectedColumn.getHeaderValue()
														.toString()));
								popup.add(parallelActivityMenuItem);

							}
						}
					}

					JMenuItem showErrorsMenuItem = new JMenuItem(
							"Show only error activities.");
					showErrorsMenuItem
							.addActionListener(new ErrorFilterActionListener(
									mainTabPanel, defaultTableModel));
					popup.addSeparator();
					popup.add(showErrorsMenuItem);
					popup.show(e.getComponent(), e.getX(), e.getY());

				}
			}
		});

		// Very heavy operation.check with Larry.
		if (mainTabPanel.indexOfTab("Grid") == -1)
			mainTabPanel.add("Grid", scrollPane);
		else {
			mainTabPanel.remove(mainTabPanel.indexOfTab("Grid"));
			mainTabPanel.add("Grid", scrollPane);
		}
		underProgress = false;
		// TableColumnAdjuster tca = new
		// TableColumnAdjuster(fct.getFixedTable());
		// tca.adjustColumns();
		scrollPane.revalidate();
		scrollPane.repaint();

	}

	private void setTimeBar(ParseResultModel message) {
		busyLabel.setBusy(false);
		busyLabel.setVisible(false);
		busyLabel.setEnabled(false);
		TimeBarView example = new TimeBarView();
		final JPanel timeBarPanel = example.buildTimeBar(message);
		if (!(mainTabPanel.indexOfTab("TimeBar") == -1)) {
			mainTabPanel.remove(mainTabPanel.indexOfTab("TimeBar"));
		}
		JScrollPane timeBarScrollPane = new JScrollPane(timeBarPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// timeBarScrollPane.add(timeBarPanel);
		mainTabPanel.add("TimeBar", timeBarScrollPane);
		mainTabPanel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				JTabbedPane pane = (JTabbedPane) evt.getSource();
				int selectedTabIndex = pane.getSelectedIndex();
				if (pane.indexOfTab("TimeBar") == selectedTabIndex) {
					timeBarPanel.requestFocusInWindow();
				}
				// pane.getComponent(selectedTabIndex).setF
			}
		});
		timeBarPanel.requestFocusInWindow();
	}

	public void actionPerformed(ActionEvent actionEvent) {
		String command = actionEvent.getActionCommand();
		if (command.equals("Analyze") && !underProgress) {
			if (logFilePath.getText().length() > 0) {
				underProgress = true;
				String chosenLogFilePath = logFilePath.getText();
				busyLabel.setVisible(true);
				busyLabel.setBusy(true);
				busyLabel.setEnabled(true);
				busyLabel.setToolTipText("Loading...");
				IUpdateInformable informable = new IUpdateInformable() {
					@Override
					public void messagePublished(Object message) {
						if (message instanceof ParseResultModel) {
							int tabCount = mainTabPanel.getTabCount() - 1;
							while (tabCount >= 1) {
								mainTabPanel.remove(tabCount);
								tabCount--;
							}
							if (errorOnlyView.isSelected()
									|| mixView.isSelected()
									|| timeLineView.isSelected()) {
								if (errorOnlyView.isSelected()
										|| mixView.isSelected())
									showTable((ParseResultModel) message);
								if (timeLineView.isSelected()) {
									setTimeBar((ParseResultModel) message);
								}
							} else {
								// nothing selected
								busyLabel.setBusy(false);
								busyLabel.setVisible(false);
								busyLabel.setEnabled(false);
							}
							// setGanttChart((ParseResultModel) message);
						}
					}

					public void showTable(ParseResultModel parseModel) {
						ParseResultTableWorker flatModelDaemon = new ParseResultTableWorker(
								parseModel) {
							@Override
							protected void done() {
								busyLabel.setBusy(false);
								busyLabel.setVisible(false);
								busyLabel.setEnabled(false);
								FlatParseTableModel flatModel;
								try {
									flatModel = (FlatParseTableModel) get();
									if (errorOnlyView.isSelected())
										setErrorOnlyTable(flatModel);
									if (mixView.isSelected())
										setTable(flatModel);
									underProgress = false;
								} catch (InterruptedException e) {

									e.printStackTrace();
								} catch (ExecutionException e) {
									e.printStackTrace();
								}

							}

						};
						flatModelDaemon.execute();
						initModel = null;
					}
				};

				// "" empty props file means default.
				LogParseWorker worker = new LogParseWorker(
						chosenLogFilePath.trim(), logPropsPath.getText()
								.length() > 0 ? logPropsPath.getText().trim()
								: "", informable) {
					// This method is invoked when the worker is finished with
					// its task
					@Override
					protected void done() {
						try {
							// progressBar.setVisible(false);
						} catch (Exception e) {
						}
					}
				};
				// A property listener used to update the progress bar
				PropertyChangeListener listener = new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub

					}
				};
				worker.addPropertyChangeListener(listener);

				// Start the worker.
				worker.execute();
			}

		} else if (command.equals("Exit")) {

		}

	}
}
