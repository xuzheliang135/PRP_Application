package com.example.administrator.myapplication.util;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

public class Record implements Serializable {
    private long date;
    private String date_str;
    private LinkedList<Integer> points;

    public Record() {
        date = new Date().getTime();
        points = new LinkedList<Integer>();
    }

    synchronized void append_points(int value) {
        points.add(value);
    }

    String getDate_str() {
        return date_str;
    }

    long getDate() {
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

    synchronized LinkedList<Integer> getPoints() {
        return points;
    }
}