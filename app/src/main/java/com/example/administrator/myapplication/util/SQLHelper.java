package com.example.administrator.myapplication.util;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class SQLHelper {
    public class DBHelper extends SQLiteOpenHelper {

        //数据库的名字
        private static final String DB_NAME = "BeatData.db";
        //创建两张表
        private static final String TBL1_NAME = "beat_rate";
        private static final String TBL2_NAME = "points";

        private static final String CREATE_TBL1 = " create table if not exists " + " beat_rate(_id integer primary key autoincrement,date,beat_rate)";
        private static final String CREATE_TBL2 = " create table if not exists" + " points(_id integer primary key autoincrement,date,y,position)";


        public DBHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TBL1);
            db.execSQL(CREATE_TBL2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }

    public class SQLUil {
        public DBHelper dbHelper;

        public void insert_beat_rate(long date, int beat_rate) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "insert into beat_rate(date,beat_rate) values(?,?)";
            Object obj[] = {date, beat_rate};
            db.execSQL(sql, obj);
            db.close();
        }


        //删除第一张表中数据
        public void delete_beat_rate(String date) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String whereClause = "date=?";
            String[] whereArgs = new String[]{date};
            db.delete(dbHelper.TBL1_NAME, whereClause, whereArgs);
            db.close();
        }


        //向第二张表中添加数据
        public void insert_points(long date, int y, int position) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "insert into insert_points(date,y,position) values(?,?,?)";
            Object obj[] = {date, y, position};
            db.execSQL(sql, obj);
            db.close();
        }


        //删除第二张表中数据
        public void delete_points(String date) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String whereClause = "date=?";
            String[] whereArgs = new String[]{date};
            db.delete(dbHelper.TBL2_NAME, whereClause, whereArgs);
            db.close();
        }


        //从数据库生成record对象
        public Record get_record(long Date) {
            Record data = new Record();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("select beat_rate from points where date=Date", null);
            if (c.getCount() > 0) {
                data.beat_rate = c.getInt(2);
                data.date = Date;
                c.close();
            }
            Cursor d = db.rawQuery("select y,postion from points where date=Date", null);
            if (d.getCount() > 0) {
                while (d.moveToNext()) {
                    // 拿到每一行date的数值
                    data.points.add(d.getInt(d.getColumnIndex("position")), d.getInt(d.getColumnIndex("y")));
                    d.close();
                }
            }
            return data;
        }


        //从Record写入数据库
        void record(Record record) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "insert into beat_rate(date,beat_rate) values(?,?)";
            Object obj[] = {record.date, record.beat_rate};
            db.execSQL(sql, obj);
            String sql2 = "insert into points(date,y,position) values(?,?,?)";
            for (int j = 0; j <= record.points.size(); j++) {
                Object obj2[] = {record.date, record.points.get(j), j};
                db.execSQL(sql2, obj2);
            }
            db.close();
        }


        //获取Record list
        public List<Record> get_record_list() {
            List<Record> record_list = new LinkedList<Record>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query("beat_rate", null, null, null, null, null, null);//
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // 拿到每一行date的数值
                    record_list.add(get_record(cursor.getLong(cursor.getColumnIndex("date"))));
                }
                cursor.close();
            }
            return record_list;
        }
    }
}