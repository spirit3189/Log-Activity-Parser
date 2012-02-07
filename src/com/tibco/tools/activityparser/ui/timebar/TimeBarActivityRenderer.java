/*
 *  File: EventRenderer.java 
 *  Copyright (c) 2004-2008  Peter Kliem (Peter.Kliem@jaret.de)
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import de.jaret.util.date.Interval;
import de.jaret.util.swing.GraphicsHelper;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.TimeBarRenderer;

/**
 * Renderer for time bar events.
 * 
 * @author : Jagdeesh Karicherla
 * @main author Peter Kliem
 */
public class TimeBarActivityRenderer implements TimeBarRenderer {
	EventRendererComponent _eventComponent;

	public TimeBarActivityRenderer() {
		_eventComponent = new EventRendererComponent();
	}

	public JComponent getTimeBarRendererComponent(TimeBarViewer tbv,
			Interval value, boolean isSelected, boolean overlapping) {
		if (value instanceof TimeBarInterval) {
			_eventComponent.setEventInterval((TimeBarInterval) value);
			_eventComponent.setSelected(isSelected);
			return _eventComponent;
		} else {
			throw new RuntimeException("unsupported "
					+ value.getClass().getName());
		}
	}

	public class EventRendererComponent extends JComponent {
		TimeBarInterval _interval;
		boolean _selected;
		boolean _exception;

		public EventRendererComponent() {
			setLayout(null);
			setOpaque(false);
		}

		public void setEventInterval(TimeBarInterval interval) {
			_interval = interval;
		}

		public String getToolTipText() {
			return "<html><b>" + _interval.getTitle() + "</b></html>";
		}

		public void setSelected(boolean selected) {
			_selected = selected;
		}

		public void setException(boolean exception) {
			_exception = exception;
		}

		public boolean hasException() {
			return _exception;
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int height = getHeight();
			int width = getWidth();

			int y = height / 3;
			int bheight = height / 3;
			int yEnd = y + bheight;
			if (_selected) {
				g.setColor(Color.BLUE);
			} else if (_interval.getException()) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.GREEN);
			}
			g.fillRect(0, y, width - 1, height / 3);

			g.setColor(Color.BLACK);
			g.drawRect(0, y, width - 1, height / 3);
			// g.draw

			GraphicsHelper.drawStringCenteredVCenter(g, _interval.getTitle(),
					0, width, height / 2);
		}

		public boolean contains(int x, int y) {
			if (y >= getHeight() / 3 && y <= getHeight() / 3 + getHeight() / 3) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * {@inheritDoc} Simple default implementation.
	 */
	public Rectangle getPreferredDrawingBounds(Rectangle intervalDrawingArea,
			TimeBarViewerDelegate delegate, Interval interval,
			boolean selected, boolean overlap) {
		return intervalDrawingArea;
	}

}
