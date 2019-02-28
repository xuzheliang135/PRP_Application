package com.example.administrator.myapplication.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    //数据库的名字
    private static final String DB_NAME = "BeatData.db";
    private static final String CREATE_TBL1 = " create table if not exists" + " points(_id integer primary key autoincrement,date,y,position)";

    //创建数据库
    DBHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, 1);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL1);
    }

    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}

