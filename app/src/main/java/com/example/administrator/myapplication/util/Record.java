package com.example.administrator.myapplication.util;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class Record implements Serializable {
    private long date;
    private String date_str;
    private LinkedList<Integer> points;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd hh:mm:ss", Locale.getDefault());

    Record() {
        Date nowDate = new Date();
        date = nowDate.getTime();
        date_str = dateFormat.format(nowDate);
        points = new LinkedList<Integer>();
    }

    synchronized void append_points(int value) {
        points.add(value);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        date_str = dateFormat.format(new Date(date));
    }

    public void setPoints(LinkedList<Integer> points) {
        this.points = points;
    }

    public String getDate_str() {
        return date_str;
    }

    synchronized LinkedList<Integer> getPoints() {
        return points;
    }
}