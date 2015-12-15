package com.biegajmy.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventDateTime {

    private static final Locale PL = new Locale("pl", "PL");
    private static final SimpleDateFormat DT_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    private static final SimpleDateFormat D_FORMAT = new SimpleDateFormat("EEEE dd/MM/yyyy", PL);

    private Calendar calendar;

    private EventTime time;

    private EventDate date;

    public EventDateTime() {
        this.calendar = Calendar.getInstance();
        this.time = new EventTime(this.calendar);
        this.date = new EventDate(this.calendar);
    }

    public EventTime getTime() {
        return time;
    }

    public EventDate getDate() {
        return date;
    }

    public void setTime(EventTime time) {
        this.time = time;
    }

    public void setDate(EventDate date) {
        this.date = date;
    }

    public void set(String str) {
        try {
            calendar.setTime(DT_FORMAT.parse(str));
        } catch (ParseException e) {
        }
    }

    public void set(long timestamp) {
        calendar.setTime(new Date(timestamp));
    }

    public long getTimestamp() {
        return calendar.getTime().getTime();
    }

    @Override public String toString() {
        return String.format("%s %s", this.date, this.time);
    }

    public static class EventTime {

        private Calendar calendar;

        public EventTime(Calendar calendar) {
            this.calendar = calendar;
        }

        public int getHour() {
            return this.calendar.get(Calendar.HOUR_OF_DAY);
        }

        public int getMinute() {
            return this.calendar.get(Calendar.MINUTE);
        }

        public void setHour(int hh) {
            this.calendar.set(Calendar.HOUR_OF_DAY, hh);
        }

        public void setMinute(int mm) {
            this.calendar.set(Calendar.MINUTE, mm);
        }

        public void set(String time) {
            try {
                int hh = Integer.parseInt(time.split(":")[0]);
                int mm = Integer.parseInt(time.split(":")[1]);
                this.calendar.set(Calendar.HOUR_OF_DAY, hh);
                this.calendar.set(Calendar.MINUTE, mm);
            } catch (Exception e) {
            }
        }

        @Override public String toString() {
            return String.format("%02d:%02d", getHour(), getMinute());
        }
    }

    public static class EventDate {

        private Calendar calendar;

        public EventDate(Calendar calendar) {
            this.calendar = calendar;
        }

        public int getYear() {
            return this.calendar.get(Calendar.YEAR);
        }

        public int getMonth() {
            return this.calendar.get(Calendar.MONTH) + 1;
        }

        public int getDay() {
            return this.calendar.get(Calendar.DAY_OF_MONTH);
        }

        public void setYear(int yy) {
            this.calendar.set(Calendar.YEAR, yy);
        }

        public void setMonth(int mm) {
            this.calendar.set(Calendar.MONTH, mm - 1);
        }

        public void setDay(int dd) {
            this.calendar.set(Calendar.DAY_OF_MONTH, dd);
        }

        public void set(String date) {
            try {
                String[] split = date.split(":");
                int dd = Integer.parseInt(split[0]);
                int mm = Integer.parseInt(split[1]);
                int yy = Integer.parseInt(split[2]);
                this.calendar.set(Calendar.DAY_OF_MONTH, dd);
                this.calendar.set(Calendar.MONTH, mm);
                this.calendar.set(Calendar.YEAR, yy);
            } catch (Exception e) {
            }
        }

        @Override public String toString() {
            return String.format("%02d/%02d/%04d", getDay(), getMonth(), getYear());
        }

        public String toLongString() {
            return D_FORMAT.format(calendar.getTime()).toUpperCase();
        }
    }
}


