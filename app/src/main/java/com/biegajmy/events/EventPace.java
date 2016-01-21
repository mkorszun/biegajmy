package com.biegajmy.events;

public class EventPace {

    private Double pace;
    private int minutes;
    private int seconds;

    public EventPace(double pace) {
        update(this.pace = pace);
    }

    public EventPace(int min, int sec) {
        update(this.minutes = min, this.seconds = sec);
    }

    public Double getPace() {
        return pace;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setPace(Double pace) {
        update(this.pace = pace);
    }

    public void setMinutes(int minutes) {
        update(this.minutes = minutes, seconds);
    }

    public void setSeconds(int seconds) {
        update(minutes, this.seconds = seconds);
    }

    @Override public String toString() {
        return (pace == null || pace == 0.0) ? null : String.format("%02d:%02d", minutes, seconds);
    }

    private void update(int mm, int ss) {
        this.pace = (double) mm + (double) ss / 60d;
    }

    private void update(double pace) {
        this.minutes = this.pace.intValue();
        this.seconds = (int) round(((pace - minutes) * 60), 0);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
