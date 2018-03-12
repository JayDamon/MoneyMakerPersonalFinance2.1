package com.moneymaker.utilities;

import javafx.collections.ObservableList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created for MoneyMaker by Jay Damon on 8/27/2016.
 */
public class DateUtility {

    public static final String CALENDAR_DISPLAY_DATE = "dd-MMM-yy";
    public static final String SQL_INPUT_DATE = "yyyy-MM-dd";

    public static String getCalendarDisplayDate(Calendar calendar) {
        if (calendar != null) {
//            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
            SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy");

            return format.format(calendar.getTime());
        } else {
            return "";
        }
    }

    public static java.sql.Date getSQLDate(Calendar calendar) {
        if (calendar != null) {
            return new java.sql.Date(calendar.getTimeInMillis());
        } else {
            return null;
        }
    }

    public static Calendar getCalDateFromSQL(java.sql.Date date) {
        Calendar cal = DateUtility.getCalBeginningOfDay();
        if (date != null) {
            cal.setTime(date);
            DateUtility.setCalBeginningOfDay(cal);
            return cal;
        } else {
            return null;
        }
    }

    public static Calendar parseStringCalendar(String date) {

        Calendar calendarDate = Calendar.getInstance();
        Date dateForCalendar = stringToDate(date);
        calendarDate.setTime(dateForCalendar);

        return calendarDate;
    }

    private static Date stringToDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date convertDate = new Date();
        try {
            convertDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertDate;
    }

    public static Calendar getWeekDayStartDate(Calendar startDate, String occurrence) {
        switch (occurrence) {
            case "Monday":
                startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case "Tuesday":
                startDate.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case "Wednesday":
                startDate.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case "Thursday":
                startDate.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case "Friday":
                startDate.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case "Saturday":
                startDate.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
            case "Sunday":
                startDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
        }
        return startDate;
    }

    public static Calendar getCalBeginningOfDay() {
        Calendar cal = Calendar.getInstance();
        setCalBeginningOfDay(cal);
        return cal;
    }

    public static void setCalBeginningOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public static void setCalDate(Calendar cal, int year, int month, int day) {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
    }
}
