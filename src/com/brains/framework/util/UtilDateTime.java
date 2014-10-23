package com.brains.framework.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Utility class for handling java.util.Date, the java.sql data/time classes and related
 */
public class UtilDateTime {
    public static final String[] months = {// // to be translated over CommonMonthName, see example in accounting
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November",
        "December"
    };

    public static final String[] days = {// to be translated over CommonDayName, see example in accounting
        "Monday", "Tuesday", "Wednesday",
        "Thursday", "Friday", "Saturday", "Sunday"
    };

    public static final String[][] timevals = {
        {"1000", "millisecond"},
        {"60", "second"},
        {"60", "minute"},
        {"24", "hour"},
        {"168", "week"}
    };

    public static final DecimalFormat df = new DecimalFormat("0.00;-0.00");
    /**
     * JDBC escape format for java.sql.Date conversions.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * JDBC escape format for java.sql.Timestamp conversions.
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_TIME_FORMAT_NOSEC = "yyyy-MM-dd HH:mm";
    /**
     * JDBC escape format for java.sql.Time conversions.
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static double getInterval(Date from, Date thru) {
        return thru != null ? thru.getTime() - from.getTime() : 0;
    }

    public static int getIntervalInDays(Timestamp from, Timestamp thru) {
        return thru != null ? (int) ((thru.getTime() - from.getTime()) / (24*60*60*1000)) : 0;
    }

    public static Timestamp addDaysToTimestamp(Timestamp start, int days) {
        return new Timestamp(start.getTime() + (24L*60L*60L*1000L*days));
    }

    public static Timestamp addDaysToTimestamp(Timestamp start, Double days) {
        return new Timestamp(start.getTime() + ((int) (24L*60L*60L*1000L*days)));
    }

    public static double getInterval(Timestamp from, Timestamp thru) {
        return thru != null ? thru.getTime() - from.getTime() + (thru.getNanos() - from.getNanos()) / 1000000 : 0;
    }


    /**
     * Return a Timestamp for right now
     *
     * @return Timestamp for right now
     */
    public static java.sql.Timestamp nowTimestamp() {
        return getTimestamp(System.currentTimeMillis());
    }

    /**
     * Convert a millisecond value to a Timestamp.
     * @param time millsecond value
     * @return Timestamp
     */
    public static java.sql.Timestamp getTimestamp(long time) {
        return new java.sql.Timestamp(time);
    }

    /**
     * Convert a millisecond value to a Timestamp.
     * @param milliSecs millsecond value
     * @return Timestamp
     */
    public static Timestamp getTimestamp(String milliSecs) throws NumberFormatException {
        return new Timestamp(Long.parseLong(milliSecs));
    }

    /**
     * Returns currentTimeMillis as String
     *
     * @return String(currentTimeMillis)
     */
    public static String nowAsString() {
        return Long.toString(System.currentTimeMillis());
    }

    /**
     * Return a string formatted as yyyyMMddHHmmss
     *
     * @return String formatted for right now
     */
    public static String nowDateString() {
        return nowDateString("yyyy-MM-dd hh:mm:ss");
    }
    
    public static String nowDateStringDetail() {
        return nowDateString("yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * Return a string formatted as format
     *
     * @return String formatted for right now
     */
    public static String nowDateString(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * Return a Date for right now
     *
     * @return Date for right now
     */
    public static java.util.Date nowDate() {
        return new java.util.Date();
    }


    /**
     * Return the date for the first day of the year
     *
     * @param stamp
     * @return java.sql.Timestamp
     */
    public static java.sql.Timestamp getYearStart(java.sql.Timestamp stamp) {
        return getYearStart(stamp, 0, 0, 0);
    }

    public static java.sql.Timestamp getYearStart(java.sql.Timestamp stamp, int daysLater) {
        return getYearStart(stamp, daysLater, 0, 0);
    }

    public static java.sql.Timestamp getYearStart(java.sql.Timestamp stamp, int daysLater, int yearsLater) {
        return getYearStart(stamp, daysLater, 0, yearsLater);
    }
    public static java.sql.Timestamp getYearStart(java.sql.Timestamp stamp, Number daysLater, Number monthsLater, Number yearsLater) {
        return getYearStart(stamp, (daysLater == null ? 0 : daysLater.intValue()),
                (monthsLater == null ? 0 : monthsLater.intValue()), (yearsLater == null ? 0 : yearsLater.intValue()));
    }

    public static Calendar toCalendar(java.sql.Timestamp stamp) {
        Calendar cal = Calendar.getInstance();
        if (stamp != null) {
            cal.setTimeInMillis(stamp.getTime());
        }
        return cal;
    }
    
    /**
     *
     * @param date The date String: YYYY-MM-DD
     * @return
     */
    public static Calendar toCalendar(String date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
        	 DateFormat df = DateFormat.getDateInstance();
        	 Date date1;
			try {
				date1 = df.parse(date);
	            cal.setTime(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return cal;
    }

    /**
     * Converts a date String into a java.sql.Date
     *
     * @param date The date String: MM/DD/YYYY
     * @return A java.sql.Date made from the date String
     */
    public static java.sql.Date toSqlDate(String date) {
        java.util.Date newDate = toDate(date, "00:00:00");

        if (newDate != null) {
            return new java.sql.Date(newDate.getTime());
        } else {
            return null;
        }
    }
    
    public static java.sql.Date toSqlDate(Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime());
        } else {
            return null;
        }
    }    

    /**
     * Makes a java.sql.Date from separate Strings for month, day, year
     *
     * @param monthStr The month String
     * @param dayStr   The day String
     * @param yearStr  The year String
     * @return A java.sql.Date made from separate Strings for month, day, year
     */
    public static java.sql.Date toSqlDate(String monthStr, String dayStr, String yearStr) {
        java.util.Date newDate = toDate(monthStr, dayStr, yearStr, "0", "0", "0");

        if (newDate != null) {
            return new java.sql.Date(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * Makes a java.sql.Date from separate ints for month, day, year
     *
     * @param month The month int
     * @param day   The day int
     * @param year  The year int
     * @return A java.sql.Date made from separate ints for month, day, year
     */
    public static java.sql.Date toSqlDate(int month, int day, int year) {
        java.util.Date newDate = toDate(month, day, year, 0, 0, 0);

        if (newDate != null) {
            return new java.sql.Date(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * Converts a time String into a java.sql.Time
     *
     * @param time The time String: either HH:MM or HH:MM:SS
     * @return A java.sql.Time made from the time String
     */
    public static java.sql.Time toSqlTime(String time) {
        java.util.Date newDate = toDate("1/1/1970", time);

        if (newDate != null) {
            return new java.sql.Time(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * Makes a java.sql.Time from separate Strings for hour, minute, and second.
     *
     * @param hourStr   The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A java.sql.Time made from separate Strings for hour, minute, and second.
     */
    public static java.sql.Time toSqlTime(String hourStr, String minuteStr, String secondStr) {
        java.util.Date newDate = toDate("0", "0", "0", hourStr, minuteStr, secondStr);

        if (newDate != null) {
            return new java.sql.Time(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * Makes a java.sql.Time from separate ints for hour, minute, and second.
     *
     * @param hour   The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A java.sql.Time made from separate ints for hour, minute, and second.
     */
    public static java.sql.Time toSqlTime(int hour, int minute, int second) {
        java.util.Date newDate = toDate(0, 0, 0, hour, minute, second);

        if (newDate != null) {
            return new java.sql.Time(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * Converts a date and time String into a Timestamp
     *
     * @param dateTime A combined data and time string in the format "MM/DD/YYYY HH:MM:SS", the seconds are optional
     * @return The corresponding Timestamp
     */
    public static java.sql.Timestamp toTimestamp(String dateTime) {
        java.util.Date newDate = toDate(dateTime);

        if (newDate != null) {
            return new java.sql.Timestamp(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * Converts a date String and a time String into a Timestamp
     *
     * @param date The date String: MM/DD/YYYY
     * @param time The time String: either HH:MM or HH:MM:SS
     * @return A Timestamp made from the date and time Strings
     */
    public static java.sql.Timestamp toTimestamp(String date, String time) {
        java.util.Date newDate = toDate(date, time);

        if (newDate != null) {
            return new java.sql.Timestamp(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * Makes a Timestamp from separate Strings for month, day, year, hour, minute, and second.
     *
     * @param monthStr  The month String
     * @param dayStr    The day String
     * @param yearStr   The year String
     * @param hourStr   The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A Timestamp made from separate Strings for month, day, year, hour, minute, and second.
     */
    public static java.sql.Timestamp toTimestamp(String monthStr, String dayStr, String yearStr, String hourStr,
            String minuteStr, String secondStr) {
        java.util.Date newDate = toDate(monthStr, dayStr, yearStr, hourStr, minuteStr, secondStr);

        if (newDate != null) {
            return new java.sql.Timestamp(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * Makes a Timestamp from separate ints for month, day, year, hour, minute, and second.
     *
     * @param month  The month int
     * @param day    The day int
     * @param year   The year int
     * @param hour   The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A Timestamp made from separate ints for month, day, year, hour, minute, and second.
     */
    public static java.sql.Timestamp toTimestamp(int month, int day, int year, int hour, int minute, int second) {
        java.util.Date newDate = toDate(month, day, year, hour, minute, second);

        if (newDate != null) {
            return new java.sql.Timestamp(newDate.getTime());
        } else {
            return null;
        }
    }

    public static java.sql.Timestamp toTimestamp(Date date) {
        if (date == null) return null;
        return new Timestamp(date.getTime());
    }

    /**
     * Converts a date and time String into a Date
     *
     * @param dateTime A combined data and time string in the format "MM/DD/YYYY HH:MM:SS", the seconds are optional
     * @return The corresponding Date
     */
    public static java.util.Date toDate(String dateTime) {
        if (dateTime == null||dateTime.trim().length()==0) {
            return null;
        }
        dateTime = dateTime.trim();
        if(dateTime.length()<=10){
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	try {
				return sdf.parse(dateTime);
			} catch (ParseException e) {
			}
        }else{
	        // dateTime must have one space between the date and time...
	        String date = dateTime.substring(0, dateTime.indexOf(" "));
	        String time = dateTime.substring(dateTime.indexOf(" ") + 1);
	
	        return toDate(date, time);
        }
        return null;
    }
    
    public static java.sql.Timestamp toFormatStringTimestamp(String dateTime){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			return toTimestamp(sdf.parse(dateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }

    /**
     * Converts a date String and a time String into a Date
     *
     * @param date The date String: MM/DD/YYYY
     * @param time The time String: either HH:MM or HH:MM:SS
     * @return A Date made from the date and time Strings
     */
    public static java.util.Date toDate(String date, String time) {
        if (date == null || time == null) return null;
        String month;
        String day;
        String year;
        String hour;
        String minute;
        String second;

        int dateSlash1 = date.indexOf("/");
        int dateSlash2 = date.lastIndexOf("/");

        if (dateSlash1 <= 0 || dateSlash1 == dateSlash2) return null;
        int timeColon1 = time.indexOf(":");
        int timeColon2 = time.lastIndexOf(":");

        if (timeColon1 <= 0) return null;
        month = date.substring(0, dateSlash1);
        day = date.substring(dateSlash1 + 1, dateSlash2);
        year = date.substring(dateSlash2 + 1);
        hour = time.substring(0, timeColon1);

        if (timeColon1 == timeColon2) {
            minute = time.substring(timeColon1 + 1);
            second = "0";
        } else {
            minute = time.substring(timeColon1 + 1, timeColon2);
            second = time.substring(timeColon2 + 1);
        }

        return toDate(month, day, year, hour, minute, second);
    }

    /**
     * Makes a Date from separate Strings for month, day, year, hour, minute, and second.
     *
     * @param monthStr  The month String
     * @param dayStr    The day String
     * @param yearStr   The year String
     * @param hourStr   The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A Date made from separate Strings for month, day, year, hour, minute, and second.
     */
    public static java.util.Date toDate(String monthStr, String dayStr, String yearStr, String hourStr,
            String minuteStr, String secondStr) {
        int month, day, year, hour, minute, second;

        try {
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
            year = Integer.parseInt(yearStr);
            hour = Integer.parseInt(hourStr);
            minute = Integer.parseInt(minuteStr);
            second = Integer.parseInt(secondStr);
        } catch (Exception e) {
            return null;
        }
        return toDate(month, day, year, hour, minute, second);
    }

    /**
     * Makes a Date from separate ints for month, day, year, hour, minute, and second.
     *
     * @param month  The month int
     * @param day    The day int
     * @param year   The year int
     * @param hour   The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A Date made from separate ints for month, day, year, hour, minute, and second.
     */
    public static java.util.Date toDate(int month, int day, int year, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.set(year, month - 1, day, hour, minute, second);
            calendar.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            return null;
        }
        return new java.util.Date(calendar.getTime().getTime());
    }

    /**
     * Makes a date String in the given from a Date
     *
     * @param date The Date
     * @return A date String in the given format
     */
    public static String toDateString(java.util.Date date, String format) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = null;
        if (format != null) {
            dateFormat = new SimpleDateFormat(format);
        } else {
            dateFormat = new SimpleDateFormat();
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        return dateFormat.format(date);
    }

    /**
     * Makes a date String in the format MM/DD/YYYY from a Date
     *
     * @param date The Date
     * @return A date String in the format MM/DD/YYYY
     */
    public static String toDateString(java.util.Date date) {
        return toDateString(date, "MM/dd/yyyy");
    }

    /**
     * Makes a time String in the format HH:MM:SS from a Date. If the seconds are 0, then the output is in HH:MM.
     *
     * @param date The Date
     * @return A time String in the format HH:MM:SS or HH:MM
     */
    public static String toTimeString(java.util.Date date) {
        if (date == null) return "";
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        return (toTimeString(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
    }

    /**
     * Makes a time String in the format HH:MM:SS from a separate ints for hour, minute, and second. If the seconds are 0, then the output is in HH:MM.
     *
     * @param hour   The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A time String in the format HH:MM:SS or HH:MM
     */
    public static String toTimeString(int hour, int minute, int second) {
        String hourStr;
        String minuteStr;
        String secondStr;

        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = "" + minute;
        }
        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = "" + second;
        }
        if (second == 0) {
            return hourStr + ":" + minuteStr;
        } else {
            return hourStr + ":" + minuteStr + ":" + secondStr;
        }
    }

    /**
     * Makes a combined data and time string in the format "MM/DD/YYYY HH:MM:SS" from a Date. If the seconds are 0 they are left off.
     *
     * @param date The Date
     * @return A combined data and time string in the format "MM/DD/YYYY HH:MM:SS" where the seconds are left off if they are 0.
     */
    public static String toDateTimeString(java.util.Date date) {
        if (date == null) {
        	return "";
        }
        String dateString = toDateString(date);
        String timeString = toTimeString(date);

        if (dateString != null && timeString != null) {
            return dateString + " " + timeString;
        } else {
            return "";
        }
    }

    public static String toGmtTimestampString(Timestamp timestamp) {
        DateFormat df = DateFormat.getDateTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(timestamp);
    }


    /**
     * Makes a Timestamp for the beginning of the month
     *
     * @return A Timestamp of the beginning of the month
     */
    public static java.sql.Timestamp monthBegin() {
        Calendar mth = Calendar.getInstance();

        mth.set(Calendar.DAY_OF_MONTH, 1);
        mth.set(Calendar.HOUR_OF_DAY, 0);
        mth.set(Calendar.MINUTE, 0);
        mth.set(Calendar.SECOND, 0);
        mth.set(Calendar.MILLISECOND, 0);
        mth.set(Calendar.AM_PM, Calendar.AM);
        return new java.sql.Timestamp(mth.getTime().getTime());
    }


    public static int weekNumber(Timestamp input, int startOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(startOfWeek);

        if (startOfWeek == Calendar.MONDAY) {
           calendar.setMinimalDaysInFirstWeek(4);
        } else if (startOfWeek == Calendar.SUNDAY) {
           calendar.setMinimalDaysInFirstWeek(3);
        }

        calendar.setTime(new java.util.Date(input.getTime()));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    // ----- New methods that take a timezone and locale -- //


    /**
     * Returns a List of day name Strings - suitable for calendar headings.
     * @param locale
     * @return List of day name Strings
     */
    public static List<String> getDayNames(Locale locale) {
        Calendar tempCal = Calendar.getInstance(locale);
        tempCal.set(Calendar.DAY_OF_WEEK, tempCal.getFirstDayOfWeek());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", locale);
        List<String> resultList = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            resultList.add(dateFormat.format(tempCal.getTime()));
            tempCal.roll(Calendar.DAY_OF_WEEK, 1);
        }
        return resultList;
    }

    /**
     * Returns a List of month name Strings - suitable for calendar headings.
     *
     * @param locale
     * @return List of month name Strings
     */
    public static List<String> getMonthNames(Locale locale) {
        Calendar tempCal = Calendar.getInstance(locale);
        tempCal.set(Calendar.MONTH, Calendar.JANUARY);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", locale);
        List<String> resultList = new ArrayList<String>();
        for (int i = Calendar.JANUARY; i <= tempCal.getActualMaximum(Calendar.MONTH); i++) {
            resultList.add(dateFormat.format(tempCal.getTime()));
            tempCal.roll(Calendar.MONTH, 1);
        }
        return resultList;
    }

    /**
     * Returns an initialized DateFormat object.
     *
     * @param dateFormat
     *            optional format string
     * @param tz
     * @param locale
     *            can be null if dateFormat is not null
     * @return DateFormat object
     */
    public static DateFormat toDateFormat(String dateFormat, TimeZone tz, Locale locale) {
        DateFormat df = null;
        if (dateFormat == null) {
            df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        } else {
            df = new SimpleDateFormat(dateFormat);
        }
        df.setTimeZone(tz);
        return df;
    }

    /**
     * Returns an initialized DateFormat object.
     * @param dateTimeFormat optional format string
     * @param tz
     * @param locale can be null if dateTimeFormat is not null
     * @return DateFormat object
     */
    public static DateFormat toDateTimeFormat(String dateTimeFormat, TimeZone tz, Locale locale) {
        DateFormat df = null;
        if (dateTimeFormat == null) {
            df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
        } else {
            df = new SimpleDateFormat(dateTimeFormat);
        }
        df.setTimeZone(tz);
        return df;
    }

    /**
     * Returns an initialized DateFormat object.
     * @param timeFormat optional format string
     * @param tz
     * @param locale can be null if timeFormat is not null
     * @return DateFormat object
     */
    public static DateFormat toTimeFormat(String timeFormat, TimeZone tz, Locale locale) {
        DateFormat df = null;
        if (timeFormat == null) {
            df = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
        } else {
            df = new SimpleDateFormat(timeFormat);
        }
        df.setTimeZone(tz);
        return df;
    }

    /**
     * Localized String to Timestamp conversion. To be used in tandem with timeStampToString().
     */
    public static Timestamp stringToTimeStamp(String dateTimeString, TimeZone tz, Locale locale) throws ParseException {
        return stringToTimeStamp(dateTimeString, null, tz, locale);
    }

    /**
     * Localized String to Timestamp conversion. To be used in tandem with timeStampToString().
     */
    public static Timestamp stringToTimeStamp(String dateTimeString, String dateTimeFormat, TimeZone tz, Locale locale) throws ParseException {
        DateFormat dateFormat = toDateTimeFormat(dateTimeFormat, tz, locale);
        Date parsedDate = dateFormat.parse(dateTimeString);
        return new Timestamp(parsedDate.getTime());
    }

    /**
     * Localized Timestamp to String conversion. To be used in tandem with stringToTimeStamp().
     */
    public static String timeStampToString(Timestamp stamp, TimeZone tz, Locale locale) {
        return timeStampToString(stamp, null, tz, locale);
    }

    /**
     * Localized Timestamp to String conversion. To be used in tandem with stringToTimeStamp().
     */
    public static String timeStampToString(Timestamp stamp, String dateTimeFormat, TimeZone tz, Locale locale) {
        DateFormat dateFormat = toDateTimeFormat(dateTimeFormat, tz, locale);
        dateFormat.setTimeZone(tz);
        return dateFormat.format(stamp);
    }

    /** Returns a TimeZone object based upon a time zone ID. Method defaults to
     * server's time zone if tzID is null or empty.
     * @see java.util.TimeZone
     */
    public static TimeZone toTimeZone(String tzId) {
        if (UtilValidate.isEmpty(tzId)) {
            return TimeZone.getDefault();
        } else {
            return TimeZone.getTimeZone(tzId);
        }
    }

    /** Returns a TimeZone object based upon an hour offset from GMT.
     * @see java.util.TimeZone
     */
    public static TimeZone toTimeZone(int gmtOffset) {
        if (gmtOffset > 12 || gmtOffset < -14) {
            throw new IllegalArgumentException("Invalid GMT offset");
        }
        String tzId = gmtOffset > 0 ? "Etc/GMT+" : "Etc/GMT";
        return TimeZone.getTimeZone(tzId + gmtOffset);
    }

    
    
    public static List<String> getMonthAmongTwoDate(String dateStart,String dateEnd){
    	  DateFormat df = DateFormat.getDateInstance();
    	  List<String> list = new ArrayList<String>();
    	  Date date1 = null; // 开始日期
    	  Date date2 = null; // 结束日期
    	  try {
    	   date1 = df.parse(dateStart+"-01");
    	   date2 = df.parse(dateEnd+"-01");
    	  } catch (ParseException e) {
    	   e.printStackTrace();
    	  }

    	  Calendar c1 = Calendar.getInstance();
    	  Calendar c2 = Calendar.getInstance();
    	  //添加第一个月，即开始时间
    	  list.add(dateStart);
    	  c1.setTime(date1);
    	  c2.setTime(date2);
    	  while (c1.compareTo(c2) <= 0) {
    	   c1.add(Calendar.MONTH, 1);// 开始日期加一个月直到等于结束日期为止
    	   Date ss = c1.getTime();
    	   String str = df.format(ss);
    	   str = str.substring(0, str.lastIndexOf("-"));
    	   list.add(str);
    	  }
    	  return list;
    }
    
    
}

