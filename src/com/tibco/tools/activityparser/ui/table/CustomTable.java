package com.tibco.tools.activityparser.ui.table;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import com.tibco.tools.activityparser.models.LogTableCell;

// TODO: Auto-generated Javadoc
/**
 * Custom JTable with basic operations common for all tables.
 * 
 * @author Jagdeesh Karicherla
 */
public class CustomTable extends JTable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3289302433207846933L;

	/**
	 * Instantiates a new custom table.
	 */
	public CustomTable() {
		super();
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Individual cell selection is enabled
		this.setCellSelectionEnabled(true);

		ListSelectionModel cellSelectionModel = this.getSelectionModel();
		cellSelectionModel
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		AbstractAction handleStartKey = new AbstractAction() {
			private static final long serialVersionUID = 5474571687083161202L;

			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int selectedRow = table.getSelectedRow();
				int selectedColumn = table.getSelectedColumn();
				Object value = table.getValueAt(selectedRow, selectedColumn);
				if (value instanceof LogTableCell) {
					LogTableCell selectedLogTableCell = (LogTableCell) table
							.getValueAt(selectedRow, selectedColumn);
					table.changeSelection(
							selectedLogTableCell.getStartRowNumber(),
							selectedColumn, false, false);
					table.requestFocus();
				}
			}
		};

		AbstractAction handleEndKey = new AbstractAction() {
			private static final long serialVersionUID = -5275451066463930079L;

			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int selectedRow = table.getSelectedRow();
				int selectedColumn = table.getSelectedColumn();
				Object value = table.getValueAt(selectedRow, selectedColumn);
				if (value instanceof LogTableCell) {
					LogTableCell selectedLogTableCell = (LogTableCell) table
							.getValueAt(selectedRow, selectedColumn);
					table.changeSelection(
							selectedLogTableCell.getEndRowNumber(),
							selectedColumn, false, false);
					table.requestFocus();
				}
			}
		};
		this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "handleEnter");
		this.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
				"handleBackSpace");
		this.getActionMap().put("handleEnter", handleEndKey);
		this.getActionMap().put("handleBackSpace", handleStartKey);
	}

	public void scrollToVisible(int rowIndex, int vColIndex) {
		if (!(this.getParent() instanceof JViewport)) {
			return;
		}
		JViewport viewport = (JViewport) this.getParent();

		// This rectangle is relative to the table where the
		// northwest corner of cell (0,0) is always (0,0).
		Rectangle rect = this.getCellRect(rowIndex, vColIndex, true);

		// The location of the viewport relative to the table
		// Point pt = viewport.getViewPosition();
		Rectangle r2 = viewport.getVisibleRect();
		// Translate the cell location so that it is relative
		// to the view, assuming the northwest corner of the
		// view is (0,0)
		// rect.setLocation(rect.x - pt.x, rect.y - pt.y);
		this.scrollRectToVisible(new Rectangle(rect.x, rect.y, (int) r2
				.getWidth(), (int) r2.getHeight()));

		// Scroll the area into view
		// viewport.scrollRectToVisible(rect);
	}

}
