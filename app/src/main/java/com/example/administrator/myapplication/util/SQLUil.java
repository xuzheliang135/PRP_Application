package com.example.administrator.myapplication.util;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;


public class SQLUil {
    public SQLUil(Context context) {
        dbHelper = new DBHelper(context.getApplicationContext());
    }

    private DBHelper dbHelper;

    //向第二张表中添加数据（备用）
    public void insert_points(long date, int y, int position) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into insert_points(date,y,position) values(?,?,?)";
        Object[] obj = {date, y, position};
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
        Cursor c = db.rawQuery("select y,position from points where date=" + date, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                // 将点加入记录
                data.getPoints().add(c.getInt(c.getColumnIndex("position")), c.getInt(c.getColumnIndex("y")));
            }
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
                long Date = cursor.getLong(cursor.getColumnIndex("date"));
                record_list.add(Date);
            }
        }
        cursor.close();
        return record_list;
    }
    //从Record写入数据库
    void record(Record record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into points(date,y,position) values(?,?,?)";
        for (int j = 0; j < record.getPoints().size(); j++) {
            Object[] obj = {record.getDate(), record.getPoints().get(j), j};
            db.execSQL(sql, obj);
        }
//        db.close();
    }
}
