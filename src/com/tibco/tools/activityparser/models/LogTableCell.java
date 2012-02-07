package com.tibco.tools.activityparser.models;

import java.io.Serializable;

/**
 * Represents a cell in the main table.
 * @author Jagdeesh Karicherla
 */
public class LogTableCell implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 200906292210L;
	
	/** The cell value. */
	private String cellValue;
	
	/** The row number. */
	private int rowNumber;
	
	/** The column number. */
	private int columnNumber;
	
	/** The start row number. */
	private int startRowNumber;
	
	/** The end row number. */
	private int endRowNumber;
	
	/** The highlight. */
	private Boolean highlight = false;
	
	/** The tool tip. */
	private String toolTip = "";
	
	/** The error. */
	private Boolean error = false;
	
	/** The TYPE. */
	private LogTableCellType TYPE = LogTableCellType.NORMAL;


	/**
	 * Gets the start row number.
	 *
	 * @return the start row number
	 */
	public int getStartRowNumber() {
		return startRowNumber;
	}

	/**
	 * Sets the start row number.
	 *
	 * @param startRowNumber the new start row number
	 */
	public void setStartRowNumber(int startRowNumber) {
		this.startRowNumber = startRowNumber;
	}

	/**
	 * Gets the end row number.
	 *
	 * @return the end row number
	 */
	public int getEndRowNumber() {
		return endRowNumber;
	}

	/**
	 * Sets the end row number.
	 *
	 * @param endRowNumber the new end row number
	 */
	public void setEndRowNumber(int endRowNumber) {
		this.endRowNumber = endRowNumber;
	}
	
	/**
	 * Gets the cell value.
	 *
	 * @return the cell value
	 */
	public String getCellValue() {
		return cellValue;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(LogTableCellType type) {
		this.TYPE = type;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public LogTableCellType getType() {
		return this.TYPE;
	}

	/**
	 * Sets the cell value.
	 *
	 * @param cellValue the new cell value
	 */
	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}

	/**
	 * Gets the row number.
	 *
	 * @return the row number
	 */
	public int getRowNumber() {
		return rowNumber;
	}

	/**
	 * Sets the row number.
	 *
	 * @param rowNumber the new row number
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * Gets the column number.
	 *
	 * @return the column number
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * Sets the column number.
	 *
	 * @param columnNumber the new column number
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	/**
	 * Checks for highlight.
	 *
	 * @return the boolean
	 */
	public Boolean hasHighlight() {
		return highlight;
	}

	/**
	 * Sets the highlight.
	 *
	 * @param highlight the new highlight
	 */
	public void setHighlight(Boolean highlight) {
		this.highlight = highlight;
	}

	/**
	 * Gets the tool tip.
	 *
	 * @return the tool tip
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * Sets the tool tip.
	 *
	 * @param toolTip the new tool tip
	 */
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.cellValue;
	}

	/**
	 * Checks for error.
	 *
	 * @return the boolean
	 */
	public Boolean hasError() {
		return error;
	}

	/**
	 * Sets the error.
	 *
	 * @param error the new error
	 */
	public void setError(Boolean error) {
		this.error = error;
	}

}
