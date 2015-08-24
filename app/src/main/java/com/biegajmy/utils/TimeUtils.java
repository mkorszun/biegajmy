package com.biegajmy.utils;

import java.util.Date;
import org.joda.time.Interval;
import org.joda.time.Period;

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
        Interval interval = new Interval(date1.getTime(), date2.getTime());
        Period period = interval.toPeriod();

        int yy = period.getYears();
        int mm = period.getMonths();
        int dd = period.getDays();
        int hh = period.getHours();
        int min = period.getMinutes();
        int ss = period.getSeconds();

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
