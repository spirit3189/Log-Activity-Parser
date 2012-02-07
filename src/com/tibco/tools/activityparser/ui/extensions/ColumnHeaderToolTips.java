package com.tibco.tools.activityparser.ui.extensions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ColumnHeaderToolTips.
 */
public class ColumnHeaderToolTips extends MouseMotionAdapter {

	/** The cur col. */
	TableColumn curCol;

	/** The tips. */
	Map<TableColumn, String> tips = new HashMap<TableColumn, String>();

	/**
	 * Sets the tool tip.
	 * 
	 * @param col
	 *            the col
	 * @param tooltip
	 *            the tooltip
	 */
	public void setToolTip(TableColumn col, String tooltip) {
		if (tooltip == null) {
			tips.remove(col);
		} else {
			tips.put(col, tooltip);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionAdapter#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent evt) {
		TableColumn col = null;
		JTableHeader header = (JTableHeader) evt.getSource();
		JTable table = header.getTable();
		TableColumnModel colModel = table.getColumnModel();
		int vColIndex = colModel.getColumnIndexAtX(evt.getX());

		// Return if not clicked on any column header
		if (vColIndex >= 0) {
			col = colModel.getColumn(vColIndex);
		}

		if (col != curCol) {
			header.setToolTipText((String) tips.get(col));
			curCol = col;
		}
	}
}
