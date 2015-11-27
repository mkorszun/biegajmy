package com.biegajmy.utils;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;

public class TimeUtils {

    private static final String YEAR = "r temu";
    private static final String MONTH = "m-c temu";
    private static final String DAY = "dn temu";
    private static final String HOUR = "godz temu";
    private static final String MINUTE = "min temu";
    private static final String SECOND = "sec temu";
    private static final String NOW = "Teraz";

    public static class TimeDiff {

        public String unit;
        public int value;

        public TimeDiff(int value, String unit) {
            this.value = value;
            this.unit = unit;
        }

        @Override public String toString() {
            if (value > 0) {
                return String.format("%d%s", value, unit);
            } else {
                return NOW;
            }
        }
    }

    public static TimeDiff getDiff(Date date1, Date date2) {

        if (date1.after(date2)) return new TimeDiff(0, "");
        DateTime start1 = new DateTime(date1.getTime());
        DateTime start2 = new DateTime(date2.getTime());

        int yy = Years.yearsBetween(start1, start2).getYears();
        int mm = Months.monthsBetween(start1, start2).getMonths();
        int dd = Days.daysBetween(start1, start2).getDays();
        int hh = Hours.hoursBetween(start1, start2).getHours();
        int min = Minutes.minutesBetween(start1, start2).getMinutes();
        int ss = Seconds.secondsBetween(start1, start2).getSeconds();

        if (yy > 0) {
            return new TimeDiff(yy, YEAR);
        }

        if (mm > 0) {
            return new TimeDiff(mm, MONTH);
        }

        if (dd > 0) {
            return new TimeDiff(dd, DAY);
        }

        if (hh > 0) {
            return new TimeDiff(hh, HOUR);
        }

        if (min > 0) {
            return new TimeDiff(min, MINUTE);
        }

        if (ss > 0) {
            return new TimeDiff(min, SECOND);
        }

        return new TimeDiff(0, "");
    }
}
