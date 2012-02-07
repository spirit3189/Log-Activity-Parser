package com.tibco.tools.activityparser.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;

/**
 * A helper class for custom table & flat model.
 * 
 * @author Jagdeesh Karicherla
 */
public class FlatModelHelper {

	public static String getFilterTabName(JTabbedPane jtp) {
		String filterTabName = "Filter";

		int count = 1;
		while (count > 0) {
			if (jtp.indexOfTab(filterTabName + count) == -1) {
				filterTabName = filterTabName + count;
				// jtp.add(filterTabName, scrollPane);
				count = -1;
			}
			count++;
		}
		return filterTabName;
	}

	public static DefaultTableModel filterColumns(
			DefaultTableModel sourceModel, int startRow, int endRow) {
		DefaultTableModel model = new DefaultTableModel();
		Vector<?> data = sourceModel.getDataVector();
		for (int column = 0; column < sourceModel.getColumnCount(); column++) {
			boolean nullColumn = true;
			List<Object> colData = new ArrayList<Object>(
					sourceModel.getRowCount());
			for (int i = startRow; i <= endRow; i++) {
				Vector<?> row = (Vector<?>) data.elementAt(i);
				Object cellData = row.get(column);
				if (cellData != null) {
					nullColumn = false;
				}
				colData.add(cellData);
			}
			if (!nullColumn) {
				model.addColumn(sourceModel.getColumnName(column),
						colData.toArray());
			}
		}
		return model;
	}
}
