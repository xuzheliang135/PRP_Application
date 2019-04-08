package com.example.administrator.myapplication.util;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import com.example.administrator.myapplication.Config;

import java.util.LinkedList;


public class SQLUtil {
    public SQLUtil(Context context) {
        dbHelper = new DBHelper(context.getApplicationContext());
    }

    private DBHelper dbHelper;

    //向第二张表中添加数据（备用）
    public void insert_points(long date, int ch, int y, int position) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into insert_points(date,ch,y,position) values(?,?,?,?)";
        Object[] obj = {date, ch, y, position};
        db.execSQL(sql, obj);
        db.close();
    }

    //删除第二张表中数据（备用）
    public void delete_points(String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "date=?";
        String[] whereArgs = new String[]{date};
        db.delete("points", whereClause, whereArgs);
        db.close();
    }

    //从数据库生成record对象,（在获取Record List时内部调用）
    public Record get_record(long date) {
        Record data = new Record();
        data.setDate(date);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        for (int i = 0; i < Config.totalChannelNumber; i++) {
            Cursor c = db.rawQuery("select y from points where date=" + date + " and ch=" + i + " order by position ASC", null);
            if (c.getCount() > 0)
                while (c.moveToNext())
                    data.getPoints(i).add(c.getInt(c.getColumnIndex("y")));// 将点加入记录
            c.close();
        }
        return data;
    }

    //获取Record list
    public LinkedList<Record> get_record_list() {
        LinkedList<Record> record_list = new LinkedList<Record>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("points", null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            while (cursor.moveToPrevious()) {
                long Date = cursor.getLong(cursor.getColumnIndex("date"));
                record_list.add(get_record(Date));
                while (!cursor.isFirst() && (Date == cursor.getLong(cursor.getColumnIndex("date")))) {
                    cursor.moveToPrevious();
                }
            }
        }
        cursor.close();
        return record_list;
    }

    public LinkedList<Long> get_date_list() {
        LinkedList<Long> record_list = new LinkedList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct date from points order by date desc", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                long date = cursor.getLong(cursor.getColumnIndex("date"));
                record_list.add(date);
            }
        }
        cursor.close();
        return record_list;
    }

    //从Record写入数据库
    void record(Record record) {
        long date = record.getDate();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into points(date,ch,y,position) values(?,?,?,?)";
        SQLiteStatement stat = db.compileStatement(sql);
        db.beginTransaction();
        for (int k = 0; k < Config.channelNumber; k++) {
            LinkedList<Integer> points = record.getPoints(k);
            int position = 0;
            for (int value : points) {
                stat.bindLong(1, date);
                stat.bindLong(2, k);
                stat.bindLong(3, value);
                stat.bindLong(4, position++);
                if (stat.executeInsert() < 0) Log.d("my_debug", "save error!");
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        Log.d("my_debug", "save completed!");
    }
}
