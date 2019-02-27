package com.example.administrator.myapplication.util;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Record implements Serializable {
    long date;
    private String date_str;
    LinkedList<Integer> points;
    int beat_rate;

    public Record() {
        date = new Date().getTime();
        points = new LinkedList<Integer>();
    }

    public void append_points(int value) {
        points.add(value);
    }

    void save(SQLHelper.DBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into beat_rate(date,beat_rate) values(?,?)";
        Object obj[] = {date, beat_rate};
        db.execSQL(sql, obj);
        String sql2 = "insert into points(date,y,position) values(?,?,?)";
        for (int j = 0; j <= points.size(); j++) {
            Object obj2[] = {date, points.get(j), j};
            db.execSQL(sql2, obj2);
        }
        db.close();
    }

    static List<Record> get_record_list(SQLHelper.SQLUil sqlUil) {
        List<Record> record_list = new LinkedList<Record>();
        SQLiteDatabase db = sqlUil.dbHelper.getReadableDatabase();

        Cursor cursor = db.query("beat_rate", null, null, null, null, null, null);//
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // 拿到每一行date的数值
                record_list.add(sqlUil.get_record(cursor.getLong(cursor.getColumnIndex("date"))));
            }
            cursor.close();
        }
        return record_list;
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

    public int getBeat_rate() {
        return beat_rate;
    }

    public void setBeat_rate(int beat_rate) {
        this.beat_rate = beat_rate;
    }

    public LinkedList<Integer> getPoints() {
        return points;
    }
}