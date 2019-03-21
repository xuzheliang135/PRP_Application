package com.example.administrator.myapplication.util;


import com.example.administrator.myapplication.Config;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class Record implements Serializable {
    private long date;
    private String date_str;
    private LinkedList<LinkedList<Integer>> pointsList;
    static private DateFormat dateFormat = new SimpleDateFormat("MM/dd hh:mm:ss", Locale.getDefault());

    Record() {
        Date nowDate = new Date();
        date = nowDate.getTime();
        date_str = dateFormat.format(nowDate);
        //四通道数据
        for (int i = 0; i < Config.channelNumber; i++) {
            pointsList.add(new LinkedList<Integer>());
        }
    }

    //输入通道序号存入点
    synchronized void append_points(int ch, int value) {
        pointsList.get(ch).add(value);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        date_str = format(date);
    }

    public void setPoints(int ch, LinkedList<Integer> points) {
        pointsList.set(ch, points);
    }

    public String getDate_str() {
        return date_str;
    }

    static public String format(long date) {
        return dateFormat.format(new Date(date));

    }

    synchronized LinkedList<Integer> getPoints(int ch) {
        return pointsList.get(ch);
    }
}