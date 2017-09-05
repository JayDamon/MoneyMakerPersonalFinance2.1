package com.moneymaker.utilities;

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

//    public static String cleanDate(String date) {
//        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM").withLocale(Locale.ENGLISH);
//
//        int year = -1;
//        int month = -1;
//        int day = -1;
//
//        String[] dateParts = null;
//
//        if (date.contains("/")) {
//            dateParts = date.split("/");
//        }
//        if (date.contains(".")) {
//            dateParts = date.split(".");
//        }
//        if (date.contains("-")) {
//            dateParts = date.split("-");
//        }
//        if (date.contains(",")) {
//            dateParts = date.split(",");
//        }
//
//        if (dateParts != null && dateParts.length == 3) {
//            if (dateParts[0].length() == 4) {
//                year = Integer.parseInt(dateParts[0]);
//                if (dateParts[1].length() == 3) {
//                    TemporalAccessor accessor = parser.parse(dateParts[1]);
//                    month = accessor.get(ChronoField.MONTH_OF_YEAR);
//                } else {
//                    month = Integer.parseInt(dateParts[1]);
//                }
//                month = month < 1 ? 1 : month;
//                month = month > 12 ? 12 : month;
//
////                if (dateParts[2].length() > 2) {
////                    day = Integer.parseInt(StringUtils.substring(dateParts[2], 0, 2));
////                } else {
//                day = Integer.parseInt(dateParts[2]);
////                }
//                day = day < 1 ? 1 : day;
//                day = day > 31 ? 12 : day;
//
//            } else if (dateParts[2].length() == 4) {
//                year = Integer.parseInt(dateParts[2]);
//                if (dateParts[1].length() == 3) {
//                    TemporalAccessor accessor = parser.parse(dateParts[1]);
//                    month = accessor.get(ChronoField.MONTH_OF_YEAR);
//                } else {
//                    month = Integer.parseInt(dateParts[1]);
//                }
//                month = month < 1 ? 1 : month;
//                month = month > 12 ? 12 : month;
//
////                if (dateParts[0].length() > 2) {
////                    day = Integer.parseInt(StringUtils.substring(dateParts[0], 0, 2));
////                } else {
//                day = Integer.parseInt(dateParts[0]);
////                }
//                day = day < 1 ? 1 : day;
//                day = day > 31 ? 12 : day;
//            }
//            if (day != -1 && year != -1) {
//                return year + "-" + month + "-" + day;
//            }
//        }
//        return "";
//    }

    public static String getCalendarDisplayDate(Calendar calendar) {
        if (calendar != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
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

//    public static String formatDate(String date)  {
//        Date initDate = null;
//        try {
//            initDate = new SimpleDateFormat("MM/dd/yyyy").parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//
//        return formatter.format(initDate);
//    }
//
//    public static String formatFromTableDate(String date) {
//        Date initDate = null;
//        try {
//            initDate = new SimpleDateFormat("dd-MMM-yy").parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//
//        return formatter.format(initDate);
//    }

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

    public static Calendar setCalDate(Calendar cal, int year, int month, int day) {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal;
    }

//    public static Calendar getCalBeginningOfDay() {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//
//        return cal;
//    }

}
