package com.tibco.tools.activityparser.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.tibco.tools.activityparser.LogActivityFormatter;
import com.tibco.tools.activityparser.models.LogTableCell;
import com.tibco.tools.activityparser.models.LogTableCellType;

/**
 * Cell renderer class used by the all the jtables in the app.
 * 
 * @author Jagdeesh Karicherla
 */
public class CustomCellRenderer extends DefaultTableCellRenderer {

	public CustomCellRenderer() {

	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		String _formattedValue;
		if (value == null)
			_formattedValue = "-";
		else if (value instanceof Date) {
			_formattedValue = new SimpleDateFormat(LogActivityFormatter
					.getInstance().getLogActivityFormat().TimeStampFormat)
					.format((Date) value);

		} else if (value instanceof LogTableCell) {
			LogTableCell cellValue = (LogTableCell) value;
			JLabel cellComponent = cellLabelWrapper(cellValue);
			return cellComponent;
		}

		else if (value instanceof List<?>) {
			JScrollPane jScrollPane = new JScrollPane();
			JPanel cellPanel = new JPanel();
			cellPanel.setLayout(new BoxLayout(cellPanel, BoxLayout.Y_AXIS));
			List<LogTableCell> cellValues = (List<LogTableCell>) value;
			for (LogTableCell cellValue : cellValues) {
				JPanel cellContainer = new JPanel();
				cellContainer.setBorder(BorderFactory
						.createLineBorder(Color.black));
				JLabel cellComponent = cellLabelWrapper(cellValue);
				cellContainer.setBackground(cellComponent.getBackground());
				cellContainer.add(cellComponent);
				cellPanel.add(cellContainer);
			}
			// jScrollPane.add(cellPanel);
			jScrollPane.setViewportView(cellPanel);
			int rowHeight = table.getRowHeight();
			int scrollPaneHeight = jScrollPane.getPreferredSize().height
					+ cellValues.size() * 4;
			rowHeight = Math.max(rowHeight, scrollPaneHeight);
			table.setRowHeight(row, rowHeight);
			return jScrollPane;

		} else {
			_formattedValue = value.toString();
		}

		if (value == null) {
			JLabel cellComponent = new JLabel("-", SwingConstants.CENTER);
			// cellComponent.setBackground(LogActivityFormatter.getInstance()
			// .getLogActivityFormat().NormalColor);
			// cellComponent.setOpaque(true);
			return cellComponent;

		}
		JLabel defaultCell = new JLabel(_formattedValue, SwingConstants.CENTER);
		if (isSelected) {
			defaultCell.setBackground(Color.YELLOW);
			defaultCell.setOpaque(true);
			defaultCell.setForeground(table.getSelectionForeground());
		}
		if (hasFocus) {
			defaultCell.setForeground(table.getSelectionBackground());
			defaultCell.setBackground(table.getSelectionForeground());
			defaultCell.setOpaque(true);
		}
		return defaultCell;
	}

	private JLabel cellLabelWrapper(LogTableCell cellValue) {
		JLabel cellComponent = new JLabel(cellValue.getCellValue(),
				SwingConstants.CENTER);
		LogTableCellType cellType = cellValue.getType();
		switch (cellType) {
		case NORMAL:
			// cellComponent.setText("");
			// cellComponent.setBackground(LogActivityFormatter.getInstance()
			// .getLogActivityFormat().NormalColor);
			// cellComponent.setOpaque(true);
			// colors get added for normal if user wants to have.
			break;
		case HIGHLIGHT:
			cellComponent.setBackground(LogActivityFormatter.getInstance()
					.getLogActivityFormat().HightLightColor);
			cellComponent.setOpaque(true);
			break;
		case ACTIVITY:
			cellComponent.setBackground(LogActivityFormatter.getInstance()
					.getLogActivityFormat().NormalColor);
			cellComponent.setOpaque(true);
			break;
		case ERROR:
			cellComponent.setBackground(LogActivityFormatter.getInstance()
					.getLogActivityFormat().ErrorColor);
			cellComponent.setOpaque(true);
			break;
		}
		cellComponent.setToolTipText(cellValue.getToolTip());
		cellComponent.setAlignmentX(Component.CENTER_ALIGNMENT);
		return cellComponent;
	}
}
