package com.esevinale.myappportfolio.utils;

import java.util.Calendar;

public class Utils {
    public static String createGteDate() {
        int monthGte = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return primaryReleaseDateToString(monthGte, Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String createLteDate() {
        int monthLte = Calendar.getInstance().get(Calendar.MONTH) + 2;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (monthLte > 12)
            return primaryReleaseDateToString(1, year + 1);
        else
            return primaryReleaseDateToString(monthLte, year);
    }

    private static String primaryReleaseDateToString(int monthInt, int year) {
        String month = Integer.toString(monthInt);
        if (month.length() == 1)
            month = "0".concat(month);
        return Integer.toString(year) + "-" + month + "-01";
    }
}
