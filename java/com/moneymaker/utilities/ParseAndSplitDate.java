package com.moneymaker.utilities;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Jay Damon on 9/18/2016.
 */
public class ParseAndSplitDate {

    private int formattedYear;
    private int formattedMonth;
    private int formattedDay;

    public static Calendar getDate(String inputDate) {
        Calendar cal = Calendar.getInstance();
        //ToDo not working properly
        String[] splitDate = getSplitDate(inputDate);
        int dateLength = splitDate.length;
        switch (dateLength) {
            case 2:
                cal = getMonthDay(splitDate, cal);
                break;
            case 3:
                int yearPosition = findYearPosition(splitDate);
                cal = getMonthDayYear(splitDate, yearPosition, cal);
        }
        return cal;
    }

    public ParseAndSplitDate(String inputDate) {
        int yearPosition;

        String[] dateSplit = getSplitDate(inputDate);

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        assert dateSplit != null;
        int dateLength = dateSplit.length;


        switch (dateLength) {
            case 2:
                formattedYear = thisYear;
                setFormattedYear(formattedYear);
                setMonthDay(dateSplit);
                break;
            case 3:
                yearPosition = findYearPosition(dateSplit);
                setMonthDayYear(dateSplit, yearPosition);
                break;
        }
    }

    private static String[] getSplitDate(String inputDate) {
        String[] dateSplit = null;
        if (inputDate.contains("/")) {
            dateSplit = inputDate.split("/");
        } else if (inputDate.contains("-")) {
            dateSplit = inputDate.split("-");
        } else if (inputDate.contains(".")) {
            dateSplit = inputDate.split(".");
        }
        return dateSplit;
    }

    private void setMonthDayYear(String[] dateSplit, int yearPosition) {
        int formattedMonth = 0;
        int formattedDay = 0;
        int formattedYear = 0;

        for (String s : dateSplit) {
            int currentPosition = Arrays.asList(dateSplit).indexOf(s);
            switch (yearPosition) {
                case 0:
                    switch (currentPosition) {
                        case 0:
                            formattedYear = Integer.parseInt(s);
                            break;
                        case 1:
                            formattedMonth = Integer.parseInt(s);
                            break;
                        case 2:
                            formattedDay= Integer.parseInt(s);
                            break;
                    }
                    break;
                case 2:
                    switch (currentPosition) {
                        case 0:
                            formattedMonth = Integer.parseInt(s);
                            break;
                        case 1:
                            formattedDay = Integer.parseInt(s);
                            break;
                        case 2:
                            formattedYear = Integer.parseInt(s);
                            break;
                    }
                    break;
            }
        }

        setFormattedYear(formattedYear);
        setFormattedDay(formattedDay);
        setFormattedMonth(formattedMonth);
    }

    private static Calendar getMonthDayYear(String[] dateSplit, int yearPosition, Calendar cal) {
        int formattedMonth = 0;
        int formattedDay = 0;
        int formattedYear = 0;

        for (String s : dateSplit) {
            int currentPosition = Arrays.asList(dateSplit).indexOf(s);
            switch (yearPosition) {
                case 0:
                    switch (currentPosition) {
                        case 0:
                            formattedYear = Integer.parseInt(s);
                            break;
                        case 1:
                            formattedMonth = Integer.parseInt(s);
                            break;
                        case 2:
                            formattedDay= Integer.parseInt(s);
                            break;
                    }
                    break;
                case 2:
                    switch (currentPosition) {
                        case 0:
                            formattedMonth = Integer.parseInt(s);
                            break;
                        case 1:
                            formattedDay = Integer.parseInt(s);
                            break;
                        case 2:
                            formattedYear = Integer.parseInt(s);
                            break;
                    }
                    break;
            }
        }
        cal.set(formattedYear, formattedMonth - 1, formattedDay);
        return cal;
    }

    private void setMonthDay(String[] dateSplit) {
        int formattedMonth = 0;
        int formattedDay = 0;

        for (String s : dateSplit) {
            int currentPosition = Arrays.asList(dateSplit).indexOf(s);
            switch (currentPosition) {
                case 0:
                    formattedMonth = Integer.parseInt(s);
                    break;
                case 1:
                    formattedDay = Integer.parseInt(s);
                    break;
            }

        }

        setFormattedMonth(formattedMonth);
        setFormattedDay(formattedDay);
    }

    private static Calendar getMonthDay(String[] dateSplit, Calendar cal) {
        int formattedMonth = 0;
        int formattedDay = 0;

        for (String s : dateSplit) {
            int currentPosition = Arrays.asList(dateSplit).indexOf(s);
            switch (currentPosition) {
                case 0:
                    formattedMonth = Integer.parseInt(s);
                    break;
                case 1:
                    formattedDay = Integer.parseInt(s);
                    break;
            }

        }
        cal.set(Calendar.getInstance().get(Calendar.YEAR),formattedMonth, formattedDay);
        return cal;
    }

    private static int findYearPosition(String[] dateSplit) {
        int i = 0;
        int yearPosition = 2;
        for (String s : dateSplit) {
            if (s.length() == 4) {
                yearPosition = i;
                break;
            }
            i++;
        }
        return yearPosition;
    }

    public int getFormattedYear() {
        return formattedYear;
    }

    private void setFormattedYear(int formattedYear) {
        this.formattedYear = formattedYear;
    }

    public int getFormattedMonth() {
        return formattedMonth;
    }

    private void setFormattedMonth(int formattedMonth) {
        this.formattedMonth = formattedMonth;
    }

    public int getFormattedDay() {
        return formattedDay;
    }

    private void setFormattedDay(int formattedDay) {
        this.formattedDay = formattedDay;
    }
}
