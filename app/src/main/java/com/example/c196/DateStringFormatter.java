package com.example.c196;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStringFormatter {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;

    public static String formatDateForText(int year, int month, int day) {
        String formattedDate = new StringBuilder().append(formatMonth(month)).append(" ").append(day).append(" ").append(year).toString();
        return formattedDate;
    }

    public static Date formatDateForDatabase(int year, int month, int day) {
        Date formattedDate;
        try {
            formattedDate = dateFormat.parse(new StringBuilder().append(year).append('-').append(String.format("%02d", month+1)).append('-').append(day).toString());
        } catch (Exception e) {
            e.printStackTrace();
            formattedDate = new Date();
        }
        return formattedDate;
    }

    private static String formatMonth(int month) {
        String m = "";
        switch (month) {
            case 0: m = "Jan"; break;
            case 1: m = "Feb"; break;
            case 2: m = "Mar"; break;
            case 3: m = "Apr"; break;
            case 4: m = "May"; break;
            case 5: m = "Jun"; break;
            case 6: m = "Jul"; break;
            case 7: m = "Aug"; break;
            case 8: m = "Sep"; break;
            case 9: m = "Oct"; break;
            case 10: m = "Nov"; break;
            case 11: m = "Dec"; break;
        }
        return m;
    }
}
