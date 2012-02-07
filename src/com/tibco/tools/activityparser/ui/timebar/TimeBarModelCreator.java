/*
 *  File: ModelCreator.java 
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

import java.util.Calendar;

import com.tibco.tools.activityparser.models.Activity;
import com.tibco.tools.activityparser.models.LogActivityBuilder;
import com.tibco.tools.activityparser.models.ParseResultModel;
import com.tibco.tools.activityparser.utils.LogHelper;

import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.model.DefaultRowHeader;
import de.jaret.util.ui.timebars.model.DefaultTimeBarModel;
import de.jaret.util.ui.timebars.model.TimeBarModel;

/**
 * Simple model creator creating a model for timebar.
 * events.
 * 
 * @author kliem
 */
public class TimeBarModelCreator {

	public static TimeBarModel createTimeBarFlatModel(ParseResultModel initModel) {
		DefaultTimeBarModel model = new DefaultTimeBarModel();
		for (Object keyMap : initModel.getActivitiesGroupedByThread().keySet()) {
			String threadName = keyMap.toString();
			LogActivityBuilder activityBuilder = initModel
					.getActivitiesGroupedByThread().get(keyMap);
			DefaultRowHeader header = new DefaultRowHeader(threadName);
			TimeBarRowForActivity row = new TimeBarRowForActivity(header);
			for (int i = 0; i < activityBuilder.getActivityList().size(); i++) {
				{
					Activity activity = activityBuilder.getActivityList()
							.get(i);

					JaretDate startDate = LogHelper.Util2JaretDate(activity
							.getStartEvent().getTimeStamp());
					JaretDate endDate = LogHelper.Util2JaretDate(activity
							.getEndEvent().getTimeStamp());
					if (activity.getStartEvent().getTimeStamp()
							.compareTo(activity.getEndEvent().getTimeStamp()) == 0) {
						if (activity.isException()) {
							System.out.println(threadName);
						}
						System.out.println(threadName);
					}
					Calendar startCal = Calendar.getInstance();
					startCal.setTime(activity.getStartEvent().getTimeStamp());
					Calendar endCal = Calendar.getInstance();
					endCal.setTime(activity.getEndEvent().getTimeStamp());
					TimeBarInterval interval = new TimeBarInterval(
							startDate.copy(), endDate.copy());
					if (activity.isException()) {
						interval.setException(true);
						System.out.println("has Exception");
					}
					interval.setTitle(" ");
					row.addInterval(interval);
				}

			}
			model.addRow(row);
		}

		return model;

	}

}
