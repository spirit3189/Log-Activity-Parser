package com.tibco.tools.activityparser.utils;

import java.awt.Color;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.tibco.tools.activityparser.LogActivityFormatter;
import com.tibco.tools.activityparser.models.Activity;
import com.tibco.tools.activityparser.models.LogPseudoTimeChange;

import de.jaret.util.date.JaretDate;

/**
 * A helper class.
 * 
 * @author Jagdeesh Karicherla
 */
public class LogHelper {

	public static Date parseDate(String timeStamp) {
		String[] timeStampFormats = { LogActivityFormatter.getInstance()
				.getLogActivityFormat().TimeStampFormat };
		Date parsedDate = null;
		try {
			parsedDate = DateUtils.parseDate(timeStamp, timeStampFormats);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parsedDate;
	}

	public static Date changeDate(Date oldDate, LogPseudoTimeChange timeChange)
			throws ParseException {
		Date newDate = null;
		if (timeChange == LogPseudoTimeChange.INCREASE)
			newDate = DateUtils.addMilliseconds(oldDate, 1);
		else
			newDate = DateUtils.addMilliseconds(oldDate, -1);
		return newDate;
	}

	public static JaretDate Util2JaretDate(Date utilDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(utilDate);
		JaretDate jaretDate = new JaretDate();
		jaretDate.setDateTime(cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.MONTH), cal.get(Calendar.YEAR),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND) , cal.get(Calendar.MILLISECOND));
		return jaretDate;
	}

	public static Color stringToColor(String colorName) {
		Color color = null;
		Field colorField;
		try {
			colorField = Color.class.getField(colorName);
			color = (Color) colorField.get(null);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return color;
	}

	public static String getColorName(Color colorParam) {
		try {
			Field[] field = Class.forName("java.awt.Color").getDeclaredFields();
			for (Field f : field) {
				String colorName = f.getName();
				Class<?> t = f.getType();
				if (t == java.awt.Color.class) {
					Color defined = (Color) f.get(null);
					if (defined.equals(colorParam)) {
						return colorName.toUpperCase();
					}
				}
			}
		} catch (Exception e) {
		}
		return "NO_MATCH";
	}

	public static void printLogActivity(Activity activity) {
		System.out.println("\n##### Activity start  ####");
		System.out.println("Thread Name : " + activity.getThreadName());
		System.out.println("Start Time: "
				+ activity.getStartEvent().getTimeStamp().toString());
		System.out.println("End Time: "
				+ activity.getEndEvent().getTimeStamp().toString());
		System.out.println("Start Message: "
				+ activity.getStartEvent().getMessage());
		System.out.println("End Message: "
				+ activity.getEndEvent().getMessage());
		System.out.println("Activity has error ?  "
				+ new Boolean(activity.getHasError()).toString());
		System.out.println("##### Activity end  ####\n");

	}

}
