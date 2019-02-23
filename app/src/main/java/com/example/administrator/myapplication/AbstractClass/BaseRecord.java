package com.example.administrator.myapplication.AbstractClass;

import java.util.LinkedList;

public abstract class BaseRecord {
    private long date;
    private String date_str;
    private LinkedList<Integer> points;
    private int beat_rate;

    public String getDate_str() {
        return date_str;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setDate_str(String date_str) {
        this.date_str = date_str;
    }

    public void setPoints(LinkedList<Integer> points) {
        this.points = points;
    }

    public int getBeat_rate() {
        return beat_rate;
    }

    public void setBeat_rate(int beat_rate) {
        this.beat_rate = beat_rate;
    }

    public LinkedList<Integer> getPoints() {
        return points;
    }

    abstract public void append_point(int value);
}
