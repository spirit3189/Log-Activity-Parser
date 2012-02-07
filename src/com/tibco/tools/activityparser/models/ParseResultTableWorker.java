package com.tibco.tools.activityparser.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import com.tibco.tools.activityparser.LogActivityFormatter;

/**
 * The Class ParseResultTableWorker is a background worker which paints each
 * cell into table model which is later binded to Jtable.
 * 
 * @author Jagdeesh Karicherla
 */

public class ParseResultTableWorker extends
		SwingWorker<DefaultTableModel, Object[]> {

	/** The init model. */
	private final ParseResultModel initModel;

	// private DefaultTableModel parsedFlatModel;

	/**
	 * Instantiates a new parses the result table worker.
	 * 
	 * @param initModel
	 *            the init model
	 */
	public ParseResultTableWorker(ParseResultModel initModel) {
		this.initModel = initModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected DefaultTableModel doInBackground() throws Exception {
		FlatParseTableModel parsedFlatModel = new FlatParseTableModel();
		parsedFlatModel.setErrorActivities(initModel.getErrorActivities());
		parsedFlatModel.setTimeStamps(initModel.getTimeStamps());
		parsedFlatModel.addColumn("TimeStamp");
		fillColumnNames(parsedFlatModel);
		fillTimeStampColumn(parsedFlatModel);

		for (Object keyMap : initModel.getActivitiesGroupedByThread().keySet()) {
			String threadName = keyMap.toString();
			LogActivityBuilder activityBuilder = initModel
					.getActivitiesGroupedByThread().get(keyMap);
			for (int i = 0; i < activityBuilder.getActivityList().size(); i++) {
				try {
					Activity activity = activityBuilder.getActivityList()
							.get(i);

					int threadCount = initModel.getThreadNames().indexOf(
							threadName);
					int startRow = initModel.getTimeStamps().indexOf(
							activity.getStartEvent().getTimeStamp());
					int stopRow = initModel.getTimeStamps().indexOf(
							activity.getEndEvent().getTimeStamp());
					LogTableCell startCell = new LogTableCell();
					String startValue = "start";
					if (activity.isHasPseudoStart()) {
						startValue = startValue
								+ LogActivityFormatter.getInstance()
										.getLogActivityFormat().PseudoSuffix;
					}
					if (activity.isException())
						startValue = "exception";
					startCell.setCellValue(startValue);
					startCell.setToolTip(activity.getStartEvent().getMessage());
					startCell.setColumnNumber(threadCount + 1);
					startCell.setRowNumber(startRow);
					startCell.setStartRowNumber(startRow);
					startCell.setEndRowNumber(stopRow);
					if (activity.getHasError()) {
						startCell.setType(LogTableCellType.ERROR);
					} else
						startCell.setType(LogTableCellType.ACTIVITY);

					Object existingStartCellValue = parsedFlatModel.getValueAt(
							startCell.getRowNumber(),
							startCell.getColumnNumber());
					if (null == existingStartCellValue)
						parsedFlatModel.setValueAt(startCell,
								startCell.getRowNumber(),
								startCell.getColumnNumber());
					else if (existingStartCellValue instanceof LogTableCell) {
						LogTableCell cellValue = (LogTableCell) existingStartCellValue;
						List<LogTableCell> cellValues = new ArrayList<LogTableCell>();
						cellValues.add(cellValue);
						cellValues.add(startCell);
						parsedFlatModel.setValueAt(cellValues,
								startCell.getRowNumber(),
								startCell.getColumnNumber());
					} else if (existingStartCellValue instanceof List<?>) {
						List<LogTableCell> cellValue = (List<LogTableCell>) existingStartCellValue;
						cellValue.add(startCell);
						parsedFlatModel.setValueAt(cellValue,
								startCell.getRowNumber(),
								startCell.getColumnNumber());

					}

					LogTableCell endCell = new LogTableCell();
					String endValue = "end";
					if (activity.isHasPseudoEnd()) {
						endValue = endValue
								+ LogActivityFormatter.getInstance()
										.getLogActivityFormat().PseudoSuffix;
					}
					if (activity.isException())
						endValue = "exception";
					endCell.setCellValue(endValue);
					endCell.setToolTip(activity.getEndEvent().getMessage());
					endCell.setColumnNumber(threadCount + 1);
					endCell.setRowNumber(stopRow);
					endCell.setStartRowNumber(startRow);
					endCell.setEndRowNumber(stopRow);

					// if (activity.getStartEvent().getTimeStamp()
					// .compareTo(activity.getEndEvent().getTimeStamp()) == 0) {
					// System.out.println("xthread name" + threadName);
					// }
					if (!activity.isException()) {
						if (activity.getHasError()) {
							endCell.setType(LogTableCellType.ERROR);
						} else
							endCell.setType(LogTableCellType.ACTIVITY);
						Object existingEndCellValue = parsedFlatModel
								.getValueAt(endCell.getRowNumber(),
										endCell.getColumnNumber());
						if (null == existingEndCellValue)
							parsedFlatModel.setValueAt(endCell,
									endCell.getRowNumber(),
									endCell.getColumnNumber());
						else if (existingEndCellValue instanceof LogTableCell) {
							LogTableCell cellValue = (LogTableCell) existingEndCellValue;
							List<LogTableCell> cellValues = new ArrayList<LogTableCell>();
							cellValues.add(cellValue);
							cellValues.add(endCell);
							parsedFlatModel.setValueAt(cellValues,
									endCell.getRowNumber(),
									endCell.getColumnNumber());
						} else if (existingStartCellValue instanceof List<?>) {
							List<LogTableCell> cellValue = (List<LogTableCell>) existingEndCellValue;
							cellValue.add(endCell);
							parsedFlatModel.setValueAt(cellValue,
									endCell.getRowNumber(),
									endCell.getColumnNumber());

						}

					}

					if (activity.getHasError()) {
						setEmptyCells(parsedFlatModel, startRow + 1,
								stopRow - 1, threadCount + 1,
								LogTableCellType.ERROR);
					} else
						setEmptyCells(parsedFlatModel, startRow + 1,
								stopRow - 1, threadCount + 1,
								LogTableCellType.ACTIVITY);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return parsedFlatModel;
	}

	/**
	 * Fill column names.
	 * 
	 * @param parsedFlatModel
	 *            the parsed flat model
	 */
	private void fillColumnNames(DefaultTableModel parsedFlatModel) {
		for (String columnName : initModel.getThreadNames())
			parsedFlatModel.addColumn(columnName);
	}

	/**
	 * Fill time stamp column.
	 * 
	 * @param parsedFlatModel
	 *            the parsed flat model
	 */
	private void fillTimeStampColumn(DefaultTableModel parsedFlatModel) {
		for (int i = 0; i < initModel.getTimeStamps().size(); i++) {
			parsedFlatModel.insertRow(i, new Object[] { initModel
					.getTimeStamps().get(i) });
		}
	}

	/**
	 * Sets the empty cells.
	 * 
	 * @param parsedFlatModel
	 *            the parsed flat model
	 * @param startRow
	 *            the start row
	 * @param endRow
	 *            the end row
	 * @param columnNumber
	 *            the column number
	 * @param cellType
	 *            the cell type
	 */
	private void setEmptyCells(DefaultTableModel parsedFlatModel, int startRow,
			int endRow, int columnNumber, LogTableCellType cellType) {
		for (int i = startRow; i <= endRow; i++) {
			LogTableCell emptyCell = new LogTableCell();
			emptyCell.setCellValue(" ");

			String startDate = new SimpleDateFormat(LogActivityFormatter
					.getInstance().getLogActivityFormat().TimeStampFormat)
					.format((Date) parsedFlatModel.getValueAt(startRow - 1, 0));
			String endDate = new SimpleDateFormat(LogActivityFormatter
					.getInstance().getLogActivityFormat().TimeStampFormat)
					.format((Date) parsedFlatModel.getValueAt(endRow + 1, 0));
			emptyCell.setToolTip("<html>Start Time :" + startDate + "<br>"
					+ "End Time :" + endDate + "<br>" + "</html>");
			emptyCell.setColumnNumber(columnNumber);
			emptyCell.setRowNumber(i);
			emptyCell.setStartRowNumber(startRow - 1);
			emptyCell.setEndRowNumber(endRow + 1);
			emptyCell.setType(cellType);
			parsedFlatModel.setValueAt(emptyCell, emptyCell.getRowNumber(),
					emptyCell.getColumnNumber());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#process(java.util.List)
	 */
	@Override
	protected void process(List<Object[]> chunks) {
		// for (Object[] row : chunks) {
		// model.addRow(row);
		// }
	}
}