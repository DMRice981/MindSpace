package com.mindspace.app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DISPLAY_FORMAT = "MM月dd日 HH:mm";
    private static final String MONTH_DAY_FORMAT = "MM月dd日";

    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static String formatDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static String formatForDisplay(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static String formatMonthDay(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static long getStartOfDay(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfDay(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    public static long getStartOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return getStartOfDay(calendar.getTimeInMillis());
    }

    public static long getStartOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getStartOfDay(calendar.getTimeInMillis());
    }

    public static long getStartOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return getStartOfDay(calendar.getTimeInMillis());
    }

    public static long getDaysAgo(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        return getStartOfDay(calendar.getTimeInMillis());
    }

    public static String getRelativeTimeString(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 7) {
            return formatDate(timestamp);
        } else if (days > 0) {
            return days + "天前";
        } else if (hours > 0) {
            return hours + "小时前";
        } else if (minutes > 0) {
            return minutes + "分钟前";
        } else {
            return "刚刚";
        }
    }

    public static boolean isToday(long timestamp) {
        long todayStart = getStartOfDay(System.currentTimeMillis());
        long todayEnd = getEndOfDay(System.currentTimeMillis());
        return timestamp >= todayStart && timestamp <= todayEnd;
    }

    public static boolean isThisWeek(long timestamp) {
        long weekStart = getStartOfWeek();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(weekStart);
        calendar.add(Calendar.DAY_OF_WEEK, 7);
        long weekEnd = calendar.getTimeInMillis();
        return timestamp >= weekStart && timestamp < weekEnd;
    }

    public static boolean isThisMonth(long timestamp) {
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.setTimeInMillis(timestamp);
        return now.get(Calendar.YEAR) == target.get(Calendar.YEAR) 
            && now.get(Calendar.MONTH) == target.get(Calendar.MONTH);
    }
}
