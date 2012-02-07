package com.tibco.tools.activityparser.ui.actions;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.tibco.tools.activityparser.ui.extensions.ButtonTabComponent;
import com.tibco.tools.activityparser.ui.extensions.ColumnHeaderToolTips;
import com.tibco.tools.activityparser.ui.extensions.TableColumnAdjuster;
import com.tibco.tools.activityparser.ui.table.CustomCellRenderer;
import com.tibco.tools.activityparser.ui.table.CustomTable;
import com.tibco.tools.activityparser.utils.FlatModelHelper;

/**
 * The listener interface for receiving filterAction events. The class that is
 * interested in processing a filterAction event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addFilterActionListener<code> method. When
 * the filterAction event occurs, that object's appropriate
 * method is invoked.
 * /**
 * 
 * @author Jagdeesh Karicherla
 * @see FilterActionEvent
 */
public class FilterActionListener implements ActionListener {

	JTabbedPane tabPanel;

	/** The source model. */
	DefaultTableModel sourceModel;

	/** The row start. */
	int rowStart;

	/** The row end. */
	int rowEnd;
	int selectedColumn = 0;

	String selectedParentColumnName = "";

	/**
	 * Instantiates a new filter action listener.
	 * 
	 * @param jtp
	 *            the jtp
	 * @param sourceModel
	 *            the source model
	 * @param rowStart
	 *            the row start
	 * @param rowEnd
	 *            the row end
	 */
	public FilterActionListener(JTabbedPane jtp, DefaultTableModel sourceModel,
			int rowStart, int rowEnd, String selectedColumnName) {
		super();
		this.tabPanel = jtp;
		this.sourceModel = sourceModel;
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
		this.selectedParentColumnName = selectedColumnName;
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
				CustomTable filterTable = new CustomTable();
				filterTable.setModel(FlatModelHelper.filterColumns(sourceModel,
						rowStart, rowEnd));
				for (int i = 0; i < filterTable.getColumnCount(); i++) {
					filterTable.getColumnModel().getColumn(i)
							.setCellRenderer(new CustomCellRenderer());
				}
				JTableHeader header = filterTable.getTableHeader();

				ColumnHeaderToolTips headerToolTips = new ColumnHeaderToolTips();
				// Assign a tooltip for each of the columns
				for (int c = 0; c < filterTable.getColumnCount(); c++) {
					TableColumn col = filterTable.getColumnModel().getColumn(c);
					headerToolTips.setToolTip(col, col.getHeaderValue()
							.toString());
					if (col.getHeaderValue().toString() == selectedParentColumnName) {
						selectedColumn = c;
					}
				}
				header.addMouseMotionListener(headerToolTips);
				TableColumnAdjuster tca = new TableColumnAdjuster(filterTable);
				tca.adjustColumns();

				// filterTable.scrollToVisible(0, selectedColumn);
				return filterTable;
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
					Rectangle visibleRect = ((JViewport) filterTable
							.getParent()).getVisibleRect();
					filterTable.scrollRectToVisible(new Rectangle(filterTable
							.getColumnModel().getColumn(selectedColumn)
							.getWidth()
							* selectedColumn, filterTable.getRowHeight() * (0),
							(int) visibleRect.getWidth(), (int) visibleRect
									.getHeight()));

					// filterTable.scrollRectToVisible(new Rectangle(filterTable
					// .getColumnModel()
					// .getColumn(filterTable.getColumnCount() - 1)
					// .getWidth()
					// * filterTable.getColumnCount(), filterTable
					// .getRowHeight() * (1), 10, filterTable
					// .getRowHeight()));

					// filterTable
					// .scrollToVisible(0, filterTable.getColumnCount());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			}

		};
		customTableBuilder.execute();
	}
}
